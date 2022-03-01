package com.codeop.notes.list.adapter

import android.animation.ValueAnimator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.animation.AccelerateInterpolator
import androidx.core.animation.addListener
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.codeop.notes.R
import com.codeop.notes.data.Note
import com.codeop.notes.databinding.VhNoteBinding

class NotesAdapter(
    private val onDeleteClick: (Note) -> Unit,
    private val onArchiveClick: (Note) -> Unit,
    private val onEditClick: (Note) -> Unit
) : ListAdapter<Note, NotesAdapter.NotesViewHolder>(NotesDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        val binding = VhNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NotesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        holder.bind(getItem(position), onDeleteClick, onArchiveClick, onEditClick)
    }

    class NotesViewHolder(
        private val binding: VhNoteBinding,
    ) :
        RecyclerView.ViewHolder(binding.root) {
        private var isExpanded = false

        fun bind(
            note: Note,
            onDeleteClick: (Note) -> Unit,
            onArchiveClick: (Note) -> Unit,
            onEditClick: (Note) -> Unit
        ) {

            with(binding) {
                val bgColor = ContextCompat.getColor(
                    root.context,
                    Note.Color.getColorId(root.context, note.color)
                )

                root.setCardBackgroundColor(bgColor)
                noteTitle.text = note.title
                noteDescription.text = note.text

                archivedLabel.isVisible = note.archived
                btnArchive.setImageResource(if(note.archived) R.drawable.ic_unarchive else R.drawable.ic_archive)

                itemView.setOnClickListener {
                    btnGroup.isVisible = !btnGroup.isVisible
                    isExpanded = if (isExpanded) {
                        btnWrapper.collapse()
                        false
                    } else {
                        btnWrapper.expand()
                        true
                    }
                }

                btnDelete.setOnClickListener {
                    onDeleteClick(note)
                }

                btnArchive.setOnClickListener {
                    onArchiveClick(note)
                }

                btnEdit.setOnClickListener {
                    onEditClick(note)
                }
            }
        }

        private fun View.collapse() {
            this.measure(MATCH_PARENT, WRAP_CONTENT)
            val targetHeight = this.measuredHeight

            animate(this, targetHeight, 0)
        }

        private fun View.expand() {
            this.measure(MATCH_PARENT, WRAP_CONTENT)
            val targetHeight = this.measuredHeight

            animate(this, 0, targetHeight)
        }

        private fun animate(view: View, from: Int, to: Int) {
            view.layoutParams = view.layoutParams.apply { height = 0 }
            view.isVisible = true

            ValueAnimator.ofInt(from, to).apply {
                interpolator = AccelerateInterpolator()
                duration = 300
                addUpdateListener {
                    view.layoutParams = view.layoutParams.apply {
                        height = if (from > to) {
                            (from * (1 - it.animatedFraction)).toInt()
                        } else {
                            (to * it.animatedFraction).toInt()
                        }
                    }
                }

                addListener(onEnd = {
                    view.layoutParams =
                        view.layoutParams.apply { height = if (from > to) 0 else WRAP_CONTENT }
                })
            }.start()
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