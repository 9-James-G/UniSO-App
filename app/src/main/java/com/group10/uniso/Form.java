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
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class Form extends AppCompatActivity {
    TextInputEditText name, contact, room,department;
    String userID,user_email,id,imageUrl,status;
    FirebaseAuth fAuth;
    DatabaseReference dbReference,dbRequests;
    FirebaseUser user;
    DrawerLayout drawerLayout;
    Button btn_proc;
    public static String PREFS_NAME="MyPrefsFile";
    TextView tvtv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        fAuth = FirebaseAuth.getInstance();
        user = fAuth.getCurrentUser();
        dbReference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();

        name = findViewById(R.id.Formname);
        contact = findViewById(R.id.Formcontact);
        room = findViewById(R.id.Formroom);
        department = findViewById(R.id.department);
        tvtv = findViewById(R.id.tvtv);
        btn_proc= findViewById(R.id.btn_proc);
        drawerLayout = findViewById(R.id.drawer_layout);
        dbRequests = FirebaseDatabase.getInstance().getReference("Requests");

//        if (fAuth.getCurrentUser() != null) {
//            Map<String, Object> map = new HashMap<>();
//            map.put("token", SharedPrefManager.getInstance(Form.this).getToken());
//            dbReference.child(userID).updateChildren(map);
//        }

        dbReference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                User user = snapshot.getValue(User.class);
                if(user!=null){
                    String userId,userEmail,userName, userContact,userDepartment,userImage;
                    userId = user.id;
                    userEmail = user.email;
                    userName = user.name;
                    userDepartment = user.department;
                    userContact = user.contact;
                    userImage = user.imageUrl;
                    id = userId;
                    user_email=userEmail;
                    name.setText(userName);
                    contact.setText(userContact);
                    department.setText(userDepartment);
                    imageUrl =userImage;
                }dbRequests.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.child(id).exists()){
                            dbRequests.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    Requests requests = snapshot.getValue(Requests.class);
                                    if(requests!=null){
                                        String display_status;

                                        display_status = requests.status;
                                        status=display_status;
                                        //start
                                        Map<String, Object> map = new HashMap<>();
                                        map.put("token", SharedPrefManager.getInstance(Form.this).getToken());
                                        dbRequests.child(id).updateChildren(map);
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(Form.this, "Unable to fetch data!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }else{
                            status="else";
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Form.this, "Error!", Toast.LENGTH_SHORT).show();
            }
        });
//        dbRequests.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if(snapshot.child(id).exists()){
//                    dbRequests.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//                            Requests requests = snapshot.getValue(Requests.class);
//                            if(requests!=null){
//                                String display_status;
//
//                                display_status = requests.status;
//                                status=display_status;
//                            }
//                        }
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError error) {
//                            Toast.makeText(Form.this, "Unable to fetch data!", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                }else{
//                    status="else";
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });


    }//oncreate

    public void ClickMenu(View view){
        openDrawer(drawerLayout);}

    public static void openDrawer(DrawerLayout drawerLayout) {
        drawerLayout.openDrawer(GravityCompat.START);}

    public void ClickLogo(View view){
        closeDrawer(drawerLayout);}

    public static void closeDrawer(DrawerLayout drawerLayout) {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);}}


    public void ClickItems(View view){
        recreate();}

    public void ClickRequestsList(View view){
        redirectActivity(this,RequestsList.class);finish();}

    public void ClickProfile(View view){
        redirectActivity(this,Profile.class);finish();}
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
                        Toast.makeText(Form.this, "Changed successfully!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Form.this, "An Error Occurred!" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
    public void ClickLogout(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(Form.this);
        builder.setTitle("LOGOUT");
        builder.setMessage("Are you sure you want to logout?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                Map<String, Object> map = new HashMap<>();
//                map.put("token","signed out");
//                dbReference.child(userID).updateChildren(map);
                dbRequests.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.child(id).exists()){
                            dbRequests.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
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
                                    Toast.makeText(Form.this, "Unable to fetch data!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }else{
                            status="else";
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
                Toast.makeText(Form.this, "Logged out Successfully!", Toast.LENGTH_SHORT).show();
                finishAffinity();
            }});
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }});
        builder.show();}



    public static void redirectActivity(Activity activity, Class aClass){
        Intent intent1 = new Intent(activity, aClass);
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent1);
    }

    @Override
    protected void onPause() {
        super.onPause();
        closeDrawer(drawerLayout);
    }

    public void proceed(View view) {
        if(status.equals("APPROVED")){
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(Form.this);
            builder.setTitle("REQUEST APPROVED!");
            builder.setMessage("Your request have been approved. Make sure you have received the item(s) first before requesting a new one!");
            builder.setIcon(R.drawable.ic_check);
            builder.setPositiveButton("VIEW", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intentx = new Intent(Form.this,RequestsList.class);
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

    }else if(status.equals("PENDING")) {
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(Form.this);
            builder.setTitle("CONFIRMATION");
            builder.setMessage("This action will change your previous request.");
            builder.setIcon(R.drawable.ic_baseline_help_center_24);
            builder.setPositiveButton("PROCEED", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String name0 = name.getText().toString().trim();
                    String contact0 = contact.getText().toString().trim();
                    String room0 = room.getText().toString().trim();
                    String department0 = department.getText().toString().trim();
                    if (TextUtils.isEmpty(name0)) {
                        name.setError("Name required!");
                        return;
                    }
                    if (TextUtils.isEmpty(contact0) || contact0.length() < 11 || contact0.length() > 11) {
                        contact.setError("Valid contact required!");
                        return;
                    }
                    if (TextUtils.isEmpty(department0)) {
                        department.setError("Department required!");
                        return;
                    }
                    if (TextUtils.isEmpty(room0)) {
                        room.setError("Location required!");
                        return;
                    } else {
                        String name = name0, contact = contact0, room = room0, department = department0;
                        Intent intent = new Intent(Form.this, Request.class);
                        intent.putExtra("ID", id);
                        intent.putExtra("EMAIL", user_email);
                        intent.putExtra("NAME", name);
                        intent.putExtra("CONTACT", contact);
                        intent.putExtra("DEPARTMENT", department);
                        intent.putExtra("ROOM", room);
                        intent.putExtra("IMG", imageUrl);
                        startActivity(intent);
                    }
                }
            });
            builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.show();
        }else{
        String name0 = name.getText().toString().trim();
        String contact0 = contact.getText().toString().trim();
        String room0 = room.getText().toString().trim();
        String department0 = department.getText().toString().trim();
        if (TextUtils.isEmpty(name0)) {
            name.setError("Name required!");
            return;
        }
        if (TextUtils.isEmpty(contact0) || contact0.length() < 11 || contact0.length() > 11) {
            contact.setError("Valid contact required!");
            return;
        }
        if (TextUtils.isEmpty(department0)) {
            department.setError("Department required!");
            return;
        }
        if (TextUtils.isEmpty(room0)) {
            room.setError("Location required!");
            return;
        } else {
            String name = name0, contact = contact0, room = room0, department = department0;
            Intent intent = new Intent(Form.this, Request.class);
            intent.putExtra("ID", id);
            intent.putExtra("EMAIL", user_email);
            intent.putExtra("NAME", name);
            intent.putExtra("CONTACT", contact);
            intent.putExtra("DEPARTMENT", department);
            intent.putExtra("ROOM", room);
            intent.putExtra("IMG", imageUrl);
            startActivity(intent);
        }
    }//else
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
