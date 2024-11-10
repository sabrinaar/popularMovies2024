package com.tmdb.popularmovies2024.data.api

import com.tmdb.popularmovies2024.util.BASE_URL
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Retrofit {

    fun provideMoviesApi(): MoviesApi {
        // Crear el cliente OkHttp con el Interceptor
        val client = OkHttpClient.Builder()
            .addInterceptor(ApiKeyInterceptor())  // Agregar el interceptor que añade el api_key
            .build()

        // Crear Retrofit con el cliente OkHttp configurado
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)  // Establecer el cliente OkHttp con el interceptor
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MoviesApi::class.java)
    }
}
