package com.github.kornilovmikhail.weatherapp.interceptors

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class MetricInterceptor private constructor() : Interceptor {
    private val system = "metric"

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val url = request.url().newBuilder().addQueryParameter("units", system).build()
        request = request.newBuilder().url(url).build()
        return chain.proceed(request)
    }

    companion object {
        private var metricInterceptor: MetricInterceptor? = null

        fun create(): Interceptor {
            if (metricInterceptor == null) {
                metricInterceptor = MetricInterceptor()
            }
            return metricInterceptor as MetricInterceptor
        }
    }
}
