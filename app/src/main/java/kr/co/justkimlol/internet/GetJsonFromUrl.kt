package kr.co.justkimlol.internet

import android.util.Log
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kr.co.justkimlol.dataclass.ChampionUnitData
import kr.co.justkimlol.room.data.InfoVersion
import kr.co.justkimlol.room.data.LolChampInfoEntity
import kr.co.justkimlol.room.data.LolVersionEntity
import kr.co.justkimlol.room.data.RoomHelper
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import javax.net.ssl.HttpsURLConnection

const val TAG = "GETURL"
var lolVersion = "13.18.1"

sealed class GetJsonFromUrl {
    data object Loading : GetJsonFromUrl()
    class Success(val msg:String) : GetJsonFromUrl()
    class Failed(val error:String) : GetJsonFromUrl()
}

typealias getJsonLoad  = GetJsonFromUrl.Loading
typealias getJsonSuccess = GetJsonFromUrl.Success
typealias getJsonFailed = GetJsonFromUrl.Failed

fun getConnectUrl(db: RoomHelper,
                  state: (GetJsonFromUrl)-> (Unit),
                  patchDetailState: (String) -> (Unit)
) = CoroutineScope(Dispatchers.IO).launch {

    val versionUrl = "https://ddragon.leagueoflegends.com/api/versions.json"
    val champAllInfoUrl = "https://ddragon.leagueoflegends.com/cdn/#version#/data/ko_KR/champion.json"
    val champDetailUrl = "https://ddragon.leagueoflegends.com/cdn/#version#/data/ko_KR/champion/#champname#.json"

    try {
        var urlValue = versionUrl
        val getVersionJson = getJsonFileFromHttps(urlValue)

        if(getVersionJson.isEmpty()) {
            state(getJsonFailed("버전 정보 획득 실패"))
            return@launch
        }

        patchDetailState("버전 정보 체크")
        val newVersion = dbProcess(db, getVersionJson)
        lolVersion = newVersion.version
        patchDetailState("현재 버전 : ${newVersion.version}")
        if(newVersion.runPatch) {
            patchDetailState("패치 진행 : $newVersion")
            val lolInfoDb = db.roomMemoDao()
            lolInfoDb.deleteChampInfo()
            // 최신 버전의 챔프 목록 가져 오기
            urlValue = champAllInfoUrl.replace("#version#", newVersion.version)

            patchDetailState("챔피언 목록 확인")
            val getJson = getJsonFileFromHttps(urlValue)

            if(getJson.isEmpty()) {
                state(getJsonFailed("챔피언 풀 데이터 획득 실패"))
                return@launch
            }
            // 챔피언 목록 가져오기
            val list = jsonRead(getJson)
            // 개별 챔피언 정보 획득
            // key = 챔프 이름 , value = json파일
            val champItemList:MutableMap<String, String> = mutableMapOf()
            for(item in list) {
                val urlChampItem = champDetailUrl.replace("#version#", newVersion.version)
                    .replace("#champname#", item)
                val jsonData = getJsonFileFromHttps(urlChampItem)

                if(jsonData.isEmpty()) {
                    state(getJsonFailed("개별 챔프 획득 실패"))
                    return@launch
                }
                patchDetailState("챔피언 : $item")
                champItemList[item] = jsonData
            }
            // 챔프 등록
            if(championUnitInfo(db, champItemList)) {
                completeLolPath(db, InfoVersion(newVersion.no, newVersion.version, true))
            }
            patchDetailState("패치 완료")
        }
        state(getJsonSuccess("패치 완료"))
    } catch (e: Exception) {
        Log.i(TAG, "err4 = ${e.printStackTrace()}")
        patchDetailState("패치 실패" )
        state(getJsonFailed("패치 실패"))
    }
}

// Http 연결 후 json 파일 획득
suspend fun getJsonFileFromHttps(urlValue: String) : String {
    val url = URL(urlValue)
    val urlConnection = withContext(Dispatchers.IO) {
        url.openConnection()
    } as HttpsURLConnection
    urlConnection.requestMethod = "GET"

    return if (urlConnection.responseCode == HttpURLConnection.HTTP_OK) {
        val streamReader = InputStreamReader(urlConnection.inputStream)
        val buffered = BufferedReader(streamReader)
        val content = StringBuffer()
        while (true) {
            val line = buffered.readLine() ?: break
            content.append(line)
        }
        urlConnection.disconnect()
        buffered.close()
        content.toString()
    } else {
        Log.i(TAG, "connect fail")
        ""
    }
}

// 버전 처리 /////////////////////////////////////////////////////////////////////////////////////////////////////
data class VersionData(
    val element: String
)

// return true 롤 패치 진행해야함 new version != old version
//        false 롤 패치 안함     new version == old version
suspend fun dbProcess(db:RoomHelper, getVersionJson : String) : InfoVersion {

    val lolInfoDb = db.roomMemoDao()

    var dataVersion = getVersionJson
    dataVersion =  dataVersion.replace("\"", "")
        .replace("[", "")
        .replace("]", "")
    val list = dataVersion.split(",")
    val getVersion = list.get(0)


    var versionList = ArrayList(lolInfoDb.getVersionAll())

    if(versionList.isEmpty()) {
        return InfoVersion(no = -1, version = getVersion, runPatch = true)
    }

    val topVersion = versionList.first()

    val runPatch = when(topVersion.version) {
        getVersion -> {
            !topVersion.update // false면 아직 패치 못한거임 다시 하자 true면 패치가 끝났음 패치 하지 말자.
        }
        else -> true
    }

    return InfoVersion(no = topVersion.no?.let{it} ?: -1, version = getVersion, runPatch = runPatch)
}

// 챔피언 목록 처리
suspend fun jsonRead(jsonBody: String) : List<String>{
    var jsonBody = jsonBody
    val championList = mutableListOf<String>()
    var endPoint = 0
    val findKey = "\"id\":\""

    while(true) {
        val start = jsonBody.indexOf(findKey, endPoint, true)
        if(start == -1)
            break
        val end = jsonBody.indexOf("\",", start, true)

        //val name ="\"" +jsonBody.substring(start+findKey.length, end) +"\":"
        val name =jsonBody.substring(start+findKey.length, end)
        if(end == -1)
            break
        endPoint = end+1
        championList.add(name)
    }

    return championList
}

// 챔피언 개별 정보 등록 수정
// 챔피언 개별 정보
fun championUnitInfo(db: RoomHelper, champInfo: MutableMap<String, String>) : Boolean {

    val champInfoData : MutableList<LolChampInfoEntity> = mutableListOf()

    champInfo.forEach() {championName, championJsonString ->
        // val targetChampName = championName 실제 코드 적용시
        var jsonSubBody = championJsonString

        jsonSubBody = jsonSubBody.replace(
            "{\"$championName",
            "{\"" + "Main"
        )

        val champion = Gson().fromJson(jsonSubBody, ChampionUnitData::class.java)

        val championData = champion.data
        val championEngName = championData.Main.id
        val championKorName = championData.Main.name
        val championTitle = championData.Main.title
        val championKeyName = championData.Main.key
        val championStory = championData.Main.lore
        val championSkinData = championData.Main.skins
        val championSkinList = mutableMapOf<Int, String>()

        val passiveName = championData.Main.passive.name
        val passiveDescription = championData.Main.passive.description
        val passiveImage = championData.Main.passive.image.full

        val spellsQId = championData.Main.spells[0].id
        val spellsQDescription = championData.Main.spells[0].description
        val spellsQName = championData.Main.spells[0].name
        val spellsQImage = championData.Main.spells[0].image.full
        val spellsQToolTip = championData.Main.spells[0].tooltip

        val spellsWId = championData.Main.spells[1].id
        val spellsWDescription = championData.Main.spells[1].description
        val spellsWName = championData.Main.spells[1].name
        val spellsWImage = championData.Main.spells[1].image.full
        val spellsWToolTip = championData.Main.spells[1].tooltip

        val spellsEId = championData.Main.spells[2].id
        val spellsEDescription = championData.Main.spells[2].description
        val spellsEName = championData.Main.spells[2].name
        val spellsEImage = championData.Main.spells[2].image.full
        val spellsEToolTip = championData.Main.spells[2].tooltip

        val spellsRId = championData.Main.spells[3].id
        val spellsRDescription = championData.Main.spells[3].description
        val spellsRName = championData.Main.spells[3].name
        val spellsRImage = championData.Main.spells[3].image.full
        val spellsRToolTip = championData.Main.spells[3].tooltip

        var strTags = ""
        for(item in championData.Main.tags) {
            strTags += item + "\n"
        }

        strTags = strTags.removeRange(strTags.length-1, strTags.length)

        for(item in championSkinData) {
            championSkinList[item.num] = item.name
        }

        val skinData = String.let {
            var skinInfo = ""
            for(item in championSkinList) {
                skinInfo += String.format("[${item.key},${item.value}]")
                skinInfo += "\n"
            }
            skinInfo = skinInfo.removeRange(skinInfo.length-1, skinInfo.length)
            skinInfo
        }

        val championDetail = LolChampInfoEntity(
            champKeyId= championKeyName.toInt(),
            nameEng = championEngName,
            nameKor = championKorName,
            title = championTitle,
            story = championStory,
            tagList = strTags,
            skinList = skinData,

            passiveName = passiveName,
            passiveDescription = passiveDescription,
            passiveImage = passiveImage,

            spellsQid = spellsQId,
            spellsQDescription = spellsQDescription,
            spellsQName = spellsQName,
            spellsQImage = spellsQImage,
            spellsQtooltip = spellsQToolTip,

            spellsWid = spellsWId,
            spellsWDescription = spellsWDescription,
            spellsWName = spellsWName,
            spellsWImage = spellsWImage,
            spellsWtooltip = spellsWToolTip,

            spellsEid = spellsEId,
            spellsEDescription = spellsEDescription,
            spellsEName = spellsEName,
            spellsEImage = spellsEImage,
            spellsEtooltip = spellsEToolTip,

            spellsRid = spellsRId,
            spellsRDescription = spellsRDescription,
            spellsRName = spellsRName,
            spellsRImage = spellsRImage,
            spellsRtooltip = spellsRToolTip
        )

        champInfoData.add(championDetail)
    }
    db.roomMemoDao().insertChampAll(champInfoData)
    return true
}

// 쳄프 개별 등록 완료 버전 정보에 패치 완료 업데이트 등록
fun completeLolPath(db:RoomHelper, infoVersion : InfoVersion) {
    val lolInfoDb = db.roomMemoDao()
    if(infoVersion.no == -1)
        lolInfoDb.insertVersion(LolVersionEntity( version = infoVersion.version, update =true))
    else
        lolInfoDb.patchVersionState(LolVersionEntity(infoVersion.no, infoVersion.version, true))
}
