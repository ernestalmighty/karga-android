package com.gayyedfam.grainsmartkarga

import android.app.Application
import com.bugfender.sdk.Bugfender
import com.facebook.FacebookSdk
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

        FacebookSdk.setApplicationId("1694922733995611")
        FacebookSdk.sdkInitialize(this)
    }
}