package com.group10.uniso;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class MyAPIAdapter extends RecyclerView.Adapter<MyAPIAdapter.MyViewHolder> {

    private Context myJSONcontext;
    private List<DataFromAPI> myData;

    public MyAPIAdapter(Context myJSONcontext, List<DataFromAPI> myData) {
        this.myJSONcontext = myJSONcontext;
        this.myData = myData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        LayoutInflater inflater = LayoutInflater.from(myJSONcontext);
        v = inflater.inflate(R.layout.json_items, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tvDisNumber.setText(myData.get(position).getId());
        holder.tvDisName.setText(myData.get(position).getName());
        Glide.with(myJSONcontext).load(myData.get(position).getImage()).into(holder.disImage);
    }

    @Override
    public int getItemCount() {
        return myData.size();
    }

    public static class MyViewHolder extends  RecyclerView.ViewHolder{
        TextView tvDisNumber, tvDisName;
        ImageView disImage;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDisNumber = itemView.findViewById(R.id.tvDisNumber);
            tvDisName = itemView.findViewById(R.id.tvDisName);
            disImage = itemView.findViewById(R.id.disImage);
        }
    }

}

