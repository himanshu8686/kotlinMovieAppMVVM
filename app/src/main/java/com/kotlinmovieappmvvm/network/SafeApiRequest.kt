package com.kotlinmovieappmvvm.network

import com.kotlinmovieappmvvm.utils.ApiException
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response

/**
 * Project Name :KotlinMovieAppMVVM
 * Created By :Himanshu sharma on 17-03-2021
 * Package : com.kotlinmovieappmvvm.network
 */
abstract class SafeApiRequest {
    // a suspend function of type Generic will return type T
    // This apiRequest function takes another functionas a  parameter
    // This function is api call which we want to execute and it is a suspend function which gives aResponse of type T
    suspend fun <T:Any> apiRequest(call : suspend ()->Response<T>) : T? {
        val response=call.invoke()
        if (response.isSuccessful){
            return response.body()
        }else{
            throw ApiException(response.code().toString())
        }
    }
}