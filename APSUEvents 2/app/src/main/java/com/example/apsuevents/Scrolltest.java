package com.example.apsuevents;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

public class Scrolltest extends AppCompatActivity {

    Button btpicker, next, back;
    RadioGroup etypelist, eprivlist;
    RadioButton etype, epriv;
    TextView tw;
    EditText name, cap, desc;
    int PLACE_PICKER_REQUEST = 1;
    String etitle, ecap, edesc, pcheck;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolltest);

        btpicker = findViewById(R.id.bt_picker);
        back = findViewById(R.id.back);
        next = findViewById(R.id.btnSave);
        tw = findViewById(R.id.eplaceseltext);
        etypelist = findViewById(R.id.TypeGroup);
        eprivlist = findViewById(R.id.privacy);
        name = findViewById(R.id.etitle_ip);
        cap = findViewById(R.id.ecap_ip);
        desc = findViewById(R.id.edesc_ip);

        etitle = name.getText().toString();
        ecap = cap.getText().toString();
        edesc = desc.getText().toString();
        pcheck = tw.toString();

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

}