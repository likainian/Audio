package com.fly.core.network.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * Created by likainian on 2021/7/15
 * Description:
 */

class ProgressInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)
        return response.newBuilder().body(response.body()?.let { ProgressResponseBody(it) }).build()
    }
}