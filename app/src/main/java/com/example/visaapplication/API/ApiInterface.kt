package com.example.visaapplication.API

import com.example.visaapplication.model.CustomerModel
import com.example.visaapplication.model.PaymentIntent
import com.example.visaapplication.utils.Utils.SECRET_KEY
import retrofit2.Response
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiInterface {

    @Headers("Authorization : Bearer $SECRET_KEY")
    @POST("v1/customers")
    suspend fun getCustomer() : Response<CustomerModel>

    @Headers("Authorization : Bearer $SECRET_KEY",
        "Stripe-Version: 2024-06-20")
    @POST("v1/ephemeral_keys")
    suspend fun getEphemeralKey(
        @Query("customer") customer : String
    ) : Response<CustomerModel>

    @Headers("Authorization : Bearer $SECRET_KEY")
    @POST("v1/payment_intents")
    suspend fun getPaymentIntent(
        @Query("customer") customer : String,
        @Query("amount") amount  : String = "129900",
        @Query("currency") currency : String = "inr",
        @Query("automatic_payment_methods[enabled]") automatePay : Boolean = true,
    ) : Response<PaymentIntent>
}