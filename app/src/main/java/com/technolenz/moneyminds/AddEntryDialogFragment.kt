package com.technolenz.moneyminds

import android.app.Dialog
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class AddEntryDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        val view = requireActivity().layoutInflater.inflate(R.layout.dialog_add_entry, null)
        builder.setView(view)
            .setPositiveButton("Add") { dialog, _ ->
                val title = view.findViewById<EditText>(R.id.entryTitle).text.toString()
                val amount = view.findViewById<EditText>(R.id.entryAmount).text.toString().toFloat()
                (targetFragment as? FinancialOverviewFragment)?.onEntryAdded(title, amount)
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }

        return builder.create()
    }
}
