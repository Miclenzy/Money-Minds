package com.technolenz.moneyminds

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class BudgetCategoryDialogFragment : DialogFragment() {

    interface BudgetCategoryListener {
        fun onCategoryAdded(categoryName: String, budgetLimit: Float)
    }

    private var listener: BudgetCategoryListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = targetFragment as? BudgetCategoryListener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        val view = requireActivity().layoutInflater.inflate(R.layout.dialog_add_budget_category, null)
        builder.setView(view)
            .setPositiveButton("Add") { dialog, _ ->
                val categoryName = view.findViewById<EditText>(R.id.categoryName).text.toString()
                val budgetLimit = view.findViewById<EditText>(R.id.budgetLimit).text.toString().toFloat()
                listener?.onCategoryAdded(categoryName, budgetLimit)
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }

        return builder.create()
    }
}

