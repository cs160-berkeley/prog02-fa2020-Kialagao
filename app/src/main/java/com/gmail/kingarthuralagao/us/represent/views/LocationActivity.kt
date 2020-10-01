package com.gmail.kingarthuralagao.us.represent.views

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.gmail.kingarthuralagao.us.represent.R
import com.gmail.kingarthuralagao.us.represent.api.Geolocation
import com.gmail.kingarthuralagao.us.represent.databinding.ActivityLocationBinding
import com.gmail.kingarthuralagao.us.represent.models.Result
import com.gmail.kingarthuralagao.us.represent.models.Results
import com.gmail.kingarthuralagao.us.represent.viewmodels.LocationActivityViewModel
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


const val TAG = "LocationActivity"

class LocationActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var locationManager : LocationManager
    private lateinit var locationListener: LocationListener
    private lateinit var binding: ActivityLocationBinding
    private lateinit var viewModel : LocationActivityViewModel
    private lateinit var gson : Gson
    private lateinit var retrofit : Retrofit
    private lateinit var geolocationApi : Geolocation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLocationBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this).get(LocationActivityViewModel::class.java)

        setContentView(binding.root)

        binding.currentLocationBtn.setOnClickListener(this)

        locationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationListener = LocationListener { location ->
            getResults(location.latitude, location.longitude)
        }


        gson = GsonBuilder()
            .setLenient()
            .create()

        retrofit = Retrofit.Builder()
            .baseUrl(resources.getString(R.string.GeolocationBaseUrl))
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        geolocationApi = retrofit.create(Geolocation::class.java)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, 60000,
                    0F,
                    locationListener
                )
            }
        }
    }

    override fun onClick(view: View?) {
        when (view!!.id) {
            binding.currentLocationBtn.id -> {
                if (ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    //binding.currentLocationBtn.startAnimation()

                    locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER, 60000,
                        0F,
                        locationListener
                    )
                } else {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        1
                    )
                }
            }
        }
    }

    private fun getResults(lat: Double, lng: Double) {
        val formattedQuery = "${lat},${lng}"
        val call = geolocationApi.getResults(formattedQuery, "street_address", resources.getString(R.string.api_key))
        call.enqueue(object : Callback<Results> {
            override fun onResponse(call: Call<Results>, response: Response<Results>) {
                if (!response.isSuccessful) { //Error
                    Log.i(TAG, "Code: ${response.code()}")
                    //textViewResult.setText("Code: " + response.code())
                    return
                }
                val results: List<Result> = response.body()!!.results
                for (result in results) {
                    Log.i(TAG, result.formattedAddress)
                }
            }

            override fun onFailure(call: Call<Results>, t: Throwable) {
                Log.i("Error", t.message!!)
            }
        })
    }
}


