package com.patri.nutriplanner


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.patri.nutriplanner.User.UserActivity
import com.patri.nutriplanner.Fragments.Home.HomeFragment
import com.patri.nutriplanner.Fragments.Settings.SettingsFragment
import com.patri.nutriplanner.Fragments.List.ListFragment
import com.patri.nutriplanner.Fragments.Pantry.PantryFragment
import com.patri.nutriplanner.database.entities.toDatabase
import com.patri.nutriplanner.databinding.ActivityMainBinding
import com.patri.nutriplanner.Fragments.Planning.PlanningFragment
import com.patri.nutriplanner.Fragments.Planning.PlanningProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var room: FoodDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        room = FoodDatabase.getDatabase(this)

        // Aplicar el modo oscuro si está habilitado
        CoroutineScope(Dispatchers.Main).launch {
            val isDarkMode = DataStoreManager.isDarkModeEnabled(this@MainActivity)
            if (isDarkMode) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        // Verificar si el usuario ya está registrado
        if (!PreferencesHelper.isUserRegistered(this)) {// Si no está registrado, redirigir a UserActivity

            //Inicializo la base de datos
            CoroutineScope(Dispatchers.IO).launch {
                fillDatabase()
            }

            val intent = Intent(this, UserActivity::class.java)
            startActivity(intent)
            finish() // Finaliza esta actividad para que no se pueda volver a ella
            return

        } else {//SI ESTÁ REGISTRADO...
            //configurar el menú de navegación
                    setupBottomNavigationBar()

        }
    }

    private fun setupBottomNavigationBar() {
        val homeFragment = HomeFragment()
        val listFragment = ListFragment()
        val pantryFragment = PantryFragment()
        val planningFragment = PlanningFragment()
        val settingsFragment = SettingsFragment()

        setCurrentFragment(homeFragment)

        binding.bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> setCurrentFragment(homeFragment)
                R.id.nav_shoppingList -> setCurrentFragment(listFragment)
                R.id.nav_pantry -> setCurrentFragment(pantryFragment)
                R.id.nav_planning -> setCurrentFragment(planningFragment)
                R.id.nav_settings -> setCurrentFragment(settingsFragment)
            }
            true
        }
    }

    private fun setCurrentFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.containerView, fragment)
            commit()
        }
    }

    fun selectBottomNavItem(itemId: Int) {
        binding.bottomNavigationView.selectedItemId = itemId
    }

    private suspend fun fillDatabase(){

            val foodList = FoodProvider.getFood().map { it.toDatabase() }
            room.getFoodDao().deleteAllFoodList()
            room.getFoodDao().insertAll(foodList)
            Log.d("ListFragment", "Alimentos encontrados: ${room.getFoodDao().getAllFood()}")

            val planningList = PlanningProvider.getWeek().map { it.toDatabase() }
            room.getPlanningDao().deleteAllPlanningList()
            room.getPlanningDao().insertOrUpdate(planningList)
            Log.d("MainActivity", "Datos de planificación cargados: ${room.getPlanningDao().getAllDays()}")


    }
}