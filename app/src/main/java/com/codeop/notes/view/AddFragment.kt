package com.codeop.notes.view

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.codeop.notes.R
import com.codeop.notes.data.Note
import com.codeop.notes.databinding.FragmentAddBinding
import com.codeop.notes.repository.NotesRepository

class AddFragment(val onSaveClick: () -> Unit) : Fragment(R.layout.fragment_add) {
    private lateinit var binding: FragmentAddBinding
    private var selectedColor: Note.Color = Note.Color.WHITE

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAddBinding.bind(view)

        with(binding) {

            listOf(
                btnColorRed to Note.Color.RED,
                btnColorBlue to Note.Color.BLUE,
                btnColorYellow to Note.Color.YELLOW,
                btnColorGreen to Note.Color.GREEN,
                btnColorWhite to Note.Color.WHITE
            ).forEach {
                onColorButtonClick(it.first, it.second)
            }

            btnSave.setOnClickListener {
                val title = binding.titleInput.text.toString()
                val description = binding.descriptionInput.text.toString()

                if (title.isNotBlank() && description.isNotBlank()) {
                    NotesRepository.getInstance(requireContext())
                        .addNewNote(Note.createNote(title, description, selectedColor))
                }

                onSaveClick()
            }
        }
    }

    private fun onColorButtonClick(button: ImageButton, color: Note.Color) {
        button.setOnClickListener {
            val c = ContextCompat.getColor(requireContext(), color.colorId)
            binding.card.setCardBackgroundColor(c)
            selectedColor = color
        }
    }
}