package com.technolenz.moneyminds

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.technolenz.moneyminds.Note
import com.technolenz.moneyminds.databinding.ActivityAddNoteBinding

class AddNoteActivity : AppCompatActivity() {
 private lateinit var binding: ActivityAddNoteBinding

 override fun onCreate(savedInstanceState: Bundle?) {
  super.onCreate(savedInstanceState)
  binding = ActivityAddNoteBinding.inflate(layoutInflater)
  setContentView(binding.root)

  binding.saveButton.setOnClickListener {
   val title = binding.noteTitle.text.toString()
   val content = binding.noteContent.text.toString()
   saveNoteToLocal(title, content)
   finish()
  }
 }

 private fun saveNoteToLocal(title: String, content: String) {
  val sharedPreferences = getSharedPreferences("notes", Context.MODE_PRIVATE)
  val editor = sharedPreferences.edit()

  // Load existing notes
  val gson = Gson()
  val json = sharedPreferences.getString("notes_json", null)
  val notes: MutableList<Note> = if (json != null) {
   val type = object : TypeToken<MutableList<Note>>() {}.type
   gson.fromJson(json, type)
  } else {
   mutableListOf()
  }

  // Add new note
  val note = Note(
   id = System.currentTimeMillis().toInt(),
   title = title,
   content = content,
   timestamp = System.currentTimeMillis()
  )
  notes.add(note)

  // Save back to SharedPreferences
  val updatedJson = gson.toJson(notes)
  editor.putString("notes_json", updatedJson)
  editor.apply()
 }
}
