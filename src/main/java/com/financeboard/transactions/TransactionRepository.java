package com.financeboard.transactions;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByDateBetweenOrderByDateDesc(LocalDate start, LocalDate end);

    List<Transaction> findByCategoryIdAndDateBetweenOrderByDateDesc(
            Long categoryId, LocalDate start, LocalDate end);

    List<Transaction> findByTypeAndDateBetweenOrderByDateDesc(
            Transaction.TransactionType type, LocalDate start, LocalDate end);

    List<Transaction> findAllByOrderByDateDesc();

    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.type = :type " +
           "AND t.date BETWEEN :start AND :end")
    BigDecimal sumByTypeAndDateBetween(@Param("type") Transaction.TransactionType type,
                                       @Param("start") LocalDate start,
                                       @Param("end") LocalDate end);

    @Query("SELECT t.category.name, COALESCE(SUM(t.amount), 0) FROM Transaction t " +
           "WHERE t.type = 'EXPENSE' AND t.date BETWEEN :start AND :end " +
           "AND t.category IS NOT NULL " +
           "GROUP BY t.category.name ORDER BY SUM(t.amount) DESC")
    List<Object[]> sumExpensesByCategoryBetween(@Param("start") LocalDate start,
                                                @Param("end") LocalDate end);

    @Query("SELECT FUNCTION('MONTH', t.date), COALESCE(SUM(t.amount), 0) " +
           "FROM Transaction t WHERE t.type = :type AND FUNCTION('YEAR', t.date) = :year " +
           "GROUP BY FUNCTION('MONTH', t.date) ORDER BY FUNCTION('MONTH', t.date)")
    List<Object[]> monthlyTotalsByTypeAndYear(@Param("type") Transaction.TransactionType type,
                                               @Param("year") int year);

    List<Transaction> findTop10ByOrderByDateDesc();

    @Query("SELECT t FROM Transaction t WHERE " +
           "(:categoryId IS NULL OR t.category.id = :categoryId) AND " +
           "(:type IS NULL OR t.type = :type) AND " +
           "t.date BETWEEN :start AND :end " +
           "ORDER BY t.date DESC")
    List<Transaction> findFiltered(@Param("categoryId") Long categoryId,
                                   @Param("type") Transaction.TransactionType type,
                                   @Param("start") LocalDate start,
                                   @Param("end") LocalDate end);
}
