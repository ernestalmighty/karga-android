package com.gayyedfam.grainsmartkarga.ui.splash

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gayyedfam.grainsmartkarga.domain.usecase.SetupDataUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

/**
 * Created by emgayyed on 3/8/20.
 */
class SplashViewModel @ViewModelInject constructor(val setupDataUseCase: SetupDataUseCase): ViewModel() {

    var splashStateLiveData = MutableLiveData<SplashState>()
    private val disposable = CompositeDisposable()

    fun storeUserLocation(lat: Double, lon: Double) {
        disposable.add(
        setupDataUseCase(lat, lon)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally {
                splashStateLiveData.value = SplashState.Nothing
            }
            .subscribe(
                {
                    splashStateLiveData.value = SplashState.LocationSaved
                },
                {
                    splashStateLiveData.value = SplashState.LocationDenied
                }
            ))
    }
}