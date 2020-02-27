package com.example.usecure;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class Home extends AppCompatActivity {

    Button voiceCommandBtn;
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
    }

}
