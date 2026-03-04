package com.example.loan_service.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class EmiCalculator {

    public static BigDecimal calculate(
            BigDecimal principal,
            BigDecimal annualRate,
            int tenureMonths) {

        BigDecimal monthlyRate = annualRate
                .divide(BigDecimal.valueOf(12), 10, RoundingMode.HALF_UP)
                .divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP);

        BigDecimal onePlusRPowerN =
                (BigDecimal.ONE.add(monthlyRate))
                        .pow(tenureMonths);

        BigDecimal numerator =
                principal.multiply(monthlyRate)
                        .multiply(onePlusRPowerN);

        BigDecimal denominator =
                onePlusRPowerN.subtract(BigDecimal.ONE);

        return numerator.divide(denominator, 2, RoundingMode.HALF_UP);
    }
}