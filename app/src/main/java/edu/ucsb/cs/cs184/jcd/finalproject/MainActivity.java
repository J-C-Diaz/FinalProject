package edu.ucsb.cs.cs184.jcd.finalproject;;

import android.app.FragmentManager;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentTransaction;
import android.app.FragmentTransaction;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import edu.ucsb.cs.cs184.jcd.finalproject.FirebaseHelper;

import static android.content.ContentValues.TAG;


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, AddEventFrag.OnFragmentInteractionListener {
	Calendar mCalendar;
	private FirebaseAuth mAuth;
	FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
	String userID = user.getUid();
	DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("user");
	DatabaseReference currentUser = mRef.child(userID);

	//TODO: pass this badboy to FirebaseHelper
	DatabaseReference eventSet = FirebaseDatabase.getInstance().getReference("events");
	ArrayList<Event> events = new ArrayList<>();
	
	//FirebaseHelper fbHelper = new FirebaseHelper(eventSet);

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



		final HashSet<Date> eventsR = new HashSet<>();
		eventsR.add(new Date());

		final FragmentManager fm = getFragmentManager();
		final EventDialog eventDialog = new EventDialog();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
				Toast myToast = Toast.makeText(getApplicationContext(),
						"Starting Add Event Fragment",
						Toast.LENGTH_SHORT);
				myToast.show();
				AddEventFrag eFrag = AddEventFrag.newInstance();
				eFrag.setArguments(getIntent().getExtras());
				View view1 = findViewById(R.id.childMain);
				view1.setVisibility(View.INVISIBLE);
				//FragmentManager fragManager = getSupportFragmentManager();
				FragmentTransaction transaction = fm.beginTransaction();
				transaction.replace(R.id.mainFrame, eFrag).commit();

			}
        });




		final edu.ucsb.cs.cs184.jcd.finalproject.CalendarView cv = findViewById(R.id.calendar_view);
		cv.updateCalendar(eventsR);

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
				cv.updateCalendar(eventsR);

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

		eventSet.addChildEventListener(new ChildEventListener() {
			@Override
			public void onChildAdded(DataSnapshot dataSnapshot, String s) {
				fetchData(dataSnapshot);
				Log.d(TAG, "Here1" + Integer.toString(events.size()));
			}

			@Override
			public void onChildChanged(DataSnapshot dataSnapshot, String s) {
				Log.d(TAG, "CHECK123" + dataSnapshot.child("date").toString());
				fetchData(dataSnapshot);

			}

			@Override
			public void onChildRemoved(DataSnapshot dataSnapshot) {

				fetchData(dataSnapshot);
			}

			@Override
			public void onChildMoved(DataSnapshot dataSnapshot, String s) {

			}

			@Override
			public void onCancelled(DatabaseError databaseError) {

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

//		fbHelper.retrieve();
//		fbHelper.retrieve();
//		ArrayList<Event> eventi = fbHelper.events;
//		Log.d(TAG, "EVENTSIZE" + Integer.toString(eventi.size()));
//
//		if(!eventi.isEmpty()){
//			for(Event e : eventi){
//
//				Log.d("Frick", e.title + " " + e.date);
//			}
//		}
	}

	private Event fetchData(DataSnapshot dataSnapshot)
	{
		//events.clear();



		String finish = dataSnapshot.child("time").child("finish").getValue().toString();
		String start = dataSnapshot.child("time").child("start").getValue().toString();
		String date = dataSnapshot.child("date").getValue().toString();
		String location = dataSnapshot.child("location").getValue().toString();
		String title = dataSnapshot.child("title").getValue().toString();

		Log.d(TAG, "Here2" + dataSnapshot.child("date").toString());

		Event e = new Event(title, location, date, start, finish);

		Log.d(TAG, "Here3" + dataSnapshot.child("date").toString());

		if(!events.contains(e)) {
			events.add(e);
		}
		Log.d(TAG, "Here4" + dataSnapshot.child("date").toString());
		Log.d(TAG, "EVENTSIZEE " + Integer.toString(events.size()));
/*
        for (DataSnapshot ds : dataSnapshot.getChildren())
        {
            Log.d(TAG, "made it");
            if(!(ds==null)) {

                events.add(e);
            }
        }
        for(Event e : events){
            Log.d("Frick", e.title + " " + e.date);
        }*/
		return e;
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

	@Override
	public void onFragmentInteraction(Uri uri){

	}
}
