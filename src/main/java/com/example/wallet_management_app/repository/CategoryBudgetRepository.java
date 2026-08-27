package com.example.wallet_management_app.repository;

import java.util.Optional;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.wallet_management_app.entity.CategoryBudget;
import java.time.YearMonth;

public interface CategoryBudgetRepository extends JpaRepository<CategoryBudget, Long> {
    Optional<CategoryBudget> findByUserIdAndCategoryIdAndTargetMonth(
        Long userId, Long categoryId, YearMonth targetMonth
    );
    boolean existsByUserIdAndCategoryIdAndTargetMonth(
        Long userId, Long categoryId, YearMonth targetMonth
    );
    boolean existsByUserIdAndCategoryId(
        Long userId, Long categoryId
    );

    List<CategoryBudget> findByUserIdAndTargetMonth(
        Long userId, YearMonth targetMonth
    );
}