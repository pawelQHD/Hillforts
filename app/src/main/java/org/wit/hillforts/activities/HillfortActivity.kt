package org.wit.hillforts.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_hillfort.*
import kotlinx.android.synthetic.main.content_hillfort_map.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast
import org.wit.hillforts.helpers.*
import org.wit.hillforts.main.MainApp
import org.wit.hillforts.models.HillfortModel
import org.wit.hillforts.models.Location
import org.wit.placemark.R

class HillfortActivity : AppCompatActivity(), AnkoLogger {

    var hillfort = HillfortModel()
    lateinit var app: MainApp
    var edit = false
    val IMAGE_REQUEST = 1
    val LOCATION_REQUEST = 2
    lateinit var map: GoogleMap
    val defaultLocation = Location(52.245696, -7.139102, 15f)
    private lateinit var locationService: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hillfort)
        app = application as MainApp
        mapView3.onCreate(savedInstanceState)
        here.isEnabled = false
        locationService = LocationServices.getFusedLocationProviderClient(this)

        toolbarAdd.title = title
        setSupportActionBar(toolbarAdd)

        mapView3.getMapAsync {
            map = it
            configureMap()
        }

        if (intent.hasExtra("placemark_edit")) {
            edit = true
            hillfort = intent.extras.getParcelable<HillfortModel>("placemark_edit")
            hillfortTitle.setText(hillfort.townland)
            hillfortCounty.setText(hillfort.county)
            hillfortDate.setText(hillfort.date)
            //hillfortPosition.setText(hillfort.position)
            placemarkImage.setImageBitmap(readImageFromPath(this, hillfort.image))
            if (hillfort.image != null) {
                chooseImage.setText(R.string.change_hillfort_image)
            } else {
                hillfort.lat = defaultLocation.lat
                hillfort.lng = defaultLocation.lng
                hillfort.zoom = defaultLocation.zoom
            }
        }
        chooseImage.setOnClickListener {
            showImagePicker(this, IMAGE_REQUEST)

        }
        placemarkLocation.setOnClickListener {
            val location = Location(52.245696, -7.139102, 15f)
            if (hillfort.zoom != 0f) {
                defaultLocation.lat =  hillfort.lat
                defaultLocation.lng = hillfort.lng
                defaultLocation.zoom = hillfort.zoom
            }
            startActivityForResult(intentFor<MapsActivity>().putExtra("location", location), LOCATION_REQUEST)
        }
        here.setOnClickListener {
            setCurrentLocation()
        }
    }
    fun save() {
        hillfort.townland = hillfortTitle.text.toString()
        hillfort.county = hillfortCounty.text.toString()
        hillfort.date = hillfortDate.text.toString()

        if (edit) {
            app.hillforts.update(hillfort.copy())
            setResult(201)
            finish()
        } else {
            if (hillfort.townland.isNotEmpty()) {
                app.hillforts.create(hillfort.copy())
                setResult(200)
                finish()
            } else {
                toast(R.string.enter_hillfort_title)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_hillfort, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.item_save -> {
                save()
            }
            R.id.item_cancel -> {
                setResult(RESULT_CANCELED)
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun configureMap() {
        map.uiSettings.setZoomControlsEnabled(true)
        val loc = LatLng(hillfort.lat, hillfort.lng)
        val options = MarkerOptions().title(hillfort.townland).position(loc)
        map.addMarker(options)
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, hillfort.zoom))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            IMAGE_REQUEST -> {
                if (data != null) {
                    hillfort.image = data.getData().toString()
                    placemarkImage.setImageBitmap(readImage(this, resultCode, data))
                    chooseImage.setText(R.string.change_hillfort_image)
                }
            }
            LOCATION_REQUEST -> {
                if (data != null) {
                    val location = data.extras.getParcelable<Location>("location")
                    map.clear()
                    hillfort.lat = location.lat
                    hillfort.lng = location.lng
                    hillfort.zoom = location.zoom
                    configureMap()
                }
            }
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        mapView3.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView3.onLowMemory()
    }

    override fun onPause() {
        super.onPause()
        mapView3.onPause()
    }

    override fun onResume() {
        super.onResume()
        mapView3.onResume()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        mapView3.onSaveInstanceState(outState)
    }
    override fun onStart() {
        super.onStart()
        if (checkLocationPermissions(this)) {
            here.isEnabled = true
        }
    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (isPermissionGranted(requestCode, grantResults)) {
            here.isEnabled = true
        }
    }
    @SuppressLint("MissingPermission")
    fun setCurrentLocation() {
        locationService.lastLocation.addOnSuccessListener {
            defaultLocation.lat = it.latitude
            defaultLocation.lng = it.longitude
            hillfort.lat = it.latitude
            hillfort.lng = it.longitude
            configureMap()
        }
    }
}


