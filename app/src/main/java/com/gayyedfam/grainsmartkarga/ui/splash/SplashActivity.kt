package com.gayyedfam.grainsmartkarga.ui.splash

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.gayyedfam.grainsmartkarga.R
import com.gayyedfam.grainsmartkarga.ui.main.MainActivity
import com.gayyedfam.grainsmartkarga.ui.main.MainViewModel
import com.google.android.gms.location.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    private val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1000
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var hasExpired = false

    private val splashViewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        setupLocationClient()

        splashViewModel.splashStateLiveData.observe(this, Observer {
            when(it) {
                is SplashState.LocationSaved, SplashState.LocationDenied -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish();
                }
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray) {
        when (requestCode) {
            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {
                if (grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    setupLocationClient()
                } else {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish();
                }
            }
        }
    }

    private fun setupLocationClient() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION)
            }
        } else {
            fusedLocationClient.lastLocation.addOnSuccessListener { lastLocation ->
                val locationRequest = LocationRequest.create()
                locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

                val locationCallback = object : LocationCallback() {
                    override fun onLocationResult(locationResult: LocationResult?) {
                        locationResult?.let {result ->
                            fusedLocationClient.removeLocationUpdates(this)
                            result.locations.forEach {newLocation ->
                                if(!hasExpired) {
                                    hasExpired = true
                                    splashViewModel.storeUserLocation(newLocation.latitude, newLocation.longitude)
                                }
                            }
                        } ?: {
                            if(!hasExpired) {
                                hasExpired = true
                                splashViewModel.storeUserLocation(lastLocation.latitude, lastLocation.longitude)
                            }
                        }()
                    }
                }

                val looper = Looper.getMainLooper()
                Handler(looper)
                    .postDelayed(Runnable {
                        if(!hasExpired) {
                            hasExpired = true
                            startActivity(Intent(this, MainActivity::class.java))
                            finish();
                        }
                    }, 5000)

                fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, looper)
            }
        }
    }
}