package com.kotlinmovieappmvvm.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kotlinmovieappmvvm.models.PopularMovieResponse
import com.kotlinmovieappmvvm.repositories.MovieRepository
import com.kotlinmovieappmvvm.utils.Coroutines
import kotlinx.coroutines.Job


class MovieViewModel(
    private val movieRepository: MovieRepository
) : ViewModel() {
    private val _popularMovies = MutableLiveData<List<PopularMovieResponse.PopularMovieItem>>()
    private lateinit var job : Job

    /**
     *  This getter is made because we don't want to expose mutable list to the ui
     */
    val getPopularMovies : LiveData<List<PopularMovieResponse.PopularMovieItem>>
        get() {
            return _popularMovies
        }

    fun setPopularMovieListToLiveData(){

        job = Coroutines.ioThenMain(
            {movieRepository.getPopularMovies()},
            {
                //_popularMovies.value = it.tot
                popularMovieResponse->
                if (popularMovieResponse != null) {
                    _popularMovies.value = popularMovieResponse.popularMovieItems
                }
                Log.d("TAG","List=> "+popularMovieResponse?.popularMovieItems)

            }
        )
    }

    /**
     *  To clear the Job
     */
    override fun onCleared() {
        super.onCleared()
        if (::job.isInitialized) job.cancel()
    }
}