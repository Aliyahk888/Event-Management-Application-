package com.example.apsuevents;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomePage extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private Button buttonLogout;
    private Button host;
    private Button profile;
    private Button explore;
    private Button mine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        firebaseAuth= FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser()==null){
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }

        FirebaseUser user = firebaseAuth.getCurrentUser();
        buttonLogout=(Button)findViewById(R.id.buttonLogout);
        host=(Button)findViewById(R.id.host);
        profile=(Button)findViewById(R.id.account);
        explore=(Button)findViewById(R.id.explore);
        mine=(Button)findViewById(R.id.mine);

        Toast.makeText(this, "Welcome "+user.getEmail(), Toast.LENGTH_LONG).show();
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v==buttonLogout){
                    firebaseAuth.signOut();
                    finish();
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                }

            }
        });

        host.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Scrolltest.class));
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),UserProfile.class));
            }
        });

        explore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), BrowseEvent.class));

            }
        });

        mine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),my_events.class));
            }
        });
    }
}