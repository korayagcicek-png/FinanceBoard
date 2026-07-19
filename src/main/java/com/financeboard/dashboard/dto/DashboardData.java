package com.financeboard.dashboard.dto;

import com.financeboard.budgeting.dto.BudgetStatus;
import com.financeboard.goals.Goal;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class DashboardData {

    private int year;
    private int month;

    private BigDecimal totalIncome;
    private BigDecimal totalExpense;
    private BigDecimal netAmount;

    // Category name -> amount (for pie chart)
    private Map<String, BigDecimal> expensesByCategory;
    private Map<String, String> categoryColors;

    // Monthly trend data (last 6 months): label -> {income, expense}
    private List<String> trendLabels;
    private List<BigDecimal> trendIncome;
    private List<BigDecimal> trendExpense;

    private List<BudgetStatus> budgetStatuses;
    private List<Goal> activeGoals;

    public DashboardData() {}

    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }

    public int getMonth() { return month; }
    public void setMonth(int month) { this.month = month; }

    public BigDecimal getTotalIncome() { return totalIncome; }
    public void setTotalIncome(BigDecimal totalIncome) { this.totalIncome = totalIncome; }

    public BigDecimal getTotalExpense() { return totalExpense; }
    public void setTotalExpense(BigDecimal totalExpense) { this.totalExpense = totalExpense; }

    public BigDecimal getNetAmount() { return netAmount; }
    public void setNetAmount(BigDecimal netAmount) { this.netAmount = netAmount; }

    public boolean isNetPositive() {
        return netAmount != null && netAmount.compareTo(BigDecimal.ZERO) >= 0;
    }

    public Map<String, BigDecimal> getExpensesByCategory() { return expensesByCategory; }
    public void setExpensesByCategory(Map<String, BigDecimal> expensesByCategory) { this.expensesByCategory = expensesByCategory; }

    public Map<String, String> getCategoryColors() { return categoryColors; }
    public void setCategoryColors(Map<String, String> categoryColors) { this.categoryColors = categoryColors; }

    public List<String> getTrendLabels() { return trendLabels; }
    public void setTrendLabels(List<String> trendLabels) { this.trendLabels = trendLabels; }

    public List<BigDecimal> getTrendIncome() { return trendIncome; }
    public void setTrendIncome(List<BigDecimal> trendIncome) { this.trendIncome = trendIncome; }

    public List<BigDecimal> getTrendExpense() { return trendExpense; }
    public void setTrendExpense(List<BigDecimal> trendExpense) { this.trendExpense = trendExpense; }

    public List<BudgetStatus> getBudgetStatuses() { return budgetStatuses; }
    public void setBudgetStatuses(List<BudgetStatus> budgetStatuses) { this.budgetStatuses = budgetStatuses; }

    public List<Goal> getActiveGoals() { return activeGoals; }
    public void setActiveGoals(List<Goal> activeGoals) { this.activeGoals = activeGoals; }
}
