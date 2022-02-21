package com.codeop.notes.db

import androidx.room.*
import com.codeop.notes.data.Note

@Dao
interface NoteDao {
    @Query("SELECT * FROM Note")
    fun getAll(): List<Note>

    @Update(entity = Note::class)
    fun update(note: Note)

    @Insert
    fun insert(note: Note)

    @Delete
    fun delete(note: Note)
}