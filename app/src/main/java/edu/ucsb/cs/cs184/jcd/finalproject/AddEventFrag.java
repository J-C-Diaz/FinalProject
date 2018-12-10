package edu.ucsb.cs.cs184.sonam.firebasetester;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddEventFrag.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddEventFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddEventFrag extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
    public static final int TYPE_CLASS_DATETIME = 0x00000004;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private EditText t;
    private EditText l;
    private EditText d;
    private EditText s;
    private EditText f;

    private String selected = " ";

    public DatabaseReference mDatabase;

    private OnFragmentInteractionListener mListener;

    public AddEventFrag() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static AddEventFrag newInstance() {
        AddEventFrag fragment = new AddEventFrag();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
        //fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(receiver,
                new IntentFilter("date_picker"));LocalBroadcastManager.getInstance(getContext()).registerReceiver(receiver,
                new IntentFilter("date_picker"));

        LocalBroadcastManager.getInstance(getContext()).registerReceiver(receiver,
                new IntentFilter("time_picker"));LocalBroadcastManager.getInstance(getContext()).registerReceiver(receiver,
                new IntentFilter("time_picker"));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View fragmentView = inflater.inflate(R.layout.fragment_add_event, container, false);
        // Inflate the layout for this fragment
        t = (TextInputEditText)fragmentView.findViewById(R.id.titleText);
        l = (TextInputEditText)fragmentView.findViewById(R.id.locationText);
        d = (TextInputEditText)fragmentView.findViewById(R.id.dateText);
        d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(v);
            }
        });
        s = (TextInputEditText)fragmentView.findViewById(R.id.startTimeText);
        s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selected = "s";
                showStartTimePickerDialog(v);
            }
        });
        f = (TextInputEditText)fragmentView.findViewById(R.id.finishTimeText);
        f.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selected = "f";
                showFinishTimePickerDialog(v);
            }
        });

        mDatabase = FirebaseDatabase.getInstance().getReference();


        final Button submitButton = (Button) fragmentView.findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(t.getText().toString().isEmpty()){
                    t.setError( "Event title is required!" );
                }
                if(l.getText().toString().isEmpty()){
                    l.setError( "Event location is required!" );
                }
                if(d.getText().toString().isEmpty()){
                    d.setError( "Event date is required!" );
                }
                if(s.getText().toString().isEmpty()){
                    s.setError( "Event start time is required!" );
                }
                if(f.getText().toString().isEmpty()){
                    f.setError( "Event finish time is required!" );
                }
                else{
                    submitEvent();
                }
            }
        });

        return fragmentView;
    }


    public void showStartTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFrag();
        newFragment.show(getFragmentManager(), "startTimePicker");
    }

    public void showFinishTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFrag();
        newFragment.show(getFragmentManager(), "finishTimePicker");
    }
    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFrag();
        newFragment.show(getFragmentManager(), "datePicker");
    }
    private void submitEvent(){
        String title;
        String location;
        String date;
        String start;
        String finish;
        title = t.getText().toString();
        location = l.getText().toString();
        date = d.getText().toString();
        start = s.getText().toString();
        finish = f.getText().toString();

        //TODO: CALLL FUNCTION TO RESPOND TO UPDATE DATABASE
        writeNewEvent(title, location, date, start, finish);
    }

    public void writeNewEvent(String title, String location, String date, String start, String finish){
        Event e = new Event(title, location, date, start, finish);
        mDatabase.child("events").child(title).setValue(e);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals("date_picker")){
                int year = intent.getIntExtra("year",0);
                int month = intent.getIntExtra("month", 0);
                month = month+1;
                int day = intent.getIntExtra("day", 0);
                String date = Integer.toString(month) + "/" + Integer.toString(day) + "/" + Integer.toString(year);
                d.setText(date ,TextView.BufferType.EDITABLE);
            }

            if(intent.getAction().equals("time_picker")) {
                int hour = intent.getIntExtra("hour", 0);
                int minute = intent.getIntExtra("minute",0);
                String time = Integer.toString(hour) + ":" + Integer.toString(minute);
                //String textVer = intent.getStringExtra("textVer");
                if( selected.equals("s") ){
                    s.setText(time, TextView.BufferType.EDITABLE);
                }
                if( selected.equals("f")){
                    f.setText(time, TextView.BufferType.EDITABLE);
                }
            }
        }
    };

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
