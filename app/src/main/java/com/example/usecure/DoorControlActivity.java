/*packages*/
package com.example.usecure;

/*imports*/
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
import android.widget.Toast;
import android.widget.ToggleButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;

/*door control activity control code*/
public class DoorControlActivity extends AppCompatActivity {

    // variables
    private ToggleButton doorControlToggle;
    private Button addNewDoorBtn, homeBtn;
    private Spinner doorSpinner;
    private EditText nameOfNewDoorText;
    private DatabaseReference mDatabase;
    private ArrayAdapter spinnerArrayAdapter;
    private ArrayList<String> doorOptions = new ArrayList<String>();

    @Override // when starting the page do these
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_door_control );

        // initialize all variables into view by ids
        doorSpinner = (Spinner) findViewById( R.id.doorSpinner );
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

        // home button function
        homeBtn.setOnClickListener( new View.OnClickListener() {
            @Override // go to home page once button is pressed
            public void onClick(View v) {
                // creat intent to go to home page and then initiate it
                Intent homeIntent = new Intent( getApplicationContext(), Home.class );
                startActivity( homeIntent );
            }
        } );
        // button to add new door to Firebase database & spinner drop down menu
        addNewDoorBtn.setOnClickListener( new View.OnClickListener() {
            @Override // add a new door name and switch state
            public void onClick(View v) {

                // variables
                String userID = mDatabase.push().getKey();
                String getDoorName = nameOfNewDoorText.getText().toString();
                int switchState = 0;
                String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                // add door name given by user, update ArrayListOfDoors and firebase database
                doorOptions.add(getDoorName);
                ArrayListOfDoors addToList = new ArrayListOfDoors( userID, switchState );
                mDatabase.child( userUid ).child( "Door Control" ).child( getDoorName ).setValue( addToList );

                // let user know doors were added
                Toast.makeText( DoorControlActivity.this, "Door Added", Toast.LENGTH_SHORT ).show();
            }
        } );

        // spinner / drop down menu for door control
        doorSpinner.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {

            @Override // show doors availabe and let user know which door is currently selected
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                // showing all doors available and selecting correct door selected by user
                String spinnerText = parent.getItemAtPosition( position ).toString();
                Toast.makeText( parent.getContext(), spinnerText, Toast.LENGTH_LONG ).show();
                String result = (String) parent.getItemAtPosition( position );

                // show user selected door
                Toast.makeText(getApplicationContext(), "Selected: " + result, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
;
            }
        } );

        /*not done completely *****************/
        doorControlToggle.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener() {

            // variables to use to get correct item from firebase database
            int switchStateOn = 1, getSwitchStateOff = 0;
            String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

            @Override // when toggle is selected either update firebase database to 1 or 0
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                // variables
                String spinnerSelected = doorSpinner.getSelectedItem().toString();

                if (isChecked)
                {
                    // used to turn switch state of doors on
                    mDatabase.child( userUid ).child( "Door Control" ).child( spinnerSelected ).setValue( "0" );
                }
                else
                {
                    // used to turn switch state of doors off
                    mDatabase.child( userUid ).child( "Door Control" ).child( spinnerSelected ).setValue( "1" );
                }
            }
        } );
    }
}
