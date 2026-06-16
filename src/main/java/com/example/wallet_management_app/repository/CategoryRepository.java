package com.example.wallet_management_app.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.wallet_management_app.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByUserId(Long userId);
    boolean existsByUserIdAndName(Long userId, String name);
}