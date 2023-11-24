package kr.co.lol.internet

import kr.co.lol.internet.ImageUrl.Companion.fullUrl



sealed class ImageUrl   {
    class ProfileImage(val userProfile: Int, val version: String): ImageUrl()
    class ChampionImage(val championImage: String, val version: String) : ImageUrl()
    class ChampionLoadingImage(val championImage: String) : ImageUrl()
    class ChampionTilesImg(val championImage: String) : ImageUrl()
    class ChampionLargeImage(val chapionImage: String, val championSkinNum: Int) : ImageUrl()
    class ChampionPassiveImage(val passiveImage: String, val version: String) : ImageUrl()
    class ChampionSpellsImage(val spells: String, val version: String) : ImageUrl()
    class MatchItem(val item: String, val version: String) : ImageUrl()
    class MatchSpell(val spell: String, val version: String) : ImageUrl()

    companion object {
        fun fullUrl(imageUrl: ImageUrl) : String = when(imageUrl) {
            is ImageUrl.ProfileImage -> {
                "https://ddragon.leagueoflegends.com/cdn/#version/img/profileicon/#profile.png".replace(
                    "#profile", "${imageUrl.userProfile}", true
                ).replace(
                    "#version", imageUrl.version
                )
            }

            is ImageUrl.ChampionImage -> {
                "https://ddragon.leagueoflegends.com/cdn/#version/img/champion/#championImage.png".replace(
                    "#championImage", imageUrl.championImage, true
                ).replace(
                    "#version", imageUrl.version
                )
            }


            is ImageUrl.ChampionLoadingImage -> {
                "https://ddragon.leagueoflegends.com/cdn/img/champion/loading/##_0.jpg".replace(
                    "##", imageUrl.championImage, true
                )
            }

            is ImageUrl.ChampionTilesImg-> {
                "https://ddragon.leagueoflegends.com/cdn/img/champion/tiles/##_0.jpg".replace(
                    "##", imageUrl.championImage
                )
            }

            is ImageUrl.ChampionLargeImage -> {
                "https://ddragon.leagueoflegends.com/cdn/img/champion/splash/#1_#2.jpg".replace(
                    "#1", imageUrl.chapionImage, true
                ).replace("#2", "${imageUrl.championSkinNum}")
            }

            is ImageUrl.ChampionPassiveImage -> {
                "https://ddragon.leagueoflegends.com/cdn/#version/img/passive/#championPassiveImage".replace(
                    "#version", imageUrl.version, true
                ).replace("#championPassiveImage", imageUrl.passiveImage)
            }

            is ImageUrl.ChampionSpellsImage -> {
                "https://ddragon.leagueoflegends.com/cdn/#version/img/spell/#championPassiveImage".replace(
                    "#version", imageUrl.version, true
                ).replace("#championPassiveImage", imageUrl.spells)
            }

            is ImageUrl.MatchItem -> {
                "https://ddragon.leagueoflegends.com/cdn/#version/img/item/#itemImage".replace(
                    "#version", imageUrl.version, true
                ).replace("#itemImage", imageUrl.item)
            }

            is ImageUrl.MatchSpell -> {
                "https://ddragon.leagueoflegends.com/cdn/#version/img/spell/#spellImage".replace(
                    "#version", imageUrl.version, true
                ).replace("#spellImage", imageUrl.spell)
            }
        }
    }
}

typealias  fullProfileUrl = ImageUrl.ProfileImage
typealias  fullChampUrl = ImageUrl.ChampionImage
typealias  fullChampLoadingUrl = ImageUrl.ChampionLoadingImage
typealias  fullChampTilesUrl = ImageUrl.ChampionTilesImg
typealias  fullChampSkinUrl = ImageUrl.ChampionLargeImage
typealias  fullChampPassiveUrl = ImageUrl.ChampionPassiveImage
typealias  fullChampSpellsUrl = ImageUrl.ChampionSpellsImage
typealias  fullMatchItemUrl = ImageUrl.MatchItem
typealias  matchSpell = ImageUrl.MatchSpell


fun profileUrl(userProfile: Int): String {
    val result = fullProfileUrl(userProfile, lolVersion)
    return fullUrl(result)
}

fun champUrl(championImage: String): String {
    val result = fullChampUrl(championImage, lolVersion)
    return fullUrl(result)
}

fun champLoadingUrl(championImage: String): String {
    val result = fullChampLoadingUrl(championImage)
    return fullUrl(result)
}
fun champTilesUrl(championImage: String): String {
    val result = fullChampTilesUrl(championImage)
    return fullUrl(result)
}

fun champSkinUrl(championImage: String, skinNumber: Int): String {
    val result = fullChampSkinUrl(championImage, skinNumber)
    return fullUrl(result)
}

fun champPassiveUrl(passive: String) : String {
    val result  = fullChampPassiveUrl(passive, lolVersion)
    return fullUrl(result)
}

fun champSpellsUrl(spells: String) : String {
    val result  = fullChampSpellsUrl(spells, lolVersion)
    return fullUrl(result)
}

fun matchItem(item: String) : String {
    val result  = fullMatchItemUrl(item, lolVersion)
    return fullUrl(result)
}

fun matchSpell(spell: String) : String {
    val result  = matchSpell(spell, lolVersion)
    return fullUrl(result)
}


