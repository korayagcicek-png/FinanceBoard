package com.financeboard.budgeting.dto;

import java.math.BigDecimal;

public class BudgetStatus {
    private Long budgetId;
    private String categoryName;
    private String categoryColor;
    private BigDecimal limitAmount;
    private BigDecimal spentAmount;
    private BigDecimal remaining;
    private double usagePercent;
    private boolean overBudget;

    public BudgetStatus(Long budgetId, String categoryName, String categoryColor,
                        BigDecimal limitAmount, BigDecimal spentAmount) {
        this.budgetId = budgetId;
        this.categoryName = categoryName;
        this.categoryColor = categoryColor != null ? categoryColor : "#6c757d";
        this.limitAmount = limitAmount;
        this.spentAmount = spentAmount;
        this.remaining = limitAmount.subtract(spentAmount);
        this.overBudget = spentAmount.compareTo(limitAmount) > 0;
        this.usagePercent = limitAmount.compareTo(BigDecimal.ZERO) > 0
                ? spentAmount.divide(limitAmount, 4, java.math.RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100))
                    .doubleValue()
                : 0;
    }

    public Long getBudgetId() { return budgetId; }
    public String getCategoryName() { return categoryName; }
    public String getCategoryColor() { return categoryColor; }
    public BigDecimal getLimitAmount() { return limitAmount; }
    public BigDecimal getSpentAmount() { return spentAmount; }
    public BigDecimal getRemaining() { return remaining; }
    public double getUsagePercent() { return usagePercent; }
    public boolean isOverBudget() { return overBudget; }
    public int getUsagePercentCapped() { return (int) Math.min(usagePercent, 100); }

    public String getProgressBarClass() {
        if (overBudget) return "progress-bar bg-danger";
        if (usagePercent > 80) return "progress-bar bg-warning";
        return "progress-bar bg-success";
    }
}
