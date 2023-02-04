package com.gayyedfam.grainsmartkarga.ui.profile

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.fragment.app.Fragment
import com.gayyedfam.grainsmartkarga.BuildConfig
import com.gayyedfam.grainsmartkarga.R
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import kotlinx.android.synthetic.main.item_delivery_input.*
import kotlinx.android.synthetic.main.item_delivery_map.*

class MapsFragment : Fragment() {

    private val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1000
    private lateinit var map: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val callback = OnMapReadyCallback { googleMap ->
        this.map = googleMap
        this.map.uiSettings.isZoomControlsEnabled = true
        setupLocationClient()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(callback)

        context?.let {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(it)
            Places.initialize(it, BuildConfig.MAPS_API_KEY)
            Places.createClient(it)
        }

//        val searchMapsFragment =
//            activity?.supportFragmentManager?.findFragmentById(R.id.searchMapsFragment)
//                    as AutocompleteSupportFragment
//        searchMapsFragment.setPlaceFields(listOf(Place.Field.ID, Place.Field.NAME))
//        // Set up a PlaceSelectionListener to handle the response.
//        searchMapsFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
//            override fun onPlaceSelected(place: Place) {
//                place.latLng?.let {
//                    updateLocationMarker(it.latitude, it.longitude)
//                }
//            }
//
//            override fun onError(status: Status) {
//
//            }
//        })
    }

    override fun onResume() {
        super.onResume()
        mapView?.onResume()
    }

    override fun onStart() {
        super.onStart()
        mapView?.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView?.onStop()
    }

    override fun onPause() {
        super.onPause()
        mapView?.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView?.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView?.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView?.onSaveInstanceState(outState)
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray) {
        when (requestCode) {
            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {
                if (grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    setupLocationClient()
                }
            }
        }
    }

    private fun setupLocationClient() {
        context?.let {
            if (checkSelfPermission(it, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(it, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION)
            } else {
                fusedLocationClient.lastLocation.addOnSuccessListener {location ->
                    location?.let {
                        updateLocationMarker(location.latitude, location.longitude)
                    } .run {
                        val locationRequest = LocationRequest.create()
                        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                        locationRequest.interval = 20 * 1000

                        val locationCallback = object : LocationCallback() {
                            override fun onLocationResult(locationResult: LocationResult) {
                                locationResult.let {result ->
                                    fusedLocationClient.removeLocationUpdates(this)
                                    result.locations.forEach {newLocation ->
                                        updateLocationMarker(newLocation.latitude, newLocation.longitude)
                                        return
                                    }
                                }
                            }
                        }

                        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
                    }
                }
            }
        }
    }

    private fun updateLocationMarker(
        latitude: Double,
        longitude: Double
    ) {
        map.apply {
            val updatedLocation = LatLng(latitude, longitude)
            addMarker(
                MarkerOptions()
                    .position(updatedLocation)
                    .title("You are here")
                    .draggable(true)
            )

            moveCamera(CameraUpdateFactory.newLatLngZoom(updatedLocation, 18.0f))

            val geoCoder = Geocoder(requireContext())


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                geoCoder.getFromLocation(latitude, longitude, 1, Geocoder.GeocodeListener {
                    val firstAddress = it.first()

                    val deviceAddress = StringBuilder()
                        .append(firstAddress.subThoroughfare)
                        .append(firstAddress.adminArea)
                        //.append(firstAddress.countryCode)
                        .append(firstAddress.countryName)
                        //.append(firstAddress.featureName)
                        .append(firstAddress.locale)
                        .append(firstAddress.locality)
                    //.append(firstAddress.premises)
                    //.append(firstAddress.subAdminArea)
                    //.append(firstAddress.phone)

                    //textEditAddress.setText(deviceAddress.toString())
                })
            } else {
                geoCoder.getFromLocation(latitude, longitude, 1)
            }
        }
    }

}