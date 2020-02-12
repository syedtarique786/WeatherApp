package com.android.weatherapp.model.viewmodel

import android.app.Activity
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.android.weatherapp.BuildConfig
import com.android.weatherapp.main.WeatherByCityActivity
import com.android.weatherapp.main.WeatherByGpsActivity
import com.android.weatherapp.network.RetrofitService
import com.android.weatherapp.repo.WeatherRepository
import org.junit.Rule
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock



/**
 * Created by    :   Syed
 * Created on    :   12,February,2020.
 * Modified By   :
 * Modified On   :   12,February,2020.
 * Copyright (c) 2020 @Syed. All rights reserved.
 */



/*@RunWith(PowerMockRunner::class)
@PowerMockIgnore("com.android.weatherapp.*", "org.mockito.*", "android.*")
@PrepareForTest(WeatherByCityViewModel::class, WeatherByGpsActivity::class, WeatherByCityActivity::class, TextUtils::class)*/
public class WeatherByCityViewModelTest  {


    private var viewModel: WeatherByCityViewModel? = null
    private var activity: Activity? = null
    val cities = "Delhi, Kolkata, Mumbai"
    var searchKey : List<String>? = null
    @Mock
    private val repository: WeatherRepository? = null

    @InjectMocks
    private val itemService: RetrofitService? = null

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Before
    @Throws(Exception::class)
    fun setup() {
        //MockitoAnnotations.initMocks(this)
        activity = mock(Activity::class.java)
        viewModel = WeatherByCityViewModel()
        searchKey = cities.trim().split(",")
    }
    

    @Test
    fun isLoading() {
    }

    @Test
    fun getSearchCities() {
        isCitiesCountAreValid()

    }

    @Test
    fun getMutableLiveData() {
    }

    @Test
    fun getUiState() {
    }

    @Test
    fun getReFetchingState() {
    }

    @Test
    fun getShowError() {
    }

    @Test
    fun getSearchEnable() {
    }

    @Test
    fun getWatcher() {
    }

    @Test
    fun setWatcher() {
    }

    @Test
    fun getWeatherRepository() {
    }

    @Test
    fun isCitiesCountAreValid() {
        if (searchKey!!.size < 3) {               // Min limit
            assert(false)
        } else if (searchKey!!.size > 7) {        //Max Limit
            assert(false)
        }
        assert(true)
    }
}