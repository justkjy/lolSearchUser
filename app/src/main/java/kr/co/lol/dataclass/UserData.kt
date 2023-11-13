package kr.co.lol.dataclass


// https://kr.api.riotgames.com/lol/summoner/v4/summoners/by-name/dongk?api_key=RGAPI-575da62e-2796-4447-805e-55351307aea8
data class UserData(
    val accountId: String,
    val id: String,
    val name: String,
    val profileIconId: Int,
    val puuid: String,
    val revisionDate: Long,
    val summonerLevel: Int
)