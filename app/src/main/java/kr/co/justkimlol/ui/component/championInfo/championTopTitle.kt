package kr.co.justkimlol.ui.component.championInfo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RadialGradientShader
import androidx.compose.ui.graphics.Shader
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.AsyncImage
import kr.co.justkimlol.R
import kr.co.justkimlol.internet.champSkinUrl
import kr.co.justkimlol.internet.profileUrl
import kr.co.justkimlol.ui.theme.LolInfoViewerTheme
import kr.co.justkimlol.ui.theme.Paddings

@Composable
fun ChampionTopImg(
    profileId : Int = 5986,
    userName : String = "Just Kim",
    userTier : String = "GOLD",
    skillNum : Int = 150,
) {
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .padding(Paddings.none)
            .background(MaterialTheme.colorScheme.background)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Paddings.small)
        ) {
            ConstraintLayout(
                Modifier.fillMaxSize()
            ) {
                val (backleftImg, backrightImg, backColor) = createRefs()
                val (profileImg, levelImg, profileName, tearIcon) = createRefs()

                createHorizontalChain(profileName, tearIcon, chainStyle = ChainStyle.Packed)
                val barrierValue = createTopBarrier(profileName, tearIcon)
                
                // 배경 이미지
                //Row{
                    AsyncImage(
                        model = champSkinUrl("Ornn", 1),
                        contentScale = ContentScale.FillBounds,
                        placeholder = painterResource(R.drawable.warrior_helmet),
                        contentDescription = "유저 레벨",
                        modifier = Modifier
                            .aspectRatio(16f / 9f)

                            .constrainAs(backleftImg) {
                                top.linkTo(parent.top)
                                start.linkTo(parent.start, margin = 5.dp)
                                end.linkTo(backrightImg.start, margin = 5.dp)
                            }
                    )
                    AsyncImage(
                        model = champSkinUrl("Volibear", 1),
                        contentScale = ContentScale.FillHeight,
                        placeholder = painterResource(R.drawable.warrior_helmet),
                        contentDescription = "유저 레벨",
                        modifier = Modifier
                            .aspectRatio(16f / 9f)
                            .constrainAs(backrightImg) {
                                top.linkTo(parent.top)
                                start.linkTo(backleftImg.end, margin = 0.dp)
                                end.linkTo(parent.end, margin = 5.dp)
                            }
                    )
                //}

                val largeRadialGradient = object : ShaderBrush() {
                    override fun createShader(size: Size): Shader {
                        val biggerDimension = maxOf(size.height/2, size.width)
                        return RadialGradientShader(
                            colors = listOf(
                                Color.Black.copy(alpha = 1.0f),
                                Color.Black.copy(alpha = 0.5f),
                                Color.Black.copy(alpha = 0.0f)
                            ),
                            center = size.center,
                            radius = biggerDimension / 2f,
                            colorStops = listOf(0f, 0f, 0.95f)
                        )
                    }
                }

                val brush = Brush.radialGradient(listOf(
                    Color.Black,
                    Color.Black.copy(0.5f),
                    Color.White.copy(0.0f))
                )
                Box(
                    Modifier
                        .width(200.dp)
                        .height(200.dp)
                        //.background(largeRadialGradient)
                        .background(brush)
                        .padding(4.dp)
                        .constrainAs(backColor) {
                            top.linkTo(levelImg.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                ) { }

                AsyncImage(model = profileUrl(profileId),
                            contentDescription = null,
                            placeholder = painterResource(R.drawable.user_cowboy),
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .constrainAs(profileImg){
                                    top.linkTo(levelImg.top)
                                    bottom.linkTo(levelImg.bottom)
                                    start.linkTo(levelImg.start)
                                    end.linkTo(levelImg.end)

                                }
                    )

                AsyncImage(
                    model = skillLevelResource(skillNum),
                    contentDescription = null,
                    placeholder = painterResource(R.drawable.user_cowboy),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .constrainAs(levelImg){
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            bottom.linkTo(barrierValue, 10.dp)
                        }
                )


                Text(
                    text = userName,
                    modifier = Modifier
                        .constrainAs(profileName){
                            bottom.linkTo(parent.bottom, 10.dp)
                            end.linkTo(profileName.start, 10.dp)
                        }
                )

                AsyncImage(
                    model = tierResource(userTier),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                        .constrainAs(tearIcon) {
                            start.linkTo(profileName.end, 10.dp)
                            top.linkTo(profileName.top)
                        }
                )
            }

        }
    }
}

@Composable
@Preview
fun PreviewChampionTopImg() {
    LolInfoViewerTheme(true) {
        ChampionTopImg()
    }
}