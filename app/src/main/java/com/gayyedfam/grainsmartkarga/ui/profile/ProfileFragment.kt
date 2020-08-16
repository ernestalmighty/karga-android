package com.gayyedfam.grainsmartkarga.ui.profile

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Intent
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
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_profile.*


@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private lateinit var rootView: View
    private val LOCATION_REQUEST = 1002
    private val profileViewModel: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_profile, container, false)
        return rootView
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == LOCATION_REQUEST && resultCode == RESULT_OK) {
            data?.let {
                val place = Autocomplete.getPlaceFromIntent(it)
                val name = place.name
                val latLang = place.latLng

                val addressComponents = place.addressComponents
                val clientAddress = StringBuilder()
                addressComponents?.let { component ->
                    component.asList().forEach { address ->
                        clientAddress.append(address.name)
                        clientAddress.append(" ")
                    }
                }

                textEditAddress.setText(clientAddress.toString())
                textEditDetailAddress.setText(name)

                profileViewModel.locationSelected(latLang)
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imageViewBack.setOnClickListener {
            findNavController().popBackStack()
        }

        textEditAddress.setOnClickListener {
            val fields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS_COMPONENTS)

            context?.let {
                val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
                    .setCountries(listOf("PH","SG"))
                    .build(it)
                startActivityForResult(intent, LOCATION_REQUEST)
            }
        }

        buttonSave.setOnClickListener {
            val detailAddress = textEditDetailAddress.text.toString()
            val address = textEditAddress.text.toString()
            val name = textEditName.text.toString()
            val contact = textEditContact.text.toString()

            var isValid = true

            if(address.isBlank()) {
                isValid = false
                textInputAddress.error = "Please provide your address"
            } else {
                textInputAddress.error = ""
            }

            if(detailAddress.isBlank()) {
                isValid = false
                textInputDetailAddress.error = "Please provide a detailed address"
            } else {
                textInputDetailAddress.error = ""
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
                profileViewModel.save(name, contact, address, detailAddress, getDeviceId(), textEditInstructions.text.toString())
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
                    textEditAddress.setText(it.profile.address1)
                    textEditDetailAddress.setText(it.profile.address2)
                    textEditInstructions.setText(it.profile.deliveryInstruction)
                }
                is ProfileViewState.DeviceAddressLoaded -> {

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