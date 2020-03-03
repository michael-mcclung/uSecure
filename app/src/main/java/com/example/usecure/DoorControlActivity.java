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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class DoorControlActivity extends AppCompatActivity {

    ToggleButton doorControlToggle;
    Spinner spinnerDropDown;

    Button submitBtn, addNewDoorBtn;
    EditText nameOfNewDoorText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_door_control );

        doorControlToggle = (ToggleButton) findViewById( R.id.doorControlToggle );
        spinnerDropDown = (Spinner) findViewById( R.id.spinnerDropDown );

        submitBtn = (Button) findViewById( R.id.submitBtn );
        addNewDoorBtn = (Button) findViewById( R.id.addNewDoorBtn );
        nameOfNewDoorText = (EditText) findViewById( R.id.nameOfNewDoorText );

        addNewDoorBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String getFileName = nameOfNewDoorText.getText().toString();
                nameOfNewDoorText.setText( getFileName );

                DatabaseReference dataBaseRef = FirebaseDatabase.getInstance().getReference();
                dataBaseRef = dataBaseRef.child( "doorControl" ).child( nameOfNewDoorText.getText().toString() + ".door" );
            }
        } );

        ArrayList<String> arrayList = new ArrayList<>( );
        if (arrayList.isEmpty()) {
            arrayList.add( nameOfNewDoorText.getText().toString() );
        } else {
            arrayList.add( nameOfNewDoorText.getText().toString() );
        }
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
