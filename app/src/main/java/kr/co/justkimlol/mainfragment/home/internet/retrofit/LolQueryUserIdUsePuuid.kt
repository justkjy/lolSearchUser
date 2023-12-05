package kr.co.justkimlol.mainfragment.home.internet.retrofit

import kr.co.justkimlol.dataclass.UserData
import kr.co.justkimlol.dataclass.UserIdUsePuuidData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface LolQueryUserIdUsePuuid {
    @GET("/lol/summoner/v4/summoners/by-puuid/{encryptedPUUID}")
    suspend fun getUserInfo(
        @Path("encryptedPUUID") puuid: String,
        @Query("api_key") apiKey : String
    ): Response<UserIdUsePuuidData>
}