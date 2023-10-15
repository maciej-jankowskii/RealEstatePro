package com.realestate.model.credit;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Credit {

    private double monthlyIncome;
    private double monthlyExpenses;
    private double interestRate;
    private int loanTerm;
    private double downPayment;
    private double loanAmount;

}
