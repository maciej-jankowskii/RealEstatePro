package com.realestate.service;

import com.realestate.model.credit.Credit;
import org.springframework.stereotype.Service;

@Service
public class CreditService {

    public void calculateLoanEligibility(Credit credit) {
        double monthlyInterestRate = credit.getInterestRate() / 12;
        int numberOfPayments = credit.getLoanTerm() * 12;
        double loanAmount = (credit.getMonthlyIncome() - credit.getMonthlyExpenses()) * (1 - Math.pow(1 + monthlyInterestRate, -numberOfPayments)) / monthlyInterestRate;

        credit.setLoanAmount(loanAmount);
    }
}
