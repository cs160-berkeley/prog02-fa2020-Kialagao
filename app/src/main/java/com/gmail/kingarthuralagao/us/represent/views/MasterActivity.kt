package com.gmail.kingarthuralagao.us.represent.views

import android.Manifest
import android.content.Context
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
import com.gmail.kingarthuralagao.us.represent.adapters.RepresentativesRecyclerViewAdapter
import com.gmail.kingarthuralagao.us.represent.models.GeolocationResult
import com.gmail.kingarthuralagao.us.represent.viewmodels.MasterActivityViewModel
import kotlinx.android.synthetic.main.activity_master.*

class MasterActivity : AppCompatActivity(), OptionsFragment.IButtonClickListener, RepresentativesFragment.IRepresentativesFragmentListener {

    private val optionsFragment : OptionsFragment by lazy {
        OptionsFragment()
    }

    private val representativesFragment : RepresentativesFragment by lazy {
        Log.i(TAG, "creatingRepFragment")
        RepresentativesFragment.newInstance(representativesRecyclerViewAdapter, masterActivityViewModel.inputAddress)
    }

    private val TAG = javaClass.simpleName
    private lateinit var locationManager : LocationManager
    private lateinit var locationListener: LocationListener
    lateinit var masterActivityViewModel : MasterActivityViewModel
    lateinit var representativesRecyclerViewAdapter: RepresentativesRecyclerViewAdapter
    private val representativesList = mutableListOf<MutableMap<String, String>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_master)

        masterActivityViewModel = ViewModelProvider(this).get(MasterActivityViewModel::class.java)

        locationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                Log.i(TAG, "onLocationChanged")
                masterActivityViewModel.fetchResults(location.latitude, location.longitude, resources.getString(R.string.api_key))
                locationManager.removeUpdates(this)
            }
        }

        masterActivityViewModel.geolocationMutableLiveData.observe(this, geolocationObserver)
        masterActivityViewModel.representativesMutableLiveData.observe(this, representativesObserver)
        supportFragmentManager.beginTransaction().add(fragmentContainer.id, optionsFragment).commit()
    }


    private val geolocationObserver = Observer<List<GeolocationResult>> {
        Log.i(TAG, "onObserver")
        if (it != null && it.isNotEmpty()) {
            var address = it[0].formattedAddress
            for (result in it) {
                Log.i(TAG, result.formattedAddress)
                if (!result.formattedAddress.contains("Unnamed")) {
                    address = result.formattedAddress
                    break
                }
            }

            masterActivityViewModel.fetchRepresentatives(address, resources.getString(R.string.api_key))
        }
    }


    private val representativesObserver = Observer<MutableList<MutableMap<String, String>>> {
        if (it != null) {
            for (i in it) {
                Log.i(TAG, i["name"]!!)
            }

            if (optionsFragment.isVisible) {
                representativesRecyclerViewAdapter = RepresentativesRecyclerViewAdapter(it)
                optionsFragment.binding.currentLocationBtn.doneLoadingAnimation(
                    R.color.colorAccent,
                    optionsFragment.doneImage
                )
                object : CountDownTimer(2000, 1000) {
                    override fun onTick(millisUntilFinished: Long) {
                        if (millisUntilFinished <= 1000L) {
                            optionsFragment.binding.currentLocationBtn.revertAnimation {
                                optionsFragment.binding.currentLocationBtn.background = resources.getDrawable(R.drawable.current_location_btn, null)
                            }
                        }
                    }

                    override fun onFinish() {
                        if (supportFragmentManager.findFragmentByTag("options") == null) {
                            supportFragmentManager.beginTransaction()
                                .add(fragmentContainer.id, representativesFragment, "options")
                                .hide(optionsFragment).commit()
                        } else {
                            supportFragmentManager.beginTransaction().hide(optionsFragment).commit()
                        }
                    }
                }.start()
            } else {
                representativesRecyclerViewAdapter.setData(it)
                representativesFragment.binding.addressTv.text = masterActivityViewModel.inputAddress
            }
        }
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

    private fun getUserLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "onClick")
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0F, locationListener)
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
        }
    }


    // *************************************** Communication With Fragments ***************************************** //
    override fun onCurrentLocationBtnClick() {
        getUserLocation()
    }

    override fun onSearchLocationBtnClick() {
        //
    }

    override fun onCardViewItemClick(position: Int) {
        TODO("Not yet implemented")
    }

    override fun onIconClick(tag: String) {
        if (tag == resources.getString(R.string.myLocationIconTag)) {
            getUserLocation()
        } else {
            Toast.makeText(this, "CLicked", Toast.LENGTH_SHORT).show()
        }
    }
}
