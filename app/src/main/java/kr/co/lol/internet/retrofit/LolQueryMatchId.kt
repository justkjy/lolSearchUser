package kr.co.lol.internet.retrofit

import kr.co.lol.dataclass.UserMatchId
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface LolQueryMatchId {
    @GET("/lol/match/v5/matches/{matchId}")
    suspend fun getMatchList(
        @Path("matchId") matchId: String,
        @Query("api_key") apiKey: String
    ): Response<UserMatchId>
}