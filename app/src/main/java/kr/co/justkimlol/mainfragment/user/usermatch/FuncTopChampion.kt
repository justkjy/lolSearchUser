package kr.co.justkimlol.mainfragment.user.usermatch

import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import kr.co.justkimlol.ui.theme.LolInfoViewerTheme

class champScoreViewModel : ViewModel() {
    private var champId: Int = 0;
    private var champEngName: String = ""
    private var champKorName : String = ""
    private var champScore: Int = 0
    private var winCount : Int = 0
    private var loseCount : Int = 0

    fun addWinCount(win: Int) {
        winCount += win
    }

    fun addLoseCount(lose: Int) {
        loseCount += lose
    }

    val win : Int
        get() = winCount

    val lose : Int
        get() = loseCount

}

@Composable
fun funcTopChampion(view : champScoreViewModel =  viewModel()) {
    var progress by remember { mutableStateOf(0.5f) }

    CircularProgressIndicator(
        progress = progress,
        modifier = Modifier.size(100.dp),
        color = Color.Magenta,
        strokeWidth = 7.dp
    )
}

@Composable
@Preview
fun PreviewfuncTopChamp() {
    LolInfoViewerTheme {
        funcTopChampion()
    }
}