package com.codeop.notes.db

import androidx.room.*
import com.codeop.notes.data.Note
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Query("SELECT * FROM Note")
    fun getAll(): Flow<List<Note>>

    @Update(entity = Note::class)
    fun update(note: Note)

    @Insert
    fun insert(note: Note)

    @Delete
    fun delete(note: Note)
}