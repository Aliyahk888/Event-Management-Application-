package com.example.apsuevents;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.time.LocalDate;

public class Scrolltest extends AppCompatActivity {

    Button btpicker, next, back, button3;
    RadioGroup etypelist, eprivlist;
    RadioButton etype, epriv, epriv2;
    TextView tw, tid;
    EditText name, cap, desc, pswd, cpswd;
    int PLACE_PICKER_REQUEST = 1;
    String etitle, ecap, edesc, pcheck, ecur_cap, coordinates, ehost, contact_host;
    RadioButton eventBtn;
    RadioButton privacyBtn;
    DatePicker myDatePicker;
    TimePicker myTimePicker;
    int priv_flag=0;



    private static final String TAG = MainActivity.class.getSimpleName();
    private DatabaseReference mFirebaseDatabase;
    private DatabaseReference mFirebaseDatabase2;
    private FirebaseDatabase mFirebaseInstance;
    private String userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolltest);

        btpicker = (Button) findViewById(R.id.bt_picker);
        back = (Button) findViewById(R.id.back_tobrowse);
        next = (Button) findViewById(R.id.btnSave);
        tw = (TextView) findViewById(R.id.eplaceseltext);
        tid = (TextView) findViewById(R.id.eplaceidtext);
        etypelist = (RadioGroup) findViewById(R.id.TypeGroup);
        eprivlist =(RadioGroup) findViewById(R.id.privacy);
        name = (EditText)findViewById(R.id.etitle_ip);
        cap = (EditText)findViewById(R.id.ecap_ip);
        pswd = (EditText)findViewById(R.id.closed_psswd);
        cpswd = (EditText)findViewById(R.id.conf_psswd);
        desc = (EditText) findViewById(R.id.edesc_ip);
        epriv=(RadioButton)findViewById(R.id.closed);
        epriv2=(RadioButton)findViewById(R.id.open);
        myDatePicker = (DatePicker) findViewById(R.id.datePicker);
        myTimePicker = (TimePicker)findViewById(R.id.timePicker1);
        button3= (Button)findViewById(R.id.button3);




        etitle = name.getText().toString();
        ecap = cap.getText().toString();
        edesc = desc.getText().toString();
        pcheck = tw.toString();




        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String ename= name.getText().toString();
                String ecap= cap.getText().toString();
                String edesc= desc.getText().toString();
                int selected = etypelist.getCheckedRadioButtonId();
                eventBtn=(RadioButton)findViewById(selected);
                String event = eventBtn.getText().toString();
                int priv=eprivlist.getCheckedRadioButtonId();
                privacyBtn=(RadioButton)findViewById(priv);
                String privacy= privacyBtn.getText().toString();
                String placeID = tid.getText().toString();
                String edate = DateFormat.getDateInstance().format(myDatePicker.getCalendarView().getDate());
                Integer ehour = myTimePicker.getHour();
                Integer emin= myTimePicker.getMinute();
                String etime = ehour+":"+emin;
                String pswd_string= pswd.getText().toString().trim();
                String attendee = "";


                if(isEmpty(name) || isEmpty(cap) || isEmpty(desc)) {
                    Toast.makeText(getApplicationContext(),"Please Enter All fields",Toast.LENGTH_LONG).show();
                }
                else if(verifyPassword()&&priv_flag==0){
                    Toast.makeText(getApplicationContext(),"Passwords Do Not Match",Toast.LENGTH_LONG).show();
                }
                else if(TextUtils.isEmpty(userId)){
                    createUser(ename, event, privacy, ecap,ecur_cap, edesc, edate, etime, pswd_string, attendee);
                    Toast.makeText(getApplicationContext(),"Event Created !!!",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getApplicationContext(),HomePage.class));
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),HomePage.class));
            }
        });



        btpicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(Scrolltest.this),PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });





        final FirebaseUser user1 = FirebaseAuth.getInstance().getCurrentUser();
        final String hostemail = user1.getEmail();
        ehost = hostemail;

        mFirebaseInstance = FirebaseDatabase.getInstance();

        // get reference to 'users' node
        mFirebaseDatabase = mFirebaseInstance.getReference("users");
        mFirebaseDatabase2 = mFirebaseInstance.getReference("details");


        mFirebaseDatabase2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    if (childSnapshot.child("email").getValue().equals(ehost)) {
                        String parent = childSnapshot.getKey();
                        contact_host=snapshot.child(parent).child("phone").getValue().toString();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        /*
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ename= name.getText().toString();
                String ecap= cap.getText().toString();
                String edesc= desc.getText().toString();
                int selected = etypelist.getCheckedRadioButtonId();
                eventBtn=(RadioButton)findViewById(selected);
                String event = eventBtn.getText().toString();
                int priv=eprivlist.getCheckedRadioButtonId();
                privacyBtn=(RadioButton)findViewById(priv);
                String privacy= privacyBtn.getText().toString();
                String edate = DateFormat.getDateInstance().format(myDatePicker.getCalendarView().getDate());
                Integer ehour = myTimePicker.getHour();
                Integer emin= myTimePicker.getMinute();
                String etime = ehour+":"+emin;
                String pswd_string= pswd.getText().toString().trim();
                String attendee = "";


                // Check for already existed userId

            }
        });

         */

    }
    /**
     * Creating new user node under 'users'
     */
    private void createUser(String name, String event, String privacy, String capacity, String cur_capacity, String description, String date, String time, String pswd, String attendee) {

        cur_capacity = "0";
        if (TextUtils.isEmpty(userId)) {
            userId = mFirebaseDatabase.push().getKey();
        }

        User user = new User(name, event, privacy, capacity, cur_capacity, description, date, time, pswd, coordinates, attendee, ehost, contact_host);

        mFirebaseDatabase.child(userId).setValue(user);

        addUserChangeListener();
    }

    /**
     * User data change listener
     */
    private void addUserChangeListener() {
        // User data change listener
        mFirebaseDatabase.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                // Check for null
                if (user == null) {
                    Log.e(TAG, "User data is null!");
                    return;
                }

                Log.e(TAG, "User data is changed!" + user.name );


                // clear edit text
                name.setText("");

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e(TAG, "Failed to read user", error.toException());
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PLACE_PICKER_REQUEST) {
            if(resultCode == RESULT_OK){
                Place place = PlacePicker.getPlace(data, this);
                tw.setText("!! Location has been selected !!");
                tid.setText(place.getId());
                coordinates = place.getLatLng().toString();
            }
        }
    }

    private boolean isEmpty(EditText et){
        return et.getText().toString().trim().length() == 0;
    }
    private boolean isEmpty(TextView tw){
        return tw.getText().toString().trim().length() == 0;
    }

    public void closed_enabled(View view) {
        if(epriv.isChecked())
        {
            priv_flag=0;
            pswd.setVisibility(View.VISIBLE);
            cpswd.setVisibility(View.VISIBLE);
        }
    }

    public void open_enabled(View view) {
        if(epriv2.isChecked())
        {
            priv_flag=1;
            pswd.setVisibility(View.GONE);
            cpswd.setVisibility(View.GONE);
        }
    }

    public boolean verifyPassword(){
        pswd = (EditText)findViewById(R.id.closed_psswd);
        String pswd_string= pswd.getText().toString().trim();
        cpswd = (EditText)findViewById(R.id.conf_psswd);
        String cpswd_string= cpswd.getText().toString().trim();
        if(cpswd_string!=pswd_string)
            return false;
        else return true;

    }




}