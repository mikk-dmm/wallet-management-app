package com.example.wallet_management_app.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor
public class DashboardDto {
    
    private YearMonth targetMonth;

    private LocalDate periodStart;
    private LocalDate periodEnd;

    private BigDecimal monthlyBudget;

    private BigDecimal monthlyExpenseTotal;

    private BigDecimal remainingAmount;

    private List<String> alertMessages;

    private List<CategoryProgressDto> categoryProgressList;

    private List<DashboardFixedExpenseTemplateDto> fixedExpenseTemplates;
}