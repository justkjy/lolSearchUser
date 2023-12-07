package kr.co.justkimlol.ui.component.userInfo.userdata

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import kr.co.justkimlol.viewModel.SharedViewModel
import kr.co.justkimlol.ui.component.GameType
import kr.co.justkimlol.ui.component.startPadding
import kr.co.justkimlol.ui.component.userInfoLevelBottomHeight
import kr.co.justkimlol.ui.component.userInfoLevelCenter1Height
import kr.co.justkimlol.ui.component.userInfoLevelCenter2Height
import kr.co.justkimlol.ui.component.userInfoLevelTopHeight
import kr.co.justkimlol.ui.theme.LolInfoViewerTheme
import kr.co.justkimlol.ui.theme.Paddings
import androidx.lifecycle.viewmodel.compose.viewModel
import kr.co.justkimlol.viewModel.user.UserViewModel

@Composable
fun UserLolCard(
    viewModel : UserViewModel = viewModel()
) {
    val userColumnHeight = userInfoLevelTopHeight +
                            userInfoLevelCenter2Height +
                            userInfoLevelBottomHeight


    Card (
        shape = MaterialTheme.shapes.large,
        modifier = Modifier
            .padding(Paddings.large)
            .fillMaxWidth()
            .height(userColumnHeight)
    ){
        viewModel.let {
            val userName = it.userId.observeAsState("").value
            val puuid = it.puuid.observeAsState("").value
            val tier = it.loltear.observeAsState("").value
            val summonerLevel = it.summonerLevel.observeAsState(0).value
            val profileId = it.profileIconId.observeAsState(0).value
            val rank = it.lolrank.observeAsState("").value
            val apiKey = it.apiKey.value!!
            val championName = it.champEngList.observeAsState(emptyList()).value
            val matchList = it.matchList.observeAsState(emptyList()).value

            val rankInfo  = RankInfo(
                tier,
                rank,
                summonerLevel,
                profileId,
                championName
            )

            //Log.i("TEST", "user = $matchList")
            UserItem(
                userId = userName,
                puuId = puuid,
                apiKey = apiKey,
                gameType = GameType.LOL,
                constraintSet = funcConstraintLOLSet(),
                level1Title = "일반",
                level3Title = "최고의 챔피언",
                rankInfo = rankInfo,
                matchList = matchList
            )

        }
    }
}

@Composable
fun UserTFTCard() {
    val userColumnHeight = userInfoLevelTopHeight +
                        userInfoLevelCenter1Height +
                        userInfoLevelCenter2Height +
                        userInfoLevelBottomHeight

    Card (
        shape = MaterialTheme.shapes.large,
        modifier = Modifier
            .padding(Paddings.large)
            .fillMaxWidth()
            .height(userColumnHeight)
    ){
        UserItem(
            userId = "justkim",
            gameType = GameType.TFT,
            constraintSet = funcConstraintTFTSet(),
            level1Title = "일반",
            level2Title = "더블 업",
            level3Title = "상위 특성"
        )
    }
}

@Composable
fun funcConstraintLOLSet() =
    ConstraintSet { // box layout
        val level1Box = createRefFor("level1Box")
        val level3Box = createRefFor("level3Box")
        val buttonBox = createRefFor("buttonBox")

        constrain(level1Box) {
            top.linkTo(parent.top)
            start.linkTo(parent.start)
        }

        constrain(level3Box) {
            top.linkTo(level1Box.bottom, 0.dp)
            start.linkTo(parent.start)
        }

        constrain(buttonBox) {
            top.linkTo(level3Box.bottom, 0.dp)
            start.linkTo(parent.start)
        }
        createVerticalChain(level1Box, level3Box, buttonBox, chainStyle = ChainStyle.SpreadInside)
        // user layout
        val userTitle = createRefFor("level1Title")
        val userTier = createRefFor("level1TitleValue")
        val userLank = createRefFor("Level1TitleValue2")
        val userProfileImg = createRefFor("userProfileImg")
        val userSummonerLevel = createRefFor("userSummonerLevel")

        constrain(userTitle) {
            top.linkTo(parent.top, 14.dp)
            start.linkTo(parent.start, startPadding)
        }

        constrain(userTier) {
            top.linkTo(userTitle.bottom, 10.dp)
            start.linkTo(parent.start, startPadding)
        }

        constrain(userLank) {
            top.linkTo(userTitle.bottom, 10.dp)
            start.linkTo(userTier.end, 5.dp)
        }

        constrain(userProfileImg) {
            top.linkTo(parent.top, 7.dp)
            end.linkTo(parent.end, 7.dp)
            width = Dimension.fillToConstraints
        }

        constrain(userSummonerLevel) {
            top.linkTo(userProfileImg.top, 43.dp, 5.dp)
            end.linkTo(userProfileImg.end, 12.dp)
        }
    }


fun funcConstraintTFTSet() =
    ConstraintSet { // box layout
        val level1Box = createRefFor("level1Box")
        val level2Box = createRefFor("level2Box")
        val level3Box = createRefFor("level3Box")
        val buttonBox = createRefFor("buttonBox")

        constrain(level1Box) {
            top.linkTo(parent.top)
            start.linkTo(parent.start)
        }

        constrain(level2Box) {
            top.linkTo(level1Box.bottom, 0.dp)
            start.linkTo(parent.start)
        }

        constrain(level3Box) {
            top.linkTo(level2Box.bottom, 0.dp)
            start.linkTo(parent.start)
        }

        constrain(buttonBox) {
            top.linkTo(level3Box.bottom, 0.dp)
            start.linkTo(parent.start)
        }

        //createVerticalChain(level1Box, level3Box, buttonBox, chainStyle = ChainStyle.SpreadInside)

        // user layout
        val userTitle = createRefFor("level1Title")
        val userTier = createRefFor("level1TitleValue")
        val userLank = createRefFor("Level1TitleValue2")

        // level1 사용자 티어
        constrain(userTitle) {
            top.linkTo(parent.top, 14.dp)
            start.linkTo(parent.start, startPadding)
        }
        constrain(userTier) {
            top.linkTo(userTitle.bottom, 10.dp)
            start.linkTo(parent.start, startPadding)
        }

        constrain(userLank) {
            top.linkTo(userTitle.bottom, 10.dp)
            start.linkTo(userTier.end, 5.dp)
        }

        // level2 사용자 티어 2
        val userDoubleUpTitle = createRefFor("level2Title")
        val userTierImg = createRefFor("level2TierImg")
        val userDoubleUpTier = createRefFor("level2TitleValue")
        val userDoubleUpLank = createRefFor("Level2TitleValue2")

        constrain(userDoubleUpTitle) {
            top.linkTo(level2Box.top, 14.dp)
            start.linkTo(parent.start, startPadding)
        }

        constrain(userTierImg) {
            top.linkTo(level2Box.top, 14.dp)
            end.linkTo(parent.end, startPadding)
        }

        constrain(userDoubleUpTier) {
            top.linkTo(userDoubleUpTitle.bottom, 10.dp)
            start.linkTo(parent.start, startPadding)
        }

        constrain(userDoubleUpLank) {
            top.linkTo(userDoubleUpTitle.bottom, 10.dp)
            start.linkTo(userDoubleUpTier.end, 5.dp)
        }

    }



@Preview
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun UserCardPreview() {
    LolInfoViewerTheme(true) {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.background
        ) {
            UserLolCard()
            UserTFTCard()
        }
    }
}

