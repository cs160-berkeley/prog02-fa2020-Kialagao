package com.gmail.kingarthuralagao.us.represent.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gmail.kingarthuralagao.us.represent.models.GeolocationResult
import com.gmail.kingarthuralagao.us.represent.models.representatives.Result
import com.gmail.kingarthuralagao.us.represent.repositories.GeolocationRepo
import com.gmail.kingarthuralagao.us.represent.repositories.RepresentativesRepo


class MasterActivityViewModel : ViewModel() {
    private val TAG = javaClass.simpleName
    private val geolocationRepo by lazy {
        GeolocationRepo()
    }

    private val representativesRepo by lazy {
        RepresentativesRepo()
    }

    var representativesMutableLiveData = representativesRepo.mutableLiveData
    var geolocationMutableLiveData  = geolocationRepo.mutableLiveData

    fun fetchResults(lat : Double, lng : Double, key : String) {
        Log.i(TAG, "onFetchResults")
        val formattedQuery = "${lat},${lng}"

        geolocationRepo.getResults(formattedQuery, key)
    }

    fun fetchRepresentatives(address : String, key : String) {
        val formattedAddress = address.replace("\\s".toRegex(), "+")

        representativesRepo.getResult(formattedAddress, key)
    }
}