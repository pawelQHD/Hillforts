package org.wit.hillforts.main


import android.app.Application
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.wit.hillforts.models.HillfortModel

class MainApp : Application(), AnkoLogger {

    val hillforts = ArrayList<HillfortModel>()

    override fun onCreate() {
        super.onCreate()
        info("Hilforts started")
//        hillforts.add(HillfortModel("1st Hillfort", "About first..."))
//        hillforts.add(HillfortModel("2nd Hillfort", "About second..."))
//        hillforts.add(HillfortModel("3rd Hillfort", "About third..."))
    }
}