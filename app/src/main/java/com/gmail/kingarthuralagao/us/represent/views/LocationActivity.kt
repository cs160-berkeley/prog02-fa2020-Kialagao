package com.gmail.kingarthuralagao.us.represent.views

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.gmail.kingarthuralagao.us.represent.R
import com.gmail.kingarthuralagao.us.represent.databinding.ActivityLocationBinding
import com.gmail.kingarthuralagao.us.represent.models.GeolocationResult
import com.gmail.kingarthuralagao.us.represent.viewmodels.LocationActivityViewModel


class LocationActivity : AppCompatActivity(), View.OnClickListener {

    private val TAG = javaClass.simpleName
    private lateinit var locationManager : LocationManager
    private lateinit var locationListener: LocationListener
    private lateinit var binding: ActivityLocationBinding
    private lateinit var viewModel : LocationActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLocationBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this).get(LocationActivityViewModel::class.java)

        setContentView(binding.root)

        binding.currentLocationBtn.setOnClickListener(this)

        locationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                fetchResults(location.latitude, location.longitude)
            }
            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}

            override fun onProviderEnabled(provider: String?) {}

            override fun onProviderDisabled(provider: String?) {}
        }

        viewModel.geolocationMutableLiveData.observe(this,
            { results ->
                val tempBMP = BitmapFactory.decodeResource(
                    resources,
                    R.drawable.ic_done_white_48dp
                )
                binding.currentLocationBtn.doneLoadingAnimation(R.color.colorAccent, tempBMP)

                if (results != null && !results.isEmpty()) {
                    for (result in viewModel.geolocationMutableLiveData.value!!) {
                        Log.i(TAG, result.formattedAddress)
                    }
                    Log.i(TAG, "Hello")
                    viewModel.fetchRepresentatives(results[0].formattedAddress, resources.getString(R.string.api_key))
                }


                object : CountDownTimer(1000, 1000) {
                    override fun onTick(millisUntilFinished: Long) {
                    }

                    override fun onFinish() {
                        binding.currentLocationBtn.revertAnimation {
                            binding.currentLocationBtn.background = resources.getDrawable(
                                R.drawable.current_location_btn,
                                null
                            )
                        }
                    }
                }.start()
            })

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
                binding.currentLocationBtn.startAnimation()

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

    private fun fetchResults(lat: Double, lng: Double) {
        viewModel.fetchResults(lat, lng, resources.getString(R.string.api_key))
    }
}


