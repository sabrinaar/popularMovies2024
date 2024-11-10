package com.tmdb.popularmovies2024.data.dto

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
data class PeliculaDetalles (
    val id: Int,
    @SerializedName("poster_path")
    var portada: String = "",
    @SerializedName("original_title")
    var titulo: String = "",
    @SerializedName("original_language")
    var idioma_original: String = "",
    @SerializedName("popularity")
    val popularidad: Double,
    @SerializedName("release_date")
    val fecha: String,
    @SerializedName("runtime")
    val duracion: Int,
    @SerializedName("overview")
    val descripcion: String,
    @SerializedName("vote_average")
    val rating: Double,
    @SerializedName("genres")
    val genero: List<ResponseGenero> = listOf()
): Parcelable

@Parcelize
data class ResponseGenero(
    val id:Int=0,
    @SerializedName("name")
    val nombre_genero:String = ""
): Parcelable
