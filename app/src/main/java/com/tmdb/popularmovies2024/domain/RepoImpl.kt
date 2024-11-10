package com.tmdb.popularmovies2024.domain

import com.tmdb.popularmovies2024.data.repositories.RepoMoviesImpl
import com.tmdb.popularmovies2024.data.dto.PeliculaDetalles
import com.tmdb.popularmovies2024.data.dto.ResponsePeliculasPopulares
import com.tmdb.popularmovies2024.data.dto.ResponseRating
import com.tmdb.popularmovies2024.vo.Resource

class RepoImpl(private val repoMoviesImpl: RepoMoviesImpl) : RepoMovies {

    override suspend fun getPopularMovies(pageNum: Int): Resource<ResponsePeliculasPopulares> {
        return repoMoviesImpl.getPopularMovies(pageNum)
    }


    override suspend fun getDetailsMovie(idMovie: Int): Resource<PeliculaDetalles> {
        return repoMoviesImpl.getDetailsMovie(idMovie)
    }

    override suspend fun setRating(idMovie: Int, rating:Float) : Resource<ResponseRating> {
        return repoMoviesImpl.setRating(idMovie,rating)
    }



}