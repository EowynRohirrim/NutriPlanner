package com.patri.nutriplanner.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.patri.nutriplanner.User.User

@Entity(tableName = "user")
data class UserEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Int = 0,
    @ColumnInfo(name = "nombre") val name: String,
    @ColumnInfo(name = "edad") val age: String,
    @ColumnInfo(name = "gender") val gender: String
)

fun User.toDatabase() = UserEntity(
    name = name,
    age = age,
    gender = gender
)
