package org.wit.hillforts.room

import android.arch.persistence.room.*
import org.wit.hillforts.models.HillfortModel

@Dao
interface HillfortDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun create(hillfort: HillfortModel)

    @Query("SELECT * FROM HillfortModel")
    fun findAll(): List<HillfortModel>

    @Update
    fun update(hillfort: HillfortModel)

    @Delete
    fun deletePlacemark(hillfort: HillfortModel)

    @Query("select * from HillfortModel where id = :arg0")
    fun findById(id: Long): HillfortModel
}