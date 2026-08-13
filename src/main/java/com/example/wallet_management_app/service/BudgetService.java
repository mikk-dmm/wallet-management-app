package com.example.wallet_management_app.service;

import com.example.wallet_management_app.entity.MonthlyBudget;
import com.example.wallet_management_app.entity.CategoryBudget;
import com.example.wallet_management_app.entity.User;
import com.example.wallet_management_app.entity.Category;
import com.example.wallet_management_app.repository.CategoryRepository;
import com.example.wallet_management_app.repository.MonthlyBudgetRepository;
import com.example.wallet_management_app.repository.CategoryBudgetRepository;
import com.example.wallet_management_app.repository.UserRepository;
import com.example.wallet_management_app.dto.CategoryBudgetDisplayDto;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BudgetService {
    private final MonthlyBudgetRepository monthlyBudgetRepository;
    private final CategoryBudgetRepository categoryBudgetRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    // MonthlyBudget Methods
    public void saveMonthlyBudget(Long userId, YearMonth targetMonth, BigDecimal budgetAmount) {
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

    // CategoryBudget Methods
    public List<CategoryBudgetDisplayDto> findCategoryBudgets(Long userId, YearMonth targetMonth) {

        List<CategoryBudget> categoryBudgets =
                categoryBudgetRepository.findByUserIdAndTargetMonth(userId, targetMonth);
                
        return categoryBudgets.stream()
                .map(categoryBudget -> new CategoryBudgetDisplayDto(
                        categoryBudget.getCategory().getName(),
                        categoryBudget.getBudgetAmount()
                ))
                .toList();
    }

    public Category ownedCategoryCheck(Long userId, Long categoryId) {

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        if (!category.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("Category does not belong to the user");
        }

        return category;
    }
    
    public Optional<CategoryBudget> findCategoryBudget(Long userId, Long categoryId, YearMonth targetMonth) {

        ownedCategoryCheck(userId, categoryId);

        CategoryBudget categoryBudget =
                categoryBudgetRepository.findByUserIdAndCategoryIdAndTargetMonth(userId, categoryId, targetMonth)
                        .orElseThrow(() -> new IllegalArgumentException("Category budget not found for the specified user, category, and month"));

        return Optional.of(categoryBudget);
    }

    public void createCategoryBudget(
        Long userId,
        Long categoryId,
        YearMonth targetMonth,
        BigDecimal budgetAmount) {
        
            User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

            Category category = ownedCategoryCheck(userId, categoryId);

            if (categoryBudgetRepository
                    .existsByUserIdAndCategoryIdAndTargetMonth(
                        userId,
                        categoryId,
                        targetMonth
                    )) {
                throw new IllegalArgumentException("this category budget is already exists");
            }
            
            CategoryBudget categoryBudget = new CategoryBudget();
            categoryBudget.setUser(user);
            categoryBudget.setCategory(category);
            categoryBudget.setTargetMonth(targetMonth);
            categoryBudget.setBudgetAmount(budgetAmount);

            categoryBudgetRepository.save(categoryBudget);
        }

    public void updateCategoryBudget(
        Long userId,
        Long categoryId,
        YearMonth targetMonth,
        BigDecimal budgetAmount) {

            CategoryBudget categoryBudget = findCategoryBudget(userId, categoryId, targetMonth)
                    .orElseThrow(() -> new IllegalArgumentException("Category budget not found for the specified user, category, and month"));

            categoryBudget.setBudgetAmount(budgetAmount);
            categoryBudgetRepository.save(categoryBudget);
        }

    public void deleteCategoryBudet(
        Long userId,
        Long categoryId,
        YearMonth targetMonth) {
            CategoryBudget categoryBudget = findCategoryBudget(userId, categoryId, targetMonth)
                    .orElseThrow(() -> new IllegalArgumentException("Category budget not found for the specified user, category, and month"));

            categoryBudgetRepository.delete(categoryBudget);
        }
}