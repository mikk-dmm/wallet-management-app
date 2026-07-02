package com.example.wallet_management_app.service;
import java.util.List;
import com.example.wallet_management_app.entity.Category;
import com.example.wallet_management_app.entity.User;
import com.example.wallet_management_app.repository.CategoryRepository;
import com.example.wallet_management_app.repository.ExpenditureRepository;
import com.example.wallet_management_app.repository.UserRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final ExpenditureRepository expenditureRepository;

    public List<Category> findCategories(Long userId) {
        return categoryRepository.findByUserId(userId);
    }

    public Category findCategory(Long userId, Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        if (!category.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("Category does not belong to the user");
        }

        return category;
    }
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

    public void updateCategory(Long userId, Long categoryId, String name) {
        Category category = findCategory(userId, categoryId);

        if (!category.getName().equals(name)
                && categoryRepository.existsByUserIdAndName(userId, name)) {
            throw new IllegalArgumentException("Category with the same name already exists for this user");
        }

        category.setName(name);
        categoryRepository.save(category);
    }

    public void deleteCategory(Long userId, Long categoryId) {
        Category category = findCategory(userId, categoryId);

        if (expenditureRepository.existsByUserIdAndCategoryId(userId, categoryId)) {
            throw new IllegalArgumentException("Cannot delete category with associated expenditures");
        }
        categoryRepository.delete(category);
    }

}