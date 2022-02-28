package com.group10.uniso;

public class User {
    public String id, name, contact,department, email,registered_as,imageUrl;

    public User(String id,String name, String contact,String department, String email,String registered_as,String imageUrl) {
        this.id = id;
        this.name = name;
        this.contact = contact;
        this.department = department;
        this.email = email;
        this.registered_as = registered_as;
        this.imageUrl = imageUrl;
    }

    public User(){

    }
}