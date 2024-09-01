package com.technolenz.moneyminds.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.technolenz.moneyminds.AddNoteActivity
import com.technolenz.moneyminds.Note
import com.technolenz.moneyminds.NoteDetailActivity
import com.technolenz.moneyminds.NotesAdapter
import com.technolenz.moneyminds.R
import com.technolenz.moneyminds.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerViewNotes.layoutManager = LinearLayoutManager(context)
        loadNotes()

        val fabAddNote: FloatingActionButton = view.findViewById(R.id.fab)
        fabAddNote.setOnClickListener {
            val intent = Intent(requireContext(), AddNoteActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        // Reload notes when the fragment resumes
        loadNotes()
    }

    private fun loadNotes() {
        val sharedPreferences = requireContext().getSharedPreferences("notes", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("notes_json", null)
        val notes: MutableList<Note> = if (json != null) {
            val type = object : TypeToken<MutableList<Note>>() {}.type
            gson.fromJson(json, type)
        } else {
            mutableListOf()
        }

        // Setting up the adapter with a click listener to open the note details
        binding.recyclerViewNotes.adapter = NotesAdapter(
            notes,
            { note -> openNoteDetail(note) },
            { note -> showContextMenu(note) }
        )
    }

    private fun openNoteDetail(note: Note) {
        val intent = Intent(requireContext(), NoteDetailActivity::class.java)
        intent.putExtra("note_id", note.id)
        intent.putExtra("note_title", note.title)
        intent.putExtra("note_content", note.content)
        startActivity(intent)
    }

    private fun showContextMenu(note: Note) {
        val popup = PopupMenu(requireContext(), binding.recyclerViewNotes)
        popup.menuInflater.inflate(R.menu.note_context_menu, popup.menu)
        popup.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_delete -> {
                    deleteNote(note)
                    true
                }
                else -> false
            }
        }
        popup.show()
    }

    private fun editNote(note: Note) {
        val intent = Intent(requireContext(), NoteDetailActivity::class.java)
        intent.putExtra("note_id", note.id)
        intent.putExtra("note_title", note.title)
        intent.putExtra("note_content", note.content)
        startActivity(intent)
    }

    private fun deleteNote(note: Note) {
        val sharedPreferences = requireContext().getSharedPreferences("notes", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("notes_json", null)
        val notes: MutableList<Note> = if (json != null) {
            val type = object : TypeToken<MutableList<Note>>() {}.type
            gson.fromJson(json, type)
        } else {
            mutableListOf()
        }

        // Remove the note
        notes.remove(note)

        // Save the updated list back to SharedPreferences
        val editor = sharedPreferences.edit()
        val updatedJson = gson.toJson(notes)
        editor.putString("notes_json", updatedJson)
        editor.apply()

        // Reload notes after deletion
        loadNotes()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
