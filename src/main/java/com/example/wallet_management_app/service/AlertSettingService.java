package com.example.wallet_management_app.service;

import com.example.wallet_management_app.entity.AlertSetting;
import com.example.wallet_management_app.entity.User;
import com.example.wallet_management_app.repository.AlertSettingRepository;
import com.example.wallet_management_app.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AlertSettingService {
    private final AlertSettingRepository alertSettingRepository;
    private final UserRepository userRepository;

    public AlertSetting findAlertSettingByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return alertSettingRepository.findByUserId(userId)
                .orElseGet(() -> {
                    AlertSetting newSetting = new AlertSetting();
                    newSetting.setUser(user);
                    newSetting.setMonthlyBudgetAlertEnabled(false);
                    newSetting.setMonthlyBudgetAlertThreshold(0);
                    newSetting.setRemainingAmountAlertEnabled(false);
                    newSetting.setRemainingAmountThreshold(BigDecimal.ZERO);
                    return alertSettingRepository.save(newSetting);
                });
    }

    public void saveAlertSetting(
        Long userId,
        Boolean monthlyBudgetAlertEnabled,
        Integer monthlyBudgetAlertThreshold,
        Boolean remainingAmountAlertEnabled,
        BigDecimal remainingAmountThreshold) {
        AlertSetting setting = findAlertSettingByUserId(userId);

        setting.setMonthlyBudgetAlertEnabled(monthlyBudgetAlertEnabled);
        setting.setMonthlyBudgetAlertThreshold(monthlyBudgetAlertThreshold);
        setting.setRemainingAmountAlertEnabled(remainingAmountAlertEnabled);
        setting.setRemainingAmountThreshold(remainingAmountThreshold);
        alertSettingRepository.save(setting);
    }
}
