package com.android.weatherapp.model.viewmodel

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.weatherapp.BuildConfig
import com.android.weatherapp.model.ByGpsModel
import com.android.weatherapp.network.APIService
import com.android.weatherapp.network.RetrofitService
import com.android.weatherapp.repo.WeatherRepository
import com.android.weatherapp.utils.Event
import com.android.weatherapp.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
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

class WeatherByGpsViewModel : ViewModel() {

    private val computationContext: CoroutineContext by lazy { Dispatchers.Default }
    private val foregroundContext: CoroutineContext by lazy { Dispatchers.Main }  /// WHY lazy

    private lateinit var job: Job
    val isLoading = ObservableField<Boolean>()

    private var mutableLiveData: MutableLiveData<ByGpsModel>? = null
    private var newsRepository: WeatherRepository? = null

    private val _uiState = MutableLiveData<UIModelGps>()
    val uiState: LiveData<UIModelGps>
        get() = _uiState

    private val _reFetchingState = MutableLiveData<Unit>()
    val reFetchingState: LiveData<Unit>
        get() = _reFetchingState


    private val _weatherByGpsModel = MutableLiveData<ByGpsModel>()
    val weatherByGps: LiveData<ByGpsModel>
        get() = _weatherByGpsModel


    private val _showError = MutableLiveData<String>()
    val showError: LiveData<String>
        get() = _showError

    init {
        val weatherAPI = RetrofitService.createService(APIService::class.java)
        newsRepository = WeatherRepository(weatherAPI)
    }


    /*fun fetchWeatherByGps(latitude: Double, longitude: Double) {
        if (mutableLiveData != null) {
            return
        }
        isLoading.set(true)

        val weatherAPI = RetrofitService.createService(APIService::class.java)
        newsRepository = WeatherRepository(weatherAPI)

        job = Coroutines.ioThenMain(
                { newsRepository!!.getWeatherByGps(latitude.toString(), longitude.toString(), BuildConfig.APP_KEY) },
                { it ->
                    isLoading.set(false)
                    _weatherByGpsModel.value = it
                    if (_weatherByGpsModel.value?.getStatus() == 200) {
                        //_weatherByGpsModel.value = mutableLiveData!!.value
                        emitUiState(
                                showSuccess = Event(_weatherByGpsModel.value)
                        )
                    } else {
                        emitUiState(
                                showError = Event(true)
                        )
                    }
                }
        )
    }*/



    fun fetchWeatherByGps(latitude: Double, longitude: Double) {
        if (mutableLiveData != null) {
            return
        }
        isLoading.set(true)
        viewModelScope.launch(computationContext) {
            val result = newsRepository!!.getGpsWeatherData(latitude.toString(), longitude.toString(), BuildConfig.APP_KEY)
            withContext(foregroundContext) {
                if (result is Result.Success && result.data.getStatus() == 200) {
                    _weatherByGpsModel.value = result.data
                    emitUiState(showSuccess = Event(_weatherByGpsModel.value))
                } else {
                    showError()
                    emitUiState(showError = Event(true))
                    _showError.value = (result as Result.Error).exception.message
                }
                isLoading.set(false)
            }
        }

    }




    fun getWeatherRepository(): LiveData<ByGpsModel>? {
        return mutableLiveData
    }


    private fun showLoading() {
        emitUiState(showProgress = true)
    }

    private fun showError() {
        emitUiState(showError = Event(true))
    }


    private fun emitUiState(
            showProgress: Boolean = false,
            showError: Event<Boolean>? = null,
            showSuccess: Event<ByGpsModel?>? = null) {
        val uiModel = UIModelGps(showProgress, showError, showSuccess)
        _uiState.value = uiModel
    }


    override fun onCleared() {
        super.onCleared()
        if (::job.isInitialized) job.cancel()
    }

}

data class UIModelGps(
        val showProgress: Boolean,
        val showError: Event<Boolean>?,
        val showSuccess: Event<ByGpsModel?>?
)

