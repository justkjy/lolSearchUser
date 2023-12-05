package kr.co.justkimlol.ui.component.userInfo.userdata

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import kr.co.justkimlol.R
import kr.co.justkimlol.mainfragment.home.internet.champUrl
import kr.co.justkimlol.ui.component.startPadding
import kr.co.justkimlol.ui.theme.LolInfoViewerTheme
import kr.co.justkimlol.ui.theme.Paddings


@Composable
fun ChampTopList(champList:MutableList<String>) {
        LazyRow(
            modifier = Modifier.padding(start = startPadding, end = startPadding)
                .fillMaxWidth()
        ){
            items(champList) { item ->
                AsyncImage(
                    model = champUrl(item),
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(R.drawable.warrior_helmet),
                    contentDescription = item,
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(40.dp)
                )
                Spacer(modifier = Modifier.padding(Paddings.medium))
            }
        }
}

@Composable
fun TftDackTopList(dackList:MutableList<Int>) {
    LazyRow(
        modifier = Modifier.padding(start = startPadding, end = startPadding)
            .fillMaxWidth()
    ){
        items(dackList) { item ->
            AsyncImage(
                model = item,
                contentScale = ContentScale.Crop,
                contentDescription = "",
                modifier = Modifier
                    .clip(CircleShape)
                    .size(40.dp)
            )
            Spacer(modifier = Modifier.padding(Paddings.medium))
        }
    }
}

@Preview
@Composable
fun ChampTopListPreview() {
    LolInfoViewerTheme(darkTheme = true) {
        UserInfo()
    }
}