package com.gayyedfam.grainsmartkarga.utils

import android.content.Context
import android.util.Log
import com.facebook.ads.AdSettings
import com.facebook.ads.AudienceNetworkAds
import com.gayyedfam.grainsmartkarga.BuildConfig.DEBUG

/**
 * Created by emgayyed on 11/8/20.
 */
class AudienceNetworkInitializeHelper : AudienceNetworkAds.InitListener {

    override fun onInitialized(result: AudienceNetworkAds.InitResult) {
        Log.d(AudienceNetworkAds.TAG, result.message)
    }

    companion object {

        internal fun initialize(context: Context) {
            if (!AudienceNetworkAds.isInitialized(context)) {
                if (DEBUG) {
                    AdSettings.turnOnSDKDebugger(context)
                    AdSettings.setTestMode(true)
                }

                AudienceNetworkAds
                    .buildInitSettings(context)
                    .withInitListener(AudienceNetworkInitializeHelper())
                    .initialize()
            }
        }
    }
}