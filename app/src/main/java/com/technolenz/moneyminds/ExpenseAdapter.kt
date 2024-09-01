package com.technolenz.moneyminds

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ExpenseAdapter(
    private val expenses: List<Expense>,
    private val onItemLongClick: (Expense) -> Unit // Callback for long click
) : RecyclerView.Adapter<ExpenseAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.expenseTitle)
        val amount: TextView = view.findViewById(R.id.expenseAmount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_expense, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val expense = expenses[position]
        holder.title.text = expense.title
        holder.amount.text = "${expense.amount}"

        holder.itemView.setOnLongClickListener {
            onItemLongClick(expense)
            true
        }
    }

    override fun getItemCount() = expenses.size
}
