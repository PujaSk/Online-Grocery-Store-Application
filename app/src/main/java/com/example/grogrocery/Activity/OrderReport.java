package com.example.grogrocery.Activity;

public class OrderReport {
    private int orderId;
    private String userName;      // Name from Reg_Master
    private String itemName;      // Name from Item_Master
    private int quantity;
    private double totalAmount;   // From Bill_Master
    private String orderDate;

    public OrderReport(int orderId, String userName, String itemName, int quantity, double totalAmount, String orderDate) {
        this.orderId = orderId;
        this.userName = userName;
        this.itemName = itemName;
        this.quantity = quantity;
        this.totalAmount = totalAmount;
        this.orderDate = orderDate;
    }

    // Getters
    public int getOrderId() {
        return orderId;
    }

    public String getUserName() {
        return userName;
    }

    public String getItemName() {
        return itemName;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public String getOrderDate() {
        return orderDate;
    }
}
