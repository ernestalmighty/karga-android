package com.gayyedfam.grainsmartkarga

import android.app.Application
import com.bugfender.sdk.Bugfender
import com.facebook.FacebookSdk
import com.facebook.ads.AdSettings
import com.facebook.ads.AudienceNetworkAds
import com.gayyedfam.grainsmartkarga.BuildConfig.DEBUG
import com.gayyedfam.grainsmartkarga.utils.AudienceNetworkInitializeHelper
import dagger.hilt.android.HiltAndroidApp


/**
 * Created by emgayyed on 17/7/20.
 */
@HiltAndroidApp
class GrainsmartKargaApp: Application() {
    override fun onCreate() {
        super.onCreate()

        Bugfender.init(this, "clSABi1yqEh5akBvuRLwkuPzwJlETbkk", true)
        Bugfender.enableCrashReporting()
        Bugfender.enableUIEventLogging(this)
        Bugfender.enableLogcatLogging()

        FacebookSdk.setApplicationId(this.getString(R.string.facebook_app_id))
        FacebookSdk.sdkInitialize(this)
        FacebookSdk.setAutoLogAppEventsEnabled(true)

        AudienceNetworkInitializeHelper.initialize(this)
    }
}