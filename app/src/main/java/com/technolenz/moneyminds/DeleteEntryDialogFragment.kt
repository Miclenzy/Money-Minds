package com.technolenz.moneyminds

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class DeleteEntryDialogFragment : DialogFragment() {

    @SuppressLint("MissingInflatedId")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        val view = requireActivity().layoutInflater.inflate(R.layout.dialog_delete_entry, null)
        builder.setView(view)
            .setPositiveButton("Delete") { dialog, _ ->

                val title = view.findViewById<TextView>(R.id.dialogTitle).text.toString()
                val amount = view.findViewById<TextView>(R.id.dialogAmount).text.toString().toFloat()
                val expense = Expense(title, amount.toDouble())
                (targetFragment as? FinancialOverviewFragment)?.onEntryDeleted(expense)
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }

        // Set dialog text fields
        val title = arguments?.getString("title") ?: ""
        val amount = arguments?.getFloat("amount") ?: 0f
        view.findViewById<TextView>(R.id.dialogTitle).text = title
        view.findViewById<TextView>(R.id.dialogAmount).text = "$amount"

        return builder.create()
    }
}
