package com.example.usecure;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {

    // variables
    private FirebaseAuth mAuth;

    EditText newEmailText, newPasswordText;
    Button goBackLogInBtn, registerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );

        // set the view now
        setContentView( R.layout.activity_sign_up );

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        goBackLogInBtn = (Button) findViewById( R.id.goBackLogInBtn );
        goBackLogInBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goBackIntent = new Intent( getApplicationContext(), MainActivity.class );
                startActivity( goBackIntent );
            }
        } );

        registerBtn = (Button) findViewById( R.id.registerBtn );
        registerBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpUser();
            }
        } );
    }

    private void signUpUser() {
        // texts
        newEmailText = findViewById( R.id.newEmailText );
        newPasswordText = findViewById( R.id.newPasswordText );

        registerBtn.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String onClickEmail = newEmailText.getText().toString();
                final String onClickPassword = newPasswordText.getText().toString();

                if (TextUtils.isEmpty( onClickEmail )) {
                    Toast.makeText( getApplicationContext(), "Please enter your E-mail address", Toast.LENGTH_LONG ).show();
                    return;
                }
                if (TextUtils.isEmpty( onClickEmail )) {
                    Toast.makeText( getApplicationContext(), "Please enter your Password", Toast.LENGTH_LONG ).show();
                }
                if (onClickPassword.length() == 0) {
                    Toast.makeText( getApplicationContext(), "Please enter your Password", Toast.LENGTH_LONG ).show();
                }
                if (onClickPassword.length() < 8) {
                    Toast.makeText( getApplicationContext(), "Password must be at least 6 characters", Toast.LENGTH_LONG ).show();
                } else {
                    mAuth.createUserWithEmailAndPassword( onClickEmail, onClickPassword )
                            .addOnCompleteListener( SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (!task.isSuccessful()) {
                                        Toast.makeText( SignUpActivity.this, "ERROR", Toast.LENGTH_LONG ).show();
                                    } else {
                                        Toast.makeText( getApplicationContext(), "New Account Created!", Toast.LENGTH_LONG ).show();
                                        Intent signUpIntent = new Intent( SignUpActivity.this, MainActivity.class );
                                        startActivity( signUpIntent );
                                        finish();
                                    }
                                }
                            } );
                }
            }
        } );
    }
}
