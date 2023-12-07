package kr.co.justkimlol.mainfragment.user.usermatch

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemsIndexed
import co.yml.charts.common.model.Point
import co.yml.charts.ui.linechart.LineChart
import kotlinx.coroutines.flow.Flow
import kr.co.justkimlol.dataclass.UserMatchId
import kr.co.justkimlol.internet.TAG
import kr.co.justkimlol.viewModel.user.match.MatchChampViewModel
import kr.co.justkimlol.viewModel.user.match.MatchViewModel
import kr.co.justkimlol.ui.component.GameType
import kr.co.justkimlol.ui.component.championInfo.TierProfile
import kr.co.justkimlol.ui.component.circleprocess.CustomCircularProccess
import kr.co.justkimlol.ui.component.circleprocess.PreviewLineChart
import kr.co.justkimlol.ui.component.userInfo.userdata.RankInfo
import kr.co.justkimlol.ui.component.userInfo.userdata.UserLevel4
import kr.co.justkimlol.ui.theme.LolInfoViewerTheme
import kr.co.justkimlol.ui.theme.Paddings

class MatchActivity : ComponentActivity() {

    private lateinit var viewMatchModel : MatchViewModel
    private lateinit var viewChampionConditionModel : MatchChampViewModel

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        val userId = intent.getStringExtra("userId")
        val puuId = intent.getStringExtra("puuId")
        val apiKey = intent.getStringExtra("apiKey")
        val matchList = intent.getStringArrayListExtra("matchList")
        val tier = intent.getStringExtra("userTier")

        val profileId = intent.getIntExtra("profileId", 0)
        val skillNum = intent.getIntExtra("skillNum", 0)
        val topChamp1 = intent.getStringExtra("Top1")
        val topChamp2 = intent.getStringExtra("Top2")
        val topChamp3 = intent.getStringExtra("Top3")
        Log.i("TEST", "match = $matchList")
        viewMatchModel = ViewModelProvider(this)[MatchViewModel::class.java]
        Log.i("TEST", "apikey = $apiKey")
        viewMatchModel.setPageItem(matchList!!, apiKey ?: "")

        viewChampionConditionModel = ViewModelProvider(this)[MatchChampViewModel::class.java]
        viewChampionConditionModel.insertMost3Champ(topChamp1!!, topChamp2!!, topChamp3!!)

        setContent {
            LolInfoViewerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ItemsDemo(viewMatchModel.items,
                        puuId!!,
                        userId!!,
                        skillNum,
                        tier!!,
                        profileId,
                        topChamp1?: "",
                        topChamp2?: "",
                        topChamp3?: "",
                        viewChampionConditionModel.insertChampUpdate,
                    )
                }
            }
        }
    }
}


@Composable
fun ItemsDemo(
    flow: Flow<PagingData<UserMatchId>>,
    puuId: String,
    userName: String,
    skillNum: Int,
    tier: String,
    profileId: Int,
    champion1: String,
    champion2: String,
    champion3: String,
    insertChampUpdate: (index:Int, championEngName: String, win: Boolean, kill: Int, death: Int) -> Unit
) {
    val lazyPagingItems = flow.collectAsLazyPagingItems()
    LazyColumn {
        item {
            PreviewCustomCircular( userName, skillNum, tier, profileId,
                champion1,champion2, champion3)
        }

        itemsIndexed(lazyPagingItems) {index, matchBody ->
            matchBody?.let{ userMatchId->
                FuncshowMatech(index, userMatchId, puuId, insertChampUpdate)
            }
        }
    }
}

@SuppressLint("SuspiciousIndentation")
@Composable
fun PreviewCustomCircular(
    userName : String,
    skillNum : Int,
    tier : String,
    profileId : Int,
    champion1 : String,
    champion2 : String,
    champion3 : String,
    matchChampViewModel : MatchChampViewModel = viewModel()
) {

    val totalCount = matchChampViewModel.totalCountGame.observeAsState(0).value
    val totalWin = matchChampViewModel.totalWin.observeAsState(0).value
    val totalLose = matchChampViewModel.totalLose.observeAsState(0).value
    val champ1Win = matchChampViewModel.champ1WinCount.observeAsState(0).value
    val champ1Lose = matchChampViewModel.champ1LoseCount.observeAsState(0).value

    val champ2Win = matchChampViewModel.champ2WinCount.observeAsState(0).value
    val champ2Lose = matchChampViewModel.champ2LoseCount.observeAsState(0).value

    val champ3Win = matchChampViewModel.champ3WinCount.observeAsState(0).value
    val champ3Lose = matchChampViewModel.champ3LoseCount.observeAsState(0).value

    val killInfoList : List<Point>
    = matchChampViewModel.killInfoChart.observeAsState(mutableListOf(Point(0f, 0f))).value

        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(alignment = Alignment.CenterHorizontally)
            ) {
                var maxWidth = 0.5f
                if (userName.length > 16) {
                    maxWidth = 0.7f
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth(maxWidth)
                ) {
                    TierProfile(
                        modifier = Modifier.height(100.dp)
                                            .fillMaxWidth(),
                        profileId = profileId,
                        userName = userName,
                        userTier = tier,
                        skillNum = skillNum
                    )
                }

                Spacer(modifier = Modifier.padding(Paddings.small))
                Column {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.height(100.dp)
                    ) {
                        Spacer(modifier = Modifier.padding(Paddings.medium))
                        Row {
                            Text(text = "COUNT",
                                color = Color.Gray,
                                style = MaterialTheme.typography.titleSmall,
                                modifier = Modifier
                                    .padding(Paddings.small)
                            )

                            Text(text = "$totalCount",
                                color = Color.White,
                                style = MaterialTheme.typography.titleSmall,
                                modifier = Modifier
                                    .padding(Paddings.small)
                            )
                        }

                        Row {
                            Text(text = "WIN", color = Color.Gray,
                                style = MaterialTheme.typography.titleSmall,
                                modifier = Modifier
                                    .padding(Paddings.small)
                            )

                            Text(text = "$totalWin", color = Color.Blue,
                                style = MaterialTheme.typography.titleSmall,
                                modifier = Modifier
                                    .padding(Paddings.small)
                            )
                        }
                        Row {
                            Text(text = "LOSE", color = Color.Gray,
                                style = MaterialTheme.typography.titleSmall,
                                modifier = Modifier
                                    .padding(Paddings.small)
                            )

                            Text(text = "$totalLose", color = Color.Red,
                                style = MaterialTheme.typography.titleSmall,
                                modifier = Modifier
                                    .padding(Paddings.small)
                            )
                        }
                    }
                }
            }

            Card(
                shape = RoundedCornerShape(0.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(MaterialTheme.colorScheme.background)
            ) {
                Log.i(TAG, "point = $killInfoList")
                LineChart(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.background)
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    lineChartData = PreviewLineChart(killInfoList)
                )
            }

            Row {
                // champion1
                CustomCircularProccess(
                    modifier = Modifier
                        .size(130.dp)
                        .background(MaterialTheme.colorScheme.background)
                    ,
                    primaryColor = Color.Blue,
                    secondaryColor = Color.Red,
                    circleRadius = 100f,
                    championEngName = champion1,
                    winValue = champ1Win,
                    loseValue = champ1Lose,
                    onPositionChange = {

                    }
                )
                // chmapion2
                CustomCircularProccess(
                    modifier = Modifier
                        .size(130.dp)
                        .background(MaterialTheme.colorScheme.background)
                    ,
                    primaryColor = Color.Blue,
                    secondaryColor = Color.Red,
                    circleRadius = 100f,
                    championEngName = champion2,
                    winValue = champ2Win,
                    loseValue = champ2Lose,
                    onPositionChange = {

                    }
                )
                // champion3
                CustomCircularProccess(
                    modifier = Modifier
                        .size(130.dp)
                        .background(MaterialTheme.colorScheme.background)
                    ,
                    primaryColor = Color.Blue,
                    secondaryColor = Color.Red,
                    circleRadius = 100f,
                    championEngName = champion3,
                    winValue = champ3Win,
                    loseValue = champ3Lose,
                    onPositionChange = {
                    }
                )
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
            "AH3tjkvRgXPgrUEaKIeZgVJcJeRKYJFiX27RXVs4yvZuF5GqueBBY7oL4SHci2RM9LdTPW5FsL3XhQ",
            "RGAPI-a2161692-7fdf-4e6c-9d99-e3594f07fa24",
            RankInfo("GOLD", "IV", 394, 5986, listOf(
                "Maokai", "Ornn", "Galio", "Volibear")
            ),
            listOf(
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
