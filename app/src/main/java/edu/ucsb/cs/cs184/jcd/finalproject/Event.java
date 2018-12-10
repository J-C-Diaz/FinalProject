package edu.ucsb.cs.cs184.sonam.firebasetester;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Event {
    public String title;
    public String location;
    public String date;
    public Time time;



    public Event(){

    }

    public Event(String title, String location, String date, String start, String finish){
        this.title = title;
        this.location = location;
        this.date = date;
        this.time = new Time(start, finish);


    }

}

class Time{
    public String start;
    public String finish;

    public Time(String start, String finish){
        this.start = start;
        this.finish = finish;
    }
}
