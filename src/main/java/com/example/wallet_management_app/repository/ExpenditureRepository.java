package com.example.wallet_management_app.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.wallet_management_app.entity.Expenditure;

public interface ExpenditureRepository extends JpaRepository<Expenditure, Long> {
    List<Expenditure> findByUserId(Long userId);
    boolean existsByUserIdAndCategoryId(Long userId, Long categoryId);
    boolean existsByUserIdAndPaymentMethodId(Long userId, Long paymentMethodId);
}