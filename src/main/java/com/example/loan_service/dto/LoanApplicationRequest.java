package com.example.loan_service.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class LoanApplicationRequest {

    @Valid
    @NotNull
    private Applicant applicant;

    @Valid
    @NotNull
    private Loan loan;

    @Data
    public static class Applicant {

        @NotBlank
        private String name;

        @Min(21)
        @Max(60)
        private int age;

        @DecimalMin("0.01")
        private BigDecimal monthlyIncome;

        @NotNull
        private EmploymentType employmentType;

        @Min(300)
        @Max(900)
        private int creditScore;
    }

    @Data
    public static class Loan {

        @DecimalMin("10000")
        @DecimalMax("5000000")
        private BigDecimal amount;

        @Min(6)
        @Max(360)
        private int tenureMonths;

        @NotNull
        private LoanPurpose purpose;
    }
}
