package com.example.wallet_management_app.form;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
@Setter
public class CategoryForm {

    @NotBlank(message = "name is required")
    private String name;

}