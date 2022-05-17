package com.application.dustapipractice.data


import com.application.dustapipractice.BuildConfig
import com.application.dustapipractice.data.models.airquality.MeasuredValue
import com.application.dustapipractice.data.models.mornitoringstation.MornitoringStation
import com.application.dustapipractice.data.models.mornitoringstation.MornitoringStationsResponse
import com.application.dustapipractice.data.services.AirKoreaApiService
import com.application.dustapipractice.data.services.KakaoLocalAPIService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create



object Repository {

    suspend fun getNearbyMornitoringStation(latitude: Double, longitude: Double):MornitoringStation?{
        val tmCoordinates = KakaoLocalAPIService
            .getTMCoordinates(longitude, latitude)
            .body()
            ?.documents
            ?.firstOrNull()

        val tmX = tmCoordinates?.x
        val tmY = tmCoordinates?.y

        return airKoreraAPIService.getNearbyMornitoringStation(tmX!!, tmY!!)
            .body()
            ?.response
            ?.body
            ?.mornitoringStations
            ?.minByOrNull { it.tm ?: Double.MAX_VALUE }

    }

    suspend fun getLatestAirQualityData(stationName :String ):MeasuredValue?=
        airKoreraAPIService.getRealtimeAirQualities(stationName)
            .body()
            ?.response
            ?.body
            ?.measuredValues
            ?.firstOrNull()

    private val KakaoLocalAPIService: KakaoLocalAPIService by lazy {
        Retrofit.Builder()
            .baseUrl(Url.KAKAO_API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(buildHttpClint())
            .build()
            .create()
    }

    private val airKoreraAPIService: AirKoreaApiService by lazy {
        Retrofit.Builder()
            .baseUrl(Url.AIRKOREA_API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(buildHttpClint())
            .build()
            .create()
    }

    private fun buildHttpClint(): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = if (BuildConfig.DEBUG) {
                        HttpLoggingInterceptor.Level.BODY

                    } else {
                        HttpLoggingInterceptor.Level.NONE
                    }
                }
            )
            .build()


}