package com.wipro.bank.util;

import java.util.Random;

public class AccountNumberGenerator {

    private static final Random random = new Random();

    public static String generateAccountNumber() {
        // Generates 10-digit account number
        long number = 1000000000L + (long)(random.nextDouble() * 9000000000L);
        return String.valueOf(number);
    }
}