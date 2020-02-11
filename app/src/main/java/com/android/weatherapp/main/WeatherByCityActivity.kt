package com.android.weatherapp.main


import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView.OnEditorActionListener
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.weatherapp.R
import com.android.weatherapp.databinding.ActivityWeatherCityBinding
import com.android.weatherapp.model.ByCityModel
import com.android.weatherapp.model.ForeCastItem
import com.android.weatherapp.model.viewmodel.WeatherByCityViewModel
import com.android.weatherapp.ui.CitiesWeatherAdapter
import com.android.weatherapp.utils.nonNullObserve
import com.android.weatherapp.utils.showToast
import com.android.weatherapp.view.RecyclerViewClickListener
import kotlinx.android.synthetic.main.activity_weather_city.*


class WeatherByCityActivity : AppCompatActivity(), RecyclerViewClickListener {


    private var citiesWeatherAdapter = CitiesWeatherAdapter(this)
    private lateinit var layoutManager: LinearLayoutManager
    private var weatherResponse: ByCityModel? = null
    private lateinit var binding: ActivityWeatherCityBinding
    private lateinit var mViewModelFactory: ViewModelProvider.Factory
    private val weatherViewModel: WeatherByCityViewModel by lazy {
        ViewModelProviders.of(this, mViewModelFactory).get(WeatherByCityViewModel::class.java)
    }


    override fun onStart() {
        super.onStart()
        subscribeUi()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_weather_city)
        //setContentView(R.layout.activity_weather_city)
        initViews()
        mViewModelFactory = defaultViewModelProviderFactory
        binding.viewModel = weatherViewModel

        /*et_city_search.setOnEditorActionListener(OnEditorActionListener { view, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val searchKey: List<String> = view.text.toString().split(",")
                for (city in searchKey) {
                    fetchWeatherData(city.trim())
                }
            }
            false
        })*/
    }


    private fun subscribeUi() {
        weatherViewModel.uiState.observe(this, Observer { it ->
            val uiModel = it ?: return@Observer

            if (uiModel.showProgress) {
                toggleLoaderVisibility(true)
            } else {
                toggleLoaderVisibility(false)
            }

            if (uiModel.showError != null && !uiModel.showError.consumed) {
                uiModel.showError.consume()?.let {
                    val msg : String = weatherViewModel.showError?.value.toString()
                    showErrorLayout(msg)
                }
            } else {
                hideErrorLayout()
            }

            if (uiModel.showSuccess != null && !uiModel.showSuccess.consumed) {
                uiModel.showSuccess.consume()?.let {
                    //fillUiWithData(it)
                    showDataOnUi()
                } ?: run {
                    val msg : String = weatherViewModel.showError?.value.toString()
                    showErrorLayout(msg)
                }
            }
        })

        weatherViewModel.mutableLiveData.observe(this, Observer<ByCityModel?> {
            weatherResponse = it
            weatherResponse?.let {
                fillUiWithData(weatherResponse!!)
            }
        })


        weatherViewModel.showError.observe(this, Observer { errMsg ->
            showErrorLayout(errMsg)
        })
    }


    private fun initViews() {
        //Setup RecyclerView
        layoutManager = LinearLayoutManager(applicationContext)
        binding.rvCityWeather.layoutManager = LinearLayoutManager(applicationContext)
        binding.rvCityWeather.setHasFixedSize(true)
        citiesWeatherAdapter = CitiesWeatherAdapter(this)
        binding.rvCityWeather.adapter = citiesWeatherAdapter
    }


    private fun fillUiWithData(@NonNull weatherResponse: ByCityModel) {
        if (weatherResponse.getStatus() == 200) {
            citiesWeatherAdapter.addItem(weatherViewModel.getWeatherRepository()?.value!!)
            binding.viewModel = weatherViewModel
            binding.rvCityWeather.adapter = citiesWeatherAdapter
            citiesWeatherAdapter.notifyDataSetChanged()

            showDataOnUi()
        } else {  // Error
            showErrorLayout(weatherResponse.getMessage())
        }
    }




    private fun fetchWeatherData(key: String) {
        showLoaderOnUi()
        //weatherViewModel.fetchCitiesWeather(key)
    }


    /**
     * Show loader on UI & hide any past dat from the screen
     * */
    private fun showLoaderOnUi() {
        toggleLoaderVisibility(true)
        toggleContentVisibilityOnUi(false)
    }


    /**
     * Toggle content visibility on the screen
     * @param visible set Visibility to View.VISIBLE when #visible is true, otherwise View.GONE
     * */
    private fun toggleContentVisibilityOnUi(visible: Boolean) {
        binding.rvCityWeather.visibility = if (visible) View.VISIBLE else View.GONE
    }


    /**
     * Show Data on UI & hide any loader or error from the UI if any
     * */
    private fun showDataOnUi() {
        toggleLoaderVisibility(false)
        toggleContentVisibilityOnUi(true)
    }


    private fun toggleLoaderVisibility(visible: Boolean) {
        loader.visibility = if (visible) View.VISIBLE else View.GONE
    }


    private fun showErrorLayout(byCityModel: String?) {
        //toggleContentVisibilityOnUi(false)
        toggleLoaderVisibility(false)
        tvNoCityError.visibility = View.VISIBLE
        byCityModel?.let {
            tvNoCityError.text = byCityModel
        } ?: run {
            tvNoCityError.text = getString(R.string.something_went_wrong)
        }
        showToast(this@WeatherByCityActivity, tvNoCityError.text.toString())
    }

    private fun hideErrorLayout() {
        tvNoCityError.visibility = View.GONE
    }


    override fun onCityWaetherItemClick(view: View, foreCastItem: ByCityModel) {
        showToast(this, "Click on city weather item ")
    }

    override fun onForcastWaetherItemClick(view: View, foreCastItem: ForeCastItem) {

    }
}
