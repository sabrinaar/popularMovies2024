package com.tmdb.popularmovies2024.ui.network

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.tmdb.popularmovies2024.R
import com.tmdb.popularmovies2024.databinding.FragmentSinConexionBinding


class FragmentSinConexion : Fragment() {

    private var _activityConnectionBinding: FragmentSinConexionBinding? = null
    private val activityConnectionBinding get() = _activityConnectionBinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _activityConnectionBinding = FragmentSinConexionBinding.inflate(inflater, container, false)
        return activityConnectionBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activityConnectionBinding.buttonReconectar.setOnClickListener {
            findNavController().navigate(R.id.action_fragment_Sin_Conexion_to_fragment_Peliculas_Populares2)
        }
    }
}