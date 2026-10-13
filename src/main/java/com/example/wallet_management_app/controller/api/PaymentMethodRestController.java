package com.example.wallet_management_app.controller.api;

import com.example.wallet_management_app.entity.PaymentMethod;
import com.example.wallet_management_app.service.PaymentMethodService;
import com.example.wallet_management_app.dto.api.paymentMethod.PaymentMethodListResponseDto;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import com.example.wallet_management_app.security.CustomUserDetails;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/payment-methods")
@RequiredArgsConstructor
public class PaymentMethodRestController {

    private final PaymentMethodService paymentMethodService;

    @GetMapping
    public ResponseEntity<List<PaymentMethodListResponseDto>> findAll(
        @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long userId = userDetails.getUserId();

        List<PaymentMethod> paymentMethods = paymentMethodService.findPaymentMethods(userId);

        List<PaymentMethodListResponseDto> responses =
            paymentMethods.stream().map(paymentMethod -> new PaymentMethodListResponseDto(
                paymentMethod.getId(),
                paymentMethod.getName()
            )).toList();
        
        return ResponseEntity.ok(responses);
    }
}