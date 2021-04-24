package com.example.apsuevents;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class my_event_page extends AppCompatActivity {
    private DatabaseReference mFirebaseDatabase;
    private static final String TAG = MainActivity.class.getSimpleName();
    private FirebaseDatabase mFirebaseInstance;
    TextView EventTitle;
    TextView EventCurCapacity;
    TextView EventCapacity;
    TextView EventDate;
    TextView EventTime;
    TextView EventDesc;
    Button back, del;
    Button members;
    String names="";
    String merge;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_event_page);

        mFirebaseInstance = FirebaseDatabase.getInstance();
        mFirebaseDatabase = mFirebaseInstance.getReference("users");

        back=(Button)findViewById(R.id.back_tobrowse);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), my_events.class));
            }
        });


        final Bundle b = getIntent().getExtras();
        final String title= b.getString("Title");
        final String curcap= b.getString("CurCap");
        final String capacity= b.getString("Capacity");
        String date= b.getString("Date");
        String time= b.getString("Time");
        String desc= b.getString("Desc");
        String priv= b.getString("Priv");
        final String place= b.getString("Place");
        final String pswd= b.getString("Pswd");
        final String ph_host= b.getString("Call");


        members=(Button)findViewById(R.id.members);
        members.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(my_event_page.this);
                mFirebaseDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                            if (childSnapshot.child("name").getValue().equals(title)) {
                                String parent = childSnapshot.getKey();
                                mFirebaseDatabase.child(parent).child("Attendance").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                                            String id = childSnapshot.getKey();
                                            names = names + "\n" + snapshot.child(id).getValue().toString();
                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        }
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                if (names != "") {
                    builder.setTitle("Participants");
                    builder.setMessage(names);
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
                if(names!=""){
                // Set the alert dialog yes button click listener
                builder.setPositiveButton("Okay", dialogClickListener);
                // Set the alert dialog no button click listener
                builder.setNegativeButton("Cancel", dialogClickListener);
                AlertDialog dialog = builder.create();
                // Display the alert dialog on interface
                dialog.show();
                names = "";
            }
            }
        });




        del=(Button)findViewById(R.id.delbutton);
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(my_event_page.this);
                dialog.setMessage("Are you sure you want to delete this event?");
                dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mFirebaseDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                                    if (childSnapshot.child("name").getValue().equals(title)) {
                                        String parent = childSnapshot.getKey();
                                        mFirebaseDatabase.child(parent).removeValue();
                                        Toast.makeText(getApplicationContext(), title + " has been deleted.", Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(my_event_page.this, my_events.class));
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                });

                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alertDialog = dialog.create();
                alertDialog.show();

            }
        });



        EventTitle= (TextView)findViewById(R.id.EventTitle);
        EventTitle.setText(title);
        EventDesc= (TextView)findViewById(R.id.EventDesc);
        EventDesc.setText(desc);
        EventTime= (TextView)findViewById(R.id.EventTime);
        EventTime.setText(time);
        EventDate= (TextView)findViewById(R.id.EventDate);
        EventDate.setText(date);

        EventCurCapacity= (TextView)findViewById(R.id.EventCurCapacity);
        EventCapacity= (TextView)findViewById(R.id.EventCapacity);
        EventCapacity.setText(capacity);
        EventCurCapacity.setText(curcap);



    }
}