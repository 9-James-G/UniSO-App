package com.group10.uniso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Form extends AppCompatActivity {
    TextInputEditText name, contact, room,department;
    EditText user_email;
    String userID;
    FirebaseAuth fAuth;
    FirebaseUser user;
    DatabaseReference dbReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        fAuth = FirebaseAuth.getInstance();
        user = fAuth.getCurrentUser();
        dbReference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();

        user_email = findViewById(R.id.user_email);
        name = findViewById(R.id.Formname);
        contact = findViewById(R.id.Formcontact);
        room = findViewById(R.id.Formroom);
        department = findViewById(R.id.department);


        dbReference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                User user = snapshot.getValue(User.class);
                if(user!=null){
                    String userEmail,userName, userContact,userDepartment;
                    userEmail = user.email;
                    userName = user.name;
                    userDepartment = user.department;
                    userContact = user.contact;

                    user_email.setText(userEmail);
                    name.setText(userName);
                    contact.setText(userContact);
                    department.setText(userDepartment);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Form.this, "Error!", Toast.LENGTH_SHORT).show();
            }
        });

    }


    public void proceed(View view) {
        String user_email0 = user_email.getText().toString();
        String name0 = name.getText().toString().trim();
        String contact0 = contact.getText().toString().trim();
        String room0 = room.getText().toString().trim();
        String department0 = department.getText().toString().trim();

        if(TextUtils.isEmpty(name0)){
            name.setError("Name required!");
            return;
        }
        if(TextUtils.isEmpty(contact0)) {
            contact.setError("Contact required!");
            return;
        }
        if(TextUtils.isEmpty(department0)) {
            department.setError("Department required!");
            return;
        }
        if(TextUtils.isEmpty(room0)) {
            room.setError("Location required!");
            return;
        }else{
        String user_email= user_email0,name=name0, contact=contact0, room=room0,department=department0;
        Intent intent = new Intent(Form.this, Request.class);

        intent.putExtra("EMAIL",user_email);
        intent.putExtra("NAME",name);
        intent.putExtra("CONTACT",contact);
        intent.putExtra("DEPARTMENT",department);
        intent.putExtra("ROOM",room);
        startActivity(intent);
        }
    }

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        Toast.makeText(Form.this, "Logged out successfully!", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }
}