package kr.co.justkimlol.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kr.co.justkimlol.room.data.LolChampInfoEntity
import kr.co.justkimlol.room.data.LolChampSimpleInfoEntity
import kr.co.justkimlol.room.data.LolVersionEntity

@Dao
interface LolInfoDao {
    // version Info
    @Query("SELECT * FROM LolVersionEntity ORDER BY version DESC ")
    fun getVersionAll() : List<LolVersionEntity>

    @Insert
    fun insertVersion(lolVersionInfo: LolVersionEntity)

    @Delete
    fun deleteVersion(lolVersionInfo: LolVersionEntity)

    @Update
    fun patchVersionState(updateRecord: LolVersionEntity)

    ///LolChampInfo////////////////////////////////////////////////////////////////////////////
    @Query("SELECT ChampKey, ChampEngName, ChampKorName, ChampTitle, ChampTags FROM LolChampInfoEntity")
    fun getChampAll() : List<LolChampSimpleInfoEntity>

    @Query("SELECT * FROM LolChampInfoEntity WHERE ChampEngName = :champEngName")
    fun getChampInfo(champEngName: String) : List<LolChampInfoEntity>

    @Query("SELECT * FROM LolChampInfoEntity WHERE ChampKey = :champKey")
    fun getChampItem(champKey: Int) : List<LolChampInfoEntity>

    @Insert
    fun insertChampInfo(lolChampInfo: LolChampInfoEntity)

    @Insert
    fun insertChampAll(lolChampList : List<LolChampInfoEntity>)

    @Query("DELETE FROM LolChampInfoEntity")
    fun deleteChampInfo()
}