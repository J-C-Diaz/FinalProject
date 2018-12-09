package edu.ucsb.cs.cs184.jcd.finalproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class SelectionActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);
    }

    public void nextActivity(View view) {
        startActivity(new Intent(SelectionActivity.this, CalendarActivity.class));
    }

}