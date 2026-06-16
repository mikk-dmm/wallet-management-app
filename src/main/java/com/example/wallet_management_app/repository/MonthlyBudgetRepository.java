package com.example.wallet_management_app.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.wallet_management_app.entity.MonthlyBudget;
import java.time.LocalDate;

public interface MonthlyBudgetRepository extends JpaRepository<MonthlyBudget, Long> {
    Optional<MonthlyBudget> findByUserIdAndTargetMonth(Long userId, LocalDate targetMonth);
}