package com.codeop.notes.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.codeop.notes.R
import com.codeop.notes.adapter.NotesAdapter
import com.codeop.notes.databinding.FragmentListBinding
import com.codeop.notes.repository.NotesRepository

class ListFragment(val onAddClick: () -> Unit) : Fragment(R.layout.fragment_list) {
    private lateinit var binding: FragmentListBinding

    private val notesAdapter: NotesAdapter
        get() = binding.notesList.adapter as NotesAdapter

    private val notesRepository: NotesRepository
        get() = NotesRepository.getInstance(requireContext())

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentListBinding.bind(view)

        binding.notesList.apply {
            adapter = NotesAdapter(notesRepository.getNotes()) {
                notesAdapter.apply {
                    val pos = items.indexOf(it)
                    items -= it

                    notifyItemRemoved(pos)
                }

                notesRepository.removeNote(it)
            }
        }

        binding.btnAdd.setOnClickListener {
            onAddClick()
        }
    }
}