package com.tmdb.popularmovies2024.ui.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.tmdb.popularmovies2024.domain.RepoMovies

class VMFactory(private val repoMovies: RepoMovies) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(repoMovies) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
