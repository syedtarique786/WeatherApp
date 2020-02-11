package com.android.weatherapp.main

import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.android.weatherapp.R
import com.android.weatherapp.utils.buildAlertMessageNoGps


/**
 * Created by    :   Syed
 * Created on    :   02,February,2020.
 * Modified By   :
 * Modified On   :   02,February,2020.
 * Copyright (c) Syed @2020. All rights reserved.
 */

class SplashActivity : AppCompatActivity() {


    private val GPS_ENABLE_REQUEST: Int = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
    }


    override fun onResume() {
        super.onResume()
        checkIfGpsEnabled()
    }

    private fun checkIfGpsEnabled() {
        Handler().postDelayed(
                {
                    enableLocationIfDisable()
                }, (2 * 1000).toLong()
        )
    }

    private fun enableLocationIfDisable() {
        val service = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val enabled = service.isProviderEnabled(LocationManager.GPS_PROVIDER)

        // check if GPS is enabled or not. If not send user to the GSP settings
        if (!enabled) {
            buildAlertMessageNoGps(this@SplashActivity, GPS_ENABLE_REQUEST)
        } else {
            startActivity(Intent(this@SplashActivity, WeatherOptionActivity::class.java))
            finish()
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            GPS_ENABLE_REQUEST -> {
                enableLocationIfDisable()
            }
        }

    }
}