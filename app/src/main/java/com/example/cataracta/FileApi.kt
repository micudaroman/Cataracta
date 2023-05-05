package com.example.cataracta

import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import java.util.concurrent.TimeUnit


interface FileApi {

    @Multipart
    @POST("video/upload")
    fun uploadImage(
        @Part file: MultipartBody.Part
    ): Call<UploadResponse>

    data class UploadResponse(
        val left_eye: String,
        val right_eye: String,
    )

    companion object {

        const val URL = "http://147.175.121.168:5000/"
        val logginLevel =
            HttpLoggingInterceptor.Level.BASIC  //changed, as BODY logging is no more required
        val loggingInterceptor = HttpLoggingInterceptor().setLevel(logginLevel)

        operator fun invoke(): FileApi {
            val timeout = 500L
            val okHttpClient = OkHttpClient.Builder()
                .connectTimeout(timeout, TimeUnit.SECONDS)
                .readTimeout(timeout, TimeUnit.SECONDS)
                .writeTimeout(timeout, TimeUnit.SECONDS)
                .addInterceptor(loggingInterceptor)
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl(URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(FileApi::class.java)
        }
    }
}

