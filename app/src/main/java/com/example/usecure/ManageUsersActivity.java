package com.example.usecure;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ManageUsersActivity extends AppCompatActivity {


    Button updateUserPhotoBtn, addUserBtn, backToSettingsBtn, deleteUserBtn;
    EditText user;
    ListView users;
    DatabaseReference manageUserDatabase;
    ArrayList<String> myArrayList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_manage_users );

        updateUserPhotoBtn = (Button) findViewById( R.id.updateUserPhotoBtn);
        final ArrayAdapter<String> myArrayAdapter = new ArrayAdapter<String>(ManageUsersActivity.this, android.R.layout.simple_list_item_1, myArrayList);

        user = (EditText) findViewById(R.id.deleteUser);
        users = (ListView) findViewById(R.id.ListView_1);
        users.setAdapter(myArrayAdapter);
        manageUserDatabase = FirebaseDatabase.getInstance().getReference( "Main User" ).child("Sub Users");
        manageUserDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String value = String.valueOf(dataSnapshot.child("username").getValue().toString());//getValue(String.class);
                myArrayList.add(value);
                myArrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                myArrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        updateUserPhotoBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent doorControlIntent = new Intent( getApplicationContext(), UpdateUserPhotoActivity.class );
                startActivity( doorControlIntent );
            }
        } );

        addUserBtn = (Button) findViewById( R.id.addUserBtn );
        addUserBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent adduser = new Intent(getApplicationContext(), AddUser.class);
                startActivity(adduser);
            }
        } );

        deleteUserBtn = (Button) findViewById( R.id.deleteUserBtn );
        deleteUserBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String usr = user.getText().toString().trim();
                manageUserDatabase.child(usr).removeValue();
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
