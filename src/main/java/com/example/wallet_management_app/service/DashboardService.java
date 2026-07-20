package com.example.wallet_management_app.service;

import com.example.wallet_management_app.entity.CategoryBudget;
import com.example.wallet_management_app.entity.MonthlyBudget;
import com.example.wallet_management_app.entity.FixedExpenseTemplate;
import com.example.wallet_management_app.entity.AlertSetting;
import com.example.wallet_management_app.projection.CategoryExpenseSummary;
import com.example.wallet_management_app.repository.UserRepository;
import com.example.wallet_management_app.repository.MonthlyBudgetRepository;
import com.example.wallet_management_app.repository.CategoryBudgetRepository;
import com.example.wallet_management_app.repository.ExpenditureRepository;
import com.example.wallet_management_app.repository.FixedExpenseTemplateRepository;
import com.example.wallet_management_app.dto.DashboardDto;
import com.example.wallet_management_app.dto.CategoryProgressDto;
import com.example.wallet_management_app.dto.DashboardFixedExpenseTemplateDto;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;
import java.math.BigDecimal;
import java.math.RoundingMode;

import java.time.YearMonth;
import java.time.LocalDate;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final UserRepository userRepository;
    private final MonthlyBudgetRepository monthlyBudgetRepository;
    private final ExpenditureRepository expenditureRepository;
    private final CategoryBudgetRepository categoryBudgetRepository;
    private final FixedExpenseTemplateRepository fixedExpenseTemplateRepository;

    private final AlertSettingService alertSettingService;

    public DashboardDto getDashboard(Long userId, YearMonth targetMonth) {

        userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        
        int startDay = 1;

        LocalDate periodStart = calculatePeriodStart(targetMonth, startDay);
        LocalDate periodEnd = calculatePeriodEnd(targetMonth, startDay);

        BigDecimal budgetAmount = getMonthlyBudget(userId, targetMonth);
        BigDecimal expenseTotal = getExpenseTotal(userId, periodStart, periodEnd);
        BigDecimal remainingAmount = calculateRemainingAmount(budgetAmount, expenseTotal);

        List<CategoryProgressDto> categoryProgress = getCategoryProgress(
                                                        userId,
                                                        targetMonth,
                                                        periodStart,
                                                        periodEnd
        );
        List<DashboardFixedExpenseTemplateDto> fixedExpenseTemplates = getFixedExpenseTemplates(userId);
        List<String> alertMessages = createAlertMessages(
                                            userId,
                                            budgetAmount,
                                            expenseTotal,
                                            remainingAmount
        );
        return new DashboardDto(
                targetMonth,
                periodStart,
                periodEnd,
                budgetAmount,
                expenseTotal,
                remainingAmount,
                alertMessages,
                categoryProgress,
                fixedExpenseTemplates
        );
    }

    private LocalDate calculatePeriodStart(
        YearMonth targetMonth,
        int startDay
    ) {
        return targetMonth.atDay(startDay);
    }

    private LocalDate calculatePeriodEnd(
        YearMonth targetMonth,
        int startDay
    ) {
        return targetMonth.plusMonths(1)
                        .atDay(startDay)
                        .minusDays(1);
    }

    private BigDecimal getMonthlyBudget(
        Long userId,
        YearMonth targetMonth
    ) {
        return monthlyBudgetRepository
                    .findByUserIdAndTargetMonth(userId, targetMonth)
                    .map(MonthlyBudget::getBudgetAmount)
                    .orElse(BigDecimal.ZERO);
    }

    private BigDecimal getExpenseTotal(
        Long userId,
        LocalDate periodStart,
        LocalDate periodEnd
    ) {
        return expenditureRepository
                .sumAmountByUserIdAndDateBetween(
                        userId,
                        periodStart,
                        periodEnd
                )
                .orElse(BigDecimal.ZERO);
    }

    private BigDecimal calculateRemainingAmount(
        BigDecimal budgetAmount,
        BigDecimal expenseTotal
    ) {
        return budgetAmount.subtract(expenseTotal);
    }

    private List<CategoryProgressDto> getCategoryProgress(
            Long userId,
            YearMonth targetMonth,
            LocalDate periodStart,
            LocalDate periodEnd
    ) {
        List<CategoryBudget> categoryBudgets = categoryBudgetRepository.findByUserIdAndTargetMonth(userId, targetMonth);
        List<CategoryExpenseSummary> expenseSummaries = expenditureRepository.sumCategoryAmountByUserIdAndDateBetween(userId, periodStart, periodEnd);
        Map<Long, BigDecimal> expenseMap =
                expenseSummaries.stream()
                        .collect(Collectors.toMap(
                                CategoryExpenseSummary::getCategoryId,
                                CategoryExpenseSummary::getExpenseAmount
                        ));

        return categoryBudgets.stream()
                        .map(categoryBudget -> {
                            Long categoryId = categoryBudget.getCategory().getId();
                            String categoryName = categoryBudget.getCategory().getName();
                            BigDecimal categoryBudgetAmount = categoryBudget.getBudgetAmount();

                            BigDecimal expenseAmount =
                                    expenseMap.getOrDefault(
                                            categoryId,
                                            BigDecimal.ZERO
                                    );
                            
                            BigDecimal remainingAmount = categoryBudgetAmount.subtract(expenseAmount);

                            int progressRate;

                            if (categoryBudgetAmount.compareTo(BigDecimal.ZERO) <= 0) {
                                progressRate = 0;
                            } else {
                                progressRate = expenseAmount
                                            .multiply(BigDecimal.valueOf(100))
                                            .divide(categoryBudgetAmount, 0, RoundingMode.HALF_UP).intValue();
                            }
                            
                            return new CategoryProgressDto(
                                categoryName,
                                categoryBudgetAmount,
                                expenseAmount,
                                remainingAmount,
                                progressRate
                            );
                        }).toList();
    }

    private List<DashboardFixedExpenseTemplateDto> getFixedExpenseTemplates(Long userId) {
        List<FixedExpenseTemplate> fixedExpenseTemplates = fixedExpenseTemplateRepository.findByUserId(userId);
        return fixedExpenseTemplates.stream()
                                    .map(template -> {
                                        Long templateId = template.getId();
                                        String name = template.getName();
                                        BigDecimal amount = template.getAmount();
                                        String paymentMethodName = template.getPaymentMethod().getName();
                                        Integer scheduledDay = template.getScheduledDay();

                                        return new DashboardFixedExpenseTemplateDto(
                                            templateId,
                                            name,
                                            amount,
                                            paymentMethodName,
                                            scheduledDay
                                        );
                                    }).toList();
    }

    private List<String> createAlertMessages(
            Long userId,
            BigDecimal budgetAmount,
            BigDecimal expenseTotal,
            BigDecimal remainingAmount
    ) {
        AlertSetting alertSetting =
            alertSettingService.findAlertSettingByUserId(userId);

        List<String> alertMessages = new ArrayList<>();

        if (alertSetting.getMonthlyBudgetAlertEnabled()) {
            if (budgetAmount.compareTo(BigDecimal.ZERO) > 0) {
                int usageRate = expenseTotal.multiply(BigDecimal.valueOf(100))
                            .divide(budgetAmount, 0, RoundingMode.HALF_UP).intValue();
                Integer threshold = alertSetting.getMonthlyBudgetAlertThreshold();
                if (usageRate >= threshold) {
                    alertMessages.add("月予算の" + threshold + "%に達しています");
                }
            }
        }

        if (alertSetting.getRemainingAmountAlertEnabled()) {
            BigDecimal threshold = alertSetting.getRemainingAmountThreshold();
            
            if (remainingAmount.compareTo(threshold) <= 0) {
                alertMessages.add(
                            "残り予算が少なくなっています（現在：" + remainingAmount + "円）");
            }
        }
    return alertMessages;
    }
}