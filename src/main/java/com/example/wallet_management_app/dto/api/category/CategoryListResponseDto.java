package com.example.wallet_management_app.dto.api.category;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CategoryListResponseDto {

    private Long id;

    private String categoryName;
}