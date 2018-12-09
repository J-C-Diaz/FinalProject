package cs.cs184.e_khurelbaatarEventCalendar;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MyAdapter extends RecyclerView.Adapter<MyHolder> {



    Context c;
    String[] events;
    //HashMap<Date,Event> events; //event object MAKE EVENT CLASS
    //ArrayList<String> events;

    public MyAdapter(Context c, String[] events) {
        this.c = c;
        this.events = events;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.model, parent, false);
        MyHolder holder = new MyHolder(v);

        final Dialog myDialog = new Dialog(parent.getContext());
        myDialog.setContentView(R.layout.details_dialog);
        TextView nameTxt = myDialog.findViewById(R.id.nameTxt);
        TextView time = myDialog.findViewById(R.id.timeTxt);
        TextView location = myDialog.findViewById(R.id.locationTxt);
        //nameTxt.setText();
        //time.setText();
        //location.setText();
        holder.nameTxt.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                myDialog.show();

            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        //loop through events object and get event title
       // for(Map.Entry<Date, Event> e: events.entrySet()){
            //holder.nameTxt.setText(e.getValue().title);
        //}
        holder.nameTxt.setText(events[position]);

        //holder.nameTxt.setText(events.getName());
        //holder.locationTxt.setText(events.getLocation());
        //holder.startTime.setText(events.getStartTime());
        //holder.endTime.setText(events.getEndTime());


    }

    @Override
    public int getItemCount() {
        return events.length;
        //events.size();
    }
}
