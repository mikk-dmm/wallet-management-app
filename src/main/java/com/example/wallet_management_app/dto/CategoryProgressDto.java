package com.example.wallet_management_app.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class CategoryProgressDto {
    private String categoryName;

    private BigDecimal budgetAmount;

    private BigDecimal expenseAmount;

    private BigDecimal remainingAmount;

    private int progressRate; // 0~100
}