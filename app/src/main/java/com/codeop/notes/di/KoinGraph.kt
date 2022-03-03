package com.codeop.notes.di

import androidx.room.Room
import com.codeop.notes.add.viewmodel.AddViewModel
import com.codeop.notes.db.AppDatabase
import com.codeop.notes.list.viewmodel.ListViewModel
import com.codeop.notes.repository.AppConfigRepository
import com.codeop.notes.repository.NotesRepository
import com.codeop.notes.repository.PersistenceRepository
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object KoinGraph {
    val mainModules = module {
        single { Room.databaseBuilder(get(), AppDatabase::class.java, "notes-db").build() }
        single { get<AppDatabase>().noteDao() }
        single { NotesRepository(get()) }
        single { AppConfigRepository() }

        factory { parameters -> PersistenceRepository(get(), parameters.get()) }


        viewModel { ListViewModel(get(), get()) }
        viewModel { AddViewModel(get()) }
    }
}