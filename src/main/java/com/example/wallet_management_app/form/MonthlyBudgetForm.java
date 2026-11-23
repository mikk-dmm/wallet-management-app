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

    @NotNull(message = "対象月を選択してください")
    private YearMonth targetMonth;

    @NotNull(message = "予算額を入力してください")
    @Positive(message = "予算額は0より大きい値を入力してください")
    private BigDecimal budgetAmount;
}