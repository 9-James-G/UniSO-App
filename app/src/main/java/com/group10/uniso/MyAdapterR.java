package com.group10.uniso;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapterR extends RecyclerView.Adapter<MyAdapterR.MyViewHolder> {

    Context context;

    ArrayList<Received> list1;


    public MyAdapterR(Context context, ArrayList<Received> list1) {
        this.context = context;
        this.list1 = list1;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item,parent,false);
        return  new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String id,status,name,delivery_option,room,contact,department,user_email,requested_item,imageUrl,time_requested,time_received;
        Received received = list1.get(position);
        id = received.getId();
        status = received.getStatus();
        name = received.getName();
        delivery_option= received.getDelivery_option();
        room= received.getRoom();
        contact= received.getContact();
        department= received.getDepartment();
        user_email= received.getUser_email();
        requested_item= received.getRequested_item();
        imageUrl= received.getImageUrl();
        time_requested= received.getTime_requested();
        time_received= received.getTime_received();
        if(status.equals("APPROVED")){
            holder.tvStat.setText(Html.fromHtml("<font color='#01FF0B'><b>APPROVED<b></font>"));
        }else if(status.equals("RECEIVED")){
            holder.tvStat.setText(Html.fromHtml("<font color='#065009'><b>RECEIVED<b></font>"));
        }else {
            holder.tvStat.setText(status);
        }
//        holder.tvStat.setText("STATUS: "+received.getStatus());
        holder.tvTimeReq.setText("REQUESTED AT: "+received.getTime_requested()+"\nRECEIVED AT: "+received.getTime_received());
        holder.tvName.setText("NAME: "+received.getName());
        holder.tvDeliver.setText("DELIVERY OPTION: "+received.getDelivery_option());
        holder.tvRoom.setText("ROOM: "+received.getRoom());
        holder.tvContact.setText("CONTACT: "+received.getContact());

        holder.v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.v.getContext(), ViewRequest.class);
                intent.putExtra("VID",id);
                intent.putExtra("VTIME",time_requested);
                intent.putExtra("VTIME2",time_received);
                intent.putExtra("VSTATUS",status);
                intent.putExtra("VNAME",name);
                intent.putExtra("VDELIVER",delivery_option);
                intent.putExtra("VROOM",room);
                intent.putExtra("VCONTACT",contact);
                intent.putExtra("VDEPARTMENT",department);
                intent.putExtra("VEMAIL",user_email);
                intent.putExtra("VREQ",requested_item);
                intent.putExtra("VIMG",imageUrl);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                holder.v.getContext().startActivity(intent);
                ((Activity)context).finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list1.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tvStat,tvTimeReq,tvName,tvDeliver,tvRoom, tvContact;
        View v;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvStat= itemView.findViewById(R.id.tvStat);
            tvTimeReq= itemView.findViewById(R.id.tvTimeReq);
            tvName = itemView.findViewById(R.id.tvName);
            tvDeliver = itemView.findViewById(R.id.tvDeliver);
            tvRoom = itemView.findViewById(R.id.tvRoom);
            tvContact = itemView.findViewById(R.id.tvContact);
            v=itemView;

        }
    }

}