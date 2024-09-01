package com.technolenz.moneyminds

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.technolenz.moneyminds.BudgetCategoryAdapter
import com.technolenz.moneyminds.BudgetCategoryDialogFragment
import com.technolenz.moneyminds.R
import com.technolenz.moneyminds.dataclass.BudgetCategory

class BudgetPlannerFragment : Fragment(), BudgetCategoryDialogFragment.BudgetCategoryListener {

    private val categories = mutableListOf<BudgetCategory>()
    private lateinit var spinner: Spinner
    private lateinit var saveBtn: Button
    private lateinit var spinnerAdapter: BudgetCategoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_budget_planner, container, false)

        val btnAddCategory: Button = view.findViewById(R.id.btnAddCategory)
        btnAddCategory.setOnClickListener {
            val dialog = BudgetCategoryDialogFragment()
            dialog.setTargetFragment(this, 0)
            dialog.show(parentFragmentManager, "AddBudgetCategoryDialog")
        }

        saveBtn = view.findViewById(R.id.saveBudgetButton)
        spinner = view.findViewById(R.id.budgetCategory)
        spinnerAdapter = BudgetCategoryAdapter(requireContext(), categories)
        spinner.adapter = spinnerAdapter
        loadCategories()

        saveBtn.setOnClickListener {
            // Use NavController to navigate to FinancialOverviewFragment
            findNavController().navigate(R.id.action_budgetPlannerFragment_to_financialOverviewFragment)
        }

        return view
    }

    override fun onCategoryAdded(categoryName: String, budgetLimit: Float) {
        val newCategory = BudgetCategory(categoryName, budgetLimit)
        categories.add(newCategory)
        updateSpinner()
        saveCategories()
    }

    private fun updateSpinner() {
        spinnerAdapter.notifyDataSetChanged()
    }

    private fun saveCategories() {
        val sharedPreferences = requireContext().getSharedPreferences("MoneyMindsPrefs", Context.MODE_PRIVATE)
        val categoriesJson = Gson().toJson(categories)
        sharedPreferences.edit().putString("budget_categories", categoriesJson).apply()
    }

    private fun loadCategories() {
        val sharedPreferences = requireContext().getSharedPreferences("MoneyMindsPrefs", Context.MODE_PRIVATE)
        val categoriesJson = sharedPreferences.getString("budget_categories", "")
        if (!categoriesJson.isNullOrEmpty()) {
            val type = object : TypeToken<MutableList<BudgetCategory>>() {}.type
            val loadedCategories: List<BudgetCategory> = Gson().fromJson(categoriesJson, type)
            categories.addAll(loadedCategories)
            updateSpinner()
        }
    }
}
