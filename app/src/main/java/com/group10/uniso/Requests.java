package com.group10.uniso;

public class Requests {
    private String user_email;
    private String name;
    private String contact;
    private String department;
    private String room;
    private String requested_item;
    private String delivery_option;

    public Requests(){}


    public Requests(String user_email, String name, String contact, String department, String room, String requested_item, String delivery_option) {

        this.user_email = user_email;
        this.name = name;
        this.contact = contact;
        this.department = department;
        this.room = room;
        this.requested_item = requested_item;
        this.delivery_option = delivery_option;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getRequested_item() {
        return requested_item;
    }
    public void setRequested_item(String requested_item) {
        this.requested_item = requested_item;
    }

    public String getDelivery_option() {
        return delivery_option;
    }

    public void setDelivery_option(String delivery_option) {
        this.delivery_option = delivery_option;
    }
}