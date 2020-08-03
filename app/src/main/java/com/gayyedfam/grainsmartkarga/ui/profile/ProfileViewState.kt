package com.gayyedfam.grainsmartkarga.ui.profile

import com.gayyedfam.grainsmartkarga.data.model.DeviceLocation
import com.gayyedfam.grainsmartkarga.data.model.Profile

/**
 * Created by emgayyed on 1/8/20.
 */
sealed class ProfileViewState {
    data class DeviceAddressLoaded(val deviceLocation: DeviceLocation): ProfileViewState()
    data class ProfileLoaded(val profile: Profile): ProfileViewState()
    data class ProfileLoadError(val message: String): ProfileViewState()
    object ProfileSaved: ProfileViewState()
}