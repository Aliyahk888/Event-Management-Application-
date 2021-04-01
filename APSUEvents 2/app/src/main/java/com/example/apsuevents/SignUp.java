package com.example.apsuevents;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUp extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText eml;
    private EditText pwd;
    private Button btn1;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mAuth = FirebaseAuth.getInstance();
        eml=(EditText)findViewById(R.id.email_input);
        //pwd=(EditText)findViewById(R.id.pwd_input);
        btn1=(Button)findViewById(R.id.register);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        if (mAuth.getCurrentUser() != null) {
            finish();
            startActivity(new Intent(getApplicationContext(), HomePage.class));
        }

    }

    private void registerUser() {

        String email = eml.getText().toString().trim();
        String password = pwd.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please enter email", Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter password", Toast.LENGTH_LONG).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    finish();
                    startActivity(new Intent(getApplicationContext(), HomePage.class));
                } else {
                    Toast.makeText(SignUp.this, "Registration error!", Toast.LENGTH_LONG).show();

                }
            }
        });





    }














}