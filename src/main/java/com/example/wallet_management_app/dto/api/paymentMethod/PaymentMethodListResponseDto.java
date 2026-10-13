package com.example.wallet_management_app.dto.api.paymentMethod;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PaymentMethodListResponseDto {

    private Long id;

    private String paymentMethodName;
    
}