package com.example.loan_service.repository;

import com.example.loan_service.domain.LoanApplication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LoanApplicationRepository
        extends JpaRepository<LoanApplication, UUID> {
}
