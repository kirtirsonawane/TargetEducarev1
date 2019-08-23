package com.targeteducare.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.targeteducare.Activitycommon;
import com.targeteducare.Classes.EventModel;
import com.targeteducare.EventFeedbackActivity;
import com.targeteducare.R;

import java.util.ArrayList;


public class EventAdapter extends RecyclerView.Adapter<EventAdapter.MyViewHolder> {

    Context context;
    ArrayList<EventModel> eventModels = new ArrayList<>();
    String lang = "";

    SharedPreferences preferences;
    SharedPreferences.Editor edit;

    public EventAdapter(Context context, ArrayList<EventModel> eventModels, String lang) {

        try {

            this.context = context;
            this.eventModels = eventModels;
            this.lang = lang;
            preferences = PreferenceManager.getDefaultSharedPreferences(context);
            edit = preferences.edit();



        } catch (Exception e) {
            ((Activitycommon) context).reporterror("EventAdapter", e.toString());
            e.printStackTrace();
        }

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.event_content_layout, viewGroup, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int i) {

        try {

            myViewHolder.tv_eventdate.setText(context.getResources().getString(R.string.event_date) + ": " + eventModels.get(i).getEventDate());

            if (lang.equalsIgnoreCase("mr")) {
                myViewHolder.tv_eventname.setText(eventModels.get(i).getTitle_Marathi());
            } else {
                myViewHolder.tv_eventname.setText(eventModels.get(i).getTitle());
            }

            if(eventModels.get(i).getIsSubmitFeedback().equalsIgnoreCase("1")){
                myViewHolder.tv_feedback.setVisibility(View.GONE);
            }
            else {
                myViewHolder.tv_feedback.setVisibility(View.VISIBLE);
            }

            myViewHolder.tv_feedback.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {

                        ((EventFeedbackActivity) context).gotoOnFeedbackclick(i);

                    } catch (Exception e) {
                        ((Activitycommon) context).reporterror("EventAdapter", e.toString());
                        e.printStackTrace();
                    }
                }
            });

        } catch (Exception e) {
            ((Activitycommon) context).reporterror("EventAdapter", e.toString());
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return eventModels.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_eventdate, tv_eventname, tv_feedback;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_eventdate = itemView.findViewById(R.id.tv_eventdate);
            tv_eventname = itemView.findViewById(R.id.tv_eventname);
            tv_feedback = itemView.findViewById(R.id.tv_feedback);

        }
    }
}
