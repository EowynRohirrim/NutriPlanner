package com.patri.nutriplanner

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.patri.nutriplanner.database.dao.FoodDao
import com.patri.nutriplanner.database.dao.PlanningDao
import com.patri.nutriplanner.database.dao.UserDao
import com.patri.nutriplanner.database.entities.FoodEntity
import com.patri.nutriplanner.database.entities.PlanningEntity
import com.patri.nutriplanner.database.entities.UserEntity

//Es un array con todas las tablas, con tantas clases entities como tablas tenga
@Database(entities = [FoodEntity::class, PlanningEntity::class, UserEntity::class], version =2 , exportSchema =false) //El 1 es un control de versiones, que ahora no se va a usar
abstract class FoodDatabase: RoomDatabase() {

    //Hay dos entities pues dos funciones, una por cada tabla
    abstract fun getFoodDao(): FoodDao
    abstract fun getPlanningDao(): PlanningDao
    abstract fun getUserDao(): UserDao


    companion object {
        @Volatile //garantiza la visibilidad de los cambios de esta variable en múltiple procesos
        private var INSTANCE: FoodDatabase? = null

        fun getDatabase(context: Context): FoodDatabase {
            return INSTANCE ?: synchronized(this) { //para que no se creen múltiples instancias de la bbdd
                val instance = Room.databaseBuilder( //Crea la bbdd
                    context.applicationContext,
                    FoodDatabase::class.java,
                    "NutriPlanner_database2"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}