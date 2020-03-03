package com.example.usecure;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;

public class VoiceCommandActivity extends AppCompatActivity {

    private Button recordBtn, homeBtn, deleteRecordingBtn, saveBtn;
    private TextView RecordBtnTextView, autoCompleteText, nameOfRecordingText = null;

    private MediaRecorder mRecorder;

    private String mFileName = null, userRecordindName;

    private static final String LOG_TAG = "Record_log";

    private StorageReference mStorage;

    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceStatus) {
        super.onCreate( savedInstanceStatus );
        setContentView( R.layout.activity_voice_command );

        mProgress = new ProgressDialog( this );
        mStorage = FirebaseStorage.getInstance().getReference();

        RecordBtnTextView = (TextView) findViewById( R.id.RecordBtnTextView );
        autoCompleteText = (TextView) findViewById( R.id.autoCompleteText );
        nameOfRecordingText = (TextView) findViewById( R.id.nameOfRecordingText );

        saveBtn = (Button) findViewById( R.id.saveBtn );
        deleteRecordingBtn = (Button) findViewById( R.id.deleteRecordingBtn );
        recordBtn = (Button) findViewById( R.id.recordBtn );
        homeBtn = (Button) findViewById( R.id.homeBtn );

        mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        mFileName +=  "/recorder_audio.mp4";

        saveBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String getFileName = nameOfRecordingText.getText().toString();
                nameOfRecordingText.setText( getFileName );
            }
        } );

        recordBtn.setOnTouchListener( new View.OnTouchListener() {


            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN){

                    startRecording();
                    RecordBtnTextView.setText( "Recording started..." );

                } else if (event.getAction() == MotionEvent.ACTION_UP) {

                    stopRecording();
                    RecordBtnTextView.setText( "Recording stopped!" );

                }
                return false;
            }
        });

        deleteRecordingBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteOldRecordings();
            }
        } );

        homeBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent homeIntent = new Intent( getApplicationContext(), Home.class );
                startActivity( homeIntent );
            }
        } );

    }

    private void startRecording() {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(mFileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }

        mRecorder.start();
    }

    private void stopRecording() {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;

        upLoadAudio();
    }

    private void deleteOldRecordings() {

        // Create a reference to the file to delete
        StorageReference desertRef =  mStorage.child( "Audio" ).child( autoCompleteText.getText().toString() + ".3gp" );

        mProgress.setMessage( "Deleting recording..." );
        mProgress.show();

        // Delete the file
        desertRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mProgress.setMessage( "Recording deleted!" );
                mProgress.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                mProgress.setMessage( "ERROR!" );
                mProgress.show();
                mProgress.dismiss();
            }
        });
    }

    private void upLoadAudio() {

        mProgress.setMessage( "Uploading audio..." );
        mProgress.show();

        StorageReference filepath = mStorage.child( "Audio" ).child( nameOfRecordingText.getText().toString() + ".3gp" );

        Uri uri = Uri.fromFile( new File( mFileName ) );

        filepath.putFile( uri ).addOnSuccessListener( new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                mProgress.dismiss();
                mProgress.setMessage( "Uploading finished." );

            }
        } );
    }
}
