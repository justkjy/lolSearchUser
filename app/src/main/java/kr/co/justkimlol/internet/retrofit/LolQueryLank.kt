package kr.co.justkimlol.internet.retrofit

import kr.co.justkimlol.dataclass.UserRankInfo
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

//https://kr.api.riotgames.com/lol/league/v4/entries/by-summoner/FkXYxw4axsRRqxemMKeOrwG7_3q2i9L1E10IrzYaOHIUrA?api_key=RGAPI-96b717cb-a686-478a-9236-09458f2dc7bb
interface LolQueryLank {
    @GET("/lol/league/v4/entries/by-summoner/{encryptedSummonerId}")
    suspend fun getRankInfo(
        @Path("encryptedSummonerId") encryptedSummonerId: String,
        @Query("api_key") apiKey : String
    ): Response<UserRankInfo>
}