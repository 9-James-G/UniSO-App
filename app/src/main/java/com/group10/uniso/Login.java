package com.group10.uniso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {
    TextInputEditText rEmail, rPassword;
    Button rLoginBtn1;
    TextView rcreateBtn,reset;
    FirebaseAuth fAuth;
    ProgressBar progressBar;
    SharedPreferences sharedPreferences;
    DatabaseReference dbReference;
    FirebaseUser user;
    String userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        rEmail = findViewById(R.id.staffEmail);
        rPassword = findViewById(R.id.password);
        fAuth = FirebaseAuth.getInstance();
        dbReference = FirebaseDatabase.getInstance().getReference("Requests");
        progressBar = findViewById(R.id.progressBar);
        rLoginBtn1 = findViewById(R.id.loginBtn1);
        rcreateBtn = findViewById(R.id.createBtn);
        reset = findViewById(R.id.forgot);

//        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME,MODE_PRIVATE);
//
//        my_pref = sharedPreferences.getString(KEY_PREF, null);
//
//        if (my_pref!=null){
//            Intent intent = new Intent(MainActivity.this,Form.class);
//            startActivity(intent);
//        }
        CancelledItem cai = new CancelledItem();
        rLoginBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email, password;
                email = rEmail.getText().toString().trim();
                password = rPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    rEmail.setError("User not recognized!");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    rPassword.setError("Password required!");
                    return;
                }

//                if (user_email!=null) {
//                    MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(MainActivity.this);
//                    builder.setTitle("Warning!");
//                    builder.setMessage("Logging into different account will cancel the request of the previous user");
//                    builder.setIcon(R.drawable.ic_baseline_warning_24);
//                    builder.setPositiveButton("PROCEED", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            dbReference.child(name).removeValue();
//                            Cancelled emp = new Cancelled(user_email, name, contact, department, room, item, delivery);
//                            cai.add(emp).addOnSuccessListener(suc ->
//                            {
//                                SharedPreferences.Editor editor = sharedPreferences.edit();
//                                editor.clear();
//                                editor.commit();
//                                Toast.makeText(MainActivity.this, "Login successfully!", Toast.LENGTH_SHORT).show();
//                                finish();
//                                Intent intent = new Intent(MainActivity.this, Form.class);
//                                startActivity(intent);
//                            }).addOnFailureListener(er ->
//                            {
//                                Toast.makeText(MainActivity.this, "" + er.getMessage(), Toast.LENGTH_SHORT).show();
//
//                            });
//                        }
//                    });
//                    builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            Toast.makeText(MainActivity.this, "Login cancelled!", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                    builder.show();
//                }else{
                progressBar.setVisibility(View.VISIBLE);

                //authenticate
                fAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    user = FirebaseAuth.getInstance().getCurrentUser();
                                    dbReference = FirebaseDatabase.getInstance().getReference("Users");
                                    userID = user.getUid();
                                    if(user.isEmailVerified()){
                                        //copied
                                        dbReference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                User user = snapshot.getValue(User.class);
                                                if(user!=null){
                                                    String registered;
                                                    registered = user.registered_as;
                                                    if(registered.equals("Teacher")) {
                                                        SharedPreferences sharedPreferences = getSharedPreferences(Form.PREFS_NAME,0);
                                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                                        editor.putBoolean("hasloggedIn",true);
                                                        editor.commit();
                                                        Toast.makeText(Login.this, "Login Successfully!", Toast.LENGTH_SHORT).show();
                                                        Intent intent = new Intent(getApplicationContext(), Form.class);
                                                        startActivity(intent);
                                                        progressBar.setVisibility(View.INVISIBLE);
                                                        finish();
                                                    }else{
                                                        SharedPreferences sharedPreferences = getSharedPreferences(Form.PREFS_NAME,0);
                                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                                        editor.putBoolean("hasloggedIn1",true);
                                                        editor.commit();
                                                        Toast.makeText(Login.this, "Login Successfully!", Toast.LENGTH_SHORT).show();
                                                        Intent intent = new Intent(getApplicationContext(), AdminSide.class);
                                                        startActivity(intent);
                                                        progressBar.setVisibility(View.INVISIBLE);
                                                        finish();
                                                    }
                                                }
                                            }
                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {
                                                Toast.makeText(Login.this, "Error!", Toast.LENGTH_SHORT).show();
                                            }
                                        });//copied

                                    }else{
                                        Toast.makeText(Login.this, "Error !" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.INVISIBLE);
                                    }

                                } else {
                                    Toast.makeText(Login.this, "Error !" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.INVISIBLE);
                                }
                            }
                        });
//            }

            }
        });

        rcreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Register.class);
                startActivity(intent);
//                redirectActivity(Login.this,Register.class);
//                Intent new_intent = new Intent(Login.this, Register.class);
//                startActivity(new_intent);
//                if (user_email!=null) {
//                    MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(MainActivity.this);
//                    builder.setTitle("Warning!");
//                    builder.setMessage("Logging into different account will cancel the request of the previous user");
//                    builder.setIcon(R.drawable.ic_baseline_warning_24);
//                    builder.setPositiveButton("PROCEED", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            dbReference.child(name).removeValue();
//                            Cancelled emp = new Cancelled(user_email, name, contact, department, room, item, delivery);
//                            cai.add(emp).addOnSuccessListener(suc ->
//                            {
//                                SharedPreferences.Editor editor = sharedPreferences.edit();
//                                editor.clear();
//                                editor.commit();
//                                Toast.makeText(MainActivity.this, "Login successfully!", Toast.LENGTH_SHORT).show();
//                                finish();
//                                Intent intent = new Intent(MainActivity.this, Register.class);
//                                startActivity(intent);
//                            }).addOnFailureListener(er ->
//                            {
//                                Toast.makeText(MainActivity.this, "" + er.getMessage(), Toast.LENGTH_SHORT).show();
//
//                            });
//                        }
//                    });
//                    builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//
//                        }
//                    });
//                    builder.show();
//                }else{
//                finish();
//                }
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final AlertDialog.Builder resetDialog = new AlertDialog.Builder(v.getContext());
                View mView = getLayoutInflater().inflate(R.layout.resetpassword,null);
                final EditText resetMail = (EditText) mView.findViewById(R.id.resetMail);


                resetDialog.setPositiveButton("PROCEED", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                    }
                });resetDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                resetDialog.setView(mView);
                AlertDialog dialog =resetDialog.create();
                dialog.show();
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String mail = resetMail.getText().toString();
                        if (TextUtils.isEmpty(mail)) {
                            resetMail.setError("Email cannot be empty.");
                            return;
                        }else{
                        fAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(Login.this, "A link was sent to your email!", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Login.this, "An Error Occurred!" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    }
                });
            }
        });

    }

    public void viewDevelopers(View view) {
        Intent intent = new Intent(Login.this,JSONapi.class);
        startActivity(intent);
    }
}