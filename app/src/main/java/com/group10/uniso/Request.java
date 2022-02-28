package com.group10.uniso;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class Request extends AppCompatActivity {
    TextInputEditText edit_item;
//    TextView  item,delivery;
    FirebaseAuth fAuth;
    FirebaseUser user;
    DatabaseReference dbRequests,dbReference;
    CheckBox c1,c2,c3,c4,c5,c6,c7,c8;
    DrawerLayout drawerLayout;
    SharedPreferences sharedPreferences;
    String profname,id,user_email,contact,department,room,imageUrl,name,item,delivery="",time,time_requested;
    Button pls1,pls2,pls3,pls4,pls5,pls6,min1,min2,min3,min4,min5,min6;
    EditText q1,q2,q3,q4,q5,q6;
    int num1=0,num2=0,num3=0,num4=0,num5=0,num6=0;
    Calendar calendar;
    SimpleDateFormat simpleDateFormat;
    public static String PREFS_NAME="MyPrefsFile";
//    private static final String SHARED_PREF_NAME= "preference";
//    private static final String KEY_PREF= "my_pref";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);

        fAuth = FirebaseAuth.getInstance();
        dbRequests = FirebaseDatabase.getInstance().getReference("Requests");
        user = fAuth.getCurrentUser();
        dbReference= FirebaseDatabase.getInstance().getReference("Users");
        edit_item = findViewById(R.id.edit_item);
//        item = findViewById(R.id.item);
//        delivery = findViewById(R.id.delivery);
        c1 = findViewById(R.id.c1);
        c2 = findViewById(R.id.c2);
        c3 = findViewById(R.id.c3);
        c4 = findViewById(R.id.c4);
        c5 = findViewById(R.id.c5);
        c6 = findViewById(R.id.c6);
        c7 = findViewById(R.id.c7);
        c8 = findViewById(R.id.c8);
        pls1 = findViewById(R.id.pls1);
        pls2 = findViewById(R.id.pls2);
        pls3 = findViewById(R.id.pls3);
        pls4 = findViewById(R.id.pls4);
        pls5 = findViewById(R.id.pls5);
        pls6 = findViewById(R.id.pls6);
        min1 = findViewById(R.id.min1);
        min2 = findViewById(R.id.min2);
        min3 = findViewById(R.id.min3);
        min4 = findViewById(R.id.min4);
        min5 = findViewById(R.id.min5);
        min6 = findViewById(R.id.min6);
        q1 = findViewById(R.id.q1);
        q2 = findViewById(R.id.q2);
        q3 = findViewById(R.id.q3);
        q4 = findViewById(R.id.q4);
        q5 = findViewById(R.id.q5);
        q6 = findViewById(R.id.q6);

        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("hh:mm a");
        time = simpleDateFormat.format(calendar.getTime());

        drawerLayout = findViewById(R.id.drawer_layout);

//        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME,MODE_PRIVATE);
//        String my_pref= sharedPreferences.getString(KEY_PREF, null);

        id = getIntent().getStringExtra("ID");
        user_email = getIntent().getStringExtra("EMAIL");
        name = getIntent().getStringExtra("NAME");
        contact = getIntent().getStringExtra("CONTACT");
       department = getIntent().getStringExtra("DEPARTMENT");
        room = getIntent().getStringExtra("ROOM");
        imageUrl = getIntent().getStringExtra("IMG");
        profname = name;

//        Button btn = findViewById(R.id.btn_submit);
//        RequestItem rai = new RequestItem();
//        btn.setOnClickListener(v->{
//
//            String selected = "",edit_item1,deliv="",status="PENDING",quantity;
//            edit_item1 = edit_item.getText().toString().trim();
//
//            if(c1.isChecked()){
//                selected = selected + "A4 Bond Paper, ";}
//
//            if(c2.isChecked()){
//                selected = selected + "Projector, ";
//            }
//
//            if(c3.isChecked()){
//                selected = selected + "Monitor, ";
//                }
//
//            if(c4.isChecked()){
//                selected = selected + "System Unit, ";
//            }
//            if(c5.isChecked()){
//                selected = selected + "Keyboard, ";
//
//            }
//            if(c6.isChecked()){
//                selected = selected + "Mouse, ";
//            }
//
//            if(c7.isChecked()){
//                deliv = "Delivers by Staff";
//            }if(c8.isChecked()){
//                deliv = "Take it by self";
//            }
//            if(c7.isChecked()&&c8.isChecked()){
//                deliv = "";
//            }else{
//                selected = selected + edit_item1 + "";
//            }
//            item.setText(selected);
//            delivery.setText(deliv);
//            if(TextUtils.isEmpty(selected) && TextUtils.isEmpty(edit_item1) || TextUtils.isEmpty(deliv)) {
//                Toast.makeText(this, "Make sure to select your desired item,its quantity and only 1 from delivery option!", Toast.LENGTH_LONG).show();
//                return;
//            }
//
//            Requests emp =new Requests(id,user_email,name,contact,department,room,item.getText().toString(),delivery.getText().toString(),status,imageUrl);
//            dbReference.child(id).setValue(emp).addOnSuccessListener(suc->
//            {
//                Toast.makeText(Request.this, "Request sent!", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(Request.this,RequestsList.class);
//                startActivity(intent);
//                finish();
//            }).addOnFailureListener(er->
//            {
//                Toast.makeText(Request.this, ""+er.getMessage(), Toast.LENGTH_SHORT).show();
//
//            });
//        });
    }
    public void ClickMenu(View view){
        Form.openDrawer(drawerLayout);
    }
    public void ClickLogo(View view){
        Form.closeDrawer(drawerLayout);}

//    public void ClickNewReq(View view){
//        Form.redirectActivity(this,Form.class);}

    public void ClickItems(View view){
        Form.redirectActivity(this,Form.class);
        finish();}

    public void ClickRequestsList(View view){
        Form.redirectActivity(this,RequestsList.class);finish();}

    public void ClickProfile(View view){
        Form.redirectActivity(this,Profile.class);finish();}
    public void ClickPassword(View view){
        final AlertDialog.Builder passwordDialog = new AlertDialog.Builder(view.getContext());
        View mView = getLayoutInflater().inflate(R.layout.updatepassword,null);
        final EditText updatePassword = (EditText) mView.findViewById(R.id.updatePassword);

        passwordDialog.setPositiveButton("UPDATE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


            }
        });passwordDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        passwordDialog.setView(mView);
        AlertDialog dialog =passwordDialog.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newPass = updatePassword.getText().toString();
                if (TextUtils.isEmpty(newPass)) {
                    updatePassword.setError("Please enter your new password.");
                    return;
                }
                user.updatePassword(newPass).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(Request.this, "Changed successfully!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Request.this, "An Error Occurred!" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
    public void ClickLogout(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(Request.this);
        builder.setTitle("LOGOUT");
        builder.setMessage("Are you sure you want to logout?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseAuth.getInstance().signOut();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.commit();
                Toast.makeText(Request.this, "Logged out", Toast.LENGTH_SHORT).show();
//                finish();
//                onDestroy();
finishAffinity();
//                System.exit(0);
            }});
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }});
        builder.show();
    }

    @Override
    protected void onPause(){
        super.onPause();
        Form.closeDrawer(drawerLayout);
    }

    public void back(View view) {
        finish();
    }


    public void check1(View view) {
        if(c1.isChecked()==true){
            num1=1;
            pls1.setEnabled(true);
            min1.setEnabled(true);
        }else{
            pls1.setEnabled(false);
            min1.setEnabled(false);
            num1=0;
        }
        q1.setText(String.valueOf(num1));
    }
    public void plus1(View view) {
        num1=num1+1;
        q1.setText(String.valueOf(num1));
    }
    public void minus1(View view) {
        if(num1>1){
            num1=num1-1;
            q1.setText(String.valueOf(num1));
        }
    }

    public void check2(View view) {
        if(c2.isChecked()==true){
            num2=1;
            pls2.setEnabled(true);
            min2.setEnabled(true);
        }else{
            pls2.setEnabled(false);
            min2.setEnabled(false);
            num2=0;
        }
        q2.setText(String.valueOf(num2));

    }
    public void plus2(View view) {
        num2=num2+1;
        q2.setText(String.valueOf(num2));
    }
    public void minus2(View view) {
        if(num2>1){
            num2=num2-1;
            q2.setText(String.valueOf(num2));
        }
    }
    public void check3(View view) {
        if(c3.isChecked()==true){
            num3=1;
            pls3.setEnabled(true);
            min3.setEnabled(true);
        }else{
            pls3.setEnabled(false);
            min3.setEnabled(false);
            num3=0;
        }
        q3.setText(String.valueOf(num3));
    }
    public void plus3(View view) {
        num3=num3+1;
        q3.setText(String.valueOf(num3));
    }
    public void minus3(View view) {
        if(num3>1){
            num3=num3-1;
            q3.setText(String.valueOf(num3));
        }
    }
    public void check4(View view) {
        if(c4.isChecked()==true){
            num4=1;
            pls4.setEnabled(true);
            min4.setEnabled(true);
        }else{
            pls4.setEnabled(false);
            min4.setEnabled(false);
            num4=0;
        }
        q4.setText(String.valueOf(num4));

    }
    public void plus4(View view) {
        num4=num4+1;
        q4.setText(String.valueOf(num4));
    }
    public void minus4(View view) {
        if(num4>1){
            num4=num4-1;
            q4.setText(String.valueOf(num4));
        }
    }
    public void check5(View view) {
        if(c5.isChecked()==true){
            num5=1;
            pls5.setEnabled(true);
            min5.setEnabled(true);
        }else{
            pls5.setEnabled(false);
            min5.setEnabled(false);
            num5=0;
        }
        q5.setText(String.valueOf(num5));

    }
    public void plus5(View view) {
        num5=num5+1;
        q5.setText(String.valueOf(num5));
    }
    public void minus5(View view) {
        if(num5>1){
            num5=num5-1;
            q5.setText(String.valueOf(num5));
        }
    }
    public void check6(View view) {
        if(c6.isChecked()==true){
            num6=1;
            pls6.setEnabled(true);
            min6.setEnabled(true);
        }else{
            pls6.setEnabled(false);
            min6.setEnabled(false);
            num6=0;
        }
        q6.setText(String.valueOf(num6));
    }
    public void plus6(View view) {
        num6=num6+1;
        q6.setText(String.valueOf(num6));
    }
    public void minus6(View view) {
        if(num6>1){
            num6=num6-1;
            q6.setText(String.valueOf(num6));
        }
    }

    public void Check7(View view) {
        if(c7.isChecked()){
            delivery = "Delivers by Staff";
            c8.setEnabled(false);
        }else{
            delivery="";
            c8.setEnabled(true);
        }
    }
    public void Check8(View view) {
        if(c8.isChecked()){
            delivery = "Take it by self";
            c7.setEnabled(false);
        }else{
            delivery="";
            c7.setEnabled(true);
        }
    }
    public void submit(View view) {

        String selected = "",edit_item1,status="PENDING";
        edit_item1 = edit_item.getText().toString().trim();

        if(num1>0){
            selected = selected + num1+" A4 Bond Paper, ";}

        if(num2>0){
            selected = selected +num2+ " Projector, ";
        }

        if(num3>0){
            selected = selected +num3+ " Monitor, ";
        }

        if(num4>0){
            selected = selected +num4+ " System Unit, ";
        }
        if(num5>0){
            selected = selected +num5+ " Keyboard, ";

        }
        if(num6>0){
            selected = selected +num6+ " Mouse, ";
        }
        if(edit_item1!=null){
            selected = selected + edit_item1 + "";
        }
        item=selected;
//        delivery.setText(deliv);
        if(TextUtils.isEmpty(selected) && TextUtils.isEmpty(edit_item1) || TextUtils.isEmpty(delivery)) {
            Toast.makeText(this, "Make sure to select at least 1 from the options.", Toast.LENGTH_LONG).show();
            return;
        }
        time_requested = time;
        //new
        String token=SharedPrefManager.getInstance(Request.this).getToken();
        Requests emp =new Requests(id,user_email,name,contact,department,room,item,delivery,status,imageUrl,time_requested,token);
        dbRequests.child(id).setValue(emp).addOnSuccessListener(suc->
        {
            FcmNotificationsSender notificationsSender = new FcmNotificationsSender("/topics/all",
                    "New Request!",
                    "A new request was made by a user.",getApplicationContext(),Request.this);
            notificationsSender.SendNotifications();
            Toast.makeText(Request.this, "Request sent!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Request.this,RequestsList.class);
            startActivity(intent);
            finish();
        }).addOnFailureListener(er->
        {
            Toast.makeText(Request.this, ""+er.getMessage(), Toast.LENGTH_SHORT).show();

        });
//        dbReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if(snapshot.exists()){
//                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
//        String value = String.valueOf(dataSnapshot.child("user_email").getValue());
//        Log.i("",value);
//        if(value.equals("Admin")){
//        User user = snapshot.getValue(User.class);
//        if(user!=null){
//            FcmNotificationsSender notificationsSender = new FcmNotificationsSender(user.token,
//            "UniSO",
//            "There is a new request from a user.",getApplicationContext(),Request.this);
//            notificationsSender.SendNotifications();
//        }
//        }else{
//            String nothing;
//        }
//                    }
////                    int x =0;
////                    while(x<snapshot.getChildrenCount()) {
////                        x=x+1;
////                        User user = snapshot.getValue(User.class);
////                        String admin_token = user.token;
////                        FcmNotificationsSender notificationsSender = new FcmNotificationsSender(admin_token,
////                                "UniSO",
////                                "There is a new request from a user.",getApplicationContext(),Request.this);
////                        notificationsSender.SendNotifications();
////
////                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
    }

}