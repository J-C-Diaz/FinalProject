package edu.ucsb.cs.cs184.jcd.finalproject;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.DialogFragment;
import android.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import java.util.Calendar;


public class DatePickerFrag extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
            Intent intent = new Intent("date_picker");
            intent.putExtra("year", year);
            LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
            intent.putExtra("month", month);
            LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
            intent.putExtra("day", day);
            LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);

    }
}