package kr.co.lol.ui.usermatch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kr.co.lol.dataclass.UserMatchId
import kr.co.lol.ui.component.GameType
import kr.co.lol.ui.component.userInfo.userdata.UserLevel4
import kr.co.lol.ui.theme.LolInfoViewerTheme

class MatchActivity : ComponentActivity() {

    lateinit var viewMatchModel: MatchViewModel

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)



        val userId = intent.getStringExtra("userId")
        val apiKey = intent.getStringExtra("apiKey")
        val matchList = intent.getStringArrayListExtra("matchList")

        viewMatchModel = ViewModelProvider(this).get(MatchViewModel::class.java)
        viewMatchModel.setPageItem(matchList!!, apiKey?.let { it }?: "")

        setContent {
            LolInfoViewerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ItemsDemo(viewMatchModel.items)
                }
            }
        }
    }
}

@Composable
fun ItemsDemo(flow: Flow<PagingData<UserMatchId>>) {
    val lazyPagingItems = flow.collectAsLazyPagingItems()
    LazyColumn {
        items(lazyPagingItems) { matchBody ->
            matchBody?.let{ userMatchId->
                Text("${userMatchId.info.gameStartTimestamp}")
            }
        }
    }
}





@Preview(showBackground = true)
@Composable
fun MatchPreview() {
    LolInfoViewerTheme {
        UserLevel4(
            GameType.LOL,
            "저스트킴",
            "RGAPI-d9bfe5ea-ae70-416d-a42b-1613893e45b4",
            listOf<String>(
                "KR_6806621897",
                "KR_6806502831",
                "KR_6806365513",
                "KR_6749075990",
                "KR_6749000172",
                "KR_6708042078",
                "KR_6707730519",
                "KR_6707683724",
                "KR_6707635707",
                "KR_6705539190",
                "KR_6705447645",
                "KR_6705390934",
                "KR_6700712009",
                "KR_6700684506",
                "KR_6700663863",
                "KR_6700651201",
                "KR_6696807628",
                "KR_6694972685",
                "KR_6694241809",
                "KR_6693198181",
                "KR_6693153372",
                "KR_6693123084",
                "KR_6693097519",
                "KR_6693074998",
                "KR_6690729450",
                "KR_6690082657",
                "KR_6690086005",
                "KR_6686188083",
                "KR_6685971323",
                "KR_6684358773",
                "KR_6684333980",
                "KR_6682254718",
                "KR_6682243027",
                "KR_6682226087",
                "KR_6681511239",
                "KR_6681455038",
                "KR_6681401464",
                "KR_6681360301",
                "KR_6680852168",
                "KR_6680836090",
                "KR_6680811294",
                "KR_6680794029",
                "KR_6680579733",
                "KR_6679462879",
                "KR_6679457217",
                "KR_6677990864",
                "KR_6677970066",
                "KR_6677755516",
                "KR_6675014021",
                "KR_6674983216",
                "KR_6674960996",
                "KR_6674712482",
                "KR_6669323735",
                "KR_6667817793",
                "KR_6667810905",
                "KR_6667789673",
                "KR_6666735273",
                "KR_6666708344",
                "KR_6666316154",
                "KR_6666296692",
                "KR_6665735695",
                "KR_6665688776",
                "KR_6665413566",
                "KR_6665337171",
                "KR_6665311158",
                "KR_6665226867",
                "KR_6664837495",
                "KR_6664603176",
                "KR_6664559400",
                "KR_6664503453",
                "KR_6663377146",
                "KR_6663362515",
                "KR_6663354340",
                "KR_6663344498",
                "KR_6663220261",
                "KR_6661405430",
                "KR_6661377124",
                "KR_6661316810",
                "KR_6659206373",
                "KR_6659165548",
                "KR_6659089239",
                "KR_6657322397",
                "KR_6656982565",
                "KR_6656960956",
                "KR_6656914431",
                "KR_6656856305",
                "KR_6656812515",
                "KR_6656643542",
                "KR_6656595413",
                "KR_6656121025",
                "KR_6656075994",
                "KR_6656006649",
                "KR_6655972377",
                "KR_6655910399",
                "KR_6655109948",
                "KR_6655077293",
                "KR_6655054348",
                "KR_6650683042",
                "KR_6650269412",
                "KR_6648653200"
        )
        )
    }
}
