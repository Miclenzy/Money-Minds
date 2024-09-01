package com.technolenz.moneyminds.dataclass

data class FinancialGoal(
    var goalName: String,
    var targetAmount: Float,
    var currentSavings: Float,
    var deadline: String
)

