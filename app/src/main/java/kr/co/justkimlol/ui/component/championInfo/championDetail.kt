package kr.co.justkimlol.ui.component.championInfo

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import com.google.accompanist.pager.HorizontalPager
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.em
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.google.accompanist.pager.ExperimentalPagerApi
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.rememberPagerState
import kr.co.justkimlol.R
import kr.co.justkimlol.dataclass.SpellsData
import kr.co.justkimlol.internet.champPassiveUrl
import kr.co.justkimlol.internet.champSkinUrl
import kr.co.justkimlol.internet.champSpellsUrl
import kr.co.justkimlol.ui.theme.LolInfoViewerTheme
import kr.co.justkimlol.ui.theme.Paddings

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ChampionDetail(
    onDismissRequest: () -> Unit = {},
    onConfirmation: () -> Unit = {},
    champEngName: String = "Volibear",
    skinList: MutableList<Int> = mutableListOf<Int>(19, 1, 4),
    champStory: String = volibearstory(),
    skillList : List<SpellsData> = listOf(
        SpellsData(
            passiveOrSpells = true,
            id = "P",
            image = "Annie_Passive.png",
            name = "방화광",
            description = "애니가 스킬을 4번 사용한 후 다음 공격 스킬에 맞은 적은 기절합니다"
        ),
        SpellsData(
            passiveOrSpells = false,
            id = "Q",
            image = "AnnieQ.png",
            name = "붕괴",
            description = "애니가 마나로 가득 찬 화염구를 던져 피해를 입히고 결정타를 냈을 때 사용한 마나를 되돌려 받습니다"
        ),
        SpellsData(
            passiveOrSpells = false,
            id = "W",
            image = "AnnieW.png",
            name = "소각",
            description = "애니가 원뿔 형태의 화염을 내뿜어 해당 지역에 있는 모든 적에게 피해를 입힙니다"
        ),
        SpellsData(
            passiveOrSpells = false,
            id = "E",
            image = "AnnieE.png",
            name = "용암 방패",
            description = "애니나 아군에게 보호막을 부여하고 이동 속도가 증가하며, 기본 공격이나 스킬로 공격하는 적에게 피해를 입힙니다"
        ),
        SpellsData(
            passiveOrSpells = false,
            id = "R",
            image = "AnnieR.png",
            name = "티버 소환",
            description = "애니가 자신의 곰 티버를 되살려 지정 구역에 있는 유닛에게 피해를 입힙니다. 티버는 주변의 적을 공격하거나 불태울 수도 있습니다"
        )
    )

) {

    Dialog(
        onDismissRequest = { onDismissRequest() },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        val tabItems = listOf(
            TabItem(
                title = "Story",
                unSelectedIcon = ImageVector.vectorResource(R.drawable.baseline_menu_book_24),
                selectedIcon = ImageVector.vectorResource(R.drawable.baseline_menu_book_24)
            ),
            TabItem(
                title = "Skill",
                unSelectedIcon = ImageVector.vectorResource(R.drawable.baseline_sports_martial_arts_24),
                selectedIcon = ImageVector.vectorResource(R.drawable.baseline_sports_martial_arts_24)
            )
        )

        Card(
            modifier = Modifier
                .fillMaxSize()
        ) {
            var selectedTabIndex = remember {
                mutableStateOf(1)
            }

            val pagerState = rememberPagerState (
                tabItems.size
            )

            LaunchedEffect(selectedTabIndex) {
                pagerState.animateScrollToPage(selectedTabIndex.value)
            }
//            LaunchedEffect(pagerState.currentPage, pagerState.isScrollInProgress) {
//                if(!pagerState.isScrollInProgress) {
//                    selectedTabIndex.value = pagerState.currentPage
//                }
//            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.3f)
            ){
                HorizontalPager(count = skinList.size) { page ->
                    AsyncImage(
                            model = champSkinUrl(champEngName, skinList[page]),
                            contentScale = ContentScale.FillHeight,
                            placeholder = painterResource(R.drawable.warrior_helmet),
                            contentDescription = "유저 레벨",
                        )
                }
            }

            Column(
//                modifier = Modifier
//                    .fillMaxHeight(0.1f)
            ) {

                TabRow(selectedTabIndex = selectedTabIndex.value) {
                    tabItems.forEachIndexed { index, item ->
                        Tab(selected = index == selectedTabIndex.value,
                            onClick = {
                                selectedTabIndex.value = index
                            },
                            text = {
                                Text(text = item.title)
                            }
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.8f)
            ) {
                if(selectedTabIndex.value == 0)
                    champStory(champStory)
                if(selectedTabIndex.value == 1)
                    champSkill(skillList)
            }
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .align(Alignment.CenterHorizontally)
            ) {
                TextButton(
                    onClick = {onConfirmation()},
                    colors =  ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        contentColor = Color.Blue),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text("close")
                }
            }
        }
    }
}

@Composable
fun champStory(storyText: String) {
    LazyColumn {
        item{
            Text(
                text = storyText,
                style = LocalTextStyle.current.merge(
                    TextStyle(
                        lineHeight = 2.0.em,
                        platformStyle = PlatformTextStyle(
                            includeFontPadding = false
                        ),
                        lineHeightStyle = LineHeightStyle(
                            alignment = LineHeightStyle.Alignment.Center,
                            trim = LineHeightStyle.Trim.None
                        )
                    )
                ),
                modifier = Modifier
                    .padding(Paddings.large)
            )
        }
    }

}

//passiveOrSpells = true이면 패시브
//                  false이면 스팰


@OptIn(ExperimentalPagerApi::class)
@Composable
fun champSkill(skillList: List<SpellsData>) {
    Column {
        val tabItems = skillList
        var selectedTabIndex by remember {
            mutableStateOf(0)
        }
        val pagerState = rememberPagerState (
            tabItems.size
        )

        LaunchedEffect(selectedTabIndex) {
            pagerState.animateScrollToPage(selectedTabIndex)
        }
        LaunchedEffect(pagerState.currentPage, pagerState.isScrollInProgress) {
            if(!pagerState.isScrollInProgress) {
                selectedTabIndex = pagerState.currentPage
            }
        }

        if(selectedTabIndex >= tabItems.size){
            selectedTabIndex = 0
        }

        Spacer(modifier = Modifier.padding(Paddings.medium))
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {

            TabRow(selectedTabIndex = selectedTabIndex,
                containerColor = MaterialTheme.colorScheme.background,
                    //backgroundColor = Color.Transparent.copy(0.1f),//To separate it from background
                    modifier = Modifier
                        .padding(vertical = 5.dp, horizontal = 8.dp)
                        .clip(MaterialTheme.shapes.large)//Consistent look
                ) {
                tabItems.forEachIndexed { index, item ->
                    Tab(selected = index == selectedTabIndex,
                        onClick = {
                            selectedTabIndex = index
                        },

                        text = {
                            Text(
                                text = item.id,
                                color = when(index) {
                                            selectedTabIndex -> Color.LightGray
                                            else -> MaterialTheme.colorScheme.primary
                                        }
                            )
                        },
                        icon = {
                            val url = when (item.passiveOrSpells) {
                                                true -> champPassiveUrl(item.image)
                                                false -> champSpellsUrl(item.image)
                                        }

                            AsyncImage(
                                model = url,
                                contentScale = ContentScale.FillWidth,
                                placeholder = painterResource(R.drawable.warrior_helmet),
                                contentDescription = "유저 레벨",
                                modifier = Modifier.size(40.dp)
                            )
                        },
                        modifier = Modifier.clip(MaterialTheme.shapes.large)
                    )
                }
            }
            HorizontalPager(
                count = tabItems.size,
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {index ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(Paddings.large)
                    //contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = tabItems[index].name,
                        style = MaterialTheme.typography.titleLarge
                    )

                    Spacer(modifier = Modifier.padding(Paddings.large))

                    LazyColumn {
                        item {
                            Text(
                                text = String.let{
                                    var text = tabItems[index].description
                                    var findWord = 0
                                    var startPoint : Int = 0
                                    var endPoint : Int = 0
                                    while(findWord != -1) {
                                        startPoint = text.indexOf("<")
                                        endPoint = text.indexOf(">")
                                        if((endPoint == -1) or (startPoint == -1)) {
                                            findWord = -1
                                        } else {
                                            text = text.removeRange(startPoint, endPoint+1)
                                        }
                                    }

                                    text
                                },
                                style = LocalTextStyle.current.merge(
                                    TextStyle(
                                        lineHeight = 2.0.em,
                                        platformStyle = PlatformTextStyle(
                                            includeFontPadding = false
                                        ),
                                        lineHeightStyle = LineHeightStyle(
                                            alignment = LineHeightStyle.Alignment.Center,
                                            trim = LineHeightStyle.Trim.None
                                        )
                                    )
                                )
                            )
                        }
                    }
                }
            }

        }

//
//        for(n in 1..5) {
//            Spacer(modifier = Modifier.padding(Paddings.medium))
//            val fullUrl = champPassiveUrl("AnnieQ.png")
//            Row {
//                Box(
//                    Modifier.size(40.dp)
//                ) {
//                    AsyncImage(
//                        model = champPassiveUrl("Annie_Passive.png"),
//                        contentScale = ContentScale.FillWidth,
//                        placeholder = painterResource(R.drawable.warrior_helmet),
//                        contentDescription = "유저 레벨",
//                        modifier = Modifier.size(40.dp)
//                    )
//                }
//                Spacer(modifier = Modifier.padding(Paddings.medium))
//                Column {
//                    Text("붕괴")
//                    Spacer(modifier = Modifier.padding(Paddings.medium))
//                    Text("애니가 마나로 가득 찬 화염구를 던져 피해를 입히고 결정타를 냈을 때 사용한 마나를 되돌려 받습니다.")
//                }
//            }
//        }


    }

}

fun volibearstory() : String =
    "추종자들에게 볼리베어는 여전히 폭풍 그 자체이다. 강력하고 야만적이며 고집스러울 정도로 단호한 그는 프렐요드의 동토에 필멸자들이 나타나기 전부터 존재했다. 다른 반신들과 함께 일구어낸 그 땅을 무척이나 아끼는 볼리베어는 나약하기 그지없는 문명의 발달을 극도로 혐오했고, 결국 거칠고 폭력적이었던 옛 전통을 되찾기 위해 싸움을 시작한다. 그 앞을 가로막는 자는 누구든 볼리베어의 이빨과 발톱, 천둥의 위력을 맛보게 될 것이다."


data class TabItem(
    val title: String,
    val unSelectedIcon: ImageVector,
    val selectedIcon: ImageVector
)

@Preview
@Composable
fun PreviewChampionDetail() {
    LolInfoViewerTheme {
        ChampionDetail()
    }
}
