package com.example.wallet_management_app.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.wallet_management_app.repository.UserRepository;
import com.example.wallet_management_app.repository.CategoryRepository;
import com.example.wallet_management_app.repository.PaymentMethodRepository;
import com.example.wallet_management_app.entity.User;
import com.example.wallet_management_app.entity.Category;
import com.example.wallet_management_app.entity.PaymentMethod;

import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ExpenditureServiceIntegrationTest {

    @Autowired
    private ExpenditureService expenditureService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    @Test
    public void shouldRollbackCategoryAndPaymentMethodWhenExceptionOccurs() {

        // Given
        User user = new User();
        user.setUsername("test2Username");
        user.setEmail("test2@example.com");
        user.setPassword("test2Password");
        userRepository.save(user);

        Category category = new Category();
        category.setUser(user);
        category.setName("testCategory");
        categoryRepository.save(category);

        PaymentMethod paymentMethod = new PaymentMethod();
        paymentMethod.setUser(user);
        paymentMethod.setName("testPaymentMethod");
        paymentMethodRepository.save(paymentMethod);


        // When
        assertThrows(RuntimeException.class,
            () -> expenditureService.transactionRollbackTest(
                category.getId(),
                paymentMethod.getId()
            )
        );

        // Then
        Category actualCategory = categoryRepository.findById(category.getId()).orElseThrow();
        PaymentMethod actualPaymentMethod = paymentMethodRepository.findById(paymentMethod.getId()).orElseThrow();

        assertEquals("testCategory", actualCategory.getName());
        assertEquals("testPaymentMethod", actualPaymentMethod.getName());

        // Clean up
        paymentMethodRepository.delete(paymentMethod);
        categoryRepository.delete(category);
        userRepository.delete(user);
    }
}