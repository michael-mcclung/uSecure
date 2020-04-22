// packages
package com.example.usecure;

// imports
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
import com.google.firebase.auth.FirebaseAuth;

// forgot password activity
public class ForgotPasswordActivity extends AppCompatActivity {

    // variables
    private FirebaseAuth mAuth;

    // texts and buttons
    EditText userEmailText;
    Button resetPasswordBtn, loginBackBtn;

    @Override // creates the forgot password page
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_forgot_password );

        // initiate variables / buttons
        mAuth = FirebaseAuth.getInstance();
        userEmailText = (EditText) findViewById( R.id.userEmailText );
        resetPasswordBtn = (Button) findViewById( R.id.resetPasswordBtn );
        loginBackBtn = (Button) findViewById( R.id.loginBackBtn );

        // reset password button
        resetPasswordBtn.setOnClickListener( new View.OnClickListener() {
            @Override // call resetPassword function
            public void onClick(View v) {
                resetPassword();
            }
        } );

        // go back to log in page button
        loginBackBtn.setOnClickListener( new View.OnClickListener() {
            @Override // create intent and go back to log in page once button is pressed
            public void onClick(View v) {
                Intent signInIntent = new Intent( getApplicationContext(), MainActivity.class );
                startActivity( signInIntent );
            }
        } );
    }

    // reset password function
    public void resetPassword() {

        // variable to get users email given by user
        String email = userEmailText.getText().toString().trim();

        // if not email given then alert user to type email into text field
        if (TextUtils.isEmpty( email )) {
            Toast.makeText( getApplication(), "Enter your email address", Toast.LENGTH_SHORT ).show();
            return;
        }

        // connect with firebase database
        mAuth.sendPasswordResetEmail( email ).addOnCompleteListener( new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                // if email address is valid, forgot password email to users email address
                if (task.isSuccessful()) {
                    Toast.makeText( ForgotPasswordActivity.this, "We sent you an email", Toast.LENGTH_SHORT ).show();

                  // error if users email address is invalid
                } else {
                    Toast.makeText( ForgotPasswordActivity.this, "ERROR", Toast.LENGTH_SHORT ).show();
                }
            }
        } );
    }
}
