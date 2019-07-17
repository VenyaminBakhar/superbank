package com.model;

import spark.utils.StringUtils;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "TRANSACTION_HISTORY")
public class TransactionModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int transactionId;
    private int idFrom;
    private int idTo;
    private double amount;
    private Date transactionDate;


    public int getTransactionId() { return transactionId; }

    public void setTransactionId(int transactionId) { this.transactionId = transactionId; }

    public void setIdFrom(int idFrom) {
        this.idFrom = idFrom;
    }

    public void setIdTo(int idTo) {
        this.idTo = idTo;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getIdFrom() {
        return idFrom;
    }

    public int getIdTo() {
        return idTo;
    }

    public double getAmount() {
        return amount;
    }

    public Date getTransactionDate() { return transactionDate; }

    public void setTransactionDate(Date transactionDate) { this.transactionDate = transactionDate; }

    public boolean isWellFormed() {
        if (StringUtils.isEmpty(this.amount) || StringUtils.isEmpty(idFrom) || StringUtils.isEmpty(idTo)) return false;
        if (this.idFrom == this.idTo) return false;
        return amount > 0;
    }

    @Override
    public String toString() {
        return "Transaction {" +
                "transactionId=" + transactionId +
                ", idFrom=" + idFrom +
                ", idTo=" + idTo +
                ", amount=" + amount +
                ", transactionDate=" + transactionDate +
                '}';
    }
}
