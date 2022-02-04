package com.codeop.notes.repository

import android.content.Context
import com.codeop.notes.data.Note

class NotesRepository private constructor(context: Context) {
    companion object {
        private const val NOTES_DB = "notes-db"
        private var instance: NotesRepository? = null

        fun getInstance(context: Context): NotesRepository {
            return instance ?: run {
                NotesRepository(context).also { instance = it }
            }
        }
    }

    private val persistenceRepository = PersistenceRepository(context, NOTES_DB)

    fun saveNote(note: Note) {
        persistenceRepository.writeString(note.uid, note.toString())
    }

    fun updatePositions(list: List<Note>) {
        list.forEachIndexed { index, note ->
            saveNote(note.copy(pos = index))
        }
    }

    fun getNotes(): List<Note> = persistenceRepository.getAllValues().map {
        Note.fromString(it)
    }.sortedBy { it.pos }

    fun removeNote(note: Note) {
        persistenceRepository.removeEntry(note.uid)
    }
}