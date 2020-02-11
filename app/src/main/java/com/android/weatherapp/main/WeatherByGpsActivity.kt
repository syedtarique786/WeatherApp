package com.android.weatherapp.main


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.weatherapp.R
import com.android.weatherapp.databinding.ActivityWeatherByLocationBinding
import com.android.weatherapp.model.ByCityModel
import com.android.weatherapp.model.ByGpsModel
import com.android.weatherapp.model.ForeCastItem
import com.android.weatherapp.model.viewmodel.UIModelGps
import com.android.weatherapp.model.viewmodel.WeatherByGpsViewModel
import com.android.weatherapp.ui.WeatherForecastAdapter
import com.android.weatherapp.utils.*
import com.android.weatherapp.view.RecyclerViewClickListener
import com.google.android.gms.location.*
import kotlinx.android.synthetic.main.activity_weather_city.loader
import kotlinx.android.synthetic.main.activity_weather_by_location.*


class WeatherByGpsActivity : AppCompatActivity(), RecyclerViewClickListener {

    private lateinit var binding: ActivityWeatherByLocationBinding
    private var weatherResponse: ByGpsModel? = null
    private val PERMISSION_ID = 100
    var latitude: Double = 0.0
    var longitude: Double = 0.0
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private val PLAY_SERVICES_RESOLUTION_REQUEST = 9000
    private lateinit var mViewModelFactory: ViewModelProvider.Factory

    private val weatherViewModel: WeatherByGpsViewModel by lazy {
        ViewModelProviders.of(this, mViewModelFactory).get(WeatherByGpsViewModel::class.java)
    }


    override fun onStart() {
        super.onStart()
        if (mFusedLocationClient == null) {
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        }
        subscribeUi()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_weather_by_location)

        mViewModelFactory = defaultViewModelProviderFactory
        //weatherViewModel = ViewModelProviders.of(this, mViewModelFactory).get(WeatherByGpsViewModel::class.java)
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        binding.viewModel = weatherViewModel
        binding.executePendingBindings()
    }


    private fun subscribeUi() {
        weatherViewModel.uiState.observe(this, Observer { it ->
            val uiModel = it ?: return@Observer

            if (uiModel.showProgress) {
                showLoader()
            } else {
                hideLoader()
            }

            if (uiModel.showError != null && !uiModel.showError.consumed) {
                uiModel.showError.consume()?.let { showErrorLayout() }
            } else {
                hideErrorLayout()
            }

            if (uiModel.showSuccess != null && !uiModel.showSuccess.consumed) {
                uiModel.showSuccess.consume()?.let {
                    fillUiWithData(it)
                }
            }
        })

        weatherViewModel.weatherByGps.observe(this, Observer<ByGpsModel?> { gpsModel ->
            weatherResponse = gpsModel
            fillUiWithData(weatherResponse!!)
            rvForecast.also {
                it.layoutManager = LinearLayoutManager(applicationContext)
                it.setHasFixedSize(true)
                it.adapter = WeatherForecastAdapter(weatherResponse!!.getForeCastList()!!, this)
                binding.viewModel = weatherViewModel
            }
        })

        weatherViewModel.reFetchingState.nonNullObserve(this) {
            reFetchWeatherByGps()
        }

        weatherViewModel.showError.observe(this, Observer { errMsg -> showToast(this, errMsg) })
        //observeNetwork()
    }


    private fun fillUiWithData(weatherResponse: ByGpsModel) {
        if (weatherResponse.getStatus() == 200) {
            showDataOnUi()
        } else {  // Error
            showLoaderOnUi()
        }
    }


    override fun onResume() {
        super.onResume()
        if (!checkPlayServices(this@WeatherByGpsActivity, PLAY_SERVICES_RESOLUTION_REQUEST)) {
            alertMessageOkayOnly(this@WeatherByGpsActivity, getString(R.string.message_missing_google_play))
        }
        if (mFusedLocationClient != null) {
            getLastLocation()
        }
    }


    override fun onPause() {
        super.onPause()
        if (mFusedLocationClient != null) {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback)
        }
    }


    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        var mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 0
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mFusedLocationClient.requestLocationUpdates(
                mLocationRequest, mLocationCallback,
                Looper.getMainLooper()
        )
    }


    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            var mLastLocation: Location = locationResult.lastLocation
            latitude = mLastLocation.latitude
            longitude = mLastLocation.longitude
        }
    }


    private fun isLocationEnabled(): Boolean {
        var locationManager: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == PERMISSION_ID) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLastLocation()
            }
        }
    }


    private fun getLastLocation() {
        if (checkPermissions(this@WeatherByGpsActivity)) {
            if (isLocationEnabled()) {

                mFusedLocationClient.lastLocation.addOnCompleteListener(this) { task ->
                    val location: Location? = task.result
                    if (location == null) {
                        requestNewLocationData()
                    } else {
                        latitude = location.latitude
                        longitude = location.longitude
                        showToast(this, "Your location " + latitude + " " + longitude)

                        if(isInternetOn(application)) {
                            if (latitude != 0.0 && longitude != 0.0) {
                                weatherViewModel.fetchWeatherByGps(latitude, longitude)
                            }
                        } else {
                            showNetErrorLayout()
                        }
                    }
                }
            } else {
                showToast(this, getString(R.string.message_enable_location))
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions(this@WeatherByGpsActivity, PERMISSION_ID)
        }
    }


    private fun reFetchWeatherByGps() {
        showLoaderOnUi()
        weatherViewModel.fetchWeatherByGps(latitude, longitude)
    }


    private fun showLoaderOnUi() {
        showLoader()
        rootContainer.visibility = View.GONE
    }


    private fun showDataOnUi() {
        hideLoader()
        hideNetErrorLayout()
        rootContainer.visibility = View.VISIBLE
    }


    private fun hideLoader() {
        loader.visibility = View.GONE
    }


    private fun showLoader() {
        loader.visibility = View.VISIBLE
    }


    private fun showNetErrorLayout() {
        loading_error_layout.visibility = View.VISIBLE
    }


    private fun hideNetErrorLayout() {
        loading_error_layout.visibility = View.GONE
    }

    private fun showErrorLayout() {
        errorText.visibility = View.VISIBLE

    }

    private fun hideErrorLayout() {
        errorText.visibility = View.GONE
    }


    override fun onCityWaetherItemClick(view: View, foreCastItem: ByCityModel) {

    }

    override fun onForcastWaetherItemClick(view: View, foreCastItem: ForeCastItem) {
        showToast(this, "Click on city weather item ")
    }
}
