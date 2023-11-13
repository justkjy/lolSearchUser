package kr.co.lol.dataclass

data class ChampionUnitData(
    val `data`: Data,
    val format: String,
    val type: String,
    val version: String
) {
    data class Data(
        val Main: Mainin
    ) {
        data class Mainin(
            val allytips: List<String>,
            val blurb: String,
            val enemytips: List<String>,
            val id: String,
            val image: Image,
            val info: Info,
            val key: String,
            val lore: String,
            val name: String,
            val partype: String,
            val passive: Passive,
            val recommended: List<Any>,
            val skins: List<Skin>,
            val spells: List<Spell>,
            val stats: Stats,
            val tags: List<String>,
            val title: String
        ) {
            data class Image(
                val full: String,
                val group: String,
                val h: Int,
                val sprite: String,
                val w: Int,
                val x: Int,
                val y: Int
            )

            data class Info(
                val attack: Int,
                val defense: Int,
                val difficulty: Int,
                val magic: Int
            )

            data class Passive(
                val description: String,
                val image: Image,
                val name: String
            ) {
                data class Image(
                    val full: String,
                    val group: String,
                    val h: Int,
                    val sprite: String,
                    val w: Int,
                    val x: Int,
                    val y: Int
                )
            }

            data class Skin(
                val chromas: Boolean,
                val id: String,
                val name: String,
                val num: Int
            )

            data class Spell(
                val cooldown: List<Int>,
                val cooldownBurn: String,
                val cost: List<Int>,
                val costBurn: String,
                val costType: String,
                val datavalues: Datavalues,
                val description: String,
                val effect: List<List<Int>>,
                val effectBurn: List<String>,
                val id: String,
                val image: Image,
                val leveltip: Leveltip,
                val maxammo: String,
                val maxrank: Int,
                val name: String,
                val range: List<Int>,
                val rangeBurn: String,
                val resource: String,
                val tooltip: String,
                val vars: List<Any>
            ) {
                class Datavalues

                data class Image(
                    val full: String,
                    val group: String,
                    val h: Int,
                    val sprite: String,
                    val w: Int,
                    val x: Int,
                    val y: Int
                )

                data class Leveltip(
                    val effect: List<String>,
                    val label: List<String>
                )
            }

            data class Stats(
                val armor: Int,
                val armorperlevel: Double,
                val attackdamage: Int,
                val attackdamageperlevel: Int,
                val attackrange: Int,
                val attackspeed: Double,
                val attackspeedperlevel: Double,
                val crit: Int,
                val critperlevel: Int,
                val hp: Int,
                val hpperlevel: Int,
                val hpregen: Int,
                val hpregenperlevel: Int,
                val movespeed: Int,
                val mp: Int,
                val mpperlevel: Int,
                val mpregen: Int,
                val mpregenperlevel: Int,
                val spellblock: Int,
                val spellblockperlevel: Double
            )
        }
    }
}