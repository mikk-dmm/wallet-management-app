package com.example.wallet_management_app.projection;

import java.math.BigDecimal;

public interface CategoryExpenseSummary {
    Long getCategoryId();
    BigDecimal getExpenseAmount();
}