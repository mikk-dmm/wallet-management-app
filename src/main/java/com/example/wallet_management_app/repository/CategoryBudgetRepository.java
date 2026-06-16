package com.example.wallet_management_app.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.wallet_management_app.entity.CategoryBudget;
import java.time.LocalDate;

public interface CategoryBudgetRepository extends JpaRepository<CategoryBudget, Long> {
    Optional<CategoryBudget> findByUserIdAndCategoryIdAndTargetMonth(
        Long userId, Long categoryId, LocalDate targetMonth
    );
}