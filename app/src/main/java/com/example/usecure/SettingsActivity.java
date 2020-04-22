// packages
package com.example.usecure;

// imports
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.google.firebase.auth.FirebaseAuth;

// settings activity
public class SettingsActivity extends AppCompatActivity {

    // variables
    Button manageBtn, pairDeviceBtn, passKeyBtn;
    Button notificationsBtn, homeBtn, contactUsBtn;
    FirebaseAuth mAuth;

    @Override // creates settings page
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_settings );

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        manageBtn = (Button) findViewById( R.id.manageBtn );
        pairDeviceBtn = (Button) findViewById( R.id.pairDeviceBtn );
        passKeyBtn = (Button) findViewById( R.id.passKeyBtn );
        notificationsBtn = (Button) findViewById( R.id.notificationsBtn );
        contactUsBtn = (Button) findViewById( R.id.contactUsBtn );
        homeBtn = (Button) findViewById( R.id.homeBtn );

        // go to manage user page
        manageBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent doorControlIntent = new Intent( getApplicationContext(), ManageUsersActivity.class );
                startActivity( doorControlIntent );
            }
        } );

        // go to pair device page
        pairDeviceBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent doorControlIntent = new Intent( getApplicationContext(), PairDeviceActivity.class );
                startActivity( doorControlIntent );
            }
        } );

        // go to pass key page
        passKeyBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent doorControlIntent = new Intent( getApplicationContext(), PassKeyActivity.class );
                startActivity( doorControlIntent );
            }
        } );

        // go to notifications page
        notificationsBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent doorControlIntent = new Intent( getApplicationContext(), NotificationsActivity.class );
                startActivity( doorControlIntent );
            }
        } );

        // go to contact us page
        contactUsBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent doorControlIntent = new Intent( getApplicationContext(), ContactUsActivity.class );
                startActivity( doorControlIntent );
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
