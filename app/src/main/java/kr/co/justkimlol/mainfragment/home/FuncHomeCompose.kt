package kr.co.justkimlol.mainfragment.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kr.co.justkimlol.R
import kr.co.justkimlol.ui.component.button.LeadingIconData
import kr.co.justkimlol.ui.component.button.PrimaryButton
import kr.co.justkimlol.mainfragment.home.viewModel.ChampionInitViewModel
import kr.co.justkimlol.mainfragment.home.viewModel.PatchState
import kr.co.justkimlol.ui.theme.LolWhiteTheme
import kr.co.justkimlol.ui.theme.Paddings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LevelTop(
    viewModel: ChampionInitViewModel? = null,
) {
    var userId by rememberSaveable { mutableStateOf("")   }
    var apiKey by rememberSaveable { mutableStateOf("")   }

    userId  =  viewModel?.let {
        it.userId.observeAsState("").value
    }?:""


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.8f)
            .verticalScroll(rememberScrollState())
            .padding(Paddings.none)
            .background(Color.White)
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            modifier = Modifier
                .padding(Paddings.large)
                .background(Color.White)
        ) {

            Spacer(modifier = Modifier.padding(Paddings.extra))
            Row(
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "정보 입력",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.displaySmall,
                    modifier = Modifier.fillMaxSize()
                )
            }
            Spacer(modifier = Modifier.padding(Paddings.extra))

            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    Icons.Default.Person,
                    "소환사 이름",
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
                Spacer(modifier = Modifier.padding(Paddings.xsmall))
                OutlinedTextField(
                    value = userId,
                    textStyle = MaterialTheme.typography.titleSmall,
                    label = {
                        Text(
                            text = "ID"
                        )
                    },
                    onValueChange = {
                        userId = it
                        viewModel?.let { view ->
                            view.inputUserid(it)
                        }
                    },
                    modifier = Modifier
                        .padding(
                            start = Paddings.large,
                            end = Paddings.medium
                        )
                        .fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.padding(Paddings.large))
            Row(horizontalArrangement = Arrangement.Center) {
                Icon(
                    painter = painterResource(R.drawable.baseline_key_24),
                    contentDescription = "소환사 이름",
                    modifier = Modifier.align(Alignment.CenterVertically)
                )

                Spacer(modifier = Modifier.padding(Paddings.xsmall))


                val enableText = viewModel?.let { view ->
                    view.needApiKey.observeAsState(false).value
                }?: false

                OutlinedTextField(
                    value = apiKey,
                    textStyle = MaterialTheme.typography.titleSmall,
                    label = {
                        Text(
                            //style = MaterialTheme.typography.bodyMedium,
                            text = "DEVELOPMENT API KEY"
                        )
                    },
                    enabled = enableText,
                    onValueChange = {
                        apiKey = it
                        viewModel?.let { view ->
                            view.inputApiKey(it)
                        }
                    },

                    modifier = Modifier
                        .padding(
                            start = Paddings.large,
                            end = Paddings.medium
                        )
                        .fillMaxWidth()
                )
            }

            HyperlinkInSentenceExample()

            Spacer(modifier = Modifier.padding(Paddings.large))

            val patchProgessState = viewModel?.let { view ->
                view.patchProgressStep.observeAsState("").value
            } ?: ""

            // 패치 진행 정보
            Text(
                text = patchProgessState,
                color = Color.Red,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier
                    .fillMaxSize()
                    .height(60.dp)
            )

            //Box {
            val progress = viewModel?.let {
                it.championInfo.observeAsState(PatchState.LOADING).value
            }
            var clickAble = false
            if (progress == PatchState.COMPLETE) {
                clickAble = true
            } else if (progress == PatchState.ERROR) {
                println("error")
            }

            PrimaryButton(
                text = "LogIn",
                onClick = viewModel?.let {
                    it.setInputButtonCheck

                } ?: { },
                clickAble = clickAble,
                leadingIconData = LeadingIconData(R.drawable.warrior_helmet_2750),
            )
        }
    }
}


@Composable
fun HyperlinkInSentenceExample() {
    val sourceText = "Here development API KEY : "
    val hyperlinkText = "click here"
    val endText = "\nDo NOT use this API key in a publicly available product!"
    val uri = "https://developer.riotgames.com/"

    val annotatedString = buildAnnotatedString {
        append(sourceText)
        withStyle(style = SpanStyle(color = Color.Blue)) {
            append(hyperlinkText)
            addStringAnnotation(
                tag = "URL",
                annotation = uri,
                start = length - hyperlinkText.length,
                end = length
            )
        }
        append(endText)
    }
    val uriHandler = LocalUriHandler.current
    ClickableText(
        text = annotatedString,
        onClick = { offset ->
            annotatedString.getStringAnnotations(tag = "URL", start = offset, end = offset)
                .firstOrNull()?.let { annotation ->
                    uriHandler.openUri(annotation.item)
                }
        }
    )
}

@Preview
@Composable
fun PreviewDialogBox(){
    LolWhiteTheme() {
        LevelTop()
    }
}
