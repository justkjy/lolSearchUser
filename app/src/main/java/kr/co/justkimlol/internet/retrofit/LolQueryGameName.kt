package kr.co.justkimlol.internet.retrofit

import kr.co.justkimlol.dataclass.UserData
import kr.co.justkimlol.dataclass.UserIdWithTagData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface LolQueryGameName {
    @GET("/riot/account/v1/accounts/by-riot-id/{gameName}/{tagLine}")
    suspend fun getGameUserInfo(
        @Path("gameName") gameName: String,
        @Path("tagLine") tagLine: String,
        @Query("api_key") apiKey: String
    ): Response<UserIdWithTagData>
}