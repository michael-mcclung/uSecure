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

    private Button backToSettingsBtn, cameraBtn, btnUpload, btnChoose;
    private ImageView imgView;
    private EditText userSetImageName;
    int Image_Request_Code = 7;

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
        cameraBtn = (Button) findViewById( R.id.cameraBtn );
        backToSettingsBtn = (Button) findViewById(R.id.backToSettingsBtn);
        btnChoose = (Button) findViewById( R.id.btnChoose );
        btnUpload = (Button) findViewById( R.id.btnUpload );
        imgView = (ImageView) findViewById( R.id.imgView );
        progressDialog = new ProgressDialog(getApplicationContext());

        // go to phones camera
        cameraBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent( MediaStore.ACTION_IMAGE_CAPTURE );
                startActivityForResult( cameraIntent, 0 );
            }
        } );

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
                chooseImg();
            }
        } );

        // call upload image function
        btnUpload.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImg();
            }
        } );
    }

    @Override // check android activity result codes
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult( requestCode, resultCode, data );

        // if result code is ok, then proceed
        if (resultCode == RESULT_OK) {
            Uri selectedImage = data.getData();
            imgView.setImageURI( selectedImage );
        }

        // request code check / variables
        if (requestCode == Image_Request_Code && resultCode == RESULT_OK && data != null && data.getData() != null) {
            FilePathUri = data.getData();
            try {

                /* Getting selected image into Bitmap, setting up bitmap selected image into ImageView.
                   After selecting image change choose button above text */
                Bitmap bitmapChoose = MediaStore.Images.Media.getBitmap( getContentResolver(), FilePathUri );
                imgView.setImageBitmap( bitmapChoose );
                btnChoose.setText( "Image Selected" );

                // catch exceptions
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Creating Method to get the selected image file Extension from File Path URI.
    public String GetFileExtension(Uri uri) {

        // variables
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        // Returning the file Extension.
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri)) ;

    }

    // choose image function / check android request codes
    private void chooseImg(){
        Intent chooseImage = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(chooseImage , 1);
    }

    // upload image functions
    private void uploadImg(){
        // Checking whether FilePathUri Is empty or not.
        if (FilePathUri != null) {

            // Setting progressDialog title and showing progressDialog.
            progressDialog.setTitle("Image is Uploading...");
            progressDialog.show();

            // Creating second StorageReference / adding addOnSuccessListener to second StorageReference.
            StorageReference storageReference2nd = storageReference.child(Storage_Path + System.currentTimeMillis() + "." + GetFileExtension(FilePathUri));
            storageReference2nd.putFile(FilePathUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    // Getting image name from EditText and store into string variable.
                    String TempImageName = userSetImageName.getText().toString();

                    // Hiding the progressDialog after done uploading, showing toast message after done uploading.
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Image Uploaded Successfully ", Toast.LENGTH_LONG).show();

                    @SuppressWarnings("VisibleForTests")
                    ImageUploadInfo imageUploadInfo = new ImageUploadInfo(TempImageName, storageReference.getDownloadUrl().toString());

                    // Getting image upload ID, adding image upload id s child element into databaseReference.
                    String ImageUploadId = databaseReference.push().getKey();
                    databaseReference.child(ImageUploadId).setValue(imageUploadInfo);
                }
            })
                    // If something goes wrong
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {

                            // Hiding the progressDialog, showing exception error message.
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })

                    // On progress change upload time, setting progressDialog Title.
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.setTitle("Image is Uploading...");
                        }
                    });
        }
        else {
            Toast.makeText(getApplicationContext(), "Please Select Image or Add Image Name", Toast.LENGTH_LONG).show();
        }
    }

}

