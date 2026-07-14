package com.test.springboot;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("SIMPLE")
public class SimpleCalculation extends Calculations {
    @Column(nullable = false)
    private Double firstNum;
    private Double secondNum;
    @Column(nullable = false)
    private String operator;

    protected SimpleCalculation() {}

    public SimpleCalculation(Double firstNum, Double secondNum, String operator) {
        this.firstNum = firstNum;
        this.secondNum = secondNum;
        this.operator = operator;
    }

    public Double getFirstNum() {
        return firstNum;
    }

    public void setFirstNum(Double firstNum) {
        this.firstNum = firstNum;
    }

    public Double getSecondNum() {
        return secondNum;
    }

    public void setSecondNum(Double secondNum) {
        this.secondNum = secondNum;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }
}
