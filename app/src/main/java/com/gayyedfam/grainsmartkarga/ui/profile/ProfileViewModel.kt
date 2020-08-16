package com.gayyedfam.grainsmartkarga.ui.profile

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gayyedfam.grainsmartkarga.data.model.Profile
import com.gayyedfam.grainsmartkarga.domain.usecase.GetDeviceLocationUseCase
import com.gayyedfam.grainsmartkarga.domain.usecase.GetProfileUseCase
import com.gayyedfam.grainsmartkarga.domain.usecase.SaveProfileUseCase
import com.google.android.gms.maps.model.LatLng
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

/**
 * Created by emgayyed on 1/8/20.
 */
class ProfileViewModel @ViewModelInject constructor(
    private val saveProfileUseCase: SaveProfileUseCase,
    private val getProfileUseCase: GetProfileUseCase,
    private val getDeviceLocationUseCase: GetDeviceLocationUseCase
): ViewModel() {
    var profileViewState = MutableLiveData<ProfileViewState>()
    private val disposable = CompositeDisposable()
    private lateinit var userLatLng: LatLng

    init {
        getProfile()
    }

    private fun loadDeviceLocation() {
        disposable.add(
            getDeviceLocationUseCase()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        profileViewState.value = ProfileViewState.DeviceAddressLoaded(it)
                    },
                    {

                    }
                )
        )
    }

    private fun getProfile() {
        disposable.add(
        getProfileUseCase()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    this.userLatLng = LatLng(it.addressLat, it.addressLong)
                    profileViewState.value = ProfileViewState.ProfileLoaded(it)
                },
                {
                    loadDeviceLocation()
                }
            )
        )
    }

    fun save(name: String, contact: String, address1: String, address2: String, deviceId: String, instruction: String) {
        if(!this::userLatLng.isInitialized) {
            return
        }

        val profile = Profile(
            name = name,
            contact = contact,
            address1 = address1,
            address2 = address2,
            addressLat = userLatLng.latitude,
            addressLong = userLatLng.longitude,
            deviceId = deviceId,
            deliveryInstruction = instruction
        )

        disposable.add(
        saveProfileUseCase(profile)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                profileViewState.value = ProfileViewState.ProfileSaved
            }
            .subscribe()
        )
    }

    fun locationSelected(latLang: LatLng?) {
        latLang?.let {
            userLatLng = it
        }
    }
}