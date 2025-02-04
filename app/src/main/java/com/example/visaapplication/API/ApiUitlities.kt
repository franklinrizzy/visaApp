package com.example.visaapplication.API

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiUitlities {

    fun getApiInterface() : ApiInterface{
        return Retrofit.Builder()
            .baseUrl("https://api.stripe.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(ApiInterface::class.java)
    }

}