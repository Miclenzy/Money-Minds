package com.technolenz.moneyminds

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.technolenz.moneyminds.dataclass.BudgetCategory

class FinancialOverviewFragment : Fragment() {

    private lateinit var pieChart: PieChart
    private val entries = mutableListOf<PieEntry>()
    private lateinit var recyclerView: RecyclerView
    private lateinit var expenseAdapter: ExpenseAdapter
    private val expenses = mutableListOf<Expense>()
    private lateinit var barChartBudget: BarChart

    private val categories = mutableListOf<BudgetCategory>()

    private val PREFS_NAME = "financial_overview_prefs"
    private val EXPENSES_KEY = "expenses_list"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_financial_overview, container, false)
        barChartBudget = view.findViewById(R.id.budgetBarChart)
        pieChart = view.findViewById(R.id.pieChart)
        recyclerView = view.findViewById(R.id.recyclerView)
        loadExpenses()
        setupChart()
        setupRecyclerView()
        loadCategories() // Load the categories from persistent storage
        setupBudgetChart()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fabAddEntry: FloatingActionButton = view.findViewById(R.id.fabAddEntry)
        fabAddEntry.setOnClickListener {
            val dialog = AddEntryDialogFragment()
            dialog.setTargetFragment(this, 0)
            dialog.show(parentFragmentManager, "AddEntryDialog")
        }
    }

    private fun setupRecyclerView() {
        expenseAdapter = ExpenseAdapter(expenses.toList()) { expense ->
            showDeleteDialog(expense)
        }
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = expenseAdapter
    }

    private fun showDeleteDialog(expense: com.technolenz.moneyminds.Expense) {
        val dialog = DeleteEntryDialogFragment()
        val args = Bundle().apply {
            putString("title", expense.title)
            putFloat("amount", expense.amount.toFloat())
        }
        dialog.arguments = args
        dialog.setTargetFragment(this, 0)
        dialog.show(parentFragmentManager, "DeleteEntryDialog")
    }

    fun onEntryAdded(title: String, amount: Float) {
        val newEntry = PieEntry(amount, title)
        entries.add(newEntry)
        expenses.add(Expense(title, amount.toDouble()))
        expenseAdapter.notifyDataSetChanged()
        updatePieChart()
        saveExpenses() // Save the updated list
    }

    fun onEntryDeleted(expense: Expense) {
        val entryToRemove = entries.find { it.label == expense.title }
        entryToRemove?.let {
            entries.remove(it)
            expenses.remove(expense)
            expenseAdapter.notifyDataSetChanged()
            updatePieChart()
            saveExpenses() // Save the updated list
        }
    }

    private fun setupChart() {
        updatePieChart()
    }

    private fun updatePieChart() {
        val dataSet = PieDataSet(entries, "Expenses")
        dataSet.colors = ColorTemplate.MATERIAL_COLORS.toList()
        dataSet.valueTextSize = 12f
        val pieData = PieData(dataSet)
        pieChart.data = pieData
        pieChart.description.isEnabled = false
        pieChart.setUsePercentValues(true)
        pieChart.notifyDataSetChanged()
        pieChart.invalidate()
    }

    private fun saveExpenses() {
        val sharedPreferences = requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        val gson = Gson()
        val json = gson.toJson(expenses)
        editor.putString(EXPENSES_KEY, json)
        editor.apply()
    }

    private fun loadExpenses() {
        val sharedPreferences = requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString(EXPENSES_KEY, null)
        if (json != null) {
            val type = object : TypeToken<List<Expense>>() {}.type
            val savedExpenses: List<Expense> = gson.fromJson(json, type)
            expenses.clear()
            expenses.addAll(savedExpenses)
            for (expense in expenses) {
                entries.add(PieEntry(expense.amount.toFloat(), expense.title))
            }
        }
    }

    private fun loadCategories() {
        val sharedPreferences = requireContext().getSharedPreferences("MoneyMindsPrefs", Context.MODE_PRIVATE)
        val categoriesJson = sharedPreferences.getString("budget_categories", "")
        if (!categoriesJson.isNullOrEmpty()) {
            val type = object : TypeToken<MutableList<BudgetCategory>>() {}.type
            categories.addAll(Gson().fromJson(categoriesJson, type))
        }
    }

    private fun setupBudgetChart() {
        val entries = categories.mapIndexed { index, category ->
            BarEntry(index.toFloat(), category.budgetLimit)
        }

        val dataSet = BarDataSet(entries, "Budget Categories")
        dataSet.colors = ColorTemplate.MATERIAL_COLORS.toList()

        val data = BarData(dataSet)
        barChartBudget.data = data
        barChartBudget.description.isEnabled = false
        barChartBudget.invalidate()  // Refresh the chart
    }
}
