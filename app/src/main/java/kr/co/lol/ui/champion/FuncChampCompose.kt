package kr.co.lol.ui.champion

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import kr.co.lol.dataclass.ChampAllListData
import kr.co.lol.dataclass.SpellsData
import kr.co.lol.room.data.RoomHelper
import kr.co.lol.room.data.roomHelperValue
import kr.co.lol.ui.component.button.ChampionList
import kr.co.lol.ui.component.championInfo.ChampionTopImg
import kr.co.lol.ui.component.championInfo.all.ChampionItem
import kr.co.lol.ui.component.championInfo.championDetail
import kr.co.lol.ui.component.championInfo.rotaion.championRotationList
import kr.co.lol.ui.home.LevelTop
import kr.co.lol.ui.theme.LolInfoViewerTheme
import kr.co.lol.ui.theme.LolWhiteTheme
import kr.co.lol.ui.theme.Paddings
import kr.co.lol.ui.user.UserTop

@Composable
fun ChampLolInfo(
    roomHelper: RoomHelper?,
    profileId : Int = 5986,
    userName : String = "justKim",
    userRank : String = "GOLD",
    skillNum : Int  = 500,
    champAllList : MutableList<ChampAllListData> = mutableListOf<ChampAllListData>(),
    rotationInfo : MutableList<ChampionList> = mutableListOf<ChampionList>()
) {

    var clickIndex by rememberSaveable {
        mutableIntStateOf(0)
    }

    var clicked by rememberSaveable {
        mutableStateOf(false)
    }
    val onClicked : (click : Boolean, clickedNum : Int) -> (Unit)
            = { click, clickNum ->
                    clickIndex = clickNum
                    clicked = click
            }

    Column(
        modifier = Modifier.background(MaterialTheme.colorScheme.background)
    ) {
        ChampionTopImg(
            profileId = profileId,
            userName = userName,
            userTier = userRank,
            skillNum = skillNum,
        )
        championRotationList(rotationInfo)

        Text(
            text = "챔피언 리스트",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier
                .padding(Paddings.large)
        )
        Spacer(modifier = Modifier.padding(Paddings.medium))

        LazyColumn {
            itemsIndexed(champAllList) {index, item->
                ChampionItem(
                    index = index,
                    champEngName = item.nameEng,
                    champKorName = item.nameKor,
                    champTitle = item.title,
                    positionList = ArrayList<String>(item.tag),
                    onClick = onClicked,
                )
                Spacer(modifier = Modifier.padding(Paddings.small))
            }
        }
    }

    val onConfirmation : () -> (Unit) = {
        clicked = false
    }

    if(clicked) {
        val engName = champAllList[clickIndex].nameEng
        val lolInfoDb = roomHelper!!.roomMemoDao()
        val list = lolInfoDb.getChampInfo(engName)
        // ChampStory
        var story = ""
        var skinList = mutableListOf<Int>()
        if (list.isNotEmpty()) {
            story = list.get(0).story
            val skin = list.get(0).skinList.split("\n")

            for(item in skin) {
                if(item != "") {
                    var buf = item.substring(1, item.length-1)
                    val skinNum = buf.split(",").get(0)
                    skinList.add(skinNum.toInt())
                }
            }
        }

        val skillList : List<SpellsData> = listOf(
            SpellsData(
                passiveOrSpells = true,
                id = "P",
                image = list.get(0).passiveImage,
                name = list.get(0).passiveName,
                description = list.get(0).passiveDescription
            ),
            SpellsData(
                passiveOrSpells = false,
                id = "Q",
                image = list.get(0).spellsQImage,
                name = list.get(0).spellsQName,
                description = list.get(0).spellsQDescription
            ),
            SpellsData(
                passiveOrSpells = false,
                id = "W",
                image = list.get(0).spellsWImage,
                name = list.get(0).spellsWName,
                description = list.get(0).spellsWDescription
            ),
            SpellsData(
                passiveOrSpells = false,
                id = "E",
                image = list.get(0).spellsEImage,
                name = list.get(0).spellsEName,
                description = list.get(0).spellsEDescription
            ),
            SpellsData(
                passiveOrSpells = false,
                id = "R",
                image = list.get(0).spellsRImage,
                name = list.get(0).spellsRName,
                description = list.get(0).spellsRDescription
            )
        )

        championDetail(
            skinList = skinList,
            champEngName= champAllList[clickIndex].nameEng,
            champStory = story,
            onConfirmation = onConfirmation,
            skillList = skillList
        )
    }
}

@Preview
@Composable
fun PreviewChampLolInfo() {
    LolInfoViewerTheme(true) {

        val context = LocalContext.current
        var helper: RoomHelper? = roomHelperValue(context)

        var testExamList = mutableListOf<ChampAllListData>()

        testExamList.add(ChampAllListData(106,"볼리베어", "Volibear", "무자비한 폭풍", "Fighter\nTank\n" ))
        testExamList.add(ChampAllListData(254,"바이", "Vi", "필트 오버의 집행자", "Fighter\nTank\n" ))


        ChampLolInfo(
            helper,
            champAllList = testExamList,
        )
    }
}

