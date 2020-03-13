package com.example.usecure;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Locale;

public class VoiceCommandActivity extends AppCompatActivity {

    private Button recordBtn, homeBtn, deleteRecordingBtn, saveBtn;
    private TextView autoCompleteText, nameOfRecordingText = null, textOutput;

    private DatabaseReference mDatabase;

    private final int SPEECH_RECOGNITION_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceStatus) {
        super.onCreate( savedInstanceStatus );
        setContentView( R.layout.activity_voice_command );

        mDatabase = FirebaseDatabase.getInstance().getReference( "Main User" );

        autoCompleteText = (TextView) findViewById( R.id.autoCompleteText );
        nameOfRecordingText = (TextView) findViewById( R.id.nameOfRecordingText );
        textOutput = (TextView) findViewById( R.id.textOutput );

        saveBtn = (Button) findViewById( R.id.saveBtn );
        deleteRecordingBtn = (Button) findViewById( R.id.deleteRecordingBtn );
        recordBtn = (Button) findViewById( R.id.recordBtn );
        homeBtn = (Button) findViewById( R.id.homeBtn );

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
                    String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();


                    AudioFiles af = new AudioFiles( id, name, text );
                    mDatabase.child( userUid ).child( "Voice Recognition" ).setValue( af );

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

// tried all for saving the audio file in the firebase database

// didn't work
// FirebaseUser mainPath = null;
// FirebaseUser mainPath = audioPath.getCurrentUser();

// did not work
// RegisterUserInformation mainPath = new RegisterUserInformation(  );
// String pathName = mainPath.getFname();

// didn't work
//DataSnapshot userFile = null;
// Iterable<DataSnapshot> path = userFile.child( String.valueOf( mainPath ) ).getChildren();

// not working - uses random uuid for everytime saved and not current uuid/user
// FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
// String uid = user.getUid();

//RegisterUserInformation voiceUse = new RegisterUserInformation( );

// didn't work
//String firebaseQue = voiceUse.getFname();
//String firebaseQue = mDatabase.getKey();

// didn't work
//SharedPreferences savedPath = getSharedPreferences( "pathMemory", Context.MODE_PRIVATE );
//String user = FirebaseAuth.getInstance().getCurrentUser().toString();
//String path = savedPath.getString( "pathKey", "Default" );