package com.codeop.notes.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.codeop.notes.data.Note

@Database(entities = [Note::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
}