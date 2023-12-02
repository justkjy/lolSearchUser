package kr.co.justkimlol.mainfragment.user.usermatch

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstrainedLayoutReference
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.AsyncImage
import coil.request.ImageRequest
import kr.co.justkimlol.R
import kr.co.justkimlol.dataclass.UserMatchId
import kr.co.justkimlol.internet.champSkinUrl
import kr.co.justkimlol.internet.matchItem
import kr.co.justkimlol.internet.matchSpell
import kr.co.justkimlol.internet.noneItem
import kr.co.justkimlol.room.data.roomHelperValue
import kr.co.justkimlol.ui.component.userMatchItemHeight
import kr.co.justkimlol.ui.theme.LolInfoViewerTheme
import kr.co.justkimlol.ui.theme.Paddings


@Composable
fun FuncshowMatech(
    index: Int = 0,
    userMatchId: UserMatchId? = null,
    puuId: String = "",
    insertChampUpdate: (index: Int, championEngName: String, win: Boolean, kill: Int, death: Int) -> Unit
    = {_,_, _, _, _ -> }
) {
    var gamemMode = "ARAM"
    var gameDuration = "16:31분"
    var startTime = "2023/11/25 13:25"

    var gameWin = "승리"
    var champEngName = "Alistar"
    var champKorName = "알리스타"
    var chmapNum = 12
    var champLevel =  0
    var userItem = arrayOf(0, 0, 0, 0, 0, 0)
    var userSpell = arrayOf(0, 0, 0) // 마지막껀 장신구임
    var userKill = 0
    var userDeath = 0
    var userassist = 0

    var minion = 0
    var gold = 0

    userMatchId?.let { match->
        gamemMode = gameModeConvert(match.info.gameMode)
        gameDuration = gameDurationConver(match.info.gameDuration)
        startTime = gameUnixTimeToDate(match.info.gameStartTimestamp)

        var userIndex = 0
        for(item in match.metadata.participants) {
            if(item.equals(puuId)) {
                break
            }
            userIndex++
        }
        val userPart = match.info.participants[userIndex]
        if(userPart.win) gameWin = "승리"
        else gameWin = "패배"
        champEngName = userPart.championName
        val context = LocalContext.current
        champKorName = champEngNameToKorName(champEngName, context)
        chmapNum = userPart.championId
        champLevel = userPart.champLevel
        userItem[0] = userPart.item0
        userItem[1] = userPart.item1
        userItem[2] = userPart.item2
        userItem[3] = userPart.item3
        userItem[4] = userPart.item4
        userItem[5] = userPart.item5

        userSpell[0]= userPart.summoner1Id
        userSpell[1]= userPart.summoner2Id
        userSpell[2] = userPart.item6

        userKill = userPart.kills
        userDeath = userPart.deaths
        userassist = userPart.assists

        minion = userPart.totalMinionsKilled
        gold = userPart.goldEarned

        insertChampUpdate(index, champEngName, userPart.win, userKill, userDeath)

    } ?: {

    }
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .height(userMatchItemHeight)
            .fillMaxWidth()
            .padding(Paddings.large)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Paddings.small)
        ) {
            val paddingValue = Paddings.large
            ConstraintLayout(
                Modifier.fillMaxSize()
            ) {
                // Title
                val (resultTitle, lolGameStyle, playDate, playTime, gameResult) = createRefs()
                Box(
                    modifier = Modifier
                        .fillMaxHeight(0.20f)
                        .fillMaxWidth()

                        .constrainAs(resultTitle) {
                            start.linkTo(parent.start)
                            top.linkTo(parent.top)
                        }
                ) { }

                Text(
                    text = gamemMode,
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier
                        .padding(start = paddingValue, top = paddingValue)

                        .constrainAs(lolGameStyle) {
                            start.linkTo(resultTitle.start)
                            top.linkTo(resultTitle.top)
                        }
                )

                Text(
                    text = startTime,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .padding(start = paddingValue, top = Paddings.small)

                        .constrainAs(playDate) {
                            start.linkTo(resultTitle.start)
                            top.linkTo(lolGameStyle.bottom)
                        }
                )
                Text(
                    text = "16:31분",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .padding(start = Paddings.small, top = Paddings.small)
                        .constrainAs(playTime) {
                            start.linkTo(playDate.end)
                            bottom.linkTo(playDate.bottom)
                        }
                )
                Button(
                    onClick = {},
                    shape = MaterialTheme.shapes.extraLarge,
                    modifier = Modifier
                        .padding(end = paddingValue)
                        .constrainAs(gameResult) {
                            end.linkTo(parent.end)
                            top.linkTo(lolGameStyle.top)
                        }
                ) {
                    Text(
                        text = gameWin
                    )
                }

                val brush = Brush.radialGradient(listOf(
                    Color.Black,
                    Color.Black.copy(0.5f),
                    Color.White.copy(0.0f))
                )
                val (userMatch, champPic, level, champName) = createRefs()

                Box(
                    modifier = Modifier
                        .fillMaxHeight(0.6f)
                        .padding(top = 5.dp)
                        .fillMaxWidth()
                        .constrainAs(userMatch) {
                            top.linkTo(resultTitle.bottom)
                            start.linkTo(parent.start)
                        }
                ){
                    val backColor = MaterialTheme.colorScheme.secondary
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(champSkinUrl(champEngName, 0))
                            .build(),

                        contentDescription = null,
                        placeholder = painterResource(R.drawable.user_cowboy),
                        contentScale = ContentScale.FillHeight,
                        modifier = Modifier
                            .fillMaxHeight()
                            .graphicsLayer { alpha = 0.9f }
                            .drawWithContent {
                                val colorsEnd = listOf(
                                    backColor,
                                    backColor,
                                    backColor,
                                    Color.Transparent
                                )

                                val colorsBottom = listOf(
                                    backColor,
                                    Color.Transparent
                                )
                                drawContent()
                                drawRect(
                                    brush = Brush.horizontalGradient(colorsEnd),
                                    blendMode = BlendMode.DstIn
                                )
                                drawRect(
                                    brush = Brush.verticalGradient(colorsBottom),
                                    blendMode = BlendMode.DstIn
                                )
                            }
                    )
                }
                Text(text = "${champLevel}",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .padding(start = paddingValue)
                        .constrainAs(level) {
                            start.linkTo(parent.start)
                            bottom.linkTo(userMatch.bottom, 5.dp)
                        }
                )

                Text(text = champKorName,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .padding(start = paddingValue)
                        .constrainAs(champName) {
                            start.linkTo(level.end)
                            top.linkTo(level.top)
                        }
                )

                val (item1, item2, item3, item4, item5, item6, spell1, spell2, itemExtra) = createRefs()

                val listItem1 = listOf<ConstrainedLayoutReference>(item3, item2, item1)
                val line1 = createHorizontalChain(item3, item2, item1, chainStyle = ChainStyle.Packed(1.0f))

                val listItem2 = listOf<ConstrainedLayoutReference>(item6, item5, item4)
                val line2 = createHorizontalChain(item4, item5, item6, chainStyle = ChainStyle.Packed(1.0f))

                val listItem3 = listOf<ConstrainedLayoutReference>(spell1, spell2, itemExtra)
                val line3 = createHorizontalChain(itemExtra, spell2, spell1, chainStyle = ChainStyle.Packed(1.0f))

                var itemIndex = 0
                for(item in listItem1) {
                    val itemNum = userItem[itemIndex]
                    var url = matchItem("${itemNum}.png")
                    if(itemNum == 0) {
                        url = noneItem()
                    }

                    AsyncImage(
                        model = url,
                        contentDescription = null,
                        placeholder = painterResource(R.drawable.itemblank),
                        modifier = Modifier
                            .width(35.dp)
                            .height(30.dp)
                            .padding(end = 5.dp)
                            .constrainAs(item) {
                                end.linkTo(userMatch.end)
                                bottom.linkTo(userMatch.bottom, 50.dp + 32.dp + 10.dp)
                            }
                        )
                    itemIndex++
                }

                itemIndex = 5
                for(item in listItem2) {
                    val itemNum = userItem[itemIndex]
                    var url = matchItem("${itemNum}.png")

                    if(itemNum == 0) {
                        url = noneItem()
                    }
                    AsyncImage(
                        model = url,
                        contentDescription = null,
                        placeholder = painterResource(R.drawable.itemblank),
                        modifier = Modifier
                            .width(35.dp)
                            .height(30.dp)
                            .padding(end = 5.dp)
                            .constrainAs(item) {
                                end.linkTo(userMatch.end)
                                bottom.linkTo(userMatch.bottom, 55.dp)
                            }
                    )
                    itemIndex--
                }

                var spellIndex = 2
                for(item in listItem3) {
                    var url = ""
                    if(spellIndex == 2) {
                        url = matchItem("${userSpell[2]}.png")
                    } else {
                        url = matchSpell(spellConvert(userSpell[spellIndex]))
                    }

                    AsyncImage(
                        model = url,
                        contentDescription = null,
                        placeholder = painterResource(R.drawable.itemblank),
                        modifier = Modifier
                            .size(25.dp)
                            .constrainAs(item) {
                                end.linkTo(userMatch.end)
                                bottom.linkTo(userMatch.bottom, 20.dp)
                            }
                    )
                    spellIndex--
                }

                val (scoreBox, scoreRow, minionRow, goldRow) = createRefs()
                val scoreBar = createHorizontalChain(scoreRow, minionRow, goldRow, chainStyle = ChainStyle.SpreadInside)

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(end = paddingValue)
                        .constrainAs(scoreBox) {
                            top.linkTo(userMatch.bottom)
                            start.linkTo(parent.start)
                            bottom.linkTo(parent.bottom)
                        }
                )
                Row(
                    modifier = Modifier
                        .padding(start = paddingValue)
                        .constrainAs(scoreRow) {
                            start.linkTo(parent.start)
                            top.linkTo(scoreBox.top)
                            bottom.linkTo(scoreBox.bottom)
                        }
                ) {
                    Image(
                        painterResource(R.drawable.itemblank),
                        contentDescription = "",
                        contentScale = ContentScale.None,
                        modifier = Modifier
                            .size(15.dp)
                    )

                    Spacer(modifier = Modifier.padding(Paddings.small))
    
                    val scoreValue = String.format("${userKill} / ${userDeath} / ${userassist}")
                    Text(
                        text = scoreValue,
                        style = MaterialTheme.typography.titleSmall
                    )
                }

                Row(
                    modifier = Modifier
                        .padding(end = paddingValue)
                        .constrainAs(minionRow) {
                            start.linkTo(scoreRow.end)
                            top.linkTo(scoreBox.top)
                            bottom.linkTo(scoreBox.bottom)
                        }
                    ) {
                    Image(
                        painterResource(R.drawable.minion3),
                        contentDescription = "",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .size(20.dp)
                    )
                    Spacer(modifier = Modifier.padding(Paddings.small))
                    val scoreValue = String.format("${minion}")
                    Text(
                        text = scoreValue,
                        style = MaterialTheme.typography.titleSmall
                    )
                }

                Row(
                    modifier = Modifier
                        .padding(end = paddingValue)
                        .constrainAs(goldRow) {
                            start.linkTo(minionRow.end)
                            top.linkTo(scoreBox.top)
                            bottom.linkTo(scoreBox.bottom)
                        }
                ) {

                    Image(
                        painterResource(R.drawable.gold2),
                        contentDescription = "",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .size(15.dp)
                    )

                    Spacer(modifier = Modifier.padding(Paddings.small))
                    val scoreValue = String.format("${gold}")
                    Text(
                        text = scoreValue,
                        style = MaterialTheme.typography.titleSmall
                    )
                }
            }
        }
    }
}

fun spellConvert(summonerId : Int) : String {
    return when(summonerId) {
        1 -> "SummonerBoost.png" // 정화
        3 -> "SummonerExhaust.png" //탈진
        4 -> "SummonerFlash.png" //점멸
        6 -> "SummonerHaste.png" //유체화
        7 -> "SummonerHeal.png" //회복
        11 -> "SummonerSmite.png" // 강타
        12 -> "SummonerTeleport.png" //텔(순간이동)
        13 -> "SummonerMana.png" //총명
        14 -> "SummonerDot.png"//점화
        21 -> "SummonerBarrier.png"//방어막
        32 -> "SummonerSnowball.png" //표식 (눈덩이)
        else -> ""
    }
}

fun gameModeConvert(gameMode : String)
= when(gameMode) {
        "CLASSIC" -> "소환사 협곡"
        "ARAM" -> "칼바람"
        "URF" -> "우르프"
        "NEXUSBLITZ" -> "돌격 넥서스"
        "TUTORIAL" -> "튜토리얼"
        else->"그 외"
    }

fun gameDurationConver(gameDuration: Int) : String {
    var min = gameDuration/60
    var sec = gameDuration % 60
    var hour = min / 60
    var time = ""
    if (hour > 0) {
        time = "${hour}:${min}.${sec}분"
    } else {
        time = "${min}.${sec}분"
    }
    return time
}

fun gameUnixTimeToDate(unixTime : Long) : String {
    val sdf = java.text.SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
    val date = java.util.Date(unixTime)
    return sdf.format(date)
}

fun champEngNameToKorName(champEngName: String, context: Context) : String {

    var helper = roomHelperValue(context)
    val lolInfoDb = helper!!.roomMemoDao()
    val list =  lolInfoDb.getChampInfo(champEngName)
    return if(list.isNullOrEmpty()) {
        ""
    } else {
        list.first().nameKor
    }
}

@Composable
@Preview
fun ItemPreview() {
    LolInfoViewerTheme() {
        FuncshowMatech()
    }
}


