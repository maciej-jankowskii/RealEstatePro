package com.realestate.controller;

import com.realestate.model.credit.Credit;
import com.realestate.service.CreditService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/credit")
@CrossOrigin("*")
public class CreditController {

    private final CreditService creditService;

    public CreditController(CreditService creditService) {
        this.creditService = creditService;
    }

    @PostMapping("/calculate")
    public ResponseEntity<Double> calculateLoanEligibility(@RequestBody Credit credit) {
        creditService.calculateLoanEligibility(credit);
        return ResponseEntity.ok(credit.getLoanAmount());
    }
}
