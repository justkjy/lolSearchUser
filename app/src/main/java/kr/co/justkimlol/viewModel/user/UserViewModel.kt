package kr.co.justkimlol.viewModel.user

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kr.co.justkimlol.dataclass.ChampAllListData
import kr.co.justkimlol.dataclass.ChampionMasteryTop
import kr.co.justkimlol.dataclass.UserData
import kr.co.justkimlol.dataclass.UserIdWithTagData
import kr.co.justkimlol.dataclass.UserRankInfo
import kr.co.justkimlol.internet.UserInfoStep
import kr.co.justkimlol.internet.userInfoFail
import kr.co.justkimlol.internet.userInfoGetLoading
import kr.co.justkimlol.internet.userInfoSuccess
import kr.co.justkimlol.internet.userStepMsg
import kr.co.justkimlol.room.data.roomHelperValue

class UserViewModel : ViewModel() {

    // 사용자 정보 획득 절차
    private val _stepUserState: MutableStateFlow<UserInfoStep>
        = MutableStateFlow(userInfoGetLoading)

    val stepUserState: StateFlow<UserInfoStep> = _stepUserState
    fun setStepUserFind(id : String, tagLine: String){
        _stepUserState.value = userStepMsg(id, tagLine)
    }

    fun setUserFail(errorCode : Int) {
        _stepUserState.value = userInfoFail(errorCode)
        _userFailCode.postValue(errorCode)
    }

    fun setUserSuccess(userId : String, tagLine: String) {
        _stepUserState.value = userInfoSuccess(userId, tagLine)
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

    private var _tagLine = MutableLiveData("")
    val tagLine: LiveData<String> = _tagLine

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

    private var _profileIconId = MutableLiveData(0)
    val profileIconId : LiveData<Int> = _profileIconId

    private var _summonerLevel = MutableLiveData(0)
    val summonerLevel: LiveData<Int> = _summonerLevel

    // encryptedPUUID 나중 숙련도 챔프에 쓰자.
    private var _puuid = MutableLiveData("")
    val puuid: LiveData<String> = _puuid

    // 랭크 정보
    private var _leagueId = ""
    val leagueId : String
        get() = _leagueId

    // 랭크 타임
    private var _queueType = ""
    val queueType : String
        get() = _queueType

    // 티어
    private var _loltier = MutableLiveData("")
    val loltear : LiveData<String> = _loltier

    // 랭크
    private var _lolrank = MutableLiveData("")
    val lolrank : LiveData<String> = _lolrank

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
    // ChampEngName
    private val _rawChampEngList = mutableStateListOf<String>()
    private val _champEngList = MutableLiveData<List<String>>(_rawChampEngList)
    val champEngList: LiveData<List<String>> = _champEngList // 사용

    private val _rawMatchList = mutableStateListOf<String>()
    private val _matchList = MutableLiveData<List<String>>(_rawMatchList)
    val matchList: LiveData<List<String>> = _matchList


    fun InsertApi(useApiKey: String) {
        _apiKey.postValue(useApiKey)
    }

    fun LoginUserInfo(puuid : String, userId : String, tagLine : String) {
        _userId.postValue(userId)
        _tagLine.postValue(tagLine)
        _puuid.postValue(puuid)
    }

    // 1차 사용자 정보 등록
    fun userLevelInfo(puuid: String, userId: String, tagLine: String,  profileIconId: Int, summonerLevel: Int) {
        _userId.postValue(userId)
        _tagLine.postValue(tagLine)
        _puuid.postValue(puuid)
        _profileIconId.postValue(profileIconId)
        _summonerLevel.postValue(summonerLevel)
    }

    // 2차 사용자 랭크 등록
    fun setRankInfo(rankData: UserRankInfo.UserRankInfoItem) {
        _leagueId = rankData.leagueId
        _queueType = rankData.queueType
        _loltier.postValue(rankData.tier)
        _lolrank.postValue(rankData.rank)
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

    fun setTopChampList(topChamp : List<String>) {
        _rawChampEngList.clear()
        for(item in topChamp) {
            _rawChampEngList.add(item)
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
