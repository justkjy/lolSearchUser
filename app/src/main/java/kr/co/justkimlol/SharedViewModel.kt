package kr.co.justkimlol

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kr.co.justkimlol.internet.TAG

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
    val _userId = MutableLiveData("")
    val userId: LiveData<String> = _userId

    val inputUserId: (id:String) -> (Unit) = {it ->
        _userId.value = it
    }

    // puuid
    private val _puuid = MutableLiveData("")
    val puuid: LiveData<String> = _puuid

    // 사용 프로필 아이콘 아이디
    private val _profileIconId = MutableLiveData(0)
    val profileId: LiveData<Int> = _profileIconId

    // 사용 프로필 숙련도
    private val _summonerLevel = MutableLiveData(0)
    val summonerLevel: LiveData<Int> = _summonerLevel

    // 티어
    private val _loltear = MutableLiveData("")
    val loltear: LiveData<String> = _loltear

    // 랭크
    private val _lolrank = MutableLiveData("")
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

    private val _rawChampEngList = mutableStateListOf<String>()
    private val _champEngList = MutableLiveData<List<String>>(_rawChampEngList)
    val champEngList: LiveData<List<String>> = _champEngList // 사용

    private val _rawMatchList = mutableStateListOf<String>()
    private val _matchList = MutableLiveData<List<String>>(_rawMatchList)
    val matchList: LiveData<List<String>> = _matchList

    fun sharedInputUserInfo(userId: String, puuId : String, profileId : Int, summonerLevel : Int, tear: String,
                            rank: String, win: Int, losses: Int, topChampion: List<Int>,
                            topEngChamp: List<String>, matchList: List<String>) {
        this._userId.value = userId
        this._puuid.value = puuId
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

        _rawMatchList.clear()
        _rawMatchList.addAll(matchList)
        _matchList.value = mutableListOf<String>().also {
            it.addAll(_rawMatchList)
        }
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