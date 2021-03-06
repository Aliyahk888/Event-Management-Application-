package com.example.apsuevents;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class EventPage extends AppCompatActivity {
    String new_attendee, new_phone;
    private DatabaseReference mFirebaseDatabase;
    private DatabaseReference mFirebaseDatabase2;
    private DatabaseReference mFirebaseDatabase3;
    private static final String TAG = MainActivity.class.getSimpleName();
    private FirebaseDatabase mFirebaseInstance;
    private FirebaseAuth mAuth;
    TextView EventTitle;
    TextView EventCurCapacity;
    TextView EventCapacity;
    TextView EventDate;
    TextView EventTime;
    TextView EventDesc;
    Button emap;
    EditText pssd;
    Button back, join, call;
    String title, host_ph;
    LinearLayout cc;
    int exists = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_page);
        final Bundle b = getIntent().getExtras();
        title= b.getString("Title");
        final String curcap= b.getString("CurCap");
        final String capacity= b.getString("Capacity");

        String date= b.getString("Date");
        String time= b.getString("Time");
        String desc= b.getString("Desc");
        String priv= b.getString("Priv");
        final String place= b.getString("Place");
        final String pswd= b.getString("Pswd");
        final String ph_host= b.getString("Call");

        host_ph = ph_host;

        cc=(LinearLayout)findViewById(R.id.closed_check);
        pssd=(EditText)findViewById(R.id.pswdinput);
        emap=(Button) findViewById(R.id.loc_button);
        call=(Button) findViewById(R.id.call_button);

        mFirebaseInstance = FirebaseDatabase.getInstance();

        mFirebaseDatabase2 = mFirebaseInstance.getReference("details");
        final FirebaseUser user1 = FirebaseAuth.getInstance().getCurrentUser();
        final String userEmail = user1.getEmail();

        mFirebaseDatabase2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    if (childSnapshot.child("email").getValue().equals(userEmail)) {
                        String parent = childSnapshot.getKey();
                        new_attendee=snapshot.child(parent).child("email").getValue().toString();
                        new_phone=snapshot.child(parent).child("phone").getValue().toString();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


       // mFirebaseDatabase3 = mFirebaseInstance.getReference("users");






        if(priv.equals("Closed")) {
            cc.setVisibility(View.VISIBLE);
        }

        final String lat = place.substring(place.indexOf('(')+1,place.indexOf(')'));

        emap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri mapUri = Uri.parse("geo:" + lat + "?q=" + lat);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, mapUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });





        // get reference to 'users' node
        mFirebaseDatabase = mFirebaseInstance.getReference("users");

        back=(Button)findViewById(R.id.back_tobrowse);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), BrowseEvent.class));
            }
        });

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + host_ph));
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });


        Query query = mFirebaseDatabase.orderByChild("name").equalTo(title);
       //final String list = mFirebaseDatabase.child("attendee").toString();
        //final String attendance = new_attendee + "*" + att;






        join=(Button)findViewById(R.id.delbutton);
        join.setOnClickListener(new View.OnClickListener() {
            int val=Integer.parseInt(curcap)+1;
            String newval= Integer.toString(val);
            int cap_int=Integer.parseInt(capacity);

            @Override
            public void onClick(View v) {
                final String epswd = pssd.getText().toString();
                mFirebaseDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                         if(childSnapshot.child("name").getValue().equals(title)) {
                             String parent = childSnapshot.getKey();


                                if (childSnapshot.child("Attendance").child(new_phone).exists()){   Toast.makeText(getApplicationContext(), "You are already a part of this Event !!", Toast.LENGTH_LONG).show();
                                    }
                                else if (val > cap_int) {
                                     Toast.makeText(getApplicationContext(), "Event full, sorry!", Toast.LENGTH_LONG).show();
                                 }
                                 else if(epswd.equals(pswd)){
                                 mFirebaseDatabase.child(parent).child("cur_cap").setValue(newval);
                                 mFirebaseDatabase.child(parent).child("Attendance").child(new_phone).setValue(new_attendee);
                                 //final String updated_list=list+","+uid;
                                 //mFirebaseDatabase.child(parent).child("attendee").setValue(updated_list);
                                 Toast.makeText(getApplicationContext(),"Event Successfully Joined",Toast.LENGTH_LONG).show();
                                 //Toast.makeText(getApplicationContext(),"Event Successfully Joined!",Toast.LENGTH_LONG).show();
                                 startActivity(new Intent(getApplicationContext(), BrowseEvent.class));

                                }
                                else {
                                    Toast.makeText(getApplicationContext(), "Invalid Password !!", Toast.LENGTH_LONG).show();
                                     //startActivity(new Intent(getApplicationContext(), BrowseEvent.class));
                                }
                             }
                         }
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });




            }
        });

        /*
        String hours = time.substring(0,time.indexOf(':'));
        String mins = time.substring(time.indexOf(':'));
        int timeflag = 0;
        int hrs=Integer.parseInt(hours);
        if(hrs>=0 && hrs <=12) {
            String ind = " am";
            if(hrs == 0){
                hrs=12;
            }
            else if(hrs == 12){
                ind = " pm";
            }
            time = hrs + ":" + mins + ind;
        }
        else{
            hrs=hrs-12;
            time = hrs + ":" + mins + " pm";
        }
        
         */


        EventTitle= (TextView)findViewById(R.id.EventTitle);
        EventTitle.setText(title);
        EventDesc= (TextView)findViewById(R.id.EventDesc);
        EventDesc.setText(desc);
        EventTime= (TextView)findViewById(R.id.EventTime);
        EventTime.setText("Time:  "+time);
        EventDate= (TextView)findViewById(R.id.EventDate);
        EventDate.setText(date);

        EventCurCapacity= (TextView)findViewById(R.id.EventCurCapacity);
        EventCapacity= (TextView)findViewById(R.id.EventCapacity);
        EventCapacity.setText(capacity);
        EventCurCapacity.setText(curcap);



    }

}

