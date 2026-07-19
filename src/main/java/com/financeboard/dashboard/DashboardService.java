package com.financeboard.dashboard;

import com.financeboard.budgeting.BudgetService;
import com.financeboard.budgeting.dto.BudgetStatus;
import com.financeboard.dashboard.dto.DashboardData;
import com.financeboard.goals.GoalService;
import com.financeboard.shared.CategoryService;
import com.financeboard.transactions.TransactionService;
import com.financeboard.transactions.TransactionType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.*;

@Service
@Transactional(readOnly = true)
public class DashboardService {

    private final TransactionService transactionService;
    private final BudgetService budgetService;
    private final GoalService goalService;
    private final CategoryService categoryService;

    public DashboardService(TransactionService transactionService,
                            BudgetService budgetService,
                            GoalService goalService,
                            CategoryService categoryService) {
        this.transactionService = transactionService;
        this.budgetService = budgetService;
        this.goalService = goalService;
        this.categoryService = categoryService;
    }

    public DashboardData buildDashboard(int year, int month) {
        DashboardData data = new DashboardData();
        data.setYear(year);
        data.setMonth(month);

        // Key indicators
        BigDecimal income = transactionService.sumIncomeForMonth(year, month);
        BigDecimal expense = transactionService.sumExpenseForMonth(year, month);
        data.setTotalIncome(income);
        data.setTotalExpense(expense);
        data.setNetAmount(income.subtract(expense));

        // Expenses by category (pie chart)
        List<Object[]> rawCategoryData = transactionService.sumExpensesByCategoryForMonth(year, month);
        Map<String, BigDecimal> expensesByCategory = new LinkedHashMap<>();
        for (Object[] row : rawCategoryData) {
            String catName = row[0] != null ? (String) row[0] : "Uncategorized";
            BigDecimal amount = (BigDecimal) row[1];
            expensesByCategory.put(catName, amount);
        }
        data.setExpensesByCategory(expensesByCategory);

        // Category colors for chart
        Map<String, String> colors = new LinkedHashMap<>();
        categoryService.findAll().forEach(cat -> colors.put(cat.getName(), cat.getColor()));
        data.setCategoryColors(colors);

        // Monthly trend (last 6 months)
        buildTrendData(data, year, month);

        // Budget statuses
        List<BudgetStatus> budgetStatuses = budgetService.getBudgetStatusForMonth(year, month);
        data.setBudgetStatuses(budgetStatuses);

        // Active goals
        data.setActiveGoals(goalService.findActive());

        return data;
    }

    private void buildTrendData(DashboardData data, int currentYear, int currentMonth) {
        List<String> labels = new ArrayList<>();
        List<BigDecimal> incomeList = new ArrayList<>();
        List<BigDecimal> expenseList = new ArrayList<>();

        // Build 6-month window ending at current month
        LocalDate current = LocalDate.of(currentYear, currentMonth, 1);
        List<LocalDate> months = new ArrayList<>();
        for (int i = 5; i >= 0; i--) {
            months.add(current.minusMonths(i));
        }

        // Get all monthly totals from the past
        int fromYear = months.get(0).getYear();
        List<Object[]> totals = transactionService.getMonthlyTotals(fromYear);

        // Index them
        Map<String, BigDecimal> incomeMap = new HashMap<>();
        Map<String, BigDecimal> expenseMap = new HashMap<>();
        for (Object[] row : totals) {
            int y = ((Number) row[0]).intValue();
            int m = ((Number) row[1]).intValue();
            Object typeObj = row[2];
            String type = typeObj instanceof TransactionType
                    ? ((TransactionType) typeObj).name()
                    : String.valueOf(typeObj);
            BigDecimal amount = (BigDecimal) row[3];
            String key = y + "-" + m;
            if ("INCOME".equals(type)) incomeMap.put(key, amount);
            else expenseMap.put(key, amount);
        }

        for (LocalDate monthDate : months) {
            String label = monthDate.getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH)
                    + " " + monthDate.getYear();
            labels.add(label);
            String key = monthDate.getYear() + "-" + monthDate.getMonthValue();
            incomeList.add(incomeMap.getOrDefault(key, BigDecimal.ZERO));
            expenseList.add(expenseMap.getOrDefault(key, BigDecimal.ZERO));
        }

        data.setTrendLabels(labels);
        data.setTrendIncome(incomeList);
        data.setTrendExpense(expenseList);
    }
}
