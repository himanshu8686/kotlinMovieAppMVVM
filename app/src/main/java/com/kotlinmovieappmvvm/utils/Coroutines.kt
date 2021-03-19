package com.kotlinmovieappmvvm.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

/**
 * Project Name :KotlinMovieAppMVVM
 * Created By :Himanshu sharma on 18-03-2021
 * Package : com.kotlinmovieappmvvm.utils
 */

object Coroutines{

    fun main(work:suspend(()->Unit)) =
        CoroutineScope(Dispatchers.Main).launch {
            work()
        }

    fun io(work:suspend(()->Unit)) =
        CoroutineScope(Dispatchers.IO).launch {
            work()
        }

    /**
     * This function will take work as a parameter which is a suspending function
     * which return T and we havee another parameter as a callback function
     * after executing the suspending function we will get the results into the call back function
     */
    fun<T:Any> ioThenMain(work : suspend (()->T?), callback: ((T?)->Unit)) =

        /**
         * Inside the launch we will get the data from the IO Dispatchers
         */
        CoroutineScope(Dispatchers.Main).launch {
            val data = CoroutineScope(Dispatchers.IO).async  rt@{
                return@rt work()
            }.await()
            callback(data)
        }
}