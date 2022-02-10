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

    fun writeInt(key: String, value: Int) = with(sharedPrefs.edit()) {
        putInt(key, value)
        commit()
    }

    fun readInt(key: String) = sharedPrefs.getInt(key, 0)

    fun readBoolean(key: String) = sharedPrefs.getBoolean(key, false)

    fun writeBoolean(key: String, value: Boolean) = with(sharedPrefs.edit()) {
        putBoolean(key, value)
        commit()
    }
}