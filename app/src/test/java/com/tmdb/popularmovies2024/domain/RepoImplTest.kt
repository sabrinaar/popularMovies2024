package com.example.popularmovies2024.domain

import com.tmdb.popularmovies2024.data.dto.Pelicula
import com.tmdb.popularmovies2024.data.dto.PeliculaDetalles
import com.tmdb.popularmovies2024.data.dto.ResponsePeliculasPopulares
import com.tmdb.popularmovies2024.data.dto.ResponseRating
import com.tmdb.popularmovies2024.data.repositories.RepoMoviesImpl
import com.tmdb.popularmovies2024.vo.Resource
import com.tmdb.popularmovies2024.domain.RepoImpl
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test


class RepoImplTest {

    private lateinit var repoMoviesImpl: RepoMoviesImpl
    private lateinit var repoImpl: RepoImpl
    private lateinit var pelicula: Pelicula
    private lateinit var peliculaDetails: PeliculaDetalles
    private lateinit var responseRating: ResponseRating
    private lateinit var responsePeliculasPopulares: ResponsePeliculasPopulares

    @Before
    fun setup() {
        pelicula = mockk()
        peliculaDetails = mockk()
        repoMoviesImpl = mockk()
        responsePeliculasPopulares = mockk()
        responseRating = mockk()
        repoImpl = RepoImpl(repoMoviesImpl)

    }

    @Test
    fun test_getPeliculasPopulares_returns_success() = runTest {
        // Datos de prueba
        val pageNum = 1

        // Definir el comportamiento esperado para el mock
        coEvery { repoMoviesImpl.getPopularMovies(pageNum) } returns Resource.Success(responsePeliculasPopulares)

        // Ejecutar la función en RepoImpl
        val result = repoImpl.getPopularMovies(pageNum)

        assertTrue(result is Resource.Success)

        // Verificar que el método del mock fue llamado correctamente
        coVerify { repoMoviesImpl.getPopularMovies(pageNum) }
    }

    @Test
    fun `test getPeliculasPopulares returns error`() = runTest {
        // Simular una respuesta de error
        val pageNum = 1
        val errorMessage = "Network Error"
        coEvery { repoMoviesImpl.getPopularMovies(pageNum) } returns Resource.Error(errorMessage)

        // Ejecutar la función en RepoImpl
        val result = repoImpl.getPopularMovies(pageNum)

        // Verificar que la respuesta es de tipo Error
        assertTrue(result is Resource.Error)
        assertEquals(errorMessage, (result as Resource.Error).message)

        // Verificar que el método del mock fue llamado correctamente
        coVerify { repoMoviesImpl.getPopularMovies(pageNum) }
    }

    @Test
    fun `test getDetallesPelicula returns success`() = runTest {
        // Datos de prueba
        val movieId = 1

        // Simular el comportamiento de la función
        coEvery { repoMoviesImpl.getDetailsMovie(movieId) } returns Resource.Success(peliculaDetails)

        // Ejecutar el método en RepoImpl
        val result = repoImpl.getDetailsMovie(movieId)

        // Verificar que el resultado es correcto
        assertTrue(result is Resource.Success)
        assertEquals(peliculaDetails, (result as Resource.Success).data)

        // Verificar que el método del mock fue llamado correctamente
        coVerify { repoMoviesImpl.getDetailsMovie(movieId) }
    }

    @Test
    fun `test setRating returns success`() = runTest {
        // Datos de prueba
        val movieId = 1
        val rating = 4.5f

        // Simular el comportamiento del método
        coEvery { repoMoviesImpl.setRating(movieId, rating) } returns Resource.Success(responseRating)

        // Ejecutar el método en RepoImpl
        val result = repoImpl.setRating(movieId, rating)

        // Verificar que la respuesta es un éxito
        assertTrue(result is Resource.Success)
        assertEquals(responseRating, (result as Resource.Success).data)

        // Verificar que el método del mock fue llamado correctamente
        coVerify { repoMoviesImpl.setRating(movieId, rating) }
    }

    @Test
    fun `test setRating returns error`() = runTest {
        // Simular un error en la operación
        val movieId = 1
        val rating = 4.5f
        val errorMessage = "Failed to set rating"
        
        coEvery { repoMoviesImpl.setRating(movieId, rating) } returns Resource.Error(errorMessage)

        // Ejecutar el método en RepoImpl
        val result = repoImpl.setRating(movieId, rating)

        // Verificar que la respuesta es un error
        assertTrue(result is Resource.Error)
        assertEquals(errorMessage, (result as Resource.Error).message)

        // Verificar que el método del mock fue llamado correctamente
        coVerify { repoMoviesImpl.setRating(movieId, rating) }
    }
}
