package com.technolenz.moneyminds

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.technolenz.moneyminds.dataclass.BudgetCategory

class BudgetCategoryAdapter(
    private val context: Context,
    private val categories: List<BudgetCategory>
) : BaseAdapter() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun getCount(): Int = categories.size

    override fun getItem(position: Int): Any = categories[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: inflater.inflate(android.R.layout.simple_spinner_item, parent, false)
        val category = categories[position]

        val textView = view.findViewById<TextView>(android.R.id.text1)
        textView.text = category.name // Customize this according to your layout

        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: inflater.inflate(android.R.layout.simple_spinner_dropdown_item, parent, false)
        val category = categories[position]

        val textView = view.findViewById<TextView>(android.R.id.text1)
        textView.text = category.name
        // Customize this according to your layout

        return view
    }
}
