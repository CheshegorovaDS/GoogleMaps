package com.novikova.darya

import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions


class MapFragment: Fragment(), OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener,
    GoogleMap.OnMyLocationClickListener, GoogleMap.OnMarkerClickListener {

    private val resLayout = R.layout.fragment_maps

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(resLayout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment =  childFragmentManager
            .findFragmentById(R.id.map) as? SupportMapFragment

        mapFragment?.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        googleMap.isMyLocationEnabled = (activity as MainActivity).mLocationPermissionsGranted
        googleMap.setOnMyLocationButtonClickListener(this)
        googleMap.setOnMyLocationClickListener(this)


        val marker = LatLng(0.0,0.0)
        googleMap.addMarker(MarkerOptions().position(marker).title("Zero coordinate"))

        val bridgeOmsk = LatLng(54.971814, 73.373343)
        googleMap.addMarker(
            MarkerOptions()
                .position(bridgeOmsk)
                .title("Omsk")
                .snippet("Leningradsky bridge")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET))
                // приоритет маркера по умолчанию 0
                .zIndex(0.1f)
        )

        //маркер, который можно перетаскивать
        val changingMarker = LatLng(54.0, 73.0)
        googleMap.addMarker(
            MarkerOptions()
                .position(changingMarker)
                .title("Move me")
                .draggable(true)
                .icon(
                    BitmapDescriptorFactory.fromBitmap(
                        resources.getDrawable(R.drawable.ic_flag)
                            .toBitmap(130, 130, null)
                    )
                )
        )
       googleMap.setOnMarkerClickListener(this)

        googleMap.moveCamera(CameraUpdateFactory.newLatLng(bridgeOmsk))
    }

    override fun onMyLocationButtonClick(): Boolean {
        Toast.makeText(context, "MyLocation button clicked", Toast.LENGTH_SHORT).show()
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false

    }

    override fun onMyLocationClick(location: Location) {
        Toast.makeText(context, "MyLocation clicked $location", Toast.LENGTH_SHORT).show()
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        Toast.makeText(context,"Click marker ${marker.title}", Toast.LENGTH_SHORT).show()
        return false
    }


}