package com.example.usecure;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class NotificationsActivity extends AppCompatActivity {

    Button settingsBtn2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_notifications );

        settingsBtn2 = (Button) findViewById( R.id.settingsBtn2 );
        settingsBtn2.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent homeIntent = new Intent( getApplicationContext(), SettingsActivity.class );
                startActivity( homeIntent );
            }
        } );
    }
}
