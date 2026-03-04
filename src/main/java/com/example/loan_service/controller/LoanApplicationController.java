package com.example.loan_service.controller;

import com.example.loan_service.dto.LoanApplicationRequest;
import com.example.loan_service.service.LoanApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/applications")
public class LoanApplicationController {

    private final LoanApplicationService service;

    public LoanApplicationController(LoanApplicationService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> apply(
            @Valid @RequestBody LoanApplicationRequest request) {

        Object response = service.process(request);
        return ResponseEntity.ok(response);
    }
}
