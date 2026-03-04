package com.example.loan_service.dto;

import com.example.loan_service.domain.EmploymentType;
import com.example.loan_service.domain.LoanPurpose;
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

    public Applicant getApplicant() {
        return applicant;
    }

    public void setApplicant(Applicant applicant) {
        this.applicant = applicant;
    }

    public Loan getLoan() {
        return loan;
    }

    public void setLoan(Loan loan) {
        this.loan = loan;
    }

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

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public BigDecimal getMonthlyIncome() {
            return monthlyIncome;
        }

        public void setMonthlyIncome(BigDecimal monthlyIncome) {
            this.monthlyIncome = monthlyIncome;
        }

        public EmploymentType getEmploymentType() {
            return employmentType;
        }

        public void setEmploymentType(EmploymentType employmentType) {
            this.employmentType = employmentType;
        }

        public int getCreditScore() {
            return creditScore;
        }

        public void setCreditScore(int creditScore) {
            this.creditScore = creditScore;
        }
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

        public BigDecimal getAmount() {
            return amount;
        }

        public void setAmount(BigDecimal amount) {
            this.amount = amount;
        }

        public int getTenureMonths() {
            return tenureMonths;
        }

        public void setTenureMonths(int tenureMonths) {
            this.tenureMonths = tenureMonths;
        }

        public LoanPurpose getPurpose() {
            return purpose;
        }

        public void setPurpose(LoanPurpose purpose) {
            this.purpose = purpose;
        }
    }
}
