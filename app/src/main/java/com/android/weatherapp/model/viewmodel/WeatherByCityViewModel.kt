package com.android.weatherapp.model.viewmodel

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

    private val _reFetchingState = MutableLiveData<Unit>()
    val reFetchingState: LiveData<Unit>
        get() = _reFetchingState

    private val _showError = MutableLiveData<String>()
    val showError: LiveData<String>
        get() = _showError


    private fun searchCities(cities: String) {
        //Log.e("SearchCities", " $cities")
        val searchKey: List<String> = cities.trim().split(",")
        if (searchKey.size < 3) {
            _showError.value = "Please enter at lest 3 cities name to find weather."
            return
        }
        _mutableLiveData.value = null
        for (city in searchKey) {
            isValidCityToSearch(city).let {
                if (it) {
                    fetchCitiesWeather(city)
                } else {
                    _showError.value = "Not a valid city name to search for, please try with different name"
                }
            }
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