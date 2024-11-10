package com.tmdb.popularmovies2024.ui.details

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.tmdb.popularmovies2024.R
import com.tmdb.popularmovies2024.data.dto.Pelicula
import com.tmdb.popularmovies2024.data.dto.PeliculaDetalles
import com.tmdb.popularmovies2024.data.repositories.RepoMoviesImpl
import com.tmdb.popularmovies2024.databinding.FragmentDetallesPeliculaBinding
import com.tmdb.popularmovies2024.domain.RepoImpl
import com.tmdb.popularmovies2024.ui.details.viewmodel.DetailVMFactory
import com.tmdb.popularmovies2024.vo.Resource
import com.tmdb.popularmovies2024.ui.details.viewmodel.DetailViewModel
import kotlinx.coroutines.launch
import java.net.UnknownHostException

class FragmentDetails : Fragment() {

    private lateinit var pelicula: Pelicula
    private val viewModel by viewModels<DetailViewModel> {
        DetailVMFactory(
            RepoImpl(
                RepoMoviesImpl()
            )
        )
    }
    private var _binding: FragmentDetallesPeliculaBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireArguments().let {
            pelicula = it.getParcelable("pelicula")!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetallesPeliculaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Para ocultar la toolbar en este fragmento
        activity?.let {
            (it as AppCompatActivity).supportActionBar?.hide()
        }

        // Observamos el flujo del estado de los detalles de la película
        lifecycleScope.launch {
            // Llamamos al método de ViewModel para obtener los detalles
            viewModel.obtenerDetallesPelicula(pelicula.id)
            // Colectamos los resultados del flujo
            viewModel.peliculaDetalles.collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        binding.progressBarDetalles.visibility = View.VISIBLE
                    }

                    is Resource.Success -> {
                        binding.progressBarDetalles.visibility = View.GONE
                        setInfoPelicula(result.data)
                    }

                    is Resource.Failure -> {
                        binding.progressBarDetalles.visibility = View.GONE
                        if (result.exception is UnknownHostException) {
                            findNavController().navigate(R.id.action_fragment_Detalles_Pelicula_to_fragment_Sin_Conexion)
                        }
                    }

                    is Resource.Error -> {
                        // Manejar error si es necesario
                    }

                    else -> {
                        // Manejar otros tipos de estado si es necesario
                    }
                }
            }
        }
        setRatingByUser()
    }

    private fun setRatingByUser() {
        // Observamos el flujo para la acción de calificación
        binding.ratingBarVotoUsuario.onRatingBarChangeListener =
            RatingBar.OnRatingBarChangeListener { ratingBar, _, _ ->
                val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
                    ?: return@OnRatingBarChangeListener
                with(sharedPref.edit()) {
                    putFloat(pelicula.id.toString().trim(), binding.ratingBarVotoUsuario.rating)
                    commit()
                }
                viewModel.enviarRating(pelicula.id, ratingBar.rating)

                lifecycleScope.launch {
                    viewModel.ratingState.collect { result ->
                        when (result) {
                            is Resource.Loading -> {
                                binding.progressBarDetalles.visibility = View.VISIBLE
                            }

                            is Resource.Success -> {
                                binding.progressBarDetalles.visibility = View.GONE
                            }

                            is Resource.Failure -> {
                                binding.progressBarDetalles.visibility = View.GONE
                            }

                            else -> {
                                // Manejar otros estados si es necesario
                            }
                        }
                    }
                }
            }
    }

    private fun setInfoPelicula(pelicula: PeliculaDetalles) {
        var generosPelicula = ""

        // Cargar la imagen de la película
        if (pelicula.portada.isNotEmpty()) {
            Glide.with(requireContext())
                .load(getString(R.string.portada_url_base342) + pelicula.portada)
                .centerCrop().into(binding.imagePortadaDetalle)
        } else {
            Glide.with(requireContext())
                .load(R.drawable.ic_no_image)
                .centerCrop().into(binding.imagePortadaDetalle)
        }

        // Configuración del rating
        binding.ratingBarPelicula.rating = (pelicula.rating.toFloat() * 5) / 10

        // voto del usuario
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        val ratingUser = sharedPref.getFloat(pelicula.id.toString().trim(), 0.0F)
        binding.ratingBarVotoUsuario.rating = ratingUser

        // Mostrar detalles de la película
        binding.tituloPelicula.text = pelicula.titulo
        binding.anioPelicula.text = pelicula.fecha.substring(0, 4)
        binding.fechaEstrenoPelicula.text = pelicula.fecha
        binding.ratingPelicula.text = pelicula.rating.toString()
        binding.idiomaPelicula.text = pelicula.idioma_original
        binding.duracionPelicula.text = pelicula.duracion.toString()
        binding.descripcionPelicula.text = pelicula.descripcion

        // Generar lista de géneros
        if (pelicula.genero.isNotEmpty()) {
            generosPelicula = pelicula.genero.joinToString(", ") { it.nombre_genero }
        }
        binding.generoPelicula.text = generosPelicula
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
