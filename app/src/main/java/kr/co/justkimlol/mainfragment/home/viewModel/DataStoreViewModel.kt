package kr.co.justkimlol.mainfragment.home.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kr.co.justkimlol.mainfragment.home.RegisterUserStoreData

class DataStoreViewModel(application: Application) : AndroidViewModel(application) {
    private val myDataStore = RegisterUserStoreData(application)

    fun insert(userName : String) = viewModelScope.launch {
        myDataStore.insertUserName(userName)
    }

    val read = myDataStore.getUserName.asLiveData()
}