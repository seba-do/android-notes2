package com.codeop.notes.repository

import android.content.Context
import com.codeop.notes.data.LayoutType

class AppConfigRepository private constructor(context: Context) {
    companion object {
        private const val APP_CONFIG_DB = "app-config-db"
        private const val LAYOUT_TYPE_KEY = "layout-type"
        private const val ARCHIVED_VISIBLE_KEY = "archived-visible"

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

    var isArchivedVisible: Boolean
        get() = persistenceRepository.readBoolean(ARCHIVED_VISIBLE_KEY)
        set(value) {
            persistenceRepository.writeBoolean(ARCHIVED_VISIBLE_KEY, value)
        }
}