package com.financeboard.budgeting;

import com.financeboard.budgeting.dto.BudgetForm;
import com.financeboard.budgeting.dto.BudgetStatus;
import com.financeboard.shared.Category;
import com.financeboard.shared.CategoryService;
import com.financeboard.transactions.TransactionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class BudgetService {

    private final BudgetRepository budgetRepository;
    private final CategoryService categoryService;
    private final TransactionService transactionService;

    public BudgetService(BudgetRepository budgetRepository,
                         CategoryService categoryService,
                         TransactionService transactionService) {
        this.budgetRepository = budgetRepository;
        this.categoryService = categoryService;
        this.transactionService = transactionService;
    }

    public List<Budget> findAll() {
        return budgetRepository.findAllWithCategory();
    }

    public List<Budget> findByYearAndMonth(int year, int month) {
        return budgetRepository.findByYearAndMonth(year, month);
    }

    public Optional<Budget> findById(Long id) {
        return budgetRepository.findById(id);
    }

    public List<BudgetStatus> getBudgetStatusForMonth(int year, int month) {
        List<Budget> budgets = budgetRepository.findByYearAndMonth(year, month);
        return budgets.stream().map(budget -> {
            BigDecimal spent;
            String categoryName;
            String categoryColor;
            if (budget.getCategory() != null) {
                spent = transactionService.sumExpensesByCategoryAndMonth(
                        budget.getCategory().getId(), year, month);
                categoryName = budget.getCategory().getName();
                categoryColor = budget.getCategory().getColor();
            } else {
                spent = transactionService.sumExpenseForMonth(year, month);
                categoryName = "Overall";
                categoryColor = "#495057";
            }
            return new BudgetStatus(budget.getId(), categoryName, categoryColor,
                    budget.getLimitAmount(), spent);
        }).collect(Collectors.toList());
    }

    @Transactional
    public Budget save(BudgetForm form) {
        Budget budget = new Budget();
        if (form.getId() != null) {
            budget = budgetRepository.findById(form.getId()).orElse(new Budget());
        }
        budget.setYear(form.getYear());
        budget.setMonth(form.getMonth());
        budget.setLimitAmount(form.getLimitAmount());
        if (form.getCategoryId() != null) {
            Category category = categoryService.findById(form.getCategoryId()).orElse(null);
            budget.setCategory(category);
        } else {
            budget.setCategory(null);
        }
        return budgetRepository.save(budget);
    }

    @Transactional
    public void deleteById(Long id) {
        budgetRepository.deleteById(id);
    }

    public BudgetForm toForm(Budget budget) {
        BudgetForm form = new BudgetForm();
        form.setId(budget.getId());
        form.setYear(budget.getYear());
        form.setMonth(budget.getMonth());
        form.setLimitAmount(budget.getLimitAmount());
        if (budget.getCategory() != null) {
            form.setCategoryId(budget.getCategory().getId());
        }
        return form;
    }
}
