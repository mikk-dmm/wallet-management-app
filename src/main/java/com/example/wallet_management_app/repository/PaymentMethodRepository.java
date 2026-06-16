package com.example.wallet_management_app.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.wallet_management_app.entity.PaymentMethod;

public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Long> {
    List<PaymentMethod> findByUserId(Long userId);
    boolean existsByUserIdAndName(Long userId, String name);
}