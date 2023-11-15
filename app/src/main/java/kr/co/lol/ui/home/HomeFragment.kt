package kr.co.lol.ui.home

import android.graphics.Color
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialogDefaults.containerColor
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kr.co.lol.R
import kr.co.lol.SharedViewModel
import kr.co.lol.databinding.FragmentHomeBinding
import kr.co.lol.internet.getConnectUrl
import kr.co.lol.internet.getJsonFailed
import kr.co.lol.internet.getJsonFileFromHttps
import kr.co.lol.internet.getJsonLoad
import kr.co.lol.internet.getJsonSuccess
import kr.co.lol.dataclass.ChampionRotationData
import kr.co.lol.internet.TAG
import kr.co.lol.internet.retrofit.GetJsonFromRerofit
import kr.co.lol.internet.retrofit.LolQueryUserName
import kr.co.lol.room.data.RoomHelper
import kr.co.lol.room.data.roomHelperValue
import kr.co.lol.ui.home.viewModel.ChampionInitViewModel
import kr.co.lol.ui.home.viewModel.PatchState
import kr.co.lol.ui.navigation.navFailState
import kr.co.lol.ui.navigation.navSuccessState
import kr.co.lol.ui.navigation.navWaitState
import kr.co.lol.ui.navigation.navcheckState
import kr.co.lol.ui.theme.LolWhiteTheme

class HomeFragment : Fragment() {

    // 로그인 viewModel
    private val viewHomeModel: ChampionInitViewModel by viewModels()
    // 공유 라이브 뷰
    private lateinit var sharedViewModel: SharedViewModel


    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    var helper: RoomHelper? = null

    var showErrorCode = 0
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        observeUiEffects()

        val fragmentContext = this.context ?.let { context ->
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
                viewHomeModel.feedState.collect { it ->
                    when(it) {
                        is getJsonFailed -> {
                            val message = it.error
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                            viewHomeModel.setChampionInfo(PatchState.ERROR)
                        }
                        is getJsonLoad -> {
                            Toast.makeText(context, "로딩", Toast.LENGTH_SHORT).show()
                            viewHomeModel.setChampionInfo(PatchState.PROGRESS)
                        }
                        is getJsonSuccess -> {
                            val message = it.msg
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
                        is navcheckState -> {
                            var apikey = viewHomeModel.apiKey.value!!
                            var userid = viewHomeModel.userId.value!!
                            if (userid.isEmpty()) {
                                userid = "dongk"
                            }
                            if (apikey.isEmpty()) {
                                apikey = "RGAPI-02b99080-b21d-4922-870a-94ce87bf6166"
                            }
                            sharedViewModel.inputApiKey(apikey)
                            sharedViewModel.inputUserId(userid)
                            searchUser(userid, apikey, viewHomeModel)
                        }
                        is navFailState -> {
                            viewHomeModel.setChangeCode(state.errorCode)
                            viewHomeModel.setInputWait()
                        }
                        is navSuccessState -> {
                            var apikey = viewHomeModel.apiKey.value!!


                            if (apikey.isEmpty()) {
                                apikey = "RGAPI-02b99080-b21d-4922-870a-94ce87bf6166"
                            }

                            rotationChamp(apikey)
                            view?.let { viewNav ->
                                viewNav.findNavController()
                                    .navigate(R.id.action_navigation_home_to_navigation_user)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun rotationChamp(apiKey: String)  {
        GlobalScope.launch(Dispatchers.IO) {
            val rotationChamp = getJsonFileFromHttps(
                "https://kr.api.riotgames.com/lol/platform/v3/champion-rotations?api_key=${apiKey}"
            )
            // error 대응
            val champ = Gson().fromJson(rotationChamp, ChampionRotationData::class.java)
            sharedViewModel.champRotations(champ.freeChampionIds)
        }
    }
}

suspend fun searchUser(userId : String, useApiKey: String, viewModel: ChampionInitViewModel) {

    val retrofitUserAPI = GetJsonFromRerofit.getInstance()

    try{
        val retrofitService = retrofitUserAPI.create(LolQueryUserName::class.java)
            .getUserInfo(userId, useApiKey)
        val response = retrofitService
        if(response.code() != 200 ) {
            viewModel.setNavState(navFailState(response.code()))
            return
        }

        GlobalScope.launch(Dispatchers.IO) {
            if (response.isSuccessful) {
                viewModel.setNavState(navSuccessState)
            } else {
                val errorCode = response.code()
                viewModel.setNavState(navFailState(errorCode))
            }
        }
    } catch (e: Exception) {
        Log.i(TAG, "errHomeFragment = ${e.printStackTrace()}")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageBox(view:ChampionInitViewModel = viewModel()) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val code = view.errorCode.observeAsState(0).value
    var text = ""
    if(code > 0) {
        text = viewCode(code)
    }
    Scaffold (
        modifier = Modifier.fillMaxWidth()
            .fillMaxHeight(),
        snackbarHost = { SnackbarHost(snackbarHostState) },

        content = { innerPadding ->
            //GlobalScope.launch(Dispatchers.IO) {
            Column(modifier = Modifier.fillMaxSize()
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
                    view.setCodeClear()
                }
            }
        }
    )
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





