package com.example.wallet_management_app.dto;

import lombok.Getter;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@Getter
public class ExpenditureDisplayDto {

    private Long id;

    private LocalDate expenditureDate;

    private BigDecimal amount;

    private String categoryName;

    private String paymentMethodName;
}