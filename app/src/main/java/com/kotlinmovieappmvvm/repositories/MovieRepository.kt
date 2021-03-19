package com.kotlinmovieappmvvm.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kotlinmovieappmvvm.MainActivity
import com.kotlinmovieappmvvm.models.PopularMovieResponse
import com.kotlinmovieappmvvm.network.MovieApi
import com.kotlinmovieappmvvm.network.SafeApiRequest
import com.kotlinmovieappmvvm.utils.API_KEY
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Project Name :KotlinMovieAppMVVM
 * Created By :Himanshu sharma on 17-03-2021
 * Package : com.kotlinmovieappmvvm.repositories
 */
class MovieRepository(private val movieApi: MovieApi) : SafeApiRequest() {

    /**
     *  This function will return the List of movies to the view model
     */
    suspend fun getPopularMovies() = apiRequest {

        movieApi.getPopularMovies(API_KEY, 1)

    }

}
