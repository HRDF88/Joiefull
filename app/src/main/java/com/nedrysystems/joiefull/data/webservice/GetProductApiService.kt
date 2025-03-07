package com.nedrysystems.joiefull.data.webservice

import retrofit2.http.GET

/**
 * This interface represents the API service for get all products.
 */
interface GetProductApiService {
    @GET("clothes.json")
    suspend fun getProduct(): List<GetProductApiResponse>
}