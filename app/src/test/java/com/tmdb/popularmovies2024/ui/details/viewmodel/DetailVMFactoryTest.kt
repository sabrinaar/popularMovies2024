package com.tmdb.popularmovies2024.ui.details.viewmodel


import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import com.tmdb.popularmovies2024.domain.RepoMovies
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test


class DetailVMFactoryTest {

    private lateinit var factory: ViewModelProvider.Factory

    @RelaxedMockK
    lateinit var owner: ViewModelStoreOwner

    @RelaxedMockK
    lateinit var repoMovies: RepoMovies

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxed = true)
        factory = DetailVMFactory(repoMovies)
    }

    @After
    fun close() {
        clearAllMocks()
    }

    @Test
    fun view_model_factory_should_return_view_model() {
        every { owner.viewModelStore } answers { ViewModelStore() }

        val viewModel = ViewModelProvider(owner, factory).get(DetailViewModel::class.java)

        Assert.assertNotNull(viewModel)
    }
}