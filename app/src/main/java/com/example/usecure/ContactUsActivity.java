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

public class ContactUsActivity extends AppCompatActivity {

    Button settingsBtn;
    Button submitBtn;
    ticket support;
    EditText name, email, reason;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_contact_us );

        name = (EditText) findViewById(R.id.editText4);
        email = (EditText) findViewById(R.id.editText3);
        reason = (EditText) findViewById(R.id.editText5);
        settingsBtn = (Button) findViewById( R.id.settingsBtn2 );
        submitBtn = (Button) findViewById(R.id.submitBtn);
        mDatabase = FirebaseDatabase.getInstance().getReference( "Main User").child("Support tickets");
        support = new ticket();

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                support.setEmail(email.getText().toString().trim());
                support.setName(name.getText().toString().trim());
                support.setReason(reason.getText().toString().trim());
                mDatabase.push().setValue(support);
                Toast.makeText(ContactUsActivity.this, "Ticket sent", Toast.LENGTH_LONG).show();
            }
        });

        settingsBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent homeIntent = new Intent( getApplicationContext(), SettingsActivity.class );
                startActivity( homeIntent );
            }
        } );
    }
}
