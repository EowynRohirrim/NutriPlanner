package com.patri.nutriplanner.Fragments.List

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.patri.nutriplanner.database.entities.FoodEntity
import com.patri.nutriplanner.databinding.ItemFoodBinding
import com.squareup.picasso.Picasso

class ListViewHolder (view: View) : RecyclerView.ViewHolder(view) {

    private val binding = ItemFoodBinding.bind(view)

    fun bind(foodItemResponse: FoodEntity) {

        binding.tvName.text= foodItemResponse.name
        binding.tvType.text = foodItemResponse.type.typeName

        //Picasso.get().load(foodItemResponse.image).into(binding.ivFood)

        val imageUrl = foodItemResponse.image
        if (imageUrl.isNotBlank()) {
            Picasso.get().load(imageUrl).into(binding.ivFood)
        } else {
            // Si la URL de la imagen está vacía, no hacemos nada para que la imagen no muestre nada
            binding.ivFood.setImageDrawable(null)
        }

    }
}