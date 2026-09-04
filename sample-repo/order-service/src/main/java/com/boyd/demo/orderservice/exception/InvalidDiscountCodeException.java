package com.boyd.demo.orderservice.exception;

public class InvalidDiscountCodeException extends RuntimeException {

    public InvalidDiscountCodeException(String code) {
        super("Discount code '" + code + "' is not valid.");
    }
}
