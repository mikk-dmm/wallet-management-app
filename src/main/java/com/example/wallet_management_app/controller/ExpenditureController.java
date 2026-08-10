package com.example.wallet_management_app.controller;

import com.example.wallet_management_app.dto.ExpenditureDisplayDto;
import com.example.wallet_management_app.service.ExpenditureService;
import com.example.wallet_management_app.service.CategoryService;
import com.example.wallet_management_app.service.PaymentMethodService;
import com.example.wallet_management_app.entity.Expenditure;
import com.example.wallet_management_app.entity.Category;
import com.example.wallet_management_app.entity.PaymentMethod;
import com.example.wallet_management_app.form.ExpenditureForm;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.validation.BindingResult;

import java.time.YearMonth;
import java.util.List;
import java.math.BigDecimal;
import java.time.LocalDate;
import jakarta.validation.Valid;

@Controller
@RequiredArgsConstructor
public class ExpenditureController {

    private final ExpenditureService expenditureService;
    private final CategoryService categoryService;
    private final PaymentMethodService paymentMethodService;

    @GetMapping("/expenditures")
    public String showExpenditures(
        @RequestParam(required = false) YearMonth targetMonth,
        Model model
    ) {
        System.out.println("targetMonth: " + targetMonth);
        Long userId = 1L;

        if (targetMonth == null) {
            targetMonth = YearMonth.now();
        }

        List<ExpenditureDisplayDto> expenditures =
            expenditureService.findExpenditures(userId, targetMonth);

        BigDecimal monthlyExpenseTotal =
                    expenditureService.calculateMonthlyExpenseTotal(userId, targetMonth);

        model.addAttribute("expenditures", expenditures);
        model.addAttribute("targetMonth", targetMonth);
        model.addAttribute("monthlyExpenseTotal", monthlyExpenseTotal);

        return "expenditureList";
    }

    @GetMapping("expenditures/new")
    public String showExpenditureForm(Model model) {
        Long userId = 1L;

        List<Category> categories = categoryService.findCategories(userId);
        List<PaymentMethod> paymentMethods = paymentMethodService.findPaymentMethods(userId);

        ExpenditureForm form = new ExpenditureForm();
        form.setExpenditureDate(LocalDate.now());

        model.addAttribute("expenditureForm", form);
        model.addAttribute("categories", categories);
        model.addAttribute("paymentMethods", paymentMethods);

        return "expenditureForm";
    }

    @PostMapping("/expenditures")
    public String createExpenditure(
        @ModelAttribute("expenditureForm") @Valid ExpenditureForm form,
        BindingResult bindingResult,
        Model model
    ) {
        Long userId = 1L;

        if (bindingResult.hasErrors()) {
            List<Category> categories = categoryService.findCategories(userId);
            List<PaymentMethod> paymentMethods = paymentMethodService.findPaymentMethods(userId);

            model.addAttribute("categories", categories);
            model.addAttribute("paymentMethods", paymentMethods);

            return "expenditureForm";
        }

        expenditureService.createExpenditure(userId,
            form.getCategoryId(),
            form.getPaymentMethodId(),
            form.getName(),
            form.getAmount(),
            form.getExpenditureDate(),
            form.getMemo()
        );

        return "redirect:/expenditures";
    }

    @GetMapping("/expenditures/edit/{expenditureId}")
    public String showEditExpenditureForm(
        @PathVariable("expenditureId") Long expenditureId,
        Model model
    ) {
        Long userId = 1L;

        Expenditure expenditure = expenditureService.findExpenditure(userId, expenditureId);
        ExpenditureForm form = new ExpenditureForm();

        form.setName(expenditure.getName());
        form.setAmount(expenditure.getAmount());
        form.setExpenditureDate(expenditure.getExpenditureDate());
        form.setCategoryId(expenditure.getCategory().getId());
        form.setPaymentMethodId(expenditure.getPaymentMethod().getId());
        form.setMemo(expenditure.getMemo());

        List<Category> categories = categoryService.findCategories(userId);
        List<PaymentMethod> paymentMethods = paymentMethodService.findPaymentMethods(userId);
        model.addAttribute("expenditureForm", form);
        model.addAttribute("categories", categories);
        model.addAttribute("paymentMethods", paymentMethods);
        model.addAttribute("expenditureId", expenditureId);

        return "expenditureForm";
    }

    @PostMapping("/expenditures/edit/{expenditureId}")
    public String updateExpenditure(
        @PathVariable("expenditureId") Long expenditureId,
        @ModelAttribute("expenditureForm") @Valid ExpenditureForm form,
        BindingResult bindingResult,
        Model model
    ) {
        Long userId = 1L;

        if (bindingResult.hasErrors()) {
            List<Category> categories = categoryService.findCategories(userId);
            List<PaymentMethod> paymentMethods = paymentMethodService.findPaymentMethods(userId);

            model.addAttribute("categories", categories);
            model.addAttribute("paymentMethods", paymentMethods);
            model.addAttribute("expenditureId", expenditureId);

            return "expenditureForm";
        }

        expenditureService.updateExpenditure(
            userId,
            form.getCategoryId(),
            form.getPaymentMethodId(),
            form.getName(),
            form.getAmount(),
            form.getExpenditureDate(),
            form.getMemo(),
            expenditureId
        );

        return "redirect:/expenditures";
    }

    @PostMapping("/expenditures/{expenditureId}/delete")
    public String deleteExpenditure(
        @PathVariable("expenditureId") Long expenditureId
    ) {
        Long userId = 1L;

        expenditureService.deleteExpenditure(userId, expenditureId);

        return "redirect:/expenditures";
    }
}
