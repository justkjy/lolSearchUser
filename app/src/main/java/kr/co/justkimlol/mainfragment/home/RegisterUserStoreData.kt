package kr.co.justkimlol.mainfragment.home

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore : DataStore<Preferences> by preferencesDataStore("user_pref")

class RegisterUserStoreData(context : Context) {
    private val mDataStore : DataStore<Preferences> = context.dataStore

    private val USER_NAME_KEY = stringPreferencesKey("USER_NAME")
    private val TAG_LINE_KEY = stringPreferencesKey("TAG_LINE")

    suspend fun insertUserInfo(userName : String, tagName : String) {
        mDataStore.edit { setting ->
            setting[USER_NAME_KEY] = userName
            setting[TAG_LINE_KEY] = tagName
        }
    }

    val getUserName : Flow<String> = mDataStore.data.map {
        val userName = it[USER_NAME_KEY] ?: ""
        userName
    }

    val getTagName : Flow<String> = mDataStore.data.map {
        val tagLine = it[TAG_LINE_KEY] ?: ""
        tagLine
    }
}