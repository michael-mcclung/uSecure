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

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        // texts
        final EditText emailText = findViewById( R.id.emailText );
        final EditText passwordText = findViewById( R.id.passwordText );

        // buttons
        Button signInbtn = (Button) findViewById( R.id.signinBtn );
        Button createBtn = (Button) findViewById( R.id.createBtn );
        Button forgotBtn = (Button) findViewById( R.id.forgotpasswordBtn );

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        signInbtn.setOnClickListener( new View.OnClickListener() {
            String email = emailText.getText().toString();
            String password = passwordText.getText().toString();

            @Override
            public void onClick(View v) {
                    mAuth.createUserWithEmailAndPassword( email, password ).addOnCompleteListener( MainActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (!task.isSuccessful()) {
                                        Toast.makeText( MainActivity.this, "ERROR", Toast.LENGTH_LONG ).show();
                                    } else {
                                        startActivity( new Intent( MainActivity.this, Home.class ) );
                                        finish();
                                    }
                                }

                            }
                    );


                }

        });
    }
}
