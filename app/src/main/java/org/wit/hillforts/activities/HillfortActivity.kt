package org.wit.hillforts.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_hillfort.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.toast
import org.wit.hillforts.main.MainApp
import org.wit.hillforts.models.HillfortModel
import org.wit.placemark.R

class HillfortActivity : AppCompatActivity(), AnkoLogger {

    var hillfort = HillfortModel()

    lateinit var app : MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hillfort)
        app = application as MainApp

        btnAdd.setOnClickListener() {
            hillfort.townland = hillfortTitle.text.toString()
            hillfort.county = hillfortCounty.text.toString()
            if (hillfort.townland.isNotEmpty()) {
                app.hillforts.add(hillfort)
                toast ("Title Added")
            }
    }
    }
}
