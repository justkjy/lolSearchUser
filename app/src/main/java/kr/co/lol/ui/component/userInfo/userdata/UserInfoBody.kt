package kr.co.lol.ui.component.userInfo.userdata

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kr.co.lol.ui.theme.LolInfoViewerTheme
import kr.co.lol.ui.theme.Paddings

@Composable
fun UserInfo(){
    Column {
        Text(
            text = "리그 오브 레전드",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(Paddings.medium)
                .padding(top = 10.dp)
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
        )
        UserLolCard()

        Text(
            text = "전략적 팀 전투",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(Paddings.medium)
                .padding(top = 10.dp)
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
        )
        UserTFTCard()
    }
}




@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserInfoPreview() {
    LolInfoViewerTheme(true) {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.background
        ) {
            UserInfo()
        }
    }
}