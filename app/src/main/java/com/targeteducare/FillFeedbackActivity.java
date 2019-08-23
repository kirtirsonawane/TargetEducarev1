package com.targeteducare;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.targeteducare.Adapter.FillFeedbackAdapter;
import com.targeteducare.Classes.EventModel;
import com.targeteducare.Classes.FeedbackQuestions;
import com.targeteducare.database.DatabaseHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class FillFeedbackActivity extends Activitycommon {


    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    LinearLayout layout;
    ArrayList<FeedbackQuestions> feedbackQuestions = new ArrayList<>();
    EventModel eventModel;
    FillFeedbackAdapter fillFeedbackAdapter;
    Button button_submitfeedback;
    boolean flag = false;

    SharedPreferences preferences;
    SharedPreferences.Editor edit;
    String tag = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        try {
            screenshot_capture_permission();
            super.onCreate(savedInstanceState);
            loadLocale();
            setContentView(R.layout.activity_fill_feedback);
            setmaterialDesign();
            back();
            setTitle(getResources().getString(R.string.feedback));
            tag = this.getClass().getSimpleName();
            try {
                lang = getSharedPreferences("Settings", Activity.MODE_PRIVATE).getString("Current Language", "");
                tag = this.getClass().getSimpleName();

                preferences = PreferenceManager.getDefaultSharedPreferences(FillFeedbackActivity.this);
                edit = preferences.edit();

                recyclerView = findViewById(R.id.recycler_viewforquestion);
                button_submitfeedback = findViewById(R.id.button_submitfeedback);

                try {
                    eventModel = (EventModel) getIntent().getSerializableExtra("eventfeedbackdata");

                } catch (Exception e) {
                    reporterror(tag, e.toString());
                    e.printStackTrace();
                }

                layoutManager = new LinearLayoutManager(FillFeedbackActivity.this);
                recyclerView.setLayoutManager(layoutManager);


                try {

                    JSONObject obj = new JSONObject();
                    JSONObject mainobj = new JSONObject();

                    obj.put("EventId", eventModel.getId());
                    mainobj.put("FilterParameter", obj.toString());

                    ConnectionManager.getInstance(FillFeedbackActivity.this).getfill_feedback(mainobj.toString());

                } catch (Exception e) {
                    reporterror(tag, e.toString());
                    e.printStackTrace();
                }

                button_submitfeedback.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        try {

                            JSONObject root = new JSONObject();
                            JSONObject subroot = new JSONObject();
                            JSONObject xml1 = new JSONObject();
                            JSONArray xml = new JSONArray();

                            StringBuilder b = new StringBuilder();
                            for (int k = 0; k < feedbackQuestions.size(); k++) {


                                if (feedbackQuestions.get(k).isAnswered()) {
                                    JSONObject data = new JSONObject();
                                    data.put("EventId", eventModel.getId());
                                    data.put("StudentId", GlobalValues.student.getId());
                                    data.put("Qid", feedbackQuestions.get(k).getId());
                                    JSONObject answer = new JSONObject();
                                    b.delete(0, b.length());

                                    for (int l = 0; l < feedbackQuestions.get(k).getFeedbackAnswers().size(); l++) {

                                        if (feedbackQuestions.get(k).getFeedbackAnswers().get(l).isSelected()) {

                                            if (lang.equalsIgnoreCase("mr")) {

                                                if (feedbackQuestions.get(k).getType() == 1) {
                                                    b.append(feedbackQuestions.get(k).getFeedbackAnswers().get(l).getAns());
                                                } else {
                                                    b.append(feedbackQuestions.get(k).getFeedbackAnswers().get(l).getAns() + ",");
                                                }

                                            } else {

                                                if (feedbackQuestions.get(k).getType() == 1) {
                                                    b.append(feedbackQuestions.get(k).getFeedbackAnswers().get(l).getAns());
                                                } else {
                                                    b.append(feedbackQuestions.get(k).getFeedbackAnswers().get(l).getAns());
                                                    b.append(",");
                                                    Log.e("b ", b.toString());
                                                }

                                            }
                                        }
                                        String s = b.toString();
                                        data.put("Answers", s);
                                    }
                                    xml.put(data);
                                }

                            }

                            subroot.put("subroot", xml);
                            root.put("root", subroot);
                            xml1.put("xml", root.toString());

                            JSONObject mainobj = new JSONObject();
                            mainobj.put("FilterParameter", xml1.toString());

                            Log.e("mainobj ", mainobj.toString());

                            ConnectionManager.getInstance(FillFeedbackActivity.this).save_questionFeedback(mainobj.toString());


                            eventModel.setIsSubmitFeedback("1");

                            ContentValues data = new ContentValues();
                            data.put(DatabaseHelper.JSONDATA, mainobj.toString());
                            data.put(DatabaseHelper.EVENT_ID, eventModel.getId());
                            data.put(DatabaseHelper.IS_SUBMIT, eventModel.getIsSubmitFeedback());
                            data.put(DatabaseHelper.SAVEDTIME, DateUtils.getSqliteTime());
                            DatabaseHelper.getInstance(context).save_feedback_Details(data, eventModel.getId());

                            try {
                                Intent intent = new Intent(Constants.BROADCAST_WIZARD);
                                intent.putExtra(Constants.BROADCAST_RESPONSE_CODE, 200);
                                intent.putExtra(Constants.BROADCAST_URL_ACCESS, Connection.Dataupdated.ordinal());
                                intent.putExtra(Constants.BROADCAST_DATA, eventModel.getId());
                                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            Toast.makeText(context, getResources().getString(R.string.thank_you), Toast.LENGTH_SHORT).show();
                            FillFeedbackActivity.this.finish();

                        } catch (Exception e) {
                            reporterror(tag, e.toString());
                            e.printStackTrace();
                        }
                    }
                });

            } catch (Exception e) {
                reporterror(tag, e.toString());
                e.printStackTrace();
            }

        } catch (Exception e) {
            reporterror(tag, e.toString());
            e.printStackTrace();
        }
    }

    @Override
    protected void onResponsed(int statuscode, int accesscode, String data) {
        super.onResponsed(statuscode, accesscode, data);

        if (statuscode == Constants.STATUS_OK) {

            if (accesscode == Connection.GET_FEEDBACK.ordinal()) {

                try {

                    JSONObject object = new JSONObject(GlobalValues.TEMP_STR);
                    JSONObject root = object.getJSONObject("root");


                    if (root != null) {

                        Object subrootchk = root.get("subroot");

                        if (subrootchk instanceof JSONArray) {

                            JSONArray subroot = root.getJSONArray("subroot");

                            for (int i = 0; i < subroot.length(); i++) {

                                feedbackQuestions.add(new FeedbackQuestions(subroot.getJSONObject(i)));

                            }

                        } else {

                            feedbackQuestions.add(new FeedbackQuestions(object.getJSONObject("subroot")));
                        }

                        fillFeedbackAdapter = new FillFeedbackAdapter(FillFeedbackActivity.this, feedbackQuestions, lang);
                        recyclerView.setAdapter(fillFeedbackAdapter);
                        fillFeedbackAdapter.notifyDataSetChanged();

                    }

                } catch (Exception e) {
                    reporterror(tag, e.toString());
                    e.printStackTrace();
                }

            } else if (accesscode == Connection.GET_FEEDBACKEXCEPTION.ordinal()) {
                Toast.makeText(getApplicationContext(), FillFeedbackActivity.this.getResources().getString(R.string.Connectiontimeout), Toast.LENGTH_LONG).show();
            }

        }
    }

}
