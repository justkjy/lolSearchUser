package kr.co.lol.dataclass

//passiveOrSpells = true이면 패시브
//                  false이면 스팰
data class SpellsData(
    val passiveOrSpells : Boolean,
    val id: String,
    val name : String,
    val description :String,
    val image : String,
    val spellsQtooltip : String = "",

)
