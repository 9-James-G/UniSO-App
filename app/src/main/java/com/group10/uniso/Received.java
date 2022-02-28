package com.group10.uniso;

public class Received {
    private String id;
    private String user_email;
    private String name;
    private String contact;
    private String department;
    private String room;
    private String requested_item;
    private String delivery_option;
    private String status;
    private String imageUrl;
    private String time_requested;
    private String time_received;

    public Received(){}


    public Received(String id,String user_email, String name, String contact, String department, String room, String requested_item, String delivery_option,String status,String imageUrl,String time_requested,String time_received) {

        this.id= id;
        this.user_email= user_email;
        this.name = name;
        this.contact = contact;
        this.department = department;
        this.room = room;
        this.requested_item = requested_item;
        this.delivery_option = delivery_option;
        this.status = status;
        this.imageUrl = imageUrl;
        this.time_requested = time_requested;
        this.time_received = time_received;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTime_requested() {
        return time_requested;
    }

    public void setTime_requested(String time_requested) {
        this.time_requested = time_requested;
    }

    public String getTime_received() {
        return time_received;
    }

    public void setTime_received(String time_received) {
        this.time_received = time_received;
    }
}
