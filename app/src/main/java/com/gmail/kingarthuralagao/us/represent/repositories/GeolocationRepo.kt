package com.gmail.kingarthuralagao.us.represent.repositories

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.gmail.kingarthuralagao.us.represent.models.geolocation.GeolocationResult
import com.gmail.kingarthuralagao.us.represent.models.geolocation.Results
import com.gmail.kingarthuralagao.us.represent.services.IGeolocation
import com.gmail.kingarthuralagao.us.represent.viewmodels.Resource
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class GeolocationRepo {
    private val TAG = javaClass.simpleName
    var resultList: MutableList<GeolocationResult> = mutableListOf()
    val mutableLiveData = MutableLiveData<Resource<String>>()
    lateinit var resource: Resource<String>

    fun getResults(formattedQuery: String, key: String) : MutableLiveData<Resource<String>> {
        callService(formattedQuery, key)

        return mutableLiveData
    }

    private fun callService(formattedQuery: String, key: String) {
        val apiService = RetrofitClient.geolocationApi

        apiService.getResults(formattedQuery, key).enqueue(object :
            Callback<Results> {
            override fun onResponse(call: Call<Results>, response: Response<Results>) {
                if (!response.isSuccessful) { //Error
                    Log.i(TAG, "Code: ${response.code()}")
                    resource = Resource("", response.message())
                    return
                }

                resultList.clear()
                for (result in response.body()!!.geolocationResults) {
                    resultList.add(result)
                    Log.i(TAG, result.formattedAddress)
                }

                var resultAddress = ""
                for (address in resultList) {
                    if (!address.formattedAddress.contains("Unnamed") && address.formattedAddress.contains("USA")) {
                        resultAddress = address.formattedAddress
                        break
                    }
                }

                resource = Resource(resultAddress, "Location not in the U.S.")
                mutableLiveData.value = resource
            }

            override fun onFailure(call: Call<Results>, t: Throwable) {
                Log.i("Error", t.message!!)
                resultList.clear()
                resource = Resource("", t.message)
                mutableLiveData.value = resource
            }
        })
    }

    fun updateLocation(address: String) {
        resource = Resource(address, "")
        mutableLiveData.value = resource
    }
}

object RetrofitClient {
    private val BASE_URL = "https://maps.googleapis.com/maps/api/geocode/"
    val gson = GsonBuilder().setLenient().create()

    val geolocationApi : IGeolocation by lazy{
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        retrofit.create(IGeolocation::class.java)
    }
}

