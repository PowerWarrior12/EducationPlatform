package com.example.educationplatform.data.remote

import io.atreydos.okhttpcookiestorage.AddCookiesInterceptor
import io.atreydos.okhttpcookiestorage.ReceivedCookiesInterceptor
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

private const val TIMEOUT: Long = 1000

class RetrofitService {
    fun getRetrofit(url: String, isMain: Boolean = false): Retrofit {
        return Retrofit.Builder()
            .baseUrl(url)
            .client(getClient(isMain))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }

    private fun getClient(isMain: Boolean): OkHttpClient {
        val builder = OkHttpClient.Builder()
            .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY // чтобы в логах отображалось
            })
            .addInterceptor(Interceptor { chain ->
                val builder = chain.request().newBuilder()
                builder.addHeader("Content-Type", "application/json")
                chain.proceed(builder.build())
            })


        return if (isMain) builder.addInterceptor(AddCookiesInterceptor())
            .addInterceptor(ReceivedCookiesInterceptor())
            .build() else builder.build()
    }
}