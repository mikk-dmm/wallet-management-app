package com.example.wallet_management_app.controller;

import com.example.wallet_management_app.dto.DashboardDto;
import com.example.wallet_management_app.service.DashboardService;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.YearMonth;

@Controller
@RequiredArgsConstructor
@RequestMapping("/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping
    public String showDashboard(
        @RequestParam(required = false)
        @DateTimeFormat(pattern = "yyyy-MM")
        YearMonth targetMonth, Model model) {
            
            // TODO:認証ユーザーからuserIdを取得する
            Long userId = 1L;

            YearMonth displayMonth = targetMonth != null ? targetMonth : YearMonth.now();

            DashboardDto dashboard = dashboardService.getDashboard(userId, displayMonth);

            model.addAttribute("dashboard", dashboard);

            return "dashboard";
        }
}