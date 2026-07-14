package com.test.springboot;

public class CalculationRequests {
    public record Simple(Double firstNum, Double secondNum, String operator) {}

    public record Complex(String expressionString) {}
}
