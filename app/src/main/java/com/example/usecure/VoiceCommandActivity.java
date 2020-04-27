// packages
package com.example.usecure;

// imports
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;
import java.util.Locale;

// voice command activity
public class VoiceCommandActivity extends AppCompatActivity {

    // variables
    private Button recordBtn, homeBtn, deleteRecordingBtn;
    private TextView autoCompleteText, nameOfRecordingText = null;
    private DatabaseReference voiceCommandDatabase;
    private final int SPEECH_RECOGNITION_CODE = 1;

    @Override // create voice command page
    protected void onCreate(Bundle savedInstanceStatus) {
        super.onCreate( savedInstanceStatus );
        setContentView( R.layout.activity_voice_command );

        // initiate variables
        voiceCommandDatabase = FirebaseDatabase.getInstance().getReference( "Main User" );
        autoCompleteText = (TextView) findViewById( R.id.autoCompleteText );
        nameOfRecordingText = (TextView) findViewById( R.id.nameOfRecordingText );
        deleteRecordingBtn = (Button) findViewById( R.id.deleteRecordingBtn );
        recordBtn = (Button) findViewById( R.id.recordBtn );
        homeBtn = (Button) findViewById( R.id.settingsBtn2 );

        // start recording speech to text
        recordBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSpeechToText();
            }
        } );

        // delete recording
        deleteRecordingBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // get information
                String name = autoCompleteText.getText().toString();
                String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                String deleteAudio = voiceCommandDatabase.child( userUid ).child( "Voice Recognition" ).child( name ).toString();

                // remove value form firebase databse and let user know audio deleted
                voiceCommandDatabase.getRef().removeValue();
                //Toast.makeText( VoiceCommandActivity.this, "Audio deleted", Toast.LENGTH_LONG ).show();

            }
        } );

        // go to home page
        homeBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent homeIntent = new Intent( getApplicationContext(), Home.class );
                startActivity( homeIntent );
            }
        } );

    }

    // Start speech to text intent. This opens up Google Speech Recognition API dialog box to listen the speech input.
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

    // Callback for speech recognition activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult( requestCode, resultCode, data );

        switch (requestCode) {
            case SPEECH_RECOGNITION_CODE: {
                if (resultCode == RESULT_OK && null != data) {

                    // variables
                    ArrayList<String> voiceInText = data.getStringArrayListExtra( RecognizerIntent.EXTRA_RESULTS );
                    String text = voiceInText.get( 0 );
                    String id = voiceCommandDatabase.push().getKey();
                    String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    String name = nameOfRecordingText.getText().toString().trim();

                    // create new audio file information / add to firebase realtime database
                    AudioFiles af = new AudioFiles( id, text );
                    voiceCommandDatabase.child( userUid ).child( "Voice Recognition" ).child(name).setValue( text );

                    // alert user of completion
                    Toast.makeText( this, "Audio name and audio added", Toast.LENGTH_LONG ).show();
                }
                break;
            }
        }
    }
}
