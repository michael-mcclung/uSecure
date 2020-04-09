package com.example.usecure;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    // variables
    private FirebaseAuth mAuth;

    // texts and buttons
    EditText emailText, passwordText;
    Button signinbtn, createBtn, forgotpasswordBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );

        // set the view now
        setContentView( R.layout.activity_main );

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        signinbtn = (Button) findViewById( R.id.signinBtn );
        signinbtn.setOnTouchListener( new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                loginUserAccount();
                return false;
            }
        } );

        createBtn = (Button) findViewById( R.id.createBtn );
        createBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent createIntent = new Intent( getApplicationContext(), SignUpActivity.class );
                startActivity( createIntent );
            }
        } );

        forgotpasswordBtn = (Button) findViewById( R.id.forgotpasswordBtn );
        forgotpasswordBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent forgotIntent = new Intent( getApplicationContext(), ForgotPasswordActivity.class );
                startActivity( forgotIntent );
            }
        } );
    }

    private void loginUserAccount(){
        // texts
        emailText = findViewById( R.id.emailText );
        passwordText = findViewById( R.id.passwordText );

        signinbtn.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String email = emailText.getText().toString();
                final String password = passwordText.getText().toString();

                if (TextUtils.isEmpty( email )){
                    Toast.makeText( getApplicationContext(),  "Enter your email address", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty( password )) {
                    Toast.makeText(getApplicationContext(), "Enter your password", Toast.LENGTH_SHORT).show();
                    return;
                }

                // authenticate user
                mAuth.signInWithEmailAndPassword( email, password).
                        addOnCompleteListener( MainActivity.this, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText( getApplicationContext(), "Login successful!", Toast.LENGTH_LONG ).show();
                            Intent intent = new Intent(MainActivity.this, Home.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText( getApplicationContext(), "Login failed! Please try again later.", Toast.LENGTH_LONG ).show();
                        }
                    }
                });
            }

        });
    }
}
