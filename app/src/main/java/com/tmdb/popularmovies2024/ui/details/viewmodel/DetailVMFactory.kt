package com.tmdb.popularmovies2024.ui.details.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.tmdb.popularmovies2024.domain.RepoMovies

class DetailVMFactory(private val repoMovies: RepoMovies) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DetailViewModel(repoMovies) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
