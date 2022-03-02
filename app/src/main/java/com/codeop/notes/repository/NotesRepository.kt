package com.codeop.notes.repository

import com.codeop.notes.data.Note
import com.codeop.notes.db.NoteDao
import kotlinx.coroutines.flow.Flow

class NotesRepository(private val noteDao: NoteDao) {

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