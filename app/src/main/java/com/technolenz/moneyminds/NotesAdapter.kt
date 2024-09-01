package com.technolenz.moneyminds

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.technolenz.moneyminds.databinding.ItemNoteBinding

data class Note(
    val id: Int,
    var title: String,
    var content: String,
    val timestamp: Long
)

class NotesAdapter(
    private val notes: List<Note>,
    private val clickListener: (Note) -> Unit,
    private val longClickListener: (Note) -> Unit
) : RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {

    inner class NoteViewHolder(private val binding: ItemNoteBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(note: Note) {
            binding.noteTitle.text = note.title
            binding.noteContent.text = note.content
            binding.noteTimestamp.text = android.text.format.DateFormat.format("MMM dd, yyyy h:mm a", note.timestamp)

            // Handle note click
            itemView.setOnClickListener { clickListener(note) }

            // Handle note long click
            itemView.setOnLongClickListener {
                longClickListener(note)
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding = ItemNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(notes[position])
    }

    override fun getItemCount(): Int = notes.size
}
