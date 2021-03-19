package com.kotlinmovieappmvvm.ui.home

import android.view.View
import com.kotlinmovieappmvvm.models.PopularMovieResponse

/**
 * Project Name :KotlinMovieAppMVVM
 * Created By :Himanshu sharma on 18-03-2021
 * Package : com.kotlinmovieappmvvm.ui.home
 */
interface OnMovieItemClickListener
{
    fun onDetailsButtonClick(view: View, movie: PopularMovieResponse.PopularMovieItem)
}