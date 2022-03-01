package com.codeop.notes.list.view

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.codeop.notes.R
import com.codeop.notes.list.adapter.NotesAdapter
import com.codeop.notes.data.LayoutType
import com.codeop.notes.databinding.FragmentListBinding
import com.codeop.notes.list.viewmodel.ListViewModel
import com.codeop.notes.repository.AppConfigRepository
import com.codeop.notes.repository.NotesRepository
import com.codeop.notes.utils.ListItemTouchHelper

class ListFragment : Fragment(R.layout.fragment_list) {
    private lateinit var binding: FragmentListBinding
    private lateinit var menu: Menu
    private lateinit var viewModel: ListViewModel

    private val notesAdapter: NotesAdapter
        get() = binding.notesList.adapter as NotesAdapter

    private val appConfigRepository: AppConfigRepository
        get() = AppConfigRepository.getInstance(requireContext())

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.setTitle(R.string.app_name)
        setHasOptionsMenu(true)

        viewModel = ViewModelProvider(this)[ListViewModel::class.java]

        binding = FragmentListBinding.bind(view)

        binding.notesList.apply {
            setLayoutManager(appConfigRepository.getSelectedLayoutType())

            adapter = NotesAdapter(
                onDeleteClick = {
                    viewModel.removeNote(it)
                },
                onArchiveClick = {
                    viewModel.switchArchived(it)
                },
                onEditClick = {
                    val action = ListFragmentDirections.actionListFragmentToAddFragment(it)
                    findNavController().navigate(action)
                }
            )

            ItemTouchHelper(
                ListItemTouchHelper(
                    notesAdapter
                ) { list ->
                    viewModel.updatePositions(list)
                }
            ).attachToRecyclerView(this)
        }

        binding.btnAdd.setOnClickListener {
            findNavController().navigate(R.id.action_listFragment_to_addFragment)
        }

        viewModel.notes.observe(viewLifecycleOwner) {
            notesAdapter.submitList(it)
            setAnimationVisibility(it.isEmpty())
        }
    }

    private fun setAnimationVisibility(isListEmpty: Boolean) {
        binding.emptyListAnimation.apply {
            if (isListEmpty) {
                isVisible = true
                playAnimation()
            } else {
                isVisible = false
                cancelAnimation()
            }
        }
    }

    private fun setLayoutManager(layoutType: LayoutType) {
        binding.notesList.layoutManager = when (layoutType) {
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
            R.id.btn_archive -> {
                viewModel.switchArchivedVisibility()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}