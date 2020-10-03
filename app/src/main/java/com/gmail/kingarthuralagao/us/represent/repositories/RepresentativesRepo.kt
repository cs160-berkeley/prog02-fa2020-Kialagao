package com.gmail.kingarthuralagao.us.represent.repositories

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.gmail.kingarthuralagao.us.represent.models.representatives.NormalizedInput
import com.gmail.kingarthuralagao.us.represent.services.IRepresentatives
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
    val mutableLiveData = MutableLiveData<MutableList<MutableMap<String, String>>>()
    val representativesList = mutableListOf<MutableMap<String, String>>()
    val addressInput = MutableLiveData<String>()

    fun getResult(address : String, key : String) : MutableLiveData<MutableList<MutableMap<String, String>> > {
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

                getRepresentativesInfo(response.body()!!)
                addressInput.value = buildAddress(response.body()!!.normalizedInput)
                mutableLiveData.value = representativesList
                addressInput.value = buildAddress(response.body()!!.normalizedInput)

                Log.i(TAG, "onCallResponse")
            }

            override fun onFailure(call: Call<Result>, t: Throwable) {
                Log.i("Error", t.message!!)
            }
        })
    }

    fun getRepresentativesInfo(result : Result) {

        val offices = result.offices
        representativesList.clear()
        for (officeIndex in 0 until offices.size) {
            if (offices[officeIndex].name == "U.S. Senator" || offices[officeIndex].name == "U.S. Representative") {
                for (officialIndex in offices[officeIndex].officialIndices) {
                    val m = mutableMapOf<String, String>()
                    m["office"] = offices[officeIndex].name
                    val official = result.officials[officialIndex]
                    m["name"] = official.name
                    m["party"] = official.party
                    m["photoUrl"] = official.photoUrl ?: ""
                    representativesList.add(m)
                }
            }
        }
    }

    fun buildAddress(input : NormalizedInput) : String {
        return "${input.line1} ${input.city}, ${input.state} ${input.zip}"
    }
}


object RepresentativesRetrofitClient {
    private val BASE_URL = "https://civicinfo.googleapis.com/civicinfo/v2/"
    val gson = GsonBuilder().setLenient().create()

    val representativesApi : IRepresentatives by lazy{
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        retrofit.create(IRepresentatives::class.java)
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
