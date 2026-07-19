package com.financeboard.transactions;

import com.financeboard.shared.Category;
import com.financeboard.shared.CategoryService;
import com.financeboard.transactions.dto.TransactionForm;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final CategoryService categoryService;

    public TransactionService(TransactionRepository transactionRepository, CategoryService categoryService) {
        this.transactionRepository = transactionRepository;
        this.categoryService = categoryService;
    }

    public List<Transaction> findAll() {
        return transactionRepository.findAllWithCategory();
    }

    public List<Transaction> findFiltered(Integer year, Integer month, Long categoryId) {
        if (year != null && month != null && categoryId != null) {
            return transactionRepository.findByYearMonthAndCategory(year, month, categoryId);
        } else if (year != null && month != null) {
            return transactionRepository.findByYearAndMonth(year, month);
        } else if (year != null) {
            return transactionRepository.findByYear(year);
        } else if (categoryId != null) {
            return transactionRepository.findByCategoryId(categoryId);
        }
        return transactionRepository.findAllWithCategory();
    }

    public Optional<Transaction> findById(Long id) {
        return transactionRepository.findById(id);
    }

    @Transactional
    public Transaction save(TransactionForm form) {
        Transaction transaction = new Transaction();
        if (form.getId() != null) {
            transaction = transactionRepository.findById(form.getId())
                    .orElse(new Transaction());
        }
        applyForm(transaction, form);
        return transactionRepository.save(transaction);
    }

    @Transactional
    public void deleteById(Long id) {
        transactionRepository.deleteById(id);
    }

    public BigDecimal sumIncomeForMonth(int year, int month) {
        return transactionRepository.sumByTypeAndYearAndMonth(TransactionType.INCOME, year, month);
    }

    public BigDecimal sumExpenseForMonth(int year, int month) {
        return transactionRepository.sumByTypeAndYearAndMonth(TransactionType.EXPENSE, year, month);
    }

    public BigDecimal sumExpensesByCategoryAndMonth(Long categoryId, int year, int month) {
        return transactionRepository.sumExpensesByCategoryAndYearAndMonth(categoryId, year, month);
    }

    public List<Object[]> sumExpensesByCategoryForMonth(int year, int month) {
        return transactionRepository.sumExpensesByCategoryForMonth(year, month);
    }

    public List<Object[]> getMonthlyTotals(int fromYear) {
        return transactionRepository.monthlyTotals(fromYear);
    }

    public TransactionForm toForm(Transaction transaction) {
        TransactionForm form = new TransactionForm();
        form.setId(transaction.getId());
        form.setAmount(transaction.getAmount());
        form.setDescription(transaction.getDescription());
        form.setDate(transaction.getDate());
        form.setType(transaction.getType().name());
        form.setNotes(transaction.getNotes());
        if (transaction.getCategory() != null) {
            form.setCategoryId(transaction.getCategory().getId());
        }
        return form;
    }

    private void applyForm(Transaction transaction, TransactionForm form) {
        transaction.setAmount(form.getAmount());
        transaction.setDescription(form.getDescription());
        transaction.setDate(form.getDate() != null ? form.getDate() : LocalDate.now());
        transaction.setType(TransactionType.valueOf(form.getType()));
        transaction.setNotes(form.getNotes());
        if (form.getCategoryId() != null) {
            Category category = categoryService.findById(form.getCategoryId()).orElse(null);
            transaction.setCategory(category);
        } else {
            transaction.setCategory(null);
        }
    }
}
