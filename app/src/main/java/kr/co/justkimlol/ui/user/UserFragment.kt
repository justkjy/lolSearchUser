package kr.co.justkimlol.ui.user

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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kr.co.justkimlol.SharedViewModel
import kr.co.justkimlol.databinding.FragmentUserBinding
import kr.co.justkimlol.dataclass.UserRankInfo
import kr.co.justkimlol.internet.TAG
import kr.co.justkimlol.internet.retrofit.GetJsonFromRerofit
import kr.co.justkimlol.internet.retrofit.GetJsonFromRerofit.Companion.getInstance
import kr.co.justkimlol.internet.retrofit.GetJsonFromRerofit.Companion.getInstanceAsia
import kr.co.justkimlol.internet.retrofit.LolQueryItem
import kr.co.justkimlol.internet.retrofit.LolQueryLank
import kr.co.justkimlol.internet.retrofit.LolQueryMatchList
import kr.co.justkimlol.internet.retrofit.LolQueryTopChamp
import kr.co.justkimlol.internet.userInfoFail
import kr.co.justkimlol.internet.userInfoGetloading
import kr.co.justkimlol.internet.userInfoSuccess
import kr.co.justkimlol.internet.userStepMsg
import kr.co.justkimlol.room.data.RoomHelper
import kr.co.justkimlol.room.data.roomHelperValue
import kr.co.justkimlol.ui.user.viewModel.UserViewModel
import kr.co.justkimlol.ui.theme.LolInfoViewerTheme

var appActivityViewModel : SharedViewModel? = null

class UserFragment : Fragment() {
    private var _binding: FragmentUserBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    var job: Job? = null

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

        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        appActivityViewModel = sharedViewModel


        useApiKey = sharedViewModel.apiKey.value!!

        // 뷰처리가 끝나면 시작하자. check
        runRetrofit()

        observeUiEffects()
        // 아래에 Retrofit을 적으면 에러가 남 추측하는데 함수안에 GlobalScope.launch(Dispatchers.IO)
        // 타입이라서그런듯
        // runRetrofit(useId = useId)

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
                        is userInfoGetloading -> {

                        }
                        is userStepMsg -> {
                            val userId = stepState.userId
                            runUserSearch(userId)
                        }
                        is userInfoSuccess ->{
                            Log.i("TEST", "Success")
                            insertUserInfo(stepState.userId)
                        }
                        is userInfoFail ->{
                            userViewModel.setUserInputErrCode(stepState.code)
                            userViewModel.setUserInit()
                        }
                    }
                }
            }
        }
    }

    private fun insertUserInfo(userId : String){
        var helper: RoomHelper? = null
        var champEngList = mutableListOf<String>()
        val fragmentContext = this.context ?.let { context ->
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
        Log.i("TEST", "${userViewModel.profileIconId}" +
                "${userViewModel.summonerLevel}" +
                "${userViewModel.loltear}" +
                "${userViewModel.lolrank}" +
                "${userViewModel.lolWin}"+
                "${userViewModel.lolLosses}"+
                "${champEngList}" +
                "${userViewModel.matchList}"
        )
    }

    private fun runRetrofit() {
        val useId = sharedViewModel.userId.value!!
        runUserSearch(useId)
    }

    private fun runUserSearch(useId: String) {
        try {
                GlobalScope.launch(Dispatchers.IO) {
                    userRetrofit(useId)
                }
        }  catch (e: Exception) {
            Log.i(TAG, "err1 = ${e.printStackTrace()}")
        }
    }

    suspend fun userRetrofit(userId : String) {
        val retrofitUserAPI = GetJsonFromRerofit.getInstance()

        try{
            GlobalScope.launch(Dispatchers.IO) {
                val retrofitService = retrofitUserAPI.create(LolQueryItem::class.java)
                    .getUserInfo(userId, useApiKey)
                val response = retrofitService
                if (response.isSuccessful) {
                    val comment = response.body()!!
                    userViewModel.userLevelInfo(comment)
                    //encryptedPUUID = comment.puuid // 숙련도 정보 체크때 써야함
                    Log.i(TAG, "comment.id = ${comment.id}, comment.puuid = ${comment.puuid}")
                    rankRetfofit(userId, comment.id, comment.puuid)
                } else {
                    //userViewModel.setStepState(
                    //    userInfoFail("사용자 검색 에러"),
                    //    "검색 아이디가 없거나 api 키가 만료 되었습니다."
                    //)
                    userViewModel.setUserFail(response.code())
                }
            }
        }  catch (e: Exception) {
            Log.i(TAG, "err2 = ${e.printStackTrace()}")
        }
    }

    suspend fun rankRetfofit(userId : String, encryptedId: String, encryptedPUUID: String) {
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
                topChamp(userId, encryptedPUUID)
            } else {
                userViewModel.setUserFail(response.code())
            }
        }
    }

    suspend fun topChamp(userId : String, encryptedPUUID : String) {
        val retrofitUserAPI = getInstance()
        val searchCount = 10

        val retrofitService = retrofitUserAPI.create(LolQueryTopChamp::class.java)
            .getTopChampInfo(encryptedPUUID, searchCount, useApiKey)
        val home = retrofitService.message()

        val response = retrofitService
        GlobalScope.launch(Dispatchers.IO) {
            if(response.isSuccessful) {
                response.body()?.let{
                    userViewModel.setUseChamp(it)
                }

                matchList(userId, encryptedPUUID)

            } else {
                userViewModel.setUserFail(response.code())
            }
        }
    }

    suspend fun matchList(userId: String, encryptedPUUID : String) {
        val retrofitUserAPI = getInstanceAsia()
        val start = 0
        val searchCount = 100

        val retrofitService = retrofitUserAPI.create(LolQueryMatchList::class.java)
            .getMatchList(encryptedPUUID, 0, 100, useApiKey)

        val response = retrofitService
        GlobalScope.launch(Dispatchers.IO) {
            if(response.isSuccessful) {
                response.body()?.let{
                    userViewModel.setMatchList(it)
                }
                userViewModel.setUserSuccess(userId)
            } else {
                userViewModel.setUserFail(response.code())
            }
        }
    }
}



