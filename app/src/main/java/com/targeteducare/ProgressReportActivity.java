package com.targeteducare;


import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.NestedScrollView;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.targeteducare.Classes.Exam;
import com.targeteducare.Classes.Options;
import com.targeteducare.Classes.Question;
import com.targeteducare.database.DatabaseHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class ProgressReportActivity extends Activitycommon {
    TextView tv_time, tv_topicname, tv_percentcovered, tv_questions, tv_correct, tv_skipped, tv_wrong, tv_accuracy, tv_speed, tv_remark;
    TextView tv_remarklight;
    Button btn_pracresumretry, btn_quit;
    //int flag_check_imagebutton = 0;
    //int flag_check_textview = 0;
    int flag_result = 0;
    int flag_time = 0;
    String time_Explanation = "";
    String time_Suggestion = "";
    String result_Explanation = "";
    String result_Suggestion = "";
    ImageButton arrow;
    TextView textView_suggstn_explntn;
    WebView webView_result, webView_time;
    TextView tv_result, tv_time1;
    LinearLayout linearLayout;
    //ScrollView scrollView;
    NestedScrollView page_scroll;
    LinearLayout linear_main;
    ProgressBar progressBar;
    Button endpractice;
    LinearLayout skiplayout;
    View skip_view;
    JSONArray examdata = new JSONArray();
    //ArrayList<Exam> examArrayList = new ArrayList<>();

    Exam exam = new Exam();
    int flag = 2;

    String data = "";

    long timetaken = 0;
    //String webData =  "<p><center><U><H2>\"+movName+\"(\"+movYear+\")</H2></U></center></p><p><strong>Director : </strong>\"+movDirector+\"</p><p><strong>Producer : </strong>\"+movProducer+\"</p><p><strong>Character : </strong>\"+movActedAs+\"</p><p><strong>Summary : </strong>\"+movAnecdotes+\"</p><p><strong>Synopsis : </strong>\"+movSynopsis+\"</p>\\n\"";
    String tag = "";

    int examid = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            screenshot_capture_permission();
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_progress_report);
            registerreceiver();
            toolbar = findViewById(R.id.toolbar);
            toolbar.setTitleTextColor(getResources().getColor(R.color.White));
            setSupportActionBar(toolbar);
            toolbar.setLogo(R.mipmap.ic_launcher);
            back();

            tag = this.getClass().getSimpleName();
        /*Bundle b = getIntent().getExtras();
        if(b!=null){
            if (b.containsKey("exam")) {
                this.exam = (Exam) b.getSerializable("exam");
                tv_topicname = findViewById(R.id.tv_topicnameselected);
                tv_topicname.setText(exam.getExamname());
            }
        }*/


            tv_time = findViewById(R.id.tv_timetaken);
            tv_remark = findViewById(R.id.tv_remark);
            tv_remarklight = findViewById(R.id.remark_light);

            skiplayout = findViewById(R.id.skiplayout);
            skip_view = findViewById(R.id.viewskip);

            tv_percentcovered = findViewById(R.id.tv_percentcovered);
            tv_questions = findViewById(R.id.tv_totalquestions);
            tv_correct = findViewById(R.id.tv_correctanswers);
            tv_skipped = findViewById(R.id.tv_skippedquestions);
            tv_wrong = findViewById(R.id.tv_wronganswers);
            tv_accuracy = findViewById(R.id.tv_accuracypercent);
            tv_speed = findViewById(R.id.tv_questionsperhour);
            tv_topicname = findViewById(R.id.tv_topicnameselected);

            btn_pracresumretry = findViewById(R.id.button_startpractice);
            btn_quit = findViewById(R.id.button_quitpractice);

            //expandableTextView = (ExpandableTextView) findViewById(R.id.expandable_textview);
            textView_suggstn_explntn = findViewById(R.id.tv_suggexplntn);
            arrow = findViewById(R.id.arrow);
            webView_result = findViewById(R.id.webview_displaycontentresult);
            webView_time = findViewById(R.id.webview_displaycontenttime);
            tv_result = findViewById(R.id.tv_result);
            tv_time1 = findViewById(R.id.tv_time);
            //linearLayout = findViewById(R.id.linearlayout_explsugg);
            //scrollView = findViewById(R.id.scrollView);
            page_scroll = findViewById(R.id.scroll_view_entirepage);
            linear_main = findViewById(R.id.linear_main);
            progressBar = findViewById(R.id.progbar);
            try {
                //exam = (Exam) getIntent().getSerializableExtra("progressreport");

                examid =  (int) getIntent().getSerializableExtra("examidprogress");
                examdata = DatabaseHelper.getInstance(ProgressReportActivity.this).getexamdetails(examid,"");
                if (examdata.length() > 0) {
                    JSONObject obj = examdata.getJSONObject(0);
                    exam.setSkipp(obj.getInt(DatabaseHelper.SKIPP));
                    exam.setAnswered(obj.getInt(DatabaseHelper.ANSWERED));
                    exam.setWrong(obj.getInt(DatabaseHelper.WRONG));
                    exam.setCorrect(obj.getInt(DatabaseHelper.CORRECT));
                    exam.setProgress(obj.getDouble(DatabaseHelper.PROGRESS));
                    exam.setTimetaken(obj.getLong(DatabaseHelper.TIMETAKEN));
                    exam.setSpeed(obj.getDouble(DatabaseHelper.SPEED));
                    //practiceTestModels.get(i).setTotal_questions(obj.getString(EBookDatabaseHelper.QUESTION));
                }
                Log.e("exam data ", examdata.toString());

                Log.e("exam id ", String.valueOf(examid));

            } catch (Exception e) {

            }
            endpractice = (Button) findViewById(R.id.endpractice);
            endpractice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    JSONArray array = DatabaseHelper.getInstance(ProgressReportActivity.this).getqdata(examid, "");
                    Log.e("qdata ", "qdata " + array.toString());
                    if (array.length() > 0) {
                        try {
                            //Log.e("via db ", "via db");
                            JSONObject obj = new JSONObject(array.getJSONObject(0).getString(DatabaseHelper.JSONDATA));
                            parsedata(obj);
                        } catch (Exception e) {
                            reporterror(tag, e.toString());
                            e.printStackTrace();
                        }
                    }
                }
            });

            JSONObject object = new JSONObject();
            JSONObject mainobject = new JSONObject();

            Bundle b1 = getIntent().getExtras();
            if (b1 != null) {
                if (b1.containsKey("flag")) {
                    flag = b1.getInt("flag");
                }
            }

        /*arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expandable_function();
            }
        });

        textView_suggstn_explntn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expandable_function();
            }
        });*/


            tv_result.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    expandable_function_result();
                }
            });

            tv_time1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    expandable_function_time();
                }
            });


            //tv_questions.setText(GlobalValues.exam.getTotal_questions());
            //tv_time.setText(GlobalValues.exam.getDuration());
            int correct = 0;
            int wrong = 0;
            if (flag == 0) {
                try {
                    endpractice.setVisibility(View.GONE);
                    Log.e("i am in ", String.valueOf(flag));
                    //for (int i = 0; i < examArrayList.size(); i++) {
                    JSONArray array = DatabaseHelper.getInstance(ProgressReportActivity.this).getexamdetails(exam.getExamid(), exam.getExam_type());
                    if (array.length() > 0) {
                        //Exam exam = new Exam();
                        JSONObject obj = array.getJSONObject(0);
                        Log.e("obj ", "obj " + obj.toString());
                        if (obj.has(DatabaseHelper.QUESTION)) {
                            int questions = obj.getInt(DatabaseHelper.QUESTION);
                            tv_questions.setText(questions + "");
                        }

                        btn_pracresumretry.setText(getResources().getString(R.string.attempt));


                        if (obj.has(DatabaseHelper.TIMETAKEN)) {
                            timetaken = obj.getLong(DatabaseHelper.TIMETAKEN);
                            try {
                                if (timetaken == 0) {
                                    Log.e("data in min", "data " + Long.parseLong(exam.getDurationinMin()));
                                    timetaken = Long.parseLong(exam.getDurationinMin()) * 60 * 1000;
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            tv_time.setText(getResources().getString(R.string.report_timetaken) + ": " + (timetaken / 1000) + " s");
                        }


                        //tv_questions.setText(examArrayList.get(i).getTotal_questions());
                        if (obj.has(DatabaseHelper.CORRECT)) {
                            correct = obj.getInt(DatabaseHelper.CORRECT);
                            tv_correct.setText(correct + "");
                        }
                        //tv_correct.setText(exam.getTotal_correct());
                        if (obj.has(DatabaseHelper.SKIPP)) {
                            int skipped = obj.getInt(DatabaseHelper.SKIPP);
                            tv_skipped.setText(skipped + "");
                        }

                        if (obj.has(DatabaseHelper.WRONG)) {
                            wrong = obj.getInt(DatabaseHelper.WRONG);
                            tv_wrong.setText(wrong + "");
                        }
                        //tv_wrong.setText(exam.getTotal_wrong());
                        if (obj.has(DatabaseHelper.PROGRESS)) {
                            int percent_covered = (int) obj.getDouble(DatabaseHelper.PROGRESS);
                            tv_percentcovered.setText(percent_covered + " % " + getResources().getString(R.string.covered));
                            progressBar.setProgress(percent_covered);
                        }

                        int accuracy = 0;
                        int attempted = correct + wrong;
                        if (attempted > 0)
                            accuracy = (int) (((correct * 100) / (attempted)));
                        Log.e("accuracy ", "accuracy " + correct + " " + wrong + " " + attempted + " " + accuracy);
                        tv_accuracy.setText("" + accuracy + "%");


                        try {
                            if (obj.has(DatabaseHelper.TIMETAKEN)) {
                                int speed = 0;
                                long timetaken = (int) obj.getDouble(DatabaseHelper.TIMETAKEN);
                                if (timetaken <= 0)
                                    timetaken = Long.parseLong(exam.getDurationinMin()) * 60 * 1000;

                                Log.e("dataspeed ", "data " + (correct + wrong) + " " + timetaken);
                                if ((correct + wrong) > 0)
                                    speed = (((int) timetaken / 1000)) / (correct + wrong);
                                tv_speed.setText(speed + " s/Q");
                            } else {
                                timetaken = Long.parseLong(exam.getDurationinMin()) * 60 * 1000;
                                if (timetaken > 0) {

                                    Log.e("dataspeed ", "data " + (correct + wrong) + " " + timetaken);
                                    int speed = 0;
                                    if ((correct + wrong) > 0)
                                        speed = (((int) timetaken / 1000)) / (correct + wrong);
                                    tv_speed.setText(speed + " s/Q");
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        setTitle(exam.getExamname());
                        try {
                            object.put("ExamId", String.valueOf(exam.getExamid()));
                            //object.put("ExamId", "4");
                            object.put("SubId", "0");
                            object.put("FromValue", 39);
                            //object.put("FromValue", String.valueOf(percent_covered));
                            //object.put("FromValue", String.valueOf((int)((percent_covered / 180) * 100)));
                            /*object.put("ExamId", "4");
                            object.put("SubId", "0");
                            object.put("FromValue", "65");*/
                            mainobject.put("FilterParameter", object.toString());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    //  }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //ConnectionManager.getInstance(ProgressReportActivity.this).getresultremark(mainobject.toString());
            } else if (flag == 1) {
                try {
                    /*if (exam.getIsOmr() == 0) {
                        if (!exam.getExamstatus().equalsIgnoreCase("Attempted"))
                            endpractice.setVisibility(View.VISIBLE);
                        else endpractice.setVisibility(View.GONE);
                    } else endpractice.setVisibility(View.GONE);*/


                    skiplayout.setVisibility(View.VISIBLE);
                    skip_view.setVisibility(View.VISIBLE);
                    Log.e("i am in ", String.valueOf(flag));
                    JSONArray array = DatabaseHelper.getInstance(ProgressReportActivity.this).getexamdetails(examid, "Practice Test");
                    //JSONArray array = DatabaseHelper.getInstance(ProgressReportActivity.this).getexamdetails(exam.getExamid(), exam.getExam_type());
                    if (array.length() > 0) {
                        //Exam exam = new Exam();
                        JSONObject obj = array.getJSONObject(0);
                        Log.e("obj ", "obj " + obj.toString());
                        if (obj.has(DatabaseHelper.QUESTION)) {
                            int questions = obj.getInt(DatabaseHelper.QUESTION);
                            tv_questions.setText(questions + "");
                        }
                        //btn_pracresumretry.setText("Resume Practice");
                        btn_pracresumretry.setVisibility(View.GONE);

                      /*  if (obj.has(DatabaseHelper.TIMETAKEN)) {
                            timetaken = obj.getLong(DatabaseHelper.TIMETAKEN);
                            tv_time.setText(getResources().getString(R.string.report_timetaken) + ": " + (timetaken / 1000) + " s");
                        }

                        if (timetaken == 0) {
                            timetaken = Long.parseLong(exam.getDurationinMin()) * 60 * 1000;
                            tv_time.setText(getResources().getString(R.string.report_timetaken) + ": " + (timetaken / 1000) + " s");
                        }
*/


                        if (obj.has(DatabaseHelper.TIMETAKEN)) {
                            timetaken = obj.getLong(DatabaseHelper.TIMETAKEN);
                            try {
                                if (timetaken == 0) {
                                    Log.e("data in min", "data " + Long.parseLong(exam.getDurationinMin()));
                                    timetaken = Long.parseLong(exam.getDurationinMin()) * 60 * 1000;
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            tv_time.setText(getResources().getString(R.string.report_timetaken) + ": " + (timetaken / 1000) + " s");
                        }


                        //tv_questions.setText(examArrayList.get(i).getTotal_questions());

                        if (obj.has(DatabaseHelper.CORRECT)) {
                            correct = obj.getInt(DatabaseHelper.CORRECT);
                            tv_correct.setText(correct + "");
                        }

                        //tv_correct.setText(exam.getTotal_correct());
                        if (obj.has(DatabaseHelper.SKIPP)) {
                            int skipped = obj.getInt(DatabaseHelper.SKIPP);
                            Log.e("skip qstns ", skipped + "");
                            tv_skipped.setText(skipped + "");
                        }

                        if (obj.has(DatabaseHelper.WRONG)) {
                            wrong = obj.getInt(DatabaseHelper.WRONG);
                            tv_wrong.setText(wrong + "");
                        }
                        //tv_wrong.setText(exam.getTotal_wrong());

                        if (obj.has(DatabaseHelper.PROGRESS)) {
                            int percent_covered = (int) obj.getDouble(DatabaseHelper.PROGRESS);
                            tv_percentcovered.setText(percent_covered + " % " + getResources().getString(R.string.covered));
                            progressBar.setProgress(percent_covered);
                        }

                    /*    if (timetaken > 0) {
                            int speed = 0;
                            if ((correct + wrong) > 0)
                                speed = (((int) timetaken / 1000)) / (correct + wrong);
                            tv_speed.setText(speed + " s/Q");
                        }*/

                        int accuracy = 0;
                        int attempted = correct + wrong;
                        if (attempted > 0)
                            accuracy = (int) (((correct * 100) / (attempted)));
                        Log.e("accuracy ", "accuracy " + correct + " " + wrong + " " + attempted + " " + accuracy);
                        tv_accuracy.setText("" + accuracy + "%");
                        setTitle(exam.getExamname());


                        try {
                            if (obj.has(DatabaseHelper.TIMETAKEN)) {
                                int speed = 0;
                                long timetaken = (int) obj.getDouble(DatabaseHelper.TIMETAKEN);
                                if(timetaken==0)
                                timetaken = Long.parseLong(exam.getDurationinMin()) * 60 * 1000;

                                if ((correct + wrong) > 0)
                                    speed = (((int) timetaken / 1000)) / (correct + wrong);
                                tv_speed.setText(speed + " s/Q");
                            } else {
                                timetaken = Long.parseLong(exam.getDurationinMin()) * 60 * 1000;
                                if (timetaken > 0) {
                                    int speed = 0;
                                    if ((correct + wrong) > 0)
                                        speed = (((int) timetaken / 1000)) / (correct + wrong);
                                    tv_speed.setText(speed + " s/Q");
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        try {

                            object.put("ExamId", String.valueOf(exam.getExamid()));
                            //object.put("ExamId", "4");
                            object.put("SubId", "0");
                            object.put("FromValue", 39);
                            //object.put("FromValue", String.valueOf(percent_covered));
                            //object.put("FromValue", String.valueOf((int)((percent_covered / 180) * 100)));

                            /*object.put("ExamId", "4");
                            object.put("SubId", "0");
                            object.put("FromValue", "65");*/


                            mainobject.put("FilterParameter", object.toString());


                        } catch (Exception e) {
                            e.printStackTrace();
                        }



                        /*btn_pracresumretry.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                back();
                            }
                        });*/

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //ConnectionManager.getInstance(ProgressReportActivity.this).getresultremark(mainobject.toString());

            } else if (flag == 2) {
                try {
                    Log.e("i am in ", String.valueOf(flag));
                    //  int correct=0;
                    endpractice.setVisibility(View.GONE);
                    //for (int i = 0; i < exam.size(); i++) {
                    JSONArray array = DatabaseHelper.getInstance(ProgressReportActivity.this).getexamdetails(exam.getExamid(), exam.getExam_type());
                    Log.e("array ", "array " + array.toString());
                    if (array.length() > 0) {
                        //Exam exam = new Exam();
                        JSONObject obj = array.getJSONObject(0);
                        Log.e("obj ", "obj " + obj.toString());
                        if (obj.has(DatabaseHelper.QUESTION)) {
                            int questions = obj.getInt(DatabaseHelper.QUESTION);
                            tv_questions.setText(questions + "");
                        }
                        //btn_pracresumretry.setText("Resume Practice");
                        btn_pracresumretry.setVisibility(View.GONE);

                        /*if (obj.has(DatabaseHelper.TIMETAKEN)) {
                            timetaken = obj.getLong(DatabaseHelper.TIMETAKEN);
                            tv_time.setText(getResources().getString(R.string.report_timetaken) + ": " + (timetaken / 1000) + " s");
                        }*/

                        if (obj.has(DatabaseHelper.TIMETAKEN)) {
                            timetaken = obj.getLong(DatabaseHelper.TIMETAKEN);
                            try {
                                if (timetaken == 0) {
                                    Log.e("data in min", "data " + Long.parseLong(exam.getDurationinMin()));
                                    timetaken = Long.parseLong(exam.getDurationinMin()) * 60 * 1000;
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            tv_time.setText(getResources().getString(R.string.report_timetaken) + ": " + (timetaken / 1000) + " s");
                        }


                        //tv_questions.setText(examArrayList.get(i).getTotal_questions());
                        if (obj.has(DatabaseHelper.CORRECT)) {
                            correct = obj.getInt(DatabaseHelper.CORRECT);
                            tv_correct.setText(correct + "");
                        }
                        //tv_correct.setText(exam.getTotal_correct());
                        if (obj.has(DatabaseHelper.SKIPP)) {
                            int skipped = obj.getInt(DatabaseHelper.SKIPP);
                            tv_skipped.setText(skipped + "");
                        }
                        if (obj.has(DatabaseHelper.WRONG)) {
                            wrong = obj.getInt(DatabaseHelper.WRONG);
                            tv_wrong.setText(wrong + "");
                        }
                        //tv_wrong.setText(exam.getTotal_wrong());
                        if (obj.has(DatabaseHelper.PROGRESS)) {
                            int percent_covered = (int) obj.getDouble(DatabaseHelper.PROGRESS);
                            tv_percentcovered.setText(percent_covered + " % " + getResources().getString(R.string.covered));
                            progressBar.setProgress(percent_covered);
                        }

                      /*  if (obj.has(DatabaseHelper.TIMETAKEN)) {
                            int speed = 0;
                            if ((correct + wrong) > 0)
                                speed = (((int) obj.getDouble(DatabaseHelper.TIMETAKEN) / 1000)) / (correct + wrong);
                            tv_speed.setText(speed + " s/Q");

                        }*/

                        int accuracy = 0;
                        int attempted = correct + wrong;
                        // int cal=((correct)/(attempted));
                        if (attempted > 0)
                            accuracy = (int) (((correct * 100) / (attempted)));
                        Log.e("accuracy ", "accuracy " + correct + " " + wrong + " " + attempted + " " + accuracy);
                        tv_accuracy.setText("" + accuracy + "%");
                        setTitle(exam.getExamname());


                        try {
                            if (obj.has(DatabaseHelper.TIMETAKEN)) {
                                int speed = 0;
                                long timetaken = (int) obj.getDouble(DatabaseHelper.TIMETAKEN);
                                if(timetaken==0)
                                timetaken = Long.parseLong(exam.getDurationinMin()) * 60 * 1000;
                                if ((correct + wrong) > 0)
                                    speed = (((int) timetaken / 1000)) / (correct + wrong);
                                tv_speed.setText(speed + " s/Q");
                            } else {
                                timetaken = Long.parseLong(exam.getDurationinMin()) * 60 * 1000;
                                if (timetaken > 0) {
                                    int speed = 0;
                                    if ((correct + wrong) > 0)
                                        speed = (((int) timetaken / 1000)) / (correct + wrong);
                                    tv_speed.setText(speed + " s/Q");
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        try {
                            object.put("ExamId", String.valueOf(exam.getExamid()));
                            //object.put("ExamId", "4");
                            object.put("SubId", "0");
                            object.put("FromValue", 39);
                            //object.put("FromValue", String.valueOf((int)((percent_covered / 180) * 100)));

                            /*object.put("ExamId", "4");
                            object.put("SubId", "0");
                            object.put("FromValue", "65");*/


                            mainobject.put("FilterParameter", object.toString());


                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        /*btn_pracresumretry.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                back();
                            }
                        });*/

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }


                //ConnectionManager.getInstance(ProgressReportActivity.this).getresultremark(mainobject.toString());

            } else {
                Log.e("value of flag ", "" + flag);
            }

        /*examArrayList = new ArrayList<Exam>();

        for(int i=0; i<examArrayList.size(); i++){
            Exam progressReportData = new Exam();
            examArrayList.add(progressReportData);
        }*/

            btn_quit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        } catch (Exception e) {
            reporterror(tag, e.toString());
        }
    }

    @Override
    protected void onResponsed(int statuscode, int accesscode, String data) {
        super.onResponsed(statuscode, accesscode, data);
        try {
            if (statuscode == Constants.STATUS_OK) {
                if (accesscode == Connection.GETRESULTREMARK.ordinal()) {
                    Log.e("res ", "resremark " + GlobalValues.TEMP_STR);

                    JSONObject jsonObject = new JSONObject(GlobalValues.TEMP_STR);
                    JSONObject root = jsonObject.getJSONObject("root");
                    JSONObject subroot = root.getJSONObject("subroot");
                    JSONArray subjects = subroot.getJSONArray("Subjects");
                    JSONObject resultViewData = subroot.optJSONObject("ResultViewData");
                    JSONObject timeViewData = subroot.optJSONObject("TimeViewData");

                    String subjectid = subroot.optString("subjectid");
                    String subjectname = subroot.optString("subjectname");
                    String ExamId = subroot.optString("ExamId");


                    if (resultViewData != null) {
                        String result_Subjectname = resultViewData.optString("Subjectname");
                        String result_SubjectId = resultViewData.optString("SubjectId");
                        String result_FromValue = resultViewData.optString("FromValue");
                        String result_ToValue = resultViewData.optString("ToValue");
                        String result_Diffrence = resultViewData.optString("Diffrence");
                        result_Explanation = resultViewData.optString("Explanation");
                        result_Suggestion = resultViewData.optString("Suggestion");

                        expandable_function_result();
                    }

                    if (timeViewData != null) {
                        String time_Subjectname = timeViewData.optString("Subjectname");
                        String time_SubjectId = timeViewData.optString("SubjectId");
                        String time_FromValue = timeViewData.optString("FromValue");
                        String time_ToValue = timeViewData.optString("ToValue");
                        String time_Diffrence = timeViewData.optString("Diffrence");
                        time_Explanation = timeViewData.optString("Explanation");
                        time_Suggestion = timeViewData.optString("Suggestion");

                        expandable_function_time();

                        //tv_remark.setText(Html.fromHtml(time_Suggestion));
                    }



                    /*if (tv_remark.getText().toString().trim().equals("Poor"))
                        tv_remarklight.setBackgroundColor(getResources().getColor(R.color.red));
                    else if (tv_remark.getText().toString().trim().equals("Good"))
                        tv_remarklight.setBackgroundColor(getResources().getColor(R.color.yellow));
                    else if (tv_remark.getText().toString().trim().equals("Very Good"))
                        tv_remarklight.setBackgroundColor(getResources().getColor(R.color.blue1));
                    else if (tv_remark.getText().toString().trim().equals("Excellent"))
                        tv_remarklight.setBackgroundColor(getResources().getColor(R.color.green));*/

                } else if (accesscode == Connection.GETRESULTREMARKEXCEPTION.ordinal()) {
                    Toast.makeText(getApplicationContext(), ProgressReportActivity.this.getResources().getString(R.string.Connectiontimeout), Toast.LENGTH_LONG).show();
                } else if (accesscode == Connection.SavePracticeTest.ordinal()) {
                    Toast.makeText(getApplicationContext(), "Thank You", Toast.LENGTH_LONG).show();
                    endpractice.setVisibility(View.GONE);
                    exam.setExamstatus("Attempted");
                    Log.e("res ", "res " + GlobalValues.TEMP_STR);
                    Intent intent = new Intent(Constants.BROADCAST_WIZARD);
                    intent.putExtra(Constants.BROADCAST_RESPONSE_CODE, 200);
                    intent.putExtra(Constants.BROADCAST_URL_ACCESS, Connection.Dataupdatedattemptest.ordinal());
                    intent.putExtra(Constants.BROADCAST_DATA, Integer.toString(exam.getExamid()));
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                    ProgressReportActivity.this.finish();
                } else if (accesscode == Connection.SavePracticeTestExcetion.ordinal()) {
                    Toast.makeText(getApplicationContext(), "Something went wrong please try again", Toast.LENGTH_LONG).show();
                }
            }
        } catch (Exception e) {
            reporterror(tag, e.toString());
        }
    }

    /*private void expandable_function(){
        if(flag_check_imagebutton == 0 && flag_check_textview == 0){
            flag_check_imagebutton = 1;
            flag_check_textview = 1;
            //arrow.setBackgroundResource(R.drawable.ic_keyboard_arrow_up_black_18dp);
            tv_result.setVisibility(View.VISIBLE);
            tv_time1.setVisibility(View.VISIBLE);
            linearLayout.setVisibility(View.VISIBLE);
            scrollView.scrollTo(0,(int)tv_result.getY());
            //webView_result.setVisibility(View.VISIBLE);
            //webView_time.setVisibility(View.VISIBLE);
            //webView_result.loadData(result_Explanation, "text/html", "UTF-8");
            //webView_time.loadData(time_Explanation, "text/html", "UTF-8")
            //webView_result.loadData(webData, "text/html", "UTF-8");
            //webView_time.loadData(webData, "text/html", "UTF-8");

        }
        else {
            flag_check_imagebutton = 0;
            flag_check_textview = 0;
            arrow.setBackgroundResource(R.drawable.ic_keyboard_arrow_down_black_18dp);
            webView_result.setVisibility(View.GONE);
            webView_time.setVisibility(View.GONE);
            //linearLayout.setVisibility(View.GONE);
            //tv_result.setVisibility(View.GONE);
            //tv_time1.setVisibility(View.GONE);

        }
    }*/

    private void expandable_function_result() {
       /* if(flag_result == 0){
            flag_result = 1;
            tv_result.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.ic_keyboard_arrow_up_white_24dp,0);
            tv_time1.setVisibility(View.VISIBLE);
            tv_result.setVisibility(View.VISIBLE);
            webView_result.setVisibility(View.VISIBLE);
            Log.e("Result Sugg ", result_Suggestion);
            //time_Suggestion = URLEncoder.encode(time_Suggestion).replaceAll();
            webView_result.loadData("<b>Suggestion:</b> "+result_Suggestion + "<br><br><b>Explanation:</b> "+result_Explanation, "text/html", "UTF-8");
        }
        else{
            tv_result.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.ic_keyboard_arrow_down_white_24dp,0);
            flag_result = 0;
            webView_result.setVisibility(View.GONE);
        }*/
    }

    private void expandable_function_time() {
     /*   if(flag_time == 0){
            flag_time = 1;
            tv_time1.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.ic_keyboard_arrow_up_white_24dp,0);
            tv_time1.setVisibility(View.VISIBLE);
            tv_result.setVisibility(View.VISIBLE);
            webView_time.setVisibility(View.VISIBLE);
            webView_time.loadData("<b>Suggestion:</b> "+time_Suggestion + "<br><br><b>Explanation:</b> "+time_Explanation, "text/html", "UTF-8");
            //webView_time.scrollTo(0,-(int)webView_time.getScrollY());
            //page_scroll.fullScroll(View.FOCUS_DOWN);
            //page_scroll.scrollTo(0,(int)(webView_time.getY()+page_scroll.getY()));
            //linear_main.scrollTo(0,(int)(webView_time.getY()+page_scroll.getY()));
        }
        else{
            tv_time1.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.ic_keyboard_arrow_down_white_24dp,0);
            flag_time = 0;
            webView_time.setVisibility(View.GONE);
        }*/
    }


    @Override
    protected void back() {
        try {
            final Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp);
            upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
            getSupportActionBar().setHomeAsUpIndicator(upArrow);

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ProgressReportActivity.this.finish();
                }
            });
        } catch (Exception e) {
            reporterror(tag, e.toString());
            e.printStackTrace();
        }
        super.back();
    }


    public void parsedata(JSONObject examobj) {
        try {
            JSONArray jarray = DatabaseHelper.getInstance(ProgressReportActivity.this).getans(exam.getExamid());
            //   Log.e("objexam ", "jarray " + jarray.toString());
            JSONArray array = examobj.optJSONArray("questions");
            ArrayList<Question> qdata = new ArrayList<>();
            if (array != null) {
                Log.e("array ", "array " + array.toString());
                for (int i = 0; i < array.length(); i++) {
                    qdata.add(new Question(array.getJSONObject(i)));
                }
            } else {
                JSONObject obj1 = examobj.optJSONObject("questions");
                if (obj1 != null) {
                    qdata.add(new Question(obj1));
                } else {

                }
            }

            if (!exam.getExamstatus().equalsIgnoreCase("Attempted")) {
                Double obtainedmarks = 0.0;
                int total_skipped = 0;
                int totalattempted = 0;
                int totalwrong = 0;
                int total_right = 0;
                for (int i = 0; i < qdata.size(); i++) {
                    if (qdata.get(i).isIsanswered()) {
                        totalattempted++;
                        Log.e("isanswered ", "/************************************/");
                        ArrayList<Options> optdata = qdata.get(i).getOptions();
                        for (int j = 0; j < optdata.size(); j++) {
                            if (optdata.get(j).isSelected()) {
                                Log.e("optselected ", "optselected true");
                                if (optdata.get(j).isAnswer()) {
                                    total_right++;
                                    obtainedmarks = obtainedmarks + (Double.parseDouble(qdata.get(i).getMarks()));
                                } else {
                                    qdata.get(i).setIswrongAnswer(true);
                                    totalwrong++;
                                    //  negativemarks = negativemarks + (Double.parseDouble(exam.getNeagativemarks()));
                                }
                            } else {
                                Log.e("optselected ", "optselected false");
                            }
                        }
                    } else if (qdata.get(i).isIsskipped()) {
                        total_skipped++;
                    }
                }
                try {
                    //  exam.setTimetaken(updatedTime);
                    JSONObject obj = new JSONObject();
                    obj.put("studentid", GlobalValues.student.getId());
                    obj.put("examid", exam.getExamid());
                    Calendar cal = Calendar.getInstance();
                    String d = DateUtils.formatDate(cal.getTime(), "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                    obj.put("updateddate", d);
                    long avgtime = 0;
                    if ((total_skipped + totalattempted) > 0)
                        avgtime = ((timetaken / 1000) / (total_skipped + totalattempted));
                    obj.put("averagetime", avgtime);
                    obj.put("total_marks", obtainedmarks);
                    obj.put("total_skipped", total_skipped);
                    obj.put("total_right", total_right);
                    obj.put("total_wrong", totalwrong);
                    obj.put("total_attempted", totalattempted);
                    obj.put("total_notattempted", qdata.size() - totalattempted - total_skipped);
                    JSONObject rootobj = new JSONObject();
                    JSONObject subrootobj = new JSONObject();
                    JSONObject xmlobj = new JSONObject();
                    subrootobj.put("subroot", obj);
                    rootobj.put("root", subrootobj);
                    xmlobj.put("xml", rootobj.toString());
                    JSONObject mainobj = new JSONObject();
                    mainobj.put("FilterParameter", xmlobj.toString());
                    genloading(getResources().getString(R.string.loading));
                    ConnectionManager.getInstance(ProgressReportActivity.this).savepracticetest(mainobj.toString());
                } catch (Exception e) {
                    reporterror(tag, e.toString());
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.allready_submited), Toast.LENGTH_LONG).show();
            }


        } catch (Exception e) {
            reporterror(tag, e.toString());
            e.printStackTrace();
        }
    }

}


/*HtmlTextView htmlTextView = findViewById(R.id.html_textview);
                    htmlTextView.setHtml(htmlcode, new HtmlHttpImageGetter(htmlTextView));*/