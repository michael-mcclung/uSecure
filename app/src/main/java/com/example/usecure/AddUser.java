// packages
package com.example.usecure;

// imports
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

// add user page
public class AddUser extends AppCompatActivity {

    // vairables
    Button addusr, back;
    EditText username, name, email, password;
    SubUser NewUser;
    private DatabaseReference addUserDatabase;

    @Override // start add user page
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        // initiate variables
        username = (EditText) findViewById(R.id.name);
        name = (EditText) findViewById(R.id.name1);
        email = (EditText) findViewById(R.id.email1);
        password = (EditText) findViewById(R.id.pw1);
        back = (Button) findViewById(R.id.backToSettingsBtn2);
        addusr = (Button) findViewById(R.id.adduser);
        addUserDatabase = FirebaseDatabase.getInstance().getReference( "Main User").child("Sub Users");
        NewUser = new SubUser();

        // add user information
        addusr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewUser.setUsername(username.getText().toString().trim());
                NewUser.setName(name.getText().toString().trim());
                NewUser.setEmail(email.getText().toString().trim());
                NewUser.setPassword(password.getText().toString().trim());
                addUserDatabase.child(NewUser.getUsername()).setValue(NewUser);
                Toast.makeText(AddUser.this,"User Added", Toast.LENGTH_LONG).show();
            }
        });

        // go back to manage user page
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back_1 = new Intent( getApplicationContext(), ManageUsersActivity.class);
                startActivity(back_1);
            }
        });
    }
}
