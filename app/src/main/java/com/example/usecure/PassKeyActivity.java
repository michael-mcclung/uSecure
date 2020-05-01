package com.example.usecure;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PassKeyActivity extends AppCompatActivity {

    // variables
    private Button updateBtn, homeBtn, savePassCodeBtn, reEnterCode;
    private EditText userPassCode, updatedCodeText;
    private DatabaseReference passCodeRef;

    @Override // create voice command page
    protected void onCreate(Bundle savedInstanceStatus) {
        super.onCreate( savedInstanceStatus );
        setContentView( R.layout.activity_voice_command );

        // initiate variables
        passCodeRef = FirebaseDatabase.getInstance().getReference( "Main User" );
        updateBtn = (Button) findViewById( R.id.updateBtn );
        reEnterCode = (Button) findViewById(R.id.registerBtn);
        savePassCodeBtn = (Button) findViewById( R.id.savePassCodeBtn );
        homeBtn = (Button) findViewById( R.id.settingsBtn2 );
        userPassCode = (EditText) findViewById(R.id.userPassCode);
        updatedCodeText = (EditText) findViewById(R.id.updatedCodeText);


        // start recording speech to text
        savePassCodeBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // variables
                String code = userPassCode.getText().toString();
                String passCodeUiD = FirebaseAuth.getInstance().getCurrentUser().getUid();

                // save pass code and update firebase
                passCodeRef.child(passCodeUiD).child("Enter Passcode").child("Passcode").setValue(code);
                Toast.makeText(PassKeyActivity.this, "Pass code added!", Toast.LENGTH_SHORT).show();

            }
        } );

        // delete recording
        updateBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // get information
                String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                String updatedCode = updatedCodeText.getText().toString();

                // remove value form firebase databse and let user know audio deleted
                DatabaseReference updatePassRef = FirebaseDatabase.getInstance().getReference().child("Main User").child( userUid ).child( "Enter Passcode" );
                updatePassRef.child("Passcode").setValue(updatedCodeText);
                Toast.makeText( PassKeyActivity.this, "Passcode updated", Toast.LENGTH_LONG ).show();

            }
        } );

        // go to home page
        homeBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent homeIntent = new Intent( getApplicationContext(), Home.class );
                startActivity( homeIntent );
            }
        } );
    }
}
