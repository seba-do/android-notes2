package com.codeop.notes.repository

import android.content.Context
import android.content.SharedPreferences

class PersistenceRepository(context: Context, dbName: String) {

    private val sharedPrefs: SharedPreferences =
        context.getSharedPreferences(dbName, Context.MODE_PRIVATE)

    fun writeString(key: String, value: String) = with(sharedPrefs.edit()) {
        putString(key, value)
        commit()
    }

    fun getAllValues(): List<String> = sharedPrefs.all.mapNotNull { it.value as? String }

    fun removeEntry(key: String) = with(sharedPrefs.edit()) {
        remove(key)
        commit()
    }
}