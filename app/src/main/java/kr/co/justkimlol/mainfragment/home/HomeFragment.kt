package kr.co.justkimlol.mainfragment.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import com.google.gson.Gson
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kr.co.justkimlol.R
import kr.co.justkimlol.viewModel.SharedViewModel
import kr.co.justkimlol.dataclass.ChampionRotationData
import kr.co.justkimlol.internet.TAG
import kr.co.justkimlol.internet.retrofit.GetJsonFromRetrofit
import kr.co.justkimlol.internet.retrofit.LolQueryGameName
import kr.co.justkimlol.room.data.RoomHelper
import kr.co.justkimlol.room.data.roomHelperValue
import kr.co.justkimlol.viewModel.home.ChampionInitViewModel
import kr.co.justkimlol.viewModel.user.DataStoreViewModel
import kr.co.justkimlol.viewModel.home.PatchState
import kr.co.justkimlol.ui.component.homeInfo.LevelTop
import kr.co.justkimlol.ui.navigation.navCheckState
import kr.co.justkimlol.ui.navigation.navFailState
import kr.co.justkimlol.ui.navigation.navSuccessState
import kr.co.justkimlol.ui.navigation.navWaitState
import kr.co.justkimlol.ui.theme.LolWhiteTheme
import kr.co.justkimlol.internet.getConnectUrl
import kr.co.justkimlol.internet.getJsonFailed
import kr.co.justkimlol.internet.getJsonFileFromHttps
import kr.co.justkimlol.internet.getJsonLoad
import kr.co.justkimlol.internet.getJsonSuccess


class HomeFragment : Fragment() {

    // 로그인 viewModel
    private val viewHomeModel: ChampionInitViewModel by viewModels()
    private lateinit var storeViewModel : DataStoreViewModel

    private var helper: RoomHelper? = null
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       // loadUserId
        storeViewModel = ViewModelProvider(this)[DataStoreViewModel::class.java]

        observeUiEffects()

        var searchUserName = ""
        var searchTagLine = ""

        storeViewModel.readUserName.observe(this, Observer{
            searchUserName = it.toString()
            viewHomeModel.inputUserid(searchUserName)
        })
        storeViewModel.readTagLine.observe(this, Observer{
            searchTagLine = it.toString()
            viewHomeModel.inputTagLine(searchTagLine)
        })

        this.context ?.let { context ->
            helper = roomHelperValue(context)
            this
        }

        getConnectUrl(helper!!,
            viewHomeModel.settingStatus,
            viewHomeModel.patchProgressValue
        )

        return ComposeView(requireContext()).apply {
            setContent {
                LolWhiteTheme() {
                    Column {
                        LevelTop(viewHomeModel)
                        MessageBox()
                    }
                }
            }
        }
    }

    private fun observeUiEffects() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewHomeModel.feedState.collect { 
                    when(it) {
                        is getJsonFailed -> {
                            val message = it.error
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                            viewHomeModel.setChampionInfo(PatchState.ERROR)
                        }
                        is getJsonLoad -> {
                            Toast.makeText(context, "사용 준비중입니다. ", Toast.LENGTH_SHORT).show()
                            viewHomeModel.setChampionInfo(PatchState.PROGRESS)
                        }
                        is getJsonSuccess -> {
                            viewHomeModel.setChampionInfo(PatchState.COMPLETE)
                        }
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewHomeModel.naviState.collect { state->
                    when (state) {
                        is navWaitState -> {

                        }
                        is navCheckState -> {
                            var apiKey = viewHomeModel.apiKey.value!!
                            var userid = viewHomeModel.userId.value!!
                            var tagLine = viewHomeModel.tagLine.value!!

                            if (userid.isEmpty()) {
                                userid = "dongk"
                            }

                            if (tagLine.isEmpty()) {
                                tagLine = "KR1"
                            }

                            if (apiKey.isEmpty()) {
                                apiKey = "RGAPI-a2161692-7fdf-4e6c-9d99-e3594f07fa24"
                            }

                            viewHomeModel.inputUserid(userid)
                            viewHomeModel.inputTagLine(tagLine)
                            viewHomeModel.inputApiKey(apiKey)

                            searchUser(userid, tagLine, apiKey, viewHomeModel)
                        }
                        is navFailState -> {
                            viewHomeModel.setChangeCode(state.errorCode)
                            viewHomeModel.setInputWait()
                        }
                        is navSuccessState -> {
                            // 검색한 아이디 저장
                            storeViewModel.insert(viewHomeModel.userId.value!!, viewHomeModel.tagLine.value!!)

                            val puuid = viewHomeModel.puuid.value!!
                            val userId = viewHomeModel.userId.value!!
                            val tagLine = viewHomeModel.tagLine.value!!
                            val apiKey = viewHomeModel.apiKey.value!!
                            val bundle = bundleOf(
                                "userId" to userId,
                                "puuid" to puuid,
                                "tagLine" to tagLine,
                                "apiKey" to apiKey
                            )

                            view?.findNavController()?.navigate(
                                R.id.action_navigation_home_to_navigation_user,
                                bundle)
                        }
                    }
                }
            }
        }
    }
}

@OptIn(DelicateCoroutinesApi::class)
suspend fun searchUser(userId : String, tagLine: String, useApiKey: String, viewModel: ChampionInitViewModel) {
    val retrofitUserAPI = GetJsonFromRetrofit.getInstanceAsia()

    try{
        val retrofitService = retrofitUserAPI.create(LolQueryGameName::class.java)
            .getGameUserInfo(userId, tagLine, useApiKey)
        if(retrofitService.code() != 200 ) {
            viewModel.setNavState(navFailState(retrofitService.code()))

            if(retrofitService.code() == 403) {
                viewModel.inputNeedApiKey(true)
            }
            return
        }

        GlobalScope.launch(Dispatchers.IO) {
            if (retrofitService.isSuccessful) {
                val gameName = retrofitService.body()!!
                viewModel.inputpuuidLine(gameName.puuid)
                viewModel.setNavState(navSuccessState)
            } else {
                val errorCode = retrofitService.code()
                viewModel.setNavState(navFailState(errorCode))
            }
        }
    } catch (e: Exception) {
        Log.i(TAG, "errHomeFragment = ${e.printStackTrace()}")
    }
}

@Composable
fun MessageBox(view: ChampionInitViewModel = viewModel()) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val code = view.errorCode.observeAsState(0).value
    var text = ""
    if(code > 0) {
        text = viewCode(code)
    }
    Scaffold (
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.5f),


        snackbarHost = { SnackbarHost(snackbarHostState) },

        content = { innerPadding ->
            //GlobalScope.launch(Dispatchers.IO) {
            Column(modifier = Modifier
                .fillMaxSize()
                .background(androidx.compose.ui.graphics.Color.White)
            ) {

            }
            scope.launch {
                if (text.isNotEmpty()) {
                    val result = snackbarHostState.showSnackbar( // 스낵바 결과 받기 가능
                        message = text,
                        actionLabel = "close",
                        duration = SnackbarDuration.Long
                    )
                    view.setCodeClear()
                }
            }
        }
    )
    Column(
        Modifier
            .fillMaxSize()
            .background(color = androidx.compose.ui.graphics.Color.White)
    ) {   }
}

enum class ErrorCode (
    val msg : String
) {
    CODE400("Bad request"),
    CODE401("Unauthorized : "),
    CODE403("Forbidden : api key가 없습니다. 위 링크 버튼으로 API 생성 하세요"),
    CODE404("Data not found : 찾는 아이디가 없습니다."),
    CODEERROR("연결 에러")
}

fun viewCode (code : Int) = when(code) {
    400 -> ErrorCode.CODE400.msg
    401 -> ErrorCode.CODE401.msg
    403 -> ErrorCode.CODE403.msg
    404 -> ErrorCode.CODE404.msg
    -1 -> ErrorCode.CODEERROR.msg
    else -> "other"
}





