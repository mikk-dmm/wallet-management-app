package com.example.wallet_management_app.dto.api.expenditure;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ExpenditureResponseDto {

    private Long id;

    private String name;

    private Long categoryId;

    private String categoryName;

    private Long paymentMethodId;

    private String paymentMethodName;

    private BigDecimal amount;

    private LocalDate expenditureDate;

    private String memo;
}