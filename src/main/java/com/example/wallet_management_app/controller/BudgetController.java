package com.example.wallet_management_app.controller;

import com.example.wallet_management_app.entity.Category;
import com.example.wallet_management_app.entity.CategoryBudget;
import com.example.wallet_management_app.entity.MonthlyBudget;
import com.example.wallet_management_app.form.CategoryBudgetForm;
import com.example.wallet_management_app.form.MonthlyBudgetForm;
import com.example.wallet_management_app.service.BudgetService;
import com.example.wallet_management_app.service.CategoryService;
import com.example.wallet_management_app.dto.CategoryBudgetDisplayDto;
import com.example.wallet_management_app.dto.MonthlyBudgetDisplayDto;
import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
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

    // MonthlyBudget Methods
    @GetMapping("/monthlyBudget")
    public String showMonthlyBudgets(Model model) {
        Long userId = 1L;

        List<MonthlyBudgetDisplayDto> monthlyBudgets =
                budgetService.findMonthlyBudgets(userId);

        model.addAttribute("monthlyBudgets", monthlyBudgets);

        return "monthlyBudgetList";
    }

    @GetMapping("/monthlyBudget/new")
    public String showMonthlyBudgetForm(Model model) {

        MonthlyBudgetForm form = new MonthlyBudgetForm();

        model.addAttribute("monthlyBudgetForm", form);

        return "monthlyBudgetForm";
    }

    @PostMapping("/monthlyBudget")
    public String createMonthlyBudget(
        @ModelAttribute("monthlyBudgetForm") @Valid MonthlyBudgetForm form,
        BindingResult result,
        Model model,
        RedirectAttributes redirectAttributes
    ) {
        
        Long userId = 1L;

        if (result.hasErrors()) {
            model.addAttribute("monthlyBudgetForm", form);
            return "monthlyBudgetForm";
        }

        try {
            budgetService.createMonthlyBudget(userId,
                form.getTargetMonth(),
                form.getBudgetAmount()
            );
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());

            return "redirect:/budgets/monthlyBudget";
        }

        return "redirect:/budgets/monthlyBudget";
    }

    @GetMapping("/monthlyBudget/edit/{targetMonth}")
    public String showEditMonthlyBudgetForm(
        @PathVariable("targetMonth") YearMonth targetMonth,
        Model model
    ) {

        Long userId = 1L;
        MonthlyBudget monthlyBudget = budgetService.findMonthlyBudget(userId, targetMonth);
        MonthlyBudgetForm form = new MonthlyBudgetForm();

        form.setTargetMonth(monthlyBudget.getTargetMonth());
        form.setBudgetAmount(monthlyBudget.getBudgetAmount());

        model.addAttribute("monthlyBudgetForm", form);

        return "monthlyBudgetForm";
    }

    @PostMapping("/monthlyBudget/edit/{targetMonth}")
    public String updateMonthlyBudget(
        @PathVariable("targetMonth") YearMonth targetMonth,
        @ModelAttribute("monthlyBudgetForm") @Valid MonthlyBudgetForm form,
        BindingResult result,
        Model model
    ) {
        Long userId = 1L;

        if (result.hasErrors()) {
            model.addAttribute("monthlyBudgetForm", form);

            return "monthlyBudgetForm";
        }

        budgetService.updateMonthlyBudget(
            userId,
            targetMonth,
            form.getBudgetAmount()
        );
        
        return "redirect:/budgets/monthlyBudget";
    }

    @PostMapping("/monthlyBudget/{targetMonth}/delete")
    public String deleteMonthlyBudget(
        @PathVariable("targetMonth") YearMonth targetMonth
    ) {
        Long userId = 1L;

        budgetService.deleteMonthlyBudget(userId, targetMonth);

        return "redirect:/budgets/monthlyBudget";
    }

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

        List<Category> categories = categoryService.getCategories(userId);

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
            List<Category> categories = categoryService.getCategories(userId);
            model.addAttribute("categories", categories);
            return "categoryBudgetForm";
        }

        
        try {
            budgetService.createCategoryBudget(userId,
            form.getCategoryId(),
            form.getTargetMonth(),
            form.getBudgetAmount()
        );} catch (IllegalArgumentException e) {
            List<Category> categories = categoryService.getCategories(userId);

            model.addAttribute("categories", categories);
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());

            return "redirect:/budgets/categoryBudget?targetMonth=" + form.getTargetMonth();
        }

        return "redirect:/budgets/categoryBudget?targetMonth=" + form.getTargetMonth();
    }

    @GetMapping("/categoryBudget/edit/categories/{categoryId}/month/{targetMonth}")
    public String showEditCategoryBudgetForm(
        @PathVariable("categoryId") Long categoryId,
        @PathVariable("targetMonth") YearMonth targetMonth,
        Model model
    ) {
        Long userId = 1L;

        CategoryBudget categoryBudget = budgetService.findCategoryBudget(userId, categoryId, targetMonth);
        CategoryBudgetForm form = new CategoryBudgetForm();

        form.setCategoryId(categoryBudget.getCategory().getId());
        form.setTargetMonth(categoryBudget.getTargetMonth());
        form.setBudgetAmount(categoryBudget.getBudgetAmount());
        
        List<Category> categories = categoryService.getCategories(userId);
        model.addAttribute("categoryBudgetForm", form);
        model.addAttribute("categories", categories);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("targetMonth", targetMonth);

        return "categoryBudgetForm";
    }

    @PostMapping("/categoryBudget/edit/categories/{categoryId}/month/{targetMonth}")
    public String updateCategoryBudget(
        @PathVariable("categoryId") Long categoryId,
        @PathVariable("targetMonth") YearMonth targetMonth,
        @ModelAttribute("categoryBudgetForm") @Valid CategoryBudgetForm form,
        BindingResult bindingresult,
        Model model
    ) {
        Long userId = 1L;

        if (bindingresult.hasErrors()) {
            List<Category> categories = categoryService.getCategories(userId);

            model.addAttribute("categoryId", categoryId);
            model.addAttribute("targetMonth", targetMonth);
            model.addAttribute("categories", categories);

            return "categoryBudgetForm";
        }

        budgetService.updateCategoryBudget(
            userId,
            categoryId,
            targetMonth,
            form.getBudgetAmount()
        );

        return "redirect:/budgets/categoryBudget?targetMonth=" + form.getTargetMonth();
    }

    @PostMapping("/categoryBudget/categories/{categoryId}/month/{targetMonth}/delete")
    public String deleteCategoryBudget(
        @PathVariable("categoryId") Long categoryId,
        @PathVariable("targetMonth") YearMonth targetMonth
    ) {
        Long userId = 1L;

        budgetService.deleteCategoryBudget(userId, categoryId, targetMonth);

        return "redirect:/budgets/categoryBudget?targetMonth=" + targetMonth;
    }
}