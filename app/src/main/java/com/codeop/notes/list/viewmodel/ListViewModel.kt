package com.codeop.notes.list.viewmodel

import androidx.lifecycle.*
import com.codeop.notes.data.Note
import com.codeop.notes.repository.AppConfigRepository
import com.codeop.notes.repository.NotesRepository
import kotlinx.coroutines.launch

class ListViewModel(
    private val notesRepository: NotesRepository,
    private val appConfigRepository: AppConfigRepository
) : ViewModel() {

    private val isArchiveVisible: MutableLiveData<Boolean> =
        MutableLiveData(appConfigRepository.isArchivedVisible)

    private val _notes: LiveData<List<Note>> = notesRepository.allNotes.asLiveData()

    val notesOutput = MediatorLiveData<List<Note>>()

    init {
        notesOutput.addSource(isArchiveVisible) {
            setOutputNotes()
        }

        notesOutput.addSource(_notes) {
            setOutputNotes()
        }
    }

    private fun setOutputNotes() {
        notesOutput.value = if (isArchiveVisible.value == true) {
            _notes.value ?: emptyList()
        } else {
            _notes.value?.filter { !it.archived } ?: emptyList()
        }
    }

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

        isArchiveVisible.value = appConfigRepository.isArchivedVisible
    }

    fun updatePositions(list: List<Note>) = viewModelScope.launch {
        notesRepository.updatePositions(list)
    }
}