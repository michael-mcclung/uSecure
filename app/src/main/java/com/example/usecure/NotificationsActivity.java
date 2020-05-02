package com.example.usecure;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

public class NotificationsActivity extends AppCompatActivity {

    // variables
    private Button settingsBtn2;
    private Switch doorOpenSwitch, intruderSwitchBtn;
    private DatabaseReference notificationRef;
    private static final String TAG = "NotificationsActivity";

    @Override // create notifications page
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_notifications );

        // initialize variables
        settingsBtn2 = (Button) findViewById( R.id.settingsBtn2 );
        doorOpenSwitch = (Switch) findViewById( R.id.doorOpenSwitch );
        intruderSwitchBtn = (Switch) findViewById( R.id.intruderSwitchBtn );

        // initialize firebase database main reference
        notificationRef = FirebaseDatabase.getInstance().getReference( "Main User" );

        // settings button button
        settingsBtn2.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent homeIntent = new Intent( getApplicationContext(), SettingsActivity.class );
                startActivity( homeIntent );
            }
        } );

        // uid of current user and different database reference paths for different notifications
        String Uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference ledSwitchRef = notificationRef.child( Uid ).child( "LED" ).child( "LED State" ).child( "switchState" );
        DatabaseReference intruderRef = notificationRef.child( Uid ).child( "Notifications" ).child( "Unrecognized" );

        // make a pop-up in app to user of their door being opened
        ledSwitchRef.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot ledDataSnapshot) {

                // variables
                String ledValue = ledDataSnapshot.getValue().toString();
                String ledCompare = "1";

                /* if door switch is activated and value / compare are same then make a pop up notification
                   to user and send a notification to their phone that the door has been opened */
                if (doorOpenSwitch.isChecked() && ledValue.equals( ledCompare )) {
                        Toast.makeText( getApplicationContext(), "DOOR OPENED", Toast.LENGTH_LONG ).show();
                        notifyOpenDoor();
                    }
                }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "Failed to read value.", databaseError.toException());
            }
        } );

        // make a pop-up in app to user of an intruder trying to get in
        intruderRef.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                // variables
                String value = dataSnapshot.getValue().toString();
                String compare = "1";

                /* if door switch is activated and value / compare are same then make a pop up notification
                   to user and send a notification to their phone that an intruder tried to get into their home */
                if (intruderSwitchBtn.isChecked() && value.equals( compare )) {
                        Toast.makeText( getApplicationContext(), "INTRUDER", Toast.LENGTH_LONG ).show();
                        notifyOfIntruder();
                    }
                }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "Failed to read value.", databaseError.toException());
            }
        } );
    }

    // notification of open door function
    private void notifyOpenDoor(){

        // variables
        String name = "uSecure";
        String messege = "Someone has entered your house!";

        // creating an id and name to get the correct channel to sen the notification
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel( "door", "door", NotificationManager.IMPORTANCE_DEFAULT );

            NotificationManager manager = getSystemService( NotificationManager.class );
            manager.createNotificationChannel( channel );
        }

        // setting specific characteristics about the look of the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder( this, "door" )
                .setContentText( messege )
                .setSmallIcon( R.drawable.ic_launcher_recognized_foreground )
                .setAutoCancel( true )
                .setContentTitle( name );

        // getting notification from this page and building the final id / notification
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from( this );
        managerCompat.notify( 999, builder.build() );
    }

    // notification of an intruder trying to get into home
    private void notifyOfIntruder(){

        // variables
        String name = "uSecure";
        String messege = "INTRUDER! Someone tried to enter your home";

        // creating an id and name to get the correct channel to sen the notification
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel( "intruder", "intruder", NotificationManager.IMPORTANCE_DEFAULT );

            NotificationManager manager = getSystemService( NotificationManager.class );
            manager.createNotificationChannel( channel );
        }

        // setting specific characteristics about the look of the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder( this, "intruder" )
                .setContentText( messege )
                .setSmallIcon( R.drawable.ic_launcher_unrecognized_foreground )
                .setAutoCancel( true )
                .setContentTitle( name );

        // getting notification from this page and building the final id / notification
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from( this );
        managerCompat.notify( 999, builder.build() );

    }

    // notification of a new user added
    private void notifyOfNewUser(){

        // variables
        String name = "uSecure";
        String messege = "New user was created!";

        // creating an id and name to get the correct channel to sen the notification
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel( "NewUser", "NewUser", NotificationManager.IMPORTANCE_DEFAULT );

            NotificationManager manager = getSystemService( NotificationManager.class );
            manager.createNotificationChannel( channel );
        }

        // setting specific characteristics about the look of the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder( this, "NewUser" )
                .setContentText( messege )
                .setSmallIcon( R.drawable.new_user_front )
                .setAutoCancel( true )
                .setContentTitle( name );

        // getting notification from this page and building the final id / notification
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from( this );
        managerCompat.notify( 999, builder.build() );

    }
}
