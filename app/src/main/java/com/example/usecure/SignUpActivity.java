package com.example.usecure;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SharedMemory;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

public class SignUpActivity extends AppCompatActivity {

    // variables
    private FirebaseAuth mAuth;
    EditText newEmailText, newPasswordText, fname, lname, address, phoneNum, firstName;
    Button goBackLogInBtn, registerBtn, uploadPhotoBtn;

    // firebase database reference
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference( "Main User");

    //SharedPreferences pathMemory = getSharedPreferences( "pathMemory", Context.MODE_PRIVATE );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );

        // set the view now
        setContentView( R.layout.activity_sign_up );

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // go to upload photo page
        uploadPhotoBtn = (Button) findViewById( R.id.uploadPhotoBtn );
        uploadPhotoBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goBackIntent = new Intent( getApplicationContext(), UploadPhotoActivity.class );
                startActivity( goBackIntent );
            }
        } );

        // go back to login page
        goBackLogInBtn = (Button) findViewById( R.id.goBackLogInBtn );
        goBackLogInBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goBackIntent = new Intent( getApplicationContext(), MainActivity.class );
                startActivity( goBackIntent );
            }
        } );

        // initialize register button
        registerBtn = (Button) findViewById( R.id.registerBtn );
        registerBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpUser();
            }
        } );


    }

    private void signUpUser() {

        //FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        //String uid = user.getUid();

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
                                        newEmailText = findViewById( R.id.newEmailText );
                                        newPasswordText = findViewById( R.id.newPasswordText );
                                        fname = findViewById( R.id.firstNameEditText );
                                        firstName = findViewById( R.id.firstNameEditText );
                                        lname = findViewById( R.id.lastNameEditText );
                                        address = findViewById( R.id.addressEditText );
                                        phoneNum = findViewById( R.id.phoneEditText );

                                        String email = newEmailText.getText().toString();
                                        String password = newPasswordText.getText().toString();
                                        String first = fname.getText().toString();
                                        String last = lname.getText().toString();
                                        String add = address.getText().toString();
                                        String number = phoneNum.getText().toString();
                                        String nameFirst = fname.getText().toString();

                                        //SharedPreferences.Editor editPathMem = pathMemory.edit();
                                        //editPathMem.putString( "pathKey", nameFirst);
                                        //editPathMem.apply();
                                        String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                                        RegisterUserInformation register = new RegisterUserInformation( email, password, first, last, add, number );
                                        mDatabase.child( userUid ).child( "Login Information" ).setValue( register );
                                        //RegisterUserInformation saveForLater = new RegisterUserInformation(  );

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
