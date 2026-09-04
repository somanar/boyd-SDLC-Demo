package com.boyd.demo.orderservice.exception;

public class ExpiredDiscountCodeException extends RuntimeException {

    public ExpiredDiscountCodeException(String code) {
        super("Discount code '" + code + "' has expired.");
    }
}
