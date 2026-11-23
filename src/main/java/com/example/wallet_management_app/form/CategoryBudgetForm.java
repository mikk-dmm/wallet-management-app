package com.example.wallet_management_app.form;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.YearMonth;

@Getter
@NoArgsConstructor
@Setter
public class CategoryBudgetForm {

    @NotNull(message = "カテゴリを選択してください")
    private Long categoryId;

    @NotNull(message = "対象月を選択してください")
    private YearMonth targetMonth;

    @NotNull(message = "予算額を入力してください")
    @Positive(message = "予算額は0より大きい値を入力してください")
    private BigDecimal budgetAmount;
}