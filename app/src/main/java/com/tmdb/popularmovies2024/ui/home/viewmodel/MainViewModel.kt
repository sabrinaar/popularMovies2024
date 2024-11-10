package com.tmdb.popularmovies2024.ui.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tmdb.popularmovies2024.data.dto.Pelicula
import com.tmdb.popularmovies2024.data.dto.ResponsePeliculasPopulares
import com.tmdb.popularmovies2024.domain.RepoMovies
import com.tmdb.popularmovies2024.vo.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Locale

class MainViewModel(private val repoMovies: RepoMovies) : ViewModel() {

    private val _listPeliculasPopulares = MutableStateFlow<Resource<ResponsePeliculasPopulares>>(
        Resource.Loading())
    val listPeliculasPopulares: StateFlow<Resource<ResponsePeliculasPopulares>> = _listPeliculasPopulares

    var paginaPeliculasABuscar = 1
    private var peliculasResponse: ResponsePeliculasPopulares? = null
    private val _peliculaNombre = MutableStateFlow("")
    private var newPeliculas = mutableListOf<Pelicula>()

    init {
        getPeliculasPopulares()
    }

    fun setPelicula(peliculaNombre: String) {
        _peliculaNombre.value = peliculaNombre
    }

    // Obtener películas populares filtradas por nombre
    fun getPeliculasPopularesFiltro(nombre: String) = viewModelScope.launch {
        setPelicula(nombre)
        _listPeliculasPopulares.value = Resource.Loading()
        val response = Resource.Filtro(peliculasResponse) as Resource<ResponsePeliculasPopulares>
        _listPeliculasPopulares.value = handlePeliculasPopularesResponse(response)
    }

    // Obtener películas populares al regresar
    fun getPeliculasPopularesFiltroBack() = viewModelScope.launch {
        peliculasResponse = null
        getPeliculasPopulares()
    }

    // Obtener películas populares según la página
    fun getPeliculasPopulares() = viewModelScope.launch {
        _listPeliculasPopulares.value = Resource.Loading()
        val response = repoMovies.getPopularMovies(paginaPeliculasABuscar)
        _listPeliculasPopulares.value = handlePeliculasPopularesResponse(response)
    }

    private fun handlePeliculasPopularesResponse(response: Resource<ResponsePeliculasPopulares>): Resource<ResponsePeliculasPopulares> {
        return when (response) {
            is Resource.Loading -> {
                // Se maneja automáticamente en el flujo de estado
                Resource.Loading()
            }

            is Resource.Success -> {
                // Incrementamos la página después de una carga exitosa
                paginaPeliculasABuscar++

                // Si no tenemos una respuesta previa, inicializamos las listas de películas
                peliculasResponse = peliculasResponse ?: response.data

                // Actualizamos las listas de películas
                updatePeliculasList(response.data)

                // Devolvemos el recurso de éxito con la nueva respuesta
                Resource.Success(peliculasResponse ?: response.data)
            }

            is Resource.Filtro -> {
                // Si es un filtro, actualizamos la lista con los resultados filtrados
                val filteredList = applyFilter(response.data, _peliculaNombre.value)
                newPeliculas = filteredList

                // Devolvemos el recurso de filtro
                Resource.Filtro(response.data.copy(popularPelisList = filteredList))
            }

            is Resource.Error -> {
                // Devolvemos un error
                Resource.Error(response.message)
            }

            is Resource.Failure -> {
                // Manejo de fallas si es necesario
               TODO()
            }
        }
    }

    /**
     * Función para actualizar la lista de películas agregando nuevas
     * a las ya existentes.
     */
    private fun updatePeliculasList(newData: ResponsePeliculasPopulares) {
        // Si es la primera vez que cargamos las películas, las inicializamos
        if (peliculasResponse == null) {
            peliculasResponse = newData
        } else {
            // Si ya tenemos una respuesta, combinamos las nuevas películas con las antiguas
            val allPeliculas = (peliculasResponse?.popularPelisList ?: mutableListOf()) + newData.popularPelisList
            // Eliminamos duplicados por ID y actualizamos la lista de películas
            val uniquePeliculas = allPeliculas.distinctBy { it.id }.toMutableList()
            // Asignamos las películas únicas de vuelta a la respuesta
            peliculasResponse = peliculasResponse?.copy(popularPelisList = uniquePeliculas)
        }
    }

    /**
     * Función para aplicar el filtro a las películas.
     */
    private fun applyFilter(data: ResponsePeliculasPopulares, query: String): MutableList<Pelicula> {
       val filteredMovies = data.popularPelisList.distinctBy { it.id }
        return filteredMovies.filter { pelicula ->
            pelicula.titulo.lowercase(Locale.ROOT).contains(query.lowercase(Locale.ROOT))
        }.toMutableList()
    }
}
