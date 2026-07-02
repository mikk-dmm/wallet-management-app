package com.example.wallet_management_app.service;

import java.util.List;
import java.time.LocalDate;

import com.example.wallet_management_app.entity.Expenditure;
import com.example.wallet_management_app.entity.User;
import com.example.wallet_management_app.entity.Category;
import com.example.wallet_management_app.entity.PaymentMethod;
import com.example.wallet_management_app.repository.ExpenditureRepository;
import com.example.wallet_management_app.repository.UserRepository;
import com.example.wallet_management_app.repository.CategoryRepository;
import com.example.wallet_management_app.repository.PaymentMethodRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExpenditureService {

    private final ExpenditureRepository expenditureRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final PaymentMethodRepository paymentMethodRepository;

    public List<Expenditure> findExpenditures(Long userId) {
        return expenditureRepository.findByUserId(userId);
    }

    public Expenditure findExpenditure(Long userId, Long expenditureId) {
        Expenditure expenditure = expenditureRepository.findById(expenditureId)
                .orElseThrow(() -> new IllegalArgumentException("Expenditure not found"));

        if (!expenditure.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("Expenditure does not belong to the user");
        }

        return expenditure;
    }

    private Category findOwnedCategory(Long userId, Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        if (!category.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("Category does not belong to the user");
        }

        return category;
    }

    private PaymentMethod findOwnedPaymentMethod(Long userId, Long paymentMethodId) {
        PaymentMethod paymentMethod = paymentMethodRepository.findById(paymentMethodId)
                .orElseThrow(() -> new IllegalArgumentException("Payment method not found"));

        if (!paymentMethod.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("Payment method does not belong to the user");
        }

        return paymentMethod;
    }

    public void createExpenditure(
        Long userId,
        Long categoryId,
        Long paymentMethodId,
        Long amount,
        LocalDate expenditureDate,
        String memo
    ) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Category category = findOwnedCategory(userId, categoryId);

        PaymentMethod paymentMethod = findOwnedPaymentMethod(userId, paymentMethodId);

        Expenditure expenditure = new Expenditure();
        expenditure.setUser(user);
        expenditure.setCategory(category);
        expenditure.setPaymentMethod(paymentMethod);
        expenditure.setAmount(amount);
        expenditure.setExpenditureDate(expenditureDate);
        expenditure.setMemo(memo);

        expenditureRepository.save(expenditure);
    }

    public void updateExpenditure(
        Long userId,
        Long categoryId,
        Long paymentMethodId,
        Long amount,
        LocalDate expenditureDate,
        String memo,
        Long expenditureId
    ) {
        Expenditure expenditure = findExpenditure(userId, expenditureId);

        Category category = findOwnedCategory(userId, categoryId);

        PaymentMethod paymentMethod = findOwnedPaymentMethod(userId, paymentMethodId);

        expenditure.setCategory(category);
        expenditure.setPaymentMethod(paymentMethod);
        expenditure.setAmount(amount);
        expenditure.setExpenditureDate(expenditureDate);
        expenditure.setMemo(memo);

        expenditureRepository.save(expenditure);
    }

    public void deleteExpenditure(Long userId, Long expenditureId) {
        Expenditure expenditure = findExpenditure(userId, expenditureId);
        expenditureRepository.delete(expenditure);
    }
}
