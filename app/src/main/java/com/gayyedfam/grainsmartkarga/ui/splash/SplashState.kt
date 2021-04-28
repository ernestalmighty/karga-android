package com.gayyedfam.grainsmartkarga.ui.splash

/**
 * Created by emgayyed on 3/8/20.
 */
sealed class SplashState {
    object Nothing: SplashState()
    object LocationSaved : SplashState()
    object LocationDenied : SplashState()
}