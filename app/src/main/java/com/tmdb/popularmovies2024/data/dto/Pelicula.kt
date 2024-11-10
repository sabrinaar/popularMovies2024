package com.tmdb.popularmovies2024.data.dto

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
data class Pelicula (
    val id: Int,
    @SerializedName("poster_path")
    var portada: String = "",
    @SerializedName("original_title")
    var titulo: String = ""
): Parcelable
