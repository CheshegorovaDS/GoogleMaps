package com.novikova.darya

import android.Manifest
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.fragment_maps.*
import pub.devrel.easypermissions.EasyPermissions

const val OFFSET = 300f
class MapFragment: Fragment(), OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener,
    GoogleMap.OnMyLocationClickListener, GoogleMap.OnMarkerClickListener,
    GoogleMap.OnPolylineClickListener, GoogleMap.OnPoiClickListener {

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
        googleMap.isMyLocationEnabled = EasyPermissions
            .hasPermissions(activity!!.applicationContext, Manifest.permission.ACCESS_FINE_LOCATION)
//        вид карты
        googleMap.mapType = GoogleMap.MAP_TYPE_NORMAL
//        определение местоположения
        googleMap.setOnMyLocationButtonClickListener(this)
        googleMap.setOnMyLocationClickListener(this)

        creatingMarker(googleMap)

        creatingPolyline(googleMap)

        creatingPolygon(googleMap)

        creatingCircle(googleMap)

        buttonClickListener(googleMap)

        googleMap.setOnPoiClickListener(this)

//         появление кнопок приближения и удаления
        googleMap.uiSettings.isZoomControlsEnabled = true

//        отключение ссылки на карты Google Maps
        googleMap.uiSettings.isMapToolbarEnabled = false

    }

    private fun creatingMarker(googleMap: GoogleMap) {
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

    private fun creatingPolyline(googleMap: GoogleMap) {
        val polyline = googleMap.addPolyline(
            PolylineOptions()
                .clickable(true)
                .add(
                    LatLng(-35.016, 143.321),
                    LatLng(-34.747, 145.592),
                    LatLng(-34.364, 147.891),
                    LatLng(-33.501, 150.217),
                    LatLng(-32.306, 149.248),
                    LatLng(-32.491, 147.309)
                )
        )
//        style

//        закргуленные концы
        polyline.startCap = RoundCap()
        polyline.endCap = RoundCap()

//        цвет
        polyline.color = Color.CYAN

        polyline.isGeodesic = true

        googleMap.setOnPolylineClickListener(this)
    }

    private fun  creatingPolygon(googleMap: GoogleMap) {
        val polygon = googleMap.addPolygon(
            PolygonOptions()
                .clickable(true)
                .add(
                    LatLng(-27.457, 153.040),
                    LatLng(-33.852, 151.211),
                    LatLng(-37.813, 144.962),
                    LatLng(-34.928, 138.599)
                )
        )

    }

    private fun creatingCircle(googleMap: GoogleMap) {
        val circle =  CircleOptions()
            .center(LatLng(0.0, 15.0))
            .radius(50000.0)
            .strokeColor(Color.GREEN)
            .strokeWidth(5f)
            .fillColor(0x5500ff00)
        googleMap.addCircle(circle)
    }

    private fun buttonClickListener(googleMap: GoogleMap) {
        //            перемещение камеры на указанное кол-во пикселов
        top.setOnClickListener {
            val c= CameraUpdateFactory.scrollBy(0f,-OFFSET)
            googleMap.animateCamera(c)
        }

        down.setOnClickListener {
            val c= CameraUpdateFactory.scrollBy(0f, OFFSET)
            googleMap.animateCamera(c)
        }

        right.setOnClickListener {
            val c= CameraUpdateFactory.scrollBy(OFFSET,0f)
            googleMap.animateCamera(c)
        }

        left.setOnClickListener {
            val c= CameraUpdateFactory.scrollBy(-OFFSET,0f)
            googleMap.animateCamera(c)
        }
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

    override fun onPolylineClick(polyline: Polyline?) {
        Toast.makeText(context, "Click Polyline", Toast.LENGTH_SHORT).show()
    }

    override fun onPoiClick(poi: PointOfInterest?) {
        val text = "Clicked:  ${poi?.name } \nPlace ID: ${poi?.placeId} \nLatitude: ${poi?.latLng?.latitude} \nLongitude: ${poi?.latLng?.longitude}"
        Toast.makeText(context, text ,
            Toast.LENGTH_SHORT).show()
    }


}