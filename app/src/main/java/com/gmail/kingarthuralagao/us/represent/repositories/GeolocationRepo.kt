package com.gmail.kingarthuralagao.us.represent.repositories

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.gmail.kingarthuralagao.us.represent.models.GeolocationResult
import com.gmail.kingarthuralagao.us.represent.models.Results
import com.gmail.kingarthuralagao.us.represent.services.Geolocation
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class GeolocationRepo {
    private val TAG = javaClass.simpleName
    var resultList: MutableList<GeolocationResult> = mutableListOf()
    val mutableLiveData = MutableLiveData<List<GeolocationResult>>()

    fun getResults(formattedQuery: String, key: String) : MutableLiveData<List<GeolocationResult>> {
        callService(formattedQuery, key)

        return mutableLiveData
    }

    private fun callService(formattedQuery: String, key: String) {
        val apiService = RetrofitClient.geolocationApi

        apiService.getResults(formattedQuery, "street_address", key).enqueue(object :
            Callback<Results> {
            override fun onResponse(call: Call<Results>, response: Response<Results>) {
                if (!response.isSuccessful) { //Error
                    Log.i(TAG, "Code: ${response.code()}")
                    return
                }

                resultList.clear()
                for (result in response.body()!!.geolocationResults) {
                    resultList.add(result)
                    Log.i(TAG, result.formattedAddress)
                }
                mutableLiveData.value = resultList
            }

            override fun onFailure(call: Call<Results>, t: Throwable) {
                Log.i("Error", t.message!!)
            }
        })
    }
}

object RetrofitClient {
    private val BASE_URL = "https://maps.googleapis.com/maps/api/geocode/"
    val gson = GsonBuilder().setLenient().create()

    val geolocationApi : Geolocation by lazy{
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        retrofit.create(Geolocation::class.java)
    }
}

