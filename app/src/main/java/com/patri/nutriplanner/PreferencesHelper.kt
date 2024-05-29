package com.patri.nutriplanner

import android.content.Context
import android.content.SharedPreferences


object PreferencesHelper {
    private const val PREFERENCES_FILE = "com.patri.nutriplanner.preferences"
    private const val KEY_USER_REGISTERED = "user_registered"

    // Obtener las preferencias compartidas
    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE)
    }

    // Establecer el estado de registro del usuario
    fun setUserRegistered(context: Context, isRegistered: Boolean) {
        val editor = getPreferences(context).edit()
        editor.putBoolean(KEY_USER_REGISTERED, isRegistered)
        editor.apply()
    }

    // Comprobar si el usuario está registrado
    fun isUserRegistered(context: Context): Boolean {
        return getPreferences(context).getBoolean(KEY_USER_REGISTERED, false)
    }

    // Borrar el estado de registro del usuario
    fun clearUserRegistration(context: Context) {
        val editor = getPreferences(context).edit()
        editor.remove(KEY_USER_REGISTERED)
        editor.apply()
    }
}
