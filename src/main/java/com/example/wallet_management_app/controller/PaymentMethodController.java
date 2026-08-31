package com.example.wallet_management_app.controller;

import com.example.wallet_management_app.entity.PaymentMethod;
import com.example.wallet_management_app.service.PaymentMethodService;
import com.example.wallet_management_app.form.PaymentMethodForm;

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
public class PaymentMethodController {

    private final PaymentMethodService paymentMethodService;

    @GetMapping("/paymentMethods")
    public String showPaymetnMethods(Model model) {
        
        Long userId = 1L;

        List<PaymentMethod> paymentMethods = paymentMethodService.findPaymentMethods(userId);

        model.addAttribute("paymentMethods", paymentMethods);

        return "paymentMethodList";
    }

    @GetMapping("/paymentMethod/new")
    public String showPaymentMethodForm(Model model) {

        PaymentMethodForm form = new PaymentMethodForm();
        boolean editMode = false;

        model.addAttribute("paymentMethodForm", form);
        model.addAttribute("editMode", editMode);

        return "paymentMethodForm";
    }

    @PostMapping("/paymentMethods")
    public String createPaymentMethod(
        @Valid @ModelAttribute("paymentMethodForm") PaymentMethodForm form,
        BindingResult result,
        Model model,
        RedirectAttributes redirectAttribute
    ) {

        Long userId = 1L;
        
        boolean editMode = false;

        if (result.hasErrors()) {
            model.addAttribute("editMode", editMode);
            return "paymentMethodForm";
        }

        try {
            paymentMethodService.createPaymentMethod(
                userId,
                form.getName()
            );
        } catch (IllegalArgumentException e) {
            redirectAttribute.addFlashAttribute("errorMessage", e.getMessage());

            return "redirect:/paymentMethods";
        }

        return "redirect:/paymentMethods";
    }

    @GetMapping("/paymentMethod/edit/{id}")
    public String showEditPaymentMethodForm(
        @PathVariable("id") Long paymentMethodId,
        Model model
    ) {
        Long userId = 1L;

        boolean editMode = true;

        PaymentMethod paymentMethod = paymentMethodService.findPaymentMethod(userId, paymentMethodId);
        PaymentMethodForm form = new PaymentMethodForm();

        form.setName(paymentMethod.getName());
        model.addAttribute("paymentMethodForm", form);
        model.addAttribute("editMode", editMode);
        model.addAttribute("id", paymentMethodId);

        return "paymentMethodForm";
    }

    @PostMapping("/paymentMethod/edit/{id}")
    public String updatePaymentMethod(
        @Valid @ModelAttribute("paymentMethodForm") PaymentMethodForm form,
        BindingResult result,
        @PathVariable("id") Long paymentMethodId,
        Model model,
        RedirectAttributes redirectAttribute
    ) {
        
        Long userId = 1L;
        boolean editMode = true;

        if (result.hasErrors()) {
            model.addAttribute("editMode", editMode);
            model.addAttribute("id", paymentMethodId);
            return "paymentMethodForm";
        }

        try {
            paymentMethodService.updatePaymentMethod(
                userId,
                paymentMethodId,
                form.getName());
        } catch (IllegalArgumentException e) {
            redirectAttribute.addFlashAttribute("errorMessage", e.getMessage());
        }
        
        return "redirect:/paymentMethods";
    }

    @PostMapping("/paymentMethod/{id}/delete")
    public String deletePaymentMethod(
        @PathVariable("id") Long paymentMethodId,
        RedirectAttributes redirectAttribute
    ) {
        Long userId = 1L;

        try {
            paymentMethodService.deletePaymentMethod(
                userId,
                paymentMethodId
            );
        } catch (IllegalArgumentException e) {
            redirectAttribute.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/paymentMethods";
    }
}