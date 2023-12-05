package kr.co.justkimlol.viewModel.user.match

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.yml.charts.common.model.Point
import kotlinx.coroutines.launch

class MatchChampViewModel : ViewModel() {

    private val _totalCountGame  = MutableLiveData(0)
    val totalCountGame  : LiveData<Int> = _totalCountGame

    private val _totalWin = MutableLiveData<Int>(0)
    val totalWin : LiveData<Int> = _totalWin

    private val _totalLose = MutableLiveData(0)
    val totalLose = _totalLose

    private var champion1 : String = ""

    private val _champ1WinCount = MutableLiveData(0)
    val champ1WinCount : LiveData<Int> = _champ1WinCount

    private val _champ1LoseCount = MutableLiveData(0)
    val champ1LoseCount : LiveData<Int> = _champ1LoseCount

    private var champion2 : String = ""

    private val _champ2WinCount = MutableLiveData(0)
    val champ2WinCount : LiveData<Int> = _champ2WinCount

    private val _champ2LoseCount = MutableLiveData(0)
    val champ2LoseCount : LiveData<Int> = _champ2LoseCount

    private var champion3 : String = ""

    private val _champ3WinCount = MutableLiveData(0)
    val champ3WinCount : LiveData<Int> = _champ3WinCount

    private val _champ3LoseCount = MutableLiveData(0)
    val champ3LoseCount : LiveData<Int> = _champ3LoseCount

    private val _rawKillInfoChart = mutableListOf(Point(0f, 0f))
    private val _killInfoChart = MutableLiveData<List<Point>>(_rawKillInfoChart)
    val killInfoChart: LiveData<List<Point>> = _killInfoChart

    init {

    }

    fun insertMost3Champ(champ1 : String, champ2 : String, champ3 : String) {
        champion1 = champ1
        champion2 = champ2
        champion3 = champ3
    }

    fun insertChartUpdate(kill: Int, step: Int, description: String) {
        viewModelScope.launch {
            _rawKillInfoChart.add(Point(step.toFloat(), kill.toFloat(), description))
            _killInfoChart.value = ArrayList(_rawKillInfoChart)
        }
    }

    private fun insertWinUpdate(
        championEngName : String,
        win: Int = 0,
        lose : Int = 0,
        kill : Int = 0,
        death : Int = 0
    ) {
        viewModelScope.launch {
            _totalCountGame.value = _totalCountGame.value?.inc()

            _totalWin.value = _totalWin.value?.plus(win)
            _totalLose.value = _totalLose.value?.plus(lose)

            if(champion1 == championEngName) {
                _champ1WinCount.value = _champ1WinCount.value?.plus(win)
                _champ1LoseCount.value = _champ1LoseCount.value?.plus(lose)
            }

            if(champion2 == championEngName) {
                _champ2WinCount.value = _champ2WinCount.value?.plus(win)
                _champ2LoseCount.value = _champ2LoseCount.value?.plus(lose)
            }

            if(champion3 == championEngName) {
                _champ3WinCount.value = _champ3WinCount.value?.plus(win)
                _champ3LoseCount.value = _champ3LoseCount.value?.plus(lose)
            }

            insertChartUpdate(kill, totalCountGame.value!!, championEngName)
        }
    }

    val insertChampUpdate : (index: Int, championEngName: String, win : Boolean, kill: Int, death: Int) -> Unit
            = { index, championEngName, win, kill, death ->
        var winValue = 0
        var loseValue = 0
        val killValue = kill
        val deathValue = death

        if(index + 1 > totalCountGame.value!!) {
            if (win) {
                winValue = 1
            } else {
                loseValue = 1
            }
            insertWinUpdate(championEngName, winValue, loseValue, killValue, deathValue)
        }
    }
}