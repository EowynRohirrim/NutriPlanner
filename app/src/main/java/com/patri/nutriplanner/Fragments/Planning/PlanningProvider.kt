package com.patri.nutriplanner.Fragments.Planning

import android.util.Log

class PlanningProvider {

    companion object {
        fun getWeek(): List<Day> {
            Log.d("PlanningProvider", "Providing week data: $week")
            return week
        }

        private val week: List<Day> = listOf(
            Day("Lunes", "Zumo de naranja", "Piña", "Ensalada", "Yogur", "Pasta"),
            Day("Martes", "Yogurt con avena", "Frutas", "Sándwich de jamón y queso", "Frutos secos", "Pollo al horno"),
            Day("Miércoles", "Tostadas con jamón serrano", "Batido de frutas", "Arroz con verduras", "Galletas", "Pescado a la plancha"),
            Day("Jueves", "Café con magdalena", "Nueces", "Ensalada César", "Manzana", "Tortilla de patata"),
            Day("Viernes", "Leche con cereales", "Pan con tomate", "Ensalada y hamburguesa de cerdo", "Yogurt griego", "Sopa de verduras"),
            Day("Sábado", "Magdalenas y zumo de piña", "Avellanas", "Pasta al pesto", "Gelatina", "Pizza casera"),
            Day("Domingo", "Churros con chocolate", "Tostada con aguacate", " Paella y sandía", "Smoothie de plátano", "Sushi")
        )
    }


}