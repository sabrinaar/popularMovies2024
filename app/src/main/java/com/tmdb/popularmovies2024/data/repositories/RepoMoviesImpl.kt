package com.tmdb.popularmovies2024.data.repositories

import com.tmdb.popularmovies2024.data.api.Retrofit
import com.tmdb.popularmovies2024.data.dto.PeliculaDetalles
import com.tmdb.popularmovies2024.data.dto.RatingUser
import com.tmdb.popularmovies2024.data.dto.ResponsePeliculasPopulares
import com.tmdb.popularmovies2024.data.dto.ResponseRating
import com.tmdb.popularmovies2024.domain.RepoMovies
import com.tmdb.popularmovies2024.vo.Resource
import java.lang.Exception


class RepoMoviesImpl : RepoMovies {


    override suspend fun getPopularMovies(pageNum: Int): Resource<ResponsePeliculasPopulares> {

        try {
            return Resource.Success(
                Retrofit.provideMoviesApi().getPopularMovies(pageNum)
            )
        } catch (
            e: Exception
        ) {
            return Resource.Error("Error de conexión")
        }

    }


    override suspend fun getDetailsMovie(idMovie: Int): Resource<PeliculaDetalles> {
        try {
            return Resource.Success(Retrofit.provideMoviesApi().getDetailsMovie(idMovie))
        } catch (e: Exception) {
            return Resource.Failure(e)
        }
    }


    override suspend fun setRating(idMovie: Int, rating: Float): Resource<ResponseRating> {
        try {

            val sessionId: String = Retrofit.provideMoviesApi().getGuestSession().guest_session_id
            val resp: ResponseRating = Retrofit.provideMoviesApi().setRatingMovie(
                idMovie,
                sessionId,
                RatingUser(rating)
            )
            return Resource.Success(resp)
        } catch (e: Exception) {
            return Resource.Error("Error al puntuar pelicula")
        }
    }

}