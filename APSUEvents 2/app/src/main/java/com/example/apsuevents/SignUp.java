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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignUp extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    private EditText eml, pwd, fullname, ph;
    private TextView tw;
    private Button reg, canc;







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mAuth = FirebaseAuth.getInstance();
        eml=(EditText)findViewById(R.id.email_input);
        pwd=(EditText)findViewById(R.id.psswd_input);
        fullname=(EditText)findViewById(R.id.name_input);
        ph=(EditText)findViewById(R.id.phone_input);
        reg=(Button)findViewById(R.id.register);
        canc =(Button) findViewById(R.id.exit);





        mFirebaseInstance = FirebaseDatabase.getInstance();
        mFirebaseDatabase = mFirebaseInstance.getReference("details");

        canc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });

        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullnm = fullname.getText().toString().trim();
                String emailid = eml.getText().toString().trim();
                String number = ph.getText().toString().trim();
                //String uid = mAuth.getCurrentUser().getUid();

                Deets deets = new Deets(fullnm, emailid, number);
                mFirebaseDatabase.push().setValue(deets);

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
            Toast.makeText(this, "Please enter E-mail", Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter Password", Toast.LENGTH_LONG).show();
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