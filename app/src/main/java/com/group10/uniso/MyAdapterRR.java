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

public class MyAdapterRR extends RecyclerView.Adapter<MyAdapterRR.MyViewHolder> {

    Context context;

    ArrayList<Returned> list2;


    public MyAdapterRR(Context context, ArrayList<Returned> list2) {
        this.context = context;
        this.list2 = list2;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item,parent,false);
        return  new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String id,status,name,delivery_option,room,contact,department,user_email,requested_item,imageUrl,time_requested,time_received,time_returned;
        Returned returned = list2.get(position);
        id = returned.getId();
        status = returned.getStatus();
        name = returned.getName();
        delivery_option= returned.getDelivery_option();
        room= returned.getRoom();
        contact= returned.getContact();
        department= returned.getDepartment();
        user_email= returned.getUser_email();
        requested_item= returned.getRequested_item();
        imageUrl= returned.getImageUrl();
        time_requested= returned.getTime_requested();
        time_received= returned.getTime_received();
        time_returned= returned.getTime_returned();
        if(status.equals("APPROVED")){
            holder.tvStat.setText(Html.fromHtml("<font color='#01FF0B'><b>APPROVED<b></font>"));
        }else if(status.equals("RECEIVED")){
            holder.tvStat.setText(Html.fromHtml("<font color='#065009'><b>RECEIVED<b></font>"));
        }else if(status.equals("RETURNED")){
            holder.tvStat.setText(Html.fromHtml("<font color='#000000'><b>RETURNED<b></font>"));
        }else {
            holder.tvStat.setText(status);
        }
//        holder.tvStat.setText("STATUS: "+received.getStatus());
        holder.tvTimeReq.setText("RETURNED AT: "+returned.getTime_returned());
        holder.tvName.setText("NAME: "+returned.getName());
        holder.tvDeliver.setText("DELIVERY OPTION: "+returned.getDelivery_option());
        holder.tvRoom.setText("ROOM: "+returned.getRoom());
        holder.tvContact.setText("CONTACT: "+returned.getContact());

        holder.v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.v.getContext(), ViewRequest.class);
                intent.putExtra("VID",id);
                intent.putExtra("VTIME",time_requested);
                intent.putExtra("VTIME2",time_received);
                intent.putExtra("VTIME3",time_returned);
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
        return list2.size();
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
