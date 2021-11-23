package com.group10.uniso;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.content.DialogInterface;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Pending extends AppCompatActivity {
    TextView tv;
    private Button logOut,receive;
    SharedPreferences sharedPreferences;
    FirebaseAuth fAuth;
    DatabaseReference dbReference;

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
        setContentView(R.layout.activity_pending);

        fAuth = FirebaseAuth.getInstance();
        dbReference = FirebaseDatabase.getInstance().getReference("Requests");

        tv = findViewById(R.id.tv);
        logOut = findViewById(R.id.logOut);
        receive = findViewById(R.id.receive);

        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME,MODE_PRIVATE);

        String user_email = sharedPreferences.getString(KEY_EMAIL, null);
        String name = sharedPreferences.getString(KEY_NAME, null);
        String contact = sharedPreferences.getString(KEY_CONTACT, null);
        String department = sharedPreferences.getString(KEY_DEPARTMENT, null);
        String room = sharedPreferences.getString(KEY_ROOM, null);
        String item = sharedPreferences.getString(KEY_ITEM, null);
        String delivery = sharedPreferences.getString(KEY_DELIVERY, null);

        if(name != null || user_email != null){
            tv.setText("User email: "+user_email + "\nFull Name: " +name+"\nContact: "+contact+"\nDepartment: "+department+"\nRoom: "+room+"\nItem: "+item+"\nDelivery: "+delivery);
        }
        CancelledItem cai = new CancelledItem();
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(Pending.this);
                builder.setTitle("Warning!");
                builder.setMessage("Logging out now will cancel your request!");
                builder.setIcon(R.drawable.ic_baseline_warning_24);
                builder.setPositiveButton("PROCEED", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dbReference.child(name).removeValue();
                        Cancelled emp =new Cancelled(user_email,name,contact,department,room,item,delivery);
                        cai.add(emp).addOnSuccessListener(suc->
                        {
                FirebaseAuth.getInstance().signOut();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.commit();
                finish();
                Toast.makeText(Pending.this, "Logged out", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Pending.this,MainActivity.class);
                startActivity(intent);
                        }).addOnFailureListener(er->
                        {
                            Toast.makeText(Pending.this, ""+er.getMessage(), Toast.LENGTH_SHORT).show();

                        });
                    }
                });
                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(Pending.this, "Logout cancelled!", Toast.LENGTH_SHORT).show();
            }//onClick
        });//logout
                builder.show();
            }
        });

        ReceivedItem rei = new ReceivedItem();
        receive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(Pending.this);
                builder.setTitle("Confirmation");
                builder.setMessage("Did you already received your item?");
                builder.setIcon(R.drawable.ic_baseline_help_center_24);
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
            dbReference.child(name).removeValue();
            Received emp =new Received(user_email,name,contact,department,room,item,delivery);
                rei.add(emp).addOnSuccessListener(suc->
                {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.clear();
                    editor.commit();
                    finish();
                    Toast.makeText(Pending.this, "Thank you for your patience!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Pending.this,Form.class);
                    startActivity(intent);
                }).addOnFailureListener(er-> //suc
                {
                    Toast.makeText(Pending.this, ""+er.getMessage(), Toast.LENGTH_SHORT).show();

                });
                    }
                });
                        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        builder.show();
                }
        });
    }

}