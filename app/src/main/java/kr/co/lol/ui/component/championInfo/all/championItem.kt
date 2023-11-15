package kr.co.lol.ui.component.championInfo.all

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.AsyncImage
import kr.co.lol.R
import kr.co.lol.internet.TAG
import kr.co.lol.internet.champUrl
import kr.co.lol.ui.component.championAllItemHeight
import kr.co.lol.ui.component.championInfo.champTagResource
import kr.co.lol.ui.theme.LolInfoViewerTheme

@Composable
fun ChampionItem(
    index: Int = 0,
    champEngName: String = "Aatrox",
    champKorName: String = "아트룩스",
    champTitle: String = "다르킨의 검",
    onClick: (Boolean, Int) -> (Unit) = {_,_->},
    positionList : MutableList<String> = mutableListOf<String>("Support", "Tank"),
) {
    OutlinedCard(
        shape = MaterialTheme.shapes.extraLarge,

        modifier = Modifier
            .fillMaxWidth()
            .height(championAllItemHeight)
            .border(
                width = 2.dp,
                color = Color.LightGray,
                shape = MaterialTheme.shapes.extraLarge
            )
    ) {
        ConstraintLayout  {
            val (champImgAllItem, buttonDetail,  champKorNameAllItem, champTitleItem) = createRefs()
            val (tagItem1, tagItem2) = createRefs()

            Box(
                Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.horizontalGradient(
                            listOf(
                                Color.DarkGray,
                                Color.Black
                            )
                        )
                    )
                    .padding(4.dp)
            ) { }

            if(champEngName == "Alistar") {
                positionList.get(0)
            }

            if(positionList.size > 0) {
                val position = champTagResource(positionList)
                AsyncImage(
                    model = position.get(0),
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(R.drawable.warrior_helmet),
                    contentDescription = "포지션",
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(25.dp)
                        .constrainAs(tagItem1) {
                            top.linkTo(parent.top)
                            end.linkTo(parent.end, margin = 20.dp)
                            bottom.linkTo(parent.bottom)
                        }
                )

                if(position.size > 1) {
                    AsyncImage(
                        model = position.get(1),
                        contentScale = ContentScale.Crop,
                        placeholder = painterResource(R.drawable.warrior_helmet),
                        contentDescription = "포지션",
                        modifier = Modifier
                            .clip(CircleShape)
                            .size(25.dp)
                            .constrainAs(tagItem2) {
                                top.linkTo(parent.top)
                                end.linkTo(parent.end, margin = 20.dp)
                                bottom.linkTo(parent.bottom)
                            }
                    )
                    createVerticalChain(tagItem1, tagItem2, chainStyle =  ChainStyle.Spread)
                }


            }



            Button(
                onClick = {
                          onClick(true, index)
                },
                modifier = Modifier.constrainAs(buttonDetail){
                    top.linkTo(champImgAllItem.top)
                    start.linkTo(champImgAllItem.start)
                    bottom.linkTo(champImgAllItem.bottom)
                    end.linkTo(champImgAllItem.end)
                }
            ) {
                
            }

            AsyncImage(
                model = champUrl(champEngName),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(R.drawable.warrior_helmet),
                contentDescription = "유저 레벨",
                modifier = Modifier
                    .clip(CircleShape)
                    .size(50.dp)
                    .constrainAs(champImgAllItem) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start, margin = 5.dp)
                        bottom.linkTo(parent.bottom)
                    }
            )

            Text(
                text = champEngName,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier
                    .constrainAs(champTitleItem) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end, margin = 10.dp)
                    }
            )

            Text(
                text = champTitle,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White,
                modifier = Modifier
                    .constrainAs(champKorNameAllItem) {
                        top.linkTo(champTitleItem.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end, margin = 10.dp)
                    }
            )

            createVerticalChain(champTitleItem, champKorNameAllItem, chainStyle =  ChainStyle.Spread)
        }

    }
}


@Composable
@Preview
fun ChampionItemPreview() {
    LolInfoViewerTheme(true) {
        ChampionItem()
    }
}