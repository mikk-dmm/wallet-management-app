package com.example.wallet_management_app.form;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Setter
@NoArgsConstructor
public class ExpenditureForm {

    @NotBlank(message = "name is required")
    @Size(max = 30, message = "expenditure name must be at most 30 characters")
    private String name;

    @NotNull(message = "Category is required")
    private Long categoryId;

    @NotNull(message = "Payment method is required")
    private Long paymentMethodId;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be grater than 0")
    private BigDecimal amount;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @NotNull(message = "Expenditure date is required")
    private LocalDate expenditureDate;

    @Size(max = 255, message = "Memo must be at most 255 characters")
    private String memo;
}