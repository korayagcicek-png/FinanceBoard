package com.financeboard.projects;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "financial_projects")
@Getter
@Setter
@NoArgsConstructor
public class FinancialProject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 150)
    @Column(nullable = false)
    private String name;

    @Size(max = 1000)
    private String description;

    @NotNull
    @DecimalMin(value = "0.01", message = "Estimated cost must be greater than 0")
    @Column(name = "estimated_cost", nullable = false, precision = 15, scale = 2)
    private BigDecimal estimatedCost;

    @Column(name = "actual_cost", precision = 15, scale = 2)
    private BigDecimal actualCost = BigDecimal.ZERO;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate deadline;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProjectStatus status = ProjectStatus.PLANNED;

    @Min(0) @Max(100)
    @Column(name = "progress_percent", nullable = false)
    private int progressPercent = 0;

    public enum ProjectStatus {
        PLANNED, IN_PROGRESS, COMPLETED, CANCELLED
    }

    public BigDecimal getCostVariance() {
        if (actualCost == null) return estimatedCost.negate();
        return estimatedCost.subtract(actualCost);
    }

    public boolean isOverBudget() {
        if (actualCost == null) return false;
        return actualCost.compareTo(estimatedCost) > 0;
    }
}
