package com.codeop.notes.repository

import android.content.Context
import com.codeop.notes.data.LayoutType
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf

class AppConfigRepository: KoinComponent {
    companion object {
        private const val APP_CONFIG_DB = "app-config-db"
        private const val LAYOUT_TYPE_KEY = "layout-type"
        private const val ARCHIVED_VISIBLE_KEY = "archived-visible"
    }

    private val persistenceRepository: PersistenceRepository by inject { parametersOf(APP_CONFIG_DB)}

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