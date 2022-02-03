package com.codeop.notes

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.codeop.notes.databinding.ActivityMainBinding
import com.codeop.notes.view.AddFragment
import com.codeop.notes.view.ListFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        switchToListFragment()
    }

    private fun switchToAddFragment() {
        supportFragmentManager.commit {
            replace(binding.fragmentHolder.id, AddFragment(::switchToListFragment))
        }
    }

    private fun switchToListFragment() {
        supportFragmentManager.commit {
            replace(binding.fragmentHolder.id, ListFragment(::switchToAddFragment))
        }
    }
}