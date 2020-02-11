package com.android.weatherapp.main

import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.android.weatherapp.R
import com.android.weatherapp.utils.buildAlertMessageNoGps
import kotlinx.android.synthetic.main.activity_weather_choice.*


/**
 * Created by    :   Syed
 * Created on    :   04,February,2020.
 * Modified By   :
 * Modified On   :   04,February,2020.
 * Copyright (c) Syed @2020. All rights reserved.
 */

class WeatherOptionActivity : AppCompatActivity(), View.OnClickListener {


    private val GPS_ENABLE_REQUEST: Int = 100
    private var handler: Handler? = null
    private var runnable: Runnable? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather_choice)
        initViews()
    }


    override fun onResume() {
        super.onResume()
        checkIfGpsEnabled()
    }


    private fun initViews() {
        handler = Handler()
        runnable = Runnable { enableLocationIfDisable() }
        tv_manual_entry.setOnClickListener(this)
        tv_gps_location.setOnClickListener(this)
    }


    private fun checkIfGpsEnabled() {
        handler?.postDelayed(
                {
                    runnable
                }, (2 * 1000).toLong()
        )
    }

    private fun enableLocationIfDisable() {
        val service = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val enabled = service.isProviderEnabled(LocationManager.GPS_PROVIDER)

        // check if enabled and if not send user to the GSP settings
        // Better solution would be to display a dialog and suggesting to go to Settings
        if (!enabled) {
            buildAlertMessageNoGps(this@WeatherOptionActivity, GPS_ENABLE_REQUEST)
        } else {
            startActivity(Intent(this@WeatherOptionActivity, WeatherByCityActivity::class.java))
            finish()
        }
    }


    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.tv_manual_entry -> {
                val fullScreenIntent = Intent(this, WeatherByCityActivity::class.java)
                startActivity(fullScreenIntent)
            }
            R.id.tv_gps_location -> {
                val fullScreenIntent = Intent(this, WeatherByGpsActivity::class.java)
                startActivity(fullScreenIntent)
            }
        }
    }


    override fun onStop() {
        super.onStop()
        handler?.removeCallbacks(runnable)
        handler = null
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