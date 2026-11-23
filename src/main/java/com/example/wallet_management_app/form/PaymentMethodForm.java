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

    @NotBlank(message = "支払い方法を入力してください")
    @Size(max = 30, message = "支払い方法は30字以内で入力してください")
    private String name;

}