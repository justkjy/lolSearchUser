package kr.co.justkimlol.mainfragment.user.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kr.co.justkimlol.dataclass.ChampionMasteryTop
import kr.co.justkimlol.dataclass.UserData
import kr.co.justkimlol.dataclass.UserRankInfo
import kr.co.justkimlol.internet.UserInfoStep
import kr.co.justkimlol.internet.userInfoFail
import kr.co.justkimlol.internet.userInfoGetLoading
import kr.co.justkimlol.internet.userInfoSuccess
import kr.co.justkimlol.internet.userStepMsg

class UserViewModel : ViewModel() {

    // 사용자 정보 획득 절차
    private val _stepUserState: MutableStateFlow<UserInfoStep>
        = MutableStateFlow(userInfoGetLoading)
    val stepUserState: StateFlow<UserInfoStep> = _stepUserState
    fun setStepUserFind(id : String){
        _stepUserState.value = userStepMsg(id)
    }
    fun setUserFail(errorCode : Int) {
        _stepUserState.value = userInfoFail(errorCode)
        _userFailCode.postValue(errorCode)
    }
    fun setUserSuccess(userId : String) {
        _stepUserState.value = userInfoSuccess(userId)
    }

    // 에러 코드
    private val _userFailCode = MutableLiveData(0)
    val userFailCode: LiveData<Int> = _userFailCode
    fun setUserInputErrCode(code:Int) {
        _userFailCode.postValue(code)
    }
    fun setUserCodeClear() {
        _userFailCode.postValue(0)
    }

    // 로그인 한 user
    // 검색에 활용해야하니깐 userid는 라이브 뷰로 사용하자.
    private var _userId = MutableLiveData("")
    val userId: LiveData<String> = _userId

    // 로그인한 API key
    private val _apiKey = MutableLiveData("")
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

    // 챔프 숙련도 체크
    // 챔프 숙련도 Top 10
    private var _rawChampTopList = mutableListOf<Int>()
    val champTopList : MutableList<Int>
        get() = _rawChampTopList

    private var _rawMatchList = mutableListOf<String>()
    val matchList : MutableList<String>
        get() = _rawMatchList

    // 1차 사용자 정보 등록
    fun userLevelInfo(userData: UserData) {
        _userEncryedId = userData.id
        _accountId = userData.accountId
        _userId.postValue(userData.name)
        _profileIconId = userData.profileIconId
        _puuid = userData.puuid
        _summonerLevel = userData.summonerLevel
    }

    // 2차 사용자 랭크 등록
    fun setRankInfo(rankData: UserRankInfo.UserRankInfoItem) {
            _leagueId = rankData.leagueId
            _queueType = rankData.queueType
            _loltear = rankData.tier
            _lolrank = rankData.rank
            _lolPoint = rankData.leaguePoints
            _lolWin = rankData.wins
            _lolLosses = rankData.losses
    }

    // 3차 10개 챔프 등록
    fun setUseChamp(topChamp: List<ChampionMasteryTop.ChampionMasteryTopItem>) {
        _rawChampTopList.clear()
        for(item in topChamp) {
            _rawChampTopList.add(item.championId)
        }
    }

    fun setMatchList(matchList: List<String>) {
        _rawMatchList.clear()
        for(item in matchList) {
            _rawMatchList.add(item)
        }
    }

    init {
        setUserInit()
    }

    fun setUserInit() {
        viewModelScope.launch {
            _stepUserState.value = userInfoGetLoading
        }
    }
}
