package com.gmail.kingarthuralagao.us.represent.repositories

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.gmail.kingarthuralagao.us.represent.services.Representatives
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.gmail.kingarthuralagao.us.represent.models.representatives.Result

const val role1 : String = "legislatorLowerBody"
const val role2 : String = "legislatorUpperBody"

class RepresentativesRepo {
    private val TAG = javaClass.simpleName
    val mutableLiveData = MutableLiveData<Result>()

    fun getResult(address : String, key : String) : MutableLiveData<Result> {
        Log.i(TAG, "onGetResult")

        callService(address, key)
        return mutableLiveData
    }

    private fun callService(address : String, key : String) {

        val apiService = RepresentativesRetrofitClient.representativesApi

        apiService.getResults(address, role1, role2, key).enqueue(object :
            Callback<Result> {
            override fun onResponse(call: Call<Result>, response: Response<Result>) {
                if (!response.isSuccessful || response.body() == null) { //Error
                    Log.i(TAG, "Code: ${response.code()}")
                    return
                }

                mutableLiveData.value = response.body()

                val results = response.body()!!
                for (official in results.officials) {
                    Log.i(TAG, official.name)
                }
                Log.i(TAG, "onCallResponse")
            }

            override fun onFailure(call: Call<Result>, t: Throwable) {
                Log.i("Error", t.message!!)
            }
        })
    }
}


object RepresentativesRetrofitClient {
    private val BASE_URL = "https://civicinfo.googleapis.com/civicinfo/v2/"
    val gson = GsonBuilder().setLenient().create()

    val representativesApi : Representatives by lazy{
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        retrofit.create(Representatives::class.java)
    }
}

/*
        val BASE_URL = "https://civicinfo.googleapis.com/civicinfo/v2/"
        val downloadTask = DownloadTask()
        val formattedUrl = "${BASE_URL}representatives?address=${address}&roles=${role1}&roles=${role2}&key=${key}"
        val url = "https://civicinfo.googleapis.com/civicinfo/v2/representatives?address=35%20Castillo%20Street%20San%20Francisco%20California%2C%2094134&roles=legislatorUpperBody&roles=legislatorLowerBody&key=${key}"
        downloadTask.execute(formattedUrl)
class DownloadTask : AsyncTask<String, Void, String>() {
    override fun doInBackground(vararg params: String): String {
        var result = ""
        val url: URL
        val urlConnection : HttpURLConnection

        try {
            url = URL(params[0])
            urlConnection = url.openConnection() as HttpURLConnection
            val inputStream = urlConnection.inputStream
            val reader = InputStreamReader(inputStream)
            var data = reader.read()

            while (data != -1) {
                val current = data.toChar()
                result += current
                data = reader.read()
            }
            return result
        } catch (e : Exception) {
            e.printStackTrace()
            return ""
        }
    }

    override fun onPostExecute(result: String) {
        super.onPostExecute(result)

        Log.i("RepresentativeRepo", result)
    }
}*/
