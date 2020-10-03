package com.gmail.kingarthuralagao.us.represent.views

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.gmail.kingarthuralagao.us.represent.R
import com.gmail.kingarthuralagao.us.represent.models.GeolocationResult
import com.gmail.kingarthuralagao.us.represent.models.representatives.Result
import com.gmail.kingarthuralagao.us.represent.viewmodels.MasterActivityViewModel
import kotlinx.android.synthetic.main.activity_master.*

class MasterActivity : AppCompatActivity(), OptionsFragment.OnButtonClick {

    private val optionsFragment : OptionsFragment by lazy {
        OptionsFragment()
    }
    private val TAG = javaClass.simpleName
    private lateinit var locationManager : LocationManager
    private lateinit var locationListener: LocationListener
    lateinit var masterActivityViewModel : MasterActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_master)

        masterActivityViewModel = ViewModelProvider(this).get(MasterActivityViewModel::class.java)

        locationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                Log.i(TAG, "onLocationChanged")
                fetchResults(location.latitude, location.longitude)
                locationManager.removeUpdates(this)
            }
            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
            override fun onProviderEnabled(provider: String?) {}
            override fun onProviderDisabled(provider: String?) {}
        }

        masterActivityViewModel.geolocationMutableLiveData.observe(this, geolocationObserver)
        masterActivityViewModel.representativesMutableLiveData.observe(this, representativesObserver)
        supportFragmentManager.beginTransaction().add(fragmentContainer.id, optionsFragment).commit()
    }


    private val geolocationObserver = Observer<List<GeolocationResult>> {
        Log.i(TAG, "onObserver")
        if (it != null && it.isNotEmpty()) {
            for (result in it) {
                Log.i(TAG, result.formattedAddress)
            }
            masterActivityViewModel.fetchRepresentatives(it[0].formattedAddress, resources.getString(R.string.api_key))
        }
    }


    private val representativesObserver = Observer<Result> {
        if (it != null) {
            for (official in it.officials) {
                Log.i(TAG, official.name)
            }
            optionsFragment.onDataFetched()
        }
    }

    private fun fetchResults(lat: Double, lng: Double) {
        masterActivityViewModel.fetchResults(lat, lng, resources.getString(R.string.api_key))
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
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

    override fun onCurrentLocationBtnClick() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
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

    override fun onSearchLocationBtnClick() {
        //
    }
}
