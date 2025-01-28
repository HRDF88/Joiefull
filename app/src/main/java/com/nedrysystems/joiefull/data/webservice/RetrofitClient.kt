package com.nedrysystems.joiefull.data.webservice

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 * Singleton object that provides a Retrofit client configured for interacting with the product API.
 * It sets up the base URL, logging interceptor, Moshi converter, and HTTP client.
 */
object RetrofitClient {


    /**
     * The base URL for the API endpoints.
     */
    private const val baseUrl =
        "https://raw.githubusercontent.com/OpenClassrooms-Student-Center/D-velopper-une-interface-accessible-en-Jetpack-Compose/main/api/"

    /**
     * Logging interceptor to log HTTP request and response details.
     * It is configured to log the body of the requests and responses.
     */
    private val login = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    /**
     * OkHttp client configured with the logging interceptor.
     */
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(login)
        .build()

    /**
     * Moshi instance used for JSON serialization and deserialization.
     * Includes the Kotlin JSON adapter factory for seamless integration with Kotlin data classes.
     */
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    /**
     * Lazily initialized Retrofit service for the `GetProductApiService` interface.
     * Configured with the base URL, Moshi converter, and the custom OkHttp client.
     *
     * @return An instance of [GetProductApiService] for interacting with the product API.
     */
    val getProductApiService: GetProductApiService by lazy {
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(okHttpClient)
            .build()
            .create(GetProductApiService::class.java)
    }
}