package com.technolenz.moneyminds

import android.app.NotificationManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.technolenz.moneyminds.dataclass.FinancialGoal

class FinancialGoalsFragment : Fragment(), AddFinancialGoalDialogFragment.AddFinancialGoalListener {

    private val goals = mutableListOf<FinancialGoal>()
    private lateinit var goalRecyclerView: RecyclerView
    private lateinit var goalProgressBarChart: BarChart
    private lateinit var btnAddGoal: Button
    private lateinit var adapter: FinancialGoalAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_financial_goals, container, false)
        goalRecyclerView = view.findViewById(R.id.goalRecyclerView)
        goalProgressBarChart = view.findViewById(R.id.goalProgressBarChart)
        btnAddGoal = view.findViewById(R.id.btnAddGoal)

        // Set up the RecyclerView with the onEditClicked callback
        adapter = FinancialGoalAdapter(goals) { goal ->
            showEditGoalDialog(goal)
        }
        goalRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        goalRecyclerView.adapter = adapter

        loadGoals()
        setupGoalProgressBarChart()

        btnAddGoal.setOnClickListener {
            val dialog = AddFinancialGoalDialogFragment()
            dialog.setTargetFragment(this, 0)
            dialog.show(parentFragmentManager, "AddFinancialGoalDialog")
        }

        return view
    }

    override fun onGoalAddedOrEdited(goalName: String, targetAmount: Float, currentSavings: Float, deadline: String) {
        // Find and update the existing goal if editing
        val existingGoal = goals.find { it.goalName == goalName }
        if (existingGoal != null) {
            existingGoal.targetAmount = targetAmount
            existingGoal.currentSavings = currentSavings
            existingGoal.deadline = deadline
        } else {
            val newGoal = FinancialGoal(goalName, targetAmount, currentSavings, deadline)
            goals.add(newGoal)
        }
        adapter.notifyDataSetChanged()
        saveGoals()
        setupGoalProgressBarChart()
    }

    private fun loadGoals() {
        val sharedPreferences = requireContext().getSharedPreferences("MoneyMindsPrefs", Context.MODE_PRIVATE)
        val goalsJson = sharedPreferences.getString("financial_goals", "")
        if (!goalsJson.isNullOrEmpty()) {
            val type = object : TypeToken<MutableList<FinancialGoal>>() {}.type
            goals.addAll(Gson().fromJson(goalsJson, type))
        }
    }

    private fun setupGoalProgressBarChart() {
        val entries = goals.mapIndexed { index, goal ->
            BarEntry(index.toFloat(), goal.currentSavings / goal.targetAmount * 100)
        }
        val dataSet = BarDataSet(entries, "Goal Progress")
        val data = BarData(dataSet)
        goalProgressBarChart.data = data
        goalProgressBarChart.invalidate()
    }

    private fun saveGoals() {
        val sharedPreferences = requireContext().getSharedPreferences("MoneyMindsPrefs", Context.MODE_PRIVATE)
        val goalsJson = Gson().toJson(goals)
        sharedPreferences.edit().putString("financial_goals", goalsJson).apply()
    }

    private fun showEditGoalDialog(goal: FinancialGoal) {
        val dialog = AddFinancialGoalDialogFragment()
        val args = Bundle().apply {
            putString("goalName", goal.goalName)
            putFloat("targetAmount", goal.targetAmount)
            putFloat("currentSavings", goal.currentSavings)
            putString("deadline", goal.deadline)
        }
        dialog.arguments = args
        dialog.setTargetFragment(this, 1)
        dialog.show(parentFragmentManager, "EditFinancialGoalDialog")
    }




}

