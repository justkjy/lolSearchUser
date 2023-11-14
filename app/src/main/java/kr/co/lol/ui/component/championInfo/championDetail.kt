package kr.co.lol.ui.component.championInfo

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import kr.co.lol.R
import kr.co.lol.internet.champSkinUrl
import kr.co.lol.ui.theme.LolInfoViewerTheme
import kr.co.lol.ui.theme.Paddings

@OptIn(ExperimentalFoundationApi::class, ExperimentalPagerApi::class)
@Composable
fun championDetail(
    onDismissRequest: () -> Unit = {},
    onConfirmation: () -> Unit = {},
    champEngName: String = "Volibear",
    skinList: MutableList<Int> = mutableListOf<Int>(19, 1, 4),
    champStory: String = volibearstory()
) {

    Dialog(
        onDismissRequest = { onDismissRequest() },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.4f)
            ){
//                LazyRow(
//                    modifier = Modifier
//                        .fillMaxSize()
//                ) {
//                    items(skinList) {item->
//                        AsyncImage(
//                            model = champSkinUrl(champEngName, item),
//                            contentScale = ContentScale.FillHeight,
//                            placeholder = painterResource(R.drawable.warrior_helmet),
//                            contentDescription = "유저 레벨",
//                        )
//                    }
//                }
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
                modifier = Modifier
                    .fillMaxHeight(0.8f)

                ) {
                val story = champStory
                Text(
                    text = story,
                    //color = MaterialTheme.colorScheme.primary,
                    //style = MaterialTheme.typography.titleSmall,
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
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxSize()
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

fun volibearstory() : String =
    "추종자들에게 볼리베어는 여전히 폭풍 그 자체이다. 강력하고 야만적이며 고집스러울 정도로 단호한 그는 프렐요드의 동토에 필멸자들이 나타나기 전부터 존재했다. 다른 반신들과 함께 일구어낸 그 땅을 무척이나 아끼는 볼리베어는 나약하기 그지없는 문명의 발달을 극도로 혐오했고, 결국 거칠고 폭력적이었던 옛 전통을 되찾기 위해 싸움을 시작한다. 그 앞을 가로막는 자는 누구든 볼리베어의 이빨과 발톱, 천둥의 위력을 맛보게 될 것이다."

@Preview
@Composable
fun PreviewChampionDetail() {
    LolInfoViewerTheme {
        championDetail()
    }
}


fun volibear_story() = "추종자들에게 볼리베어는 여전히 폭풍 그 자체이다. 강력하고 야만적이며 고집스러울 정도로 단호한 그는 프렐요드의 동토에 필멸자들이 나타나기 전부터 존재했다. 다른 반신들과 함께 일구어낸 그 땅을 무척이나 아끼는 볼리베어는 나약하기 그지없는 문명의 발달을 극도로 혐오했고, 결국 거칠고 폭력적이었던 옛 전통을 되찾기 위해 싸움을 시작한다. 그 앞을 가로막는 자는 누구든 볼리베어의 이빨과 발톱, 천둥의 위력을 맛보게 될 것이다.\",\"blurb\":\"추종자들에게 볼리베어는 여전히 폭풍 그 자체이다. 강력하고 야만적이며 고집스러울 정도로 단호한 그는 프렐요드의 동토에 필멸자들이 나타나기 전부터 존재했다. 다른 반신들과 함께 일구어낸 그 땅을 무척이나 아끼는 볼리베어는 나약하기 그지없는 문명의 발달을 극도로 혐오했고, 결국 거칠고 폭력적이었던 옛 전통을 되찾기 위해 싸움을 시작한다. 그 앞을 가로막는 자는 누구든 볼리베어의 이빨과 발톱, 천둥의 위력을 맛보게 될 것이다."

