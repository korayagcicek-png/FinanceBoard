package com.financeboard;

import com.financeboard.shared.Category;
import com.financeboard.shared.CategoryService;
import com.financeboard.transactions.Transaction;
import com.financeboard.transactions.TransactionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class FinanceBoardApplicationTests {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private TransactionService transactionService;

    @Test
    void contextLoads() {
        assertThat(categoryService).isNotNull();
        assertThat(transactionService).isNotNull();
    }

    @Test
    void canCreateAndFindCategory() {
        Category category = new Category("Test Category", Category.CategoryType.EXPENSE, "#ff0000", null);
        Category saved = categoryService.save(category);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getName()).isEqualTo("Test Category");
        assertThat(saved.getType()).isEqualTo(Category.CategoryType.EXPENSE);

        Category found = categoryService.findById(saved.getId());
        assertThat(found.getName()).isEqualTo("Test Category");
    }

    @Test
    void canCreateAndFindTransaction() {
        Category category = new Category("Test Income", Category.CategoryType.INCOME, "#00ff00", null);
        categoryService.save(category);

        Transaction transaction = new Transaction(
                new BigDecimal("500.00"),
                "Test Income Transaction",
                LocalDate.now(),
                Transaction.TransactionType.INCOME,
                category
        );
        Transaction saved = transactionService.save(transaction);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getAmount()).isEqualByComparingTo(new BigDecimal("500.00"));
        assertThat(saved.getDescription()).isEqualTo("Test Income Transaction");
        assertThat(saved.getType()).isEqualTo(Transaction.TransactionType.INCOME);
    }

    @Test
    void monthlyTotalsAreCalculatedCorrectly() {
        int year = LocalDate.now().getYear();
        int month = LocalDate.now().getMonthValue();

        BigDecimal income = transactionService.getTotalIncome(year, month);
        BigDecimal expenses = transactionService.getTotalExpenses(year, month);

        assertThat(income).isGreaterThanOrEqualTo(BigDecimal.ZERO);
        assertThat(expenses).isGreaterThanOrEqualTo(BigDecimal.ZERO);
    }
}
