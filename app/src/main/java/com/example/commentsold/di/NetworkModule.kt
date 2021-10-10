package com.example.commentsold.di

import android.content.Context
import com.example.commentsold.CommentSoldApplication
import com.example.commentsold.data.network.ApiService
import com.example.commentsold.utils.Constants.Companion.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideApplication(@ApplicationContext app: Context): CommentSoldApplication {
        return app as CommentSoldApplication
    }

    @Provides
    @Singleton
    fun provideContext(application: CommentSoldApplication): Context {
        return application.applicationContext
    }

    @Singleton
    @Provides
    fun provideNetworkInterceptor(
        sessionManager: SessionManager
    ): Interceptor {
        return Interceptor { chain ->
            val builder = chain.request().newBuilder()
            val defaultHeaders: MutableMap<String, String> = mutableMapOf(
                Pair("Authorization", "Bearer ${sessionManager.getAuthToken()}"),
                Pair("Content-Type", "application/json")
            )
            for (header in defaultHeaders) {
                builder.addHeader(header.key, header.value)
            }

            val request = builder.build()
            val response = chain.proceed(request)
            when (response.code) {
                // TODO this is NOT the correct way to do this. This should throw an
                // exception and propagate an ErrorResponse back to the ViewModel. I
                // ran out of time to research the solution.
                500 -> {
                }
            }
            response
        }
    }

    @Singleton
    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        return interceptor
    }

    @Singleton
    @Provides
    fun provideHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        networkInterceptor: Interceptor
    ): OkHttpClient {
        return OkHttpClient
            .Builder()
            .addInterceptor(loggingInterceptor)
            .addNetworkInterceptor(networkInterceptor)
            .readTimeout(15, TimeUnit.SECONDS)
            .connectTimeout(15, TimeUnit.SECONDS)
            .build()
    }

    @Singleton
    @Provides
    fun provideConverterFactory(): GsonConverterFactory =
        GsonConverterFactory.create()

    @Singleton
    @Provides
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(gsonConverterFactory)
            .build()
    }

    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit): ApiService =
        retrofit.create(ApiService::class.java)
}
