package com.gayyedfam.grainsmartkarga.utils

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*

/**
 * Created by emgayyed on 11/8/20.
 */
class LocationUtil(val activity: AppCompatActivity) {

    private val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1000
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    fun init() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)
        setupLocationClient()
    }

    private fun setupLocationClient() {
        if (ContextCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                activity.requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION)
            }
        } else {
            fusedLocationClient.lastLocation.addOnSuccessListener { lastLocation ->
                val locationRequest = LocationRequest.create()
                locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

                val locationCallback = object : LocationCallback() {
                    override fun onLocationResult(locationResult: LocationResult) {
                        fusedLocationClient.removeLocationUpdates(this)
                        locationResult.locations.forEach { newLocation ->

                        }
                    }
                }

                fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
            }
        }
    }
}