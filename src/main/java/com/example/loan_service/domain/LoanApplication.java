package com.example.loan_service.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Data
public class LoanApplication {

    @Id
    private UUID id;

    private String applicantName;
    private int age;
    private BigDecimal monthlyIncome;
    private int creditScore;

    @Enumerated(EnumType.STRING)
    private EmploymentType employmentType;

    private BigDecimal loanAmount;
    private int tenureMonths;

    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;

    @Enumerated(EnumType.STRING)
    private RiskBand riskBand;

    private BigDecimal interestRate;
    private BigDecimal emi;
    private BigDecimal totalPayable;
}
