package kr.co.lol.internet

import kr.co.lol.internet.ImageUrl.Companion.fullUrl

sealed class ImageUrl   {
    class ProfileImage(val userProfile: Int): ImageUrl()
    class ChampionImage(val championImage: String) : ImageUrl()
    class ChampionLoadingImage(val championImage: String) : ImageUrl()
    class ChampionLargeImage(val chapionImage: String, val championSkinNum: Int) : ImageUrl()

    companion object {
        fun fullUrl(imageUrl: ImageUrl) : String = when(imageUrl) {
            is ImageUrl.ProfileImage -> {
                "https://ddragon.leagueoflegends.com/cdn/13.21.1/img/profileicon/##.png".replace(
                    "##", "${imageUrl.userProfile}", true
                )
            }

            is ImageUrl.ChampionImage -> {
                "https://ddragon.leagueoflegends.com/cdn/13.21.1/img/champion/##.png".replace(
                    "##", imageUrl.championImage, true
                )
            }


            is ImageUrl.ChampionLoadingImage -> {
                "https://ddragon.leagueoflegends.com/cdn/img/champion/loading/##_0.jpg".replace(
                    "##", imageUrl.championImage, true
                )
            }

            is ImageUrl.ChampionLargeImage -> {
                "https://ddragon.leagueoflegends.com/cdn/img/champion/splash/#1_#2.jpg".replace(
                    "#1", imageUrl.chapionImage, true
                ).replace("#2", "${imageUrl.championSkinNum}")
            }
        }
    }
}

typealias  fullProfileUrl = ImageUrl.ProfileImage
typealias  fullChampUrl = ImageUrl.ChampionImage
typealias  fullChampLoadingUrl = ImageUrl.ChampionLoadingImage
typealias  fullChampSkinUrl = ImageUrl.ChampionLargeImage


fun profileUrl(userProfile: Int): String {
    val result = fullProfileUrl(userProfile)
    return fullUrl(result)
}

fun champUrl(championImage: String): String {
    val result = fullChampUrl(championImage)
    return fullUrl(result)
}

fun champLoadingUrl(championImage: String): String {
    val result = fullChampLoadingUrl(championImage)
    return fullUrl(result)
}

fun champSkinUrl(championImage: String, skinNumber: Int): String {
    val result = fullChampSkinUrl(championImage, skinNumber)
    return fullUrl(result)
}


