package com.example.usecure;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class SettingsActivity extends AppCompatActivity {

    Button manageBtn, pairDeviceBtn, passKeyBtn;
    Button notificationsBtn, appearBtn, contactUsBtn;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_settings );

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        manageBtn = (Button) findViewById( R.id.manageBtn );
        manageBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent doorControlIntent = new Intent( getApplicationContext(), ManageUsersActivity.class );
                startActivity( doorControlIntent );
            }
        } );

        pairDeviceBtn = (Button) findViewById( R.id.pairDeviceBtn );
        pairDeviceBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent doorControlIntent = new Intent( getApplicationContext(), PairDeviceActivity.class );
                startActivity( doorControlIntent );
            }
        } );

        passKeyBtn = (Button) findViewById( R.id.passKeyBtn );
        passKeyBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent doorControlIntent = new Intent( getApplicationContext(), PassKeyActivity.class );
                startActivity( doorControlIntent );
            }
        } );

        notificationsBtn = (Button) findViewById( R.id.notificationsBtn );
        notificationsBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent doorControlIntent = new Intent( getApplicationContext(), NotificationsActivity.class );
                startActivity( doorControlIntent );
            }
        } );

        appearBtn = (Button) findViewById( R.id.appearBtn );
        appearBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent doorControlIntent = new Intent( getApplicationContext(), AppearenceActivity.class );
                startActivity( doorControlIntent );
            }
        } );

        contactUsBtn = (Button) findViewById( R.id.contactUsBtn );
        contactUsBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent doorControlIntent = new Intent( getApplicationContext(), ContactUsActivity.class );
                startActivity( doorControlIntent );
            }
        } );
    }
}
