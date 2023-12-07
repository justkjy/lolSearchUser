package kr.co.justkimlol.viewModel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kr.co.justkimlol.ui.component.button.ChampionList

class SharedViewModel : ViewModel() {
    ////////////////////////////////////////////////////////////////////////////////////////////////
    // for UserInfo/////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    // 로그인한 API key
    private val _apiKey = MutableLiveData("")
    val apiKey : LiveData<String> = _apiKey
    val inputApiKey: (key:String) -> (Unit) = {it ->
        _apiKey.value = it
    }

    // user Id
    private val _userId = MutableLiveData("")
    val userId: LiveData<String> = _userId
    val inputUserId: (id:String) -> (Unit) = {it ->
        _userId.value = it
    }

    // puuid
    private val _puuid = MutableLiveData("")
    val puuid: LiveData<String> = _puuid
    val inputpuuid: (puuid:String) -> (Unit) = {it ->
        _puuid.value = it
    }

    // 사용 프로필 아이콘 아이디
    private val _profileIconId = MutableLiveData(0)
    val profileId: LiveData<Int> = _profileIconId
    val inputProfileId: (profileId: Int) -> (Unit) = { it ->
        _profileIconId.value = it
    }

    // 사용 프로필 숙련도
    private val _summonerLevel = MutableLiveData(0)
    val summonerLevel: LiveData<Int> = _summonerLevel
    val inputSummonerLevel: (summonerLevel: Int) -> (Unit) = {
        _summonerLevel.value = it
    }

    // 티어
    private val _loltier = MutableLiveData("")
    val loltiar: LiveData<String> = _loltier
    val inputLoltier : (loltier: String) -> Unit = {
        _loltier.value = it
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    // Champion Info //////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////
    // 로테이션 챔프
    private val _rawChampRotations = mutableStateListOf<Int>()
    private val _champRotations = MutableLiveData<List<Int>>(_rawChampRotations)
    val championRotationData: LiveData<List<Int>> = _champRotations

    // 로테이션 정보
    fun champRotations(list: List<Int>) {
        for(item in list) {
            _rawChampRotations.add(item)
        }
    }

    private val _rawChampNameRotations = mutableStateListOf<ChampionList>()
    private val _champNameRotations = MutableLiveData<MutableList<ChampionList>>(_rawChampNameRotations)
    val champNameRotations : LiveData<MutableList<ChampionList>> = _champNameRotations

    fun setChampNameRotations(champNameList : List<ChampionList>) {
        _rawChampNameRotations.clear()
        for(item in champNameList) {
            _rawChampNameRotations.add(item)
        }
    }
}