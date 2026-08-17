package com.example.wallet_management_app.form;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.YearMonth;

@Getter
@NoArgsConstructor
@Setter
public class CategoryBudgetForm {

    @NotNull(message = "Category is required")
    private Long categoryId;

    @NotNull(message = "Target month is required")
    private YearMonth targetMonth;

    @NotNull(message = "Budget amount is required")
    private BigDecimal budgetAmount;
}