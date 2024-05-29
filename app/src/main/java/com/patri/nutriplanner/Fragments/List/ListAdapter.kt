package com.patri.nutriplanner.Fragments.List

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.patri.nutriplanner.R
import com.patri.nutriplanner.database.entities.FoodEntity

class ListAdapter(var foodList: MutableList<FoodEntity> = mutableListOf(),
                    private val itemClickListener: (FoodEntity) -> Unit,
                    private val onItemLongClick: (FoodEntity) -> Unit
                    ) : RecyclerView.Adapter<ListViewHolder>() {

    fun updateList(list: List<FoodEntity>) {
        foodList.clear()
        foodList.addAll(list)
        notifyDataSetChanged()
        Log.d("ListAdapter", "Lista actualizada con ${foodList.size} elementos")
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_food, parent, false)
        )
    }
    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(foodList[position])
        holder.itemView.setOnClickListener {
            itemClickListener(foodList[position])
        }
        holder.itemView.setOnLongClickListener {
            onItemLongClick(foodList[position])
            true
        }
    }
    override fun getItemCount() = foodList.size


    // Función para eliminar un elemento de la lista
    fun removeItem(position: Int) {
        foodList.removeAt(position)
        notifyItemRemoved(position)
    }

    // Función para obtener el elemento en una posición específica
    fun getItemAtPosition(position: Int): FoodEntity {
        return foodList[position]
    }
}