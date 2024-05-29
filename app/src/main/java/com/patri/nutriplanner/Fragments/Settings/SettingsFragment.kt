package com.patri.nutriplanner.Fragments.Settings

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import com.patri.nutriplanner.DataStoreManager
import com.patri.nutriplanner.FoodDatabase
import com.patri.nutriplanner.PreferencesHelper
import com.patri.nutriplanner.User.UserActivity
import com.patri.nutriplanner.databinding.FragmentSettingsBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SettingsFragment : Fragment() {


    private lateinit var binding: FragmentSettingsBinding
    private lateinit var room: FoodDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        room = FoodDatabase.getDatabase(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Load saved dark mode preference
        lifecycleScope.launch {
            val isDarkMode = DataStoreManager.isDarkModeEnabled(requireContext())
            binding.switchDarkMode.isChecked = isDarkMode
            if (isDarkMode) enableDarkMode() else disableDarkMode()
        }

        binding.LInfoUser.setOnClickListener {
            val intent = Intent(requireContext(), UserActivity::class.java)
            startActivity(intent)
        }

        binding.switchDarkMode.setOnCheckedChangeListener { _, value ->
            // Guardar preferencia en DataStore
            lifecycleScope.launch {
                DataStoreManager.setDarkMode(requireContext(), value)
            }

            // Retrasar el cambio de modo oscuro para permitir la animación del Switch
            postponeEnterTransition()
            Handler(Looper.getMainLooper()).postDelayed({
                if (value) {
                    enableDarkMode()
                } else {
                    disableDarkMode()
                }
                startPostponedEnterTransition()

            }, 300) // Retardo de 300ms para permitir la animación del Switch
        }

        binding.LClearApp.setOnClickListener {
            showClearConfirmationDialog()
        }

    }

    private fun enableDarkMode() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
    }

    private fun disableDarkMode() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }

    private fun clearAppData() {
        lifecycleScope.launch(Dispatchers.IO) {
            //room.clearAllTables()
            room.getUserDao().deleteAllUsers()
            room.getPlanningDao().clearPlanningColumns()
            room.getFoodDao().deleteAllFoodList()

            // Limpiar estado de registro del usuario
            withContext(Dispatchers.Main) {
                PreferencesHelper.clearUserRegistration(requireContext())
            }
        }
    }


    private fun showClearConfirmationDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Confirmación requerida")
        builder.setMessage("¿Estás seguro de que quieres vaciar la aplicación? Esto eliminará todos los datos almacenados." +
                "\n\nEs una acción PERMANENTE e IRREVERSIBLE.")
        builder.setPositiveButton("Vaciar aplicación") { _, _ ->
            clearAppData()
        }
        builder.setNegativeButton("Cancelar") { dialog, _ ->
            dialog.dismiss()
        }
        builder.show()
    }
}
