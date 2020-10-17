package com.gmail.kingarthuralagao.us.represent.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.gmail.kingarthuralagao.us.represent.*
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

    fun fetchRepresentatives(address : String, key : String) {
        val formattedAddress = address.replace("\\s".toRegex(), "+")
        inputAddress = address
        representativesRepo.getResult(formattedAddress, key)
    }

    fun getRandomCoordinate() : Pair<Double, Double> {
        return locationRandomizer.getRandomCoordinate()
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
    private val rectanglesList : MutableList<Rectangle> by lazy {
        getRectangles()
    }

    fun getRandomCoordinate() : Pair<Double, Double>{
        val randomNum = oneToHundred()
        Log.i("LocationRandomizer", randomNum.toString())
        val rectangle = getRectangle(randomNum)

        val randomLat = generateRandomLatitude(rectangle.s,  rectangle.n)
        val randomLng = generateRandomLongitude(rectangle.w, rectangle.e)
        Log.d(javaClass.simpleName, "Latitude: $randomLat, Longitude: $randomLng")
        return Pair(randomLat, randomLng)
    }

    private fun oneToHundred() : Double {
        return Random.nextDouble(0.0, 100.0) //
    }

    private fun generateRandomLatitude(south : Double, north : Double) : Double {
        return Random.nextDouble(south, north)
    }

    private fun generateRandomLongitude(west : Double, east : Double) : Double {
        return Random.nextDouble(west, east)
    }

    private fun getRectangle(rectNumber : Double) : Rectangle {
        if (rectNumber < 1.4) {
            return rectanglesList[0]
        } else if (rectNumber >= 1.4 && rectNumber < 4.0) {
            return rectanglesList[1]
        } else if (rectNumber >= 4.0 && rectNumber < 9.6) {
            return rectanglesList[2]
        } else if (rectNumber >= 9.6 && rectNumber < 63.3) {
            return rectanglesList[3]
        } else if (rectNumber >= 63.3 && rectNumber < 71.3) {
            return rectanglesList[4]
        } else if (rectNumber >= 71.3 && rectNumber < 73.5) {
            return rectanglesList[5]
        } else if (rectNumber >= 73.5 && rectNumber < 73.9) {
            return rectanglesList[6]
        } else if (rectNumber >= 73.9 && rectNumber < 74.2) {
            return rectanglesList[7]
        } else if (rectNumber >= 74.2 && rectNumber < 78.1) {
            return rectanglesList[8]
        } else if (rectNumber >= 78.1 && rectNumber < 79.8) {
            return rectanglesList[9]
        } else if (rectNumber >= 79.8 && rectNumber < 80.2) {
            return rectanglesList[10]
        } else if (rectNumber >= 80.2 && rectNumber < 100.0) {
            return rectanglesList[11]
        } else if (rectNumber >= 100 && rectNumber < 100.6) {
            return rectanglesList[12]
        } else if (rectNumber >= 100.6 && rectNumber < 100.8) {
            return rectanglesList[13]
        } else {
            return rectanglesList[14]
        }
    }
}