package kr.co.lol.internet.retrofit

import android.util.Log
import kr.co.lol.internet.TAG
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class GetJsonFromRerofit {
    companion object {
        private const val BASE_URL = "https://kr.api.riotgames.com/lol/"
        private const val ASIA_URL = "https://asia.api.riotgames.com/lol/"
        private const val SEA_URL = "https://asia.api.riotgames.com/lol/"
        private var INSTANCE_KR: Retrofit? = null
        private var INSTANCE_ASIA: Retrofit? = null
        private var INSTANCE_SEA: Retrofit? = null
        fun getInstance(): Retrofit {
            if (INSTANCE_KR == null) {
                INSTANCE_KR = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return  INSTANCE_KR!!
        }

        fun getInstanceAsia(): Retrofit {
            if (INSTANCE_ASIA == null) {
                INSTANCE_ASIA = Retrofit.Builder()
                    .baseUrl(ASIA_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return  INSTANCE_ASIA!!
        }

        fun getInstanceSea(): Retrofit {
            if (INSTANCE_ASIA == null) {
                INSTANCE_ASIA = Retrofit.Builder()
                    .baseUrl(SEA_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return  INSTANCE_SEA!!
        }
    }
}