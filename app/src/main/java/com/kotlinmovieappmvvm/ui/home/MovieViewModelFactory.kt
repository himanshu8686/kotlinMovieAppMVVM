package com.kotlinmovieappmvvm.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kotlinmovieappmvvm.repositories.MovieRepository

/**
 * Project Name :KotlinMovieAppMVVM
 * Created By :Himanshu sharma on 18-03-2021
 * Package : com.kotlinmovieappmvvm.ui.home
 */

class MovieViewModelFactory(
    private val movieRepository: MovieRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MovieViewModel(movieRepository) as T
    }
}