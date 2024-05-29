package com.patri.nutriplanner.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.patri.nutriplanner.database.entities.PlanningEntity

@Dao
interface PlanningDao {

    @Query("SELECT * FROM vertical_bars")
    suspend fun getAllDays(): List<PlanningEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(verticalBars: List<PlanningEntity>)

    @Update
    suspend fun update(verticalBar: PlanningEntity)

    @Query("SELECT * FROM vertical_bars WHERE label = :dayLabel")
    suspend fun getDayByLabel(dayLabel: String): PlanningEntity?

    @Query("DELETE FROM vertical_bars")
    suspend fun deleteAllPlanningList()

    // Nueva consulta para vaciar columnas específicas
    @Query("UPDATE vertical_bars SET breakfast = '', snack = '', lunch = '', snack2 = '', dinner = ''")
    suspend fun clearPlanningColumns()

}