package com.example.grogrocery.Activity;

public class Item {
    private int id;
    private String name;
    private int price;
    private int qty;
    private byte[] avatar;
    private String categoryId;
    private String description;

    public Item(int id, String name, int price, int qty, byte[] avatar, String categoryId, String description) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.qty = qty;
        this.avatar = avatar;
        this.categoryId = categoryId;
        this.description = description;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public int getPrice() { return price; }
    public int getQty() { return qty; }
    public byte[] getAvatar() { return avatar; }
    public String getCategoryId() { return categoryId; }
    public String getDescription() { return description; }


    // Setter for qty
    public void setQty(int qty) {
        this.qty = qty;
    }

}
