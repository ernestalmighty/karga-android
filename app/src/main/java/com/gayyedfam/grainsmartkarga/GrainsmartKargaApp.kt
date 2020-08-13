package com.gayyedfam.grainsmartkarga

import android.app.Application
import com.bugfender.sdk.Bugfender
import com.facebook.FacebookSdk
import com.facebook.ads.AdSettings
import com.facebook.ads.AudienceNetworkAds
import com.gayyedfam.grainsmartkarga.BuildConfig.DEBUG
import com.gayyedfam.grainsmartkarga.utils.AudienceNetworkInitializeHelper
import com.google.android.libraries.places.api.Places
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
        Places.initialize(applicationContext, this.getString(R.string.google_api_key))
        Places.createClient(this)
    }
}