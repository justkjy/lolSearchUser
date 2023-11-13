package kr.co.lol.internet.retrofit

import kr.co.lol.dataclass.UserData
import kr.co.lol.dataclass.UserIdData
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface LolQueryUserName {
    @GET("/lol/summoner/v4/summoners/by-name/{summonerName}")
    suspend fun getUserInfo(
        @Path("summonerName") summonerName: String,
        @Query("api_key") api_key : String
    ): Response<UserData>
}



