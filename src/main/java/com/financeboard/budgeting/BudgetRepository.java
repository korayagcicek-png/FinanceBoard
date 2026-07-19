package com.financeboard.budgeting;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long> {

    @Query("SELECT b FROM Budget b LEFT JOIN FETCH b.category WHERE b.year = :year AND b.month = :month")
    List<Budget> findByYearAndMonth(@Param("year") int year, @Param("month") int month);

    @Query("SELECT b FROM Budget b LEFT JOIN FETCH b.category WHERE b.year = :year")
    List<Budget> findByYear(@Param("year") int year);

    @Query("SELECT b FROM Budget b LEFT JOIN FETCH b.category ORDER BY b.year DESC, b.month DESC")
    List<Budget> findAllWithCategory();

    Optional<Budget> findByCategoryIdAndYearAndMonth(Long categoryId, int year, int month);
}
