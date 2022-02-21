package com.codeop.notes.view

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.codeop.notes.R
import com.codeop.notes.data.Note
import com.codeop.notes.databinding.FragmentAddBinding
import com.codeop.notes.repository.NotesRepository
import com.codeop.notes.utils.DrawableHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddFragment : Fragment(R.layout.fragment_add) {
    private lateinit var binding: FragmentAddBinding
    private var selectedColor: Note.Color = Note.Color.WHITE
    private var noteToEdit: Note? = null

    private val notesRepository: NotesRepository
        get() = NotesRepository.getInstance(requireContext())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.setTitle(R.string.title_add_note)

        val args: AddFragmentArgs by navArgs()
        noteToEdit = args.note
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAddBinding.bind(view)
        setNoteColor(Note.Color.WHITE)

        with(binding) {
            noteToEdit?.let {
                titleInput.setText(it.title)
                descriptionInput.setText(it.text)
                setNoteColor(it.color)
            }

            listOf(
                btnColorRed to Note.Color.RED,
                btnColorBlue to Note.Color.BLUE,
                btnColorYellow to Note.Color.YELLOW,
                btnColorGreen to Note.Color.GREEN,
                btnColorWhite to Note.Color.WHITE
            ).forEach {
                DrawableHelper.getTintedDrawable(requireContext(), it.second)?.let { drawable ->
                    it.first.background = drawable
                }
                onColorButtonClick(it.first, it.second)
            }

            btnSave.setOnClickListener {
                val title = binding.titleInput.text.toString()
                val description = binding.descriptionInput.text.toString()

                if (title.isNotBlank() && description.isNotBlank()) {
                    CoroutineScope(Dispatchers.IO).launch {
                        noteToEdit?.copy(
                            title = title,
                            text = description,
                            color = selectedColor
                        )?.let {
                            notesRepository.updateNote(it)
                        } ?: run {
                            notesRepository.saveNote(
                                Note.createNote(
                                    title,
                                    description,
                                    selectedColor
                                )
                            )
                        }
                    }
                }

                findNavController().navigateUp()
            }
        }
    }

    private fun onColorButtonClick(button: ImageButton, color: Note.Color) {
        button.setOnClickListener {
            setNoteColor(color)
        }
    }

    private fun setNoteColor(color: Note.Color) {
        val c = ContextCompat.getColor(
            requireContext(),
            Note.Color.getColorId(requireContext(), color)
        )
        binding.card.setCardBackgroundColor(c)
        selectedColor = color
    }
}