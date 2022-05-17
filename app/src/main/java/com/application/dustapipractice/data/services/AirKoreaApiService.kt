package com.application.dustapipractice.data.services

import com.application.dustapipractice.BuildConfig
import com.application.dustapipractice.data.models.airquality.AirQualityResponse
import com.application.dustapipractice.data.models.mornitoringstation.MornitoringStationsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface AirKoreaApiService {

    @GET(
        "B552584/MsrstnInfoInqireSvc/getNearbyMsrstnList" +
                "?serviceKey=${BuildConfig.AIRKOREA_SERVICE_KEY}" +
                "&returnType=json"
    )
    suspend fun getNearbyMornitoringStation(
        @Query("tmX") tmX: Double,
        @Query("tmY") tmY: Double
    ): Response<MornitoringStationsResponse>

    @GET(
        "B552584/ArpltnInforInqireSvc/getMsrstnAcctoRltmMesureDnsty" +
                "?serviceKey=${BuildConfig.AIRKOREA_SERVICE_KEY}" +
                "&returnType=json" +
                "&dataTerm=DAILY" +
                "&ver=1.3"
    )
    suspend fun getRealtimeAirQualities(
        @Query("stationName") stationName: String
    ):Response<AirQualityResponse>


}