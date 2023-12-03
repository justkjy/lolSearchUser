package kr.co.justkimlol.ui.component.championInfo.rotaion

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.snapping.SnapLayoutInfoProvider
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import kr.co.justkimlol.ui.component.button.ChampionList
import kr.co.justkimlol.ui.theme.LolInfoViewerTheme
import kr.co.justkimlol.ui.theme.Paddings

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ChampionRotationList(
    championList : MutableList<ChampionList> = mutableListOf()
) {
    Column(
        modifier = Modifier
            .padding(Paddings.large)
            .fillMaxWidth()
    ) {
        Text(
            text = "로테이션 챔피언",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleSmall,
        )

        val listChampion = mutableListOf<ChampionList>().also {
            if(championList.isEmpty()) {
                it.add(ChampionList("Mordekaiser", "모데카이저"))
                it.add(ChampionList("Malphite", "말파이트"))
                it.add(ChampionList("MonkeyKing", "오공"))
                it.add(ChampionList("Morgana", "모르가나"))
                it.add(ChampionList("Ornn", "오른"))
                it.add(ChampionList("Olaf", "올라프"))
                it.add(ChampionList("Yuumi", "유미"))
                it.add(ChampionList("Yasuo", "야스오"))
                it.add(ChampionList("Karthus", "카서스"))
                it.add(ChampionList("Karma", "카르마"))
            } else {
                it.addAll(championList)
            }
        }

        val state = rememberLazyListState()
        val snappingLayout = remember(state) { SnapLayoutInfoProvider(state) }
        val flingBehavior = rememberSnapFlingBehavior(snappingLayout)

        LazyRow(
            verticalAlignment = Alignment.CenterVertically,
            state = state,
            flingBehavior = flingBehavior
        ) {
            itemsIndexed(listChampion) { index, item ->
                ChampionImgCard(state, index,item.championKorName, item.championEngName)
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview
@Composable
fun ChampionRotationListPreview() {
    LolInfoViewerTheme(true) {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.background
        ) {
            ChampionRotationList()
        }
    }
}