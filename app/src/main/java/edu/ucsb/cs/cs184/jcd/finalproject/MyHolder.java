package edu.ucsb.cs.cs184.jcd.finalproject;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;



public class MyHolder extends RecyclerView.ViewHolder {
    TextView nameTxt, timeTxt, locationTxt;


    public MyHolder(View itemView) {
        super(itemView);

        nameTxt= itemView.findViewById(R.id.nameTxt);
        timeTxt = itemView.findViewById(R.id.timeTxt);
        locationTxt = itemView.findViewById(R.id.locationTxt);


    }

}
