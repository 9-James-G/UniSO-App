package com.group10.uniso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class AdminSide extends AppCompatActivity {
    RecyclerView recyclerView;
    DatabaseReference database;
    MyAdapter myAdapter;
    ArrayList<GetReq> list;
    FirebaseAuth fAuth;
    FirebaseUser user;
    String userID;
    DatabaseReference dbReference,dbRequests;
    DrawerLayout drawerLayout;
    public static String PREFS_NAME="MyPrefsFile";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_side);

        FirebaseMessaging.getInstance().subscribeToTopic("all");

        fAuth = FirebaseAuth.getInstance();
        user = fAuth.getCurrentUser();
        dbReference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();
        recyclerView = findViewById(R.id.requestsList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        myAdapter = new MyAdapter(this,list);
        recyclerView.setAdapter(myAdapter);
//        if (fAuth.getCurrentUser() != null) {
//            Map<String, Object> map = new HashMap<>();
//            map.put("token", SharedPrefManager.getInstance(AdminSide.this).getToken());
//            dbReference.child(userID).updateChildren(map);
//        }
//        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
//        layoutManager.setReverseLayout(true);
//        layoutManager.setStackFromEnd(true);
////        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//
//        list = new ArrayList<>();
//        myAdapter = new MyAdapter(this,list);
//        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.setAdapter(myAdapter);
//        user = fAuth.getCurrentUser();wewqeqw
//        dbReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//list.clear();
//                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
//
//                    GetReq requests = dataSnapshot.getValue(GetReq.class);
//                    list.add(requests);
//
//                }
//                myAdapter.notifyDataSetChanged();
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

        Query pen_query = FirebaseDatabase.getInstance().getReference("Requests")
                .orderByChild("status")
                .equalTo("PENDING");
        pen_query.addListenerForSingleValueEvent(valueEventListener);
        drawerLayout = findViewById(R.id.drawer_layout);}
    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
//            list.clear();
            if (dataSnapshot.exists()) {
                list.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){

                    GetReq requests = snapshot.getValue(GetReq.class);
                    list.add(requests);

                }
                Collections.sort(list, new Comparator<GetReq>() {
                    @Override
                    public int compare(GetReq o1, GetReq o2) {
                        return o1.time_requested.compareToIgnoreCase(o2.time_requested);
                    }
                });
                myAdapter.notifyDataSetChanged();
            }else{
                list.clear();
                Toast.makeText(AdminSide.this, "Nothing to see here.", Toast.LENGTH_SHORT).show();
                myAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

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
        recreate();}
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
                        Toast.makeText(AdminSide.this, "Changed successfully!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AdminSide.this, "An Error Occurred!" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
    public void ClickLogout(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(AdminSide.this);
        builder.setTitle("LOGOUT");
        builder.setMessage("Are you sure you want to logout?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseMessaging.getInstance().unsubscribeFromTopic("all");
//                Map<String, Object> map = new HashMap<>();
//                map.put("token","signed out");
//                dbReference.child(userID).updateChildren(map);
                SharedPreferences sharedPreferences = getSharedPreferences(Form.PREFS_NAME,0);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("hasloggedIn1",false);
                editor.commit();
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(AdminSide.this, "Logged out Successfully!", Toast.LENGTH_SHORT).show();
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
    long back_pressed;

    @Override
    public void onBackPressed() {
        if (back_pressed + 1000 > System.currentTimeMillis()){
            super.onBackPressed();
        }
        else{
            Toast.makeText(getBaseContext(),
                    "Double tap to exit!", Toast.LENGTH_SHORT)
                    .show();
        }
        back_pressed = System.currentTimeMillis();
    }
}
//    Query pen_query = FirebaseDatabase.getInstance().getReference("Requests")
//            .orderByChild("status")
//            .equalTo("PENDING");
//        pen_query.addListenerForSingleValueEvent(valueEventListener);
//                drawerLayout = findViewById(R.id.drawer_layout);}
//                ValueEventListener valueEventListener = new ValueEventListener() {
//@Override
//public void onDataChange(DataSnapshot dataSnapshot) {
//        list.clear();
//        if (dataSnapshot.exists()) {
//        for (DataSnapshot snapshot : dataSnapshot.getChildren()){
//
//        GetReq requests = snapshot.getValue(GetReq.class);
//        list.add(requests);
//
//        }
//        myAdapter.notifyDataSetChanged();
//        }
//        }
//
//@Override
//public void onCancelled(DatabaseError databaseError) {
//
//        }
//        };