package com.arkademy.peworld.utils.sharedpreference

import android.content.Context
import android.content.SharedPreferences

class PreferenceHelper(context: Context) {

    private var sharedPreferences: SharedPreferences
    private var PREFERENCE_NAME = "PREFERENCE_NAME"
    var editor : SharedPreferences.Editor


    init {
        sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
    }

    fun putString(key: String, value: String?){
        editor.putString(key, value)
            .apply()
    }

    fun putBoolean(key: String, value: Boolean){
        editor.putBoolean(key, value)
            .apply()
    }
    fun putInt(key: String, value: Int) {
        editor.putInt(key, value)
    }

    fun getString(key: String): String? {
        return sharedPreferences.getString(key, null)
    }

    fun getInt(key: String): Int? {
        return sharedPreferences.getInt(key, 0)
    }

    fun getBoolean(key: String): Boolean? {
        return sharedPreferences.getBoolean(key, false)
    }

    fun clear() {
        editor.clear().apply()
    }
}