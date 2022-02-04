package com.codeop.notes.repository

import android.content.Context
import com.codeop.notes.data.LayoutType

class AppConfigRepository private constructor(context: Context) {
    companion object {
        private const val APP_CONFIG_DB = "app-config-db"
        private const val LAYOUT_TYPE_KEY = "layout-type"

        private var instance: AppConfigRepository? = null

        fun getInstance(context: Context): AppConfigRepository {
            return instance ?: run {
                AppConfigRepository(context).also { instance = it }
            }
        }
    }

    private val persistenceRepository = PersistenceRepository(context, APP_CONFIG_DB)

    fun saveSelectedLayoutType(type: LayoutType) {
        persistenceRepository.writeInt(LAYOUT_TYPE_KEY, type.ordinal)
    }

    fun getSelectedLayoutType(): LayoutType {
        return LayoutType.values()[persistenceRepository.readInt(LAYOUT_TYPE_KEY)]
    }
}