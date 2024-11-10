package com.tmdb.popularmovies2024.ui.details.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tmdb.popularmovies2024.domain.RepoMovies
import com.tmdb.popularmovies2024.vo.Resource
import com.tmdb.popularmovies2024.data.dto.PeliculaDetalles
import com.tmdb.popularmovies2024.data.dto.ResponseRating
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers

class DetailViewModel(private val repoMovies: RepoMovies) : ViewModel() {

    // StateFlow para emitir los estados de la película
    private val _peliculaDetalles = MutableStateFlow<Resource<PeliculaDetalles>>(Resource.Loading())
    val peliculaDetalles: StateFlow<Resource<PeliculaDetalles>> = _peliculaDetalles


    // StateFlow para emitir los estados del rating
    private val _ratingState = MutableStateFlow<Resource<ResponseRating>>(Resource.Loading())
    val ratingState: StateFlow<Resource<ResponseRating>> = _ratingState

    // Función para obtener los detalles de la película
    fun obtenerDetallesPelicula(idPelicula: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = repoMovies.getDetailsMovie(idPelicula)
                _peliculaDetalles.value = result // Emitir el resultado
            } catch (e: Exception) {
                _peliculaDetalles.value = Resource.Failure(e) // Emitir error si ocurre
            }
        }
    }

    // Función para enviar el rating de la película
    fun enviarRating(idPelicula: Int, rating: Float) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = repoMovies.setRating(idPelicula, rating)
                _ratingState.value = result // Emitir el resultado
            } catch (e: Exception) {
                _ratingState.value = Resource.Failure(e) // Emitir error si ocurre
            }
        }
    }
}
