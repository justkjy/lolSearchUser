package kr.co.lol.dataclass

data class ChampAllListData(
    val champKey: Int,
    val nameKor : String,
    val nameEng : String,
    val title : String,
    val _tag : String
) {
    val deckResourceList =  mutableListOf<Int>()
    val tag : List<String>
        get() = _tag.split("\n")
}
