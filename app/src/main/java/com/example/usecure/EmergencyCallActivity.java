// packages
package com.example.usecure;

// imports
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

// emergency call activity
public class EmergencyCallActivity extends AppCompatActivity {

    @Override // used to produce a emergency call page
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_emergency_call );
    }
}
