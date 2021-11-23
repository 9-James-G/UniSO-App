package com.group10.uniso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {
    //TextInputEditText - Material Design
    TextInputEditText rName,rContact,rEmail, rPassword,rDep ;
    Button RegisterBtn;
    TextView LoginBtn;
    FirebaseAuth fAuth;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        rName = findViewById(R.id.editRName);
        rContact = findViewById(R.id.editRContact);
        rDep = findViewById(R.id.editDep);
        rEmail = findViewById(R.id.staffEmail);
        rPassword = findViewById(R.id.password);

        RegisterBtn = findViewById(R.id.register);
        LoginBtn = findViewById(R.id.logintxt);

        fAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar);

        if (fAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }

        RegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name, contact,department, email, password;

                name = rName.getText().toString().trim();
                contact = rContact.getText().toString().trim();
                department = rDep.getText().toString().trim();
                email = rEmail.getText().toString().trim();
                password = rPassword.getText().toString().trim();

                if (TextUtils.isEmpty(name)) {
                    rName.setError("Name required!");
                    return;
                }
                if (TextUtils.isEmpty(contact)) {
                    rContact.setError("Contact required!");
                    return;
                }
                if (TextUtils.isEmpty(department)) {
                    rDep.setError("Department required!");
                    return;
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
                    rPassword.setError("Password must be 6 characters long.");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                //registering to firebase
                fAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    User user = new User(name, contact,department, email);
                                    FirebaseDatabase.getInstance().getReference("Users")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(Register.this, "User Created!", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(getApplicationContext(), Form.class);
                                                startActivity(intent);
                                            }else{
                                                Toast.makeText(Register.this, "Failed to Register!", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                                } else {
                                    Toast.makeText(Register.this, "Error !" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.INVISIBLE);
                                }
                            }
                        });

            }
        });

        LoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

    }
}