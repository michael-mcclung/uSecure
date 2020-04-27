package com.example.usecure;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class UpdateUserPhotoActivity extends AppCompatActivity {

    private Button backToSettingsBtn, btnUpload, btnChoose;
    private ImageView imgView;
    private EditText userSetImageName;
    private final int PICK_IMAGE_REQUEST = 71;

    // references
    Uri FilePathUri;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    ProgressDialog progressDialog ;

    // folder path for firebase storage
    String Storage_Path = "All_Image_Uploads";

    // root database name for FB database
    String Database_Path = "All_Image_Uploads_Databse";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user_photo);

        // initiate variables
        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference(Database_Path);
        userSetImageName = (EditText) findViewById( R.id.userSetImageName );
        backToSettingsBtn = (Button) findViewById(R.id.backToSettingsBtn);
        btnChoose = (Button) findViewById( R.id.btnChoose );
        btnUpload = (Button) findViewById( R.id.btnUpload );
        imgView = (ImageView) findViewById( R.id.imgView );
        progressDialog = new ProgressDialog(getApplicationContext());

        // go back to settings page
        backToSettingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backToSettingsIntent = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(backToSettingsIntent);
            }
        });

        // call choose image function
        btnChoose.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        } );

        // call upload image function
        btnUpload.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        } );
    }

    // choose image from phones gallery of photos
    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            FilePathUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), FilePathUri);
                imgView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // upload image functions
    private void uploadImage() {
        if(FilePathUri != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            // get what user wants name of image to be
            String nameOfFile = userSetImageName.getText().toString().trim();

            // uploads to firebase storage
            StorageReference ref = storageReference.child("images/" + nameOfFile) ;

            // upload file to storage, let user know uploading failed and/or show user percentage of uploading file
            ref.putFile(FilePathUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Uploading...", Toast.LENGTH_SHORT).show();
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }
    }

}

