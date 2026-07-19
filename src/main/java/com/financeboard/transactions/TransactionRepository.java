package com.financeboard.transactions;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByOrderByDateDesc();

    @Query("SELECT t FROM Transaction t LEFT JOIN FETCH t.category WHERE YEAR(t.date) = :year AND MONTH(t.date) = :month ORDER BY t.date DESC")
    List<Transaction> findByYearAndMonth(@Param("year") int year, @Param("month") int month);

    @Query("SELECT t FROM Transaction t LEFT JOIN FETCH t.category WHERE YEAR(t.date) = :year ORDER BY t.date DESC")
    List<Transaction> findByYear(@Param("year") int year);

    @Query("SELECT t FROM Transaction t LEFT JOIN FETCH t.category WHERE t.category.id = :categoryId ORDER BY t.date DESC")
    List<Transaction> findByCategoryId(@Param("categoryId") Long categoryId);

    @Query("SELECT t FROM Transaction t LEFT JOIN FETCH t.category WHERE YEAR(t.date) = :year AND MONTH(t.date) = :month AND t.category.id = :categoryId ORDER BY t.date DESC")
    List<Transaction> findByYearMonthAndCategory(@Param("year") int year, @Param("month") int month, @Param("categoryId") Long categoryId);

    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.type = :type AND YEAR(t.date) = :year AND MONTH(t.date) = :month")
    BigDecimal sumByTypeAndYearAndMonth(@Param("type") TransactionType type, @Param("year") int year, @Param("month") int month);

    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.type = 'EXPENSE' AND t.category.id = :categoryId AND YEAR(t.date) = :year AND MONTH(t.date) = :month")
    BigDecimal sumExpensesByCategoryAndYearAndMonth(@Param("categoryId") Long categoryId, @Param("year") int year, @Param("month") int month);

    @Query("SELECT t.category.name, COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.type = 'EXPENSE' AND YEAR(t.date) = :year AND MONTH(t.date) = :month GROUP BY t.category.name")
    List<Object[]> sumExpensesByCategoryForMonth(@Param("year") int year, @Param("month") int month);

    @Query("SELECT YEAR(t.date), MONTH(t.date), t.type, COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE YEAR(t.date) >= :fromYear GROUP BY YEAR(t.date), MONTH(t.date), t.type ORDER BY YEAR(t.date), MONTH(t.date)")
    List<Object[]> monthlyTotals(@Param("fromYear") int fromYear);

    @Query("SELECT t FROM Transaction t LEFT JOIN FETCH t.category ORDER BY t.date DESC")
    List<Transaction> findAllWithCategory();

    @Query("SELECT t FROM Transaction t LEFT JOIN FETCH t.category WHERE YEAR(t.date) = :year AND MONTH(t.date) = :month AND t.category.id = :categoryId ORDER BY t.date DESC")
    List<Transaction> findByFilters(@Param("year") int year, @Param("month") int month, @Param("categoryId") Long categoryId);
}
