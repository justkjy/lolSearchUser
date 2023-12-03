package kr.co.justkimlol.ui.component.homeInfo

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
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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

@Composable
fun LevelTop(
    viewModel: ChampionInitViewModel? = null,
) {
    var userId by rememberSaveable { mutableStateOf("")   }
    var tagLine by rememberSaveable { mutableStateOf("")   }

    var apiKey by rememberSaveable { mutableStateOf("")   }

    userId  =  viewModel?.userId?.observeAsState("")?.value ?:""
    tagLine  =  viewModel?.tagLine?.observeAsState("")?.value ?:""

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
            val iconColumn = 0.07f
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    Icons.Default.Person,
                    "소환사 이름",
                    modifier = Modifier.align(Alignment.CenterVertically)
                        .fillMaxWidth(iconColumn)
                )

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
                        viewModel?.inputUserid(it)
                    },
                    modifier = Modifier
                        .padding(
                            start = Paddings.large,
                            end = Paddings.medium
                        )
                        .fillMaxWidth()
                )
            }

            Row {
                Text(
                    text = "",
                    modifier = Modifier.fillMaxWidth(iconColumn)
                )
                OutlinedTextField(
                    value = tagLine,
                    textStyle = MaterialTheme.typography.titleSmall,
                    label = {
                        Text(
                            text = "TAG",
                            color = Color.Gray
                        )
                    },
                    leadingIcon = {
                        Text(
                            text = "#",
                            style = MaterialTheme.typography.headlineSmall,
                        )
                    },
                    onValueChange = {
                        tagLine = it
                        viewModel?.inputTagLine(tagLine)
                    },
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                tagLine = "KR1"
                                viewModel?.inputTagLine(tagLine)
                            }) {
                            Icon(
                                Icons.Filled.Clear,
                                contentDescription = "필드 지우기"
                            )
                        }
                    },
                    modifier = Modifier
                        .padding(
                            start = Paddings.large,
                            end = Paddings.medium
                        )
                        .fillMaxWidth(0.6f)
                )
                Text(
                    text = "기본 테그 KR1",
                    color = Color.Gray,
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.fillMaxSize()
                        .align(alignment = Alignment.Bottom)
                )
            }

            Spacer(modifier = Modifier.padding(Paddings.large))
            Row(horizontalArrangement = Arrangement.Center) {
                Icon(
                    painter = painterResource(R.drawable.baseline_key_24),
                    contentDescription = "소환사 이름",
                    modifier = Modifier.align(Alignment.CenterVertically)
                        .fillMaxWidth(iconColumn)
                )

                val enableText = viewModel?.needApiKey?.observeAsState(false)?.value ?: false
                OutlinedTextField(
                    value = apiKey,
                    textStyle = MaterialTheme.typography.titleSmall,
                    label = {
                        Text(
                            text = "DEVELOPMENT API KEY"
                        )
                    },
                    enabled = enableText,
                    onValueChange = {
                        apiKey = it
                        viewModel?.inputApiKey(it)
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

            val patchProgressState = viewModel?.patchProgressStep?.observeAsState("")?.value ?: ""

            // 패치 진행 정보
            Text(
                text = patchProgressState,
                color = Color.Red,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier
                    .fillMaxSize()
                    .height(60.dp)
            )

            val progress = viewModel?.championInfo?.observeAsState(PatchState.LOADING)?.value
            var clickAble = false
            if (progress == PatchState.COMPLETE) {
                clickAble = true
            } else if (progress == PatchState.ERROR) {
                println("error")
            }

            PrimaryButton(
                text = "LogIn",
                onClick = viewModel?.setInputButtonCheck ?: { },
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
