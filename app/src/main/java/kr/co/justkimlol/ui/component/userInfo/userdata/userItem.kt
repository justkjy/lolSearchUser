package kr.co.justkimlol.ui.component.userInfo.userdata

import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.layoutId
import androidx.core.content.ContextCompat.startActivity
import coil.compose.AsyncImage
import kr.co.justkimlol.R
import kr.co.justkimlol.mainfragment.home.internet.profileUrl
import kr.co.justkimlol.ui.component.GameType
import kr.co.justkimlol.ui.component.button.LeadingIconData
import kr.co.justkimlol.ui.component.button.PrimaryButton
import kr.co.justkimlol.ui.component.startPadding
import kr.co.justkimlol.ui.theme.Paddings
import kr.co.justkimlol.mainfragment.user.usermatch.MatchActivity

@Composable
fun UserItem(
    userId : String,
    puuId : String = "AH3tjkvRgXPgrUEaKIeZgVJcJeRKYJFiX27RXVs4yvZuF5GqueBBY7oL4SHci2RM9LdTPW5FsL3XhQ",
    apiKey: String = "",
    gameType: GameType = GameType.LOL,
    constraintSet: ConstraintSet,
    level1Title: String = "일반",
    level2Title: String = "",
    level3Title: String = "",
    rankInfo: RankInfo = RankInfo("GOLD", "IV", 394, 5986, mutableListOf<String>()),
    matchList: List<String> = listOf<String>(
        "KR_6812175923",
        "KR_6810800561",
        "KR_6810787381",
        "KR_6810534387",
        "KR_6810499776",
        "KR_6810457996",
        "KR_6810423013",
        "KR_6810363384",
        "KR_6808880454",
        "KR_6808841052",
        "KR_6808776793",
        "KR_6808713712",
        "KR_6807565304",
        "KR_6807546560",
        "KR_6807534122",
        "KR_6807510917",
        "KR_6807331764",
        "KR_6807283627",
        "KR_6806026800",
        "KR_6806003457",
        "KR_6805976124",
        "KR_6805941764",
        "KR_6805904685",
        "KR_6805857984",
        "KR_6805835268",
        "KR_6805747483",
        "KR_6805704144",
        "KR_6802286886",
        "KR_6802264432",
        "KR_6802236108",
        "KR_6802170366",
        "KR_6802129112",
        "KR_6799029793",
        "KR_6799014182",
        "KR_6797560020",
        "KR_6797531525",
        "KR_6797498931",
        "KR_6796198625",
        "KR_6796173016",
        "KR_6796140725",
        "KR_6793626335",
        "KR_6793594198",
        "KR_6793551446",
        "KR_6793520948",
        "KR_6793490155",
        "KR_6793471472",
        "KR_6793443780",
        "KR_6793219429",
        "KR_6793189468",
        "KR_6793168007",
        "KR_6793128613",
        "KR_6789790392",
        "KR_6789766949",
        "KR_6789752107",
        "KR_6789723344",
        "KR_6789681960",
        "KR_6789653581",
        "KR_6788518345",
        "KR_6788499513",
        "KR_6788491288",
        "KR_6788466523",
        "KR_6787153711",
        "KR_6787141437",
        "KR_6787122654",
        "KR_6785883035",
        "KR_6785872063",
        "KR_6785840866",
        "KR_6785822670",
        "KR_6784331812",
        "KR_6784297835",
        "KR_6784254745",
        "KR_6784213757",
        "KR_6780772482",
        "KR_6780744984",
        "KR_6779548240",
        "KR_6779532088",
        "KR_6779495979",
        "KR_6779154449",
        "KR_6779132411",
        "KR_6779130276",
        "KR_6779112456",
        "KR_6779083139",
        "KR_6779044201",
        "KR_6778986992",
        "KR_6778204002",
        "KR_6777913011",
        "KR_6777898247",
        "KR_6777880916",
        "KR_6776898426",
        "KR_6776881874",
        "KR_6776873459",
        "KR_6776599702",
        "KR_6776582922",
        "KR_6776557940",
        "KR_6776547388",
        "KR_6776516368",
        "KR_6776487108",
        "KR_6776444475",
        "KR_6776401632",
        "KR_6776361099"
    )
) {

    ConstraintLayout (
        constraintSet,
        modifier = Modifier.fillMaxSize()
    ){
        //// level1 일반, TFT 랭크 ////////////////////////////////////////////////////////////
        UserLevel1(level1Title, rankInfo)

        if(gameType == GameType.LOL) {
            AsyncImage(
                model = profileUrl(rankInfo.profileId),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(R.drawable.user_cowboy),
                contentDescription = "유저 레벨",
                modifier = Modifier
                    .clip(CircleShape)
                    .size(50.dp)
                    .layoutId("userProfileImg")
            )

            OutlinedButton(
                onClick = {},
                enabled = false,
                border = BorderStroke(0.5.dp, Color.Black),
                contentPadding = PaddingValues(),
                modifier = Modifier
                    .padding(Paddings.none)
                    .size(25.dp, 14.dp)
                    .defaultMinSize(1.dp, 1.dp)
                    .background(
                        MaterialTheme.colorScheme.primary
                    )
                    .padding(0.dp)
                    .layoutId("userSummonerLevel")

            ) {
                Text(
                    text = rankInfo.summonerLevel.toString(),
                    style = MaterialTheme.typography.labelMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(Paddings.none)
                        .width(25.dp)
                        .height(12.dp)
                        .background(
                            MaterialTheme.colorScheme.primary
                        )
                )
            }
        }

        if(gameType == GameType.TFT) UserLevel2(level2Title,rankInfo)

        //// level3 최고 챔피언 리스트, TFT 상위 특성 ////////////////////////////////////////////////////////////
        UserLevel3(level3Title, rankInfo, gameType/*GameType.LOL*/)

        /// level 4 대전 기록
        UserLevel4(gameType, userId, puuId, apiKey, rankInfo, matchList)

        // level 사이 라인 그리기
        val lineColor = MaterialTheme.colorScheme.primary
        Canvas(modifier = Modifier.fillMaxSize()) {
            var counter = 1
            if(GameType.TFT == gameType) {
                counter = 2
            }

            for(heightLine in 1..counter){
                drawLine(
                    color = lineColor,
                    start = Offset(startPadding.toPx() ,220.0f * heightLine),
                    end =  Offset(380.dp.toPx() - startPadding.toPx(), 220.0f * heightLine),
                    strokeWidth = 1.dp.toPx()
                )
            }
        }
    }
}

@Composable
fun UserLevel1(title: String = "랭크", rankInfo : RankInfo) {
    Box (
        modifier = boxModifier(80.dp, "level1Box")
    )
    Text(
        text = title,
        color = MaterialTheme.colorScheme.primary,
        style = MaterialTheme.typography.titleSmall,
        modifier = Modifier.layoutId("level1Title")
    )

    Text(
        text = rankInfo.tier,
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.layoutId("level1TitleValue")
    )
    Text (
        text = rankInfo.rank,
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.layoutId("Level1TitleValue2")
    )
}

@Composable
fun UserLevel2(title: String = "더블 업", RankInfo : RankInfo) {
    Box (
        modifier = boxModifier(80.dp, "level2Box")
    )
    Text(
        text = title,
        color = MaterialTheme.colorScheme.primary,
        style = MaterialTheme.typography.titleSmall,
        modifier = Modifier.layoutId("level2Title")
    )

    Image(
        painter = painterResource(id = RankInfo.tierResourceId),
        contentDescription = RankInfo.tier,
        modifier = Modifier
            .layoutId("level2TierImg")
            .padding(Paddings.none)
            .size(52.dp)
    )

    Text(
        text = RankInfo.tier,
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.layoutId("level2TitleValue")
    )
    Text (
        text = RankInfo.rank,
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.layoutId("Level2TitleValue2")
    )
}

@Composable
fun UserLevel3(
    title: String = "최고의 챔피언",
    rankInfo: RankInfo,
    gameType: GameType,
) {
    Box (
        modifier = boxModifier(90.dp, "level3Box")
    ){
        Column(modifier = Modifier.padding(Paddings.none)) {
            Text(
                text = title,
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(start = startPadding, top = 10.dp)
            )
            Spacer(modifier = Modifier.padding(Paddings.small))
            if(gameType == GameType.LOL)
                ChampTopList(rankInfo.championName)
            else {
                TftDackTopList(rankInfo.tftResource())
            }
        }
    }
}

@Composable
fun UserLevel4(gameType: GameType,
               userId: String,
               puuId: String,
               apiKey: String,
               rankInfo: RankInfo,
               matchList: List<String>) {
    var clicked by rememberSaveable {
        mutableStateOf(false)
    }

    Box (
        contentAlignment = Alignment.Center,
        modifier = boxModifier(80.dp, "buttonBox")
            .background(MaterialTheme.colorScheme.secondary)
            .padding(start = startPadding, end = startPadding)
    ){
        val leadingIconData = LeadingIconData(iconDrawable = R.drawable.earth, iconContentDescription = R.string.primaryButtonText)
        PrimaryButton(
            leadingIconData = leadingIconData,
            id = R.string.primaryButtonDesc,
            onClick = {
                clicked = true
            }
        )
    }

    //userId
    if(GameType.LOL == gameType && clicked){
        val context = LocalContext.current
        val intent = Intent(context, MatchActivity::class.java)

        intent.putExtra("userId", userId)
        intent.putExtra("apiKey", apiKey)
        intent.putExtra("puuId", puuId)
        intent.putExtra("profileId", rankInfo.profileId )
        intent.putExtra("userTier", rankInfo._tier )
        intent.putExtra("skillNum", rankInfo.summonerLevel )
        intent.putExtra("matchList", ArrayList<String>(matchList))

        intent.putExtra("Top1", rankInfo.championName[0]?.let{ it } ?: "")
        intent.putExtra("Top2", rankInfo.championName[1]?.let{ it } ?: "")
        intent.putExtra("Top3", rankInfo.championName[2]?.let{ it } ?: "")
        startActivity(context, intent, null)
        clicked = false
    }
}

@Composable
fun boxModifier(height: Dp, layoutId: String) = Modifier
    .fillMaxWidth()
    .height(height)
    .focusable(enabled = false)
    .layoutId(layoutId)