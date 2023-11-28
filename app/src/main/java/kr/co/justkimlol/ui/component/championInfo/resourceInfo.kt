package kr.co.justkimlol.ui.component.championInfo

import kr.co.justkimlol.R


sealed class ResourceInfo {
    class ChampPosition(val tag: String) : ResourceInfo()
    class TierIcon(val tier : String) : ResourceInfo()
    class skillLevelIcon(val level : Int) : ResourceInfo()

    companion object {
        fun converResource(champ: ResourceInfo): Int =
            when (champ) {
                is ResourceInfo.ChampPosition -> {
                    positionIn(champ.tag)
                }

                is ResourceInfo.TierIcon -> {
                    tearIconIn(champ.tier)
                }

                is ResourceInfo.skillLevelIcon -> {
                    eogBoderIn(champ.level)
                }
            }


        private fun positionIn(tierName: String) = when (tierName) {
            "Support" -> R.drawable.statmodshealthscalingicon

            "Mage" -> R.drawable.statmodsmagicresicon

            "Fighter" -> R.drawable.statmodsattackspeedicon

            "Assassin" -> R.drawable.statmodsadaptiveforceicon

            "Tank" -> R.drawable.statmodsarmoricon

            "Marksman"-> R.drawable.statmodscdrscalingicon

            else -> {
                R.drawable.warrior_helmet
            }
        }

        private fun tearIconIn(tearIcon: String) = when (tearIcon) {
            "CHALLENGER" -> R.drawable.rank_challenger

            "GRANDMASTER" -> R.drawable.rank_grandmaster

            "MASTER" -> R.drawable.rank_master

            "DIAMOND" -> R.drawable.rank_diamond

            "EMERALD" -> R.drawable.rank_emerald

            "PLATINUM" -> R.drawable.rank_platinum

            "GOLD" -> R.drawable.rank_gold

            "SILVER" -> R.drawable.rank_silver

            "BRONZE" -> R.drawable.rank_bronze

            "IRON" -> R.drawable.rank_iron

            else -> R.drawable.rank_iron
        }

        private fun eogBoderIn(eog: Int) = when (eog) {

            in 0..29 -> R.drawable.eog_border_1_4k

            in 30..49 -> R.drawable.eog_border_30_4k

            in 50..74 -> R.drawable.eog_border_50_4k

            in 75..99 -> R.drawable.eog_border_75_4k

            in 100..124 -> R.drawable.eog_border_100_4k

            in 125..149 -> R.drawable.eog_border_125_4k

            !in 0..149  -> R.drawable.eog_border_150_4k

            else -> R.drawable.eog_border_1_4k

        }
    }
}




typealias resourcePosition = ResourceInfo.ChampPosition
typealias resourceTier = ResourceInfo.TierIcon
typealias resourceEog= ResourceInfo.skillLevelIcon

///////////////////////////////////////////////////////////use
fun champTagResource(tag: MutableList<String>) : MutableList<Int> {
    val resourceIdList = mutableListOf<Int>().let { items ->
        for(item in tag) {
            val id = resourcePosition(item)
            items.add(ResourceInfo.converResource(id))
        }
        items
    }
    return resourceIdList
}

fun tierResource(tier: String) : Int {
    val tierValue = resourceTier(tier)
    return ResourceInfo.converResource(tierValue)
}

fun skillLevelResource(level: Int) : Int {
    val tierValue = resourceEog(level)
    return ResourceInfo.converResource(tierValue)
}