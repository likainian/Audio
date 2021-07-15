package com.fly.core.network

import com.example.testproject.network.interceptor.LogInterceptor
import com.fly.core.network.interceptor.ProgressInterceptor
import com.google.gson.Gson
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

import java.util.concurrent.TimeUnit

/**
 * Created by likainian on 2021/7/15
 * Description:
 */

internal object RetrofitClient {
    private var retrofit: Retrofit? = null
    val instance: Retrofit?
        @Synchronized get() {
            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                    .baseUrl("https://api.github.com/")
                    .client(
                        OkHttpClient.Builder()
                            .connectTimeout(15, TimeUnit.SECONDS)
                            .readTimeout(20, TimeUnit.SECONDS)
                            .writeTimeout(20, TimeUnit.SECONDS)
                            .retryOnConnectionFailure(true)
                            .addInterceptor(ProgressInterceptor())
                            .addInterceptor(LogInterceptor())
                            .build()
                    )
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(Gson()))
                    .build()
            }
            return retrofit
        }
}