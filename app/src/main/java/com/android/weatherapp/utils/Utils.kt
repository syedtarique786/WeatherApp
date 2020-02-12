package com.android.weatherapp.utils

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.os.Build
import android.provider.Settings
import android.text.TextUtils
import android.view.Gravity
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.android.weatherapp.R
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import java.io.IOException
import androidx.databinding.adapters.TextViewBindingAdapter.OnTextChanged
import android.widget.TextView
import androidx.databinding.BindingAdapter


/**
 * Created by    :   Syed
 * Created on    :   02,February,2020.
 * Modified By   :
 * Modified On   :   02,February,2020.
 * Copyright (c) Syed @2020. All rights reserved.
 */

fun buildAlertMessageNoGps(activity: Activity, requestCode: Int) {

    val builder = AlertDialog.Builder(activity)
    builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
            .setCancelable(false)
            .setPositiveButton("Yes") { dialog, id ->
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                activity.startActivityForResult(intent, requestCode)
            }
            .setNegativeButton("No") { dialog, id ->
                dialog.cancel()
                activity.finish()
            }
    val alert: AlertDialog = builder.create()
    alert.show()

}


fun checkPermissions(activity: Activity): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            return true
        }
        return false
    } else {
        true
    }
}

fun requestPermissions(activity: Activity, PERMISSION_ID: Int) {
    ActivityCompat.requestPermissions(
            activity,
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION),
            PERMISSION_ID
    )
}


fun checkPlayServices(activity: Activity, playServicesResolutionRequest: Int): Boolean {
    val apiAvailability = GoogleApiAvailability.getInstance()
    val resultCode = apiAvailability.isGooglePlayServicesAvailable(activity)

    if (resultCode != ConnectionResult.SUCCESS) {
        if (apiAvailability.isUserResolvableError(resultCode)) {
            apiAvailability.getErrorDialog(activity, resultCode, playServicesResolutionRequest)
        } else {
            activity.finish()
        }

        return false
    }

    return true
}


fun alertMessageOkayOnly(activity: Activity, message: String) {

    val builder = AlertDialog.Builder(activity)
    builder.setTitle(activity.getString(R.string.app_name))
            .setMessage(message)
            .setCancelable(false)
            .setPositiveButton(activity.getString(R.string.label_okay)) { dialog, id ->
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                activity.startActivity(intent)
            }

    val alert: AlertDialog = builder.create()
    alert.show()

}


fun showToast(context: Context, msg: String) {
    val toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT)
    toast.setGravity(Gravity.CENTER, 0, 500)
    toast.show()
}

fun showToastL(context: Context, msg: String) {
    val toast = Toast.makeText(context, msg, Toast.LENGTH_LONG)
    toast.setGravity(Gravity.CENTER, 0, 500)
    toast.show()
}

fun isValidCityToSearch(city: String) : Boolean{
    return !TextUtils.isEmpty(city) && city.length>=3
}

fun isInternetOn(application: Application): Boolean {
    val cm = application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork = cm.activeNetworkInfo
    return activeNetwork != null && activeNetwork.isConnectedOrConnecting
}


suspend fun <T : Any> safeApiCall(call: suspend () -> Result<T>, errorMessage: String): Result<T> {
    return try {
        call()
    } catch (e: Exception) {
        // An exception was thrown when calling the API so we're converting this to an IOException
        Result.Error(IOException(errorMessage, e))
    }
}