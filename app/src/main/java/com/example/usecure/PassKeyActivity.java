package com.example.usecure;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PassKeyActivity extends AppCompatActivity {

    // variables
    EditText userPassCode, updatedCodeText;
    DatabaseReference passCodeRef;
    Button updateBtn, reEnterCode, savePassCodeBtn, homeBtn, settingsBackBtn;

    @Override // create voice command page
    protected void onCreate(Bundle savedInstanceStatus) {
        super.onCreate( savedInstanceStatus );
        setContentView( R.layout.activity_pass_key );

        // initiate variables
        passCodeRef = FirebaseDatabase.getInstance().getReference( "Main User" );

        // variables
        settingsBackBtn = (Button) findViewById( R.id.settingsBackBtn );
        updateBtn = (Button) findViewById( R.id.updateBtn );
        reEnterCode = (Button) findViewById( R.id.registerBtn );
        savePassCodeBtn = (Button) findViewById( R.id.savePassCodeBtn );
        homeBtn = (Button) findViewById( R.id.settingsBtn2 );
        userPassCode = (EditText) findViewById(R.id.userPassCode);
        updatedCodeText = (EditText) findViewById(R.id.updatedCodeText);


        // start recording speech to text
        savePassCodeBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // variables
                String passCodeUiD = FirebaseAuth.getInstance().getCurrentUser().getUid();
                String code = userPassCode.getText().toString();

                // add passcode to firebase database and let user know passcode was added
                passCodeRef.child( passCodeUiD ).child( "Enter Passcode" ).child( "Passcode" ).setValue( code );
                Toast.makeText( PassKeyActivity.this, "Pass code added!", Toast.LENGTH_LONG ).show();
            }
        } );

        // delete recording
        updateBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // variables
                String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                String updatedCodeTxt = updatedCodeText.getText().toString();

                // remove value form firebase databse and let user know audio deleted
                passCodeRef.child( userUid ).child( "Enter Passcode" ).child( "Passcode" ).setValue( updatedCodeTxt );
                Toast.makeText( PassKeyActivity.this, "Passcode updated", Toast.LENGTH_LONG ).show();

            }
        } );

        // go to home page
        settingsBackBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent settingsIntent = new Intent( getApplicationContext(), SettingsActivity.class );
                startActivity( settingsIntent );
            }
        } );
    }
}
