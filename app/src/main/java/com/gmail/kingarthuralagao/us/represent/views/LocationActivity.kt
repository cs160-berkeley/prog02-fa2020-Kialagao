package com.gmail.kingarthuralagao.us.represent.views

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
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
import com.gmail.kingarthuralagao.us.represent.models.representatives.Result
import com.gmail.kingarthuralagao.us.represent.viewmodels.LocationActivityViewModel


class LocationActivity : AppCompatActivity(), View.OnClickListener {

    public val TAG = javaClass.simpleName
    private lateinit var locationManager : LocationManager
    private lateinit var locationListener: LocationListener
    private lateinit var binding: ActivityLocationBinding
    private lateinit var viewModel : LocationActivityViewModel
    private val doneImage : Bitmap by lazy {
        BitmapFactory.decodeResource(resources, R.drawable.ic_done_white_48dp)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLocationBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this).get(LocationActivityViewModel::class.java)

        setContentView(binding.root)

        binding.currentLocationBtn.setOnClickListener(this)

        locationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                Log.i(TAG, "onLocationChanged")
                fetchResults(location.latitude, location.longitude)
                locationManager.removeUpdates(this)
                //fetchResults(latitude, longitude)
            }
            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
            override fun onProviderEnabled(provider: String?) {}
            override fun onProviderDisabled(provider: String?) {}
        }

        /*
        viewModel.geolocationMutableLiveData.observe(this,
            { results ->
                binding.currentLocationBtn.doneLoadingAnimation(R.color.colorAccent, doneImage)
                Log.i(TAG, "onObserver")
                if (results != null && results.isNotEmpty()) {
                    for (result in results) {
                        Log.i(TAG, result.formattedAddress)
                    }
                    //Log.i(TAG, "Hello")
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
            })*/
        viewModel.geolocationMutableLiveData.observe(this, geolocationObserver)
        viewModel.representativesMutableLiveData.observe(this, representativesObserver)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, 5000,
                    0F,
                    locationListener
                )
            }
        }
    }

    override fun onClick(view: View?) {
        when (view!!.id) {
            binding.currentLocationBtn.id -> {

                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    binding.currentLocationBtn.startAnimation()
                    Log.i(TAG, "onClick")
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0F, locationListener)

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

    val geolocationObserver = Observer<List<GeolocationResult>> {
        Log.i(TAG, "onObserver")
        if (it != null && it.isNotEmpty()) {
            for (result in it) {
                Log.i(TAG, result.formattedAddress)
            }
            viewModel.fetchRepresentatives(it[0].formattedAddress, resources.getString(R.string.api_key))
        }
    }


    val representativesObserver = Observer<Result> {
        if (it != null) {
            for (official in it.officials) {
                Log.i(TAG, official.name)
            }
        }

        binding.currentLocationBtn.doneLoadingAnimation(R.color.colorAccent, doneImage)
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
    }

}




