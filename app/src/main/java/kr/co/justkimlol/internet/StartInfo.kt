package kr.co.justkimlol.internet

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.File
import java.io.FileReader
import java.io.FileWriter

fun writeTextFile(directory: String, filename: String, content: String) {
    val dir = File(directory)
    if(!dir.exists()){
        dir.mkdirs()
    }

    val writer = FileWriter("$directory/$filename")
    val buffer = BufferedWriter(writer)
    buffer.write(content)
    buffer.close()
}

fun readTextFile(fullPath: String): String {
    val file = File(fullPath)
    if (!file.exists()) return ""
    val reader = FileReader(file)
    val buffer = BufferedReader(reader)
    var temp = ""
    val result = StringBuffer()
    while(true) {
        temp = buffer.readLine()
        if (temp == null) break;
        else result.append(buffer)
    }
    buffer.close()
    return result.toString()
}

class DataStoreManager(context:Context) {

    private val Context.dataStore : DataStore<Preferences> by preferencesDataStore(name = "initInfo")
    private val dataStore = context.dataStore

    companion object {
        val VERSION_INFO_KEY = stringPreferencesKey("VERSION")
        val JSON_BODY_SIZE_KEY = intPreferencesKey("SIZE")
        val JSON_BODY_KEY = stringPreferencesKey("JSONBODY")

    }

    suspend fun championInfoEdit(
        version : String,
        bodySize : Int,
        jsonBody : String
    ) {
        dataStore.edit { setting ->
            setting[VERSION_INFO_KEY] = version
            setting[JSON_BODY_SIZE_KEY] = bodySize
            setting[JSON_BODY_KEY] = jsonBody
        }
    }

    val initVersion: Flow<String?> = dataStore.data.map { preferences ->
        preferences[VERSION_INFO_KEY]
    }

    val initSize: Flow<Int?> = dataStore.data.map {  preferences ->
        preferences[JSON_BODY_SIZE_KEY]
    }

    val initJsonBody: Flow<String?> = dataStore.data.map { preferences ->
        preferences[JSON_BODY_KEY]
    }

}