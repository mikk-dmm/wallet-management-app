package com.example.wallet_management_app.form;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Getter
@NoArgsConstructor
@Setter
public class PaymentMethodForm {

    @NotBlank(message = "name is required")
    @Size(max = 30, message = "payment method name is must be at most 30 characters")
    private String name;

}