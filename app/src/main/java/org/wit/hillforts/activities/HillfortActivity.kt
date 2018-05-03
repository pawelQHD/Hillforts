package org.wit.hillforts.activities

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_hillfort.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast
import org.wit.hillforts.helpers.readImage
import org.wit.hillforts.helpers.readImageFromPath
import org.wit.hillforts.helpers.showImagePicker
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
    //var location = Location(52.245696, -7.139102, 15f)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hillfort)
        app = application as MainApp

        toolbarAdd.title = title
        setSupportActionBar(toolbarAdd)

        if (intent.hasExtra("placemark_edit")) {
            edit = true
            btnAdd.setText(R.string.save_hillfort)
            hillfort = intent.extras.getParcelable<HillfortModel>("placemark_edit")
            hillfortTitle.setText(hillfort.townland)
            hillfortCounty.setText(hillfort.county)
            hillfortDate.setText(hillfort.date)
            //hillfortPosition.setText(hillfort.position)
            placemarkImage.setImageBitmap(readImageFromPath(this, hillfort.image))
            if (hillfort.image != null) {
                chooseImage.setText(R.string.change_hillfort_image)
            }
        }

        btnAdd.setOnClickListener() {
            /*hillfort.townland = hillfortTitle.text.toString()
            hillfort.county = hillfortCounty.text.toString()
            //hillfort.position = hillfortPosition.text.toString()
            hillfort.date = hillfortDate.text.toString()

            if (edit) {
                app.hillforts.update(hillfort.copy())
                setResult(201)
                finish()
            } else if (hillfort.townland.isEmpty()) {
                toast(R.string.enter_hillfort_title)
            } else {
                app.hillforts.create(hillfort.copy())
                setResult(200)
                finish()
            }*/

            hillfort.townland = hillfortTitle.text.toString()
            hillfort.county = hillfortCounty.text.toString()
            hillfort.date = hillfortDate.text.toString()

            if (edit) {
                app.hillforts.update(hillfort.copy())
                setResult(201)
                finish()
            }
            else {
                if (hillfort.townland.isNotEmpty()) {
                    app.hillforts.create(hillfort.copy())
                    setResult(200)
                    finish()
                }
                else {
                    toast(R.string.enter_hillfort_title)
                }
            }
        }
        chooseImage.setOnClickListener {
            showImagePicker(this, IMAGE_REQUEST)

        }
        placemarkLocation.setOnClickListener {
            val location = Location(52.245696, -7.139102, 15f)
            if (hillfort.zoom != 0f) {
                location.lat =  hillfort.lat
                location.lng = hillfort.lng
                location.zoom = hillfort.zoom
            }
            startActivityForResult(intentFor<MapsActivity>().putExtra("location", location), LOCATION_REQUEST)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_hillfort, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.item_cancel -> {
                setResult(RESULT_CANCELED)
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
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
                    hillfort.lat = location.lat
                    hillfort.lng = location.lng
                    hillfort.zoom = location.zoom
                }
            }
        }
    }
}


