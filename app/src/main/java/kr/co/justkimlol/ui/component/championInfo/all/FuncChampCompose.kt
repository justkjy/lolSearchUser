package kr.co.justkimlol.ui.component.championInfo.all

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kr.co.justkimlol.dataclass.ChampAllListData
import kr.co.justkimlol.dataclass.SpellsData
import kr.co.justkimlol.room.data.RoomHelper
import kr.co.justkimlol.room.data.roomHelperValue
import kr.co.justkimlol.ui.component.button.ChampionList
import kr.co.justkimlol.ui.component.championInfo.ChampionDetail
import kr.co.justkimlol.ui.component.championInfo.ChampionTopImg
import kr.co.justkimlol.ui.component.championInfo.rotaion.ChampionRotationList
import kr.co.justkimlol.ui.component.startPadding
import kr.co.justkimlol.ui.theme.LolInfoViewerTheme
import kr.co.justkimlol.ui.theme.Paddings

@Composable
fun ChampLolInfo(
    roomHelper: RoomHelper?,
    profileId : Int = 5986,
    userName : String = "justKim",
    userRank : String = "GOLD",
    skillNum : Int  = 500,
    champAllList : MutableList<ChampAllListData> = mutableListOf(),
    rotationInfo : MutableList<ChampionList> = mutableListOf()
) {
    var clickIndex by rememberSaveable {
        mutableIntStateOf(0)
    }

    var fillterName by rememberSaveable {
        mutableStateOf("")
    }

    var clicked by rememberSaveable {
        mutableStateOf(false)
    }

    var clickedChampEngName by rememberSaveable {
        mutableStateOf("")
    }

    val onClicked : (click : Boolean, clickedNum : Int, champEngName : String) -> (Unit)
            = { click, clickNum, champEngName->
                    clickIndex = clickNum
                    clicked = click
                    clickedChampEngName = champEngName
            }

    Column(
        modifier = Modifier.background(MaterialTheme.colorScheme.background)
            .padding(bottom = 60.dp)
    ) {
        ChampionTopImg(
            profileId = profileId,
            userName = userName,
            userTier = userRank,
            skillNum = skillNum,
        )
        ChampionRotationList(rotationInfo)

        Row {
            Text(
                text = "챔피언 리스트",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier
                    .padding(Paddings.large)
            )

            TextField(
                value = fillterName,
                onValueChange = {fillterName = it},
                singleLine = true,
                textStyle = MaterialTheme.typography.titleSmall,
                label = {
                        Text(
                            text ="챔피언 이름(kor)",
                            style = MaterialTheme.typography.titleSmall
                        )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "챔피언 이름"
                    )
                },
                trailingIcon = {
                    IconButton(
                        onClick = {
                            fillterName = ""
                        }) {
                        Icon(
                            Icons.Filled.Clear,
                            contentDescription = "필드 지우기"
                        )
                    }
                },

                modifier = Modifier.padding(start = startPadding, end = startPadding)
            )
        }

        Spacer(modifier = Modifier.padding(Paddings.medium))

        LazyColumn {
            itemsIndexed(
                champAllList.filter { champList ->
                    champList.nameKor.indexOf(fillterName) >= 0
                }
            ) {index, item->
                ChampionItem(
                    index = index,
                    champEngName = item.nameEng,
                    champTitle = item.title,
                    positionList = ArrayList(item.tagList),
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
        Log.i("champ", clickedChampEngName)
        val engName = clickedChampEngName
        val lolInfoDb = roomHelper!!.roomMemoDao()
        val list = lolInfoDb.getChampInfo(engName)

        // ChampStory
        var story = ""
        val skinList = mutableListOf<Int>()
        if (list.isNotEmpty()) {
            story = list[0].story
            val skin = list[0].skinList.split("\n")

            for(item in skin) {
                if(item != "") {
                    val buf = item.substring(1, item.length-1)
                    val skinNum = buf.split(",").get(0)
                    skinList.add(skinNum.toInt())
                }
            }
        }

        val skillList : List<SpellsData> = listOf(
            SpellsData(
                passiveOrSpells = true,
                id = "P",
                image = list[0].passiveImage,
                name = list[0].passiveName,
                description = list[0].passiveDescription
            ),
            SpellsData(
                passiveOrSpells = false,
                id = "Q",
                image = list[0].spellsQImage,
                name = list[0].spellsQName,
                description = list[0].spellsQDescription
            ),
            SpellsData(
                passiveOrSpells = false,
                id = "W",
                image = list[0].spellsWImage,
                name = list[0].spellsWName,
                description = list[0].spellsWDescription
            ),
            SpellsData(
                passiveOrSpells = false,
                id = "E",
                image = list[0].spellsEImage,
                name = list[0].spellsEName,
                description = list[0].spellsEDescription
            ),
            SpellsData(
                passiveOrSpells = false,
                id = "R",
                image = list[0].spellsRImage,
                name = list[0].spellsRName,
                description = list[0].spellsRDescription
            )
        )

        ChampionDetail(
            skinList = skinList,
            champEngName= clickedChampEngName,
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
        val helper: RoomHelper? = roomHelperValue(context)

        val testExamList = mutableListOf<ChampAllListData>()

        testExamList.add(ChampAllListData(106,"볼리베어", "Volibear", "무자비한 폭풍", "Fighter\nTank\n" ))
        testExamList.add(ChampAllListData(254,"바이", "Vi", "필트 오버의 집행자", "Fighter\nTank\n" ))

        ChampLolInfo(
            helper,
            champAllList = testExamList,
        )
    }
}

