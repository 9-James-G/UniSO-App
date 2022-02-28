package com.group10.uniso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Register extends AppCompatActivity {

    private Spinner spinner;
    String department,imageUrl="https://firebasestorage.googleapis.com/v0/b/uniso-38e5c.appspot.com/o/1640752514918.webp?alt=media&token=666a536c-c31a-4ce9-9652-eee7ac2040a7";
    TextInputEditText rID,rName,rContact, rEmail, rPassword;
    Button RegisterBtn;
    TextView LoginBtn;
    FirebaseAuth fAuth;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        spinner = findViewById(R.id.spinner);
        rID = findViewById(R.id.editRID);
        rName = findViewById(R.id.editRName);
        rContact = findViewById(R.id.editRContact);
        rEmail = findViewById(R.id.staffEmail);
        rPassword = findViewById(R.id.password);
        RegisterBtn = findViewById(R.id.register);
        LoginBtn = findViewById(R.id.logintxt);
        progressBar = findViewById(R.id.progressBar);
        fAuth = FirebaseAuth.getInstance();

        String registered_as = "Teacher";

//        if (fAuth.getCurrentUser() != null) {
//            startActivity(new Intent(getApplicationContext(), Login.class));
//            finish();
//        }

        List<String> categories = new ArrayList<>();
        categories.add(0, "Select your DEPARTMENT" );
        categories.add("CAS");
        categories.add("CEA");
        categories.add("CHTM");
        categories.add("CITE");
        categories.add("COP");
        categories.add("PICE");
        ArrayAdapter<String> dataAdapter;
        dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,
                categories);
        //dropdown layout style

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position,
                                       long id) {
                if(parent.getItemAtPosition(position).equals("Select your DEPARTMENT")){

                }else if(parent.getItemAtPosition(position).equals("CAS")){
                    department = "CAS";
                }else if(parent.getItemAtPosition(position).equals("CEA")){
                    department = "CEA";
                }else if(parent.getItemAtPosition(position).equals("CHTM")){
                    department = "CHTM";
                }else if(parent.getItemAtPosition(position).equals("CITE")){
                    department = "CITE";
                }else if(parent.getItemAtPosition(position).equals("COP")){
                    department = "COP";
                }else if(parent.getItemAtPosition(position).equals("PICE")){
                    department = "PICE";
                }else {
                    // N/A - Wait for other departments....
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        RegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id, name, contact, email, password;
                id= rID.getText().toString().trim();
                name = rName.getText().toString().trim();
                contact = rContact.getText().toString().trim();
                email = rEmail.getText().toString().trim();
                password = rPassword.getText().toString().trim();
                if (TextUtils.isEmpty(id)) {
                    rID.setError("ID required!");
                    return;
                }
                if (TextUtils.isEmpty(name)) {
                    rName.setError("Name required!");
                    return;
                }
                if (TextUtils.isEmpty(contact)|| contact.length()<11|| contact.length()>11) {
                    rContact.setError("Valid contact required!");
                    return;
                }
                if (TextUtils.isEmpty(department)) {
                    Toast.makeText(Register.this, "Please select your department!", Toast.LENGTH_LONG).show();
                }
                if (TextUtils.isEmpty(email)) {
                    rEmail.setError("Email must be valid!");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    rPassword.setError("Password required!");
                    return;
                }
                if (password.length() < 6) {
                    rPassword.setError("Password must have at least 6 characters.");
                    return;
                }
                Query queryID=FirebaseDatabase.getInstance().getReference().child("Users").orderByChild("id").equalTo(id);
                queryID.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.getChildrenCount()>0){
                            rID.setError("ID already in use!");
                            return;
                        }else{
                            fAuth.createUserWithEmailAndPassword(email, password)
                                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {
                                                progressBar.setVisibility(View.VISIBLE);
//                                                String token=SharedPrefManager.getInstance(Register.this).getToken();
                                                User user = new User(id,name, contact,department, email,registered_as,imageUrl);
                                                FirebaseDatabase.getInstance().getReference("Users")
                                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                        .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if(task.isSuccessful()){
                                                            FirebaseUser verify = FirebaseAuth.getInstance().getCurrentUser();
                                                            verify.sendEmailVerification();
                                                            Toast.makeText(Register.this, "Verification sent!", Toast.LENGTH_LONG).show();
                                                            progressBar.setVisibility(View.INVISIBLE);
//                                                            Intent new_intent =new Intent(Register.this, Login.class);
//                                                            startActivity(new_intent);
                                                            finish();
                                                        }else{
                                                            Toast.makeText(Register.this, "Failed to Register!", Toast.LENGTH_LONG).show();
                                                        }
                                                    }
                                                });
                                            } else {
                                                Toast.makeText(Register.this, "Error !" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                                            }
                                        }
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                //registering to firebase
                //

            }
        });

        LoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
//                startActivity(new Intent(getApplicationContext(), Login.class));
            }
        });

    }
}