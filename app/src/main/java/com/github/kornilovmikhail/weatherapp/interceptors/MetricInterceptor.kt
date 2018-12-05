package com.github.kornilovmikhail.weatherapp.interceptors

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class MetricInterceptor private constructor() : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val url = request.url().newBuilder().addQueryParameter("units", system).build()
        request = request.newBuilder().url(url).build()
        return chain.proceed(request)
    }

    companion object {
        private const val system = "metric"
        val instance: MetricInterceptor by lazy {
            MetricInterceptor()
        }
    }
}
