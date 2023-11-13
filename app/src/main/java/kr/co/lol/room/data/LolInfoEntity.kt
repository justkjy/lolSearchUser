package kr.co.lol.room.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey



// List 형태는 처음과 끝은 {}로 구분하며 스킨은 {no, name}행태로 한다.
// Tag는 [type, type]으로 구분한다.

@Entity(tableName= "LolChampInfoEntity")
data class LolChampInfoEntity (
    @PrimaryKey(autoGenerate = true) var no: Int? = null,
    @ColumnInfo(name="ChampKey") var champKeyId : Int,
    @ColumnInfo(name="ChampEngName") val nameEng: String,
    @ColumnInfo(name="ChampKorName") val nameKor: String,
    @ColumnInfo(name="ChampTitle") val title: String,
    @ColumnInfo(name="ChampStory") val story: String,
    @ColumnInfo(name="ChampTags") val tagList : String,
    @ColumnInfo(name="ChampSkinInfo") val skinList: String
)

@Entity(tableName= "LolChampInfoEntity")
data class LolChampSimpleInfoEntity (
    @ColumnInfo(name="ChampKey") var champKeyId : Int,
    @ColumnInfo(name="ChampEngName") val nameEng: String,
    @ColumnInfo(name="ChampKorName") val nameKor: String,
    @ColumnInfo(name="ChampTitle") val title: String,
    @ColumnInfo(name="ChampTags") val tagList : String,
)