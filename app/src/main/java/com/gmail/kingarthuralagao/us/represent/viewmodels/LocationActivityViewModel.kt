package com.gmail.kingarthuralagao.us.represent.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gmail.kingarthuralagao.us.represent.models.GeolocationResult
import com.gmail.kingarthuralagao.us.represent.models.representatives.Result
import com.gmail.kingarthuralagao.us.represent.repositories.GeolocationRepo
import com.gmail.kingarthuralagao.us.represent.repositories.RepresentativesRepo


class LocationActivityViewModel : ViewModel() {
    private val TAG = javaClass.simpleName
    private val geolocationRepo by lazy {
        GeolocationRepo()
    }

    private val representativesRepo by lazy {
        RepresentativesRepo()
    }

    var representativesMutableLiveData = MutableLiveData<Result>()
    val geolocationMutableLiveData = MutableLiveData<List<GeolocationResult>>()

    fun fetchResults(lat : Double, lng : Double, key : String) {
        val formattedQuery = "${lat},${lng}"
        val results = geolocationRepo.getResults(formattedQuery, key).value

        geolocationMutableLiveData.value = geolocationRepo.getResults(formattedQuery, key).value
    }

    fun getAddresses() : LiveData<List<GeolocationResult>> {
        return geolocationMutableLiveData
    }

    fun fetchRepresentatives(address : String, key : String) {
        val formattedAddress = address.replace("\\s".toRegex(), "+")
        representativesMutableLiveData.postValue(representativesRepo.getResult(formattedAddress, key).value)
    }
}