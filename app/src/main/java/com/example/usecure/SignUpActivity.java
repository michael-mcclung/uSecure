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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

// signup user activity page
public class SignUpActivity extends AppCompatActivity {

    // variables
    private FirebaseAuth mAuth;
    EditText newEmailText, newPasswordText, fname, lname, address, phoneNum;
    Button goBackLogInBtn, registerBtn;

    // firebase database reference
    DatabaseReference signUpDatabase = FirebaseDatabase.getInstance().getReference( "Main User");

    @Override // start sign up page
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );

        // set the view now
        setContentView( R.layout.activity_sign_up );

        // Initialize variables
        mAuth = FirebaseAuth.getInstance();
        goBackLogInBtn = (Button) findViewById( R.id.goBackLogInBtn );
        registerBtn = (Button) findViewById( R.id.registerBtn );
        fname = (EditText) findViewById(R.id.firstNameEditText);
        lname = (EditText) findViewById(R.id.lastNameEditText);
        newEmailText = (EditText) findViewById(R.id.newEmailText);
        newPasswordText = (EditText) findViewById(R.id.newPasswordText);
        address = (EditText) findViewById(R.id.addressEditText);
        phoneNum = (EditText) findViewById(R.id.phoneEditText);

        // go back to login page
        goBackLogInBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goBackIntent = new Intent( getApplicationContext(), MainActivity.class );
                startActivity( goBackIntent );
            }
        } );

        // call register user function
        registerBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                signUpUser();
                return false;
            }
        });
    }

    // register user function
    private void signUpUser() {

        // initialize variables
        newEmailText = findViewById( R.id.newEmailText );
        newPasswordText = findViewById( R.id.newPasswordText );

        // when register button is pressed, check these
        registerBtn.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // variables
                String onClickEmail = newEmailText.getText().toString();
                final String onClickPassword = newPasswordText.getText().toString();

                // if no email then alert user to enter email address
                if (TextUtils.isEmpty( onClickEmail )) {
                    Toast.makeText( getApplicationContext(), "Please enter your E-mail address", Toast.LENGTH_LONG ).show();
                    return;
                }

                // if no password then alert user to enter password
                if (TextUtils.isEmpty( onClickEmail )) {
                    Toast.makeText( getApplicationContext(), "Please enter your Password", Toast.LENGTH_LONG ).show();
                }

                // if password is equal to 0 then alert user to change password
                if (onClickPassword.length() == 0) {
                    Toast.makeText( getApplicationContext(), "Please enter your Password", Toast.LENGTH_LONG ).show();
                }

                // if password is less than 0 then alert user that password has a criteria
                if (onClickPassword.length() < 6) {
                    Toast.makeText( getApplicationContext(), "Password must be at least 6 characters", Toast.LENGTH_LONG ).show();

                // else validate user's credentials
                } else {
                    mAuth.createUserWithEmailAndPassword( onClickEmail, onClickPassword )
                            .addOnCompleteListener( SignUpActivity.this, new OnCompleteListener<AuthResult>() {

                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    // if log in is invalid then alert user with an error messege
                                    if (task.isSuccessful()) {

                                        // variables used to update firebase database
                                        String email = newEmailText.getText().toString();
                                        String password = newPasswordText.getText().toString();
                                        String first = fname.getText().toString();
                                        String last = lname.getText().toString();
                                        String add = address.getText().toString();
                                        String number = phoneNum.getText().toString();
                                        String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                                        // create new logging in credentials, upload to firebase realtime database amd alert user of new account created
                                        RegisterUserInformation register = new RegisterUserInformation( email, password, first, last, add, number );
                                        signUpDatabase.child( userUid ).child( "Login Information" ).setValue( register);
                                        Toast.makeText( getApplicationContext(), "New Account Created!", Toast.LENGTH_LONG ).show();

                                        // once all is validated / created then go to log in page
                                        Intent signUpIntent = new Intent( SignUpActivity.this, MainActivity.class );
                                        startActivity( signUpIntent );
                                        finish();

                                     // else validate information
                                    } else {
                                        Toast.makeText( SignUpActivity.this, "ERROR", Toast.LENGTH_LONG ).show();
                                    }
                                }
                            } );
                }
            }
        } );
    }
}
