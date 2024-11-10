package com.tmdb.popularmovies2024.data.api

import com.tmdb.popularmovies2024.util.API_KEY
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class ApiKeyInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        // Obtener la solicitud original
        val originalRequest: Request = chain.request()

        // Modificar la URL para añadir el parámetro api_key
        val newUrl: HttpUrl = originalRequest.url.newBuilder()
            .addQueryParameter("api_key", API_KEY)  // Añadir el api_key
            .build()

        // Crear una nueva solicitud con la URL modificada
        val newRequest: Request = originalRequest.newBuilder()
            .url(newUrl)
            .build()

        // Proceder con la nueva solicitud
        return chain.proceed(newRequest)
    }
}