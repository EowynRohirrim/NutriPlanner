package com.patri.nutriplanner.database.dao


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.patri.nutriplanner.database.entities.FoodEntity

@Dao
interface FoodDao {

    // Obtener todos los alimentos ordenados alfabéticamente por nombre
    @Query("SELECT * FROM food_table2 ORDER BY LOWER(name) ASC")
    suspend fun getAllFood(): List<FoodEntity>

    //Select se usa query
    //Búsqueda
    @Query("SELECT * FROM food_table2 WHERE name Like  :query")
    suspend fun getFood(query: String): List<FoodEntity>

    // Búsqueda por nombre exacto (ignorando mayúsculas/minúsculas)
    @Query("SELECT * FROM food_table2 WHERE LOWER(name) = LOWER(:name)")
    suspend fun getFoodByName(name: String): List<FoodEntity>


    //Select por estado del alimento
    @Query("SELECT * FROM food_table2 WHERE state = :state ORDER BY LOWER(name) ASC")
    suspend fun getFoodByState(state: String): List<FoodEntity>

    /***Métodos para borrar e insertar se necesitan si o si*/

    //Para insertar se usa insert, insertar en la tabla
    @Insert(onConflict = OnConflictStrategy.REPLACE) //onConflict si hay error de los superheroes entonces reemplaza
    suspend fun insertAll(foodList: List<FoodEntity>) //objetos de la base de datos

    //Delete se usa query, para borrar la BBDD
    @Query("DELETE FROM food_table2")
    suspend fun deleteAllFoodList()

    // Para eliminar un alimento por su ID
    @Query("DELETE FROM food_table2 WHERE id = :id")
    suspend fun deleteFoodById(id: Int)

    // Consulta para obtener un FoodEntity por su id
    @Query("SELECT * FROM food_table2 WHERE id = :id")
    suspend fun getFoodById(id: Int): FoodEntity

    // Consulta para actualizar un FoodEntity en la base de datos
    @Update
    suspend fun update(foodEntity: FoodEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(foodEntity: FoodEntity)
}