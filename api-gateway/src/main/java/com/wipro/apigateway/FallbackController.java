package com.wipro.apigateway;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fallback")
public class FallbackController {

    @GetMapping("/account")
    public String accountFallback() {
        return "Account Service is currently down. Please try again later!";
    }

    @GetMapping("/transaction")
    public String transactionFallback() {
        return "Transaction Service is currently down. Please try again later!";
    }
}
