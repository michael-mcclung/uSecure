package com.example.usecure;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DoorControlActivity extends AppCompatActivity {

    // variables
    private ToggleButton doorControlToggle;
    private Button addNewDoorBtn, homeBtn;
    private Spinner doorSpinner;
    private EditText nameOfNewDoorText;
    private TextView outputSpinnerTv;
    private DatabaseReference mDatabase;
    private ArrayAdapter spinnerArrayAdapter;
    private ArrayList<String> doorOptions = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_door_control );

        // initialize all variables into view by ids
        doorSpinner = (Spinner) findViewById( R.id.doorSpinner );
        outputSpinnerTv = (TextView) findViewById( R.id.outputSpinnerTv );
        addNewDoorBtn = (Button) findViewById( R.id.addNewDoorBtn );
        nameOfNewDoorText = (EditText) findViewById( R.id.nameOfNewDoorText );
        doorControlToggle = (ToggleButton) findViewById( R.id.doorControlToggle );
        homeBtn = (Button) findViewById( R.id.homeBtn );

        // initialize reference for Firebase database
        mDatabase = FirebaseDatabase.getInstance().getReference( "Main User");

        // creating the array adapter instance having the list of options
        spinnerArrayAdapter = new ArrayAdapter( this, android.R.layout.simple_spinner_item, doorOptions );
        spinnerArrayAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );

        // setting the array adapter data on the Spinner
        doorSpinner.setAdapter( spinnerArrayAdapter );

        homeBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent homeIntent = new Intent( getApplicationContext(), Home.class );
                startActivity( homeIntent );
            }
        } );
        // button to add new door to Firebase database & spinner drop down menu
        addNewDoorBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userID = mDatabase.push().getKey();
                String getDoorName = nameOfNewDoorText.getText().toString();
                int switchState = 0;
                String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                doorOptions.add(getDoorName);
                ArrayListOfDoors addToList = new ArrayListOfDoors( userID, switchState );
                mDatabase.child( userUid ).child( "Door Control" ).child( getDoorName ).setValue( addToList );
                Toast.makeText( DoorControlActivity.this, "Door Added", Toast.LENGTH_SHORT ).show();


            }
        } );

        doorSpinner.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String spinnerText = parent.getItemAtPosition( position ).toString();
                Toast.makeText( parent.getContext(), spinnerText, Toast.LENGTH_LONG ).show();

                String result = (String) parent.getItemAtPosition( position );
                Toast.makeText(getApplicationContext(), "Selected : " + result, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
;
            }
        } );

        doorControlToggle.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener() {

            // variables to use to get correct item from firebase database
            int switchStateOn = 1, getSwitchStateOff = 0;
            //String id = mDatabase.push().getKey();
            String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                String spinnerSelected = doorSpinner.getSelectedItem().toString();
                if (isChecked)
                {
                    // used to turn switch state of doors on
                    //ArrayListOfDoors addToListOn = new ArrayListOfDoors( switchStateOn );
                    mDatabase.child( userUid ).child( "Door Control" ).child( spinnerSelected ).setValue( "0" );
                }
                else
                {
                    // used to turn switch state of doors off
                    //ArrayListOfDoors addToListOff = new ArrayListOfDoors( getSwitchStateOff );
                    mDatabase.child( userUid ).child( "Door Control" ).child( spinnerSelected ).setValue( "1" );
                }
            }
        } );
    }
}
