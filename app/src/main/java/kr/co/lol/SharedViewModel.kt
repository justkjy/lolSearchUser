package kr.co.lol

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kr.co.lol.internet.TAG
import kr.co.lol.ui.home.viewModel.useChampLevel

class SharedViewModel : ViewModel() {
    ////////////////////////////////////////////////////////////////////////////////////////////////
    // for UserInfo/////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    // 로그인한 API key
    private val _apiKey = MutableLiveData<String>("")
    val apiKey : LiveData<String> = _apiKey
    val inputApiKey: (key:String) -> (Unit) = {it ->
        _apiKey.value = it
    }

    // user Id
    val _userId = MutableLiveData<String>("")
    val userId: LiveData<String> = _userId
    val inputUserId: (id:String) -> (Unit) = {it ->
        _userId.value = it
    }

    // 사용 프로필 아이콘 아이디
    private val _profileIconId = MutableLiveData<Int>(0)
    val profileId: LiveData<Int> = _profileIconId

    // 사용 프로필 숙련도
    private val _summonerLevel = MutableLiveData<Int>(0)
    val summonerLevel: LiveData<Int> = _summonerLevel

    // 티어
    private val _loltear = MutableLiveData<String>("")
    val loltear: LiveData<String> = _loltear

    // 랭크
    private val _lolrank = MutableLiveData<String>("")
    val lolrank : LiveData<String> = _lolrank

    // wins
    private val _lolWin = MutableLiveData<Int>(0)
    val lolWin: LiveData<Int> = _lolWin

    // losses
    private val _lolLosses = MutableLiveData<Int>(0)
    val lolLosses: LiveData<Int> = _lolLosses

    // 챔프 숙련도 체크
    private val _champTopTenList = MutableStateFlow<MutableList<Int>>(mutableListOf())
    val champTopTenList: StateFlow<MutableList<Int>> = _champTopTenList

//    // 챔프 숙련도 영문명
//    private val _champEngList = MutableStateFlow<MutableList<String>>(mutableListOf())
//    val champEngList: StateFlow<MutableList<String>> = _champEngList

    private val _rawChampEngList = mutableStateListOf<String>()
    private val _champEngList = MutableLiveData<List<String>>(_rawChampEngList)
    val champEngList: LiveData<List<String>> = _champEngList // 사용




    fun sharedInputUserInfo(userId: String, profileId : Int, summonerLevel : Int, tear: String, rank: String,
                      win: Int, losses: Int, topChampion: List<Int>, topEngChamp: List<String>) {
        this._userId.value = userId
        this._profileIconId.value = profileId
        this._summonerLevel.value = summonerLevel
        this._loltear.value = tear
        this._lolrank.value = rank
        this._lolWin.value = win
        this._lolLosses.value = losses

        this._champTopTenList.value.addAll(topChampion)

        _rawChampEngList.clear()
        _rawChampEngList.addAll(topEngChamp)
        _champEngList.value = mutableListOf<String>().also {
            it.addAll(_rawChampEngList)
        }
        Log.i(TAG, "----------")
        //this._champEngList.value.addAll(topEngChamp)
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
        //TODO("All이 되지 않음 ")
        for(item in list) {
            _rawChampRotations.add(item)
        }
    }


}