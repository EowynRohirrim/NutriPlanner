package com.patri.nutriplanner.Fragments.List

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.patri.nutriplanner.FoodDatabase
import com.patri.nutriplanner.R
import com.patri.nutriplanner.database.entities.FoodEntity
import com.patri.nutriplanner.database.entities.FoodState
import com.patri.nutriplanner.database.entities.FoodType
import com.patri.nutriplanner.databinding.DialogFoodBinding
import com.patri.nutriplanner.databinding.DialogSearchBinding
import com.patri.nutriplanner.databinding.FragmentListBinding
import com.patri.nutriplanner.Fragments.List.search.SearchAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ListFragment : Fragment(R.layout.fragment_list) {

    private lateinit var binding: FragmentListBinding
    private lateinit var adapter: ListAdapter
    private lateinit var room: FoodDatabase

    // URL por defecto para la imagen
    private val defaultImageUrl = "android.resource://com.patri.nutriplanner/drawable/ic_launcher50"


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentListBinding.bind(view)

        room = FoodDatabase.getDatabase(requireContext())// Inicializar el objeto room

        initUI()

        // Configuración del clic del botón flotante para mostrar el diálogo
        binding.fabAddFood.setOnClickListener {
            showFoodDialog(null) // Cambiado a null para indicar un nuevo alimento

        }

        // Configuración del clic del botón flotante para mostrar el diálogo de búsqueda
        binding.fabSearchFood.setOnClickListener {
            showSearchDialog() // Mostrar el diálogo de búsqueda
        }

        setupItemTouchHelper() // Configuración del ItemTouchHelper para eliminar alimentos al deslizar
    }

    /**
     * Método que inicializa la interfaz
     */
    private fun initUI() {

        adapter = ListAdapter(
            itemClickListener = { foodEntity -> showFoodDialog(foodEntity) },
            onItemLongClick = { foodEntity -> showDeleteDialog(foodEntity) }
        )
        binding.rvFood.setHasFixedSize(true)
        binding.rvFood.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvFood.adapter = adapter

        showShoppingFood()
    }

    /**
     * Método que filtra los alimentos por el estado SHOPPING y los muestra en el recyclerview
     */
    private fun showShoppingFood() {
        Log.d("ListFragment", "showShoppingFood: Start")
        binding.progressBar.visibility = View.VISIBLE
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val shoppingFood: List<FoodEntity> = room.getFoodDao().getFoodByState(FoodState.SHOPPING.name)
                Log.d("lista","${shoppingFood}")
                withContext(Dispatchers.Main) {
                    Log.d("ListFragment", "showShoppingFood: Fetched ${shoppingFood.size} items")
                    if (shoppingFood.isEmpty()) {
                        Log.d("ListFragment", "No se encontraron alimentos en estado de compras")
                    } else {
                        Log.d("ListFragment", "Alimentos encontrados: ${shoppingFood.size}")
                    }
                    adapter.updateList(shoppingFood)
                    binding.progressBar.visibility = View.GONE
                }
            } catch (e: Exception) {
                Log.e("ListFragment", "Error fetching shopping food", e)
                withContext(Dispatchers.Main) {
                    binding.progressBar.visibility = View.GONE
                }
            }
        }
    }

    /**
     * Método para mostrar el diálogo de añadir alimento a la base de datos
     */
    private fun showFoodDialog(foodEntity: FoodEntity?) {
        val dialogBinding = DialogFoodBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogBinding.root)
            .setCancelable(true)
            .create()

        // Configurar el spinner de tipo de alimento
        val foodTypeAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            FoodType.values().map { it.typeName }
        )
        dialogBinding.spinnerFoodType.adapter = foodTypeAdapter

        // Configurar el spinner de estado de alimento
        val foodStateAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            FoodState.values().map { it.stateDescription }
        )
        dialogBinding.spinnerFoodState.adapter = foodStateAdapter

        //Modificar alimento
        if (foodEntity != null) {
            // Si el alimento no es null, configurar los valores actuales en el diálogo
            dialogBinding.editTextName.setText(foodEntity.name)
            dialogBinding.spinnerFoodType.setSelection(FoodType.values().indexOf(foodEntity.type))
            dialogBinding.editTextImageUrl.setText(foodEntity.image)
            dialogBinding.spinnerFoodState.setSelection(FoodState.values().indexOf(foodEntity.state))
            dialogBinding.buttonAddFood.setText("Actualizar alimento")
        }

        // Añadir un TextWatcher al campo de texto del nombre del alimento
        dialogBinding.editTextName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Llamar a la función para verificar si el nombre del alimento ya existe
                checkIfFoodExists(s.toString(), dialogBinding)
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        dialogBinding.buttonAddFood.setOnClickListener {
            val name = dialogBinding.editTextName.text.toString()
            val type = FoodType.values()[dialogBinding.spinnerFoodType.selectedItemPosition]
            var imageUrl = dialogBinding.editTextImageUrl.text.toString()
            val state = FoodState.values()[dialogBinding.spinnerFoodState.selectedItemPosition]

            if (imageUrl.isEmpty()) {
                imageUrl = defaultImageUrl
            }

            if (foodEntity == null) {
                addFoodToDatabase(name, type, imageUrl, state)
            } else {
                updateFoodInDatabase(foodEntity.id, name, type, imageUrl, state)
            }
            dialog.dismiss()
        }

        // Configurar el botón para cerrar el diálogo
        dialogBinding.btnCancel.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }


    private fun checkIfFoodExists(name: String, dialogBinding: DialogFoodBinding) {
        CoroutineScope(Dispatchers.IO).launch {
            val existingFood = room.getFoodDao().getFoodByName(name)
            withContext(Dispatchers.Main) {
                if (existingFood.isNotEmpty()) {
                    // Mostrar mensaje de error si el alimento ya existe
                    dialogBinding.editTextName.error = "El alimento ya existe en la base de datos"
                    dialogBinding.buttonAddFood.isEnabled = false
                } else {
                    dialogBinding.editTextName.error = null
                    dialogBinding.buttonAddFood.isEnabled = true
                }
            }
        }
    }

    private fun updateFoodInDatabase(id: Int, name: String, type: FoodType, imageUrl: String, state: FoodState) {
        CoroutineScope(Dispatchers.IO).launch {
            val updatedFood = FoodEntity(id = id, name = name, type = type, image = imageUrl, state = state)
            room.getFoodDao().update(updatedFood)
            val shoppingFood: List<FoodEntity> = room.getFoodDao().getFoodByState(FoodState.SHOPPING.name)
            withContext(Dispatchers.Main) {
                adapter.updateList(shoppingFood)
            }
        }
    }


    /**
     * Método para añadir un alimento de la base de datos
     */
    private fun addFoodToDatabase(name: String, type: FoodType, imageUrl: String, state: FoodState) {
        CoroutineScope(Dispatchers.IO).launch {
            val newFood = FoodEntity(name = name, type = type, image = imageUrl, state = state)
            room.getFoodDao().insertAll(listOf(newFood))
            val shoppingFood: List<FoodEntity> = room.getFoodDao().getFoodByState(FoodState.SHOPPING.name)
            withContext(Dispatchers.Main) {
                Log.d("ListFragment", "Nuevo alimento añadido: ${newFood.name}")
                Log.d("ListFragment", "Alimentos en estado de compras: ${shoppingFood.size}")
                adapter.updateList(shoppingFood)
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
                    updateFoodStateToPantry(foodEntity)
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
    private fun updateFoodStateToPantry(foodEntity: FoodEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            val updatedFood = foodEntity.copy(state = FoodState.PANTRY)
            room.getFoodDao().update(updatedFood)

            //Actualizo recyclerview
            val shoppingFood: List<FoodEntity> = room.getFoodDao().getFoodByState(FoodState.SHOPPING.name)
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
            val shoppingFood: List<FoodEntity> = room.getFoodDao().getFoodByState(FoodState.SHOPPING.name)
            withContext(Dispatchers.Main) {
                adapter.updateList(shoppingFood)
            }
        }
    }

    /**
     *************************************** DIALOGO BUSCAR ******************************
     */
    private fun showSearchDialog() {
        val dialogBinding = DialogSearchBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogBinding.root)
            .setCancelable(true)
            .create()

        val searchAdapter = SearchAdapter(onItemClick = { foodEntity ->
            handleFoodItemClick(foodEntity, dialog)
        })

        dialogBinding.rvFoodSearch.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = searchAdapter
        }

        dialogBinding.svSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
          override fun onQueryTextChange(newText: String?): Boolean {
              filterFoodList(newText, searchAdapter)
              return true
          }
        })
        // Cargar todos los alimentos inicialmente
        CoroutineScope(Dispatchers.IO).launch {
            val allFood: List<FoodEntity> = room.getFoodDao().getAllFood()
            withContext(Dispatchers.Main) {
                searchAdapter.updateList(allFood)
            }
        }

        // Configurar el botón para cerrar el diálogo
        dialogBinding.buttonExit.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun filterFoodList(query: String?, adapter: SearchAdapter) {
        CoroutineScope(Dispatchers.IO).launch {
            val filteredFood = if (query.isNullOrEmpty()) {
                room.getFoodDao().getAllFood() // Obtener todos los alimentos si el texto está vacío
            } else {
                room.getFoodDao().getFood("%$query%")
            }
            withContext(Dispatchers.Main) {
                adapter.updateList(filteredFood)
            }
        }
    }

    private fun handleFoodItemClick(foodEntity: FoodEntity, dialog: AlertDialog) {
        if (foodEntity.state == FoodState.NULL) {
            CoroutineScope(Dispatchers.IO).launch {
                val updatedFood = foodEntity.copy(state = FoodState.SHOPPING)
                room.getFoodDao().update(updatedFood)
                val allFood = room.getFoodDao().getAllFood()
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "${foodEntity.name} añadido a la lista de la compra", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                }

                //Actualizo recyclerview
                val shoppingFood: List<FoodEntity> = room.getFoodDao().getFoodByState(FoodState.SHOPPING.name)
                withContext(Dispatchers.Main) {
                    Log.d("ListFragment", "Alimento movido a PANTRY: ${updatedFood.name}")
                    adapter.updateList(shoppingFood)
                }
            }

        } else {
            Toast.makeText(requireContext(), "${foodEntity.name} ya está en uso.", Toast.LENGTH_SHORT).show()
        }
    }


    /**
     *************************************** DIALOGO ELIMINAR ******************************
     */

    private fun showDeleteDialog(foodEntity: FoodEntity) {
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Eliminar alimento")
            .setMessage("¿Estás seguro de que quieres eliminar ${foodEntity.name}?")
            .setPositiveButton("Eliminar") { _, _ ->
                deleteFoodFromDatabase(foodEntity)
                showShoppingFood() // Actualizar la lista después de eliminar
            }
            .setNegativeButton("Cancelar", null)
            .create()

        dialog.show()
    }

    /**
     * Método para borrar un alimento de la base de datos
     */
    private fun deleteFoodFromDatabase(foodEntity: FoodEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            room.getFoodDao().deleteFoodById(foodEntity.id)
            withContext(Dispatchers.Main) {
                showShoppingFood()
            }
        }
    }


}