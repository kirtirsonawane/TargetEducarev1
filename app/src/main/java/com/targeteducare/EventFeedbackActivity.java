package com.targeteducare;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.targeteducare.Adapter.EventAdapter;
import com.targeteducare.Classes.EventModel;
import com.targeteducare.database.DatabaseHelper;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;

public class EventFeedbackActivity extends Activitycommon {

    SharedPreferences preferences;
    SharedPreferences.Editor edit;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    EventAdapter eventAdapter;
    ArrayList<EventModel> eventModels = new ArrayList<>();
    LinearLayout mainlayout;
    TextView noevents;
String tag="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        try {
            screenshot_capture_permission();
            super.onCreate(savedInstanceState);
            loadLocale();
            setContentView(R.layout.activity_event_feedback);
            setmaterialDesign();
            back();
            setTitle(getResources().getString(R.string.feedback));

            lang = getSharedPreferences("Settings", Activity.MODE_PRIVATE).getString("Current Language", "");
            tag = this.getClass().getSimpleName();

            preferences = PreferenceManager.getDefaultSharedPreferences(EventFeedbackActivity.this);
            edit = preferences.edit();

            recyclerView = findViewById(R.id.recycler_view_forevent);
            mainlayout = findViewById(R.id.mainlayout);
            noevents = findViewById(R.id.noevents);

            layoutManager = new LinearLayoutManager(EventFeedbackActivity.this);
            recyclerView.setLayoutManager(layoutManager);


            try {
                JSONObject obj = new JSONObject();
                JSONObject mainobj = new JSONObject();

                if (GlobalValues.student.getCenterId().equalsIgnoreCase("")) {
                    noevents.setVisibility(View.VISIBLE);
                } else {
                    noevents.setVisibility(View.GONE);
                    obj.put("CenterId", GlobalValues.student.getCenterId());
                    mainobj.put("FilterParameter", obj.toString());

                    ConnectionManager.getInstance(EventFeedbackActivity.this).getevent_feedback(mainobj.toString());
                }


            } catch (Exception e) {
                reporterror(tag, e.toString());
                e.printStackTrace();
            }

        } catch (Exception e) {
            reporterror(tag, e.toString());
            e.printStackTrace();
        }

    }

    public void gotoOnFeedbackclick(int i) {

        try {

            Intent i1 = new Intent(EventFeedbackActivity.this, FillFeedbackActivity.class);
            i1.putExtra("eventfeedbackdata", eventModels.get(i));
            startActivity(i1);

        } catch (Exception e) {
            reporterror(tag, e.toString());
            e.printStackTrace();
        }

    }

    @Override
    protected void onResponsed(int statuscode, int accesscode, String data) {
        super.onResponsed(statuscode, accesscode, data);

        if (statuscode == Constants.STATUS_OK) {

            if (accesscode == Connection.GETEVENTFEEDBACK.ordinal()) {

                try {

                    JSONObject obj = new JSONObject(GlobalValues.TEMP_STR);
                    JSONObject root = obj.getJSONObject("root");

                    Object json = root.get("subroot");


                    if (json instanceof JSONArray) {
                        JSONArray subroot = root.getJSONArray("subroot");

                        for (int i = 0; i < subroot.length(); i++) {
                            eventModels.add(new EventModel(subroot.getJSONObject(i)));
                        }


                    } else {
                        JSONObject subroot = root.getJSONObject("subroot");
                        eventModels.add(new EventModel(root.getJSONObject("subroot")));

                    }

                    try {

                        for (int i = 0; i < eventModels.size(); i++) {
                            JSONArray array = DatabaseHelper.getInstance(EventFeedbackActivity.this).get_feedback_details(eventModels.get(i).getId());
                            if (array.length() > 0) {

                                JSONObject a = array.getJSONObject(0);
                                eventModels.get(i).setIsSubmitFeedback(a.getString(DatabaseHelper.IS_SUBMIT));
                            }
                        }
                    } catch (Exception e) {
                        reporterror(tag, e.toString());
                        e.printStackTrace();
                    }

                    eventAdapter = new EventAdapter(EventFeedbackActivity.this, eventModels, lang);
                    recyclerView.setAdapter(eventAdapter);


                } catch (Exception e) {
                    reporterror(tag, e.toString());
                    e.printStackTrace();
                }
            }else if(accesscode==Connection.GETEVENTFEEDBACKEXCEPTION.ordinal())
            {
                Toast.makeText(getApplicationContext(),EventFeedbackActivity.this.getResources().getString(R.string.Connectiontimeout),Toast.LENGTH_LONG).show();
            } else if (accesscode == Connection.Dataupdated.ordinal()) {

                try {

                    for (int i = 0; i < eventModels.size(); i++) {
                        if (eventModels.get(i).getId().equalsIgnoreCase(data)) {
                            eventModels.get(i).setIsSubmitFeedback("1");
                            eventAdapter.notifyDataSetChanged();
                            break;
                        }
                    }
                } catch (Exception e) {
                    reporterror(tag, e.toString());
                    e.printStackTrace();
                }
            }else if(accesscode==Connection.DataupdatedEXCEPTION.ordinal())
            {
                Toast.makeText(getApplicationContext(),EventFeedbackActivity.this.getResources().getString(R.string.Connectiontimeout),Toast.LENGTH_LONG).show();
            }
        }
    }

}
