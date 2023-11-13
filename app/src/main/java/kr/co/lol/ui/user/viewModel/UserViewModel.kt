package kr.co.lol.ui.user.viewModel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kr.co.lol.dataclass.ChampionMasteryTop
import kr.co.lol.dataclass.UserData
import kr.co.lol.dataclass.UserRankInfo
import kr.co.lol.internet.TAG
import kr.co.lol.internet.UserInfoStep
import kr.co.lol.internet.userInfoFail
import kr.co.lol.internet.userInfoGetloading
import kr.co.lol.internet.userInfoSuccess
import kr.co.lol.internet.userStepMsg

/* oldViewModel
class UserViewModel : ViewModel() {

    // 정보 획득 확인
    private val _stepUserState: MutableStateFlow<UserInfoStep>
        = MutableStateFlow<UserInfoStep>(userInfoGetloading)
    val stepUserState: StateFlow<UserInfoStep> = _stepUserState

    // 로그인 한 user
    private val _userId = MutableLiveData<String>("")
    val userId: LiveData<String> = _userId

    // 로그인한 API key
    private val _apiKey = MutableLiveData<String>("")
    val apiKey : LiveData<String> = _apiKey

    // 로그인한 암호화된 랭크 검사때 쓰자.
    private val _userEncryedId = MutableLiveData<String>("")
    val userEncryedId: LiveData<String> = _userEncryedId

    private val _accountId = MutableLiveData<String>("")
    val acccountId: LiveData<String> = _accountId

    private val _profileIconId = MutableLiveData<Int>(0)
    val profileIconId: LiveData<Int> = _profileIconId

    private val _summonerLevel = MutableLiveData<Int>(0)
    val summonerLevel: LiveData<Int> = _summonerLevel

    // encryptedPUUID 나중 숙련도 챔프에 쓰자.
    private val _puuid = MutableLiveData<String>("")
    val puuid: LiveData<String> = _puuid

    // 랭크 정보
    private val _leagueId = MutableLiveData<String>("")
    val leageId:LiveData<String> = _leagueId
    // 랭크 타임
    private val _queueType = MutableLiveData<String>("")
    val queueType : LiveData<String> = _queueType
    // 티어
    private val _loltear = MutableLiveData<String>("")
    val loltear: LiveData<String> = _loltear
    // 랭크
    private val _lolrank = MutableLiveData<String>("")
    val lolrank = _lolrank
    // 포인트
    private val _lolPoint = MutableLiveData<Int>(0)
    val lolPoint:LiveData<Int> = _lolPoint
    // wins
    private val _lolWin = MutableLiveData<Int>(0)
    val lolWin: LiveData<Int> = _lolWin
    // losses
    private val _lolLosses = MutableLiveData<Int>(0)
    val lolLosses: LiveData<Int> = _lolLosses

    // apikey등록
    val setApiKey: (key:String, userId:String) -> (Unit) = {key, userId ->
        _userId.value = userId
        _apiKey.value = key
    }

    // 챔프 숙련도 체크
//    private val _champTopTenList = MutableStateFlow<MutableList<useChampLevel>>(mutableListOf())
//    val champTopTenList: StateFlow<MutableList<useChampLevel>> = _champTopTenList

    // 챔프 숙련도 Top 10
    private val _rawchampTopList = mutableStateListOf<Int>()
    private val _champTopList  = MutableLiveData<List<Int>>(_rawchampTopList)
    val champTopList: LiveData<List<Int>> = _champTopList


    // 1차 사용자 정보 등록
    suspend fun userLevelInfo(userData: UserData) {
        _userEncryedId.postValue(userData.id)
        _accountId.postValue(userData.accountId)
        _userId.postValue(userData.name)
        _profileIconId.postValue(userData.profileIconId)
        _puuid.postValue( userData.puuid)
        _summonerLevel.postValue(userData.summonerLevel)

    }

    // 2차 사용자 랭크 등록
    suspend fun setRankInfo(rankData: UserRankInfo.UserRankInfoItem) {
            _leagueId.postValue(rankData.leagueId)
            _queueType.postValue(rankData.queueType)
            _loltear.postValue(rankData.tier)
            _lolrank.postValue(rankData.rank)
            _lolPoint.postValue(rankData.leaguePoints)
            _lolWin.postValue(rankData.wins)
            _lolLosses.postValue( rankData.losses)
    }

    // 3차 10개 챔프 등록
    suspend fun setUseChamp(topChamp: List<ChampionMasteryTop.ChampionMasteryTopItem>) {
        for(item in topChamp) {
            //_rawchampTopList.add(item)
        }
    }

    init {
        settingInit()
    }

    fun settingInit() {
        viewModelScope.launch {
            _stepUserState.value = userInfoGetloading
        }
    }


    val setStepUserState: (stepState: UserInfoStep) -> (Unit) = { step ->
        _stepUserState.value = step
    }

    // BACK 그라운드 상태에서 STATE에 바로 입력하면 에러 난다.
    fun setStepState(step: UserInfoStep, msg: String ="") {
        when(step) {
            is UserInfoStep.Fail -> {
                //userInfoFail(msg)
            }
            is UserInfoStep.StepMsg -> {
            //    userInfoFail(msg)
            }
            is UserInfoStep.Success -> {
                _stepUserState.value = userInfoSuccess()
            }
            UserInfoStep.loading -> TODO()
        }
    }
}
 */
class UserViewModel : ViewModel() {

    // 사용자 정보 획득 절차
    private val _stepUserState: MutableStateFlow<UserInfoStep>
        = MutableStateFlow<UserInfoStep>(userInfoGetloading)
    val stepUserState: StateFlow<UserInfoStep> = _stepUserState
    fun setStepUserFind(id : String){
        _stepUserState.value = userStepMsg(id)
    }
    fun setUserFail(errorCode : Int) {
        _stepUserState.value = userInfoFail(errorCode)
        _userFailCode.postValue(errorCode)
    }
    fun setUserSuccess() {
        _stepUserState.value = userInfoSuccess()
    }

    // 에러 코드
    private val _userFailCode = MutableLiveData<Int>(0)
    val userFailCode: LiveData<Int> = _userFailCode
    fun setUserInputErrCode(code:Int) {
        _userFailCode.postValue(code)
    }
    fun setUserCodeClear() {
        _userFailCode.postValue(0)
    }

    // 로그인 한 user
    // 검색에 활용해야하니깐 userid는 라이브 뷰로 사용하자.
    private var _userId = MutableLiveData<String>("")
    val userId: LiveData<String> = _userId

    // 로그인한 API key
    private val _apiKey = MutableLiveData<String>("")
    val apiKey : LiveData<String> = _apiKey

    // 로그인한 암호화된 랭크 검사때 쓰자.
    private var _userEncryedId = ""
    val userEncryedId: String
        get() = _userEncryedId

    private var _accountId = ""
    val accountId: String
        get() = _accountId

    private var _profileIconId = 0
    val profileIconId : Int
        get() = _profileIconId

    private var _summonerLevel = 0
    val summonerLevel: Int
        get() = _summonerLevel

    // encryptedPUUID 나중 숙련도 챔프에 쓰자.
    private var _puuid = ""
    val puuid: String
        get() = _puuid

    // 랭크 정보
    private var _leagueId = ""
    val leagueId : String
        get() = _leagueId

    // 랭크 타임
    private var _queueType = ""
    val queueType : String
        get() = _queueType

    // 티어
    private var _loltear = ""
    val loltear : String
        get() = _loltear

    // 랭크
    private var _lolrank = ""
    val lolrank : String
        get() = _lolrank

    // 포인트
    private var _lolPoint = 0
    val lolPoint : Int
        get() = _lolPoint

    // wins
    private var _lolWin = 0
    val lolWin : Int
        get() = _lolWin

    // losses
    private var _lolLosses = 0
    val lolLosses : Int
        get() = _lolLosses

    // apikey등록
    val setApiKey: (key:String, userId:String) -> (Unit) = {key, userId ->
        _userId.value = userId
        _apiKey.value = key
    }

    // 챔프 숙련도 체크

    // 챔프 숙련도 Top 10
//    private val _rawchampTopList = mutableStateListOf<Int>()
//    private val _champTopList  = MutableLiveData<List<Int>>(_rawchampTopList)
//    val champTopList: LiveData<List<Int>> = _champTopList
    private var _rawChampTopList = mutableListOf<Int>()
    val champTopList : MutableList<Int>
        get() = _rawChampTopList

    // 1차 사용자 정보 등록
    suspend fun userLevelInfo(userData: UserData) {
        _userEncryedId = userData.id
        _accountId = userData.accountId
        _userId.postValue(userData.name)
        _profileIconId = userData.profileIconId
        _puuid = userData.puuid
        _summonerLevel = userData.summonerLevel
    }

    // 2차 사용자 랭크 등록
    suspend fun setRankInfo(rankData: UserRankInfo.UserRankInfoItem) {
            _leagueId = rankData.leagueId
            _queueType = rankData.queueType
            _loltear = rankData.tier
            _lolrank = rankData.rank
            _lolPoint = rankData.leaguePoints
            _lolWin = rankData.wins
            _lolLosses = rankData.losses
    }

    // 3차 10개 챔프 등록
    suspend fun setUseChamp(topChamp: List<ChampionMasteryTop.ChampionMasteryTopItem>) {
        _rawChampTopList.clear()
        for(item in topChamp) {
            _rawChampTopList.add(item.championId)
        }
    }

    init {
        setUserInit()
    }

    fun setUserInit() {
        viewModelScope.launch {
            _stepUserState.value = userInfoGetloading
        }
    }

    val setStepUserState: (stepState: UserInfoStep) -> (Unit) = { step ->
        _stepUserState.value = step
    }

//  BACK 그라운드 상태에서 STATE에 바로 입력하면 에러 난다.
//    suspend fun setStepState(step: UserInfoStep, msg: String ="") {
//        when(step) {
//            is userInfoFail -> {
//                //userInfoFail(msg)
//            }
//            is userStepMsg -> {
//            //    userInfoFail(msg)
//            }
//            is userInfoSuccess -> {
//                _stepUserState.value = userInfoSuccess()
//                Log.i(TAG, "완료")
//            }
//            UserInfoStep.loading -> TODO()
//        }
//    }
}
