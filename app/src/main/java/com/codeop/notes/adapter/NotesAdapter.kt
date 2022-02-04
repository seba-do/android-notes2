package com.codeop.notes.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.codeop.notes.data.Note
import com.codeop.notes.databinding.VhNoteBinding

class NotesAdapter(
    private val onItemClick: (Note) -> Unit,
    private val onDeleteClick: (Note) -> Unit
) : ListAdapter<Note, NotesAdapter.NotesViewHolder>(NotesDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        val binding = VhNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NotesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        holder.bind(getItem(position), onItemClick, onDeleteClick)
    }

    class NotesViewHolder(
        private val binding: VhNoteBinding,
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            note: Note,
            onItemClick: (Note) -> Unit,
            onDeleteClick: (Note) -> Unit
        ) {
            itemView.setOnClickListener {
                onItemClick(note)
            }

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

    class NotesDiffUtil : androidx.recyclerview.widget.DiffUtil.ItemCallback<Note>() {
        override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem.uid == newItem.uid
        }

        override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem == newItem
        }
    }
}