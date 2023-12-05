package kr.co.justkimlol.mainfragment.home.internet.retrofit

import kr.co.justkimlol.dataclass.MatchListData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface LolQueryMatchList {
    @GET("/lol/match/v5/matches/by-puuid/{puuid}/ids")
    suspend fun getMatchList(
        @Path("puuid") puuid: String,
        @Query("start") start : Int,
        @Query("count") count : Int,
        @Query("api_key") apiKey: String
    ): Response<MatchListData>
}