package kr.co.lol.ui.usermatch

import android.bluetooth.BluetoothSocketException
import android.text.BoringLayout
import android.text.TextPaint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstrainedLayoutReference
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.android.engage.common.datamodel.Image
import kr.co.lol.R
import kr.co.lol.internet.champSkinUrl
import kr.co.lol.internet.fullChampSkinUrl
import kr.co.lol.internet.matchItem
import kr.co.lol.internet.matchSpell
import kr.co.lol.ui.component.userMatchItemHeight
import kr.co.lol.ui.theme.LolInfoViewerTheme
import kr.co.lol.ui.theme.Paddings
import kr.co.lol.ui.theme.shapes


@Composable
fun FuncshowMatech(

) {
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
                    text = "무작이 총력전",
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier
                        .padding(start = paddingValue, top = paddingValue)

                        .constrainAs(lolGameStyle) {
                            start.linkTo(resultTitle.start)
                            top.linkTo(resultTitle.top)
                        }
                )

                Text(
                    text = "2023/11/24",
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
                        text = "승리"
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
                            .data(champSkinUrl("Alistar", 29))
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
                Text(text = "17",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .padding(start = paddingValue)
                        .constrainAs(level) {
                            start.linkTo(parent.start)
                            bottom.linkTo(userMatch.bottom, 5.dp)
                        }
                )

                Text(text = "알리스타",
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
                val line1 = createHorizontalChain(item3, item2, item1, chainStyle = ChainStyle.Packed(0.9f))

                val listItem2 = listOf<ConstrainedLayoutReference>(item6, item5, item4)
                val line2 = createHorizontalChain(item4, item5, item6, chainStyle = ChainStyle.Packed(0.9f))

                val listItem3 = listOf<ConstrainedLayoutReference>(spell1, spell2, itemExtra)
                val line3 = createHorizontalChain(itemExtra, spell2, spell1, chainStyle = ChainStyle.Packed(0.9f))


                for(item in listItem1) {
                    AsyncImage(
                        model = matchItem("1058.png"),
                        contentDescription = null,
                        placeholder = painterResource(R.drawable.itemblank),
                        modifier = Modifier
                            .size(30.dp)
                            .constrainAs(item) {
                                end.linkTo(parent.end)
                                bottom.linkTo(userMatch.bottom, 50.dp + 32.dp)
                            }
                        )
                }

                for(item in listItem2) {
                    AsyncImage(
                        model = matchItem("1058.png"),
                        contentDescription = null,
                        placeholder = painterResource(R.drawable.itemblank),
                        modifier = Modifier
                            .size(30.dp)
                            .constrainAs(item) {
                                end.linkTo(parent.end)
                                bottom.linkTo(userMatch.bottom, 50.dp)
                            }
                    )
                }

                for(item in listItem3) {
                    AsyncImage(
                        model = matchSpell(spellConvert(3)),
                        contentDescription = null,
                        placeholder = painterResource(R.drawable.itemblank),
                        modifier = Modifier
                            .size(25.dp)
                            .constrainAs(item) {
                                end.linkTo(parent.end)
                                bottom.linkTo(userMatch.bottom, 25.dp)
                            }
                    )
                }

                val (scoreBox, scoreRow, minionRow, goldRow) = createRefs()
                val scoreBar = createHorizontalChain(scoreRow, minionRow, goldRow, chainStyle = ChainStyle.SpreadInside)

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .constrainAs(scoreBox) {
                            top.linkTo(userMatch.bottom)
                            start.linkTo(parent.start)
                            bottom.linkTo(parent.bottom)
                        }
                )
                Row(
                    modifier = Modifier.padding(start = paddingValue)
                        .constrainAs(scoreRow) {
                            start.linkTo(parent.start)
                            top.linkTo(scoreBox.top)
                            bottom.linkTo(scoreBox.bottom)
                    }
                ) {
                    Image(
                        painterResource(R.drawable.score),
                        contentDescription = "",
                        contentScale = ContentScale.None,
                        modifier = Modifier
                            .size(15.dp)
                    )

                    val kill = 3
                    val death = 0
                    val assist = 5
                    val scoreValue = String.format("${kill} / ${death} / ${assist}")
                    Text(
                        text = scoreValue,
                        style = MaterialTheme.typography.titleSmall
                    )
                }

                Row(
                    modifier = Modifier.padding(end = paddingValue)
                        .constrainAs(minionRow) {
                            start.linkTo(scoreRow.end)
                            top.linkTo(scoreBox.top)
                            bottom.linkTo(scoreBox.bottom)
                        }
                    ) {
                    Image(
                        painterResource(R.drawable.minion),
                        contentDescription = "",
                        contentScale = ContentScale.None,
                        modifier = Modifier
                            .size(15.dp)
                    )

                    val minion = 300

                    val scoreValue = String.format("${minion}")
                    Text(
                        text = scoreValue,
                        style = MaterialTheme.typography.titleSmall
                    )
                }

                Row(
                    modifier = Modifier.padding(end = paddingValue)
                        .constrainAs(goldRow) {
                            start.linkTo(minionRow.end)
                            top.linkTo(scoreBox.top)
                            bottom.linkTo(scoreBox.bottom)
                        }
                ) {
                    Image(
                        painterResource(R.drawable.items),
                        contentDescription = "",
                        contentScale = ContentScale.None,
                        modifier = Modifier
                            .size(15.dp)
                    )

                    val gold = 10000

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


@Composable
@Preview
fun ItemPreview() {
    LolInfoViewerTheme() {
        FuncshowMatech()
    }
}


