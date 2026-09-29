package com.example.wallet_management_app.service;

import java.util.List;
import com.example.wallet_management_app.entity.Category;
import com.example.wallet_management_app.entity.User;
import com.example.wallet_management_app.repository.CategoryRepository;
import com.example.wallet_management_app.repository.ExpenditureRepository;
import com.example.wallet_management_app.repository.UserRepository;

import jakarta.transaction.Transactional;

import com.example.wallet_management_app.repository.CategoryBudgetRepository;
import com.example.wallet_management_app.exception.CategoryNotFoundException;
import com.example.wallet_management_app.dto.CategoryDisplayDto;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final ExpenditureRepository expenditureRepository;
    private final CategoryBudgetRepository categoryBudgetRepository;

    // Displayed for CategoryList
    public List<CategoryDisplayDto> findCategories(Long userId) {
        List<Category> categories =
                categoryRepository.findByUserId(userId);
        return categories.stream()
                .map(category -> new CategoryDisplayDto(
                    category.getName()
                ))
                .toList();
    }

    // For get CategoryList
    public List<Category> getCategories(Long userId) {
        return categoryRepository.findByUserId(userId);
    }

    public Category findCategory(Long userId, Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found"));

        if (!category.getUser().getId().equals(userId)) {
            throw new CategoryNotFoundException("Category not found");
        }

        return category;
    }

    @Transactional
    public void createCategory(Long userId, String name) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (categoryRepository.existsByUserIdAndName(userId, name)) {
            throw new IllegalArgumentException("Category with the same name already exists for this user");
        }

        Category category = new Category();
        category.setUser(user);
        category.setName(name);

        categoryRepository.save(category);
    }

    @Transactional
    public void updateCategory(Long userId, Long categoryId, String name) {
        Category category = findCategory(userId, categoryId);

        if (!category.getName().equals(name)
                && categoryRepository.existsByUserIdAndName(userId, name)) {
            throw new IllegalArgumentException("Category with the same name already exists for this user");
        }

        category.setName(name);
        categoryRepository.save(category);
    }

    @Transactional
    public void deleteCategory(Long userId, Long categoryId) {
        Category category = findCategory(userId, categoryId);

        if (expenditureRepository.existsByUserIdAndCategoryId(userId, categoryId)
                    || categoryBudgetRepository.existsByUserIdAndCategoryId(userId, categoryId)
    ) {
            throw new IllegalArgumentException("Cannot delete category with associated expenditures");
        }
        categoryRepository.delete(category);
    }

}