package com.codeop.notes.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.codeop.notes.data.Note
import com.codeop.notes.databinding.VhNoteBinding

class NotesAdapter(
    var items: List<Note>,
    private val onDeleteClick: (Note) -> Unit
) : RecyclerView.Adapter<NotesAdapter.NotesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        val binding = VhNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NotesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        holder.bind(items[position], onDeleteClick)
    }

    override fun getItemCount() = items.size

    class NotesViewHolder(
        private val binding: VhNoteBinding,
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(note: Note, onDeleteClick: (Note) -> Unit) {
            with(binding) {
                root.setCardBackgroundColor(
                    ContextCompat.getColor(root.context, note.color.colorId)
                )
                noteTitle.text = note.title
                noteDescription.text = note.text

                btnDelete.setOnClickListener {
                    onDeleteClick(note)
                }
            }
        }
    }
}