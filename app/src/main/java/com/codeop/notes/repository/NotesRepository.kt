package com.codeop.notes.repository

import android.content.Context
import com.codeop.notes.data.Note
import com.codeop.notes.db.DB

class NotesRepository private constructor(context: Context) {
    companion object {
        private var instance: NotesRepository? = null

        fun getInstance(context: Context): NotesRepository {
            return instance ?: run {
                NotesRepository(context).also { instance = it }
            }
        }
    }

    private val db = DB.getInstance(context)

    fun saveNote(note: Note) {
        db.noteDao().insert(note)
    }

    fun updateNote(note: Note) {
        db.noteDao().update(note)
    }

    fun archiveNote(note: Note) {
        db.noteDao().update(note.copy(archived = true, pos = Int.MAX_VALUE))
    }

    fun unarchiveNote(note: Note) {
        db.noteDao().update(note.copy(archived = false, pos = -1))
    }

    fun updatePositions(list: List<Note>) {
        list.forEachIndexed { index, note ->
            db.noteDao().update(note.copy(pos = index))
        }
    }

    fun getNotes(): List<Note> = db.noteDao().getAll()

    fun getActiveNotes(): List<Note> = db.noteDao().getAll()
        .filter { !it.archived }
        .sortedBy { it.pos }

    fun getArchivedNotes(): List<Note> = db.noteDao().getAll()
        .filter { it.archived }
        .sortedBy { it.pos }

    fun removeNote(note: Note) {
        db.noteDao().delete(note)
    }
}