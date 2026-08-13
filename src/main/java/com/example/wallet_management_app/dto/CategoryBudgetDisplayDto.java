package com.example.wallet_management_app.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class CategoryBudgetDisplayDto {

    private String categoryName;
    
    private BigDecimal budgetAmount;
}