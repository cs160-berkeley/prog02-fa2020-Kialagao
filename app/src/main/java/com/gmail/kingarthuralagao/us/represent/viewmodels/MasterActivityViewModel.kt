package com.gmail.kingarthuralagao.us.represent.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gmail.kingarthuralagao.us.represent.AlaskaBoundaries
import com.gmail.kingarthuralagao.us.represent.HawaiiBoundaries
import com.gmail.kingarthuralagao.us.represent.MainBoundaries
import com.gmail.kingarthuralagao.us.represent.adapters.RepresentativesRecyclerViewAdapter
import com.gmail.kingarthuralagao.us.represent.models.GeolocationResult
import com.gmail.kingarthuralagao.us.represent.models.representatives.Result
import com.gmail.kingarthuralagao.us.represent.repositories.GeolocationRepo
import com.gmail.kingarthuralagao.us.represent.repositories.RepresentativesRepo
import kotlin.random.Random

class MasterActivityViewModel : ViewModel() {
    private val TAG = javaClass.simpleName
    private val locationRandomizer by lazy {
        LocationRandomizer()
    }

    private val geolocationRepo by lazy {
        GeolocationRepo()
    }

    private val representativesRepo by lazy {
        RepresentativesRepo()
    }

    var representativesMutableLiveData = representativesRepo.mutableLiveData
    var geolocationMutableLiveData  = geolocationRepo.mutableLiveData
    var inputAddress = ""

    fun fetchResults(lat : Double, lng : Double, key : String) {
        Log.i(TAG, "onFetchResults")
        val formattedQuery = "${lat},${lng}"

        geolocationRepo.getResults(formattedQuery, key)
    }

    fun hasValidAddress(lat : Double, lng : Double, key : String) {
        val formattedQuery = "${lat},${lng}"
    }

    fun fetchRepresentatives(address : String, key : String) {
        val formattedAddress = address.replace("\\s".toRegex(), "+")
        inputAddress = address
        representativesRepo.getResult(formattedAddress, key)
    }

    fun getRandomCoordinates() : MutableList<Pair<Double, Double>> {
        return locationRandomizer.getRandomCoordinates()
    }

    fun isInTheUS(lat : Double, lng: Double) : Boolean {
        return withinAlaska(lat, lng) || withinHawaii(lat, lng) || withinTheBorders(lat, lng)
    }

    private fun withinAlaska(lat : Double, lng: Double) : Boolean{
        return (AlaskaBoundaries.southernMost <= lat && lat <= AlaskaBoundaries.northernMost)
                && ((AlaskaBoundaries.westernMost <= lng && lng <= AlaskaBoundaries.easternMost))
    }

    private fun withinHawaii(lat : Double, lng: Double) : Boolean {
        return (HawaiiBoundaries.southernMost <= lat && lat <= HawaiiBoundaries.northernMost)
                && (HawaiiBoundaries.westernMost <= lng && lng <= HawaiiBoundaries.easternMost)
    }

    private fun withinTheBorders(lat : Double, lng: Double) : Boolean{
        return (MainBoundaries.southernMost <= lat && lat <= MainBoundaries.northernMost)
                && (MainBoundaries.westernMost <= lng && lng <= MainBoundaries.easternMost)
    }
}

data class Resource<T>(var data: T?, var message: String?) {
    companion object {
        fun <T> success(data: T?): Resource<T> {
            return Resource(data, null)
        }

        fun <T> error(msg: String, data: T?): Resource<T> {
            return Resource(data, msg)
        }
    }
}

class LocationRandomizer {
    fun getRandomCoordinates() : MutableList<Pair<Double, Double>>{
        val stateNum = oneToFifty()
        val coordinatePairs = mutableListOf<Pair<Double, Double>>()
        when (stateNum) {
            49 -> {
                for (i in 0..10) {
                    val lat = generateRandomLatitude(AlaskaBoundaries.southernMost, AlaskaBoundaries.northernMost)
                    val long = generateRandomLongitude(AlaskaBoundaries.westernMost, AlaskaBoundaries.easternMost)
                    val latLng = Pair(lat, long)
                    coordinatePairs.add(latLng)
                }
            }
            50 -> {
                for (i in 0..10) {
                    val lat = generateRandomLatitude(HawaiiBoundaries.southernMost, HawaiiBoundaries.northernMost)
                    val long = generateRandomLongitude(HawaiiBoundaries.westernMost, HawaiiBoundaries.easternMost)
                    val latLng = Pair(lat, long)
                    coordinatePairs.add(latLng)
                }
            }
            else -> {
                for (i in 0..10) {
                    val lat = generateRandomLatitude(MainBoundaries.southernMost, MainBoundaries.northernMost)
                    val long = generateRandomLongitude(MainBoundaries.westernMost, MainBoundaries.easternMost)
                    val latLng = Pair(lat, long)
                    coordinatePairs.add(latLng)
                }
            }
        }
        return coordinatePairs
    }

    private fun oneToFifty() : Int {
        return Random.nextInt(1,51) //
    }

    private fun generateRandomLatitude(south : Double, north : Double) : Double {
        return Random.nextDouble(south, north + 1)
    }

    private fun generateRandomLongitude(west : Double, east : Double) : Double {
        return Random.nextDouble(west, east + 1)
    }
}