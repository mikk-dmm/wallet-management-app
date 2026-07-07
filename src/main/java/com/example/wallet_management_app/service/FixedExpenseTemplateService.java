package com.example.wallet_management_app.service;

import java.util.List;
import com.example.wallet_management_app.entity.FixedExpenseTemplate;
import com.example.wallet_management_app.entity.User;
import com.example.wallet_management_app.entity.Category;
import com.example.wallet_management_app.entity.PaymentMethod;
import com.example.wallet_management_app.repository.UserRepository;
import com.example.wallet_management_app.repository.CategoryRepository;
import com.example.wallet_management_app.repository.PaymentMethodRepository;
import com.example.wallet_management_app.repository.FixedExpenseTemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FixedExpenseTemplateService {
    private final FixedExpenseTemplateRepository fixedExpenseTemplateRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final PaymentMethodRepository paymentMethodRepository;

    public List<FixedExpenseTemplate> findExpenseTemplates(Long userId) {
        return fixedExpenseTemplateRepository.findByUserId(userId);
    }

    public FixedExpenseTemplate findExpenseTemplate(Long userId, Long templateId) {
        FixedExpenseTemplate template = fixedExpenseTemplateRepository.findById(templateId)
                .orElseThrow(() -> new IllegalArgumentException("Fixed expense template not found"));
        if (!template.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("User is not authorized to access this template");
        }

        return template;
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

    public void createFixedExpenseTemplate(
        Long userId,
        Long categoryId,
        Long paymentMethodId,
        String name,
        Long amount,
        Integer scheduledDay,
        String memo
    ) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Category category = findOwnedCategory(userId, categoryId);
        PaymentMethod paymentMethod = findOwnedPaymentMethod(userId, paymentMethodId);

        FixedExpenseTemplate template = new FixedExpenseTemplate();
        template.setUser(user);
        template.setCategory(category);
        template.setPaymentMethod(paymentMethod);
        template.setName(name);
        template.setAmount(amount);
        template.setScheduledDay(scheduledDay);
        template.setMemo(memo);

        fixedExpenseTemplateRepository.save(template);
    }

    public void updateFixedExpenseTemplate(
            Long userId,
            Long templateId,
            Long categoryId,
            Long paymentMethodId,
            String name,
            Long amount,
            Integer scheduledDay,
            String memo
    ) {
        FixedExpenseTemplate template = findExpenseTemplate(userId, templateId);
        Category category = findOwnedCategory(userId, categoryId);
        PaymentMethod paymentMethod = findOwnedPaymentMethod(userId, paymentMethodId);

        template.setCategory(category);
        template.setPaymentMethod(paymentMethod);
        template.setName(name);
        template.setAmount(amount);
        template.setScheduledDay(scheduledDay);
        template.setMemo(memo);
        fixedExpenseTemplateRepository.save(template);
    }

    public void deleteFixedExpenseTemplate(Long userId, Long templateId) {
        FixedExpenseTemplate template = findExpenseTemplate(userId, templateId);
        fixedExpenseTemplateRepository.delete(template);
    }
}