package kr.co.lol.internet.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class GetJsonFromRerofit {
    companion object {
        private const val BASE_URL = "https://kr.api.riotgames.com/lol/"
        private var INSTANCE: Retrofit? = null

        fun getInstance(): Retrofit {
            if (INSTANCE == null) {
                INSTANCE = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return  INSTANCE!!
        }
    }
}