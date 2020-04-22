// packages
package com.example.usecure;

// imports
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

// main page / log in page
public class MainActivity extends AppCompatActivity {

    // variables
    private FirebaseAuth mAuth;
    EditText emailText, passwordText;
    Button signinbtn, createBtn, forgotpasswordBtn;

    @Override // start main page
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        // Initialize vaiables
        mAuth = FirebaseAuth.getInstance();
        signinbtn = (Button) findViewById( R.id.signinBtn );
        createBtn = (Button) findViewById( R.id.createBtn );
        forgotpasswordBtn = (Button) findViewById( R.id.forgotpasswordBtn );

        // call login user account function / or return false once button is pressed
        signinbtn.setOnTouchListener( new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                loginUserAccount();
                return false;
            }
        } );

        // go to create account page once button is pressed
        createBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent createIntent = new Intent( getApplicationContext(), SignUpActivity.class );
                startActivity( createIntent );
            }
        } );

        // go to forgot password page once button is pressed
        forgotpasswordBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent forgotIntent = new Intent( getApplicationContext(), ForgotPasswordActivity.class );
                startActivity( forgotIntent );
            }
        } );
    }

    // log in user account function
    private void loginUserAccount(){

        // initiate variables
        emailText = findViewById( R.id.emailText );
        passwordText = findViewById( R.id.passwordText );

        // sign in button
        signinbtn.setOnClickListener( new View.OnClickListener() {

            @Override // once button is pressed check these
            public void onClick(View v) {

                // variables
                String email = emailText.getText().toString();
                final String password = passwordText.getText().toString();

                // if user did not type an email address in correct field then alert them to do so
                if (TextUtils.isEmpty( email )){
                    Toast.makeText( getApplicationContext(),  "Enter your email address", Toast.LENGTH_SHORT).show();
                    return;
                }
                // if user did not type a password then alert them to do so
                if (TextUtils.isEmpty( password )) {
                    Toast.makeText(getApplicationContext(), "Enter your password", Toast.LENGTH_SHORT).show();
                    return;
                }

                // authenticate user / make sure credentials are valid
                mAuth.signInWithEmailAndPassword( email, password).
                        addOnCompleteListener( MainActivity.this, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText( getApplicationContext(), "Login successful!", Toast.LENGTH_LONG ).show();
                            Intent intent = new Intent(MainActivity.this, Home.class);
                            startActivity(intent);
                            finish();

                          // log in failed as user credentials are invalid
                        } else {
                            Toast.makeText( getApplicationContext(), "Login failed! Please try again later.", Toast.LENGTH_LONG ).show();
                        }
                    }
                });
            }

        });
    }
}
