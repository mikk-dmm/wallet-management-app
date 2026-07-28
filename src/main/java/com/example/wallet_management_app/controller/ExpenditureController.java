package com.example.wallet_management_app.controller;

import com.example.wallet_management_app.dto.ExpenditureDisplayDto;
import com.example.wallet_management_app.service.ExpenditureService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.YearMonth;
import java.util.List;
import java.math.BigDecimal;

@Controller
@RequiredArgsConstructor
public class ExpenditureController {
    private final ExpenditureService expenditureService;

    @GetMapping("/expenditures")
    public String showExpenditures(
        @RequestParam(required = false) YearMonth targetMonth,
        Model model
    ) {
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
}
