package com.tmdb.popularmovies2024.ui.home

import com.tmdb.popularmovies2024.ui.home.page.PaginationScrollListener
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.tmdb.popularmovies2024.R
import com.tmdb.popularmovies2024.data.dto.Pelicula
import com.tmdb.popularmovies2024.data.repositories.RepoMoviesImpl
import com.tmdb.popularmovies2024.databinding.FragmentListaPeliculasPopulBinding
import com.tmdb.popularmovies2024.domain.RepoImpl
import com.tmdb.popularmovies2024.ui.home.adapter.AdaptadorPeliculas
import com.tmdb.popularmovies2024.ui.home.viewmodel.MainViewModel
import com.tmdb.popularmovies2024.ui.home.viewmodel.VMFactory
import com.tmdb.popularmovies2024.vo.Resource
import kotlinx.coroutines.launch

const val QUERY_PAGE_SIZE = 20

class FragmentMainPeliculasPopulares : Fragment() {

    private var _fragmentMainPopularMoviesBinding: FragmentListaPeliculasPopulBinding? = null
    private val fragmentMainPopularMoviesBinding get() = _fragmentMainPopularMoviesBinding!!

    private val viewmodel by viewModels<MainViewModel> { VMFactory(RepoImpl(RepoMoviesImpl())) }
    lateinit var adaptadorPeliculas: AdaptadorPeliculas
    var isLoading = false
    var isLastPage = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _fragmentMainPopularMoviesBinding = FragmentListaPeliculasPopulBinding.inflate(inflater, container, false)
        return fragmentMainPopularMoviesBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.let {
            (it as AppCompatActivity).supportActionBar?.show()
        }

        setupRecyclerView()
        setUpBuscador()
        setUpObserver()

        adaptadorPeliculas.setOnItemClickListener {
            val bundle = Bundle()
            bundle.putParcelable("pelicula", it)
            findNavController().navigate(R.id.fragment_Detalles_Pelicula, bundle)
        }
    }

    private fun setUpObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewmodel.listPeliculasPopulares.collect { response ->
                when (response) {
                    is Resource.Loading -> {
                        showProgressBar()
                    }
                    is Resource.Filtro -> {
                        hideProgressBar()
                        updateMovieList(response.data.popularPelisList)
                    }
                    is Resource.Success -> {
                        hideProgressBar()
                        updateMovieList(response.data.popularPelisList)
                        val totalPages = response.data.totalPaginas + 2
                        isLastPage = viewmodel.paginaPeliculasABuscar >= totalPages
                    }
                    is Resource.Error -> {
                        hideProgressBar()
                        Log.e("TAG", "Error: ${response.message}")
                        findNavController().navigate(R.id.action_fragment_Peliculas_Populares_to_fragment_Sin_Conexion2)
                    }
                    is Resource.Failure -> {
                        hideProgressBar()
                        //Log.e("TAG", "Failure: ${response.throwable?.message}")
                    }
                }
            }
        }
    }

    private fun updateMovieList(moviesList: List<Pelicula>) {
        adaptadorPeliculas.submitMoviesList(moviesList)
    }

    private fun setUpBuscador() {
        fragmentMainPopularMoviesBinding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    viewmodel.getPeliculasPopularesFiltro(it) // Buscar peli al hacer submit
                }
                return false
            }

            override fun onQueryTextChange(query: String?): Boolean {
                if (query.isNullOrEmpty()) { // Cuando borra el nombre a buscar
                    viewmodel.getPeliculasPopularesFiltroBack()
                }
                return false
            }
        })
    }

    private fun hideProgressBar() {
        fragmentMainPopularMoviesBinding.progressBarPopular.visibility = View.GONE
        isLoading = false
    }

    private fun showProgressBar() {
        fragmentMainPopularMoviesBinding.progressBarPopular.visibility = View.VISIBLE
        isLoading = true
    }

    private fun setupRecyclerView() {
        adaptadorPeliculas = AdaptadorPeliculas()
        fragmentMainPopularMoviesBinding.rvPeliculasPopulares.apply {
            adapter = adaptadorPeliculas
            layoutManager = GridLayoutManager(requireContext(), 3)
            addOnScrollListener(
                PaginationScrollListener(
                    onLoadMore = { viewmodel.getPeliculasPopulares() },
                    isLoading = { isLoading },
                    isLastPage = { isLastPage }
                )
            )
        }
    }
}
