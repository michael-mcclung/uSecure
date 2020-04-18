package com.example.usecure;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddUser extends AppCompatActivity {

    Button addusr, back;
    EditText username, name, email, password;
    SubUser NewUser;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);
        username = (EditText) findViewById(R.id.name);
        name = (EditText) findViewById(R.id.name1);
        email = (EditText) findViewById(R.id.email1);
        password = (EditText) findViewById(R.id.pw1);
        back = (Button) findViewById(R.id.backToSettingsBtn2);
        addusr = (Button) findViewById(R.id.adduser);
        mDatabase = FirebaseDatabase.getInstance().getReference( "Main User").child("Sub Users");
        NewUser = new SubUser();

        addusr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewUser.setUsername(username.getText().toString().trim());
                NewUser.setName(name.getText().toString().trim());
                NewUser.setEmail(email.getText().toString().trim());
                NewUser.setPassword(password.getText().toString().trim());
                mDatabase.child(NewUser.getUsername()).setValue(NewUser);
                Toast.makeText(AddUser.this,"User Added", Toast.LENGTH_LONG).show();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back_1 = new Intent( getApplicationContext(), ManageUsersActivity.class);
                startActivity(back_1);
            }
        });
    }
}
