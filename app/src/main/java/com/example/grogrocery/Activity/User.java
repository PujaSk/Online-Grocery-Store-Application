// User.java

package com.example.grogrocery.Activity;

public class User {
    private int id;
    private String name;
    private String email;
    private String phone;
    private String city;

    public User(int id, String name, String email, String phone, String city) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.city = city;
    }

    // Getters and Setters
    public int getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getCity() { return city; }

    public void setName(String name) { this.name = name; }
    public void setCity(String city) { this.city = city; }
}
