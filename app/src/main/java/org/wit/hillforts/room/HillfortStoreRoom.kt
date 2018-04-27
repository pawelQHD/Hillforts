package org.wit.hillforts.room

import android.arch.persistence.room.Room
import android.content.Context
import org.jetbrains.anko.coroutines.experimental.bg
import org.wit.hillforts.models.HillfortModel
import org.wit.hillforts.models.HillfortStore

class HillfortStoreRoom(val context: Context) : HillfortStore {

    var dao: HillfortDao

    init {
        val database = Room.databaseBuilder(context, Database::class.java, "room_sample.db")
                .fallbackToDestructiveMigration()
                .build()
        dao = database.hillfortDao()
    }

    override suspend fun findAll(): List<HillfortModel> {
        val deferredPlacemarks = bg {
            dao.findAll()
        }
        val placemarks = deferredPlacemarks.await()
        return placemarks
    }

    override fun create(placemark: HillfortModel) {
        bg {
            dao.create(placemark)
        }
    }

    override fun update(placemark: HillfortModel) {
        bg {
            dao.update(placemark)
        }
    }
    override fun delete(placemark: HillfortModel) {
        bg {
            dao.deletePlacemark(placemark)
        }
    }
}