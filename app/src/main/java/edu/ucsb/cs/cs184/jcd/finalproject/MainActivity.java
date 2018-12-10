package edu.ucsb.cs.cs184.jcd.finalproject;;

import android.app.FragmentManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
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
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);


		final HashSet<Date> events = new HashSet<>();
		events.add(new Date());

		final FragmentManager fm = getFragmentManager();
		final EventDialog eventDialog = new EventDialog();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });




		final edu.ucsb.cs.cs184.jcd.finalproject.CalendarView cv = findViewById(R.id.calendar_view);
		cv.updateCalendar(events);

		// assign event handler
		cv.setEventHandler(new CalendarView.EventHandler()
		{
			@Override
			public void onDayLongPress(Date date)
			{
                eventDialog.show(fm,"Events_tag");

				// show returned day
				//DateFormat df = SimpleDateFormat.getDateInstance();
				//Toast.makeText(MainActivity.this, df.format(date), Toast.LENGTH_SHORT).show();
			}

			@Override
			public void setEvents() {
				cv.updateCalendar(events);

			}
		});

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

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		//if (id == R.id.action_settings)
		//{
		//	return true;
		//}

		return super.onOptionsItemSelected(item);
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
