package com.example.usecure;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class ManageUsersActivity extends AppCompatActivity {


    Button updateUserPhotoBtn, addUserBtn, backToSettingsBtn, deleteUserBtn;
    ListView users;
    DatabaseReference manageUserDatabase;
    ArrayList<String> myArrayList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_manage_users );

        updateUserPhotoBtn = (Button) findViewById( R.id.updateUserPhotoBtn);
        /*final ArrayAdapter<String> myArrayAdapter = new ArrayAdapter<String>(ManageUsersActivity.this, android.R.layout.simple_list_item_1, myArrayList);

        users = (ListView) findViewById(R.id.ListView_1);
        users.setAdapter(myArrayAdapter);
        manageUserDatabase = FirebaseDatabase.getInstance().getReference( "Main User" ).child("Sub Users");
        manageUserDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String value = dataSnapshot.getValue(String.class);
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
        });*/



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
