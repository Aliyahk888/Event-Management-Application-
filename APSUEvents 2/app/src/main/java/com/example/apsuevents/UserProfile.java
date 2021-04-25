package com.example.apsuevents;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserProfile extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    private DatabaseReference mFirebaseDatabase2;
    private FirebaseDatabase mFirebaseInstance2;
    private Button backhome;
    TextView d_username;
    TextView d_email;
    TextView d_mobile;
    Button delacc,home;
    FirebaseUser user1;
    FirebaseUser user2;
    String new_phone;
    String joined_events="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        backhome = (Button) findViewById(R.id.backhome);
        d_username=(TextView)findViewById(R.id.d_username);
        d_email=(TextView)findViewById(R.id.d_email);
        d_mobile=(TextView)findViewById(R.id.d_mobile);
        delacc=(Button)findViewById(R.id.delacc);
        user1 = mAuth.getInstance().getCurrentUser();
        home=(Button)findViewById(R.id.home);

        mFirebaseInstance = FirebaseDatabase.getInstance();

        // get reference to 'details' node
        mFirebaseDatabase = mFirebaseInstance.getReference("details");

        mFirebaseInstance2 = FirebaseDatabase.getInstance();

        // get reference to 'users' node
        mFirebaseDatabase2 = mFirebaseInstance.getReference("users");

        final FirebaseUser user1 = FirebaseAuth.getInstance().getCurrentUser();

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), HomePage.class));
            }
        });

        backhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(UserProfile.this);
                mFirebaseDatabase2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for (DataSnapshot childSnapshot : snapshot.getChildren()){
                            if (childSnapshot.child("Attendance").child(new_phone).exists()){
                                joined_events=joined_events+"\n"+childSnapshot.child("name").getValue().toString();

                            }
                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                if (joined_events != "") {
                    builder.setTitle("Events Joined");
                    builder.setMessage(joined_events);
                }
                // Set click listener for alert dialog buttons
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                // User clicked the Yes button
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                // User clicked the No button
                                break;
                        }
                    }
                };
                if(joined_events!=""){
                    // Set the alert dialog yes button click listener
                    builder.setPositiveButton("Okay", dialogClickListener);
                    // Set the alert dialog no button click listener
                    builder.setNegativeButton("Cancel", dialogClickListener);
                    AlertDialog dialog = builder.create();
                    // Display the alert dialog on interface
                    dialog.show();
                    joined_events = "";
                }
            }



        });



        final String eml = user1.getEmail();
        d_email.setText(eml);

        mFirebaseDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    if (childSnapshot.child("email").getValue().equals(eml)) {
                        String parent = childSnapshot.getKey();
                         new_phone=snapshot.child(parent).child("phone").getValue().toString();
                        d_username.setText(snapshot.child(parent).child("fullname").getValue().toString());
                        d_mobile.setText(snapshot.child(parent).child("phone").getValue().toString());



                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        delacc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(UserProfile.this);
                dialog.setTitle("Are you sure?");
                dialog.setMessage("Deleting this account will completely erase all your activity on this app");
                dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        user1.delete()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            mFirebaseDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                                                        if (childSnapshot.child("email").getValue().equals(eml)) {
                                                            String parent = childSnapshot.getKey();
                                                            mFirebaseDatabase.child(parent).removeValue();

                                                        }
                                                    }


                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });


                                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                        }
                                        else
                                        {
                                            Toast.makeText(UserProfile.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });


                    }
                });
                dialog.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                });

                AlertDialog alertDialog = dialog.create();
                alertDialog.show();







            }
        });

    }

}

