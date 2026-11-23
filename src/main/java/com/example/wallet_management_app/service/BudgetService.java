package com.example.wallet_management_app.service;

import com.example.wallet_management_app.entity.MonthlyBudget;
import com.example.wallet_management_app.entity.CategoryBudget;
import com.example.wallet_management_app.entity.User;
import com.example.wallet_management_app.entity.Category;
import com.example.wallet_management_app.repository.CategoryRepository;
import com.example.wallet_management_app.repository.MonthlyBudgetRepository;
import com.example.wallet_management_app.repository.CategoryBudgetRepository;
import com.example.wallet_management_app.repository.UserRepository;

import jakarta.transaction.Transactional;

import com.example.wallet_management_app.dto.CategoryBudgetDisplayDto;
import com.example.wallet_management_app.dto.MonthlyBudgetDisplayDto;
import com.example.wallet_management_app.exception.CategoryNotFoundException;

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
                .orElseThrow(() -> new IllegalArgumentException("該当する対象月とユーザーには、月間予算は存在しません"));

        return monthlyBudget;
    }

    @Transactional
    public void createMonthlyBudget(Long userId, YearMonth targetMonth, BigDecimal budgetAmount) {

        User user = userRepository.findById(userId)
                        .orElseThrow(() -> new IllegalArgumentException("ユーザーが存在しません"));

        if (monthlyBudgetRepository.existsByUserIdAndTargetMonth(userId, targetMonth)) {
            throw new IllegalArgumentException("該当の対象月には月間予算がすでに登録されています");
        }

        MonthlyBudget monthlyBudget = new MonthlyBudget();
        monthlyBudget.setUser(user);
        monthlyBudget.setTargetMonth(targetMonth);
        monthlyBudget.setBudgetAmount(budgetAmount);

        monthlyBudgetRepository.save(monthlyBudget);
    }

    @Transactional
    public void updateMonthlyBudget(Long userId, YearMonth targetMonth, BigDecimal budgetAmount) {

        MonthlyBudget monthlyBudget = findMonthlyBudget(userId, targetMonth);

        monthlyBudget.setBudgetAmount(budgetAmount);
        monthlyBudgetRepository.save(monthlyBudget);
    }

    @Transactional
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
                .orElseThrow(() -> new CategoryNotFoundException("カテゴリが見つかりません"));

        if (!category.getUser().getId().equals(userId)) {
            throw new CategoryNotFoundException("カテゴリが見つかりません");
        }

        return category;
    }
    
    public CategoryBudget findCategoryBudget(Long userId, Long categoryId, YearMonth targetMonth) {

        ownedCategoryCheck(userId, categoryId);

        CategoryBudget categoryBudget =
                categoryBudgetRepository.findByUserIdAndCategoryIdAndTargetMonth(userId, categoryId, targetMonth)
                        .orElseThrow(() -> new IllegalArgumentException("該当するカテゴリ・対象月・ユーザーには、カテゴリ予算が存在しません"));

        return categoryBudget;
    }

    @Transactional
    public void createCategoryBudget(
        Long userId,
        Long categoryId,
        YearMonth targetMonth,
        BigDecimal budgetAmount
    ) {
            User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("ユーザーが見つかりません"));

            Category category = ownedCategoryCheck(userId, categoryId);

            if (categoryBudgetRepository
                    .existsByUserIdAndCategoryIdAndTargetMonth(
                        userId,
                        categoryId,
                        targetMonth
                    )) {
                throw new IllegalArgumentException("このカテゴリ予算はすでに設定されています");
            }

            CategoryBudget categoryBudget = new CategoryBudget();
            categoryBudget.setUser(user);
            categoryBudget.setCategory(category);
            categoryBudget.setTargetMonth(targetMonth);
            categoryBudget.setBudgetAmount(budgetAmount);

            categoryBudgetRepository.save(categoryBudget);
    }

    @Transactional
    public void updateCategoryBudget(
        Long userId,
        Long categoryId,
        YearMonth targetMonth,
        BigDecimal budgetAmount
    ) {

            CategoryBudget categoryBudget = findCategoryBudget(userId, categoryId, targetMonth);

            categoryBudget.setBudgetAmount(budgetAmount);
            categoryBudgetRepository.save(categoryBudget);
    }

    @Transactional
    public void deleteCategoryBudget (
        Long userId,
        Long categoryId,
        YearMonth targetMonth
    ) {
            CategoryBudget categoryBudget = findCategoryBudget(userId, categoryId, targetMonth);

            categoryBudgetRepository.delete(categoryBudget);
    }
}