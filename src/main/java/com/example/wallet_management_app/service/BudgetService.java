package com.example.wallet_management_app.service;

import com.example.wallet_management_app.entity.MonthlyBudget;
import com.example.wallet_management_app.entity.CategoryBudget;
import com.example.wallet_management_app.entity.User;
import com.example.wallet_management_app.entity.Category;
import com.example.wallet_management_app.repository.CategoryRepository;
import com.example.wallet_management_app.repository.MonthlyBudgetRepository;
import com.example.wallet_management_app.repository.CategoryBudgetRepository;
import com.example.wallet_management_app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class BudgetService {
    private final MonthlyBudgetRepository monthlyBudgetRepository;
    private final CategoryBudgetRepository categoryBudgetRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    public void saveMonthlyBudget(Long userId, LocalDate targetMonth, Long budgetAmount) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        MonthlyBudget monthlyBudget = monthlyBudgetRepository.findByUserIdAndTargetMonth(userId, targetMonth)
                .orElseGet(() -> {
                    MonthlyBudget newBudget = new MonthlyBudget();
                    newBudget.setUser(user);
                    newBudget.setTargetMonth(targetMonth);
                    return newBudget;
                });
        monthlyBudget.setBudgetAmount(budgetAmount);
        monthlyBudgetRepository.save(monthlyBudget);
    }

    public void saveCategoryBudget(Long userId, Long categoryId, LocalDate targetMonth, Long budgetAmount) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        if (!category.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("Category does not belong to the user");
        }
        CategoryBudget categoryBudget = categoryBudgetRepository.findByUserIdAndCategoryIdAndTargetMonth(userId, categoryId, targetMonth)
                .orElseGet(() -> {
                    CategoryBudget newCategoryBudget = new CategoryBudget();
                    newCategoryBudget.setUser(user);
                    newCategoryBudget.setCategory(category);
                    newCategoryBudget.setTargetMonth(targetMonth);
                    return newCategoryBudget;
                });
        categoryBudget.setBudgetAmount(budgetAmount);
        categoryBudgetRepository.save(categoryBudget);
    }
}