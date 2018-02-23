package org.wit.hillforts.models

interface HillfortStore {
    fun findAll(): List<HillfortModel>
    fun create(placemark: HillfortModel)
}