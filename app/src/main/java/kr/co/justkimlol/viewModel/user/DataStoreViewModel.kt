package kr.co.justkimlol.viewModel.user

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kr.co.justkimlol.mainfragment.home.RegisterUserStoreData

class DataStoreViewModel(application: Application) : AndroidViewModel(application) {
    private val myDataStore = RegisterUserStoreData(application)

    fun insert(userName : String, tagLine: String) = viewModelScope.launch {
        myDataStore.insertUserInfo(userName, tagLine)
    }
    val readUserName = myDataStore.getUserName.asLiveData()
    val readTagLine = myDataStore.getTagName.asLiveData()

}