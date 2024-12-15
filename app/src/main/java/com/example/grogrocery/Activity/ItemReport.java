package com.example.grogrocery.Activity;

public class ItemReport {
    private int iid;            // Item ID
    private String name;        // Item Name
    private double price;       // Item Price
    private int qty;           // Item Quantity

    public ItemReport(int iid, String name, double price, int qty) {
        this.iid = iid;
        this.name = name;
        this.price = price;
        this.qty = qty;
    }

    public int getIid() {
        return iid;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getQty() {
        return qty;
    }
}
