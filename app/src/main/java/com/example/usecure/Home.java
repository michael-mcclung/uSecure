// packages
package com.example.usecure;

// imports
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.google.firebase.auth.FirebaseAuth;

// home activity
public class Home extends AppCompatActivity {

    // variables
    Button voiceCommandBtn, logoutBtn, securityCamFeedBtn, settingsBtn;
    Button doorControlBtn, emergencyBtn;
    FirebaseAuth mAuth;

    @Override // starts home page activities
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_home );

        // Initialize variables
        mAuth = FirebaseAuth.getInstance();
        emergencyBtn = (Button) findViewById( R.id.emergencyBtn );
        doorControlBtn = (Button) findViewById( R.id.doorControlBtn );
        settingsBtn = (Button) findViewById( R.id.settingsBtn2 );
        voiceCommandBtn = (Button) findViewById( R.id.voiceCommandBtn );
        securityCamFeedBtn = (Button) findViewById( R.id.securityCamFeedBtn );
        logoutBtn = (Button) findViewById( R.id.logoutBtn );

        // emergency button
        emergencyBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // set intent to call police with a click of a button
                Intent callIntent = new Intent( Intent.ACTION_DIAL );
                callIntent.setData( Uri.parse( "tel:911" ) );
                startActivity( callIntent );
            }
        } );

        // door control button / go to door control page
        doorControlBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent doorControlIntent = new Intent( getApplicationContext(), DoorControlActivity.class );
                startActivity( doorControlIntent );
            }
        } );

        // settings button / go to settings page
        settingsBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent settingsIntent = new Intent( getApplicationContext(), SettingsActivity.class );
                startActivity( settingsIntent );
            }
        } );

        // voice control button / go to voice control page
        voiceCommandBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent voiceIntent = new Intent( getApplicationContext(), VoiceCommandActivity.class );
                startActivity( voiceIntent );
            }
        } );

        // security feed button / go to security feed page
        securityCamFeedBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraFeedIntent = new Intent( getApplicationContext(), uploadPhoto_toPi.class );
                startActivity( cameraFeedIntent );
            }
        } );

        // log out button / call logout function
        logoutBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        } );
    }

    // logout functions
    public void logout() {

        /* get instance, make intent, add android flags, start activity to log out of app
           make sure back button can't be used to go back into app without logging in */
        mAuth.getInstance().signOut();
        Intent intent = new Intent(Home.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        System.out.println( "User Logged Out" );
        finish();
    }

}
