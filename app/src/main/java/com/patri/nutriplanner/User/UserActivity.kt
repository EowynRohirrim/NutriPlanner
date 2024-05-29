package com.patri.nutriplanner.User

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.patri.nutriplanner.FoodDatabase
import com.patri.nutriplanner.MainActivity
import com.patri.nutriplanner.PreferencesHelper
import com.patri.nutriplanner.R
import com.patri.nutriplanner.database.entities.UserEntity
import com.patri.nutriplanner.databinding.ActivityUserBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.DecimalFormat
class UserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserBinding
    private var isMaleSelected: Boolean = true
    private var isFemaleSelected: Boolean = false
    private var currentAge: Int = 30
    private lateinit var room: FoodDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializar el objeto room usando el singleton
        room = FoodDatabase.getDatabase(this)

        // Comentar o eliminar la sección de borrado de usuarios
        // lifecycleScope.launch(Dispatchers.IO) {
        //     room.getUserDao().deleteAllUsers()
        // }

        loadUserData()
        initComponents()
        initListeners()
    }

    private fun initComponents() {
        binding.rsAge.values = listOf(currentAge.toFloat())
        updateGenderSelection()
    }

    private fun initListeners() {
        binding.rsAge.addOnChangeListener { slider, value, fromUser ->
            currentAge = value.toInt()
            binding.tvAge.text = currentAge.toString()
        }

        binding.viewMale.setOnClickListener {
            isMaleSelected = true
            isFemaleSelected = false
            updateGenderSelection()
        }

        binding.viewFemale.setOnClickListener {
            isMaleSelected = false
            isFemaleSelected = true
            updateGenderSelection()
        }

        binding.btnGuardar.setOnClickListener {
            val name = binding.etNombre.text.toString()
            val gender = if (isMaleSelected) "Male" else "Female"

            if (name.isNotEmpty()) {
                saveUserToDatabase(name, currentAge, gender)
            } else {
                Toast.makeText(this, "Por favor, ingrese su nombre", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun updateGenderSelection() {
        if (isMaleSelected) {
            binding.viewMale.setCardBackgroundColor(ContextCompat.getColor(this, R.color.blueaquamarine))
            binding.viewFemale.setCardBackgroundColor(ContextCompat.getColor(this, R.color.bluedark))
        } else {
            binding.viewMale.setCardBackgroundColor(ContextCompat.getColor(this, R.color.bluedark))
            binding.viewFemale.setCardBackgroundColor(ContextCompat.getColor(this, R.color.blueaquamarine))
        }
    }

    private fun saveUserToDatabase(name: String, age: Int, gender: String) {
        val user = UserEntity(name = name, age = age.toString(), gender = gender)

        Log.d("UserActivity", "Saving user: $user") // Añade este log


        lifecycleScope.launch(Dispatchers.IO) {
            try {
                room.getUserDao().deleteAllUsers() // Eliminar usuarios existentes
                room.getUserDao().insertUser(user)
                PreferencesHelper.setUserRegistered(this@UserActivity, true)
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@UserActivity, "Usuario guardado con éxito", Toast.LENGTH_LONG).show()
                    Log.d("User", "Usuario añadido: ${user.name}")
                    navigateToMainActivity()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@UserActivity, "Error al guardar el usuario", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun loadUserData() {
        lifecycleScope.launch(Dispatchers.IO) {
            val user = room.getUserDao().getUser()

            Log.d("UserActivity", "Loaded user: $user") // Añade este log

            user?.let {
                withContext(Dispatchers.Main) {
                    binding.etNombre.setText(it.name)
                    currentAge = it.age.toInt()
                    binding.rsAge.values = listOf(currentAge.toFloat())
                    binding.tvAge.text = it.age
                    isMaleSelected = it.gender == "Male"
                    updateGenderSelection()
                }
            }
        }
    }
}
