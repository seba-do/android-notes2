package com.codeop.notes.view

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.codeop.notes.R
import com.codeop.notes.adapter.NotesAdapter
import com.codeop.notes.data.LayoutType
import com.codeop.notes.data.Note
import com.codeop.notes.databinding.FragmentListBinding
import com.codeop.notes.repository.AppConfigRepository
import com.codeop.notes.repository.NotesRepository

class ListFragment(
    private val onAddClick: () -> Unit,
    private val onItemClick: (Note) -> Unit,
) : Fragment(R.layout.fragment_list) {
    private lateinit var binding: FragmentListBinding
    private lateinit var menu: Menu

    private val notesAdapter: NotesAdapter
        get() = binding.notesList.adapter as NotesAdapter

    private val notesRepository: NotesRepository
        get() = NotesRepository.getInstance(requireContext())

    private val appConfigRepository: AppConfigRepository
        get() = AppConfigRepository.getInstance(requireContext())


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.setTitle(R.string.app_name)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentListBinding.bind(view)

        binding.notesList.apply {
            setLayoutManager(appConfigRepository.getSelectedLayoutType())

            adapter = NotesAdapter(onItemClick) {
                notesRepository.removeNote(it)
                notesAdapter.submitList(notesRepository.getNotes())
            }

            notesAdapter.submitList(notesRepository.getNotes())
            itemTouchHelper.attachToRecyclerView(this)
        }

        binding.btnAdd.setOnClickListener {
            onAddClick()
        }
    }

    private fun setLayoutManager(layoutType: LayoutType) {
        binding.notesList.layoutManager = when(layoutType) {
            LayoutType.GRID -> StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            else -> LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }

        appConfigRepository.saveSelectedLayoutType(layoutType)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu, menu)
        this.menu = menu
        when (appConfigRepository.getSelectedLayoutType()) {
            LayoutType.GRID -> menu.findItem(R.id.btn_to_grid)?.isVisible = false
            LayoutType.LINEAR -> menu.findItem(R.id.btn_to_list)?.isVisible = false
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.btn_to_grid -> {
                menu.apply {
                    findItem(R.id.btn_to_list)?.isVisible = true
                    findItem(R.id.btn_to_grid)?.isVisible = false
                    setLayoutManager(LayoutType.GRID)
                }
            }
            R.id.btn_to_list -> {
                menu.apply {
                    findItem(R.id.btn_to_grid)?.isVisible = true
                    findItem(R.id.btn_to_list)?.isVisible = false
                    setLayoutManager(LayoutType.LINEAR)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private val  itemTouchHelper: ItemTouchHelper by lazy {
        val simpleItemTouchCallback = object: ItemTouchHelper.SimpleCallback(UP or DOWN or START or END, 0) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                val from = viewHolder.adapterPosition
                val to = target.adapterPosition

                val list = notesAdapter.currentList.toMutableList()
                val fromLocation = list[from]
                list.removeAt(from)
                if (to < from) {
                    list.add(to + 1, fromLocation)
                } else {
                    list.add(to - 1, fromLocation)
                }

                notesRepository.updatePositions(list)

                notesAdapter.submitList(list)

                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {}

            override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
                super.onSelectedChanged(viewHolder, actionState)

                if (actionState == ACTION_STATE_DRAG) {
                    viewHolder?.itemView?.alpha = 0.5f
                }
            }

            override fun clearView(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ) {
                super.clearView(recyclerView, viewHolder)

                viewHolder.itemView.alpha = 1.0f
            }
        }

        ItemTouchHelper(simpleItemTouchCallback)
    }
}