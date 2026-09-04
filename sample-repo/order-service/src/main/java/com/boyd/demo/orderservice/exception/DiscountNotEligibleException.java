package com.boyd.demo.orderservice.exception;

public class DiscountNotEligibleException extends RuntimeException {

    public DiscountNotEligibleException(String code) {
        super("Discount code '" + code + "' is not eligible for this order.");
    }
}
