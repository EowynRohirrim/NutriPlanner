package com.patri.nutriplanner.Fragments.Planning

import PlanningAdapter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.patri.nutriplanner.FoodDatabase
import com.patri.nutriplanner.database.entities.PlanningEntity
import com.patri.nutriplanner.databinding.FragmentPlanningBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlanningFragment : Fragment() {

    private lateinit var binding: FragmentPlanningBinding
    private lateinit var adapter: PlanningAdapter
    private lateinit var room: FoodDatabase // Instancia objeto Room

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlanningBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        room = FoodDatabase.getDatabase(requireContext())

        initUI()
        loadData()

        // Set up FAB refresh action
        binding.fabRefresh.setOnClickListener {
            clearPlanningData()
        }
    }

    private fun initUI() {
        binding.rvDays.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
        adapter = PlanningAdapter(onTextChanged = { planningEntity ->
            saveData(planningEntity)
        })
        binding.rvDays.adapter = adapter
    }

    private fun loadData() {
        lifecycleScope.launch(Dispatchers.IO) {
            val planningList = room.getPlanningDao().getAllDays()
            Log.d("PlanningFragment", "Loaded data: $planningList")
            withContext(Dispatchers.Main) {
                adapter.updateList(planningList)
            }
        }
    }

    /**
     * Detecta cuando el usuario abandona el fragment
     */
    override fun onPause() {
        super.onPause()
        saveData(adapter.getList())
    }

    /**
     * Método para guardar una lista de PlanningEntity.
     */
    private fun saveData(planningList: List<PlanningEntity>) {
        lifecycleScope.launch(Dispatchers.IO) {
            Log.d("PlanningFragment", "Saving data: $planningList")
            room.getPlanningDao().insertOrUpdate(planningList)
        }
    }


    /**
     * Método para guardar un único PlanningEntity.
     */
    private fun saveData(planningEntity: PlanningEntity) {
        lifecycleScope.launch(Dispatchers.IO) {
            Log.d("PlanningFragment", "Saving single entity: $planningEntity")
            room.getPlanningDao().insertOrUpdate(listOf(planningEntity))
        }
    }

    private fun clearPlanningData() {
        lifecycleScope.launch(Dispatchers.IO) {
            // Obtener la lista actual de planificación
            val planningList = room.getPlanningDao().getAllDays()

            // Actualizar cada elemento para que los EditText se vacíen
            val updatedPlanningList = planningList.map {
                it.copy(
                    breakfast = "",
                    snack = "",
                    lunch = "",
                    snack2 = "",
                    dinner = ""
                )
            }

            // Guardar la lista actualizada en la base de datos
            room.getPlanningDao().insertOrUpdate(updatedPlanningList)

            withContext(Dispatchers.Main) {
                // Actualizar la lista en el adaptador para reflejar los cambios en la UI
                adapter.updateList(updatedPlanningList)
            }
        }
    }
}
