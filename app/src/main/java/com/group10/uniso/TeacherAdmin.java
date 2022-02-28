package com.group10.uniso;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TeacherAdmin extends AppCompatActivity {
Button teacher, admin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_admin);

        teacher = findViewById(R.id.teacher);
        admin = findViewById(R.id.admin);

        teacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String registered_as = "Teacher";
                Intent intent = new Intent(TeacherAdmin.this, Register.class);
                intent.putExtra("REG_AS",registered_as);
                startActivity(intent);
            }
        });

            admin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String registered_as = "Admin";
                    Intent intent = new Intent(TeacherAdmin.this, Register.class);
                    intent.putExtra("REG_AS",registered_as);
                    startActivity(intent);
                }
            });
    }
}