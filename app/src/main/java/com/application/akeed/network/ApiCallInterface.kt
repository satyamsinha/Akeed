package com.application.akeed.network

import retrofit2.Call
import retrofit2.http.*

interface ApiCallInterface {

    @Headers("Content-Type:application/json")
    @GET
    fun  getItemsDetails( @Url searchText: String): Call<ResponseData>
}