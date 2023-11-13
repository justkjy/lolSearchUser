package kr.co.lol.ui.home.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kr.co.lol.internet.GetJsonFromUrl
import kr.co.lol.internet.getJsonLoad
import kr.co.lol.ui.navigation.NavViewState
import kr.co.lol.ui.navigation.navSuccessState
import kr.co.lol.ui.navigation.navWaitState
import kr.co.lol.ui.navigation.navcheckState

enum class PatchState {LOADING, PROGRESS, COMPLETE, ERROR}
enum class NavProgState {SUCCESS, WAIT, USERINFO, CHAMPINFO}

class ChampionInitViewModel : ViewModel() {
    // 상태 정보 Flow
    private val _feedState : MutableStateFlow<GetJsonFromUrl> =   MutableStateFlow<GetJsonFromUrl>(GetJsonFromUrl.Loading)
    val feedState: StateFlow<GetJsonFromUrl> = _feedState

    // 네비게이션 Flow
    private val _naviState : MutableStateFlow<NavViewState> =
        MutableStateFlow<NavViewState>(NavViewState.WaitState)
    val naviState:MutableStateFlow<NavViewState> = _naviState

    // 신챔프 패치 정보
    private val _championInfo = MutableLiveData(PatchState.LOADING)
    val championInfo : LiveData<PatchState> = _championInfo
    val setChampionInfo:(PatchState) -> Unit = {
        _championInfo.value = it
    }

    // 입력 유저 아이디 등록
    private val _userId = MutableLiveData<String>("")
    val userId : LiveData<String> = _userId
    fun inputUserid(id: String){
        this._userId.value = id
    }

    private val _apiKey = MutableLiveData<String>("")
    val apiKey : LiveData<String> = _apiKey
    fun inputApiKey(id: String){
        this._apiKey.value = id
    }

    // 버튼 눌렀을때 랭크 정보 점프 네비게이션
    private val _inputButtonCheck = MutableLiveData(false)
    val iputButtonCheck:LiveData<Boolean> = _inputButtonCheck

    val setInputButtonCheck:() -> Unit = {
        _naviState.value = navcheckState
    }

    val setNavState:(navViewState: NavViewState) -> Unit = {
        _naviState.value = it
    }

    val setInputWait:() -> Unit = {
        _naviState.value = navWaitState
    }

    // 신 패치 진행 상태
    private val _patchProgressStep = MutableLiveData("")
    val patchProgressStep : LiveData<String> = _patchProgressStep
    val patchProgressValue: (String) -> Unit = {
        _patchProgressStep.postValue(it)
    }

    private val _errorCode = MutableLiveData(0)
    val errorCode : LiveData<Int> = _errorCode
    val setChangeCode: (code: Int) -> (Unit) = { it ->
        _errorCode.value = it
    }

    fun setCodeClear() {
        _errorCode.value = 0
    }


    val settingStatus : (state: GetJsonFromUrl)->(Unit) = { it ->
        _feedState.value = it
    }


    init {
        settingInit()
    }

    fun settingInit() {
        viewModelScope.launch {
            _feedState.value = getJsonLoad
        }
    }


}