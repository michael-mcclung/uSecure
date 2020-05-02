package com.example.usecure;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class uploadPhoto_toPi extends AppCompatActivity {
    private Button backToHomeBtn, upload;
    private DatabaseReference photoDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_photo_to_pi);
        photoDatabase = FirebaseDatabase.getInstance().getReference("Main User");
        backToHomeBtn = (Button) findViewById(R.id.backToHomeBtn);
        upload = (Button) findViewById(R.id.uploadPhotoBtn3);

        backToHomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backToHome = new Intent(getApplicationContext(), Home.class);
                startActivity(backToHome);
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userUidPhoto = FirebaseAuth.getInstance().getCurrentUser().getUid();
                photoDatabase.child(userUidPhoto).child("Images").child("Status").setValue(1);
                Toast.makeText(getApplicationContext(),"Upload Photo process has started", Toast.LENGTH_LONG).show();
            }
        });

    }
}
