package com.tmdb.popularmovies2024.data.api


import com.tmdb.popularmovies2024.data.dto.PeliculaDetalles
import com.tmdb.popularmovies2024.data.dto.RatingUser
import com.tmdb.popularmovies2024.data.dto.ResponsePeliculasPopulares
import com.tmdb.popularmovies2024.data.dto.ResponseRating
import com.tmdb.popularmovies2024.data.dto.ResponseSession
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface MoviesApi {

    @GET("movie/popular")
    suspend fun getPopularMovies(@Query("page") page: Int): ResponsePeliculasPopulares

    @GET("movie/{movie_id}")
    suspend fun getDetailsMovie(@Path("movie_id") id: Int): PeliculaDetalles

    @GET("authentication/guest_session/new")
    suspend fun getGuestSession(): ResponseSession

    @Headers("Content-Type: application/json")
    @POST("movie/{movie_id}/rating")
    suspend fun setRatingMovie(
        @Path("movie_id") movie_id: Int,
        @Query("guest_session_id") guest_session_id: String,
        @Body rating: RatingUser
    ): ResponseRating

}