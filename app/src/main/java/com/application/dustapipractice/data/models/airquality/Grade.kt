package com.application.dustapipractice.data.models.airquality

import androidx.annotation.ColorRes
import com.application.dustapipractice.R
import com.google.gson.annotations.SerializedName

enum class Grade(
    val label: String,
    val emoji: String,
    @ColorRes val colorResID: Int
) {
    @SerializedName("1")
    GOOD("좋음", "\uD83D\uDE06", R.color.blue),

    @SerializedName("2")
    NOMAL("보통", "\uD83D\uDE42", R.color.green),

    @SerializedName("3")
    BAD("나쁨", "\uD83D\uDE1E", R.color.yellow),

    @SerializedName("4")
    AWFUL("매우나쁨", "\uD83E\uDD2E", R.color.red),

    UNKNOWN("미측정", "\uD83E\uDD25", R.color.gray);

    override fun toString(): String {
        return "$label $emoji"
    }
}