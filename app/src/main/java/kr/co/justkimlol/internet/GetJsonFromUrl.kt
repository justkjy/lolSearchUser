package kr.co.justkimlol.internet

import android.util.Log
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.co.justkimlol.dataclass.ChampionUnitData
import kr.co.justkimlol.room.data.InfoVersion
import kr.co.justkimlol.room.data.LolChampInfoEntity
import kr.co.justkimlol.room.data.LolVersionEntity
import kr.co.justkimlol.room.data.RoomHelper
import java.io.FileNotFoundException
import java.net.URL

const val TAG = "GETURL"
var lolVersion = "13.18.1"

sealed class GetJsonFromUrl {
    data object Loading : GetJsonFromUrl()
    class Success(val msg: String) : GetJsonFromUrl()
    class Failed(val error: String) : GetJsonFromUrl()
}

typealias getJsonLoad = GetJsonFromUrl.Loading
typealias getJsonSuccess = GetJsonFromUrl.Success
typealias getJsonFailed = GetJsonFromUrl.Failed

fun getConnectUrl(
    db: RoomHelper,
    state: (GetJsonFromUrl) -> (Unit),
    patchDetailState: (String) -> (Unit)
) = CoroutineScope(Dispatchers.IO).launch {

    val versionUrl = "https://ddragon.leagueoflegends.com/api/versions.json"
    val champAllInfoUrl =
        "https://ddragon.leagueoflegends.com/cdn/#version#/data/ko_KR/champion.json"
    val champDetailUrl =
        "https://ddragon.leagueoflegends.com/cdn/#version#/data/ko_KR/champion/#champname#.json"

    val runPatchCheck: Boolean
    val newVersionNo: Int
    var getJsonChampData = ""

    // 버전 정보
    try {
        val getVersionJson = getJsonFileFromHttps(versionUrl)

        if (getVersionJson.isEmpty()) {
            state(getJsonFailed("버전 정보 획득 실패"))
            return@launch
        } else {
            patchDetailState("버전 정보 체크")
            val newVersion = dbProcess(db, getVersionJson)
            lolVersion = newVersion.version
            newVersionNo = newVersion.no
            runPatchCheck = newVersion.runPatch
            patchDetailState("현재 버전 : ${newVersion.version}")
        }
    } catch (e: FileNotFoundException) {
        Log.i(TAG, "version connect Error = ${e.printStackTrace()}")
        patchDetailState("버전 정보 확인 실패 : 서버에 접속 실패")
        state(getJsonFailed("패치 실패"))
        return@launch
    }

    // 챔피언 목록 패치
    if (runPatchCheck) {
        try {
            patchDetailState("패치 진행 : $lolVersion")
            val lolInfoDb = db.roomMemoDao()
            lolInfoDb.deleteChampInfo()
            // 최신 버전의 챔프 목록 가져 오기
            val urlValue = champAllInfoUrl.replace("#version#", lolVersion)

            patchDetailState("챔피언 목록 확인")
            getJsonChampData = getJsonFileFromHttps(urlValue)

            if (getJsonChampData.isEmpty()) {
                state(getJsonFailed("챔피언 데이터 획득 실패"))
                return@launch
            }
        } catch (e: FileNotFoundException) {
            Log.i(TAG, "챔피언 리스트 획득 실패 = ${e.printStackTrace()}")
            patchDetailState("챔피언 정보 획득 실패 : 서버 연결 실패")
            state(getJsonFailed("패치 실패"))
            return@launch
        }
    } else {
        state(getJsonSuccess("패치 완료 : 패치 내용 없음"))
        return@launch
    }

    // 챔피언 개별 디테일 정보 획득
    try {
        // 챔피언 목록 가져오기
        val list = jsonRead(getJsonChampData)
        // 개별 챔피언 정보 획득
        // key = 챔프 이름 , value = json파일
        val champItemList: MutableMap<String, String> = mutableMapOf()
        for (item in list) {
            val urlChampItem = champDetailUrl.replace("#version#", lolVersion)
                .replace("#champname#", item)
            val jsonData = getJsonFileFromHttps(urlChampItem)

            if (jsonData.isEmpty()) {
                state(getJsonFailed("개별 챔프 획득 실패"))
                return@launch
            }
            patchDetailState("챔피언 : $item")
            champItemList[item] = jsonData
        }
        // 챔프 DB 등록
        if (championUnitInfo(db, champItemList)) {
            completeLolPath(db, InfoVersion(newVersionNo, lolVersion, true))
        }
        patchDetailState("패치 완료")
        state(getJsonSuccess("패치 완료"))
    } catch (e: NumberFormatException) {
        Log.i(TAG, "JsonSyntaxExceptione = ${e.printStackTrace()}")
        patchDetailState("개별 챔피언 정보 확인 실패 : 내부적 데이터 구성이 잘못 되었거나 서버의 데이터가 변경되었습니다.")
        state(getJsonFailed("패치 실패"))

    } catch (e: FileNotFoundException) {
        Log.i(TAG, " 챔피언 정보 확인 = ${e.printStackTrace()}")
        patchDetailState("개별 챔피언 정보 확인 실패 : 서버 연결 실패")
        state(getJsonFailed("패치 실패"))
    } catch (e: Exception) {
        Log.i(TAG, "champion detail Info Error = ${e.printStackTrace()}")
        state(getJsonFailed("개별 챔프 획득 실패"))
    }
}

// Http 연결 후 json 파일 획득
fun getJsonFileFromHttps(urlValue: String): String {
    return URL(urlValue).readText()
}

// return true 롤 패치 진행해야함 new version != old version
//        false 롤 패치 안함     new version == old version
fun dbProcess(db: RoomHelper, getVersionJson: String): InfoVersion {

    val lolInfoDb = db.roomMemoDao()

    var dataVersion = getVersionJson
    dataVersion = dataVersion.replace("\"", "")
        .replace("[", "")
        .replace("]", "")
    val list = dataVersion.split(",")
    val getVersion = list.get(0)


    val versionList = ArrayList(lolInfoDb.getVersionAll())

    if (versionList.isEmpty()) {
        return InfoVersion(no = -1, version = getVersion, runPatch = true)
    }

    val topVersion = versionList.first()

    val runPatch = when (topVersion.version) {
        getVersion -> {
            !topVersion.update // false면 아직 패치 못한거임 다시 하자 true면 패치가 끝났음 패치 하지 말자.
        }

        else -> true
    }
    return InfoVersion(no = topVersion.no ?: -1, version = getVersion, runPatch = runPatch)
}

// 챔피언 목록 처리
fun jsonRead(jsonBody: String): List<String> {
    val championList = mutableListOf<String>()
    var endPoint = 0
    val findKey = "\"id\":\""

    while (true) {
        val start = jsonBody.indexOf(findKey, endPoint, true)
        if (start == -1)
            break
        val end = jsonBody.indexOf("\",", start, true)
        val name = jsonBody.substring(start + findKey.length, end)
        if (end == -1)
            break
        endPoint = end + 1
        championList.add(name)
    }

    return championList
}

// 챔피언 개별 정보 등록 수정
// 챔피언 개별 정보
fun championUnitInfo(db: RoomHelper, champInfo: MutableMap<String, String>): Boolean {

    val champInfoData: MutableList<LolChampInfoEntity> = mutableListOf()

    champInfo.forEach() { (championName, championJsonString) ->
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
        for (item in championData.Main.tags) {
            strTags += item + "\n"
        }

        strTags = strTags.removeRange(strTags.length - 1, strTags.length)

        for (item in championSkinData) {
            championSkinList[item.num] = item.name
        }

        val skinData = String.let {
            var skinInfo = ""
            for (item in championSkinList) {
                skinInfo += String.format("[${item.key},${item.value}]")
                skinInfo += "\n"
            }
            skinInfo = skinInfo.removeRange(skinInfo.length - 1, skinInfo.length)
            skinInfo
        }

        val championDetail = LolChampInfoEntity(
            champKeyId = championKeyName.toInt(),
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
fun completeLolPath(db: RoomHelper, infoVersion: InfoVersion) {
    val lolInfoDb = db.roomMemoDao()
    if (infoVersion.no == -1)
        lolInfoDb.insertVersion(LolVersionEntity(version = infoVersion.version, update = true))
    else
        lolInfoDb.patchVersionState(LolVersionEntity(infoVersion.no, infoVersion.version, true))
}
