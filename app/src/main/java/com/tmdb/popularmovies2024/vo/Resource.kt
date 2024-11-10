package com.tmdb.popularmovies2024.vo

/**
 * Created by Sabrina
 */
sealed class Resource<out T> {
    class Loading<out T> : Resource<T>()
    data class Success<out T>(val data: T) : Resource<T>()
    data class Filtro<out T>(val data: T) : Resource<T>()

    data class Failure<out T>(val exception: Exception) : Resource<T>()
    data class Error<out T>(val message: String, val data: T? = null) : Resource<T>()

}