package com.codeop.notes.add.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codeop.notes.data.Note
import com.codeop.notes.repository.NotesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddViewModel(private val notesRepository: NotesRepository) : ViewModel() {

    fun saveNote(note: Note?, title: String, text: String, color: Note.Color) =
        viewModelScope.launch(Dispatchers.IO) {
            note?.copy(
                title = title,
                text = text,
                color = color
            )?.let {
                notesRepository.updateNote(it)
            } ?: run {
                notesRepository.saveNote(
                    Note.createNote(
                        title = title,
                        description = text,
                        color = color
                    )
                )
            }
        }
}