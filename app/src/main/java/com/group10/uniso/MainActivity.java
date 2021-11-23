package com.group10.uniso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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
import android.content.DialogInterface;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    TextInputEditText rEmail, rPassword;
    Button rLoginBtn1;
    TextView rcreateBtn;
    FirebaseAuth fAuth;
    ProgressBar progressBar;
    SharedPreferences sharedPreferences;
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
        setContentView(R.layout.activity_main);
        rEmail = findViewById(R.id.staffEmail);
        rPassword = findViewById(R.id.password);
        fAuth = FirebaseAuth.getInstance();
        dbReference = FirebaseDatabase.getInstance().getReference("Requests");
        progressBar = findViewById(R.id.progressBar);
        rLoginBtn1 = findViewById(R.id.loginBtn1);
        rcreateBtn = findViewById(R.id.createBtn);


        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME,MODE_PRIVATE);

        String user_email = sharedPreferences.getString(KEY_EMAIL, null);
        String name = sharedPreferences.getString(KEY_NAME, null);
        String contact = sharedPreferences.getString(KEY_CONTACT, null);
        String department = sharedPreferences.getString(KEY_DEPARTMENT, null);
        String room = sharedPreferences.getString(KEY_ROOM, null);
        String item = sharedPreferences.getString(KEY_ITEM, null);
        String delivery = sharedPreferences.getString(KEY_DELIVERY, null);

        if (user_email!=null){
            Intent intent = new Intent(MainActivity.this,Pending.class);
            startActivity(intent);
        }
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

                if (user_email!=null) {
                    MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(MainActivity.this);
                    builder.setTitle("Warning!");
                    builder.setMessage("Logging into different account will cancel the request of the previous user");
                    builder.setIcon(R.drawable.ic_baseline_warning_24);
                    builder.setPositiveButton("PROCEED", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dbReference.child(name).removeValue();
                            Cancelled emp = new Cancelled(user_email, name, contact, department, room, item, delivery);
                            cai.add(emp).addOnSuccessListener(suc ->
                            {
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.clear();
                                editor.commit();
                                Toast.makeText(MainActivity.this, "Login successfully!", Toast.LENGTH_SHORT).show();
                                finish();
                                Intent intent = new Intent(MainActivity.this, Form.class);
                                startActivity(intent);
                            }).addOnFailureListener(er ->
                            {
                                Toast.makeText(MainActivity.this, "" + er.getMessage(), Toast.LENGTH_SHORT).show();

                            });
                        }
                    });
                    builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(MainActivity.this, "Login cancelled!", Toast.LENGTH_SHORT).show();
                        }
                    });
                    builder.show();
                }else{
                progressBar.setVisibility(View.VISIBLE);

                //authenticate
                fAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(MainActivity.this, "Login Successfully!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), Form.class);
                                    startActivity(intent);

                                } else {
                                    Toast.makeText(MainActivity.this, "Error !" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.INVISIBLE);
                                }
                            }
                        });
            }

            }
        });

        rcreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user_email!=null) {
                    MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(MainActivity.this);
                    builder.setTitle("Warning!");
                    builder.setMessage("Logging into different account will cancel the request of the previous user");
                    builder.setIcon(R.drawable.ic_baseline_warning_24);
                    builder.setPositiveButton("PROCEED", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dbReference.child(name).removeValue();
                            Cancelled emp = new Cancelled(user_email, name, contact, department, room, item, delivery);
                            cai.add(emp).addOnSuccessListener(suc ->
                            {
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.clear();
                                editor.commit();
                                Toast.makeText(MainActivity.this, "Login successfully!", Toast.LENGTH_SHORT).show();
                                finish();
                                Intent intent = new Intent(MainActivity.this, Register.class);
                                startActivity(intent);
                            }).addOnFailureListener(er ->
                            {
                                Toast.makeText(MainActivity.this, "" + er.getMessage(), Toast.LENGTH_SHORT).show();

                            });
                        }
                    });
                    builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    builder.show();
                }else{
                startActivity(new Intent(getApplicationContext(), Register.class));
                }
            }
        });

    }
}
