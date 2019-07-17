package com.model;


import spark.utils.StringUtils;

import javax.persistence.*;


@Entity
@Table(name="ACCOUNT")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String holderName;
    private double balance;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHolderName() {
        return holderName;
    }

    public void setHolderName(String holderName) {
        this.holderName = holderName;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        if (balance < 0) throw new IllegalArgumentException("");
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", holderName='" + holderName + '\'' +
                ", balance=" + balance +
                '}';
    }

    public void validateForm() {
        if (StringUtils.isEmpty(holderName)) throw new IllegalArgumentException("Holder name can't be null or empty");
        if (balance < 0) throw new IllegalArgumentException("Balance can not be negative");
    }
}
