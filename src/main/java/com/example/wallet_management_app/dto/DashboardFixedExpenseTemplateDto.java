package com.example.wallet_management_app.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class DashboardFixedExpenseTemplateDto {

    private Long templateId;

    private String name;

    private BigDecimal amount;

    private String paymentMethodName;

    private Integer scheduledDay;
}