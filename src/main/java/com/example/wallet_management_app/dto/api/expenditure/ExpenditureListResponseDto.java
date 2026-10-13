package com.example.wallet_management_app.dto.api.expenditure;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.YearMonth;
import java.math.BigDecimal;
import java.util.List;

@Getter
@AllArgsConstructor
public class ExpenditureListResponseDto {

    private YearMonth targetMonth;

    private BigDecimal monthlyExpenseTotal;

    private List<ExpenditureResponseDto> expenditures;

}