package edu.ucsb.cs.cs184.sonam.firebasetester;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TimePicker;

import java.util.Calendar;



public class TimePickerFrag extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {

    private String textVer = null;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // Do something with the time chosen by the user
        Intent intent = new Intent("time_picker");
        intent.putExtra("hour", hourOfDay);
        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
        intent.putExtra("minute", minute);
        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);

        //Fragment currentFragment = getChildFragmentManager().findFragmentById(R.id.timePick);
        //Fragment currentFragment = getChildFragmentManager().findFragmentByTag("finishTimePicker");

//        if(textVer.equals("s")){
//            intent.putExtra("textVer", "s");
//            LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
//        }
//        if(textVer.equals("f")){
//            intent.putExtra("textVer", "f");
//            LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
//        }

    }
}
