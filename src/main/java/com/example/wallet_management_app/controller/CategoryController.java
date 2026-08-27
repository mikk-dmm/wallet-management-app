package com.example.wallet_management_app.controller;

import com.example.wallet_management_app.entity.Category;
import com.example.wallet_management_app.service.CategoryService;
import com.example.wallet_management_app.form.CategoryForm;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.validation.BindingResult;

import jakarta.validation.Valid;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/categories")
    public String showCategories(Model model) {

        Long userId = 1L;

        List<Category> categories = categoryService.getCategories(userId);

        model.addAttribute("categories", categories);

        return "categoryList";
    }

    @GetMapping("/category/new")
    public String showCategoryForm(Model model) {

        CategoryForm form = new CategoryForm();

        boolean editMode = false;
        model.addAttribute("categoryForm", form);
        model.addAttribute("editMode", editMode);

        return "categoryForm";
    }

    @PostMapping("/categories")
    public String createCategory(
        @Valid @ModelAttribute("categoryForm") CategoryForm form,
        BindingResult result,
        Model model,
        RedirectAttributes redirectAttributes
    ) {
        Long userId = 1L;

        boolean editMode = false;

        if (result.hasErrors()) {
            model.addAttribute("editMode", editMode);
            return "categoryForm";
        }

        try {
            categoryService.createCategory(userId, form.getName());
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());

            return "redirect:/categories";
        }

        return "redirect:/categories";
    }

    @GetMapping("/category/edit/{categoryId}")
    public String showEditCategoryForm(
        @PathVariable("categoryId") Long categoryId,
        Model model
    ) {
        Long userId = 1L;

        Category category = categoryService.findCategory(userId, categoryId);
        CategoryForm form = new CategoryForm();
        form.setName(category.getName());
        boolean editMode = true;

        model.addAttribute("categoryForm", form);
        model.addAttribute("editMode", editMode);
        model.addAttribute("categoryId", categoryId);

        return "categoryForm";
    }

    @PostMapping("/category/edit/{categoryId}")
    public String updateCategory(
        @Valid @ModelAttribute("categoryForm") CategoryForm form,
        BindingResult result,
        @PathVariable("categoryId") Long categoryId,
        Model model,
        RedirectAttributes redirectAttributes
    ) {

        Long userId = 1L;
        boolean editMode = true;

        if (result.hasErrors()) {
            model.addAttribute("editMode", editMode);
            model.addAttribute("categoryId", categoryId);
            return "categoryForm";
        }

        try {
            categoryService.updateCategory(
                userId,
                categoryId,
                form.getName()
            );
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/categories";
    }

    @PostMapping("category/{categoryId}/delete")
    public String deleteCategory(
        @PathVariable("categoryId") Long categoryId,
        RedirectAttributes redirectAttributes
    ) {
        
        Long userId = 1L;

        try {
            categoryService.deleteCategory(userId, categoryId);
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        
        return "redirect:/categories";
    }
}