package com.financeboard.budgeting;

import com.financeboard.shared.Category;
import com.financeboard.shared.CategoryService;
import com.financeboard.transactions.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BudgetService {

    private final BudgetRepository budgetRepository;
    private final CategoryService categoryService;
    private final TransactionService transactionService;

    public List<Budget> findByMonth(int year, int month) {
        return budgetRepository.findByMonthAndYear(month, year);
    }

    public Budget findById(Long id) {
        return budgetRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Budget not found: " + id));
    }

    public Map<Budget, BigDecimal> getBudgetWithSpending(int year, int month) {
        List<Budget> budgets = findByMonth(year, month);
        Map<String, BigDecimal> spendingByCategory = transactionService.getExpensesByCategory(year, month);
        Map<Budget, BigDecimal> result = new LinkedHashMap<>();
        for (Budget budget : budgets) {
            BigDecimal spent = spendingByCategory.getOrDefault(budget.getCategory().getName(), BigDecimal.ZERO);
            result.put(budget, spent);
        }
        return result;
    }

    @Transactional
    public Budget save(Budget budget) {
        if (budget.getCategory() != null && budget.getCategory().getId() != null) {
            Category category = categoryService.findById(budget.getCategory().getId());
            budget.setCategory(category);
        }
        return budgetRepository.save(budget);
    }

    @Transactional
    public void delete(Long id) {
        budgetRepository.deleteById(id);
    }
}
