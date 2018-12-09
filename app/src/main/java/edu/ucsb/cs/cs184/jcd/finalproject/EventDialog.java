package cs.cs184.e_khurelbaatarEventCalendar;


import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class EventDialog extends DialogFragment {

    String[] events ={"Dance", "Concert" , "StudyHall", "Club Meeting"};
    RecyclerView rv;
    MyAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View rootView = inflater.inflate(R.layout.event_dialog, container);
        rv = rootView.findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(this.getActivity()));

        //adapter
        adapter = new MyAdapter(this.getActivity(), events);
        rv.setAdapter(adapter);

        this.getDialog().setTitle("List of Events");
        return rootView;
    }
}




