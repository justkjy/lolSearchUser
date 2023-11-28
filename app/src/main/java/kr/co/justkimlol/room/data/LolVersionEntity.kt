package kr.co.justkimlol.room.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName= "LolVersionEntity")
data class LolVersionEntity (
    @PrimaryKey(autoGenerate = true) var no: Int? = null,
    @ColumnInfo(name = "version") var version: String,
    @ColumnInfo(name = "updateState") var update: Boolean = false
)

data class InfoVersion(val no : Int, val version: String, val runPatch : Boolean)