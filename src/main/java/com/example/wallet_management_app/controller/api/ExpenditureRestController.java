package com.example.wallet_management_app.controller.api;

import com.example.wallet_management_app.service.ExpenditureService;
import com.example.wallet_management_app.entity.Expenditure;
import com.example.wallet_management_app.dto.api.expenditure.*;
import com.example.wallet_management_app.security.CustomUserDetails;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;

import java.util.List;
import java.time.YearMonth;

import org.springframework.format.annotation.DateTimeFormat;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequiredArgsConstructor
@RequestMapping("/api/expenditures")
public class ExpenditureRestController {

    private final ExpenditureService expenditureService;

    @GetMapping("/{id}")
    public ResponseEntity<ExpenditureResponseDto> findOne(
        @PathVariable("id") Long expenditureId,
        @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long userId = userDetails.getUserId();

        Expenditure expenditure = expenditureService.findExpenditure(userId, expenditureId);

        ExpenditureResponseDto response = toResponseDto(expenditure);

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ExpenditureResponseDto> create(
        @RequestBody @Valid ExpenditureRequestDto requestDto,
        @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long userId = userDetails.getUserId();

        Expenditure expenditure =
                    expenditureService.createExpenditure(
                        userId,
                        requestDto.getCategoryId(),
                        requestDto.getPaymentMethodId(),
                        requestDto.getName(),
                        requestDto.getAmount(),
                        requestDto.getExpenditureDate(),
                        requestDto.getMemo()
                    );

        ExpenditureResponseDto response = toResponseDto(expenditure);
    
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(
        @PathVariable("id") Long expenditureId,
        @RequestBody @Valid ExpenditureRequestDto requestDto,
        @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long userId = userDetails.getUserId();

        expenditureService.updateExpenditure(
            userId,
            requestDto.getCategoryId(),
            requestDto.getPaymentMethodId(),
            requestDto.getName(),
            requestDto.getAmount(),
            requestDto.getExpenditureDate(),
            requestDto.getMemo(),
            expenditureId
        );

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
        @PathVariable("id") Long expenditureId,
        @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long userId = userDetails.getUserId();

        expenditureService.deleteExpenditure(userId, expenditureId);

        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<ExpenditureResponseDto>> findAll(
        @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM") YearMonth targetMonth,
        @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long userId = userDetails.getUserId();

        if (targetMonth == null) {
            targetMonth = YearMonth.now();
        }
        List<Expenditure> expenditures =
            expenditureService.findExpenditures(
                userId,
                targetMonth
        );
        
        List<ExpenditureResponseDto> responses =
            expenditures.stream().map(expenditure -> toResponseDto(expenditure)).toList();
        
        return ResponseEntity.ok(responses);
    }

    private ExpenditureResponseDto toResponseDto(Expenditure expenditure) {
        return new ExpenditureResponseDto(
            expenditure.getId(),
            expenditure.getName(),
            expenditure.getCategory().getId(),
            expenditure.getCategory().getName(),
            expenditure.getPaymentMethod().getId(),
            expenditure.getPaymentMethod().getName(),
            expenditure.getAmount(),
            expenditure.getExpenditureDate(),
            expenditure.getMemo()
        );
    }
    
}
