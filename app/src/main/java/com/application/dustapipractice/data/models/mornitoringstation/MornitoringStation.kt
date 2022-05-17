package com.application.dustapipractice.data.models.mornitoringstation


import com.google.gson.annotations.SerializedName

data class MornitoringStation(
    @SerializedName("addr")
    val addr: String?,
    @SerializedName("stationName")
    val stationName: String?,
    @SerializedName("tm")
    val tm: Double?
)