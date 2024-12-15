// File: Category.java
package com.example.grogrocery.Activity;

public class Category {
    private String id;
    private String name;
    private byte[] avatar;

    public Category(String id, String name, byte[] avatar) {
        this.id = id;
        this.name = name;
        this.avatar = avatar;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public byte[] getAvatar() {
        return avatar;
    }

    // Optional: Setters if you need to modify categories
    public void setName(String name) {
        this.name = name;
    }

    public void setAvatar(byte[] avatar) {
        this.avatar = avatar;
    }
}
