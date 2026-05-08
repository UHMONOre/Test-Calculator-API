package com.test.springboot;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
@Table(name = "calculations")
public class Calculations {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false)
    private Double firstNum;
    private Double secondNum;
    @Column(nullable = false)
    private String operator;
    private Double result;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties("savedCalc")
    private User user;

    public Calculations() {
    }

    public int getId() {
        return id;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Double getResult() {
        return result;
    }

    public void setResult(Double result) {
        this.result = result;
    }
}
