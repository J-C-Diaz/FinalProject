package edu.ucsb.cs.cs184.jcd.finalproject;

import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class FirebaseHelper {

    DatabaseReference db;
    Boolean saved=null;
    ArrayList<Event> events;

    public FirebaseHelper(DatabaseReference db) {
        this.db = db;
        events=new ArrayList<>();
    }

    //SAVE
//    public Boolean save(Event event)
//    {
//        if(event==null)
//        {
//            saved=false;
//        }else {
//
//            try
//            {
//                db.child("").push().setValue(spacecraft);
//                saved=true;
//            }catch (DatabaseException e)
//            {
//                e.printStackTrace();
//                saved=false;
//            }
//
//        }
//
//        return saved;
//    }

    //READ
    public void retrieve()
    {


        db.addChildEventListener(new ChildEventListener() {
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

}