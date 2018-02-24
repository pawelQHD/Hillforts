package org.wit.hillforts.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_hillfort.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.toast
import org.wit.hillforts.main.MainApp
import org.wit.hillforts.models.HillfortModel
import org.wit.placemark.R

class HillfortActivity : AppCompatActivity(), AnkoLogger {

    var hillfort = HillfortModel()
    lateinit var app: MainApp
    var edit = false

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
        }

        btnAdd.setOnClickListener() {
            hillfort.townland = hillfortTitle.text.toString()
            hillfort.county = hillfortCounty.text.toString()

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
    }


