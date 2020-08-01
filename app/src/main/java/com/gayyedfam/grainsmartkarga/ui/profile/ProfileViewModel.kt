package com.gayyedfam.grainsmartkarga.ui.profile

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gayyedfam.grainsmartkarga.data.model.Profile
import com.gayyedfam.grainsmartkarga.domain.usecase.GetProfileUseCase
import com.gayyedfam.grainsmartkarga.domain.usecase.SaveProfileUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

/**
 * Created by emgayyed on 1/8/20.
 */
class ProfileViewModel @ViewModelInject constructor(
    private val saveProfileUseCase: SaveProfileUseCase,
    private val getProfileUseCase: GetProfileUseCase
): ViewModel() {
    var profileViewState = MutableLiveData<ProfileViewState>()
    private val disposable = CompositeDisposable()

    init {
        getProfile()
    }

    private fun getProfile() {
        disposable.add(
        getProfileUseCase()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    profileViewState.value = ProfileViewState.ProfileLoaded(it)
                },
                {
                    it.message?.let { error ->
                        profileViewState.value = ProfileViewState.ProfileLoadError(error)
                    }
                }
            )
        )
    }

    fun save(name: String, contact: String, address: String, deviceId: String) {
        val profile = Profile(
            name = name,
            contact = contact,
            address = address,
            deviceId = deviceId
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
}