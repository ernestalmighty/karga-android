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
        FacebookSdk.setApplicationId(BuildConfig.FACEBOOK_APP_ID)
        FacebookSdk.sdkInitialize(this)
        FacebookSdk.setAutoLogAppEventsEnabled(true)

        AudienceNetworkInitializeHelper.initialize(this)
        // Initialize the SDK
        Places.initialize(applicationContext, BuildConfig.PLACES_API_KEY)
        Places.createClient(this)
    }
}