package com.example.usecure;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.os.TokenWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class DoorControlActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_door_control );

        final ToggleButton doorControlToggle;
        final Spinner spinnerDropDown;
        Button submitBtn;

        doorControlToggle = (ToggleButton) findViewById( R.id.doorControlToggle );
        spinnerDropDown = (Spinner) findViewById( R.id.spinnerDropDown );
        submitBtn = (Button) findViewById( R.id.submitBtn );

        ArrayList<String> arrayList = new ArrayList<>( );
        arrayList.add("Door 1");
        arrayList.add("Door 2");
        arrayList.add("Door 3");
        arrayList.add("Door 4");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDropDown.setAdapter(arrayAdapter);
        spinnerDropDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String displayName = parent.getItemAtPosition(position).toString();
                Toast.makeText(parent.getContext(), "Selected: " + displayName, Toast.LENGTH_LONG).show();
            }
            @Override
            public void onNothingSelected(AdapterView <?> parent) {
            }
        });

        submitBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseReference mDatabase;
                int on = 1, off = 0;
                String sOn = "ON", sOff = "OFF";

                if (spinnerDropDown.isSelected()){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        if (doorControlToggle.isVisibleToUserForAutofill( on )){
                            mDatabase = FirebaseDatabase.getInstance().getReference().child( "doorControl" );
                            mDatabase.push().setValue( 1 );
                        }
                    }
                }
            }
    });
}}
