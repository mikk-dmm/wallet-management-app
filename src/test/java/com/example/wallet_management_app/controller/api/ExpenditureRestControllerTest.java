package com.example.wallet_management_app.controller.api;

import com.example.wallet_management_app.service.ExpenditureService;
import com.example.wallet_management_app.exception.ApiExceptionHandler;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;

import org.springframework.test.web.servlet.MockMvc;

import org.springframework.security.test.context.support.WithMockUser;

import org.springframework.test.context.bean.override.mockito.MockitoBean;

import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(ExpenditureRestController.class)
@Import(ApiExceptionHandler.class)
public class ExpenditureRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ExpenditureService expenditureService;

    @Test
    @WithMockUser
    void create_retruns400_whenAmountIsNegative() throws Exception {

        String requestJson = """
                {
                    "name": "Lunch",
                    "categoryId": 1,
                    "paymentMethodId": 1,
                    "amount": -100,
                    "expenditureDate": "2026-10-13",
                    "memo": ""
                }
                """;

        mockMvc.perform(
            post("/api/expenditures")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
        )
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.amount")
                .value("Amount must be greater than 0"));

    }

    @Test
    @WithMockUser
    void create_returns400_whenNameIsBlank() throws Exception {

        String requestJson = """
                {
                    "name": "",
                    "categoryId": 1,
                    "paymentMethodId": 1,
                    "amount": 100,
                    "expenditureDate": "2026-10-13",
                    "memo": ""
                }
                """;

        mockMvc.perform(
            post("/api/expenditures")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
        )
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.name")
                .value("name is required"));

    }
}