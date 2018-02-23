package org.wit.hillforts.models

import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class HillfortMemStore : HillfortStore, AnkoLogger {

    val placemarks = ArrayList<HillfortModel>()

    override fun findAll(): List<HillfortModel> {
        return placemarks
    }

    override fun create(placemark: HillfortModel) {
        placemarks.add(placemark)
        logAll()
    }

    internal fun logAll() {
        placemarks.forEach{ info("${it}") }
    }
}