package com.financeboard.dashboard;

import com.financeboard.budgeting.BudgetService;
import com.financeboard.goals.GoalService;
import com.financeboard.projects.ProjectService;
import com.financeboard.transactions.Transaction;
import com.financeboard.transactions.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashboardService {

    private final TransactionService transactionService;
    private final BudgetService budgetService;
    private final GoalService goalService;
    private final ProjectService projectService;

    public DashboardData getDashboardData(int year, int month) {
        BigDecimal totalIncome = transactionService.getTotalIncome(year, month);
        BigDecimal totalExpenses = transactionService.getTotalExpenses(year, month);
        BigDecimal netBalance = totalIncome.subtract(totalExpenses);

        return DashboardData.builder()
                .year(year)
                .month(month)
                .totalIncome(totalIncome)
                .totalExpenses(totalExpenses)
                .netBalance(netBalance)
                .recentTransactions(transactionService.findRecent(10))
                .budgetOverview(budgetService.getBudgetWithSpending(year, month))
                .activeGoals(goalService.findActive())
                .activeProjects(projectService.findActive())
                .expensesByCategory(transactionService.getExpensesByCategory(year, month))
                .monthlyIncomes(transactionService.getMonthlyTotals(Transaction.TransactionType.INCOME, year))
                .monthlyExpenses(transactionService.getMonthlyTotals(Transaction.TransactionType.EXPENSE, year))
                .build();
    }
}
