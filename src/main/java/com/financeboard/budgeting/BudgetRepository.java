package com.financeboard.budgeting;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long> {

    List<Budget> findByMonthAndYear(int month, int year);

    Optional<Budget> findByCategoryIdAndMonthAndYear(Long categoryId, int month, int year);
}
