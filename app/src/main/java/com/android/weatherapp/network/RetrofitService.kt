package com.android.weatherapp.network

import com.android.weatherapp.BuildConfig
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


/**
 * Created by    :   Syed
 * Created on    :   03,February,2020.
 * Modified By   :
 * Modified On   :   03,February,2020.
 * Copyright (c) 2020 Syed. All rights reserved.
 */

class RetrofitService {

    private val httpClient = OkHttpClient.Builder()
            .addInterceptor(getLoggingInterceptor())
            //.addNetworkInterceptor(provideCacheInterceptor())
            //.addInterceptor(provideOfflineCacheInterceptor())
            .build()


    private val retrofit = Retrofit.Builder()
            .client(httpClient)
            .baseUrl(BuildConfig.APP_API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()


    fun <S> createService(serviceClass: Class<S>): S {

        return retrofit.create(serviceClass)
    }


    private fun getLoggingInterceptor(): Interceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return interceptor
    }



    companion object {
        private val httpClient = OkHttpClient.Builder()
                .addInterceptor(getLoggingInterceptor())
                //.addNetworkInterceptor(provideCacheInterceptor())
                //.addInterceptor(provideOfflineCacheInterceptor())
                .build()

        private val retrofit = Retrofit.Builder()
                .client(httpClient)
                .baseUrl(BuildConfig.APP_API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        private fun getLoggingInterceptor(): Interceptor {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            return interceptor
        }


        private fun provideOfflineCacheInterceptor(): Interceptor {
            return Interceptor { chain ->
                try {
                    chain.proceed(chain.request())
                } catch (e: Exception) {
                    val cacheControl = CacheControl.Builder()
                            .onlyIfCached()
                            .maxStale(Companion.MAX_STALE, TimeUnit.MINUTES)
                            .build()

                    val offlineRequest = chain.request().newBuilder()
                            .cacheControl(cacheControl)
                            .build()
                    chain.proceed(offlineRequest)
                }
            }
        }


        private fun provideCacheInterceptor(): Interceptor {
            return Interceptor { chain ->
                var request = chain.request()
                val originalResponse = chain.proceed(request)
                val cacheControl = originalResponse.header("Cache-Control")

                if (cacheControl == null ||
                        cacheControl.contains("no-store") ||
                        cacheControl.contains("no-cache") ||
                        cacheControl.contains("must-revalidate") ||
                        cacheControl.contains("max-stale=0")) {

                    val cc = CacheControl.Builder()
                            .maxStale(Companion.MAX_STALE, TimeUnit.MINUTES)
                            .build()
                    request = request.newBuilder()
                            .cacheControl(cc)
                            // .cacheControl(CacheControl.FORCE_NETWORK)
                            .build()
                    chain.proceed(request)

                } else {
                    originalResponse
                }
            }
        }


        fun <S> createService(serviceClass: Class<S>): S {

            return retrofit.create(serviceClass)
        }

        private const val MAX_STALE: Int = 1


        operator fun <S> invoke(serviceClass: Class<S>): S  {
            return retrofit.create(serviceClass)
        }
    }

}

