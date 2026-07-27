package com.example.wallet_management_app.repository;

import java.util.List;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.wallet_management_app.entity.Expenditure;
import com.example.wallet_management_app.projection.CategoryExpenseSummary;

public interface ExpenditureRepository extends JpaRepository<Expenditure, Long> {
        List<Expenditure> findByUserId(Long userId);
        boolean existsByUserIdAndCategoryId(Long userId, Long categoryId);
        boolean existsByUserIdAndPaymentMethodId(Long userId, Long paymentMethodId);

        List<Expenditure> findByUserIdAndExpenditureDateBetweenOrderByExpenditureDateDescIdDesc(
                Long userId,
                LocalDate periodStart,
                LocalDate periodEnd
        );

        @Query("""
                SELECT SUM(e.amount)
                FROM Expenditure e
                WHERE e.user.id = :userId
                        AND e.expenditureDate BETWEEN :periodStart AND :periodEnd
                """)
        Optional<BigDecimal> sumAmountByUserIdAndDateBetween(
                @Param("userId") Long userId,
                @Param("periodStart") LocalDate periodStart,
                @Param("periodEnd") LocalDate periodEnd
        );

        @Query("""
                SELECT
                        e.category.id AS categoryId,
                        SUM(e.amount) AS expenseAmount
                FROM Expenditure e
                WHERE e.user.id = :userId
                        AND e.expenditureDate BETWEEN :periodStart AND :periodEnd
                GROUP BY e.category.id
        """)
        List<CategoryExpenseSummary> sumCategoryAmountByUserIdAndDateBetween(
                @Param("userId") Long userId,
                @Param("periodStart") LocalDate periodStart,
                @Param("periodEnd") LocalDate periodEnd
        );
}