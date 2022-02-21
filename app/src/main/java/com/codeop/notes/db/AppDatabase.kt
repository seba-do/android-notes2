package com.codeop.notes.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.codeop.notes.data.Note

@Database(entities = [Note::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
}

object DB {
    private var instance: AppDatabase? = null

    fun getInstance(context: Context) =
        instance ?: Room.databaseBuilder(context, AppDatabase::class.java, "notes-db").build()
            .also { instance = it }
}