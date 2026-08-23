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
import com.example.wallet_management_app.dto.MonthlyBudgetDisplayDto;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BudgetService {
    private final MonthlyBudgetRepository monthlyBudgetRepository;
    private final CategoryBudgetRepository categoryBudgetRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    // MonthlyBudget Methods
    
    public List<MonthlyBudgetDisplayDto> findMonthlyBudgets(Long userId) {

        List<MonthlyBudget> monthlyBudgets =
                monthlyBudgetRepository.findByUserIdOrderByTargetMonthDesc(userId);

        return monthlyBudgets.stream()
                .map(monthlyBudget ->  new MonthlyBudgetDisplayDto(
                    monthlyBudget.getTargetMonth(),
                    monthlyBudget.getBudgetAmount()
                ))
                .toList();
    }

    public MonthlyBudget findMonthlyBudget(Long userId, YearMonth targetMonth) {

        MonthlyBudget monthlyBudget =
                monthlyBudgetRepository.findByUserIdAndTargetMonth(userId, targetMonth)
                .orElseThrow(() -> new IllegalArgumentException("monthlybudget not found for the specified user and month"));

        return monthlyBudget;
    }

    public void createMonthlyBudget(Long userId, YearMonth targetMonth, BigDecimal budgetAmount) {

        User user = userRepository.findById(userId)
                        .orElseThrow(() -> new IllegalArgumentException("user not found"));

        if (monthlyBudgetRepository.existsByUserIdAndTargetMonth(userId, targetMonth)) {
            throw new IllegalArgumentException("Monthly Budget already exists");
        }

        MonthlyBudget monthlyBudget = new MonthlyBudget();
        monthlyBudget.setUser(user);
        monthlyBudget.setTargetMonth(targetMonth);
        monthlyBudget.setBudgetAmount(budgetAmount);

        monthlyBudgetRepository.save(monthlyBudget);
    }

    public void updateMonthlyBudget(Long userId, YearMonth targetMonth, BigDecimal budgetAmount) {

        MonthlyBudget monthlyBudget = findMonthlyBudget(userId, targetMonth);

        monthlyBudget.setBudgetAmount(budgetAmount);
        monthlyBudgetRepository.save(monthlyBudget);
    }

    public void deleteMonthlyBudget(Long userId, YearMonth targetMonth) {

        MonthlyBudget monthlyBudget = findMonthlyBudget(userId, targetMonth);

        monthlyBudgetRepository.delete(monthlyBudget);
    }

    // CategoryBudget Methods

    public List<CategoryBudgetDisplayDto> findCategoryBudgets(Long userId, YearMonth targetMonth) {

        List<CategoryBudget> categoryBudgets =
                categoryBudgetRepository.findByUserIdAndTargetMonth(userId, targetMonth);
                
        return categoryBudgets.stream()
                .map(categoryBudget -> new CategoryBudgetDisplayDto(
                        categoryBudget.getCategory().getId(),
                        categoryBudget.getCategory().getName(),
                        categoryBudget.getTargetMonth(),
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
    
    public CategoryBudget findCategoryBudget(Long userId, Long categoryId, YearMonth targetMonth) {

        ownedCategoryCheck(userId, categoryId);

        CategoryBudget categoryBudget =
                categoryBudgetRepository.findByUserIdAndCategoryIdAndTargetMonth(userId, categoryId, targetMonth)
                        .orElseThrow(() -> new IllegalArgumentException("Category budget not found for the specified user, category, and month"));

        return categoryBudget;
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

            CategoryBudget categoryBudget = findCategoryBudget(userId, categoryId, targetMonth);

            categoryBudget.setBudgetAmount(budgetAmount);
            categoryBudgetRepository.save(categoryBudget);
        }

    public void deleteCategoryBudget(
        Long userId,
        Long categoryId,
        YearMonth targetMonth) {
            CategoryBudget categoryBudget = findCategoryBudget(userId, categoryId, targetMonth);

            categoryBudgetRepository.delete(categoryBudget);
        }
}