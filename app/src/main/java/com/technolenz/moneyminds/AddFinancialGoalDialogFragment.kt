package com.technolenz.moneyminds

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import java.util.Calendar

class AddFinancialGoalDialogFragment : DialogFragment() {

    interface AddFinancialGoalListener {
        fun onGoalAddedOrEdited(goalName: String, targetAmount: Float, currentSavings: Float, deadline: String)
    }

    private var listener: AddFinancialGoalListener? = null
    private lateinit var deadlineTextView: TextView
    private var selectedDate: String = ""

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = targetFragment as? AddFinancialGoalListener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        val view = requireActivity().layoutInflater.inflate(R.layout.dialog_add_financial_goal, null)
        deadlineTextView = view.findViewById(R.id.goalDeadline)
        val btnSelectDate: Button = view.findViewById(R.id.btnSelectDate)

        val args = arguments
        val goalName = args?.getString("goalName") ?: ""
        val targetAmount = args?.getFloat("targetAmount") ?: 0f
        val currentSavings = args?.getFloat("currentSavings") ?: 0f
        selectedDate = args?.getString("deadline") ?: ""

        view.findViewById<EditText>(R.id.goalName).setText(goalName)
        view.findViewById<EditText>(R.id.goalTargetAmount).setText(targetAmount.toString())
        view.findViewById<EditText>(R.id.goalCurrentSavings).setText(currentSavings.toString())
        deadlineTextView.text = selectedDate

        btnSelectDate.setOnClickListener {
            showDatePicker()
        }

        builder.setView(view)
            .setPositiveButton("Save") { dialog, _ ->
                val newGoalName = view.findViewById<EditText>(R.id.goalName).text.toString()
                val newTargetAmount = view.findViewById<EditText>(R.id.goalTargetAmount).text.toString().toFloat()
                val newCurrentSavings = view.findViewById<EditText>(R.id.goalCurrentSavings).text.toString().toFloat()
                listener?.onGoalAddedOrEdited(newGoalName, newTargetAmount, newCurrentSavings, selectedDate)
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }

        return builder.create()
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val datePicker = DatePickerDialog(requireContext(), { _, year, month, dayOfMonth ->
            val date = "${dayOfMonth}/${month + 1}/${year}"
            selectedDate = date
            deadlineTextView.text = date
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))

        datePicker.show()
    }
}




