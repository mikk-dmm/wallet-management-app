package com.example.wallet_management_app.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.YearMonth;

@Getter
@AllArgsConstructor
public class CategoryBudgetDisplayDto {

    private Long categoryId;

    private String categoryName;
    
    private YearMonth targetMonth;

    private BigDecimal budgetAmount;
}