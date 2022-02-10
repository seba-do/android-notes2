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

    fun archiveNote(note: Note) {
        persistenceRepository.writeString(note.uid, note.copy(archived = true, pos = Int.MAX_VALUE).toString())
    }

    fun unarchiveNote(note: Note) {
        persistenceRepository.writeString(note.uid, note.copy(archived = false, pos = -1).toString())
    }

    fun updatePositions(list: List<Note>) {
        list.forEachIndexed { index, note ->
            saveNote(note.copy(pos = index))
        }
    }

    fun getActiveNotes(): List<Note> = persistenceRepository.getAllValues()
        .map { Note.fromString(it) }
        .filter { !it.archived }
        .sortedBy { it.pos }

    fun getArchivedNotes(): List<Note> = persistenceRepository.getAllValues()
        .map { Note.fromString(it) }
        .filter { it.archived }
        .sortedBy { it.pos }

    fun removeNote(note: Note) {
        persistenceRepository.removeEntry(note.uid)
    }
}