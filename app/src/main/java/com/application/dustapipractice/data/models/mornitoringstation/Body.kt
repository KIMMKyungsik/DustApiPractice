package com.application.dustapipractice.data.models.mornitoringstation


import com.google.gson.annotations.SerializedName

data class Body(
    @SerializedName("items")
    val mornitoringStations: List<MornitoringStation>?,
    @SerializedName("numOfRows")
    val numOfRows: Int?,
    @SerializedName("pageNo")
    val pageNo: Int?,
    @SerializedName("totalCount")
    val totalCount: Int?
)