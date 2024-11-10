package com.tmdb.popularmovies2024.data.dto

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.tmdb.popularmovies2024.data.dto.Pelicula
import kotlinx.parcelize.Parcelize

@Parcelize
data class ResponsePeliculasPopulares (
    @SerializedName("page")
    val page: Int = 1,
    @SerializedName("results")
    var popularPelisList: MutableList<Pelicula>,
    @SerializedName("total_pages")
    var totalPaginas: Int,
    @SerializedName("total_results")
    var totalResultados: Int
): Parcelable
