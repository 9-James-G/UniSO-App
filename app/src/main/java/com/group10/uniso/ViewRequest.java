package com.group10.uniso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ViewRequest extends AppCompatActivity {
    ImageView profileImg;
    TextView viewReq,viewSt;
    DatabaseReference dbReference,dbReceived,dbReturned,dbRequests;
    String verify,reqstatus,time,userID;
    DrawerLayout drawerLayout;
    FirebaseAuth fAuth;
    FirebaseUser user;
    Calendar calendar;
    SimpleDateFormat simpleDateFormat;
    long maxid=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_request);
        fAuth = FirebaseAuth.getInstance();
        user = fAuth.getCurrentUser();
        userID = user.getUid();
        profileImg = findViewById(R.id.profileImg);
        viewReq = findViewById(R.id.viewReq);
        viewSt = findViewById(R.id.viewSt);
        Button btnApproved = findViewById(R.id.btnApprove);
        Button btnNotApproved = findViewById(R.id.btnNotApproved);
        Button returned = findViewById(R.id.returned);
        dbReference = FirebaseDatabase.getInstance().getReference("Users");
        dbReceived = FirebaseDatabase.getInstance().getReference("Received");
        dbRequests = FirebaseDatabase.getInstance().getReference("Requests");
        dbReturned = FirebaseDatabase.getInstance().getReference("Returned");

        String id= getIntent().getStringExtra("VID");
        String time_requested= getIntent().getStringExtra("VTIME");
        String time_received = getIntent().getStringExtra("VTIME2");
        String time_returned = getIntent().getStringExtra("VTIME3");
        reqstatus = getIntent().getStringExtra("VSTATUS");
        String name = getIntent().getStringExtra("VNAME");
        String delivery = getIntent().getStringExtra("VDELIVER");
        String room = getIntent().getStringExtra("VROOM");
        String contact = getIntent().getStringExtra("VCONTACT");
        String department = getIntent().getStringExtra("VDEPARTMENT");
        String user_email = getIntent().getStringExtra("VEMAIL");
        String item = getIntent().getStringExtra("VREQ");
        String img = getIntent().getStringExtra("VIMG");
        String token = getIntent().getStringExtra("VTOK");

        dbReceived.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(id).exists()){
                verify="exist";
                }else{
                    verify="no";
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Picasso.get().load(img).into(profileImg);
        if(reqstatus.equals("APPROVED")){
            viewSt.setText(Html.fromHtml("<font color='#01FF0B'><b>APPROVED<b></font>"));
            btnApproved.setVisibility(View.GONE);
            viewReq.setText("Requested at: "+time_requested+"\nID: "+id+ "\nFull Name: " +name+"\nDepartment: "+department+"\nDelivery: "+delivery+"\nRoom: "+room+"\nEmail:\n"+user_email +"\nContact No.: "+contact+"\nRequested Item(s):\n"+item);
        }else if(reqstatus.equals("RECEIVED")){
            viewSt.setText(Html.fromHtml("<font color='#065009'><b>RECEIVED<b></font>"));
            btnApproved.setVisibility(View.GONE);
            btnNotApproved.setVisibility(View.GONE);
            returned.setVisibility(View.VISIBLE);
            viewReq.setText("Requested at: "+time_requested+"\nReceived at: "+time_received+"\nID: "+id+ "\nFull Name: " +name+"\nDepartment: "+department+"\nDelivery: "+delivery+"\nRoom: "+room+"\nEmail:\n"+user_email +"\nContact No.: "+contact+"\nRequested Item(s):\n"+item);
        }else if(reqstatus.equals("RETURNED")){
            viewSt.setText(Html.fromHtml("<font color='#000000'><b>RETURNED<b></font>"));
            btnApproved.setVisibility(View.GONE);
            btnNotApproved.setVisibility(View.GONE);
            returned.setVisibility(View.GONE);
            viewReq.setText("RETURNED AT: "+time_returned+"\n\nRequested at: "+time_requested+"\nReceived at: "+time_received+"\nID: "+id+ "\nFull Name: " +name+"\nDepartment: "+department+"\nDelivery: "+delivery+"\nRoom: "+room+"\nEmail:\n"+user_email +"\nContact No.: "+contact+"\nRequested Item(s):\n"+item);
        }else{
        viewSt.setText(reqstatus);
            viewReq.setText("Requested at: "+time_requested+"\nID: "+id+ "\nFull Name: " +name+"\nDepartment: "+department+"\nDelivery: "+delivery+"\nRoom: "+room+"\nEmail:\n"+user_email +"\nContact No.: "+contact+"\nRequested Item(s): \n"+item);
        }

        btnApproved.setOnClickListener(v-> {
            if (verify.equals("exist")) {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(ViewRequest.this);
                builder.setTitle("APPROVAL INVALID!");
                builder.setMessage("Requested item(s) not yet returned!\nPlease verify first if the user who made the request had already returned their past requested item(s) before approving their new request.");
                builder.setIcon(R.drawable.ic_baseline_warning_24);
                builder.setPositiveButton("VERIFY", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intentx = new Intent(ViewRequest.this,AdminReceived.class);
                        startActivity(intentx);
                        finish();
                    }
                });
                builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
            }else{
                AlertDialog.Builder builder = new AlertDialog.Builder(ViewRequest.this);
                builder.setTitle("CONFIRMATION");
                builder.setMessage("Approve this request?");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //start
                        Map<String, Object> map = new HashMap<>();
                        map.put("status", "APPROVED");
                        dbRequests.child(id).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                FcmNotificationsSender notificationsSender = new FcmNotificationsSender(token,
                                        "APPROVED",
                                        "Your request has been approved",getApplicationContext(),ViewRequest.this);
                                notificationsSender.SendNotifications();
                                Toast.makeText(ViewRequest.this, "Data updated!", Toast.LENGTH_SHORT).show();
                                redirectActivity(ViewRequest.this,AdminApproved.class);finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(ViewRequest.this, "Failed to update!", Toast.LENGTH_SHORT).show();
                            }
                        });//end
                    }});
                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }});
                builder.show();

        }
        });
        btnNotApproved.setOnClickListener(v->{
            AlertDialog.Builder builder = new AlertDialog.Builder(ViewRequest.this);
            builder.setTitle("CONFIRMATION");
            builder.setMessage("Deny this request?");
            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Map<String, Object> map= new HashMap<>();
                    map.put("status","NOT APPROVED");
                    dbRequests.child(id).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            FcmNotificationsSender notificationsSender = new FcmNotificationsSender(token,
                                    "DENIED",
                                    "Your request has been denied.",getApplicationContext(),ViewRequest.this);
                            notificationsSender.SendNotifications();
                            Toast.makeText(ViewRequest.this, "Data updated!", Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(getApplicationContext(), AdminSide.class);
//                    startActivity(intent);
//                    onDestroy();
                            redirectActivity(ViewRequest.this,AdminSide.class);finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ViewRequest.this, "Failed to update!", Toast.LENGTH_SHORT).show();
                        }
                    });

                }});
            builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }});
            builder.show();

        });

        dbReturned.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    maxid=(snapshot.getChildrenCount());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        returned.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(ViewRequest.this);
                builder.setTitle("CONFIRMATION");
                builder.setMessage("Did they already returned the item(s) they borrowed?");
                builder.setIcon(R.drawable.ic_baseline_help_center_24);
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dbReceived.child(id).removeValue();
                            String time_returned = time ,requested_item=item,delivery_option=delivery,status="RETURNED",imageUrl=img ;

                            Returned returnedDB = new Returned(id,user_email,name,contact,department,room,requested_item,delivery_option,status,imageUrl,time_requested,time_received,time_returned);
                            dbReturned.child(String.valueOf(maxid+1)).setValue(returnedDB).addOnSuccessListener(suc->
                            {
                                Toast.makeText(ViewRequest.this, "Returned item(s) verified!", Toast.LENGTH_SHORT).show();
                                redirectActivity(ViewRequest.this,AdminReceived.class);finish();
                            }).addOnFailureListener(er-> //suc
                            {
                                Toast.makeText(ViewRequest.this, ""+er.getMessage(), Toast.LENGTH_SHORT).show();

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
//        super.onBackPressed();
        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("hh:mm a");
        time = simpleDateFormat.format(calendar.getTime());
    }
    public void ClickMenu(View view){
        openDrawer(drawerLayout);}

    public static void openDrawer(DrawerLayout drawerLayout) {
        drawerLayout.openDrawer(GravityCompat.START);}

    public void ClickLogo(View view){
        closeDrawer(drawerLayout);}

    public static void closeDrawer(DrawerLayout drawerLayout) {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);}}

    public void ClickApproved(View view){
        redirectActivity(this,AdminApproved.class);finish();}
    public void ClickReceived(View view){
        redirectActivity(this,AdminReceived.class);finish();}
    public void ClickPendingReq(View view){
        redirectActivity(this,AdminSide.class);finish();}
    public void ClickReturned(View view){
        redirectActivity(this,AdminReturned.class);finish();}
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
                        Toast.makeText(ViewRequest.this, "Changed successfully!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ViewRequest.this, "An Error Occurred!" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
    public void ClickLogout(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(ViewRequest.this);
        builder.setTitle("LOGOUT");
        builder.setMessage("Are you sure you want to logout?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseMessaging.getInstance().subscribeToTopic("");
                Map<String, Object> map = new HashMap<>();
                map.put("token","signed out");
                dbReference.child(userID).updateChildren(map);
                SharedPreferences sharedPreferences = getSharedPreferences(Form.PREFS_NAME,0);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("hasloggedIn1",false);
                editor.commit();
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(ViewRequest.this, "Logged out Successfully!", Toast.LENGTH_SHORT).show();
                finishAffinity();
            }});
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }});
        builder.show();}

    public static void redirectActivity(Activity activity, Class aClass){
        Intent intent = new Intent(activity, aClass);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        closeDrawer(drawerLayout);
    }
    @Override
    public void onBackPressed() {
        if(reqstatus.equals("APPROVED")){
        startActivity(new Intent(this, AdminApproved.class));finish();}
        if(reqstatus.equals("PENDING")){
            startActivity(new Intent(this, AdminSide.class));finish();}
        if(reqstatus.equals("RECEIVED")){
            startActivity(new Intent(this, AdminReceived.class));finish();}
        if(reqstatus.equals("RETURNED")){
            startActivity(new Intent(this, AdminReturned.class));finish();}
    }
}