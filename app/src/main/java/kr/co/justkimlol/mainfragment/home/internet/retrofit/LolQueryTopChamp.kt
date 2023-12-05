package kr.co.justkimlol.mainfragment.home.internet.retrofit

import kr.co.justkimlol.dataclass.ChampionMasteryTop
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


//https://kr.api.riotgames.com/lol/champion-mastery/v4/champion-masteries/by-puuid/AH3tjkvRgXPgrUEaKIeZgVJcJeRKYJFiX27RXVs4yvZuF5GqueBBY7oL4SHci2RM9LdTPW5FsL3XhQ/top?count=10&api_key=RGAPI-96b717cb-a686-478a-9236-09458f2dc7bb
interface LolQueryTopChamp {
    @GET("/lol/champion-mastery/v4/champion-masteries/by-puuid/{encryptedPUUID}/top")
    suspend fun getTopChampInfo(
        @Path("encryptedPUUID") encryptedPUUID: String,
        @Query("count") count : Int,
        @Query("api_key") apiKey : String
    ): Response<ChampionMasteryTop>
}