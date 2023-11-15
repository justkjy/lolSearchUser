package kr.co.lol.ui.component.championInfo.rotaion

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import kr.co.lol.R
import kr.co.lol.internet.champLoadingUrl
import kr.co.lol.internet.champTilesUrl
import kr.co.lol.ui.component.championInfoHeight
import kr.co.lol.ui.component.championInfoWith
import kr.co.lol.ui.theme.LolInfoViewerTheme
import kr.co.lol.ui.theme.Paddings
import java.lang.Math.abs

@Composable
fun ChampionImgCard(
    state : LazyListState,
    index : Int = 0,
    champinKorName : String = "아트룩스",
    champinEngName : String = "Aatrox",
) {
    val backgroundColor by remember {
        derivedStateOf {

            val layoutInfo = state.layoutInfo
            val visibleItemsInfo = layoutInfo.visibleItemsInfo
            val itemInfo = visibleItemsInfo.firstOrNull { it.index == index}

            itemInfo?.let {

                //First item
                if (itemInfo.index == 0 && itemInfo.offset == 0)
                    //return@derivedStateOf Red
                    return@derivedStateOf Red

                //Last item
                val viewportWidth = layoutInfo.viewportEndOffset + layoutInfo.viewportStartOffset
                if (itemInfo.index + 1 == layoutInfo.totalItemsCount &&
                    itemInfo.offset + itemInfo.size <= viewportWidth)
                    return@derivedStateOf Red

                //Other items
                val delta = 5
                val center = state.layoutInfo.viewportEndOffset / 2
                val childCenter = it.offset + it.size / 2
                val target = childCenter - center
                if (target in -delta..delta) return@derivedStateOf Red
            }
            Blue
        }
    }

//    val converColor = when(backgroundColor) {
//        Red -> Color.LightGray
//        else-> MaterialTheme.colorScheme.primary
//    }
    val converColor = Color.LightGray

    val scale by remember {
        derivedStateOf {
            val currentItem = state.layoutInfo.visibleItemsInfo.firstOrNull { it.index == index } ?: return@derivedStateOf 1.0f
            val halfRowWidth = state.layoutInfo.viewportSize.width/2
            (1f - minOf(1f, abs(currentItem.offset + (currentItem.size / 2) - halfRowWidth ).toFloat() / halfRowWidth) * 0.10f)
        }
    }

    Card(
        shape = MaterialTheme.shapes.large,
        modifier = Modifier
            .padding(Paddings.large)
            .width(championInfoWith)
            .height(championInfoHeight + 50.dp)
            .scale(scale)
            .zIndex(scale * 10),
    ){
        AsyncImage(
            model = champTilesUrl(champinEngName),
            contentScale = ContentScale.FillWidth,
            placeholder = painterResource(R.drawable.user_cowboy),
            contentDescription = "champinKorName",
            modifier = Modifier
                .size(championInfoWith, championInfoHeight)
        )
        Spacer(modifier = Modifier.padding(Paddings.small))
        Text(
            text = champinKorName,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            //color = MaterialTheme.colorScheme.primary,
            color = converColor,
            modifier = Modifier
                .padding(Paddings.small)
                .fillMaxWidth()
        )
    }
}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun championLarPreview() {
    LolInfoViewerTheme(true) {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.background

        ) {
            //ChampionImgCard()
        }
    }
}