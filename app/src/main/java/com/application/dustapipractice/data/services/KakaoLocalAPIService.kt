package com.application.dustapipractice.data.services

import com.application.dustapipractice.BuildConfig
import com.application.dustapipractice.data.models.tmcoordinates.TMCoordinatesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Query

interface KakaoLocalAPIService {

    @Headers("Authorization: KakaoAK ${BuildConfig.KAKAO_API_KEY}")
    @GET("v2/local/geo/transcoord.json?output_coord=TM")
    suspend fun getTMCoordinates(
        @Query("x") longitude : Double,
        @Query("y") latitude : Double
    ) : Response<TMCoordinatesResponse>
}