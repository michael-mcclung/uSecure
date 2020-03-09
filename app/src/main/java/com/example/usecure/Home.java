package com.example.usecure;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class Home extends AppCompatActivity {

    Button voiceCommandBtn, logoutBtn, securityCamFeedBtn, settingsBtn;
    Button doorControlBtn, emergencyBtn;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_home );

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        emergencyBtn = (Button) findViewById( R.id.emergencyBtn );
        emergencyBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent( Intent.ACTION_DIAL );
                callIntent.setData( Uri.parse( "tel:9512623062" ) );
                startActivity( callIntent );
            }
        } );

        doorControlBtn = (Button) findViewById( R.id.doorControlBtn );
        doorControlBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent doorControlIntent = new Intent( getApplicationContext(), DoorControlActivity.class );
                startActivity( doorControlIntent );
            }
        } );

        settingsBtn = (Button) findViewById( R.id.settingsBtn );
        settingsBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent settingsIntent = new Intent( getApplicationContext(), SettingsActivity.class );
                startActivity( settingsIntent );
            }
        } );

        voiceCommandBtn = (Button) findViewById( R.id.voiceCommandBtn );
        voiceCommandBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent voiceIntent = new Intent( getApplicationContext(), VoiceCommandActivity.class );
                startActivity( voiceIntent );
            }
        } );

        securityCamFeedBtn = (Button) findViewById( R.id.securityCamFeedBtn );
        securityCamFeedBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraFeedIntent = new Intent( getApplicationContext(), SecurityCameraFeedActivity.class );
                startActivity( cameraFeedIntent );
            }
        } );

        logoutBtn = (Button) findViewById( R.id.logoutBtn );
        logoutBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        } );
    }

    public void logout() {
        mAuth.getInstance().signOut();
        Intent intent = new Intent(Home.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        System.out.println( "User Logged Out" );
        //Intent logoutIntent = new Intent( getApplicationContext(), MainActivity.class );
        //startActivity( logoutIntent );
        finish();
    }

}
