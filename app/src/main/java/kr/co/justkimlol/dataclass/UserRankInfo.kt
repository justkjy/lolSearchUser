package kr.co.justkimlol.dataclass

/*json정보
//[
//{
//    "leagueId": "c040e152-ccbb-449d-81ff-6e5a21b37497",
//    "queueType": "RANKED_SOLO_5x5",
//    "tier": "GOLD",
//    "rank": "IV",
//    "summonerId": "FkXYxw4axsRRqxemMKeOrwG7_3q2i9L1E10IrzYaOHIUrA",
//    "summonerName": "Dongk",
//    "leaguePoints": 92,
//    "wins": 4,
//    "losses": 5,
//    "veteran": false,
//    "inactive": false,
//    "freshBlood": false,
//    "hotStreak": false
//}
//]
*/
class UserRankInfo : ArrayList<UserRankInfo.UserRankInfoItem>(){
    data class UserRankInfoItem(
        val freshBlood: Boolean,
        val hotStreak: Boolean,
        val inactive: Boolean,
        val leagueId: String,
        val leaguePoints: Int,
        val losses: Int,
        val queueType: String,
        val rank: String,
        val summonerId: String,
        val summonerName: String,
        val tier: String,
        val veteran: Boolean,
        val wins: Int
    )
}