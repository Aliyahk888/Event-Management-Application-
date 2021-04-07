package com.example.apsuevents;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EventPage extends AppCompatActivity {
    String uid;
    private DatabaseReference mFirebaseDatabase;
    private static final String TAG = MainActivity.class.getSimpleName();
    private FirebaseDatabase mFirebaseInstance;
    private FirebaseAuth mAuth;
    TextView EventTitle;
    TextView EventCurCapacity;
    TextView EventCapacity;
    TextView EventDate;
    TextView EventTime;
    TextView EventDesc;
    Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_page);
        Bundle b = getIntent().getExtras();
        String title= b.getString("Title");
        String curcap= b.getString("CurCap");
        String capacity= b.getString("Capacity");
        String date= b.getString("Date");
        String time= b.getString("Time");
        String desc= b.getString("Desc");

        back=(Button)findViewById(R.id.back_tobrowse);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), BrowseEvent.class));
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