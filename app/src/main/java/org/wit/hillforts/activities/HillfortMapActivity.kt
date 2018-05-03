package org.wit.hillforts.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
//import org.wit.hillforts.R
import kotlinx.android.synthetic.main.activity_hillfort_map.*
import kotlinx.android.synthetic.main.content_hillfort_map.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.wit.hillforts.main.MainApp
import org.wit.placemark.R

class HillfortMapActivity : AppCompatActivity(), GoogleMap.OnMarkerClickListener {

    lateinit var map: GoogleMap
    lateinit var app: MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hillfort_map)
        setSupportActionBar(toolbarMaps)
        app = application as MainApp

        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync {
            map = it
            configureMap()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    fun configureMap() {
        map.uiSettings.setZoomControlsEnabled(true)
        map.setOnMarkerClickListener(this)
        async(UI) {
            app.hillforts.findAll().forEach {
                val loc = LatLng(it.lat, it.lng)
                val options = MarkerOptions().title(it.townland).position(loc)
                map.addMarker(options).tag = it.id
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, it.zoom))
            }
        }
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        currentTitle.text = marker.title
        return false
    }
}
