package com.gayyedfam.grainsmartkarga

import android.app.Application
import com.facebook.FacebookSdk
import com.gayyedfam.grainsmartkarga.utils.AudienceNetworkInitializeHelper
import com.google.android.libraries.places.api.Places
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import dagger.hilt.android.HiltAndroidApp


/**
 * Created by emgayyed on 17/7/20.
 */
@HiltAndroidApp
class GrainsmartKargaApp: Application() {
    override fun onCreate() {
        super.onCreate()

        FacebookSdk.setApplicationId(this.getString(R.string.facebook_app_id))
        FacebookSdk.sdkInitialize(this)
        FacebookSdk.setAutoLogAppEventsEnabled(true)

        AudienceNetworkInitializeHelper.initialize(this)
        // Initialize the SDK
        Places.initialize(applicationContext, "AIzaSyBVCx7vWCTkLAGHf3w4nsOZF78VZyuMVhw")
        Places.createClient(this)
    }
}