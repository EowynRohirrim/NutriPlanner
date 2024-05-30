package com.patri.nutriplanner.database.entities

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.patri.nutriplanner.Food



// Define un enum para los diferentes tipos de comida
enum class FoodType(val typeName: String) {
    NULL("Sin tipo de alimento"),
    LACTEOS("Lácteos y derivados"),
    CARNE("Carne"),
    PESCADO("Pescado"),
    HUEVOS("Huevos"),
    LEGUMBRES("Legumbres"),
    FRUTOSSECOS("Frutos secos"),
    VERDURAS("Verduras y hortalizas"),
    FRUTAS("Frutas"),
    CEREALES("Cereales y derivados"),
    ACEITE("Aceite y alimentos grasos")
}

// Enum para el estado del alimento
enum class FoodState(val stateName: String, val stateDescription: String) {
    NULL("Null", "Sin asignar"),
    SHOPPING("Shopping", "Lista de la compra"),
    PANTRY("Pantry", "Despensa")
}


/**                             Tabla del Recycler View                  **/
@Entity(tableName="food_table2")
data class FoodEntity(// Es como crear una tabla en SQL

    //Clave primaria
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Int = 0, //el primer registro que mete es cero (estilo lista)
    @NonNull @ColumnInfo(name = "name") val name: String,
    @NonNull @ColumnInfo(name  = "type") val type: FoodType,
    @ColumnInfo(name = "image") val image: String,
    @ColumnInfo(name = "state") val state: FoodState

    )

//Mapeo
fun Food.toDatabase() = FoodEntity(
    name = name,
    type = FoodType.valueOf(type.uppercase()),
    image = image,
    state = FoodState.valueOf(state.uppercase())
)


