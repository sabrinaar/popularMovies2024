package com.tmdb.popularmovies2024.ui.details.viewmodel

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.tmdb.popularmovies2024.domain.RepoMovies
import com.tmdb.popularmovies2024.vo.Resource
import com.tmdb.popularmovies2024.data.dto.PeliculaDetalles
import com.tmdb.popularmovies2024.data.dto.ResponseRating
import com.tmdb.popularmovies2024.domain.RepoImpl
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class DetailViewModelTest {

    private lateinit var movieDetails: PeliculaDetalles
    private lateinit var responseRating: ResponseRating
    private lateinit var factory: ViewModelProvider.Factory
    private lateinit var repoImpl: RepoImpl
    private lateinit var owner: ViewModelStoreOwner
    private lateinit var repoMovies: RepoMovies// Mock del repositorio
    private lateinit var viewModel: DetailViewModel
    private val coroutineDispatcher = StandardTestDispatcher()
    private val testCoroutineScope = TestScope(coroutineDispatcher)

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        movieDetails = mockk()
        responseRating = mockk()
        repoImpl = mockk()
        repoMovies = mockk()
        factory = DetailVMFactory(repoImpl)
        viewModel = DetailViewModel(repoMovies)
        owner = mockk(relaxed = true)

    }

    /**
     * inicializamos el stateflow con el loading y luego emitimos el success
     */

    @Test
    fun `obtenerDetallesPelicula debe emitir Success cuando el repositorio devuelve datos correctos`() =
       runTest {
            val idPelicula = 1
            coEvery { repoMovies.getDetailsMovie(idPelicula) } returns Resource.Success(
                movieDetails
            )

           val values = mutableListOf<Resource<PeliculaDetalles>>()

            viewModel.obtenerDetallesPelicula(idPelicula)


            // Recolectamos los valores emitidos por el StateFlow
            val job = launch {

                viewModel.peliculaDetalles
                    .collect { value ->
                        values.add(value)
                    }
            }
            advanceUntilIdle()  // Avanza hasta que todas las corutinas pendientes se completen

            assertEquals(Resource.Success(movieDetails), values.first())

            // Cancelamos la recolección al final del test
            job.cancel()
        }

    @Test
    fun `obtenerDetallesPelicula debe emitir Loading como estado inicial`() = runTest {
        val idPelicula = 1

        coEvery { repoMovies.getDetailsMovie(idPelicula) } returns Resource.Loading()

        val result = viewModel.peliculaDetalles.first()

        assertTrue(result is Resource.Loading)
    }

    @Test
    fun `obtenerDetallesPelicula debe emitir Failure cuando ocurre una excepción`() = runTest {
        val idPelicula = 1
        coEvery { repoMovies.getDetailsMovie(idPelicula) } throws Exception("Error en la API")

        val values = mutableListOf<Resource<PeliculaDetalles>>()

        viewModel.obtenerDetallesPelicula(idPelicula)

        val job = launch {
            viewModel.peliculaDetalles
                .collect { value ->
                    values.add(value)
                }
        }
        advanceUntilIdle()  // Avanza hasta que todas las corutinas pendientes se completen

        val result = viewModel.peliculaDetalles.first()

        assertTrue(result is Resource.Failure)
        assertEquals("Error en la API", (result as Resource.Failure).exception.message)
        job.cancel()
    }

    @Test
    fun `enviarRating debe emitir Success cuando el repositorio responde correctamente`() =
        runTest {
            val idPelicula = 1
            val rating = 4.5f
            val responseRating = ResponseRating(200, "OK")
            coEvery { repoMovies.setRating(idPelicula, rating) } returns Resource.Success(
                responseRating
            )

            val values = mutableListOf<Resource<PeliculaDetalles>>()

            viewModel.enviarRating(idPelicula, rating)
            val job = launch {
                viewModel.peliculaDetalles
                    .collect { value ->
                        values.add(value)
                    }
            }
            advanceUntilIdle()  // Avanza hasta que todas las corutinas pendientes se completen

            val result = viewModel.ratingState.first()

            assertTrue(result is Resource.Success)
            assertEquals(responseRating, (result as Resource.Success).data)
            job.cancel()
        }

    @Test
    fun `enviarRating debe emitir Failure cuando ocurre una excepción`() = runTest {
        // Dado
        val idPelicula = 1
        val rating = 4.5f
        coEvery {
            repoMovies.setRating(
                idPelicula,
                rating
            )
        } throws Exception("Error al enviar rating")

        val values = mutableListOf<Resource<PeliculaDetalles>>()

        viewModel.enviarRating(idPelicula, rating)
        val job = launch {
            viewModel.peliculaDetalles
                .collect { value ->
                    values.add(value)
                }
        }
        advanceUntilIdle()  // Avanza hasta que todas las corutinas pendientes se completen

        val result = viewModel.ratingState.first()

        assertTrue(result is Resource.Failure)
        assertEquals("Error al enviar rating", (result as Resource.Failure).exception.message)
        job.cancel()
    }

}