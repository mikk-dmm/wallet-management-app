package com.example.wallet_management_app.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.wallet_management_app.entity.MonthlyBudget;
import java.time.YearMonth;
import java.util.List;

public interface MonthlyBudgetRepository extends JpaRepository<MonthlyBudget, Long> {
    Optional<MonthlyBudget> findByUserIdAndTargetMonth(Long userId, YearMonth targetMonth);
    List<MonthlyBudget> findByUserIdOrderByTargetMonthDesc(Long userId);
    boolean existsByUserIdAndTargetMonth(Long userId, YearMonth targetMonth);
}