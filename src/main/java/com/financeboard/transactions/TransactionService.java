package com.financeboard.transactions;

import com.financeboard.shared.Category;
import com.financeboard.shared.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final CategoryService categoryService;

    public List<Transaction> findAll() {
        return transactionRepository.findAllByOrderByDateDesc();
    }

    public List<Transaction> findRecent(int limit) {
        return transactionRepository.findTop10ByOrderByDateDesc();
    }

    public Transaction findById(Long id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Transaction not found: " + id));
    }

    public List<Transaction> findByMonth(int year, int month) {
        YearMonth ym = YearMonth.of(year, month);
        return transactionRepository.findByDateBetweenOrderByDateDesc(
                ym.atDay(1), ym.atEndOfMonth());
    }

    public List<Transaction> findFiltered(Long categoryId, String type, int year, int month) {
        YearMonth ym = YearMonth.of(year, month);
        Transaction.TransactionType transactionType = (type != null && !type.isBlank())
                ? Transaction.TransactionType.valueOf(type) : null;
        return transactionRepository.findFiltered(
                categoryId, transactionType, ym.atDay(1), ym.atEndOfMonth());
    }

    public BigDecimal getTotalIncome(int year, int month) {
        YearMonth ym = YearMonth.of(year, month);
        return transactionRepository.sumByTypeAndDateBetween(
                Transaction.TransactionType.INCOME, ym.atDay(1), ym.atEndOfMonth());
    }

    public BigDecimal getTotalExpenses(int year, int month) {
        YearMonth ym = YearMonth.of(year, month);
        return transactionRepository.sumByTypeAndDateBetween(
                Transaction.TransactionType.EXPENSE, ym.atDay(1), ym.atEndOfMonth());
    }

    public Map<String, BigDecimal> getExpensesByCategory(int year, int month) {
        YearMonth ym = YearMonth.of(year, month);
        List<Object[]> results = transactionRepository.sumExpensesByCategoryBetween(
                ym.atDay(1), ym.atEndOfMonth());
        Map<String, BigDecimal> map = new LinkedHashMap<>();
        for (Object[] row : results) {
            map.put((String) row[0], (BigDecimal) row[1]);
        }
        return map;
    }

    public Map<Integer, BigDecimal> getMonthlyTotals(Transaction.TransactionType type, int year) {
        List<Object[]> results = transactionRepository.monthlyTotalsByTypeAndYear(type, year);
        Map<Integer, BigDecimal> map = new LinkedHashMap<>();
        for (int i = 1; i <= 12; i++) {
            map.put(i, BigDecimal.ZERO);
        }
        for (Object[] row : results) {
            map.put(((Number) row[0]).intValue(), (BigDecimal) row[1]);
        }
        return map;
    }

    @Transactional
    public Transaction save(Transaction transaction) {
        if (transaction.getCategory() != null && transaction.getCategory().getId() != null) {
            Category category = categoryService.findById(transaction.getCategory().getId());
            transaction.setCategory(category);
        }
        return transactionRepository.save(transaction);
    }

    @Transactional
    public void delete(Long id) {
        transactionRepository.deleteById(id);
    }
}
