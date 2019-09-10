package com.targeteducare;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.targeteducare.Adapter.Course_New_Adapter;
import com.targeteducare.Adapter.PeakNewAdapter;
import com.targeteducare.Classes.Course_New;
import com.targeteducare.database.DatabaseHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class PracticeTestSelectActivity extends Activitycommon {

    RecyclerView recyclerView, recycler_view_peak;
    Course_New_Adapter course_new_adapter;
    RecyclerView.LayoutManager layoutManager, layoutManager1;
    ArrayList<Course_New> course_new = new ArrayList<>();
    PeakNewAdapter peakNewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            screenshot_capture_permission();
            super.onCreate(savedInstanceState);
            loadLocale();
            setContentView(R.layout.activity_practice_test_select);
            setmaterialDesign();
            back();

            recyclerView = findViewById(R.id.recycler_course);
            layoutManager = new LinearLayoutManager(PracticeTestSelectActivity.this);
            recyclerView.setLayoutManager(layoutManager);

            recycler_view_peak = findViewById(R.id.recycler_view_peak);
            layoutManager1 = new LinearLayoutManager(PracticeTestSelectActivity.this);
            recycler_view_peak.setLayoutManager(layoutManager1);

            course_new_adapter = new Course_New_Adapter(PracticeTestSelectActivity.this, course_new, lang);
            recyclerView.setAdapter(course_new_adapter);
            course_new_adapter.notifyDataSetChanged();

            try {

                JSONObject obj = new JSONObject();
                JSONObject mainobj = new JSONObject();
                obj.put("studentid", GlobalValues.student.getId());
                mainobj.put("FilterParameter", obj.toString());
                ConnectionManager.getInstance(PracticeTestSelectActivity.this).getvalidcourse(mainobj.toString());

            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    protected void onResponsed(int statuscode, int accesscode, String data) {
        super.onResponsed(statuscode, accesscode, data);

        if (statuscode == Constants.STATUS_OK) {

            if (accesscode == Connection.GET_VALIDCOURSE.ordinal()) {

                try {
                    //Log.e("response ", GlobalValues.TEMP_STR);
                    course_new.clear();
                    JSONObject object = new JSONObject(GlobalValues.TEMP_STR);
                    JSONObject root = object.optJSONObject("root");

                    Object json = root.get("subroot");


                    if (json instanceof JSONArray) {
                        JSONArray subroot = root.optJSONArray("subroot");

                        for (int i = 0; i < subroot.length(); i++) {
                            JSONObject obj = subroot.optJSONObject(i);
                            course_new.add(new Course_New(obj));
                        }

                    } else {

                        course_new.add(new Course_New(root.optJSONObject("subroot")));

                    }

                    course_new_adapter.notifyDataSetChanged();

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (accesscode == Connection.NoExamAvailable_course.ordinal()) {
                try {
                    if (!((Activity) context).isFinishing()) {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.nodata), Toast.LENGTH_LONG).show();
                    } else {
                        //Log.e("no activity", "no activity ");
                    }

                } catch (Exception e) {
                    reporterror(tag, e.toString());
                    e.printStackTrace();
                }
            } else if (accesscode == Connection.GET_VALIDCOURSE_EXCEPTION.ordinal()) {

                try {
                    course_new.clear();
                    JSONArray array = DatabaseHelper.getInstance(PracticeTestSelectActivity.this).get_courseandpeak();
                    JSONObject obj = array.optJSONObject(0);

                    String a = obj.optString("JSONDATA");
                    JSONArray jsonArr = new JSONArray(a);

                    for (int i = 0; i < jsonArr.length(); i++) {

                        JSONObject a1 = jsonArr.optJSONObject(i);
                        course_new.add(new Course_New(a1));
                    }

                    course_new_adapter.notifyDataSetChanged();


                } catch (Exception e) {
                    reporterror(tag, e.toString());
                    e.printStackTrace();
                }
            }

        }
    }

    public void gotofragmentactivity(int courseid, String peakno) {


        try {
            Intent i = new Intent(PracticeTestSelectActivity.this, PracticeTestActivity.class);
            i.putExtra("courseid", courseid);
            i.putExtra("peakno", peakno);
            startActivity(i);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /*public void gotopeakadapter(Course_New course_new, int i) {

        if(i == 0){
            recycler_view_peak.setVisibility(View.GONE);
        }else{

            peakNewAdapter = new PeakNewAdapter(PracticeTestSelectActivity.this, course_new, lang);
            recycler_view_peak.setAdapter(peakNewAdapter);
            peakNewAdapter.notifyDataSetChanged();
            recycler_view_peak.setVisibility(View.VISIBLE);
        }
    }*/
}
