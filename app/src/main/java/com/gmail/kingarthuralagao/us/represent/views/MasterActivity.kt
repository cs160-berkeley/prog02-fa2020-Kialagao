package com.gmail.kingarthuralagao.us.represent.views

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.gmail.kingarthuralagao.us.represent.R
import com.gmail.kingarthuralagao.us.represent.adapters.RepresentativesRecyclerViewAdapter
import com.gmail.kingarthuralagao.us.represent.models.geolocation.GeolocationResult
import com.gmail.kingarthuralagao.us.represent.viewmodels.MasterActivityViewModel
import com.gmail.kingarthuralagao.us.represent.viewmodels.Resource
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import kotlinx.android.synthetic.main.activity_master.*
import kotlin.properties.Delegates

const val AUTOCOMPLETE_REQUEST_CODE = 2
const val PERMISSION_REQUEST_CODE = 1

class MasterActivity : AppCompatActivity(), OptionsFragment.IButtonClickListener, RepresentativesFragment.IRepresentativesFragmentListener,
    ElectionInformationFragment.IIconClickListener, RepresentativeInfoDialog.IOnLinkClickListener {
    private val optionsFragment : OptionsFragment by lazy {
        OptionsFragment()
    }

    lateinit var electionInformationFragment: ElectionInformationFragment

    private val TAG = javaClass.simpleName
    private lateinit var locationManager : LocationManager
    private lateinit var locationListener: LocationListener
    lateinit var masterActivityViewModel : MasterActivityViewModel
    private lateinit var representativesRecyclerViewAdapter: RepresentativesRecyclerViewAdapter
    private var representativePosition by Delegates.notNull<Int>()

    override fun onBackPressed() {
        if (optionsFragment.isVisible) {
            return
        }
        electionInformationFragment.cleanAdapter()
        super.onBackPressed()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_master)

        masterActivityViewModel = ViewModelProvider(this).get(MasterActivityViewModel::class.java)

        locationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                Log.i(TAG, "onLocationChanged")
                if (!masterActivityViewModel.isInTheUS(location.latitude, location.longitude)) {

                    if (optionsFragment.isVisible) {
                        optionsFragment.manageButtons(R.drawable.ic_error)
                        optionsFragment.startTimer(resources.getString(R.string.locationOutsideUS))
                    } else {
                        //representativesFragment.showErrorMsg(resources.getString(R.string.locationOutsideUS))
                    }
                    locationManager.removeUpdates(this)
                } else {
                    masterActivityViewModel.fetchResults(
                        location.latitude, location.longitude, resources.getString(
                            R.string.api_key
                        )
                    )
                    locationManager.removeUpdates(this)
                }
            }

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
        }

        masterActivityViewModel.geolocationMutableLiveData.observe(this, geolocationObserver)
        /*masterActivityViewModel.representativesMutableLiveData.observe(
            this,
            representativesObserver
        )*/
        if (supportFragmentManager.findFragmentByTag("options") == null) {
            supportFragmentManager.beginTransaction().add(fragmentContainer.id, optionsFragment).commit()
        }
    }

    private fun switchFragments() {
        val address = masterActivityViewModel.geolocationMutableLiveData.value?.data.toString()
        electionInformationFragment = ElectionInformationFragment.newInstance(address)
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_right_to_left, R.anim.enter_left_to_right, R.anim.exit_left_to_right)
            .replace(fragmentContainer.id, electionInformationFragment,"electionInformation")
            .addToBackStack(null)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()
    }

    private fun getUserLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0F, locationListener
            )
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), PERMISSION_REQUEST_CODE
            )
        }
    }

    private fun initializePlaceAutocomplete() {
        Places.initialize(applicationContext, resources.getString(R.string.api_key))

        val placesClient = Places.createClient(this)
        val fields = listOf(Place.Field.NAME, Place.Field.ADDRESS)

        val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
            .setCountry("US")
            .build(this)
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE)
    }

    private val geolocationObserver = Observer<Resource<String>> {
        Log.i(TAG, "onObserver")
        if (it != null && it.data!!.isNotEmpty()) {
            Log.i(TAG, it.data!!)

            fetchElectionInformation(it.data!!)
        } else {
            Log.i(TAG, "Geolocation Error: ${it.message}")
            if (optionsFragment.isVisible) {
                optionsFragment.manageButtons(R.drawable.ic_error)
                optionsFragment.startTimer(resources.getString(R.string.locationOutsideUS))
            } else {
                electionInformationFragment.showErrorMsg(resources.getString(R.string.locationOutsideUS))
            }
        }

    }

    private fun fetchElectionInformation(address : String) {

        if (optionsFragment.isVisible) {
            optionsFragment.manageButtons(0)
            if (optionsFragment.activeButton?.id == optionsFragment.binding.currentLocationBtn.id) {
                Handler(Looper.getMainLooper())
                Handler(Looper.getMainLooper()).postDelayed({optionsFragment.binding.currentLocationBtn.doneLoadingAnimation(R.color.colorAccent,
                    BitmapFactory.decodeResource(resources, R.drawable.ic_done_white_48dp)) }, 0)
                Handler(Looper.getMainLooper()).postDelayed({ optionsFragment.binding.currentLocationBtn.revertAnimation() }, 1500)
                Handler(Looper.getMainLooper()).postDelayed({switchFragments()}, 2000)
            } else {
                switchFragments()
            }
        } else {
            electionInformationFragment.updateVoterInfo(address)
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
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0F, locationListener
                )
            }
        } else {
            optionsFragment.manageButtons(0)
            Handler(Looper.getMainLooper()).postDelayed({ optionsFragment.binding.currentLocationBtn.revertAnimation() }, 0)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    data?.let {
                        val place = Autocomplete.getPlaceFromIntent(data)
                        Log.i(TAG, place.address.toString())
                        masterActivityViewModel.updateLocation(place.address.toString())
                    }
                }
                AutocompleteActivity.RESULT_ERROR -> {
                    data?.let {
                        val status = Autocomplete.getStatusFromIntent(data)
                        Log.i(TAG, status.statusMessage.toString())
                    }
                }
                Activity.RESULT_CANCELED -> {
                    optionsFragment.manageButtons(0)
                    // The user canceled the operation.
                }
            }
            return
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    // *************************************** Communication With Fragments ***************************************** //
    override fun onCurrentLocationBtnClick() {
        getUserLocation()
    }

    override fun onSearchLocationBtnClick() {
        initializePlaceAutocomplete()
    }

    override fun onRandomizeLocation() {
        val randomCoordinate = masterActivityViewModel.getRandomCoordinate()
        masterActivityViewModel.fetchResults(randomCoordinate.first, randomCoordinate.second, resources.getString(R.string.api_key))
    }

    override fun onCardViewItemClick(representativeMap : MutableMap<String, String>) {
        //Log.i(TAG, position.toString())
        //representativePosition = position
        val representativeInfoDialog = RepresentativeInfoDialog.newInstance(representativeMap as HashMap<String, String>)
        representativeInfoDialog.show(supportFragmentManager, "")
    }

    override fun onIconClick(tag: String) {
        when (tag) {
            resources.getString(R.string.myLocationIconTag) -> {
                getUserLocation()
            }
            resources.getString(R.string.searchIconTag) -> {
                initializePlaceAutocomplete()
            }
            else -> {
                val randomCoordinate = masterActivityViewModel.getRandomCoordinate()
                masterActivityViewModel.fetchResults(randomCoordinate.first, randomCoordinate.second, resources.getString(R.string.api_key))
            }
        }
    }

    override fun onLinkClick(tag: String, link: String) {
        when (tag) {
            resources.getString(R.string.website) -> {
                val browserIntent = Intent(
                    Intent.ACTION_VIEW, Uri.parse(link))
                startActivity(browserIntent)
            }

            resources.getString(R.string.phone) -> {
                val u = Uri.parse("tel:$link")
                val i = Intent(Intent.ACTION_DIAL, u)

                try {
                    startActivity(i)
                } catch (s: SecurityException) {
                    Toast.makeText(this, s.message, Toast.LENGTH_LONG).show()
                }
            }

            resources.getString(R.string.twitter) -> {
                try {
                    val intent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://twitter.com/${link}")
                    )
                    startActivity(intent)
                } catch (e: Exception) {
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://twitter.com/${link}")
                        )
                    )
                }
            }

            resources.getString(R.string.youtube) -> {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = if(link.length==24) {
                    Uri.parse("https://youtube.com/channel/${link}")
                } else {
                    Uri.parse("https://youtube.com/${link}")
                }
                startActivity(intent)
            }

            else -> {
                val emailIntent = Intent(Intent.ACTION_SEND)

                emailIntent.type = "plain/text";
                emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(link))
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject")
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Text")

                startActivity(Intent.createChooser(emailIntent, "Send mail..."))
            }
        }
    }
}
/*
const val AUTOCOMPLETE_REQUEST_CODE = 2
const val PERMISSION_REQUEST_CODE = 1
class MasterActivity : AppCompatActivity(), OptionsFragment.IButtonClickListener,
    RepresentativesFragment.IRepresentativesFragmentListener, RepresentativeInfoDialog.IOnLinkClickListener {

    private val optionsFragment : OptionsFragment by lazy {
        OptionsFragment()
    }

    lateinit var representativesFragment : RepresentativesFragment

    private val TAG = javaClass.simpleName
    private lateinit var locationManager : LocationManager
    private lateinit var locationListener: LocationListener
    lateinit var masterActivityViewModel : MasterActivityViewModel
    private lateinit var representativesRecyclerViewAdapter: RepresentativesRecyclerViewAdapter
    private var representativePosition by Delegates.notNull<Int>()

    override fun onBackPressed() {
        if (optionsFragment.isVisible) {
            return
        }
        super.onBackPressed()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_master)

        masterActivityViewModel = ViewModelProvider(this).get(MasterActivityViewModel::class.java)

        locationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                Log.i(TAG, "onLocationChanged")
                if (!masterActivityViewModel.isInTheUS(location.latitude, location.longitude)) {

                    if (optionsFragment.isVisible) {
                        optionsFragment.manageButtons(R.drawable.ic_error)
                        optionsFragment.startTimer(resources.getString(R.string.locationOutsideUS))
                    } else {
                        representativesFragment.showErrorMsg(resources.getString(R.string.locationOutsideUS))
                    }
                    locationManager.removeUpdates(this)
                } else {
                    masterActivityViewModel.fetchResults(
                        location.latitude, location.longitude, resources.getString(
                            R.string.api_key
                        )
                    )
                    locationManager.removeUpdates(this)
                }
            }
        }

        masterActivityViewModel.geolocationMutableLiveData.observe(this, geolocationObserver)
        masterActivityViewModel.representativesMutableLiveData.observe(
            this,
            representativesObserver
        )
        if (supportFragmentManager.findFragmentByTag("options") == null) {
            supportFragmentManager.beginTransaction().add(fragmentContainer.id, optionsFragment).commit()
        }
    }


    private val geolocationObserver = Observer<Resource<List<GeolocationResult>>> {
        Log.i(TAG, "onObserver")
        if (it != null && it.data!!.isNotEmpty()) {
            var address = it.data!![0].formattedAddress
            for (result in it.data!!) {
                Log.i(TAG, result.formattedAddress)
                if (!result.formattedAddress.contains("Unnamed")) {
                    address = result.formattedAddress
                    break
                }
            }

            if (!address.contains("USA")) {
                Log.i(TAG, "Geolocation Error: ${it.message}")
                if (optionsFragment.isVisible) {
                    optionsFragment.manageButtons(R.drawable.ic_error)
                    optionsFragment.startTimer(resources.getString(R.string.locationOutsideUS))
                } else {
                    representativesFragment.showErrorMsg(resources.getString(R.string.locationOutsideUS))
                }
            } else {
                masterActivityViewModel.fetchRepresentatives(
                    address,
                    resources.getString(R.string.api_key)
                )
            }
        } else {
            Log.i(TAG, "Geolocation Error: ${it.message}")
            if (optionsFragment.isVisible) {
                optionsFragment.manageButtons(R.drawable.ic_error)
                optionsFragment.startTimer(resources.getString(R.string.invalidLocation))
            } else {
                representativesFragment.showErrorMsg(resources.getString(R.string.invalidLocation))
            }
        }
    }

    private val representativesObserver = Observer<Resource<MutableList<MutableMap<String, String>>>> {
        if (!it.data.isNullOrEmpty() && it.message!!.isEmpty()) {
            for (i in it.data!!) {
                Log.i(TAG, i["name"]!!)
            }

            if (optionsFragment.isVisible) {
                representativesRecyclerViewAdapter = RepresentativesRecyclerViewAdapter(it.data!!)

                optionsFragment.manageButtons(R.drawable.ic_done_white_48dp)

                if (optionsFragment.activeButton != null) {
                    object : CountDownTimer(2000, 1000) {
                        override fun onTick(millisUntilFinished: Long) {
                            if (millisUntilFinished <= 1000L) {

                                if (optionsFragment.activeButton == optionsFragment.binding.currentLocationBtn) {
                                    optionsFragment.binding.currentLocationBtn.revertAnimation {
                                        optionsFragment.binding.currentLocationBtn.background =
                                            resources.getDrawable(
                                                R.drawable.current_location_btn,
                                                null
                                            )
                                    }
                                } else if (optionsFragment.activeButton == optionsFragment.binding.searchLocationBtn) {
                                    optionsFragment.binding.searchLocationBtn.revertAnimation {
                                        optionsFragment.binding.searchLocationBtn.background =
                                            resources.getDrawable(
                                                R.drawable.search_location_btn,
                                                null
                                            )
                                    }
                                } else {

                                }
                            }
                        }

                        override fun onFinish() {
                            switchFragments()
                        }
                    }.start()
                } else {
                    switchFragments()
                }
            } else {
                representativesRecyclerViewAdapter.setData(it.data!!)
                representativesFragment.representativesBinding.addressTv.text = masterActivityViewModel.inputAddress
            }
        } else {
            Log.i(TAG, "Representative Error: ${it.message}")
            if (optionsFragment.isVisible) {
                optionsFragment.manageButtons(R.drawable.ic_error)
                optionsFragment.startTimer(it.message!!)
            } else {
                representativesFragment.showErrorMsg(it.message!!)
            }
        }
    }

    fun switchFragments() {
        representativesFragment = RepresentativesFragment.newInstance(
            representativesRecyclerViewAdapter,
            masterActivityViewModel.inputAddress
        )
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_right_to_left, R.anim.enter_left_to_right, R.anim.exit_left_to_right)
            .replace(fragmentContainer.id, representativesFragment,"representatives")
            .addToBackStack(null)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0F, locationListener
                )
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    data?.let {
                        val place = Autocomplete.getPlaceFromIntent(data)
                        if (optionsFragment.isVisible) {
                            optionsFragment.binding.searchLocationBtn.startAnimation()
                            optionsFragment.setClickable(false)
                        }
                        masterActivityViewModel.fetchRepresentatives(place.address.toString(), resources.getString(R.string.api_key)
                        )
                    }
                }
                AutocompleteActivity.RESULT_ERROR -> {
                    data?.let {
                        val status = Autocomplete.getStatusFromIntent(data)
                        Log.i(TAG, status.statusMessage.toString())
                    }
                }
                Activity.RESULT_CANCELED -> {
                    // The user canceled the operation.
                }
            }
            return
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun getUserLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "onClick")
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0F, locationListener
            )
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), PERMISSION_REQUEST_CODE
            )
        }
    }

    private fun initializePlaceAutocomplete() {
        Places.initialize(applicationContext, resources.getString(R.string.api_key))

        // Create a new PlacesClient instance
        val placesClient = Places.createClient(this)
        // Set the fields to specify which types of place data to
        // return after the user has made a selection.
        val fields = listOf(Place.Field.NAME, Place.Field.ADDRESS)

        // Start the autocomplete intent.
        val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
            .setCountry("US")
            .build(this)
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE)
    }

    // *************************************** Communication With Fragments ***************************************** //
    override fun onCurrentLocationBtnClick() {
        getUserLocation()
    }

    override fun onSearchLocationBtnClick() {
        initializePlaceAutocomplete()
    }

    override fun onRandomizeLocation() {
        val randomCoordinate = masterActivityViewModel.getRandomCoordinate()
        masterActivityViewModel.fetchResults(randomCoordinate.first, randomCoordinate.second, resources.getString(R.string.api_key))
    }

    override fun onCardViewItemClick(position: Int) {
        representativePosition = position
        val representativeInfoDialog = RepresentativeInfoDialog.newInstance(
            masterActivityViewModel.representativesMutableLiveData.value?.data?.get(
                position
            ) as HashMap
        )
        representativeInfoDialog.show(supportFragmentManager, "")
    }

    override fun onIconClick(tag: String) {
        when (tag) {
            resources.getString(R.string.myLocationIconTag) -> {
                getUserLocation()
            }
            resources.getString(R.string.searchIconTag) -> {
                initializePlaceAutocomplete()
            }
            else -> {
                val randomCoordinate = masterActivityViewModel.getRandomCoordinate()
                masterActivityViewModel.fetchResults(randomCoordinate.first, randomCoordinate.second, resources.getString(R.string.api_key))
            }
        }
    }

    override fun onLinkClick(tag: String, link: String) {
        Toast.makeText(this, tag, Toast.LENGTH_LONG).show()
        when (tag) {
            resources.getString(R.string.website) -> {
                val browserIntent = Intent(
                    Intent.ACTION_VIEW, Uri.parse(
                        masterActivityViewModel.representativesMutableLiveData.value?.data?.get(
                            representativePosition
                        )
                            ?.get("website")
                    )
                )
                startActivity(browserIntent)
            }

            resources.getString(R.string.phone) -> {
                val u = Uri.parse("tel:$link")
                val i = Intent(Intent.ACTION_DIAL, u)

                try {
                    startActivity(i)
                } catch (s: SecurityException) {
                    Toast.makeText(this, s.message, Toast.LENGTH_LONG).show()
                }
            }

            resources.getString(R.string.twitter) -> {
                try {
                    val intent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://twitter.com/${link}")
                    )
                    startActivity(intent)
                } catch (e: Exception) {
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://twitter.com/${link}")
                        )
                    )
                }
            }

            resources.getString(R.string.youtube) -> {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = if(link.length==24) {
                    Uri.parse("https://youtube.com/channel/${link}")
                } else {
                    Uri.parse("https://youtube.com/${link}")
                }
                startActivity(intent)
            }
        }
    }
}*/
