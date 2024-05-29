package com.patri.nutriplanner.Fragments.Pantry

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.patri.nutriplanner.R
import com.patri.nutriplanner.database.entities.FoodEntity
import com.patri.nutriplanner.Fragments.List.ListViewHolder
import java.util.Locale

class PantryAdapter(var foodList: MutableList<FoodEntity> = mutableListOf()) : RecyclerView.Adapter<ListViewHolder>() {

    private var fullFoodList: List<FoodEntity> = foodList.toList()

    fun updateList(list: List<FoodEntity>) {
        foodList.clear()
        foodList.addAll(list)
        fullFoodList = list.toList()
        notifyDataSetChanged()
        Log.d("PantryAdapter", "Lista actualizada con ${foodList.size} elementos")
    }

    fun filterList(query: String) {
        foodList = if (query.isEmpty()) {
            fullFoodList.toMutableList()
        } else {
            val filteredList = fullFoodList.filter {
                it.name.toLowerCase(Locale.ROOT).contains(query.toLowerCase(Locale.ROOT))
            }
            filteredList.toMutableList()
        }
        notifyDataSetChanged()
        Log.d("PantryAdapter", "Lista filtrada con ${foodList.size} elementos")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_food, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(foodList[position])
    }

    override fun getItemCount() = foodList.size

    fun removeItem(position: Int) {
        foodList.removeAt(position)
        notifyItemRemoved(position)
    }

    fun getItemAtPosition(position: Int): FoodEntity {
        return foodList[position]
    }
}
