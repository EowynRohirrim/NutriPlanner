package com.patri.nutriplanner.Fragments.List.search

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.patri.nutriplanner.database.entities.FoodEntity
import com.patri.nutriplanner.databinding.ItemFoodSearchBinding
import com.squareup.picasso.Picasso

class SearchViewHolder (view: View, private val onItemClick: (FoodEntity) -> Unit) : RecyclerView.ViewHolder(view) {

    private val binding = ItemFoodSearchBinding.bind(view)

    fun bind(foodItemResponse: FoodEntity) {

        binding.tvName.text= foodItemResponse.name
        binding.tvState.text = foodItemResponse.state.stateDescription


        //Picasso.get().load(foodItemResponse.image).into(binding.ivFood)

        val imageUrl = foodItemResponse.image
        if (imageUrl.isNotBlank()) {
            Picasso.get().load(imageUrl).into(binding.ivFood)
        } else {
            // Si la URL de la imagen está vacía, no hacemos nada para que la imagen no muestre nada
            binding.ivFood.setImageDrawable(null)
        }

        // Agregar el listener de clic aquí
        itemView.setOnClickListener {
            onItemClick(foodItemResponse)
        }

    }
}