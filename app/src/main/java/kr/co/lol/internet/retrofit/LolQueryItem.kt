package kr.co.lol.internet.retrofit

import kr.co.lol.dataclass.UserData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

//사용자 정보 획득
interface LolQueryItem {
    @GET("summoner/v4/summoners/by-name/{userName}")
    suspend fun getUserInfo(
        @Path("userName") userName: String,
        @Query("api_key") api_key : String
    //): Call<UserData>
    ): Response<UserData>
}