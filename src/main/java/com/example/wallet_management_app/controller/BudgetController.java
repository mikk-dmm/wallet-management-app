package com.example.wallet_management_app.controller;

import com.example.wallet_management_app.entity.Category;
import com.example.wallet_management_app.form.CategoryBudgetForm;
import com.example.wallet_management_app.service.BudgetService;
import com.example.wallet_management_app.service.CategoryService;
import com.example.wallet_management_app.dto.CategoryBudgetDisplayDto;
import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.validation.BindingResult;

import jakarta.validation.Valid;

import java.time.YearMonth;

@Controller
@RequiredArgsConstructor
@RequestMapping("/budgets")
public class BudgetController {

    private final BudgetService budgetService;
    private final CategoryService categoryService;

    // CategoryBudget Methods
    @GetMapping("/categoryBudget")
    public String showCategoryBudgets(
        @RequestParam(required = false) YearMonth targetMonth,
        Model model
    ) {
        Long userId = 1L;

        if (targetMonth == null) {
            targetMonth = YearMonth.now();
        }

        List<CategoryBudgetDisplayDto> categoryBudgets =
        budgetService.findCategoryBudgets(userId, targetMonth);

        model.addAttribute("categoryBudgets", categoryBudgets);
        model.addAttribute("targetMonth", targetMonth);

        return "categoryBudgetList";
    }

    @GetMapping("/categoryBudget/new")
    public String showCategoryBudgetForm(Model model) {
        
        Long userId = 1L;

        List<Category> categories = categoryService.findCategories(userId);

        CategoryBudgetForm form = new CategoryBudgetForm();

        model.addAttribute("categoryBudgetForm", form);
        model.addAttribute("categories", categories);
        
        return "categoryBudgetForm";
    }

    @PostMapping("/categoryBudget")
    public String createCategoryBudget(
        @ModelAttribute("categoryBudgetForm") @Valid CategoryBudgetForm form,
        BindingResult result,
        Model model,
        RedirectAttributes redirectAttributes) {
        
        Long userId = 1L;

        if (result.hasErrors()) {
            List<Category> categories = categoryService.findCategories(userId);
            model.addAttribute("categories", categories);
            return "categoryBudgetForm";
        }

        
        try {
            budgetService.createCategoryBudget(userId,
            form.getCategoryId(),
            form.getTargetMonth(),
            form.getBudgetAmount()
        );} catch (IllegalArgumentException e) {
            List<Category> categories = categoryService.findCategories(userId);
            
            model.addAttribute("categories", categories);
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());

            return "redirect:/budgets/categoryBudget?targetMonth=" + form.getTargetMonth();
        }

        return "redirect:/budgets/categoryBudget?targetMonth=" + form.getTargetMonth();
    }

}