package com.example.usecure;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class Home extends AppCompatActivity {

    Button voiceCommandBtn, logoutBtn, securityCamFeedBtn;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_home );

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

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
                Intent cameraFeedIntent = new Intent(getApplicationContext(), SecurityCameraFeedActivity.class);
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
        System.out.println( "User Logged Out" );
        Intent logoutIntent = new Intent (getApplicationContext(), MainActivity.class);
        startActivity( logoutIntent );
    }

}
