package com.tmdb.popularmovies2024.domain

import com.tmdb.popularmovies2024.data.dto.PeliculaDetalles
import com.tmdb.popularmovies2024.data.dto.ResponsePeliculasPopulares
import com.tmdb.popularmovies2024.data.dto.ResponseRating
import com.tmdb.popularmovies2024.vo.Resource

interface RepoMovies {

    suspend fun getPopularMovies(pageNum: Int): Resource<ResponsePeliculasPopulares>

    suspend fun getDetailsMovie(idMovie: Int): Resource<PeliculaDetalles>

    suspend fun setRating(idMovie: Int, rating:Float) : Resource<ResponseRating>
}
