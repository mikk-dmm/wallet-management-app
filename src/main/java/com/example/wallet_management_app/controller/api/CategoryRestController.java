package com.example.wallet_management_app.controller.api;

import com.example.wallet_management_app.entity.Category;
import com.example.wallet_management_app.service.CategoryService;
import com.example.wallet_management_app.dto.api.category.CategoryListResponseDto;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import com.example.wallet_management_app.security.CustomUserDetails;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryRestController {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<CategoryListResponseDto>> findAll(
        @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long userId = userDetails.getUserId();

        List<Category> categories = categoryService.getCategories(userId);

        List<CategoryListResponseDto> responses =
            categories.stream().map(category -> new CategoryListResponseDto(
                category.getId(),
                category.getName()
            )).toList();
        
        return ResponseEntity.ok(responses);
    }
}