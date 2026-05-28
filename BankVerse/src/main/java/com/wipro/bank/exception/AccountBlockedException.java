package com.wipro.bank.exception;
public class AccountBlockedException extends RuntimeException {

    public AccountBlockedException() {
        super();
    }

    public AccountBlockedException(String message) {
        super(message);
    }
}