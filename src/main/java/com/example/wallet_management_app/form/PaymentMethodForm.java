package com.example.wallet_management_app.form;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
@Setter
public class PaymentMethodForm {

    @NotBlank(message = "name is requried")
    private String name;

}