package com.example.usecure;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ManageUsersActivity extends AppCompatActivity {


    Button uploadUserPhotoBtn, updateUserBtn, backToSettingsBtn, deleteUserBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_manage_users );

        uploadUserPhotoBtn = (Button) findViewById( R.id.uploadUserPhotoBtn );
        uploadUserPhotoBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent doorControlIntent = new Intent( getApplicationContext(), UploadPhotoActivity.class );
                startActivity( doorControlIntent );
            }
        } );

        updateUserBtn = (Button) findViewById( R.id.updateUserBtn );
        updateUserBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        } );

        deleteUserBtn = (Button) findViewById( R.id.deleteUserBtn );
        deleteUserBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        } );

        backToSettingsBtn = (Button) findViewById( R.id.backToSettingsBtn );
        backToSettingsBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent doorControlIntent = new Intent( getApplicationContext(), SettingsActivity.class );
                startActivity( doorControlIntent );
            }
        } );
    }
}
