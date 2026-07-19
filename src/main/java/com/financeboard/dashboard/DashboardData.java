package com.financeboard.dashboard;

import com.financeboard.budgeting.Budget;
import com.financeboard.goals.Goal;
import com.financeboard.projects.FinancialProject;
import com.financeboard.transactions.Transaction;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Getter
@Builder
public class DashboardData {

    private final int year;
    private final int month;

    private final BigDecimal totalIncome;
    private final BigDecimal totalExpenses;
    private final BigDecimal netBalance;

    private final List<Transaction> recentTransactions;
    private final Map<Budget, BigDecimal> budgetOverview;
    private final List<Goal> activeGoals;
    private final List<FinancialProject> activeProjects;

    // Chart data
    private final Map<String, BigDecimal> expensesByCategory;
    private final Map<Integer, BigDecimal> monthlyIncomes;
    private final Map<Integer, BigDecimal> monthlyExpenses;
}
