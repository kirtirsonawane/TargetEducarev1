package com.targeteducare.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.targeteducare.Activitycommon;
import com.targeteducare.Classes.Course;
import com.targeteducare.R;

import java.util.ArrayList;

public class CoursesAdapter extends BaseAdapter {
    Context context;
    ArrayList<Course> mdataset;
    LayoutInflater inflater;
    String lang = "mr";

    public CoursesAdapter(Context applicationContext, ArrayList<Course> mdataset, String lang) {
        try {
            this.context = applicationContext;
            inflater = (LayoutInflater.from(applicationContext));
            this.mdataset = mdataset;
            this.lang = lang;

        } catch (Exception e) {
            ((Activitycommon) context).reporterror("CourseAdapter", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public int getCount() {
        return mdataset.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.custom_spinner_item, null);

        try {
            TextView names = (TextView) view.findViewById(R.id.textview_1);
            Log.e("mdataset.get(i)", "akskasl" + mdataset.get(i).getCourse_name());
            if (lang.equalsIgnoreCase("mr"))
                names.setText(mdataset.get(i).getCoursename_inmarathi());
            else names.setText(mdataset.get(i).getCourse_name());
        } catch (Exception e) {
            ((Activitycommon) context).reporterror("CourseAdapter", e.toString());
            e.printStackTrace();
        }

        return view;
    }
}
