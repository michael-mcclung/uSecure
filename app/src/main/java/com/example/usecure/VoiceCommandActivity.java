package com.example.usecure;

import android.app.ProgressDialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;

public class VoiceCommandActivity extends AppCompatActivity {

    private Button recordBtn;
    private TextView RecordBtnTextView;

    private MediaRecorder mRecorder;

    private String mFileName = null;

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
        recordBtn = (Button) findViewById( R.id.recordBtn );

        mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        mFileName +=  "/recorder_audio.3gp";

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

    private void upLoadAudio() {

        mProgress.setMessage( "Uploading audio..." );
        mProgress.show();

        StorageReference filepath = mStorage.child( "Audio" ).child( "new_audio.3gp" );

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
