package com.example.wallet_management_app.service;

import com.example.wallet_management_app.entity.Expenditure;
import com.example.wallet_management_app.entity.User;
import com.example.wallet_management_app.entity.Category;
import com.example.wallet_management_app.entity.PaymentMethod;
import com.example.wallet_management_app.repository.CategoryRepository;
import com.example.wallet_management_app.repository.PaymentMethodRepository;
import com.example.wallet_management_app.repository.ExpenditureRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class ExpenditureServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private PaymentMethodRepository paymentMethodRepository;

    @Mock
    private ExpenditureRepository expenditureRepository;

    @InjectMocks
    private ExpenditureService expenditureService;

    @Test
    void updateExpenditure_shouldUpdateExpenditureWhenValidDataInput() {

        // Arrange the initial data

        User user = new User();
        user.setId(1L);
        user.setUsername("testUsername");
        user.setEmail("test@example.com");
        user.setPassword("testPassword");

        Category category = new Category();
        category.setId(1L);
        category.setUser(user);
        category.setName("Test Category");

        PaymentMethod paymentMethod = new PaymentMethod();
        paymentMethod.setId(1L);
        paymentMethod.setUser(user);
        paymentMethod.setName("Test Payment Method");

        Expenditure expenditure = new Expenditure();
        expenditure.setUser(user);
        expenditure.setCategory(category);
        expenditure.setPaymentMethod(paymentMethod);
        expenditure.setName("Test Expenditure");
        expenditure.setAmount(new BigDecimal("100.00"));
        expenditure.setExpenditureDate(LocalDate.of(2023, 1, 1));
        expenditure.setMemo("Test Memo");
        expenditure.setId(1L);

        // Arrange the updated data
        String updatedExpenditureName = "Updated Expenditure";
        BigDecimal updatedAmount = new BigDecimal("150.00");
        LocalDate updatedExpenditureDate = LocalDate.of(2023, 2, 1);
        String updatedMemo = "Updated Memo";
        String updatedCategoryName = "Updated Category";
        String updatedPaymentMethodName = "Updated Payment Method";

        Category updatedCategory = new Category();
        updatedCategory.setId(2L);
        updatedCategory.setUser(user);
        updatedCategory.setName(updatedCategoryName);

        PaymentMethod updatedPaymentMethod = new PaymentMethod();
        updatedPaymentMethod.setId(2L);
        updatedPaymentMethod.setUser(user);
        updatedPaymentMethod.setName(updatedPaymentMethodName);

        // Arrange the mock behavior
        when(expenditureRepository.findById(expenditure.getId())).thenReturn(Optional.of(expenditure));
        when(categoryRepository.findById(updatedCategory.getId())).thenReturn(Optional.of(updatedCategory));
        when(paymentMethodRepository.findById(updatedPaymentMethod.getId())).thenReturn(Optional.of(updatedPaymentMethod));

        // Act
        expenditureService.updateExpenditure(
            user.getId(),
            updatedCategory.getId(),
            updatedPaymentMethod.getId(),
            updatedExpenditureName,
            updatedAmount,
            updatedExpenditureDate,
            updatedMemo,
            expenditure.getId()
        );

        // Assert
        assertEquals(updatedExpenditureName, expenditure.getName());
        assertEquals(updatedAmount, expenditure.getAmount());
        assertEquals(updatedExpenditureDate, expenditure.getExpenditureDate());
        assertEquals(updatedMemo, expenditure.getMemo());
        assertEquals(updatedCategoryName, expenditure.getCategory().getName());
        assertEquals(updatedPaymentMethodName, expenditure.getPaymentMethod().getName());
    }
}