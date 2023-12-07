package kr.co.justkimlol.mainfragment.user

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kr.co.justkimlol.viewModel.SharedViewModel
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
import kr.co.justkimlol.viewModel.user.UserViewModel
import kr.co.justkimlol.room.data.RoomHelper
import kr.co.justkimlol.room.data.roomHelperValue
import kr.co.justkimlol.ui.component.userInfo.UserTop
import kr.co.justkimlol.ui.theme.LolInfoViewerTheme

class UserFragment : Fragment() {
    private var _binding: FragmentUserBinding? = null
    // This property is only valid between onCreateView and
    // 활성화 하자.
    private val userViewModel: UserViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var useApiKey: String
    private lateinit var puuid : String
    private lateinit var userId : String
    private lateinit var tagLine : String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        puuid = arguments?.getString("puuid") ?: sharedViewModel.puuid.value!!
        userId = arguments?.getString("userId") ?: ""
        tagLine = arguments?.getString("tagLine") ?: ""
        useApiKey = arguments?.getString("apiKey") ?: sharedViewModel.apiKey.value!!

        // 뷰처리가 끝나면 시작하자. check
        observeUiEffects()
        runRetrofit()

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
        userViewModel.setTopChampList(champEngList)

        sharedViewModel.inputApiKey(useApiKey)
        sharedViewModel.inputLoltier(userViewModel.loltear.value?: "")
        sharedViewModel.inputpuuid(userViewModel.puuid.value?: "")
        sharedViewModel.inputProfileId(userViewModel.profileIconId.value?:0)
        sharedViewModel.inputUserId(userId)
    }

    private fun runRetrofit() {
        userViewModel.InsertApi(useApiKey)
        runPuuidSearch(puuid, userId, tagLine)
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun runPuuidSearch(getPuuid : String, getUserId: String, gettagLine: String) {
        try {
            GlobalScope.launch(Dispatchers.IO) {
                puuidRetrofit(getPuuid, getUserId, gettagLine)
            }
        }  catch (e: Exception) {
            Log.i(TAG, "err1 = ${e.printStackTrace()}")
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun runUserSearch(getUseId: String, getTagLine: String) {
        try {
            GlobalScope.launch(Dispatchers.IO) {
                userRetrofit(getUseId, getTagLine)
            }

        }  catch (e: Exception) {
            Log.i(TAG, "err1 = ${e.printStackTrace()}")
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private suspend fun userRetrofit(getUserId : String, getTagLine: String) {
        val retrofitUserAPI = getInstanceAsia()

        try{
            GlobalScope.launch(Dispatchers.IO) {
                val retrofitService = retrofitUserAPI.create(LolQueryGameName::class.java)
                    .getGameUserInfo(getUserId, getTagLine, useApiKey)
                if (retrofitService.isSuccessful) {
                    val comment = retrofitService.body()!!
                    puuidRetrofit(comment.puuid, getUserId, getTagLine)
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
    private suspend fun puuidRetrofit(getPuuid: String, getUserId : String, getTagLine: String) {
        userViewModel.LoginUserInfo(getPuuid, getUserId, getTagLine)
        val retrofitpuuidAPI = getInstance()
        try{
            GlobalScope.launch(Dispatchers.IO) {
                val retrofitService = retrofitpuuidAPI.create(LolQueryUserIdUsePuuid::class.java)
                    .getUserInfo(getPuuid, useApiKey)
                if (retrofitService.isSuccessful) {
                    val comment = retrofitService.body()!!
                    userViewModel.userLevelInfo(
                        comment.puuid, getUserId, getTagLine, comment.profileIconId, comment.summonerLevel
                    )
                    rankRetfofit(getUserId, getTagLine, comment.id, comment.puuid)
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
    suspend fun rankRetfofit(getUserId : String, getTagLine: String, encryptedId: String, getPuuid: String) {
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
                topChamp(getUserId, getTagLine, getPuuid)
            } else {
                userViewModel.setUserFail(response.code())
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private suspend fun topChamp(getUserId : String, getTagLine: String, getPuuid : String) {
        val retrofitUserAPI = getInstance()
        val searchCount = 10

        val retrofitService = retrofitUserAPI.create(LolQueryTopChamp::class.java)
            .getTopChampInfo(getPuuid, searchCount, useApiKey)

        GlobalScope.launch(Dispatchers.IO) {
            if (retrofitService.isSuccessful) {

                retrofitService.body()?.let {

                    it.sortByDescending { item -> item.championPointsSinceLastLevel }
                    userViewModel.setUseChamp(it)
                }
                matchList(getUserId, getTagLine, getPuuid)
            } else {
                userViewModel.setUserFail(retrofitService.code())
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private suspend fun matchList(getUserId: String, getTagLine: String, encryptedPUUID : String) {
        val retrofitUserAPI = getInstanceAsia()
        val retrofitService = retrofitUserAPI.create(LolQueryMatchList::class.java)
            .getMatchList(encryptedPUUID, 0, 100, useApiKey)

        GlobalScope.launch(Dispatchers.IO) {
            if (retrofitService.isSuccessful) {
                retrofitService.body()?.let {
                    userViewModel.setMatchList(it)
                }
                userViewModel.setUserSuccess(getUserId, getTagLine)
            } else {
                userViewModel.setUserFail(retrofitService.code())
            }
        }
    }
}



