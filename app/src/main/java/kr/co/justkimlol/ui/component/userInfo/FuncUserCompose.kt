package kr.co.justkimlol.ui.component.userInfo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import kr.co.justkimlol.ui.component.userInfo.userdata.UserLolCard
import kr.co.justkimlol.ui.component.userInfo.userdata.UserTFTCard
import kr.co.justkimlol.mainfragment.home.viewCode
import kr.co.justkimlol.ui.theme.LolInfoViewerTheme
import kr.co.justkimlol.ui.theme.Paddings
import kr.co.justkimlol.viewModel.user.UserViewModel

@Composable
fun UserTop(view : UserViewModel = viewModel()){


    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var text = ""
    val code = view.userFailCode.observeAsState(0).value

    if(code > 0) {
        text = viewCode(code)
    }

    Scaffold (
        Modifier.fillMaxHeight(0.9f),
        snackbarHost = {
                            SnackbarHost(
                                hostState = snackbarHostState,
                                modifier = Modifier.padding(bottom = 50.dp)
                            )
                       },
        content = { innerPadding ->
            Column(
                modifier = Modifier.fillMaxHeight(0.9f)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
            ) {
                SearchIdWithTagLine("")

                LazyColumn {
                    item {
                        UserLolInfo()
                    }
                    item {
                        UserTftInfo()
                    }
                }
                scope.launch {
                    if (text.isNotEmpty()) {
                        val result = snackbarHostState.showSnackbar( // 스낵바 결과 받기 가능
                            message = text,
                            actionLabel = "close",
                            duration = SnackbarDuration.Long
                        )
                        //viewModel?.let{it.setChangeCode(0)}
                        // 스낵바 결과
                        when (result) {
                            SnackbarResult.Dismissed -> {
                                // 스낵바 닫기
                            }

                            SnackbarResult.ActionPerformed -> {
                                // 동작 수행
                            }
                        }
                        view.setUserCodeClear()
                    }
                }
            }


        }
    )
}

@Composable
fun UserLolInfo() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ){
        Text(
            text = "리그 오브 레전드",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .padding(Paddings.medium)
                .padding(top = 10.dp)
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
        )
        UserLolCard()
    }
}

@Composable
fun UserTftInfo() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ){
        Text(
            text = "전략적 팀 전투",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .padding(Paddings.medium)
                .padding(top = 10.dp)
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
        )
        UserTFTCard()
    }
}

@Preview
@Composable
fun PreviewUserInfo() {
    LolInfoViewerTheme(true) {
        UserTop()
    }
}