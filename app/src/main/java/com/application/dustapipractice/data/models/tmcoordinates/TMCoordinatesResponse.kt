package com.application.dustapipractice.data.models.tmcoordinates


import com.google.gson.annotations.SerializedName

data class TMCoordinatesResponse(
    @SerializedName("documents")
    val documents: List<Document>?,
    @SerializedName("meta")
    val meta: Meta?
)