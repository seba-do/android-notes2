package com.codeop.notes

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.codeop.notes.data.Note
import com.codeop.notes.databinding.ActivityMainBinding
import com.codeop.notes.view.AddFragment
import com.codeop.notes.view.ListFragment

class MainActivity : AppCompatActivity() {
    companion object {
        private const val MODIFY_FRAGMENT_TAG = "modify-fragment"
    }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.commit {
            add(binding.fragmentHolder.id, ListFragment(::switchToAddFragment) {
                switchToModifyFragment(it)
            })
        }
    }

    private fun switchToAddFragment() {
        supportFragmentManager.commit {
            replace(binding.fragmentHolder.id, AddFragment(::switchToListFragment))
            addToBackStack(MODIFY_FRAGMENT_TAG)
        }
    }

    private fun switchToModifyFragment(note: Note) {
        supportFragmentManager.commit {
            replace(binding.fragmentHolder.id, AddFragment(::switchToListFragment, note))
            addToBackStack(MODIFY_FRAGMENT_TAG)
        }
    }

    private fun switchToListFragment() {
        supportFragmentManager.popBackStack()
    }
}