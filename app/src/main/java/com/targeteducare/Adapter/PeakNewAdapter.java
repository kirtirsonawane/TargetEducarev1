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
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.targeteducare.Classes.Course_New;
import com.targeteducare.Classes.PeakNew_List;
import com.targeteducare.InternetUtils;
import com.targeteducare.PracticeTestSelectActivity;
import com.targeteducare.R;

import java.util.ArrayList;

public class PeakNewAdapter extends RecyclerView.Adapter<PeakNewAdapter.MyViewHolder> {

    Context context;
    ArrayList<PeakNew_List> peakNew_lists = new ArrayList<>();
    Course_New course_new;
    String lang = "";
    SharedPreferences preferences;
    SharedPreferences.Editor edit;

    public PeakNewAdapter(Context context, Course_New course_new, String lang) {
        this.context = context;
        this.course_new = course_new;
        this.peakNew_lists = course_new.getPeakNew_lists();
        this.lang = lang;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(context).inflate(R.layout.peak_new_select_layout, viewGroup, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        edit = preferences.edit();
        return myViewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        try {
            if (lang.equalsIgnoreCase("mr")) {
                myViewHolder.tv_peak.setText(peakNew_lists.get(i).getPeakno_InMarathi());

            } else {
                myViewHolder.tv_peak.setText(peakNew_lists.get(i).getPeakno());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            if (peakNew_lists.get(i).getIsExams() == 0) {
                myViewHolder.imageview_forward.setBackgroundResource(R.drawable.offline_textview);
            } else {
                myViewHolder.imageview_forward.setBackgroundResource(R.drawable.online_textview);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        /*AlphaAnimation fadeIn = new AlphaAnimation(0.0f , 1.0f ) ;
        myViewHolder.imageview_forward.startAnimation(fadeIn);
        AlphaAnimation fadeOut = new AlphaAnimation( 1.0f , 0.0f ) ;
        myViewHolder.imageview_forward.startAnimation(fadeOut);
        fadeIn.setDuration(500);
        fadeOut.setDuration(1200);
        fadeOut.setStartOffset(1200+fadeIn.getStartOffset()+1200);*/

        /*try {
            final Animation myAnim = AnimationUtils.loadAnimation(context, R.anim.swipe);
            myViewHolder.imageview_forward.setAnimation(myAnim);
            myAnim.setRepeatCount(2000);
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    if (!InternetUtils.getInstance(context).available()) {
                        try {
                            if (preferences.contains("selectedcourse")) {
                                if (course_new.getName().equalsIgnoreCase(preferences.getString("selectedcourse", "")) ||
                                        course_new.getName_InMarathi().equalsIgnoreCase(preferences.getString("selectedcourse", ""))) {
                                    if (preferences.contains("selectedpeak")) {
                                        if (peakNew_lists.get(i).getPeakno_InMarathi().equalsIgnoreCase(preferences.getString("selectedpeak", "")) ||
                                                peakNew_lists.get(i).getPeakno().equalsIgnoreCase(preferences.getString("selectedpeak", ""))) {
                                            if (peakNew_lists.get(i).getIsExams() == 1) {
                                                ((PracticeTestSelectActivity) context).gotofragmentactivity(course_new.getCourseid(), peakNew_lists.get(i).getPeakno());
                                            } else {
                                                Toast.makeText(context, context.getResources().getString(R.string.no_exam_available), Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            Toast.makeText(context, context.getResources().getString(R.string.no_offline_data_available), Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                } else {
                                    Toast.makeText(context, context.getResources().getString(R.string.no_offline_data_available), Toast.LENGTH_SHORT).show();
                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        if (lang.equalsIgnoreCase("mr")) {
                            edit.putString("selectedpeak", peakNew_lists.get(i).getPeakno_InMarathi());
                            edit.putString("selectedcourse", course_new.getName_InMarathi());

                        } else {
                            edit.putString("selectedpeak", peakNew_lists.get(i).getPeakno());
                            edit.putString("selectedcourse", course_new.getName());
                        }
                        edit.apply();

                        if (peakNew_lists.get(i).getIsExams() == 1) {
                            ((PracticeTestSelectActivity) context).gotofragmentactivity(course_new.getCourseid(), peakNew_lists.get(i).getPeakno());
                        } else {
                            Toast.makeText(context, context.getResources().getString(R.string.no_exam_available), Toast.LENGTH_SHORT).show();
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return peakNew_lists.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_peak;
        ImageView imageview_forward;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_peak = itemView.findViewById(R.id.tv_peak);
            imageview_forward = itemView.findViewById(R.id.imageview_forward);
        }
    }
}
