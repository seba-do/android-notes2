package com.codeop.notes.list.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.codeop.notes.data.Note
import com.codeop.notes.repository.AppConfigRepository
import com.codeop.notes.repository.NotesRepository
import kotlinx.coroutines.launch

class ListViewModel(application: Application) : AndroidViewModel(application) {

    private val notesRepository: NotesRepository = NotesRepository.getInstance(application)
    private val appConfigRepository: AppConfigRepository =
        AppConfigRepository.getInstance(application)

    private val isArchiveVisible: MutableLiveData<Boolean> = MutableLiveData(appConfigRepository.isArchivedVisible)
    private val _notes: LiveData<List<Note>> = notesRepository.allNotes.asLiveData()

    val notes: LiveData<List<Note>> = Transformations.map(_notes) {
        if (isArchiveVisible.value == true) {
            it
        } else {
            it.filter { !it.archived }
        }
    }

    private var areArchivedVisible: Boolean = appConfigRepository.isArchivedVisible

    fun removeNote(note: Note) = viewModelScope.launch {
        notesRepository.removeNote(note)
    }

    fun switchArchived(note: Note) = viewModelScope.launch {
        if (note.archived) {
            notesRepository.unarchiveNote(note)
        } else {
            notesRepository.archiveNote(note)
        }
    }

    fun switchArchivedVisibility() {
        appConfigRepository.isArchivedVisible = !appConfigRepository.isArchivedVisible
        areArchivedVisible = appConfigRepository.isArchivedVisible

        isArchiveVisible.value = appConfigRepository.isArchivedVisible
    }

    fun updatePositions(list: List<Note>) = viewModelScope.launch {
        notesRepository.updatePositions(list)
    }
}