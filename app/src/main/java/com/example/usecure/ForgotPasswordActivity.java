package com.example.usecure;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    // variables
    private FirebaseAuth mAuth;

    // texts and buttons
    EditText userEmailText;
    Button resetPasswordBtn, loginBackBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_forgot_password );

        mAuth = FirebaseAuth.getInstance();

        userEmailText = (EditText) findViewById( R.id.userEmailText );
        resetPasswordBtn = (Button) findViewById( R.id.resetPasswordBtn );
        resetPasswordBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
            }
        } );

        loginBackBtn = (Button) findViewById( R.id.loginBackBtn );
        loginBackBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = new Intent( getApplicationContext(), MainActivity.class );
                startActivity( signInIntent );
            }
        } );
    }

    public void resetPassword() {
        String email = userEmailText.getText().toString().trim();
        if (TextUtils.isEmpty( email )) {
            Toast.makeText( getApplication(), "Enter your email address", Toast.LENGTH_SHORT ).show();
            return;
        }

        mAuth.sendPasswordResetEmail( email ).addOnCompleteListener( new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText( ForgotPasswordActivity.this, "We sent you an email", Toast.LENGTH_SHORT ).show();

                } else {
                    Toast.makeText( ForgotPasswordActivity.this, "ERROR", Toast.LENGTH_SHORT ).show();
                }
            }
        } );
    }
}
