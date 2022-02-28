package com.group10.uniso;

public class Requests {
    public String id, user_email,name,contact,department,room,requested_item,delivery_option,status,imageUrl,time_requested,token;

    public Requests(String id,String user_email, String name, String contact, String department, String room, String requested_item, String delivery_option, String status,String imageUrl,String time_requested,String token) {

        this.id=id;
        this.user_email = user_email;
        this.name = name;
        this.contact = contact;
        this.department = department;
        this.room = room;
        this.requested_item = requested_item;
        this.delivery_option = delivery_option;
        this.status = status;
        this.imageUrl =imageUrl;
        this.time_requested = time_requested;
        this.token = token;
    }
public  Requests(){

}


}