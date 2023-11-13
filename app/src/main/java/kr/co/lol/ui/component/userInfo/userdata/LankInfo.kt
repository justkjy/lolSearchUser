package kr.co.lol.ui.component.userInfo.userdata

import kr.co.lol.R
import kr.co.lol.ui.component.championInfo.tierResource

/*
// tier : 티어 정보
// lank : 사용자 랭크
// summonerLevel : 사용자 숙련도
// profileId: 플로필 이미지 아이디 번호
// championName: 챔피언 숙련도 10개
 */
data class RankInfo(var _tier: String,
                    val rank: String,
                    val summonerLevel:Int,
                    val profileId: Int,
                    val championName:MutableList<String>,
                    val tftUsedeck:MutableList<String>
) {
    constructor(
        _tier: String,
        lank: String,
        summonerLevel:Int,
        profileId: Int,
        championName: MutableList<String>
    ) : this(_tier, lank, summonerLevel, profileId, championName, tftUsedeck = mutableListOf<String>().let {
        val tftdack = mutableListOf<String>("brawleremblem", "rogueemblem", "bastionemblem", "ixtalemblem", "voidemblem",
            "rogueemblem")
        tftdack
    }) { }

        val tier : String
        get() = _tier.let {
            when(it) {
                "CHALLENGER" -> "첼린저"
                "GRANDMASTER" -> "그랜드마스터"
                "MASTER" -> "마스터"
                "DIAMOND" -> "다이아 몬드"
                "EMERALD" -> "에메랄드"
                "PLATINUM" -> "플레티넘"
                "GOLD"-> "골드"
                "SILVER"-> "실버"
                "BRONZE"->"브론즈"
                "IRON" -> "아이언"
                else-> "랭크외 순위권 외"
            }
        }

    val tierResourceId
        get() = _tier.let{
            tierResource(it)
            when(it) {
                "CHALLENGER" -> R.drawable.rank_challenger
                "GRANDMASTER" -> R.drawable.rank_grandmaster
                "MASTER" -> R.drawable.rank_master
                "DIAMOND" -> R.drawable.rank_diamond
                "EMERALD" -> R.drawable.rank_emerald
                "PLATINUM" -> R.drawable.rank_platinum
                "GOLD"-> R.drawable.rank_gold
                "SILVER"-> R.drawable.rank_silver
                "BRONZE"-> R.drawable.rank_bronze
                "IRON" -> R.drawable.rank_iron
                else-> R.drawable.user_cowboy
            }
        }

    val tftResource: () -> (MutableList<Int>) =  {
        val deckResourceList =  mutableListOf<Int>()
            for(dackItem in tftUsedeck) {
                val dackResource = when (dackItem) {
                    "ixtalemblem" -> R.drawable.tft9t_item_ixtalemblem
                    "bastionemblem" -> R.drawable.tft9_item_bastionemblem
                    "brawleremblem" -> R.drawable.tft9_item_brawleremblem
                    "deadeyeemblem" -> R.drawable.tft9_item_deadeyeemblem
                    "freljordemblem" -> R.drawable.tft9_item_freljordemblem
                    "piltoveremblem" -> R.drawable.tft9_item_piltoveremblem
                    "rogueemblem" -> R.drawable.tft9_item_rogueemblem
                    "shadowislesemblem" -> R.drawable.tft9_item_shadowislesemblem
                    "slayeremblem" -> R.drawable.tft9_item_slayeremblem
                    "strategistemblem" -> R.drawable.tft9_item_strategistemblem
                    "targonemblem" -> R.drawable.tft9_item_targonemblem
                    "trickshotemblem" -> R.drawable.tft9_item_trickshotemblem
                    "voidemblem" -> R.drawable.tft9_item_voidemblem
                    "zaunemblem" -> R.drawable.tft9_item_zaunemblem
                    "vanquisheremblem" -> R.drawable.tft9_item_vanquisheremblem
                    "sorcereremblem" -> R.drawable.tft9_item_sorcereremblem
                    "noxusemblem" -> R.drawable.tft9_item_noxusemblem
                    "ioniaemblem" -> R.drawable.tft9_item_ioniaemblem
                    "bilgewateremblem" -> R.drawable.tft9_item_bilgewateremblem
                    "armorclademblem" -> R.drawable.tft9_item_armorclademblem

                    else -> R.drawable.user_cowboy
                }
                deckResourceList.add(dackResource)
            }
        deckResourceList
    }
}


