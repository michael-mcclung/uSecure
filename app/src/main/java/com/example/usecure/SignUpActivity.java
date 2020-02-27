package com.example.usecure;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {

    // variables
    private FirebaseAuth mAuth;

    Button goBackLogInBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_sign_up );

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        goBackLogInBtn = (Button) findViewById( R.id.goBackLogInBtn );
        goBackLogInBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goBackIntent = new Intent( getApplicationContext(), MainActivity.class );
                startActivity( goBackIntent );            }
        } );


    }
}
