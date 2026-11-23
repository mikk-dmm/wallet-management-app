package com.example.wallet_management_app.service;

import java.util.List;
import com.example.wallet_management_app.entity.PaymentMethod;
import com.example.wallet_management_app.entity.User;
import com.example.wallet_management_app.exception.PaymentMethodNotFoundException;
import com.example.wallet_management_app.repository.PaymentMethodRepository;
import com.example.wallet_management_app.repository.UserRepository;

import jakarta.transaction.Transactional;

import com.example.wallet_management_app.repository.ExpenditureRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentMethodService {

    private final PaymentMethodRepository paymentMethodRepository;
    private final UserRepository userRepository;
    private final ExpenditureRepository expenditureRepository;

    public List<PaymentMethod> findPaymentMethods(Long userId) {
        return paymentMethodRepository.findByUserId(userId);
    }

    public PaymentMethod findPaymentMethod(Long userId, Long paymentMethodId) {
        PaymentMethod paymentMethod = paymentMethodRepository.findById(paymentMethodId)
                .orElseThrow(() -> new PaymentMethodNotFoundException("支払い方法が見つかりません"));

        if (!paymentMethod.getUser().getId().equals(userId)) {
            throw new PaymentMethodNotFoundException("支払い方法が見つかりません");
        }

        return paymentMethod;
    }

    @Transactional
    public void createPaymentMethod(Long userId, String name) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("ユーザーが見つかりません"));

        if (paymentMethodRepository.existsByUserIdAndName(userId, name)) {
            throw new IllegalArgumentException("同じ支払い方法がすでに登録されています");
        }

        PaymentMethod paymentMethod = new PaymentMethod();
        paymentMethod.setUser(user);
        paymentMethod.setName(name);

        paymentMethodRepository.save(paymentMethod);
    }

    @Transactional
    public void updatePaymentMethod(Long userId, Long paymentMethodId, String name) {
        PaymentMethod paymentMethod = findPaymentMethod(userId, paymentMethodId);

        if (!paymentMethod.getName().equals(name)
                && paymentMethodRepository.existsByUserIdAndName(userId, name)) {
            throw new IllegalArgumentException("同じ支払い方法がすでに登録されています");
        }

        paymentMethod.setName(name);
        paymentMethodRepository.save(paymentMethod);
    }

    @Transactional
    public void deletePaymentMethod(Long userId, Long paymentMethodId) {
        PaymentMethod paymentMethod = findPaymentMethod(userId, paymentMethodId);

        if (expenditureRepository.existsByUserIdAndPaymentMethodId(userId, paymentMethodId)) {
            throw new IllegalArgumentException("この支払い方法は、支出で使用されているため削除できません");
        }
        paymentMethodRepository.delete(paymentMethod);
    }

}