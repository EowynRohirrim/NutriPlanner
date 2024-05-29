package com.patri.nutriplanner.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.patri.nutriplanner.Fragments.Planning.Day

@Entity(tableName = "vertical_bars")
data class PlanningEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Int = 0,
    @ColumnInfo(name = "label") val label: String,
    @ColumnInfo(name = "breakfast") var breakfast: String = "",
    @ColumnInfo(name = "snack") var snack: String = "",
    @ColumnInfo(name = "lunch") var lunch: String = "",
    @ColumnInfo(name = "snack2") var snack2: String = "",
    @ColumnInfo(name = "dinner") var dinner: String = ""
)
fun Day.toDatabase() = PlanningEntity(
        label = label,
        breakfast = breakfast,
        snack = snack,
        lunch = lunch,
        snack2 = snack2,
        dinner = dinner
    )
