package kr.co.justkimlol.dataclass

/* json 정보
//[
//    {
//        "puuid": "AH3tjkvRgXPgrUEaKIeZgVJcJeRKYJFiX27RXVs4yvZuF5GqueBBY7oL4SHci2RM9LdTPW5FsL3XhQ",
//        "championId": 74,
//        "championLevel": 6,
//        "championPoints": 506250,
//        "lastPlayTime": 1694963694000,
//        "championPointsSinceLastLevel": 484650,
//        "championPointsUntilNextLevel": 0,
//        "chestGranted": true,
//        "tokensEarned": 3,
//        "summonerId": "g-kninA3SR9LQWcDNOTajWVvEp_L4ruapRyhNgahPeTZIA"
//    },
//    {
//        "puuid": "AH3tjkvRgXPgrUEaKIeZgVJcJeRKYJFiX27RXVs4yvZuF5GqueBBY7oL4SHci2RM9LdTPW5FsL3XhQ",
//        "championId": 3,
//        "championLevel": 7,
//        "championPoints": 215797,
//        "lastPlayTime": 1697205028000,
//        "championPointsSinceLastLevel": 194197,
//        "championPointsUntilNextLevel": 0,
//        "chestGranted": true,
//        "tokensEarned": 0,
//        "summonerId": "g-kninA3SR9LQWcDNOTajWVvEp_L4ruapRyhNgahPeTZIA"
//    },
//    {
//        "puuid": "AH3tjkvRgXPgrUEaKIeZgVJcJeRKYJFiX27RXVs4yvZuF5GqueBBY7oL4SHci2RM9LdTPW5FsL3XhQ",
//        "championId": 30,
//        "championLevel": 5,
//        "championPoints": 180976,
//        "lastPlayTime": 1692733815000,
//        "championPointsSinceLastLevel": 159376,
//        "championPointsUntilNextLevel": 0,
//        "chestGranted": false,
//        "tokensEarned": 0,
//        "summonerId": "g-kninA3SR9LQWcDNOTajWVvEp_L4ruapRyhNgahPeTZIA"
//    },
//    {
//        "puuid": "AH3tjkvRgXPgrUEaKIeZgVJcJeRKYJFiX27RXVs4yvZuF5GqueBBY7oL4SHci2RM9LdTPW5FsL3XhQ",
//        "championId": 40,
//        "championLevel": 6,
//        "championPoints": 170435,
//        "lastPlayTime": 1692477719000,
//        "championPointsSinceLastLevel": 148835,
//        "championPointsUntilNextLevel": 0,
//        "chestGranted": false,
//        "tokensEarned": 3,
//        "summonerId": "g-kninA3SR9LQWcDNOTajWVvEp_L4ruapRyhNgahPeTZIA"
//    },
//    {
//        "puuid": "AH3tjkvRgXPgrUEaKIeZgVJcJeRKYJFiX27RXVs4yvZuF5GqueBBY7oL4SHci2RM9LdTPW5FsL3XhQ",
//        "championId": 117,
//        "championLevel": 5,
//        "championPoints": 147315,
//        "lastPlayTime": 1693149100000,
//        "championPointsSinceLastLevel": 125715,
//        "championPointsUntilNextLevel": 0,
//        "chestGranted": false,
//        "tokensEarned": 2,
//        "summonerId": "g-kninA3SR9LQWcDNOTajWVvEp_L4ruapRyhNgahPeTZIA"
//    },
//    {
//        "puuid": "AH3tjkvRgXPgrUEaKIeZgVJcJeRKYJFiX27RXVs4yvZuF5GqueBBY7oL4SHci2RM9LdTPW5FsL3XhQ",
//        "championId": 516,
//        "championLevel": 5,
//        "championPoints": 146803,
//        "lastPlayTime": 1697206970000,
//        "championPointsSinceLastLevel": 125203,
//        "championPointsUntilNextLevel": 0,
//        "chestGranted": false,
//        "tokensEarned": 0,
//        "summonerId": "g-kninA3SR9LQWcDNOTajWVvEp_L4ruapRyhNgahPeTZIA"
//    },
//    {
//        "puuid": "AH3tjkvRgXPgrUEaKIeZgVJcJeRKYJFiX27RXVs4yvZuF5GqueBBY7oL4SHci2RM9LdTPW5FsL3XhQ",
//        "championId": 57,
//        "championLevel": 6,
//        "championPoints": 137128,
//        "lastPlayTime": 1694967796000,
//        "championPointsSinceLastLevel": 115528,
//        "championPointsUntilNextLevel": 0,
//        "chestGranted": true,
//        "tokensEarned": 0,
//        "summonerId": "g-kninA3SR9LQWcDNOTajWVvEp_L4ruapRyhNgahPeTZIA"
//    },
//    {
//        "puuid": "AH3tjkvRgXPgrUEaKIeZgVJcJeRKYJFiX27RXVs4yvZuF5GqueBBY7oL4SHci2RM9LdTPW5FsL3XhQ",
//        "championId": 350,
//        "championLevel": 7,
//        "championPoints": 125215,
//        "lastPlayTime": 1694661430000,
//        "championPointsSinceLastLevel": 103615,
//        "championPointsUntilNextLevel": 0,
//        "chestGranted": false,
//        "tokensEarned": 0,
//        "summonerId": "g-kninA3SR9LQWcDNOTajWVvEp_L4ruapRyhNgahPeTZIA"
//    },
//    {
//        "puuid": "AH3tjkvRgXPgrUEaKIeZgVJcJeRKYJFiX27RXVs4yvZuF5GqueBBY7oL4SHci2RM9LdTPW5FsL3XhQ",
//        "championId": 201,
//        "championLevel": 6,
//        "championPoints": 116310,
//        "lastPlayTime": 1687684534000,
//        "championPointsSinceLastLevel": 94710,
//        "championPointsUntilNextLevel": 0,
//        "chestGranted": false,
//        "tokensEarned": 3,
//        "summonerId": "g-kninA3SR9LQWcDNOTajWVvEp_L4ruapRyhNgahPeTZIA"
//    },
//    {
//        "puuid": "AH3tjkvRgXPgrUEaKIeZgVJcJeRKYJFiX27RXVs4yvZuF5GqueBBY7oL4SHci2RM9LdTPW5FsL3XhQ",
//        "championId": 106,
//        "championLevel": 5,
//        "championPoints": 80069,
//        "lastPlayTime": 1687786946000,
//        "championPointsSinceLastLevel": 58469,
//        "championPointsUntilNextLevel": 0,
//        "chestGranted": false,
//        "tokensEarned": 0,
//        "summonerId": "g-kninA3SR9LQWcDNOTajWVvEp_L4ruapRyhNgahPeTZIA"
//    }
//]
 */
class ChampionMasteryTop : ArrayList<ChampionMasteryTop.ChampionMasteryTopItem>(){
    data class ChampionMasteryTopItem(
        val championId: Int,
        val championLevel: Int,
        val championPoints: Int,
        val championPointsSinceLastLevel: Int,
        val championPointsUntilNextLevel: Int,
        val chestGranted: Boolean,
        val lastPlayTime: Long,
        val puuid: String,
        val summonerId: String,
        val tokensEarned: Int
    )
}