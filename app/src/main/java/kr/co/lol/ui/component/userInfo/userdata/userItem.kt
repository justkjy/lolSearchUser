package kr.co.lol.ui.component.userInfo.userdata

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.layoutId
import coil.compose.AsyncImage
import kr.co.lol.R
import kr.co.lol.internet.profileUrl
import kr.co.lol.ui.component.GameType
import kr.co.lol.ui.component.button.LeadingIconData
import kr.co.lol.ui.component.button.PrimaryButton
import kr.co.lol.ui.component.startPadding
import kr.co.lol.ui.theme.Paddings

@Composable
fun UserItem(gameType: GameType = GameType.LOL,
                constraintSet: ConstraintSet,
                 level1Title : String  = "일반",
                 level2Title : String  = "",
                 level3Title : String  = "",
                 rankInfo : RankInfo = RankInfo("GOLD", "IV", 394, 5986, mutableListOf<String>())
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
        UserLevel4()

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
        modifier = BoxModifier(80.dp, "level1Box")
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
        modifier = BoxModifier(80.dp, "level2Box")
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
        modifier = Modifier.layoutId("level2TierImg")
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
fun UserLevel3(title: String = "최고의 챔피언", RankInfo : RankInfo, gameType:GameType) {
    Box (
        modifier = BoxModifier(90.dp, "level3Box")
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
                ChampTopList(RankInfo.championName)
            else {
                TftDackTopList(RankInfo.tftResource())
            }
        }
    }
}

@Composable
fun UserLevel4() {
    Box (
        contentAlignment = Alignment.Center,
        modifier = BoxModifier(80.dp, "buttonBox")
            .background(MaterialTheme.colorScheme.secondary)
            .padding(start = startPadding, end = startPadding)
    ){
        val leadingIconData = LeadingIconData(iconDrawable = R.drawable.earth, iconContentDescription = R.string.primaryButtonText)
        PrimaryButton(leadingIconData = leadingIconData, id = R.string.primaryButtonDesc)
    }
}

@Composable
fun BoxModifier(height: Dp, layoutId: String) = Modifier
    .fillMaxWidth()
    .height(height)
    .focusable(enabled = false)
    .layoutId(layoutId)