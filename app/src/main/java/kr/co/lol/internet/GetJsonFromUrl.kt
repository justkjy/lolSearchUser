package kr.co.lol.internet

import android.util.Log
import androidx.room.ColumnInfo
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.co.lol.dataclass.ChampionUnitData
import kr.co.lol.room.data.InfoVersion
import kr.co.lol.room.data.LolChampInfoEntity
import kr.co.lol.room.data.LolVersionEntity
import kr.co.lol.room.data.RoomHelper
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import javax.net.ssl.HttpsURLConnection


val TAG = "GETURL"
var lolVersion = "13.18.1"

enum class GetChampType { VERSION, ALLCHAPINFO, DETAILCHAMPINFO }

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
    var champAllInfoUrl = "https://ddragon.leagueoflegends.com/cdn/#version#/data/ko_KR/champion.json"
    var champDetailUrl = "https://ddragon.leagueoflegends.com/cdn/#version#/data/ko_KR/champion/#champname#.json"

    try {
        var urlValue = versionUrl
        var getVersionJson = getJsonFileFromHttps(urlValue)

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
            val champItemList:MutableMap<String, String> = mutableMapOf<String, String>()
            for(item in list) {
                val urlChampItem = champDetailUrl.replace("#version#", newVersion.version)
                    .replace("#champname#", item)
                val getJson = getJsonFileFromHttps(urlChampItem)

                if(getJson.isEmpty()) {
                    state(getJsonFailed("개별 챔프 획득 실패"))
                    return@launch
                }
                patchDetailState("챔피언 : $item")
                champItemList.put(item, getJson)
            }
            // 챔프 등록
            if(championUnitInfo(db, champItemList)) {
                complateLolPath(db, InfoVersion(newVersion.no, newVersion.version, true))
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
    val urlConnection = url.openConnection() as HttpsURLConnection
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

    val champInfoData : MutableList<LolChampInfoEntity> = mutableListOf<LolChampInfoEntity>()

    champInfo.forEach() {championName, championJsonString ->
        // val targetChampName = championName 실제 코드 적용시
        val targetChampName = championName

        var jsonSubBody = championJsonString


//        val removeItemMap = mapOf<String, String>("\"stats\":{" to "}],"
//            //"\"spells\":[{" to "}],"
//        )
//
//        removeItemMap.forEach { removeData, endData ->
//            val startPoint = jsonSubBody.indexOf(removeData, 0, false)
//            val endPoint = jsonSubBody.indexOf(endData, startPoint + removeData.length, false)
//            if(startPoint > -1 && startPoint < endPoint) {
//                jsonSubBody = jsonSubBody.removeRange(startPoint, endPoint + endData.length)
//            } else {
//                println("error")
//            }
//        }

        jsonSubBody = jsonSubBody.replace("{\"" + targetChampName,
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

        val spellsQ_id = championData.Main.spells[0].id
        val spellsQ_Description = championData.Main.spells[0].description
        val spellsQ_Name = championData.Main.spells[0].name
        val spellsQ_Image = championData.Main.spells[0].image.full
        val spellsQ_tooltip = championData.Main.spells[0].tooltip

        val spellsW_id = championData.Main.spells[1].id
        val spellsW_Description = championData.Main.spells[1].description
        val spellsW_Name = championData.Main.spells[1].name
        val spellsW_Image = championData.Main.spells[1].image.full
        val spellsW_tooltip = championData.Main.spells[1].tooltip

        val spellsE_id = championData.Main.spells[2].id
        val spellsE_Description = championData.Main.spells[2].description
        val spellsE_Name = championData.Main.spells[2].name
        val spellsE_Image = championData.Main.spells[2].image.full
        val spellsE_tooltip = championData.Main.spells[2].tooltip

        val spellsR_id = championData.Main.spells[3].id
        val spellsR_Description = championData.Main.spells[3].description
        val spellsR_Name = championData.Main.spells[3].name
        val spellsR_Image = championData.Main.spells[3].image.full
        val spellsR_tooltip = championData.Main.spells[3].tooltip





        var strTags = ""
        for(item in championData.Main.tags) {
            strTags += item + "\n"
        }

        strTags = strTags.removeRange(strTags.length-1, strTags.length)

        for(item in championSkinData) {
            championSkinList.put(item.num,item.name)
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

            spellsQid = spellsQ_id,
            spellsQDescription = spellsQ_Description,
            spellsQName = spellsQ_Name,
            spellsQImage = spellsQ_Image,
            spellsQtooltip = spellsQ_tooltip,

            spellsWid = spellsW_id,
            spellsWDescription = spellsW_Description,
            spellsWName = spellsW_Name,
            spellsWImage = spellsW_Image,
            spellsWtooltip = spellsW_tooltip,

            spellsEid = spellsE_id,
            spellsEDescription = spellsE_Description,
            spellsEName = spellsE_Name,
            spellsEImage = spellsE_Image,
            spellsEtooltip = spellsE_tooltip,

            spellsRid = spellsR_id,
            spellsRDescription = spellsR_Description,
            spellsRName = spellsR_Name,
            spellsRImage = spellsR_Image,
            spellsRtooltip = spellsR_tooltip
        )

        champInfoData.add(championDetail)
    }

    db.roomMemoDao().insertChampAll(champInfoData)
    return true
}

// 쳄프 개별 등록 완료 버전 정보에 패치 완료 업데이트 등록
fun complateLolPath(db:RoomHelper, infoVersion : InfoVersion) {
    val lolInfoDb = db.roomMemoDao()
    if(infoVersion.no == -1)
        lolInfoDb.insertVersion(LolVersionEntity( version = infoVersion.version, update =true))
    else
        lolInfoDb.patchVersionState(LolVersionEntity(infoVersion.no, infoVersion.version, true))
}



