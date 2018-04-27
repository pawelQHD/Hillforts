package org.wit.hillforts.models

interface HillfortStore {
    fun create(hillfort: HillfortModel)
    fun update(hillfort: HillfortModel)
    suspend fun findAll(): List<HillfortModel>
    fun delete(hillfort: HillfortModel)
}