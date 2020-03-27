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
import java.util.UUID;

public class UploadPhotoActivity extends AppCompatActivity {

    private Button cameraBtn, backToSignUpBtn, btnUpload, btnChoose;
    private ImageView imgView;
    private EditText userSetImageName;
    Uri FilePathUri;

    StorageReference storageReference;
    DatabaseReference databaseReference;

    ProgressDialog progressDialog ;

    int Image_Request_Code = 7;

    // folder path for firebase storage
    String Storage_Path = "All_Image_Uploads";

    // root database name for FB database
    String Database_Path = "All_Image_Uploads_Databse";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_upload_photo );

        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference(Database_Path);

        userSetImageName = (EditText) findViewById( R.id.userSetImageName );
        cameraBtn = (Button) findViewById( R.id.cameraBtn );
        backToSignUpBtn = (Button) findViewById( R.id.backToSignUpBtn );
        btnChoose = (Button) findViewById( R.id.btnChoose );
        btnUpload = (Button) findViewById( R.id.btnUpload );

        imgView = (ImageView) findViewById( R.id.imgView );
        progressDialog = new ProgressDialog(getApplicationContext());

        cameraBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent( MediaStore.ACTION_IMAGE_CAPTURE );
                startActivityForResult( cameraIntent, 0 );
            }
        } );

        backToSignUpBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backIntent = new Intent ( getApplicationContext(), SignUpActivity.class );
                startActivity( backIntent );
            }
        } );

        btnChoose.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              chooseImg();
            }
        } );

        btnUpload.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               uploadImg();

            }
        } );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult( requestCode, resultCode, data );
       // Bitmap bitmapSelect = (Bitmap) data.getExtras().get( "data" );
        //imgView.setImageBitmap( bitmapSelect );

        if (resultCode == RESULT_OK) {
            Uri selectedImage = data.getData();
            imgView.setImageURI( selectedImage );

        }

        if (requestCode == Image_Request_Code && resultCode == RESULT_OK && data != null && data.getData() != null) {

            FilePathUri = data.getData();

            try {

                // Getting selected image into Bitmap.
                Bitmap bitmapChoose = MediaStore.Images.Media.getBitmap( getContentResolver(), FilePathUri );

                // Setting up bitmap selected image into ImageView.
                imgView.setImageBitmap( bitmapChoose );

                // After selecting image change choose button above text.
                btnChoose.setText( "Image Selected" );

            } catch (IOException e) {

                e.printStackTrace();
            }
        }
    }

    // Creating Method to get the selected image file Extension from File Path URI.
    public String GetFileExtension(Uri uri) {

        ContentResolver contentResolver = getContentResolver();

        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        // Returning the file Extension.
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri)) ;

    }

    private void chooseImg(){
        Intent chooseImage = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(chooseImage , 1);
    }

    private void uploadImg(){
        // Checking whether FilePathUri Is empty or not.
        if (FilePathUri != null) {

            // Setting progressDialog Title.
            progressDialog.setTitle("Image is Uploading...");

            // Showing progressDialog.
            progressDialog.show();

            // Creating second StorageReference.
            StorageReference storageReference2nd = storageReference
                    .child(Storage_Path + System.currentTimeMillis() + "." + GetFileExtension(FilePathUri));

            // Adding addOnSuccessListener to second StorageReference.
            storageReference2nd.putFile(FilePathUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            // Getting image name from EditText and store into string variable.
                            String TempImageName = userSetImageName.getText().toString();

                            // Hiding the progressDialog after done uploading.
                            progressDialog.dismiss();

                            // Showing toast message after done uploading.
                            Toast.makeText(getApplicationContext(), "Image Uploaded Successfully ", Toast.LENGTH_LONG).show();

                            @SuppressWarnings("VisibleForTests")
                            ImageUploadInfo imageUploadInfo = new ImageUploadInfo(TempImageName, storageReference.getDownloadUrl().toString());

                            // Getting image upload ID.
                            String ImageUploadId = databaseReference.push().getKey();

                            // Adding image upload id s child element into databaseReference.
                            databaseReference.child(ImageUploadId).setValue(imageUploadInfo);
                        }
                    })
                    // If something goes wrong .
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {

                            // Hiding the progressDialog.
                            progressDialog.dismiss();

                            // Showing exception erro message.
                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })

                    // On progress change upload time.
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                            // Setting progressDialog Title.
                            progressDialog.setTitle("Image is Uploading...");

                        }
                    });
        }
        else {

            Toast.makeText(getApplicationContext(), "Please Select Image or Add Image Name", Toast.LENGTH_LONG).show();

        }
    }
}
