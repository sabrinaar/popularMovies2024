package com.tmdb.popularmovies2024.data.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
@Parcelize
data class ResponseRating (
        val status_code: Int,
        val status_message: String
    ): Parcelable

@Parcelize
data class ResponseSession(
    val success: Boolean,
    val guest_session_id: String,
    val expires_at: String
):Parcelable