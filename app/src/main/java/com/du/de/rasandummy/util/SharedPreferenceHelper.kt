package com.du.de.rasandummy.util

import android.content.Context
import com.du.de.rasandummy.db.Product
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SharedPreferenceHelper(private val context: Context) {

    private val NAME = "rasan_dummy_preference"

    private fun setString(key: String, value: String) {
        val editor = context.getSharedPreferences(NAME, Context.MODE_PRIVATE).edit()
        editor.putString(key, value).apply()
    }

    private fun getString(key: String): String {
        val value = context.getSharedPreferences(NAME, Context.MODE_PRIVATE).getString(key, "")
        return if (value.isNullOrEmpty()) "" else value
    }

    fun save(products: List<Product>) {
        val jsonString = Gson().toJson(products)
        setString(SharedPrefKey.RECENT, jsonString)
    }

    fun getProducts(): List<Product> {
        val jsonString = getString(SharedPrefKey.RECENT)
        if (jsonString == null || jsonString == "") {
            return ArrayList()
        }
        val myType = object : TypeToken<List<Product>>() {}.type
        return Gson().fromJson(jsonString, myType)
    }

    fun clear() {
        val editor = context.getSharedPreferences(NAME, Context.MODE_PRIVATE).edit();
        editor.clear().apply()
    }

    class SharedPrefKey {
        companion object {
            const val RECENT = "recent"
        }
    }
}
