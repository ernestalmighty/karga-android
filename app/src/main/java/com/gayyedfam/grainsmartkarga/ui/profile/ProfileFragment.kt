package com.gayyedfam.grainsmartkarga.ui.profile

import android.annotation.SuppressLint
import android.location.Geocoder
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.gayyedfam.grainsmartkarga.R
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_profile.*


@AndroidEntryPoint
class ProfileFragment : Fragment() {

    lateinit var rootView: View
    private val profileViewModel: ProfileViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_profile, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imageViewBack.setOnClickListener {
            findNavController().popBackStack()
        }

        buttonSave.setOnClickListener {
            val address = textEditAddress.text.toString()
            val name = textEditName.text.toString()
            val contact = textEditContact.text.toString()

            var isValid = true

            if(address.isBlank()) {
                isValid = false
                textInputAddress.error = "Please provide delivery address"
            } else {
                textInputAddress.error = ""
            }

            if(name.isBlank()) {
                isValid = false
                textInputName.error = "Please provide your name"
            } else {
                textInputName.error = ""
            }

            if(contact.isBlank()) {
                isValid = false
                textInputContact.error = "Please provide contact number"
            } else {
                textInputContact.error = ""
            }

            if(isValid) {
                profileViewModel.save(name, contact, address, getDeviceId())
            }
        }

        profileViewModel.profileViewState.observe(viewLifecycleOwner, Observer {
            when(it) {
                is ProfileViewState.ProfileSaved -> {
                    Snackbar.make(rootView, "Profile saved successfully!", Snackbar.LENGTH_SHORT).show()
                    findNavController().popBackStack()
                }
                is ProfileViewState.ProfileLoaded -> {
                    textEditName.setText(it.profile.name)
                    textEditContact.setText(it.profile.contact)
                    textEditAddress.setText(it.profile.address)
                }
                is ProfileViewState.DeviceAddressLoaded -> {
                    val geoCoder = Geocoder(context)
                    val address = geoCoder.getFromLocation(it.deviceLocation.locationLat, it.deviceLocation.locationLon, 1)

                    val firstAddress = address.first()

                    val deviceAddress = StringBuilder()
                        .append(firstAddress.subThoroughfare)
                        .append(", ")
                        .append(firstAddress.adminArea)
                        .append(", ")
                        .append(firstAddress.countryCode)
                        .append(", ")
                        .append(firstAddress.countryName)
                        .append(", ")
                        .append(firstAddress.featureName)
                        .append(", ")
                        .append(firstAddress.locale)
                        .append(", ")
                        .append(firstAddress.locality)
                        .append(", ")
                        .append(firstAddress.premises)
                        .append(", ")
                        .append(firstAddress.subAdminArea)

                    //textEditAddress.setText(deviceAddress.toString())
                }
            }
        })
    }

    @SuppressLint("HardwareIds")
    private fun getDeviceId(): String {
        var deviceId: String = ""

        context?.let {
            deviceId = Settings.Secure.getString(
                it.contentResolver,
                Settings.Secure.ANDROID_ID
            )
        }

        return deviceId
    }
}