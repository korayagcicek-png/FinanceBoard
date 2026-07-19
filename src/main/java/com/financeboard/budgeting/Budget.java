package com.financeboard.budgeting;

import com.financeboard.shared.Category;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "budgets", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"category_id", "budget_month", "budget_year"})
})
@Getter
@Setter
@NoArgsConstructor
public class Budget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id", nullable = false)
    @NotNull(message = "Category is required")
    private Category category;

    @Min(1) @Max(12)
    @Column(name = "budget_month", nullable = false)
    private int month;

    @Min(2000)
    @Column(name = "budget_year", nullable = false)
    private int year;

    @NotNull
    @DecimalMin(value = "0.01", message = "Limit must be greater than 0")
    @Column(name = "limit_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal limitAmount;

    public Budget(Category category, int month, int year, BigDecimal limitAmount) {
        this.category = category;
        this.month = month;
        this.year = year;
        this.limitAmount = limitAmount;
    }
}
