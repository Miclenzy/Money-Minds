package com.technolenz.moneyminds

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.technolenz.moneyminds.dataclass.FinancialGoal

class FinancialGoalAdapter(
    private val goals: MutableList<FinancialGoal>,
    private val onEditClicked: (FinancialGoal) -> Unit
) : RecyclerView.Adapter<FinancialGoalAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_financial_goal, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val goal = goals[position]
        holder.goalName.text = goal.goalName
        holder.goalDeadline.text = goal.deadline
        holder.goalProgress.text = "Progress: ${goal.currentSavings}/${goal.targetAmount}"

        // Calculate and set progress bar and progress text
        val progressPercentage = (goal.currentSavings / goal.targetAmount * 100).toInt()
        holder.progressBar.progress = progressPercentage
        holder.goalProgress.text = "$progressPercentage%"

        holder.btnEdit.setOnClickListener {
            onEditClicked(goal)
        }
    }

    override fun getItemCount() = goals.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val goalName: TextView = view.findViewById(R.id.goalName)
        val goalDeadline: TextView = view.findViewById(R.id.goalDeadline)
        val goalProgress: TextView = view.findViewById(R.id.goalProgress)
        val progressBar: ProgressBar = view.findViewById(R.id.progressBar)
        val btnEdit: Button = view.findViewById(R.id.btnEdit)
    }
}




