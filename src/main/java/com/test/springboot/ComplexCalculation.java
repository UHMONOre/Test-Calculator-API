package com.test.springboot;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("COMPLEX")
public class ComplexCalculation extends Calculations{

    private String expressionString;

    protected ComplexCalculation() {}

    public ComplexCalculation(String expressionString) {
        this.expressionString = expressionString;
    }

    public String getExpressionString() {
        return expressionString;
    }

    public void setExpressionString(String expressionString) {
        this.expressionString = expressionString;
    }
}
