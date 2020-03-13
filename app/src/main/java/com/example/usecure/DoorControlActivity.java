package com.example.usecure;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DoorControlActivity extends AppCompatActivity {

    // toggle button, buttons, spinners, and EditText field variables
    private ToggleButton doorControlToggle;
    private Button addNewDoorBtn;
    private Spinner spinnerDropDown;
    private EditText nameOfNewDoorText;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_door_control );

        doorControlToggle = (ToggleButton) findViewById( R.id.doorControlToggle );
        spinnerDropDown = (Spinner) findViewById( R.id.spinnerDropDown );

        addNewDoorBtn = (Button) findViewById( R.id.addNewDoorBtn );
        nameOfNewDoorText = (EditText) findViewById( R.id.nameOfNewDoorText );

        mDatabase = FirebaseDatabase.getInstance().getReference( "Main User");

        addNewDoorBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String id = mDatabase.push().getKey();
                String getDoorName = nameOfNewDoorText.getText().toString();
                int switchState = 0;
                String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                ArrayListOfDoors addToList = new ArrayListOfDoors( id, getDoorName, switchState );
                mDatabase.child( userUid ).child( "Door Control" ).child( getDoorName ).setValue( addToList );

               // addNewSpinnerItem();
            }
        } );

        spinnerDropDown.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String displayName = parent.getItemAtPosition( position ).toString();
                Toast.makeText( parent.getContext(), "Selected: " + displayName, Toast.LENGTH_LONG ).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        } );

        doorControlToggle.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                //mDatabase.child( userUid ).child( "Door Control" ).getRef();
            }
        } );
    }

  /*  protected void addNewSpinnerItem(){

        int switchState = 0;
        ArrayList listOfDoors = new ArrayList(  );
        listOfDoors.add( switchState );

        Spinner dynamicSpinner = new Spinner(this);
        dynamicSpinner.setAdapter( (SpinnerAdapter) listOfDoors );

    }*/
}
