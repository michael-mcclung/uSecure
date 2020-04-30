/*packages*/
package com.example.usecure;

/*imports*/
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;
import java.util.TooManyListenersException;
import java.util.ArrayList;

/*door control activity control code*/
public class DoorControlActivity extends AppCompatActivity {

    // variables
    private ToggleButton doorControlToggle;
    private Button homeBtn;
    private EditText doorSelect;
    private DatabaseReference doorControlDatabase;

    @Override // when starting the page do these
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState);
        setContentView(R.layout.activity_door_control);

        // initialize all variables into view by ids
        doorSelect = (EditText) findViewById(R.id.selectDoor);
        doorControlToggle = (ToggleButton) findViewById(R.id.doorControlToggle);
        homeBtn = (Button) findViewById(R.id.homeBtn);
        ListView doors = (ListView) findViewById(R.id.ListView_door);

        // arrays and class refernces
        final ArrayList<String> myArrayList = new ArrayList<>();
        final ArrayAdapter<String> myArrayAdapter = new ArrayAdapter<String>(DoorControlActivity.this, android.R.layout.simple_list_item_1, myArrayList);
        doors.setAdapter(myArrayAdapter);

        // initialize reference for Firebase database
        doorControlDatabase = FirebaseDatabase.getInstance().getReference("Main User");

        // door control toggle
        doorControlToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String select = doorSelect.getText().toString();
                String Uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                if(doorControlToggle.isChecked()){
                    doorControlDatabase.child(Uid).child("Door List").child(select).child("Door State").child("switchState").setValue(1);
                }else{
                    doorControlDatabase.child(Uid).child("Door List").child(select).child("Door State").child("switchState").setValue(0);
                }            }
        });

        doorControlDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String select = doorSelect.getText().toString();
                String Uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
//                String value = dataSnapshot.child(Uid).child("Door List").child(select).child("Door State").getValue().toString();
//                myArrayList.add(value);
//                myArrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                myArrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        // home button function
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override // go to home page once button is pressed
            public void onClick(View v) {
                // creat intent to go to home page and then initiate it
                Intent homeIntent = new Intent(getApplicationContext(), Home.class);
                startActivity(homeIntent);
            }
        });
    }
}