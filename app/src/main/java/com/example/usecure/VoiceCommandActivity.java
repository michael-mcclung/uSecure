package com.example.usecure;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Locale;

public class VoiceCommandActivity extends AppCompatActivity {

    private Button recordBtn, homeBtn, deleteRecordingBtn, saveBtn;
    private TextView RecordBtnTextView, autoCompleteText, nameOfRecordingText = null;
    private TextView textOutput;

    private MediaRecorder mRecorder;

    private String mFileName = null;

    private static final String LOG_TAG = "Record_log";

    private StorageReference mStorage;
    private DatabaseReference mDatabase;

    private ProgressDialog mProgress;

    private final int SPEECH_RECOGNITION_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceStatus) {
        super.onCreate( savedInstanceStatus );
        setContentView( R.layout.activity_voice_command );

        mProgress = new ProgressDialog( this );
        mStorage = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference( "Audio" );

        RecordBtnTextView = (TextView) findViewById( R.id.RecordBtnTextView );
        autoCompleteText = (TextView) findViewById( R.id.autoCompleteText );
        nameOfRecordingText = (TextView) findViewById( R.id.nameOfRecordingText );
        textOutput = (TextView) findViewById( R.id.textOutput );

        saveBtn = (Button) findViewById( R.id.saveBtn );
        deleteRecordingBtn = (Button) findViewById( R.id.deleteRecordingBtn );
        recordBtn = (Button) findViewById( R.id.recordBtn );
        homeBtn = (Button) findViewById( R.id.homeBtn );

        mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        mFileName += "/recorder_audio.mp4";

        saveBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String getFileName = textOutput.getText().toString();
                mDatabase.child( getFileName );
            }
        } );

        recordBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSpeechToText();
            }
        } );

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

    /**
     * Start speech to text intent. This opens up Google Speech Recognition API dialog box to listen the speech input.
     */
    private void startSpeechToText() {
        Intent intent = new Intent( RecognizerIntent.ACTION_RECOGNIZE_SPEECH );
        intent.putExtra( RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM );
        intent.putExtra( RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault() );
        intent.putExtra( RecognizerIntent.EXTRA_PROMPT, "Speak something..." );
        try {
            startActivityForResult( intent, SPEECH_RECOGNITION_CODE );
        } catch (ActivityNotFoundException a) {
            Toast.makeText( getApplicationContext(),
                    "Sorry! Speech recognition is not supported in this device.", Toast.LENGTH_SHORT ).show();
        }
    }

    /**
     * Callback for speech recognition activity
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult( requestCode, resultCode, data );

        switch (requestCode) {
            case SPEECH_RECOGNITION_CODE: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> voiceInText = data.getStringArrayListExtra( RecognizerIntent.EXTRA_RESULTS );
                    String text = voiceInText.get( 0 );

                    // puts speech into text in text box
                    textOutput.setText( text );

                    // new way
                    String id = mDatabase.push().getKey();
                    String name = nameOfRecordingText.getText().toString().trim();

                    AudioFiles af = new AudioFiles( id, name, text );
                    mDatabase.child( name ).setValue( af );
                    Toast.makeText( this, "Audio name and audio added", Toast.LENGTH_LONG ).show();
                }
                break;
            }
        }
    }

    // doesnt work properly
    private void deleteOldRecordings() {

        String name = autoCompleteText.getText().toString();
        Task deleteAudio = mDatabase.child( name ).removeValue();

        if (deleteAudio == null) {
            Toast.makeText( this, "Audio not found", Toast.LENGTH_LONG ).show();

        } else {
            Toast.makeText( this, "Audio deleted", Toast.LENGTH_LONG ).show();
        }
    }
}
