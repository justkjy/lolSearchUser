package kr.co.justkimlol.viewModel.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kr.co.justkimlol.mainfragment.home.internet.GetJsonFromUrl
import kr.co.justkimlol.mainfragment.home.internet.getJsonLoad
import kr.co.justkimlol.ui.navigation.NavViewState
import kr.co.justkimlol.ui.navigation.navWaitState
import kr.co.justkimlol.ui.navigation.navCheckState

enum class PatchState {LOADING, PROGRESS, COMPLETE, ERROR}

class ChampionInitViewModel : ViewModel() {
    // 상태 정보 Flow
    private val _feedState : MutableStateFlow<GetJsonFromUrl> = MutableStateFlow(GetJsonFromUrl.Loading)
    val feedState: StateFlow<GetJsonFromUrl> = _feedState

    // 네비게이션 Flow
    private val _naviState : MutableStateFlow<NavViewState> = MutableStateFlow(NavViewState.WaitState)
    val naviState:MutableStateFlow<NavViewState> = _naviState

    // 신챔프 패치 정보
    private val _championInfo = MutableLiveData(PatchState.LOADING)
    val championInfo : LiveData<PatchState> = _championInfo
    val setChampionInfo:(PatchState) -> Unit = {
        _championInfo.value = it
    }

    // 입력 유저 아이디 등록
    private val _userId = MutableLiveData("")
    val userId : LiveData<String> = _userId
    fun inputUserid(id: String){
        this._userId.value = id
    }

    // 입력 유저 테그 등록
    private val _tagLine = MutableLiveData("")
    val tagLine : LiveData<String> = _tagLine
    fun inputTagLine(tagLine: String){
        this._tagLine.value = tagLine
    }

    // puuid
    private val _puuid = MutableLiveData("")
    val puuid : LiveData<String> = _puuid
    fun inputpuuidLine(puuid: String){
        this._puuid.postValue(puuid)
    }

    private val _apiKey = MutableLiveData("")
    val apiKey : LiveData<String> = _apiKey
    fun inputApiKey(id: String){
        this._apiKey.value = id
    }

    // 키가 만료 되었거나 잘 못 되었을때 직접 넣는걸로 하자
    private val _needApiKey = MutableLiveData(false)
    val needApiKey : LiveData<Boolean> = _needApiKey
    fun inputApiKey(state: Boolean) {
        this._needApiKey.value = state
    }

    val setInputButtonCheck:() -> Unit = {
        _naviState.value = navCheckState
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

    private fun settingInit() {
        viewModelScope.launch {
            _feedState.value = getJsonLoad
        }
    }
}