package com.realestate.controller;

import com.realestate.model.credit.Credit;
import com.realestate.service.CreditService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/credit")
public class CreditController {

    private final CreditService creditService;

    public CreditController(CreditService creditService) {
        this.creditService = creditService;
    }

    @PostMapping("/calculate")
    public ResponseEntity<Double> calculateLoanEligibility(@RequestBody Credit credit){
        creditService.calculateLoanEligibility(credit);
        return ResponseEntity.ok(credit.getLoanAmount());
    }
}
