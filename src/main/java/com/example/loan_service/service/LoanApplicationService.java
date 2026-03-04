package com.example.loan_service.service;

import com.example.loan_service.domain.ApplicationStatus;
import com.example.loan_service.domain.EmploymentType;
import com.example.loan_service.domain.LoanApplication;
import com.example.loan_service.domain.RiskBand;
import com.example.loan_service.dto.LoanApplicationRequest;
import com.example.loan_service.repository.LoanApplicationRepository;
import com.example.loan_service.util.EmiCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class LoanApplicationService {

    private final LoanApplicationRepository repository;

    private static final BigDecimal BASE_RATE = BigDecimal.valueOf(12);

    public LoanApplicationService(LoanApplicationRepository repository) {
        this.repository = repository;
    }

    public Object process(LoanApplicationRequest request) {

        List<String> rejectionReasons = new ArrayList<>();

        //reject if credit score < 600
        if (request.getApplicant().getCreditScore() < 600) {
            rejectionReasons.add("CREDIT_SCORE_BELOW_600");
        }

        //Reject if age + tenure > 65
        int age = request.getApplicant().getAge();
        int tenureYears = request.getLoan().getTenureMonths() / 12;
        if (age + tenureYears > 65) {
            rejectionReasons.add("AGE_TENURE_LIMIT_EXCEEDED");
        }

        // Risk band
        RiskBand riskBand = determineRisk(request.getApplicant().getCreditScore());

        // Interest calculation
        BigDecimal finalRate = calculateFinalRate(request, riskBand);

        BigDecimal emi = EmiCalculator.calculate(
                request.getLoan().getAmount(),
                finalRate,
                request.getLoan().getTenureMonths()
        );

        BigDecimal income = request.getApplicant().getMonthlyIncome();

        // 3️⃣ EMI > 60% reject
        if (emi.compareTo(income.multiply(BigDecimal.valueOf(0.60))) > 0) {
            rejectionReasons.add("EMI_EXCEEDS_60_PERCENT");
        }

        // 4️⃣ Offer valid only if EMI <= 50%
        if (emi.compareTo(income.multiply(BigDecimal.valueOf(0.50))) > 0) {
            rejectionReasons.add("EMI_EXCEEDS_50_PERCENT");
        }

        UUID id = UUID.randomUUID();

        LoanApplication entity = new LoanApplication();
        entity.setId(id);

        if (!rejectionReasons.isEmpty()) {
            entity.setStatus(ApplicationStatus.REJECTED);
            repository.save(entity);

            return Map.of(
                    "applicationId", id,
                    "status", "REJECTED",
                    "riskBand", null,
                    "rejectionReasons", rejectionReasons
            );
        }

        BigDecimal totalPayable =
                emi.multiply(BigDecimal.valueOf(request.getLoan().getTenureMonths()));

        entity.setStatus(ApplicationStatus.APPROVED);
        entity.setRiskBand(riskBand);
        entity.setInterestRate(finalRate);
        entity.setEmi(emi);
        entity.setTotalPayable(totalPayable);

        repository.save(entity);

        return Map.of(
                "applicationId", id,
                "status", "APPROVED",
                "riskBand", riskBand,
                "offer", Map.of(
                        "interestRate", finalRate,
                        "tenureMonths", request.getLoan().getTenureMonths(),
                        "emi", emi,
                        "totalPayable", totalPayable
                )
        );
    }

    private RiskBand determineRisk(int score) {
        if (score >= 750) return RiskBand.LOW;
        if (score >= 650) return RiskBand.MEDIUM;
        return RiskBand.HIGH;
    }

    private BigDecimal calculateFinalRate(
            LoanApplicationRequest request,
            RiskBand riskBand) {

        BigDecimal rate = BASE_RATE;

        // Risk premium
        switch (riskBand) {
            case MEDIUM -> rate = rate.add(BigDecimal.valueOf(1.5));
            case HIGH -> rate = rate.add(BigDecimal.valueOf(3));
        }

        // Employment premium
        if (request.getApplicant().getEmploymentType()
                == EmploymentType.SELF_EMPLOYED) {
            rate = rate.add(BigDecimal.valueOf(1));
        }

        // Loan size premium
        if (request.getLoan().getAmount()
                .compareTo(BigDecimal.valueOf(1000000)) > 0) {
            rate = rate.add(BigDecimal.valueOf(0.5));
        }

        return rate.setScale(2, RoundingMode.HALF_UP);
    }
}
