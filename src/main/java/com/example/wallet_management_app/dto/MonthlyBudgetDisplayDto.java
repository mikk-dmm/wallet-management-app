package com.example.wallet_management_app.dto;

import lombok.Getter;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.YearMonth;

@Getter
@AllArgsConstructor
public class MonthlyBudgetDisplayDto {

    YearMonth targetMonth;

    BigDecimal budgetAmount;
}