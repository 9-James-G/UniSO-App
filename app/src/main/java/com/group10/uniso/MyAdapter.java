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

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
//AdminSide.class - adapter
    Context context;

    ArrayList<GetReq> list;


    public MyAdapter(Context context, ArrayList<GetReq> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item,parent,false);
        return  new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String id,status,name,delivery_option,room,contact,department,user_email,requested_item,imageUrl,time_requested,token;
        GetReq requests = list.get(position);
        id = requests.getId();
        status = requests.getStatus();
        name = requests.getName();
        delivery_option= requests.getDelivery_option();
        room= requests.getRoom();
        contact= requests.getContact();
        department= requests.getDepartment();
        user_email= requests.getUser_email();
        requested_item= requests.getRequested_item();
        imageUrl= requests.getImageUrl();
        time_requested = requests.getTime_requested();
        token = requests.getToken();
        if(status.equals("APPROVED")){
            holder.tvStat.setText(Html.fromHtml("<font color='#01FF0B'><b>APPROVED<b></font>"));
        }else if(status.equals("RECEIVED")){
            holder.tvStat.setText(Html.fromHtml("<font color='#065009'><b>RECEIVED<b></font>"));
        }else {
            holder.tvStat.setText(status);
        }
//        holder.tvStat.setText("STATUS: "+requests.getStatus());
        holder.tvTimeReq.setText("REQUESTED AT: "+requests.getTime_requested());
        holder.tvName.setText("NAME: "+requests.getName());
        holder.tvDeliver.setText("DELIVERY OPTION: "+requests.getDelivery_option());
        holder.tvRoom.setText("ROOM: "+requests.getRoom());
        holder.tvContact.setText("CONTACT: "+requests.getContact());

        holder.v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.v.getContext(), ViewRequest.class);
                intent.putExtra("VID",id);
                intent.putExtra("VTIME",time_requested);
                intent.putExtra("VSTATUS",status);
                intent.putExtra("VNAME",name);
                intent.putExtra("VDELIVER",delivery_option);
                intent.putExtra("VROOM",room);
                intent.putExtra("VCONTACT",contact);
                intent.putExtra("VDEPARTMENT",department);
                intent.putExtra("VEMAIL",user_email);
                intent.putExtra("VREQ",requested_item);
                intent.putExtra("VIMG",imageUrl);
                intent.putExtra("VTOK",token);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                holder.v.getContext().startActivity(intent);
                ((Activity)context).finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
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