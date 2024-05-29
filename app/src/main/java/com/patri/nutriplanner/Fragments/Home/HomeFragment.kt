package com.patri.nutriplanner.Fragments.Home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.patri.nutriplanner.FoodDatabase
import com.patri.nutriplanner.MainActivity
import com.patri.nutriplanner.Fragments.List.ListFragment
import com.patri.nutriplanner.Fragments.Pantry.PantryFragment
import com.patri.nutriplanner.R
import com.patri.nutriplanner.databinding.FragmentHomeBinding
import com.patri.nutriplanner.Fragments.Planning.PlanningFragment
import com.patri.nutriplanner.Fragments.Settings.SettingsFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.random.Random

class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var room: FoodDatabase

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)

        room = FoodDatabase.getDatabase(requireContext())

        // Muestra una frase aleatoria sobre alimentación saludable
        val randomIndex = Random.nextInt(healthyQuotes.size)
        binding.tvQuote.text = healthyQuotes[randomIndex]

        lifecycleScope.launch(Dispatchers.IO) {
            val user = room.getUserDao().getAllUsers().firstOrNull()
            withContext(Dispatchers.Main) {
                if (user != null) {
                    binding.tvHome.text = "¡Hola ${user.name}!\n¿Qué quieres consultar?"
                }
            }
        }
        binding.cvShoppingList.setOnClickListener {
            setCurrentFragment(ListFragment(), R.id.nav_shoppingList)
        }

        binding.cvPantry.setOnClickListener {
            setCurrentFragment(PantryFragment(), R.id.nav_pantry)
        }

        binding.cvPlanning.setOnClickListener {
            setCurrentFragment(PlanningFragment(), R.id.nav_planning)
        }

        binding.cvSettings.setOnClickListener {
            setCurrentFragment(SettingsFragment(), R.id.nav_settings)
        }
    }
    private fun setCurrentFragment(fragment: Fragment, itemId: Int) {
        parentFragmentManager.beginTransaction().apply {
            replace(R.id.containerView, fragment)
            addToBackStack(null)  // Si deseas agregar la transacción a la pila de retroceso
            commit()
        }
        (activity as? MainActivity)?.selectBottomNavItem(itemId)
    }

    private val healthyQuotes = listOf(
        "Comer saludable es una forma de vida, no una dieta.",
        "Incluye más frutas y verduras en tu dieta diaria.",
        "Hidrátate adecuadamente bebiendo suficiente agua.",
        "Evita los alimentos procesados y elige opciones naturales.",
        "Consume proteínas de calidad en cada comida.",
        "Modera el consumo de azúcar y sal.",
        "Elige grasas saludables como el aceite de oliva y los frutos secos.",
        "Come porciones adecuadas y evita el exceso.",
        "Planifica tus comidas para una mejor nutrición.",
        "Disfruta de una variedad de alimentos para obtener todos los nutrientes necesarios.",
        "Haz ejercicio regularmente para complementar una dieta saludable.",
        "Evita las bebidas azucaradas y opta por infusiones o agua.",
        "Practica la gratitud y el mindfulness en cada comida.",
        "Lee las etiquetas nutricionales para hacer elecciones informadas.",
        "Incorpora alimentos ricos en fibra como legumbres y cereales integrales.",
        "Prefiere cocinar en casa para tener control sobre los ingredientes.",
        "Mantén horarios regulares para las comidas y meriendas.",
        "Reduce el consumo de carnes rojas y opta por pescados y legumbres.",
        "Asegúrate de dormir lo suficiente para una buena salud general.",
        "Escucha a tu cuerpo y come cuando tengas hambre, no por aburrimiento."
    )


}