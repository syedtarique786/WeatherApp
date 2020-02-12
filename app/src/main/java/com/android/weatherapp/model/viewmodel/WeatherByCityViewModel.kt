package com.android.weatherapp.model.viewmodel

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.TextView.OnEditorActionListener
import androidx.databinding.BindingAdapter
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.weatherapp.BuildConfig
import com.android.weatherapp.model.ByCityModel
import com.android.weatherapp.network.APIService
import com.android.weatherapp.network.RetrofitService
import com.android.weatherapp.repo.WeatherRepository
import com.android.weatherapp.utils.Event
import com.android.weatherapp.utils.Result
import com.android.weatherapp.utils.isValidCityToSearch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext


/**
 * Created by    :   Syed
 * Created on    :   03,February,2020.
 * Modified By   :
 * Modified On   :   03,February,2020.
 * Copyright (c) 2020 @Syed. All rights reserved.
 */

class WeatherByCityViewModel : ViewModel() {

    private val computationContext: CoroutineContext by lazy { Dispatchers.Default }
    private val foregroundContext: CoroutineContext by lazy { Dispatchers.Main }  /// WHY lazy
    val isLoading = ObservableField<Boolean>()

    val searchCities: Function1<String, Unit> = this::searchCities

    companion object {

        @BindingAdapter("onEditorActionListener")
        fun bindOnEditorActionListener(editText: EditText, editorActionListener: OnEditorActionListener) {
            editText.setOnEditorActionListener(editorActionListener)
        }
    }

    private val _mutableLiveData = MutableLiveData<ByCityModel>()
    val mutableLiveData: LiveData<ByCityModel>
        get() = _mutableLiveData

    private var newsRepository: WeatherRepository? = null

    private val _uiState = MutableLiveData<UIModel>()
    val uiState: LiveData<UIModel>
        get() = _uiState


    private val _showError = MutableLiveData<String>()
    val showError: LiveData<String>
        get() = _showError

    private val _searchEnable = MutableLiveData<Boolean>()
    val searchEnable: LiveData<Boolean>
        get() = _searchEnable

    fun searchCities(cities: String) {
        //Log.e("SearchCities", " $cities")
        val searchKey: List<String> = cities.trim().split(",")
        if (isCitiesCountAreValid(searchKey)) {
            _searchEnable.value = true
            for (city in searchKey) {
                isValidCityToSearch(city).let {
                    if (it) {
                        fetchCitiesWeather(city)
                    } else {
                        _showError.value = "Not valid city/cities name to search for, please try with different name"
                    }
                }
            }
        }

    }

    /**
     * Check if entered cities name are in desired range
     * @param searchKey Cities name to check for the City count
     * @return true if its in range of 3-7 otherwise false*/
     fun isCitiesCountAreValid(searchKey: List<String>): Boolean {
        if (searchKey.size < 3) {               // Min limit
            _showError.value = "Please enter at lest 3 cities name to find weather."
            return false
        } else if (searchKey.size > 7) {        //Max Limit
            _showError.value = "You can't search more than 7 cities at once."
            return false
        }
        return true
    }


    var watcher: TextWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            //Log.e("afterTextChanged", s.toString())
            _searchEnable.value = false
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            //Log.e("beforeTextChanged", s.toString())
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            //Log.e("onTextChanged", s.toString())
        }
    }


    private fun fetchCitiesWeather(city: String) {
        //start loader
        isLoading.set(true)

        val weatherAPI = RetrofitService.createService(APIService::class.java)
        newsRepository = WeatherRepository(weatherAPI)

        viewModelScope.launch(computationContext) {

            val result = newsRepository!!.getCityWiseWeather(city, BuildConfig.APP_KEY)

            withContext(foregroundContext) {
                if (result is Result.Success && result.data.getStatus() == 200) {
                    _mutableLiveData.value = result.data
                    emitUiState(showSuccess = Event(_mutableLiveData.value))
                } else {
                    showError()
                    emitUiState(showError = Event(true))
                    _showError.value = (result as Result.Error).exception.message
                }
                isLoading.set(false)
            }
        }
    }


    private fun showError() {
        emitUiState(showError = Event(true))
    }


    fun getWeatherRepository(): LiveData<ByCityModel>? {
        return mutableLiveData
    }


    private fun showLoading() {
        emitUiState(showProgress = true)
    }


    private fun emitUiState(
            showProgress: Boolean = false,
            showError: Event<Boolean>? = null,
            showSuccess: Event<ByCityModel?>? = null) {
        val uiModel = UIModel(showProgress, showError, showSuccess)
        _uiState.value = uiModel
    }


}

data class UIModel(
        val showProgress: Boolean,
        val showError: Event<Boolean>?,
        val showSuccess: Event<ByCityModel?>?
)

interface CustomOnEditorActionListener {
    fun onEditorAction(data: String)
}