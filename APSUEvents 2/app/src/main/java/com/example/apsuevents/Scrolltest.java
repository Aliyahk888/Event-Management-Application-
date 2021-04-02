package com.example.apsuevents;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;

public class Scrolltest extends AppCompatActivity {

    Button btpicker, next, back, button3;
    RadioGroup etypelist, eprivlist;
    RadioButton etype, epriv, epriv2;
    TextView tw;
    EditText name, cap, desc, pswd, cpswd;
    int PLACE_PICKER_REQUEST = 1;
    String etitle, ecap, edesc, pcheck;
    RadioButton eventBtn;
    RadioButton privacyBtn;
    DatePicker myDatePicker;
    TimePicker myTimePicker;


    private static final String TAG = MainActivity.class.getSimpleName();
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    private String userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolltest);

        btpicker = (Button) findViewById(R.id.bt_picker);
        back = (Button) findViewById(R.id.back);
        next = (Button) findViewById(R.id.btnSave);
        tw = (TextView) findViewById(R.id.eplaceseltext);
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


        etitle = name.getText().toString();
        ecap = cap.getText().toString();
        edesc = desc.getText().toString();
        pcheck = tw.toString();
        button3=(Button)findViewById(R.id.button3);



        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isEmpty(name) || isEmpty(cap) || isEmpty(desc) || isEmpty(tw)) {
                    Toast.makeText(getApplicationContext(),"Please Enter All fields",Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(getApplicationContext(),"Event Created",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getApplicationContext(), HomePage.class));
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








        mFirebaseInstance = FirebaseDatabase.getInstance();

        // get reference to 'users' node
        mFirebaseDatabase = mFirebaseInstance.getReference("users");



        // Save / update the user
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


                // Check for already existed userId
                if (TextUtils.isEmpty(userId)) {
                    createUser(ename, event, privacy, ecap, edesc, edate, etime);
                } else {
                    updateUser(ename, event, privacy, ecap, edesc, edate, etime);
                }
            }
        });



        toggleButton();


    }

    private void toggleButton() {
        if (TextUtils.isEmpty(userId)) {
            button3.setText("Submit");
        } else {
            button3.setText("Update");
        }
    }

    /**
     * Creating new user node under 'users'
     */
    private void createUser(String name, String event, String privacy, String capacity, String description, String date, String time) {

        if (TextUtils.isEmpty(userId)) {
            userId = mFirebaseDatabase.push().getKey();
        }

        User user = new User(name, event, privacy, capacity, description, date, time);

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


                toggleButton();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e(TAG, "Failed to read user", error.toException());
            }
        });
    }

    private void updateUser(String name, String event, String privacy, String capacity, String description, String date, String time) {
        // updating the user via child nodes
        if (!TextUtils.isEmpty(name))
            mFirebaseDatabase.child(userId).child("name").setValue(name);
            mFirebaseDatabase.child(userId).child("event").setValue(event);
            mFirebaseDatabase.child(userId).child("privacy").setValue(privacy);
            mFirebaseDatabase.child(userId).child("capacity").setValue(capacity);
            mFirebaseDatabase.child(userId).child("description").setValue(description);
            mFirebaseDatabase.child(userId).child("date").setValue(date);
            mFirebaseDatabase.child(userId).child("time").setValue(time);


    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PLACE_PICKER_REQUEST) {
            if(resultCode == RESULT_OK){
                Place place = PlacePicker.getPlace(data, this);
                tw.setText("!! Location has been selected !!");
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
            pswd.setVisibility(View.VISIBLE);
            cpswd.setVisibility(View.VISIBLE);
        }
    }

    public void open_enabled(View view) {
        if(epriv2.isChecked())
        {
            pswd.setVisibility(View.GONE);
            cpswd.setVisibility(View.GONE);
        }
    }
}