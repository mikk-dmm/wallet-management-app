package com.example.wallet_management_app.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.wallet_management_app.entity.FixedExpenseTemplate;

public interface FixedExpenseTemplateRepository extends JpaRepository<FixedExpenseTemplate, Long> {
    List<FixedExpenseTemplate> findByUserId(Long userId);
}