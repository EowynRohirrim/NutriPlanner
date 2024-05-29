package com.patri.nutriplanner.Fragments.Pantry

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.patri.nutriplanner.FoodDatabase
import com.patri.nutriplanner.R
import com.patri.nutriplanner.database.entities.FoodEntity
import com.patri.nutriplanner.database.entities.FoodState
import com.patri.nutriplanner.databinding.FragmentPantryBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PantryFragment : Fragment(R.layout.fragment_pantry) {

    private lateinit var binding: FragmentPantryBinding
    private lateinit var adapter: PantryAdapter
    private lateinit var room: FoodDatabase

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPantryBinding.bind(view)

        //Inicializar el objeto room usando el singleton
        room = FoodDatabase.getDatabase(requireContext())

        initUI()
        setupItemTouchHelper() // Configuración del ItemTouchHelper para eliminar alimentos al deslizar
        setupSearchView() // Configurar el SearchView

    }

    private fun initUI() {

        adapter = PantryAdapter()
        binding.rvFood.setHasFixedSize(true)
        binding.rvFood.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvFood.adapter = adapter

        showPantryFood()
    }


    private fun setupSearchView() {
        binding.svPantry.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filterList(newText.orEmpty())
                return true
            }
        })
    }


    private fun showPantryFood() {
        Log.d("ListFragment", "show pantry Food: Start")
        binding.progressBar.visibility = View.VISIBLE
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val pantryFood: List<FoodEntity> = room.getFoodDao().getFoodByState(FoodState.PANTRY.name)
                Log.d("lista","${pantryFood}")
                withContext(Dispatchers.Main) {
                    Log.d("ListFragment", "show pantry Food: Fetched ${pantryFood.size} items")
                    if (pantryFood.isEmpty()) {
                        Log.d("ListFragment", "No se encontraron alimentos en estado de compras")
                    } else {
                        Log.d("ListFragment", "Alimentos encontrados: ${pantryFood.size}")
                    }
                    adapter.updateList(pantryFood)
                    binding.progressBar.visibility = View.GONE
                }
            } catch (e: Exception) {
                Log.e("ListFragment", "Error fetching pantry food", e)
                withContext(Dispatchers.Main) {
                    binding.progressBar.visibility = View.GONE
                }
            }
        }
    }

    /**
     * Método que controla las acciones a realizar con los alimentos
     * Si se desliza el alimento hacia la izquierda se borra
     * Si se desliza el alimento hacia la derecha se mueve a la despensa ''pantry''
     */
    private fun setupItemTouchHelper() {
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                // Obtener la posición del elemento deslizado
                val position = viewHolder.adapterPosition
                // Obtener el elemento correspondiente del adaptador
                val foodEntity = adapter.getItemAtPosition(position)

                if (direction == ItemTouchHelper.LEFT) {
                    updateFoodStateToNull(foodEntity)
                    // Eliminar el elemento de la base de datos
                    //deleteFoodFromDatabase(foodEntity)
                    // Eliminar el elemento del adaptador
                    //adapter.removeItem(position)
                } else if (direction == ItemTouchHelper.RIGHT) {
                    // Actualizar el estado del elemento a PANTRY
                    updateFoodStateToShopping(foodEntity)
                }
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                val itemView = viewHolder.itemView
                val paint = Paint()

                if (dX > 0) { // Deslizar hacia la derecha
                    paint.color = Color.argb(50, 0, 255, 0) // Verde translúcido
                    c.drawRect(
                        itemView.left.toFloat(), itemView.top.toFloat(),
                        itemView.left + dX, itemView.bottom.toFloat(), paint
                    )
                } else if (dX < 0) { // Deslizar hacia la izquierda
                    paint.color = Color.argb(50, 255, 0, 0) // Rojo translúcido
                    c.drawRect(
                        itemView.right + dX, itemView.top.toFloat(),
                        itemView.right.toFloat(), itemView.bottom.toFloat(), paint
                    )
                }

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }
        }

        // Configurar el ItemTouchHelper
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(binding.rvFood)
    }

    /**
     * Método para actualizar el estado de un alimento a PANTRY
     */
    private fun updateFoodStateToShopping(foodEntity: FoodEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            val updatedFood = foodEntity.copy(state = FoodState.SHOPPING)
            room.getFoodDao().update(updatedFood)

            //Actualizo recyclerview
            val shoppingFood: List<FoodEntity> = room.getFoodDao().getFoodByState(FoodState.PANTRY.name)
            withContext(Dispatchers.Main) {
                Log.d("ListFragment", "Alimento movido a PANTRY: ${updatedFood.name}")
                adapter.updateList(shoppingFood)
            }
        }
    }

    private fun updateFoodStateToNull(foodEntity: FoodEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            val updatedFood = foodEntity.copy(state = FoodState.NULL)
            room.getFoodDao().update(updatedFood)
            val shoppingFood: List<FoodEntity> = room.getFoodDao().getFoodByState(FoodState.PANTRY.name)
            withContext(Dispatchers.Main) {
                adapter.updateList(shoppingFood)
            }
        }
    }


}