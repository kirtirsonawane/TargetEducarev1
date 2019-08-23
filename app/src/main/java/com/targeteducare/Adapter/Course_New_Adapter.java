package com.targeteducare.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.targeteducare.Classes.Course_New;
import com.targeteducare.Classes.PeakNew_List;
import com.targeteducare.R;

import java.util.ArrayList;

public class Course_New_Adapter extends RecyclerView.Adapter<Course_New_Adapter.MyViewHolder> {

    Context context;
    ArrayList<Course_New> course_new = new ArrayList<>();
    String lang = "";
    RecyclerView.LayoutManager layoutManager;
    int initial = 0;


    public Course_New_Adapter(Context context, ArrayList<Course_New> course_new, String lang) {
        this.context = context;
        this.course_new = course_new;
        this.lang = lang;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(context).inflate(R.layout.course_new_select_layout, viewGroup, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {

        try {
            layoutManager = new LinearLayoutManager(context);
            myViewHolder.recycler_view_peak.setHasFixedSize(true);
            myViewHolder.recycler_view_peak.setLayoutManager(layoutManager);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (lang.equalsIgnoreCase("mr")) {
                myViewHolder.tv_course.setText(course_new.get(i).getName_InMarathi());
            } else {
                myViewHolder.tv_course.setText(course_new.get(i).getName());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (initial == 0) {
            course_new.get(0).setFlag(1);
            myViewHolder.iv_arrow.setBackgroundResource(R.drawable.ic_keyboard_arrow_up_white_24dp);
            myViewHolder.recycler_view_peak.setVisibility(View.VISIBLE);
            PeakNewAdapter adapter = new PeakNewAdapter(context, course_new.get(i), lang);
            myViewHolder.recycler_view_peak.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            initial = 1;
        }


        myViewHolder.linearlayout_course.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final PeakNewAdapter adapter = new PeakNewAdapter(context, course_new.get(i), lang);
                myViewHolder.recycler_view_peak.setAdapter(adapter);
//                adapter.notifyDataSetChanged();

                try {
                    if (course_new.get(i).getFlag() == 0) {
                        myViewHolder.iv_arrow.setBackgroundResource(R.drawable.ic_keyboard_arrow_up_white_24dp);
                        myViewHolder.recycler_view_peak.setVisibility(View.VISIBLE);
                        course_new.get(i).setFlag(1);
                        myViewHolder.recycler_view_peak.refreshDrawableState();

                    } else {
                        myViewHolder.iv_arrow.setBackgroundResource(R.drawable.ic_keyboard_arrow_down_white_24dp);
                        myViewHolder.recycler_view_peak.setVisibility(View.GONE);
                        course_new.get(i).setFlag(0);
                        adapter.notifyDataSetChanged();
                        myViewHolder.recycler_view_peak.refreshDrawableState();
                    }
//                  adapter.notifyDataSetChanged();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return course_new.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tv_course;
        RecyclerView recycler_view_peak;
        ImageView iv_arrow;
        LinearLayout linearlayout_course;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_course = itemView.findViewById(R.id.tv_course);
            recycler_view_peak = itemView.findViewById(R.id.recycler_view_peak);
            linearlayout_course = itemView.findViewById(R.id.linearlayout_course);
            iv_arrow = itemView.findViewById(R.id.iv_arrow);

            linearlayout_course.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int i =getAdapterPosition();
            final PeakNewAdapter adapter = new PeakNewAdapter(context,course_new.get(i), lang);
            try {
                if (course_new.get(i).getFlag() == 0) {
                    iv_arrow.setBackgroundResource(R.drawable.ic_keyboard_arrow_up_white_24dp);
                    recycler_view_peak.setVisibility(View.VISIBLE);
                    course_new.get(i).setFlag(1);

                } else {
                    iv_arrow.setBackgroundResource(R.drawable.ic_keyboard_arrow_down_white_24dp);
                    course_new.get(i).setFlag(0);
                    adapter.notifyDataSetChanged();
                }
                  adapter.notifyDataSetChanged();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        }
    }
