package kr.co.justkimlol.room.data

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
    @ColumnInfo(name="ChampSkinInfo") val skinList: String,

    /////////////////////////////////////////////////////////스펠
    @ColumnInfo(name="PassiveName") val passiveName: String,
    @ColumnInfo(name="PassiveDescription") val passiveDescription: String,
    @ColumnInfo(name="PassiveImage") val passiveImage: String,

    @ColumnInfo(name="SpellsQid") val spellsQid: String,
    @ColumnInfo(name="SpellsQDescription") val spellsQDescription: String,
    @ColumnInfo(name="SpellsQName") val spellsQName: String,
    @ColumnInfo(name="SpellsQImage") val spellsQImage: String,
    @ColumnInfo(name="SpellsQtooltip") val spellsQtooltip: String,

    @ColumnInfo(name="SpellsWid") val spellsWid: String,
    @ColumnInfo(name="SpellsWDescription") val spellsWDescription: String,
    @ColumnInfo(name="SpellsWName") val spellsWName: String,
    @ColumnInfo(name="SpellsWImage") val spellsWImage: String,
    @ColumnInfo(name="SpellsWtooltip") val spellsWtooltip: String,

    @ColumnInfo(name="SpellsEid") val spellsEid: String,
    @ColumnInfo(name="SpellsEDescription") val spellsEDescription: String,
    @ColumnInfo(name="SpellsEName") val spellsEName: String,
    @ColumnInfo(name="SpellsEImage") val spellsEImage: String,
    @ColumnInfo(name="SpellsEtooltip") val spellsEtooltip: String,

    @ColumnInfo(name="SpellsRid") val spellsRid: String,
    @ColumnInfo(name="SpellsRDescription") val spellsRDescription: String,
    @ColumnInfo(name="SpellsRName") val spellsRName: String,
    @ColumnInfo(name="SpellsRImage") val spellsRImage: String,
    @ColumnInfo(name="SpellsRtooltip") val spellsRtooltip: String,
)

@Entity(tableName= "LolChampInfoEntity")
data class LolChampSimpleInfoEntity (
    @ColumnInfo(name="ChampKey") var champKeyId : Int,
    @ColumnInfo(name="ChampEngName") val nameEng: String,
    @ColumnInfo(name="ChampKorName") val nameKor: String,
    @ColumnInfo(name="ChampTitle") val title: String,
    @ColumnInfo(name="ChampTags") val tagList : String,
)