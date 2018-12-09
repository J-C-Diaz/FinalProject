package edu.ucsb.cs.cs184.jcd.finalproject;

import android.app.Activity;
import android.nfc.Tag;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class CalendarActivity extends Activity implements AdapterView.OnItemSelectedListener {
    Calendar mCalendar;
    private FirebaseAuth mAuth;
    FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
    String userID = user.getUid();
    DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("user");
    DatabaseReference currentUser = mRef.child(userID);
    String pref;
    TextView mTextView;
    private Spinner mSpinner;
    //DatabaseReference userPref = currentUser.child("preference");

    //String pref = userPref.toString();
    private static final String TAG = LoginActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        mAuth = FirebaseAuth.getInstance();
        //Toast.makeText(getApplicationContext(), pref + " User Preference", Toast.LENGTH_SHORT);
        currentUser.child("preference").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Toast.makeText(getApplicationContext(), dataSnapshot.getValue().toString(), Toast.LENGTH_SHORT);
                pref = dataSnapshot.getValue().toString();
                mTextView = (TextView) findViewById(R.id.textView3);
                String message = "Currently displaying ";
                int intPref = Integer.parseInt(pref);
                if (intPref == 1){
                    message = message + "All Events";
                }
                else if (intPref == 2){
                    message = message + "Official Events";
                }
                else if (intPref == 3){
                    message = message + "User Events";

                }
                mTextView.setText(message);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mSpinner = (Spinner) findViewById(R.id.spinner2);
        mSpinner.setOnItemSelectedListener(this);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.choices_array2, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        mSpinner.setAdapter(adapter);

    }

    public void updatePreference(View view) {

    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
        if(pos != 0) {
            String newPref = Integer.toString(pos);
            currentUser.child("preference").setValue(newPref);
            mSpinner.setSelection(0);
        }

    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Hide SignUpButton

    }
}
