package kr.co.lol.ui.component

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

val startPadding: Dp = 20.dp
val USERCOLUMN_HEIGHT = 250.dp

val userInfoLevelTopHeight = 80.dp         // level1
val userInfoLevelCenter1Height = 80.dp     // level2
val userInfoLevelCenter2Height = 90.dp     // level3
val userInfoLevelBottomHeight = 80.dp      // level4

val championInfoHeight = 150.dp            // 로테이션 챔피언 정보
val championInfoWith = 90.dp

val championAllItemHeight = 60.dp         // 모든 챔피언 card 높이


enum class GameType { LOL, TFT}

enum class GameTier {CHALLENGER,    GRANDMASTER,    MASTER,    DIAMOND,    EMERALD,    PLATINUM,    GOLD,    SILVER,    BRONZE,    IRON,    NONE}

enum class ChampPositionData { TANK, ASSASSIN, FIGHTER, MAGE, SUPPORT}
