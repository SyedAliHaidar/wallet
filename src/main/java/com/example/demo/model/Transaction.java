package com.example.demo.model;

import javax.persistence.*;

@Entity
//defining class name as Table name
@Table(name="transactions")
public class Transaction {

    @Id
    @Column(name = "transaction_no")
    Integer transactionNo;
    @Column(name = "transaction_id")
    String transactionId;
    @Column(name = "user_id")
    Integer userId;
    @Column(name = "user_name")
    String userName;
    @Column(name = "date_and_time")
    String dateAndTime;
    @Column(name = "second_party")
    String secondParty;
    @Column(name = "transaction_amount")
    Integer transactionAmount;
    @Column(name = "final_amount")
    Integer finalAmount;
    @Column(name = "refundable")
    boolean refundable = false;
    @Column(name = "transaction_type")
    String transactionType;

    public Integer getTransactionNo() {
        return transactionNo;
    }

    public void setTransactionNo(Integer transactionNo) {
        this.transactionNo = transactionNo;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getDateAndTime() {
        return dateAndTime;
    }

    public void setDateAndTime(String dateAndTime) {
        this.dateAndTime = dateAndTime;
    }

    public String getSecondParty() {
        return secondParty;
    }

    public void setSecondParty(String secondParty) {
        this.secondParty = secondParty;
    }

    public Integer getFinalAmount() {
        return finalAmount;
    }

    public void setFinalAmount(Integer finalAmount) {
        this.finalAmount = finalAmount;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


    public boolean isRefundable() {
        return refundable;
    }

    public void setRefundable(boolean refundable) {
        this.refundable = refundable;
    }

    public Integer getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(Integer transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }
    @Override
    public String toString() {
        return "Transaction{" +
                "transactionNo=" + transactionNo +
                ", transactionId='" + transactionId + '\'' +
                ", userId=" + userId +
                ", userName='" + userName + '\'' +
                ", dateAndTime='" + dateAndTime + '\'' +
                ", secondParty='" + secondParty + '\'' +
                ", transactionAmount=" + transactionAmount +
                ", finalAmount=" + finalAmount +
                ", refundable=" + refundable +
                ", transactionType='" + transactionType + '\'' +
                '}';
    }
}
