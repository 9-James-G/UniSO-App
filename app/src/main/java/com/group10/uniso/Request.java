package com.group10.uniso;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class Request extends AppCompatActivity {
    TextInputEditText edit_item;
    TextView  item,delivery;
    FirebaseAuth fAuth;
    DatabaseReference dbReference;
    CheckBox c1,c2,c3,c4,c5,c6,c7,c8;
    SharedPreferences sharedPreferences;

    private static final String SHARED_PREF_NAME= "preference";
    private static final String KEY_EMAIL= "user_email";
    private static final String KEY_NAME= "name";
    private static final String KEY_CONTACT= "contact";
    private static final String KEY_DEPARTMENT= "department";
    private static final String KEY_ROOM= "room";
    private static final String KEY_ITEM= "item";
    private static final String KEY_DELIVERY= "delivery";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);

        fAuth = FirebaseAuth.getInstance();
        dbReference = FirebaseDatabase.getInstance().getReference("Requests");

        edit_item = findViewById(R.id.edit_item);
        item = findViewById(R.id.item);
        delivery = findViewById(R.id.delivery);
        c1 = findViewById(R.id.c1);
        c2 = findViewById(R.id.c2);
        c3 = findViewById(R.id.c3);
        c4 = findViewById(R.id.c4);
        c5 = findViewById(R.id.c5);
        c6 = findViewById(R.id.c6);
        c7 = findViewById(R.id.c7);
        c8 = findViewById(R.id.c8);

        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME,MODE_PRIVATE);
        String shared_Email = sharedPreferences.getString(KEY_EMAIL, null);
        if (shared_Email!=null){
            Intent intent = new Intent(Request.this,Pending.class);
            startActivity(intent);
        }

        String user_email = getIntent().getStringExtra("EMAIL");
        String name = getIntent().getStringExtra("NAME");
        String contact = getIntent().getStringExtra("CONTACT");
        String department = getIntent().getStringExtra("DEPARTMENT");
        String room = getIntent().getStringExtra("ROOM");



        Button btn = findViewById(R.id.btn_submit);
        RequestItem rai = new RequestItem();
        btn.setOnClickListener(v->{

            String selected = "",edit_item1,deliv="";
            edit_item1 = edit_item.getText().toString().trim();

            if(c1.isChecked()){
                selected = selected + "A4 Bond Paper, ";
            }
            if(c2.isChecked()){
                selected = selected + "Projector, ";
            }
            if(c3.isChecked()){
                selected = selected + "Monitor, ";
            }
            if(c4.isChecked()){
                selected = selected + "System Unit, ";
            }
            if(c5.isChecked()){
                selected = selected + "Keyboard, ";
            }
            if(c6.isChecked()){
                selected = selected + "Mouse, ";
            }
            if(c7.isChecked()){
                deliv = "Delivers by Staff";
            }if(c8.isChecked()){
                deliv = "Take it by self";
            }
            if(c7.isChecked()&&c8.isChecked()){
                deliv = "";
            }else{
                selected = selected + edit_item1 + "";
            }
            item.setText(selected);
            delivery.setText(deliv);
            if(TextUtils.isEmpty(selected) && TextUtils.isEmpty(edit_item1) || TextUtils.isEmpty(deliv)) {
                Toast.makeText(this, "Make sure to select your desired item and only 1 from delivery option!", Toast.LENGTH_LONG).show();
                return;
            }

            Requests emp =new Requests(user_email,name,contact,department,room,item.getText().toString(),delivery.getText().toString());
            rai.add(emp).addOnSuccessListener(suc->
            {

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(KEY_EMAIL,user_email);
                editor.putString(KEY_NAME,name);
                editor.putString(KEY_CONTACT,contact);
                editor.putString(KEY_DEPARTMENT,department);
                editor.putString(KEY_ROOM,room);
                editor.putString(KEY_ITEM,item.getText().toString());
                editor.putString(KEY_DELIVERY,delivery.getText().toString());
                editor.apply();
                Intent intent = new Intent(Request.this,Pending.class);
                startActivity(intent);
                Toast.makeText(Request.this, "Request sent!", Toast.LENGTH_SHORT).show();
            }).addOnFailureListener(er->
            {
                Toast.makeText(Request.this, ""+er.getMessage(), Toast.LENGTH_SHORT).show();

            });
        });
    }

    public void back(View view) {
        startActivity(new Intent(getApplicationContext(), Form.class));
    }
}