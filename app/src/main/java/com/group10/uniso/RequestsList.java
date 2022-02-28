package com.group10.uniso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class RequestsList extends AppCompatActivity {
    DrawerLayout drawerLayout;
    TextView reqtv,reqtv2,reqtv3,displayedTitle;
    private Button cancel,receive;
    FirebaseAuth fAuth;
    FirebaseUser user;
    DatabaseReference dbReference,dbRequests,dbReceived;
    String userName,userID,userEmail,id,user_email,name,contact,department,room,requested_item,delivery_option,imageUrl,time_requested,time;
    ImageView delirious;
    ProgressBar progressBar;
    public static String PREFS_NAME="MyPrefsFile";
    Calendar calendar;
    SimpleDateFormat simpleDateFormat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests_list);

        reqtv = findViewById(R.id.reqtv);
        reqtv2 = findViewById(R.id.reqtv2);
        reqtv3= findViewById(R.id.reqtv3);
        displayedTitle = findViewById(R.id.displayTitle);
        delirious= findViewById(R.id.delirious);
        progressBar = findViewById(R.id.progressBar);
        receive = findViewById(R.id.receive);
        cancel = findViewById(R.id.cancel);

        dbRequests = FirebaseDatabase.getInstance().getReference("Requests");
        fAuth = FirebaseAuth.getInstance();
        user = fAuth.getCurrentUser();
        dbReference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();
        dbReceived = FirebaseDatabase.getInstance().getReference("Received");

        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("hh:mm a");
        time = simpleDateFormat.format(calendar.getTime());
//        String user_name = getIntent().getStringExtra("NAME1");

        dbReference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                User user = snapshot.getValue(User.class);
                if(user!=null){
                    String userName1,userEmail1,id1,imageUrl1;
                    userName1 = user.name;
                    userEmail1  = user.email;
                    id1 = user.id;
                    imageUrl1 = user.imageUrl;
                    userName=userName1;
                    userEmail =userEmail1;
                    id = id1;
                    imageUrl = imageUrl1;

                }dbRequests.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.child(id).exists()){
                            dbRequests.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    Requests requests = snapshot.getValue(Requests.class);
                                    if(requests!=null){
                                        receive.setVisibility(View.VISIBLE);
                                        cancel.setVisibility(View.VISIBLE);
                                        progressBar.setVisibility(View.GONE);
                                        delirious.setVisibility(View.GONE);
                                        displayedTitle.setVisibility(View.VISIBLE);
                                        reqtv2.setVisibility(View.VISIBLE);
                                        reqtv3.setVisibility(View.VISIBLE);
                                        reqtv.setVisibility(View.VISIBLE);
                                        String display_userID,display_userEmail,display_userName, display_userContact,display_userDepartment,display_userRoom,display_userItem,display_userDelivery,display_status,req;
                                        display_userID =  requests.id;
                                        display_userEmail = requests.user_email;
                                        display_userName = requests.name;
                                        display_userContact = requests.contact;
                                        display_userDepartment = requests.department;
                                        display_userRoom = requests.room;
                                        display_userItem = requests.requested_item;
                                        display_userDelivery = requests.delivery_option;
                                        display_status = requests.status;
                                        req = requests.time_requested;
                                        reqtv.setText("Requested at: "+req+"\nID: "+display_userID+ "\nFull Name: " +display_userName+"\nDepartment: "+display_userDepartment+"\nDeliver: "+display_userDelivery+"\nRoom: "+display_userRoom+"\nEmail:\n"+display_userEmail +"\nContact No.: "+display_userContact+"\nRequested Item(s):\n"+display_userItem);
                                        if(display_status.equals("PENDING")){
                                            reqtv2.setText("Current status: ");
                                            reqtv3.setText(display_status);
                                            receive.setEnabled(false);}
                                        if(display_status.equals("APPROVED")){
                                            reqtv2.setText("Current status: ");
                                            reqtv3.setText(Html.fromHtml("<font color='#01FF0B'><b>APPROVED<b></font>"));
                                            cancel.setEnabled(false);
                                        }if(display_status.equals("NOT APPROVED")){
                                            reqtv2.setText("Current status: ");
                                            reqtv3.setText(Html.fromHtml("<font color='#FF0000'><b>DENIED<b></font>"));
                                            receive.setEnabled(false);
                                        }
                                        user_email =display_userEmail;
                                        name =display_userName;
                                        contact =display_userContact;
                                        department =display_userDepartment;
                                        room =display_userRoom;
                                        requested_item =display_userItem;
                                        delivery_option =display_userDelivery;
                                        time_requested = req;
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(RequestsList.this, "Unable to fetch data!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }else{
                            progressBar.setVisibility(View.GONE);
                            reqtv2.setVisibility(View.VISIBLE);
                            delirious.setVisibility(View.VISIBLE);
                            displayedTitle.setVisibility(View.GONE);
                            reqtv.setVisibility(View.GONE);
                            receive.setVisibility(View.GONE);
                            cancel.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(RequestsList.this, "Error!", Toast.LENGTH_SHORT).show();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(RequestsList.this);
                builder.setTitle("WARNING!");
                builder.setMessage("This action will remove your pending request!");
                builder.setIcon(R.drawable.ic_baseline_warning_24);
                builder.setPositiveButton("PROCEED", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dbRequests.child(id).removeValue();
                        recreate();
                    }
                });
                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }//onClick
                });//logout
                builder.show();
            }
        });
//        new
//        dbReference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
//                    String value = String.valueOf(dataSnapshot.child("user_email").getValue());
//                    Log.i("",value);
//                    if(userEmail.equals(value)){
//                        reqtv.setText(userEmail);
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//        end new
//
//        dbReference.child(user_name).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                Requests requests = snapshot.getValue(Requests.class);
//                if(requests!=null){
//                    receive.setVisibility(View.VISIBLE);
//                    cancel.setVisibility(View.VISIBLE);
//                    String userEmail,userName, userContact,userDepartment,userRoom,userItem,userDelivery,status1;
//                    userEmail = requests.user_email;
//                    userName = requests.name;
//                    userContact = requests.contact;
//                    userDepartment = requests.department;
//                    userRoom = requests.room;
//                    userItem = requests.requested_item;
//                    userDelivery = requests.delivery_option;
//                    status1 = requests.status;
//                    reqtv.setText("Email: "+userEmail + "\nFull Name: " +userName+"\nContact: "+userContact+"\nDepartment: "+userDepartment+"\nRoom: "+userRoom+"\nItem: "+userItem+"\nDelivery: "+userDelivery);
//                    reqtv2.setText("Your request is: "+status1);
//
//                    user_email = userEmail;
//                    name = userName;
//                    contact = userContact;
//                    department = userDepartment;
//                    room = userRoom;
//                    requested_item = userItem;
//                    delivery_option = userDelivery;
//                    status = status1;
//                }
//                if(requests.equals(null)){
//                    receive.setVisibility(View.GONE);
//                    cancel.setVisibility(View.GONE);
//                    reqtv.setVisibility(View.GONE);
//                    reqtv2.setText("You have not made any request yet.");
//                }
//                if(status.equals("APPROVED")){
//                    cancel.setEnabled(false);
//                }
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(RequestsList.this, "Unable to fetch data!", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        CancelledItem cai = new CancelledItem();
//        cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(RequestsList.this);
//                builder.setTitle("Warning!");
//                builder.setMessage("This will cancel your request!");
//                builder.setIcon(R.drawable.ic_baseline_warning_24);
//                builder.setPositiveButton("PROCEED", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dbReference.child(name).removeValue();
//                        Cancelled emp =new Cancelled(user_email,name,contact,department,room,requested_item,delivery_option);
//                        cai.add(emp).addOnSuccessListener(suc->
//                        {
//                            Toast.makeText(RequestsList.this, "Request cancelled", Toast.LENGTH_SHORT).show();
//                            Intent intent = new Intent(RequestsList.this,Form.class);
//                            startActivity(intent);
//                        }).addOnFailureListener(er->
//                        {
//                            Toast.makeText(RequestsList.this, ""+er.getMessage(), Toast.LENGTH_SHORT).show();
//
//                        });
//                    }
//                });
//                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                    }//onClick
//                });//logout
//                builder.show();
//            }
//        });
//
//
//
//        ReceivedItem rei = new ReceivedItem();
        receive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(RequestsList.this);
                builder.setTitle("CONFIRMATION");
                builder.setMessage("Did you already received your item?");
                builder.setIcon(R.drawable.ic_baseline_help_center_24);
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dbRequests.child(id).removeValue();
                        String status = "RECEIVED";
                        String time_received = time;
                        Received emp =new Received(id,user_email,name,contact,department,room,requested_item,delivery_option,status,imageUrl,time_requested,time_received);
                        dbReceived.child(id).setValue(emp).addOnSuccessListener(suc->
                        {
                            Toast.makeText(RequestsList.this, "Thank you for your patience!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RequestsList.this,Form.class);
                            startActivity(intent);
                        }).addOnFailureListener(er-> //suc
                        {
                            Toast.makeText(RequestsList.this, ""+er.getMessage(), Toast.LENGTH_SHORT).show();

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
drawerLayout = findViewById(R.id.drawer_layout);
    }
    public void ClickMenu(View view){
        Form.openDrawer(drawerLayout);
    }

    public void ClickLogo(View view){
        Form.closeDrawer(drawerLayout);}

//    public void ClickNewReq(View view){
//        Form.redirectActivity(this,Form.class);
//        finish();}

    public void ClickItems(View view){
        Form.redirectActivity(this,Form.class);
        finish();
    }

    public void ClickRequestsList(View view){
        recreate();}

    public void ClickProfile(View view){
        Form.redirectActivity(this,Profile.class);
        finish();}
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
                        Toast.makeText(RequestsList.this, "Changed successfully!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RequestsList.this, "An Error Occurred!" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
    public void ClickLogout(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(RequestsList.this);
        builder.setTitle("LOGOUT");
        builder.setMessage("Are you sure you want to logout?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                Map<String, Object> map = new HashMap<>();
//                map.put("token","signed out");
//                dbReference.child(userID).updateChildren(map);
                dbRequests.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.child(id).exists()){
                            dbRequests.child(id).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    Requests requests = snapshot.getValue(Requests.class);
                                    if(requests!=null){
                                        Map<String, Object> map = new HashMap<>();
                                        map.put("token", "signed out");
                                        dbRequests.child(id).updateChildren(map);
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(RequestsList.this, "Unable to fetch data!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }else{
                            String status="else";
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                SharedPreferences sharedPreferences = getSharedPreferences(Form.PREFS_NAME,0);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("hasloggedIn",false);
                editor.commit();
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(RequestsList.this, "Logged out Successfully!", Toast.LENGTH_SHORT).show();
                finishAffinity();


            }});
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }});
        builder.show();}
    @Override
    protected void onPause(){
        super.onPause();
        Form.closeDrawer(drawerLayout);

    }
    long back_pressed;

    @Override
    public void onBackPressed() {
        if (back_pressed + 1000 > System.currentTimeMillis()){
            super.onBackPressed();
            finishAffinity();
        }
        else{
            Toast.makeText(getBaseContext(),
                    "Double tap to exit!", Toast.LENGTH_SHORT)
                    .show();
        }
        back_pressed = System.currentTimeMillis();
    }
}
//     dbReference.addValueEventListener(new ValueEventListener() {
//@Override
//public void onDataChange(@NonNull DataSnapshot snapshot) {
//        boolean breakLoop = false;
//        while (!breakLoop) {
//        for(DataSnapshot dataSnapshot : snapshot.getChildren()){
//        String value = String.valueOf(dataSnapshot.child("user_email").getValue());
//        Log.i("",value);
//        if(userEmail.equals(value)){
//        breakLoop = true;
//        }else{
//        break;
//        }
//        }
//        }if(breakLoop){
//        dbReference.child(userName).addValueEventListener(new ValueEventListener() {
//@Override
//public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//        Requests requests = snapshot.getValue(Requests.class);
//        if(requests!=null){
//        String reqEmail,reqName, reqContact,reqDepartment,reqRoom,reqItem,reqDelivery,status1;
//        reqEmail = requests.user_email;
//        reqName = requests.name;
//        reqContact = requests.contact;
//        reqDepartment = requests.department;
//        reqRoom = requests.room;
//        reqItem = requests.requested_item;
//        reqDelivery = requests.delivery_option;
//        status1 = requests.status;
//        reqtv.setText("Email: "+reqEmail + "\nFull Name: " +reqName+"\nContact: "+reqContact+"\nDepartment: "+reqDepartment+"\nRoom: "+reqRoom+"\nItem: "+reqItem+"\nDelivery: "+reqDelivery);
//        reqtv2.setText("Your request is: "+status1);
//
//        }
//
//        }
//@Override
//public void onCancelled(@NonNull DatabaseError error) {
//        Toast.makeText(RequestsList.this, "Unable to fetch data!", Toast.LENGTH_SHORT).show();
//        }
//        });
//        }else{
//        reqtv.setText("wewewews");
//        }
//        }
//
//@Override
//public void onCancelled(@NonNull DatabaseError error) {
//
//        }
//        });