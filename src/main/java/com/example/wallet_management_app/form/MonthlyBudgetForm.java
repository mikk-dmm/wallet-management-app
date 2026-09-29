package com.example.wallet_management_app.form;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.YearMonth;
import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@Setter
public class MonthlyBudgetForm {

    @NotNull(message = "target month is required")
    private YearMonth targetMonth;

    @NotNull(message = "budget amount is required")
    @Positive(message = "budget amount is must be grater than 0")
    private BigDecimal budgetAmount;
}