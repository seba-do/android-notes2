package com.codeop.notes.repository

import android.content.Context
import com.codeop.notes.data.Note
import com.codeop.notes.db.DB
import com.codeop.notes.db.NoteDao
import kotlinx.coroutines.flow.Flow

class NotesRepository private constructor(private val noteDao: NoteDao) {
    companion object {
        private var instance: NotesRepository? = null

        fun getInstance(context: Context): NotesRepository {
            return instance ?: run {
                NotesRepository(DB.getInstance(context).noteDao()).also { instance = it }
            }
        }
    }

    val allNotes: Flow<List<Note>> = noteDao.getAll()

    suspend fun saveNote(note: Note) {
        noteDao.insert(note)
    }

    suspend fun updateNote(note: Note) {
        noteDao.update(note)
    }

    suspend fun archiveNote(note: Note) {
        noteDao.update(note.copy(archived = true, pos = Int.MAX_VALUE))
    }

    suspend fun unarchiveNote(note: Note) {
        noteDao.update(note.copy(archived = false, pos = -1))
    }

    suspend fun updatePositions(list: List<Note>) {
        list.forEachIndexed { index, note ->
            noteDao.update(note.copy(pos = index))
        }
    }

    suspend fun removeNote(note: Note) {
        noteDao.delete(note)
    }
}