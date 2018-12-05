package com.github.kornilovmikhail.weatherapp.interceptors

import java.io.IOException

import okhttp3.Interceptor
import okhttp3.Response

class ApiKeyInterceptor private constructor() : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val url = request.url().newBuilder().addQueryParameter("apikey", apiKey).build()
        request = request.newBuilder().url(url).build()
        return chain.proceed(request)
    }

    companion object {
        private const val apiKey = "2a57d6a2bc61c91c0a4cd3243f70bb0e"
        val instance: ApiKeyInterceptor by lazy {
            ApiKeyInterceptor()
        }
    }
}
