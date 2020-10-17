package com.gmail.kingarthuralagao.us.represent.repositories

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.gmail.kingarthuralagao.us.represent.models.voterinfo.VoterInfoResult
import com.gmail.kingarthuralagao.us.represent.services.IVoterInfo
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class VoterInfoRepo {
    private val TAG = javaClass.simpleName
    val mutableLiveData = MutableLiveData<MutableList<MutableMap<String, String>>>()
    val representativesList = mutableListOf<MutableMap<String, String>>()
    val addressInput = MutableLiveData<String>()

    fun getVoterInfo(address : String, key : String) : MutableLiveData<MutableList<MutableMap<String, String>>> {
        Log.i(TAG, "onGetVoterInfoResult")

        callService(address, key)
        return mutableLiveData
    }

    private fun callService(address : String, key : String) {
        val apiService = VoterInfoRetrofitClient.voterInfoApi

        apiService.getVoterInfo(address, 7000, key).enqueue(object :
            Callback<VoterInfoResult> {
            override fun onResponse(call: Call<VoterInfoResult>, response: Response<VoterInfoResult>) {
                if (!response.isSuccessful || response.body() == null) { //Error
                    var errorMessage = response.message()
                    Log.i(TAG, response.code().toString())
                    Log.i(TAG, response.toString())
                    Log.i(TAG, errorMessage)
                    //mutableLiveData.value = resource
                    return
                }

                //mutableLiveData.value = resource

                response.body()!!.dropOffLocations?.get(0)?.address?.city?.let { Log.i(TAG, it) }
            }

            override fun onFailure(call: Call<VoterInfoResult>, t: Throwable) {
                Log.i(TAG, t.message!!)
                //representativesList.clear()
                //resource = Resource(representativesList, t.message)
                //mutableLiveData.value = resource
            }
        })
    }
}

object VoterInfoRetrofitClient {
    private val BASE_URL = "https://civicinfo.googleapis.com/civicinfo/v2/"
    val gson = GsonBuilder().setLenient().create()

    val voterInfoApi : IVoterInfo by lazy{
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        retrofit.create(IVoterInfo::class.java)
    }
}