package kr.co.justkimlol.mainfragment.user

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kr.co.justkimlol.SharedViewModel
import kr.co.justkimlol.databinding.FragmentUserBinding
import kr.co.justkimlol.dataclass.UserRankInfo
import kr.co.justkimlol.internet.TAG
import kr.co.justkimlol.internet.retrofit.GetJsonFromRetrofit.Companion.getInstance
import kr.co.justkimlol.internet.retrofit.GetJsonFromRetrofit.Companion.getInstanceAsia
import kr.co.justkimlol.internet.retrofit.LolQueryGameName
import kr.co.justkimlol.internet.retrofit.LolQueryLank
import kr.co.justkimlol.internet.retrofit.LolQueryMatchList
import kr.co.justkimlol.internet.retrofit.LolQueryTopChamp
import kr.co.justkimlol.internet.retrofit.LolQueryUserIdUsePuuid
import kr.co.justkimlol.internet.userInfoFail
import kr.co.justkimlol.internet.userInfoGetLoading
import kr.co.justkimlol.internet.userInfoSuccess
import kr.co.justkimlol.internet.userStepMsg
import kr.co.justkimlol.mainfragment.user.viewModel.UserViewModel
import kr.co.justkimlol.room.data.RoomHelper
import kr.co.justkimlol.room.data.roomHelperValue
import kr.co.justkimlol.ui.component.userInfo.UserTop
import kr.co.justkimlol.ui.theme.LolInfoViewerTheme

var appActivityViewModel : SharedViewModel? = null

class UserFragment : Fragment() {
    private var _binding: FragmentUserBinding? = null

    // This property is only valid between onCreateView and
    private lateinit var useApiKey: String

    // 활성화 하자.
    private val userViewModel: UserViewModel by viewModels()
    // 공유 라이브 뷰
    private lateinit var sharedViewModel: SharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        appActivityViewModel = sharedViewModel

        useApiKey = sharedViewModel.apiKey.value!!


        // 뷰처리가 끝나면 시작하자. check
        runRetrofit()
        observeUiEffects()

        return ComposeView(requireContext()).apply {
            setContent {
                LolInfoViewerTheme(false) {
                    UserTop()
                }
            }
        }
    }

    private fun observeUiEffects() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                userViewModel.stepUserState.collect { stepState ->
                    when(stepState) {
                        is userInfoGetLoading -> {}
                        is userStepMsg -> {
                            val userId = stepState.userId
                            val tagLine = stepState.tagLine
                            runUserSearch(userId, tagLine)
                        }
                        is userInfoSuccess -> {
                            Log.i("TEST", "Success")
                            insertUserInfo(stepState.userId)
                        }
                        is userInfoFail -> {
                            userViewModel.setUserInputErrCode(stepState.code)
                            userViewModel.setUserInit()
                        }
                    }
                }
            }
        }
    }

    private fun insertUserInfo(userId : String){
        var helper: RoomHelper?
        val champEngList = mutableListOf<String>()
        this.context ?.let { context ->
            helper = roomHelperValue(context)
            val lolInfoDb = helper!!.roomMemoDao()

            for(champKey in userViewModel.champTopList){
                val list =  lolInfoDb.getChampItem(champKey)
                if(list.isNotEmpty()) {
                    val eng = list.first().nameEng
                    champEngList.add(eng)
                }
            }
            this
        }

        sharedViewModel.sharedInputUserInfo(
            userId,
            userViewModel.puuid,
            userViewModel.profileIconId,
            userViewModel.summonerLevel,
            userViewModel.loltear,
            userViewModel.lolrank,
            userViewModel.lolWin,
            userViewModel.lolLosses,
            userViewModel.champTopList,
            champEngList,
            userViewModel.matchList

        )
    }

    private fun runRetrofit() {
        val puuid = sharedViewModel.puuid.value!!
        val userId = sharedViewModel.userId.value!!
        val tagLine = sharedViewModel.tagLine.value!!
        runPuuidSearch(puuid, userId, tagLine)
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun runPuuidSearch(puuid : String, userId: String, tagLine: String) {
        try {
            GlobalScope.launch(Dispatchers.IO) {
                puuidRetrofit(puuid, userId, tagLine)
            }
        }  catch (e: Exception) {
            Log.i(TAG, "err1 = ${e.printStackTrace()}")
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun runUserSearch(useId: String, tagLine: String) {
        try {
                GlobalScope.launch(Dispatchers.IO) {
                    userRetrofit(useId, tagLine)
                }
        }  catch (e: Exception) {
            Log.i(TAG, "err1 = ${e.printStackTrace()}")
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private suspend fun userRetrofit(userId : String, tagLine: String) {
        val retrofitUserAPI = getInstanceAsia()

        try{
            GlobalScope.launch(Dispatchers.IO) {
                val retrofitService = retrofitUserAPI.create(LolQueryGameName::class.java)
                    .getGameUserInfo(userId, tagLine, useApiKey)
                if (retrofitService.isSuccessful) {
                    val comment = retrofitService.body()!!
                    puuidRetrofit(comment.puuid, userId, tagLine)
                } else {
                    //userViewModel.setStepState(
                    //    userInfoFail("사용자 검색 에러"),
                    //    "검색 아이디가 없거나 api 키가 만료 되었습니다."
                    //)
                    userViewModel.setUserFail(retrofitService.code())
                }
            }
        }  catch (e: Exception) {
            Log.i(TAG, "err2 = ${e.printStackTrace()}")
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private suspend fun puuidRetrofit(puuid: String, userId : String, tagLine: String) {
        val retrofitpuuidAPI = getInstance()

        try{
            GlobalScope.launch(Dispatchers.IO) {
                val retrofitService = retrofitpuuidAPI.create(LolQueryUserIdUsePuuid::class.java)
                    .getUserInfo(puuid, useApiKey)
                if (retrofitService.isSuccessful) {
                    val comment = retrofitService.body()!!
                    userViewModel.userLevelInfo(
                        comment.puuid, userId, tagLine, comment.profileIconId, comment.summonerLevel
                    )
                    rankRetfofit(userId, tagLine, comment.id, comment.puuid)
                } else {
                    //userViewModel.setStepState(
                    //    userInfoFail("사용자 검색 에러"),
                    //    "검색 아이디가 없거나 api 키가 만료 되었습니다."
                    //)
                    userViewModel.setUserFail(retrofitService.code())
                }
            }
        }  catch (e: Exception) {
            Log.i(TAG, "err2 = ${e.printStackTrace()}")
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    suspend fun rankRetfofit(userId : String, tagLine: String, encryptedId: String, puuid: String) {
        val retrofitUserAPI = getInstance()

        val retrofitService = retrofitUserAPI.create(LolQueryLank::class.java)
            .getRankInfo(encryptedId, useApiKey)
        val response  = retrofitService
        GlobalScope.launch(Dispatchers.IO) {
            if (response.isSuccessful) {
                response.body()?.let {
                    if(it.isEmpty()){
                        val item = UserRankInfo.UserRankInfoItem(
                            freshBlood = false, hotStreak = false, inactive = false, leagueId = "", leaguePoints = 0, losses = 0, queueType = "",
                            rank = "", wins = 0, summonerId = "", summonerName = "", tier = "", veteran = false
                        )
                        userViewModel.setRankInfo(item)
                    } else {
                        for (item in it) {
                            if (item.queueType == "RANKED_SOLO_5x5") {
                                userViewModel.setRankInfo(item)
                            }
                            Log.i(TAG, "queueType = ${item.queueType}")
                        }
                    }
                }
                topChamp(userId, tagLine, puuid)
            } else {
                userViewModel.setUserFail(response.code())
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private suspend fun topChamp(userId : String, tagLine: String, puuid : String) {
        val retrofitUserAPI = getInstance()
        val searchCount = 10

        val retrofitService = retrofitUserAPI.create(LolQueryTopChamp::class.java)
            .getTopChampInfo(puuid, searchCount, useApiKey)

        GlobalScope.launch(Dispatchers.IO) {
            if (retrofitService.isSuccessful) {
                retrofitService.body()?.let {
                    it.sortByDescending { item -> item.championPointsSinceLastLevel }
                    userViewModel.setUseChamp(it)
                }
                matchList(userId, tagLine, puuid)
            } else {
                userViewModel.setUserFail(retrofitService.code())
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private suspend fun matchList(userId: String, tagLine: String, encryptedPUUID : String) {
        val retrofitUserAPI = getInstanceAsia()
        val retrofitService = retrofitUserAPI.create(LolQueryMatchList::class.java)
            .getMatchList(encryptedPUUID, 0, 100, useApiKey)

        GlobalScope.launch(Dispatchers.IO) {
            if (retrofitService.isSuccessful) {
                retrofitService.body()?.let {
                    userViewModel.setMatchList(it)
                }
                userViewModel.setUserSuccess(userId, tagLine)
            } else {
                userViewModel.setUserFail(retrofitService.code())
            }
        }
    }
}



