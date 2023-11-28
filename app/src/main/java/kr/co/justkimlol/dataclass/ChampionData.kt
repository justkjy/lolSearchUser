package kr.co.justkimlol.dataclass

data class ChampionData(
    val `data`: List<Data>,
    val format: String,
    val type: String,
    val version: String
) {
    data class Data(
        val blurb: String,
        val id: String,
        val image: Image,
        val info: Info,
        val key: String,
        val name: String,
        val partype: String,
        //val stats: Stats,
        val tags: List<String>,
        val title: String,
        val version: String
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

//        data class Stats(
//            val armor: Int,
//            val armorperlevel: Double,
//            val attackdamage: Int,
//            val attackdamageperlevel: Double,
//            val attackrange: Int,
//            val attackspeed: Double,
//            val attackspeedperlevel: Double,
//            val crit: Int,
//            val critperlevel: Int,
//            val hp: Int,
//            val hpperlevel: Int,
//            //val hpregen: Int,
//            //val hpregenperlevel: Double,
//            val movespeed: Int,
//            val mp: Int,
//            //val mpperlevel: Double,
//            val mpregen: Double,
//            val mpregenperlevel: Double,
//            val spellblock: Int,
//            val spellblockperlevel: Double
//        )
    }
}