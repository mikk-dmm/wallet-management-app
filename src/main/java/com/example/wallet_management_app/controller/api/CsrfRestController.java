package com.example.wallet_management_app.controller.api;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.web.csrf.CsrfToken;

@RestController
public class CsrfRestController {

    @GetMapping("/api/csrf")
    public CsrfToken csrf(CsrfToken csrfToken) {

        return csrfToken;
    }
}