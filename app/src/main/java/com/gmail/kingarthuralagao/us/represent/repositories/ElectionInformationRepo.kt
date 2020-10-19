package com.gmail.kingarthuralagao.us.represent.repositories

import android.os.AsyncTask
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.gmail.kingarthuralagao.us.represent.models.representatives.NormalizedInput
import com.gmail.kingarthuralagao.us.represent.models.representatives.Result
import com.gmail.kingarthuralagao.us.represent.models.voterinfo.VoterInfoResult
import com.gmail.kingarthuralagao.us.represent.services.IRepresentatives
import com.gmail.kingarthuralagao.us.represent.services.IVoterInfo
import com.gmail.kingarthuralagao.us.represent.viewmodels.Resource
import com.google.gson.GsonBuilder
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.InputStreamReader
import java.lang.RuntimeException
import java.net.HttpURLConnection
import java.net.URL

const val role1 : String = "legislatorLowerBody"
const val role2 : String = "legislatorUpperBody"

class ElectionInformationRepo {
    private val TAG = javaClass.simpleName
    val voterInfoMutableLiveData = MutableLiveData<Resource<VoterInfoResult>>()
    val representativesMutableLiveData = MutableLiveData<Resource<MutableList<MutableMap<String, String>>>>()
    lateinit var voterInfoResource : Resource<VoterInfoResult>
    lateinit var representativesResource : Resource<MutableList<MutableMap<String, String>>>
    val representativesList = mutableListOf<MutableMap<String, String>>()
    val addressInput = MutableLiveData<String>()

    fun getVoterInfo(address : String, key : String) : MutableLiveData<Resource<VoterInfoResult>> {
        Log.i(TAG, "onGetVoterInfoResult")

        voterInfoCallService(address, key)
        return voterInfoMutableLiveData
    }

    fun getRepresentatives(address : String, key : String) : MutableLiveData<Resource<MutableList<MutableMap<String, String>>>> {
        Log.i(TAG, "onGetResult")

        representativesCallService(address, key)
        return representativesMutableLiveData
    }

    private fun voterInfoCallService(address : String, key : String) {
        val apiService = VoterInfoRetrofitClient.voterInfoApi

        apiService.getVoterInfo(address, 7000, key).enqueue(object :
            Callback<VoterInfoResult> {
            override fun onResponse(call: Call<VoterInfoResult>, response: Response<VoterInfoResult>) {
                if (!response.isSuccessful || response.body() == null) { //Error
                    var errorMessage = response.message()
                    /*
                    Log.i(TAG, response.code().toString())
                    Log.i(TAG, response.toString())
                    Log.i(TAG, errorMessage)*/
                    //mutableLiveData.value = resource
                    return
                }

                voterInfoResource = Resource(response.body(), "")
                voterInfoMutableLiveData.value = voterInfoResource

                //response.body()!!.dropOffLocations?.get(0)?.address?.city?.let { Log.i(TAG, it) }
            }

            override fun onFailure(call: Call<VoterInfoResult>, t: Throwable) {
                Log.i(TAG, t.message!!)
                //representativesList.clear()
                //resource = Resource(representativesList, t.message)
                //mutableLiveData.value = resource
            }
        })
    }

    private fun representativesCallService(address : String, key : String) {
        Log.d(TAG, "address: $address")

        /*val BASE_URL = "https://civicinfo.googleapis.com/civicinfo/v2/"
                        val downloadTask = DownloadTask()
                        val formattedUrl = "${BASE_URL}representatives?address=${address}&roles=${role1}&roles=${role2}&key=${key}"
                        downloadTask.execute(formattedUrl)*/
        val apiService = RepresentativesRetrofitClient.representativesApi

        apiService.getResults(address, role1, role2, key).enqueue(object :
            Callback<Result> {
            override fun onResponse(call: Call<Result>, response: Response<Result>) {
                if (!response.isSuccessful || response.body() == null) { //Error
                    var errorMessage = response.message()
                    Log.i(TAG, response.code().toString())
                    Log.i(TAG, response.toString())
                    if (response.code() == 404) {
                        errorMessage = "Location must be within the borders of the U.S."
                    }
                    representativesResource = Resource(representativesList, errorMessage)
                    representativesMutableLiveData.value = representativesResource
                } else {
                    if (response.body()!!.offices != null && response.body()!!.officials != null) {
                        getRepresentativesInfo(response.body()!!)
                        addressInput.value = buildAddress(response.body()!!.normalizedInput)
                        representativesResource = Resource(representativesList, "")
                        representativesMutableLiveData.value = representativesResource
                    } else {
                        val BASE_URL = "https://civicinfo.googleapis.com/civicinfo/v2/"
                        val downloadTask = DownloadTask()
                        val formattedUrl = "${BASE_URL}representatives?address=${address}&roles=${role1}&roles=${role2}&key=${key}"
                        downloadTask.execute(formattedUrl)
                        //representativesResource = Resource(representativesList, "No Information Available")
                        //representativesMutableLiveData.value = representativesResource
                    }
                    Log.i(TAG, "onCallResponse")
                }
            }

            override fun onFailure(call: Call<Result>, t: Throwable) {
                Log.i("Error", t.message!!)
                //representativesList.clear()
                representativesResource = Resource(representativesList, t.message)
                representativesMutableLiveData.value = representativesResource
            }
        })
    }


    fun getRepresentativesInfo(result : Result) {
        val offices = result.offices
        representativesList.clear()
        for (officeIndex in 0 until offices.size) {
            if (offices[officeIndex].name == "U.S. Senator" || offices[officeIndex].name == "U.S. Representative") {
                for (officialIndex in offices[officeIndex].officialIndices) {
                    try {
                        val m = mutableMapOf<String, String>()
                        m["office"] = offices[officeIndex].name
                        val official = result.officials[officialIndex]
                        m["name"] = official.name
                        m["party"] = official.party
                        m["photoUrl"] = official.photoUrl ?: ""
                        m["phone"] = official.phones[0] ?: ""
                        m["website"] = official.urls[0] ?: ""
                        for (channel in official.channels) {
                            if (channel.type == "Twitter") { m["twitter"] = channel.id }
                            if (channel.type == "YouTube") { m["youtube"] = channel.id }
                        }
                        m["email"] = if (official.emails.isNullOrEmpty()) {
                            ""
                        } else {
                            Log.d(TAG, official.emails[0].toString())
                            official.emails[0].toString()
                        }

                        representativesList.add(m)
                    } catch (e : Exception) {

                    }
                }
            }
        }
    }

    fun buildAddress(input : NormalizedInput) : String {
        return "${input.line1} ${input.city}, ${input.state} ${input.zip}"
    }

    inner class DownloadTask : AsyncTask<String, Void, String>() {
        override fun doInBackground(vararg params: String): String {
            var result = ""
            val url: URL
            val urlConnection: HttpURLConnection

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
            } catch (e: Exception) {
                e.printStackTrace()
                return ""
            }
        }

        override fun onPostExecute(result: String) {
            super.onPostExecute(result)

            try {
                val jsonObject = JSONObject(result)

                val offices = jsonObject.getJSONArray("offices")
                val officials = jsonObject.getJSONArray("officials")
                val jsonObjects = JSONObject(offices.get(0).toString())
                Log.i("RepresentativeRepo", jsonObjects.getString("name"))

                representativesList.clear()
                for (officeIndex in 0 until offices.length()) {
                    val office = JSONObject(offices[officeIndex].toString())
                    if (office.getString("name") == "U.S. Senator" || office.getString("name") == "U.S. Representative") {
                        val officialIndices = office.getJSONArray("officialIndices")

                        for (officialIndex in 0 until officialIndices.length()) {
                            try {
                                val m = mutableMapOf<String, String>()
                                m["office"] = office.getString("name")
                                val official = officials.getJSONObject(officialIndex)
                                m["name"] = official.getString("name")
                                m["party"] = official.getString("party")
                                m["photoUrl"] = official.getString("photoUrl") ?: ""

                                val phoneUrls = official.getJSONArray("phones")
                                m["phone"] = phoneUrls.get(0).toString()

                                val websiteUrls = official.getJSONArray("urls")
                                m["website"] = websiteUrls[0].toString()

                                val channels = official.getJSONArray("channels")
                                for (channelIndex in 0 until channels.length()) {
                                    val channel = JSONObject(channels[channelIndex].toString())
                                    if (channel.getString("type") == "Twitter") { m["twitter"] = channel.getString("id") }
                                    if (channel.getString("type") == "YouTube") { m["youtube"] = channel.getString("id") }
                                }

                                val emails = official.getJSONArray("emails")
                                m["email"] = if (emails == null || emails.length() == 0) {
                                    ""
                                } else {
                                    //Log.d(TAG, official.emails[0].toString())
                                    emails[0].toString()
                                }
                                Log.d(javaClass.simpleName, m.toString())
                                representativesList.add(m)
                            } catch (e : Exception) {
                                Log.d(javaClass.simpleName, e.message.toString())

                            }
                        }
                    }
                }
                representativesResource = Resource(representativesList, "")
                representativesMutableLiveData.value = representativesResource
            } catch (e: Exception) {
                Log.i("Error", e.message.toString())
            }
        }
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

// Address 1430 Boyles Mill Rd NE, Dalton, GA 30721, USA is an edge case
// I-71 Lane Rd, Orient, OH 43146, USA