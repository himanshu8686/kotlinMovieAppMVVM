package com.kotlinmovieappmvvm.network

import com.kotlinmovieappmvvm.models.PopularMovieResponse
import com.kotlinmovieappmvvm.utils.BASE_URL
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Project Name :KotlinMovieAppMVVM
 * Created By :Himanshu sharma on 17-03-2021
 * Package : com.kotlinmovieappmvvm.network
 */
interface MovieApi
{
    companion object{
        // MoviesApi() is equivalent to invoke function
        operator fun invoke() : MovieApi {
            return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
                .create(MovieApi::class.java)
        }
    }

    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("api_key") key:String,
        @Query("page") page:Int
    ) : Response<PopularMovieResponse>
}