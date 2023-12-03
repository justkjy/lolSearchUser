package kr.co.justkimlol.dataclass

data class ChampAllListData(
    val champKey: Int,
    val nameKor : String,
    val nameEng : String,
    val title : String,
    val tags : String
) {
    val tagList : List<String>
        get() = tags.split("\n")
}
