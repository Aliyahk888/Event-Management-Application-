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
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
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
    Button back, join;
    String title;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_page);
        Bundle b = getIntent().getExtras();
        title= b.getString("Title");
        final String curcap= b.getString("CurCap");
        String capacity= b.getString("Capacity");
        String date= b.getString("Date");
        String time= b.getString("Time");
        String desc= b.getString("Desc");


        mFirebaseInstance = FirebaseDatabase.getInstance();

// get reference to 'users' node
        mFirebaseDatabase = mFirebaseInstance.getReference("users");

        back=(Button)findViewById(R.id.back_tobrowse);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), BrowseEvent.class));
            }
        });

       Query query = mFirebaseDatabase.orderByChild("name").equalTo(title);


        join=(Button)findViewById(R.id.joinbutton);
        join.setOnClickListener(new View.OnClickListener() {


            int val=Integer.parseInt(curcap)+1;
            String newval= Integer.toString(val);
            @Override
            public void onClick(View v) {
                mFirebaseDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                         if(childSnapshot.child("name").getValue().equals(title)) {
                             String parent = childSnapshot.getKey();

                             mFirebaseDatabase.child(parent).child("cur_cap").setValue(newval);
                         }
                        }
                        }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });





                Toast.makeText(getApplicationContext(),"(: Event Successfully Joined :)",Toast.LENGTH_LONG).show();
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

//                        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
//                            String parent = childSnapshot.getKey();