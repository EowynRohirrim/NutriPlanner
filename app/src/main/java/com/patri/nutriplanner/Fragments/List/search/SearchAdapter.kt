package com.patri.nutriplanner.Fragments.List.search

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.patri.nutriplanner.R
import com.patri.nutriplanner.database.entities.FoodEntity

class SearchAdapter(var foodList: MutableList<FoodEntity> = mutableListOf(),
                    private val onItemClick: (FoodEntity) -> Unit
                     ) : RecyclerView.Adapter<SearchViewHolder>() {

    fun updateList(list: List<FoodEntity>) {
        foodList.clear()
        foodList.addAll(list)
        notifyDataSetChanged()
        Log.d("ListAdapter", "Lista actualizada con ${foodList.size} elementos")
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_food_search, parent, false)
        return SearchViewHolder(view, onItemClick)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(foodList[position])
    }
    override fun getItemCount() = foodList.size

}