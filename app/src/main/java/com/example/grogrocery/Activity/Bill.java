package com.example.grogrocery.Activity;

public class Bill {
    private int billId;
    private int userId;
    private double totalAmount;
    private String billDate;

    public Bill(int billId, int userId, double totalAmount, String billDate) {
        this.billId = billId;
        this.userId = userId;
        this.totalAmount = totalAmount;
        this.billDate = billDate;
    }

    public int getBillId() {
        return billId;
    }

    public int getUserId() {
        return userId;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public String getBillDate() {
        return billDate;
    }
}
