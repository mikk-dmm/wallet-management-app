package com.example.wallet_management_app.form;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Getter
@NoArgsConstructor
@Setter
public class CategoryForm {

    @NotBlank(message = "name is required")
    @Size(max = 30, message = "category name must be lower than 30 character")
    private String name;

}