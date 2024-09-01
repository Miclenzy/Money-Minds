package com.technolenz.moneyminds

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.technolenz.moneyminds.Note
import com.technolenz.moneyminds.databinding.ActivityNoteDetailBinding

class NoteDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNoteDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoteDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val noteId = intent.getIntExtra("note_id", -1)
        val title = intent.getStringExtra("note_title")
        val content = intent.getStringExtra("note_content")

        binding.noteTitleEdit.setText(title)
        binding.noteContentEdit.setText(content)

        binding.saveButton.setOnClickListener {
            saveEditedNote(noteId)
            finish() // Close the activity after saving the note
        }
    }

    private fun saveEditedNote(noteId: Int) {
        val sharedPreferences = getSharedPreferences("notes", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        val gson = Gson()
        val json = sharedPreferences.getString("notes_json", null)
        val notes: MutableList<Note> = if (json != null) {
            val type = object : TypeToken<MutableList<Note>>() {}.type
            gson.fromJson(json, type)
        } else {
            mutableListOf()
        }

        val editedNote = notes.find { it.id == noteId }
        editedNote?.title = binding.noteTitleEdit.text.toString()
        editedNote?.content = binding.noteContentEdit.text.toString()

        val updatedJson = gson.toJson(notes)
        editor.putString("notes_json", updatedJson)
        editor.apply()
    }
}
