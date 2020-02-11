package com.android.weatherapp.network

import android.app.Application
import android.text.format.DateUtils
import com.android.weatherapp.BuildConfig
import com.google.gson.Gson
import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

/**
 * Created by    :   Syed
 * Created on    :   03,February,2020.
 * Modified By   :
 * Modified On   :   03,February,2020.
 * Copyright (c) 2020 Syed. All rights reserved.
 */

class NetworkModule {

    private val MAX_STALE: Int = 2

 
    fun provideGson(): Gson = Gson()

    
    fun provideGsonConvertorFactory(): GsonConverterFactory = GsonConverterFactory.create()

      
    internal fun provideCache(application: Application): Cache {
        val cacheSize = (10 * 1024 * 1024).toLong() // 10 MB
        val httpCacheDirectory = File(application.cacheDir, "http-cache")
        return Cache(httpCacheDirectory, cacheSize)
    }

   
    fun provideOkHttpClient(cache: Cache): OkHttpClient {
        val client = OkHttpClient.Builder()
                .cache(cache)
                .connectTimeout(DateUtils.MINUTE_IN_MILLIS, TimeUnit.MILLISECONDS)
                .writeTimeout(DateUtils.MINUTE_IN_MILLIS, TimeUnit.MILLISECONDS)
                .readTimeout(DateUtils.MINUTE_IN_MILLIS, TimeUnit.MILLISECONDS)
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        client.addInterceptor(interceptor)
        client.addNetworkInterceptor(provideCacheInterceptor())
        client.addInterceptor(provideOfflineCacheInterceptor())
        return client.build()
    }


       
       
    fun provideRetrofit(mFactory: GsonConverterFactory, mClient: OkHttpClient)
            : Retrofit = Retrofit.Builder()
            .client(mClient)
            .baseUrl(BuildConfig.APP_API_BASE_URL)
            .addConverterFactory(mFactory)
            .build()

       
       
    fun provideApiService(mRetrofit: Retrofit): APIService = mRetrofit.create(APIService::class.java)

       
       
    //fun provideApiRepository(mApiService: APIService): MyRepository = MyRepositoryImpl(mApiService)

    private fun provideOfflineCacheInterceptor(): Interceptor {
        return Interceptor { chain ->
            try {
                chain.proceed(chain.request())
            } catch (e: Exception) {
                val cacheControl = CacheControl.Builder()
                        .onlyIfCached()
                        .maxStale(MAX_STALE, TimeUnit.HOURS)
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
                    cacheControl.contains("max-stale=0")
            ) {
                val cc = CacheControl.Builder()
                        .maxStale(MAX_STALE, TimeUnit.HOURS)
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

}
