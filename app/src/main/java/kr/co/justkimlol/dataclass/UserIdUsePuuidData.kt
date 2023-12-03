package kr.co.justkimlol.dataclass

//https://kr.api.riotgames.com/lol/summoner/v4/summoners/by-puuid/AH3tjkvRgXPgrUEaKIeZgVJcJeRKYJFiX27RXVs4yvZuF5GqueBBY7oL4SHci2RM9LdTPW5FsL3XhQ?api_key=RGAPI-237352f6-9b69-4e73-bf8d-cd11086d9dbe
data class UserIdUsePuuidData(
    val accountId: String,
    val id: String,
    val name: String,
    val profileIconId: Int,
    val puuid: String,
    val revisionDate: Long,
    val summonerLevel: Int
)