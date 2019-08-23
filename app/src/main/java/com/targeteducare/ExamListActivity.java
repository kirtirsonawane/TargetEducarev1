package com.targeteducare;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.targeteducare.Adapter.CoursesAdapter;
import com.targeteducare.Adapter.ExamAdapter;
import com.targeteducare.Adapter.MockTestAdapter;
import com.targeteducare.Adapter.ViewPagerAdapter;
import com.targeteducare.Classes.Course;
import com.targeteducare.Classes.Exam;
import com.targeteducare.database.DatabaseHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;

public class ExamListActivity extends Activitycommon {
    RecyclerView mRecyclerview;
    LinearLayoutManager mLayoutmanager;
    ExamAdapter adapter;
    MockTestAdapter adapter1;
    ArrayList<Exam> mdataset;
    ArrayList<Uri> filesdata;
    //int sendmailselectedpos = -1;
    Exam mailselectedexam=new Exam();
    String type = "";
    Bundle b1;
    String lang = "";
    TextView tv_notest, tv_toolbar;
    String examname = "";
    //private ArrayList<String> test = new ArrayList<>();
    TabLayout tabLayout;
    private ViewPager viewPager;
    //Exam exam = new Exam();
    /* private ArrayList<Exam> mdataset_New = new ArrayList<>();
     private ArrayList<Exam> mdataset_Missed = new ArrayList<>();
     private ArrayList<Exam> mdataset_Attempted = new ArrayList<>();*/
    Spinner sp;
    ArrayList<Course> coursedata = new ArrayList<>();
    ArrayAdapter<Course> spinnerArrayAdapter;
    ViewPagerAdapter viewpageradapter;
    String tag = "";

    //  ImageView img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            screenshot_capture_permission();
            super.onCreate(savedInstanceState);
            loadLocale();
            setContentView(R.layout.activity_exam_list);
            tag = this.getClass().getSimpleName();
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
            tv_notest = findViewById(R.id.notestavailable);
            //setmaterialDesign();
            //back();
            toolbar = findViewById(R.id.toolbar);
            tv_toolbar = findViewById(R.id.collapsing_toolbar_textview);
            tv_toolbar.setText(getResources().getString(R.string.toolbar_hey) + ", " + GlobalValues.student.getName() + "\n" + getResources().getString(R.string.toolbar_text));
            toolbar.setTitleTextColor(getResources().getColor(R.color.White));
            setSupportActionBar(toolbar);
            registerreceiver();
            toolbar.setLogo(R.mipmap.ic_launcher);
            toolbar.setPadding(30, 10, 10, 10);

            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

            final Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp);
            upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);

            getSupportActionBar().setHomeAsUpIndicator(upArrow);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

            tabLayout = (TabLayout) findViewById(R.id.tabs);
            tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.tab_new)));
            tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.tab_missed)));
            tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.tab_attempted)));
            tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#ffffff"));
            //tabLayout.setSelectedTabIndicatorHeight((int) (5 * getResources().getDisplayMetrics().density));
            tabLayout.setTabTextColors(Color.parseColor("#32c5f3"), Color.parseColor("#ffffff"));
            viewPager = findViewById(R.id.viewpager);
            viewpageradapter = new ViewPagerAdapter(ExamListActivity.this, getSupportFragmentManager(), GlobalValues.mdataset_New, GlobalValues.mdataset_Attempted, GlobalValues.mdataset_Missed,"Monthly Ranking Test");
            viewPager.setAdapter(viewpageradapter);
            tabLayout.setupWithViewPager(viewPager);
            mRecyclerview = findViewById(R.id.recyclerview_1);
            mLayoutmanager = new LinearLayoutManager(ExamListActivity.this);
            mRecyclerview.setLayoutManager(mLayoutmanager);
            //tv_test = findViewById(R.id.typeoftestname);
            tv_notest = findViewById(R.id.notestavailable);
            mdataset = new ArrayList<Exam>();

            lang = getSharedPreferences("Settings", Activity.MODE_PRIVATE).getString("Current Language", "");
            sp = (Spinner) findViewById(R.id.spinner_1);

            spinnerArrayAdapter = new ArrayAdapter<Course>(this, android.R.layout.simple_spinner_item, coursedata); //selected item will look like a spinner set from XML
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sp.setAdapter(spinnerArrayAdapter);

            sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    parseexamdata(coursedata.get(i).getCourse_id());
                    //  DatabaseHelper.getInstance(ExamListActivity.this).getexaminationdatav1(type,coursedata.get(i).getCourse_id());
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            b1 = getIntent().getExtras();
            type = b1.getString("testtype");

            if (type.equals(getResources().getString(R.string.monthly_test))) {
                setTitle(getResources().getString(R.string.monthly_test));
            } else {
                setTitle(getResources().getString(R.string.context_test));
            }
            attachUI();
        } catch (Exception e) {
            reporterror(tag, e.toString());
            e.printStackTrace();
        }
    }


    private void attachUI() {
        try {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);
            }
            b1 = getIntent().getExtras();
            type = b1.getString("testtype");

        /*img=(ImageView)findViewById(R.id.imageview_1);

        ArrayList<QuestionURL> qdata=new ArrayList<>();
        qdata.add(new QuestionURL("http://192.168.1.59:8095/images/Temp/636888521160850000_files/image042.png"));
        new DownloadImage().execute(qdata);*/
            StructureClass.defineContext(ExamListActivity.this);
            filesdata = new ArrayList<>();
            mdataset = new ArrayList<>();
            mRecyclerview = (RecyclerView) findViewById(R.id.recyclerview_1);
            mLayoutmanager = new LinearLayoutManager(ExamListActivity.this);
            mRecyclerview.setLayoutManager(mLayoutmanager);
            adapter = new ExamAdapter(ExamListActivity.this, mdataset, lang,type);
            mRecyclerview.setAdapter(adapter);

            try {
                JSONObject obj = new JSONObject();
                obj.put("studentid", GlobalValues.student.getId());
                obj.put("test_type", type);
                Calendar cal = Calendar.getInstance();
                obj.put("year", cal.get(Calendar.YEAR));
                JSONObject mainobj = new JSONObject();
                mainobj.put("FilterParameter", obj.toString());
                genloading(getResources().getString(R.string.loading));
                if (dialog != null)
                    dialog.setCancelable(true);
                ConnectionManager.getInstance(ExamListActivity.this).getexam(mainobj.toString(), type);

            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            reporterror(tag, e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 101: {
                try {
                    // if (selectedposition != -1) {

                    //JSONArray array = DatabaseHelper.getInstance(ExamListActivity.this).getansdata1(mailselectedexam.getExamid(),mailselectedexam.getLanguages());

                    if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);
                    } else {
                        JSONArray array = DatabaseHelper.getInstance(ExamListActivity.this).getansdata1(mailselectedexam.getExamid(),mailselectedexam.getLanguages());
                        if (array.length() > 0) {
                            genloading(getResources().getString(R.string.loading));
                            StructureClass.clearAll();
                            filesdata.clear();
                            createfiles(array);
                        } else {
                            if (!((Activity) context).isFinishing()) {
                                Toast.makeText(getApplicationContext(), "No data available", Toast.LENGTH_LONG).show();
                            } else {
                            }
                        }
                    }
                    // }
                } catch (Exception e) {
                    reporterror(tag, e.toString());
                    e.printStackTrace();
                }
                return;
            }
        }
    }

    @Override
    protected void onResponsed(int statuscode, int accesscode, String data) {
        super.onResponsed(statuscode, accesscode, data);
        if (statuscode == Constants.STATUS_OK) {
            if (accesscode == Connection.GET_EXAMS.ordinal()) {
                try {
                  /*  JSONObject obj1 = new JSONObject(GlobalValues.TEMP_STR).getJSONObject("root");
                    JSONArray array = new JSONArray();
                    if (obj1.optJSONArray("subroot") != null) {
                        array = obj1.getJSONArray("subroot");
                    } else if (obj1.optJSONObject("subroot") != null) {

                        if(obj1.getJSONObject("subroot").has("error"))
                        {
                            updatefragmentdata();
                            return;
                        }else {
                            array.put(obj1.getJSONObject("subroot"));
                        }
                    }
                    ContentValues c = new ContentValues();
                    c.put(DatabaseHelper.JSONDATA, array.toString());
                    c.put(DatabaseHelper.SAVEDTIME, DateUtils.getSqliteTime());
                    c.put(DatabaseHelper.TYPE, type);
                    DatabaseHelper.getInstance(ExamListActivity.this).saveexaminationdata(c, type);*/

                    mdataset.clear();

                   /* for (int i = 0; i < array.length(); i++) {
                        JSONObject obj = array.getJSONObject(i);
                        ContentValues c1 = new ContentValues();
                        c1.put(DatabaseHelper.SAVEDTIME, DateUtils.getSqliteTime());
                        c1.put(DatabaseHelper.TYPE, type);
                        c1.put(DatabaseHelper.JSONDATA, obj.toString());
                        c1.put(DatabaseHelper.COURSENAME, obj.getString("coursename"));
                        c1.put(DatabaseHelper.COURSEID, obj.getString("courseid"));
                        c1.put(DatabaseHelper.COURSENAMEINMARATHI, obj.getString("coursename_inmarathi"));
                        c1.put(DatabaseHelper.EXAMID, obj.getInt("examid"));
                        DatabaseHelper.getInstance(ExamListActivity.this).saveexaminationdatav1(c1, obj.getInt("examid"), obj.getInt("courseid"));
                    }*/

                    parseexamdata(-1);
                } catch (Exception e) {
                    reporterror(tag, e.toString());
                    e.printStackTrace();
                }
            } else if (accesscode == Connection.NoExamAvailable.ordinal()) {
                try {
                    if (!((Activity) context).isFinishing()) {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.nodata), Toast.LENGTH_LONG).show();
                    } else {
                        Log.e("no activity", "no activity ");
                    }
             /*   *//*    mdataset.clear();*//*
                    parseexamdata(-1);*/
                } catch (Exception e) {
                    reporterror(tag, e.toString());
                    e.printStackTrace();
                }
            } else if (accesscode == Connection.GET_EXAMSEXCEPTION.ordinal()) {
                try {
                    if (!((Activity) context).isFinishing()) {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.Connectiontimeout), Toast.LENGTH_LONG).show();
                    } else {
                        Log.e("no activity", "no activity ");
                    }
                   parseexamdata(-1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (accesscode == Connection.GET_QUESTIONS.ordinal()) {
                try {
                    for (int i = 0; i < mdataset.size(); i++) {
                        if (lang.equals("mr")) {
                            mdataset.get(i).setLanguages("marathi");
                        } else {
                            mdataset.get(i).setLanguages("ENGLISH");
                        }
                        int qcount = DatabaseHelper.getInstance(ExamListActivity.this).getqdatacount(mdataset.get(i).getExamid(), mdataset.get(i).getLanguages());
                        if (qcount > 0) {
                            mdataset.get(i).setIsqdownloaded(true);
                        } else {
                            mdataset.get(i).setIsqdownloaded(false);
                        }
                    }
                    adapter.notifyDataSetChanged();
                    if (!((Activity) context).isFinishing()) {
                        //Toast.makeText(getApplicationContext(), Constants.qpaperdownloaded, Toast.LENGTH_LONG).show();
                      /*  Log.e("flag","flag "+flag+" "+selectedposition);
                        if(flag==1)
                        {
                            if(selectedposition>=0 && selectedposition < mdataset.size())
                           gotoexamActivity( mdataset.get(selectedposition));
                        }*/
                    } else {
                    }
                } catch (Exception e) {
                    reporterror(tag, e.toString());
                    e.printStackTrace();
                }
            } else if (accesscode == Connection.GET_QUESTIONSEXCEPTION.ordinal()) {
                try {
                    if (!((Activity) context).isFinishing()) {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.Connectiontimeout), Toast.LENGTH_LONG).show();
                    } else {
                    }
                } catch (Exception e) {
                    reporterror(tag, e.toString());
                    e.printStackTrace();
                }
            } else if (accesscode == Connection.IPChange.ordinal()) {
                try {
                    // if (InternetUtils.getInstance(ExamListActivity.this).available()) {
                    JSONObject obj = new JSONObject();
                    obj.put("studentid", GlobalValues.student.getId());
                    //obj.put("studentid", GlobalValues.studentProfile.getId());
                    // obj.put("type", "current");
                    Calendar cal = Calendar.getInstance();
                    obj.put("year", cal.get(Calendar.YEAR));
                    JSONObject mainobj = new JSONObject();
                    mainobj.put("FilterParameter", obj.toString());
                    genloading(getResources().getString(R.string.loading));
                    ConnectionManager.getInstance(ExamListActivity.this).getexam(mainobj.toString(), type);
                    //  }
                } catch (Exception e) {
                    reporterror(tag, e.toString());
                    e.printStackTrace();
                }
            } else if (accesscode == Connection.SAVE_ANSWER.ordinal()) {
                try {
                    for (int i = 0; i < mdataset.size(); i++) {
                        int synccount = DatabaseHelper.getInstance(ExamListActivity.this).getasyncdatacount(mdataset.get(i).getExamid(), mdataset.get(i).getLanguages());
                        if (synccount > 0) {
                            mdataset.get(i).setIssync(true);
                        } else {
                            mdataset.get(i).setIssync(false);
                        }
                    }
                    adapter.notifyDataSetChanged();
                    if (!((Activity) context).isFinishing()) {
                        Toast.makeText(getApplicationContext(), "Thank You", Toast.LENGTH_LONG).show();
                    } else {
                    }
                    updatefragmentdata();
                } catch (Exception e) {
                    reporterror(tag, e.toString());
                    e.printStackTrace();
                }
            } else if (accesscode == Connection.SAVE_ANSWEREXCEPTION.ordinal()) {
                try {
                    if (!((Activity) context).isFinishing()) {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.Connectiontimeout), Toast.LENGTH_LONG).show();
                    } else {

                    }
                    for (int i = 0; i < mdataset.size(); i++) {
                        int synccount = DatabaseHelper.getInstance(ExamListActivity.this).getasyncdatacount(mdataset.get(i).getExamid(), mdataset.get(i).getLanguages());
                        if (synccount > 0) {
                            mdataset.get(i).setIssync(true);
                        } else {
                            mdataset.get(i).setIssync(false);
                        }
                    }
                    updatefragmentdata();
                } catch (Exception e) {
                    reporterror(tag, e.toString());
                    e.printStackTrace();
                }
            } else if (accesscode == Connection.Dataupdated.ordinal()) {
                try {
                    if (data.length() > 0) {
                        for (int i = 0; i < mdataset.size(); i++) {
                            try {
                                JSONArray array = DatabaseHelper.getInstance(ExamListActivity.this).getexamdetails(mdataset.get(i).getExamid(), mdataset.get(i).getExam_type());
                                if (array.length() > 0) {
                                    JSONObject obj = array.getJSONObject(array.length() - 1);
                                    mdataset.get(i).setSkipp(obj.getInt(DatabaseHelper.SKIPP));
                                    mdataset.get(i).setAnswered(obj.getInt(DatabaseHelper.ANSWERED));
                                    mdataset.get(i).setWrong(obj.getInt(DatabaseHelper.WRONG));
                                    mdataset.get(i).setCorrect(obj.getInt(DatabaseHelper.CORRECT));
                                    mdataset.get(i).setProgress(obj.getDouble(DatabaseHelper.PROGRESS));
                                    mdataset.get(i).setTimetaken(obj.getLong(DatabaseHelper.TIMETAKEN));
                                    mdataset.get(i).setSpeed(obj.getDouble(DatabaseHelper.SPEED));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            if (mdataset.get(i).getExamid() == Integer.parseInt(data)) {
                                mdataset.get(i).setIsexamgiven(true);
                                adapter.notifyDataSetChanged();
                                break;
                            }
                            int synccount = DatabaseHelper.getInstance(ExamListActivity.this).getasyncdatacount(mdataset.get(i).getExamid(), mdataset.get(i).getLanguages());
                            if (synccount > 0) {
                                mdataset.get(i).setIssync(true);
                            } else {
                                mdataset.get(i).setIssync(false);
                            }
                        }
                    } else {
                        for (int i = 0; i < mdataset.size(); i++) {
                            try {
                                JSONArray array = DatabaseHelper.getInstance(ExamListActivity.this).getexamdetails(mdataset.get(i).getExamid(), mdataset.get(i).getExam_type());
                                if (array.length() > 0) {
                                    JSONObject obj = array.getJSONObject(array.length() - 1);
                                    mdataset.get(i).setSkipp(obj.getInt(DatabaseHelper.SKIPP));
                                    mdataset.get(i).setAnswered(obj.getInt(DatabaseHelper.ANSWERED));
                                    mdataset.get(i).setWrong(obj.getInt(DatabaseHelper.WRONG));
                                    mdataset.get(i).setCorrect(obj.getInt(DatabaseHelper.CORRECT));
                                    mdataset.get(i).setProgress(obj.getDouble(DatabaseHelper.PROGRESS));
                                    mdataset.get(i).setTimetaken(obj.getLong(DatabaseHelper.TIMETAKEN));
                                    mdataset.get(i).setSpeed(obj.getDouble(DatabaseHelper.SPEED));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            int count = DatabaseHelper.getInstance(ExamListActivity.this).getqdata1count(mdataset.get(i).getExamid(), mdataset.get(i).getLanguages());
                            if (count > 0) {
                                mdataset.get(i).setIsexamgiven(true);
                            } else {
                                mdataset.get(i).setIsexamgiven(false);
                            }

                            int synccount = DatabaseHelper.getInstance(ExamListActivity.this).getasyncdatacount(mdataset.get(i).getExamid(), mdataset.get(i).getLanguages());
                            if (synccount > 0) {
                                mdataset.get(i).setIssync(true);
                            } else {
                                mdataset.get(i).setIssync(false);
                            }
                        }
                    }

                    updatefragmentdata();
                } catch (Exception e) {
                    reporterror(tag, e.toString());
                    e.printStackTrace();
                }
            }
        }
    }


    public void updatefragmentdata() {
        try {
            GlobalValues.mdataset_New.clear();
            GlobalValues.mdataset_Attempted.clear();
            GlobalValues.mdataset_Missed.clear();

            Log.e("sizee ","sizee "+ GlobalValues.mdataset_Attempted.size()+" "+ GlobalValues.mdataset_New.size()+" "+ GlobalValues.mdataset_Missed.size());

            for (int i = 0; i < mdataset.size(); i++) {
                Log.e("examstatus ","exam ststus "+mdataset.get(i).getExamstatus()+" "+((int) Math.round(mdataset.get(i).getProgress())));
                if (((int) Math.round(mdataset.get(i).getProgress())) > 0) {
                    Log.e("examstatus if ","exam ststus11 ");
                    //mdataset.get(i).setExamstatus("Attempted");
                    if (GlobalValues.mdataset_Attempted.size() > 0) {
                        if (!GlobalValues.mdataset_Attempted.get(GlobalValues.mdataset_Attempted.size() - 1).getCoursename().equalsIgnoreCase(mdataset.get(i).getCoursename())) {
                            Exam exam = new Exam();
                            exam.setIsheader(true);
                            exam.setCoursename(mdataset.get(i).getCoursename());
                            exam.setCoursename_inmarathi(mdataset.get(i).getCoursename_inmarathi());
                            GlobalValues.mdataset_Attempted.add(exam);
                        }
                    } else if (GlobalValues.mdataset_Attempted.size() == 0) {
                        Exam exam = new Exam();
                        exam.setIsheader(true);
                        exam.setCoursename(mdataset.get(i).getCoursename());
                        exam.setCoursename_inmarathi(mdataset.get(i).getCoursename_inmarathi());
                        GlobalValues.mdataset_Attempted.add(exam);
                    }
                    mdataset.get(i).setExamstatus("Attempted");
                    GlobalValues.mdataset_Attempted.add(mdataset.get(i));
                }else if (mdataset.get(i).getExamstatus().equalsIgnoreCase("Missed")) {
                    if (((int) Math.round(mdataset.get(i).getProgress())) > 0) {
                        Log.e("examstatus if ","exam ststus11 ");
                        //mdataset.get(i).setExamstatus("Attempted");
                        if (GlobalValues.mdataset_Attempted.size() > 0) {
                            if (!GlobalValues.mdataset_Attempted.get(GlobalValues.mdataset_Attempted.size() - 1).getCoursename().equalsIgnoreCase(mdataset.get(i).getCoursename())) {
                                Exam exam = new Exam();
                                exam.setIsheader(true);
                                exam.setCoursename(mdataset.get(i).getCoursename());
                                exam.setCoursename_inmarathi(mdataset.get(i).getCoursename_inmarathi());
                                GlobalValues.mdataset_Attempted.add(exam);
                            }
                        } else if (GlobalValues.mdataset_Attempted.size() == 0) {
                            Exam exam = new Exam();
                            exam.setIsheader(true);
                            exam.setCoursename(mdataset.get(i).getCoursename());
                            exam.setCoursename_inmarathi(mdataset.get(i).getCoursename_inmarathi());
                            GlobalValues.mdataset_Attempted.add(exam);
                        }
                        mdataset.get(i).setExamstatus("Attempted");
                        GlobalValues.mdataset_Attempted.add(mdataset.get(i));
                    } else if (GlobalValues.mdataset_Missed.size() > 0) {
                        Log.e("examstatus else ","exam ststus11 ");
                        if (!GlobalValues.mdataset_Missed.get(GlobalValues.mdataset_Missed.size() - 1).getCoursename().equalsIgnoreCase(mdataset.get(i).getCoursename())) {
                            Exam exam = new Exam();
                            exam.setIsheader(true);
                            exam.setCoursename(mdataset.get(i).getCoursename());
                            exam.setCoursename_inmarathi(mdataset.get(i).getCoursename_inmarathi());
                            GlobalValues.mdataset_Missed.add(exam);
                        }
                    } else if (GlobalValues.mdataset_Missed.size() == 0) {
                        Exam exam = new Exam();
                        exam.setIsheader(true);
                        exam.setCoursename(mdataset.get(i).getCoursename());
                        exam.setCoursename_inmarathi(mdataset.get(i).getCoursename_inmarathi());
                        GlobalValues.mdataset_Missed.add(exam);
                    }
                    GlobalValues.mdataset_Missed.add(mdataset.get(i));
                } else if (mdataset.get(i).getExamstatus().equalsIgnoreCase("Attempted")) {
                    if (GlobalValues.mdataset_Attempted.size() > 0) {
                        if (!GlobalValues.mdataset_Attempted.get(GlobalValues.mdataset_Attempted.size() - 1).getCoursename().equalsIgnoreCase(mdataset.get(i).getCoursename())) {
                            Exam exam = new Exam();
                            exam.setIsheader(true);
                            exam.setCoursename(mdataset.get(i).getCoursename());
                            exam.setCoursename_inmarathi(mdataset.get(i).getCoursename_inmarathi());
                            GlobalValues.mdataset_Attempted.add(exam);
                        }
                    } else if (GlobalValues.mdataset_Attempted.size() == 0) {
                        Exam exam = new Exam();
                        exam.setIsheader(true);
                        exam.setCoursename(mdataset.get(i).getCoursename());
                        exam.setCoursename_inmarathi(mdataset.get(i).getCoursename_inmarathi());
                        GlobalValues.mdataset_Attempted.add(exam);
                    }
                    GlobalValues.mdataset_Attempted.add(mdataset.get(i));
                } else {
                    if (((int) Math.round(mdataset.get(i).getProgress())) == 0) {
                        if (GlobalValues.mdataset_New.size() > 0) {
                            if (!GlobalValues.mdataset_New.get(GlobalValues.mdataset_New.size() - 1).getCoursename().equalsIgnoreCase(mdataset.get(i).getCoursename())) {
                                Exam exam = new Exam();
                                exam.setIsheader(true);
                                exam.setCoursename(mdataset.get(i).getCoursename());
                                exam.setCoursename_inmarathi(mdataset.get(i).getCoursename_inmarathi());
                                GlobalValues.mdataset_New.add(exam);
                            }
                        } else if (GlobalValues.mdataset_New.size() == 0) {
                            Exam exam = new Exam();
                            exam.setIsheader(true);
                            exam.setCoursename(mdataset.get(i).getCoursename());
                            exam.setCoursename_inmarathi(mdataset.get(i).getCoursename_inmarathi());
                            GlobalValues.mdataset_New.add(exam);
                        }
                        GlobalValues.mdataset_New.add(mdataset.get(i));
                    }else {
                        if (GlobalValues.mdataset_Attempted.size() > 0) {
                            if (!GlobalValues.mdataset_Attempted.get(GlobalValues.mdataset_Attempted.size() - 1).getCoursename().equalsIgnoreCase(mdataset.get(i).getCoursename())) {
                                Exam exam = new Exam();
                                exam.setIsheader(true);
                                exam.setCoursename(mdataset.get(i).getCoursename());
                                exam.setCoursename_inmarathi(mdataset.get(i).getCoursename_inmarathi());
                                GlobalValues.mdataset_Attempted.add(exam);
                            }
                        } else if (GlobalValues.mdataset_Attempted.size() == 0) {
                            Exam exam = new Exam();
                            exam.setIsheader(true);
                            exam.setCoursename(mdataset.get(i).getCoursename());
                            exam.setCoursename_inmarathi(mdataset.get(i).getCoursename_inmarathi());
                            GlobalValues.mdataset_Attempted.add(exam);
                        }
                        mdataset.get(i).setExamstatus("Attempted");
                        GlobalValues.mdataset_Attempted.add(mdataset.get(i));
                    }
                }
            }

            Log.e("sizee ","sizee "+ GlobalValues.mdataset_Attempted.size()+" "+ GlobalValues.mdataset_New.size()+" "+ GlobalValues.mdataset_Missed.size());
            publishBroadcast(200, Connection.Syncdata.ordinal());
         //   publishBroadcast();
            final Handler handler = new Handler();
      /*  final Runnable r = new Runnable() {
            public void run() {
                if (dataFromActivityToFragment != null)
                    dataFromActivityToFragment.sendData(GlobalValues.mdataset_New);


                if (dataFromActivityToFragmentmissed != null)
                    dataFromActivityToFragmentmissed.sendData(GlobalValues.mdataset_New);

                if (dataFromActivityToFragmentattempted != null)
                    dataFromActivityToFragmentattempted.sendData(GlobalValues.mdataset_Attempted);

            }
        };
        handler.postDelayed(r, 0);*/
         Log.e("Global views ",mdataset.size()+" "+GlobalValues.mdataset_Missed.size()+" "+GlobalValues.mdataset_New.size()+" "+GlobalValues.mdataset_Attempted.size());
            try {
                Intent intent = new Intent("updateddata");
                intent.putExtra(Constants.BROADCAST_RESPONSE_CODE, 200);
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
            } catch (Exception e) {
                Log.e("error11 ","error "+e.toString());
                e.printStackTrace();
            }
        } catch (Exception e) {
            Log.e("error11 ","error "+e.toString());
            reporterror(tag, e.toString());
            e.printStackTrace();
        }
    }
    private void publishBroadcast(final int code, final int ordinal) {
        try {
            Intent intent = new Intent(Constants.BROADCAST_WIZARD);
            intent.putExtra(Constants.BROADCAST_RESPONSE_CODE, code);
            intent.putExtra(Constants.BROADCAST_URL_ACCESS, ordinal);
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void publishBroadcast() {
        try {
            Intent intent = new Intent("updateddata");
            LocalBroadcastManager.getInstance(ExamListActivity.this).sendBroadcast(intent);
        } catch (Exception e) {
            reporterror(tag, e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.exam, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            /*case R.id.action_ip:
                Intent intent = new Intent(ExamListActivity.this, IPSettingActivity.class);
                startActivity(intent);
                break;*/
        }
        return super.onOptionsItemSelected(item);
    }

    public void parseexamdata(int courseid) {
        try {
            mdataset.clear();
            JSONArray array1 = DatabaseHelper.getInstance(ExamListActivity.this).getexaminationdatav1(type, courseid);
           Log.e("exam ","exam "+array1.toString());
            for (int i = 0; i < array1.length(); i++) {
                // JSONObject obj = new JSONObject(array1.getJSONObject(i).getString(DatabaseHelper.JSONDATA));
                try {
                    JSONObject obj = array1.getJSONObject(i);
                    JSONObject obj1 = new JSONObject(obj.getString(DatabaseHelper.JSONDATA));
                    Gson gson = new Gson();
                    Type type = new TypeToken<Exam>() {
                    }.getType();
                    Exam exam = gson.fromJson(obj1.toString(), type);
                    mdataset.add(exam);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            for (int i = 0; i < mdataset.size(); i++) {
                if (lang.equals("mr")) {
                    mdataset.get(i).setLanguages("marathi");
                    //  mdataset.get(i).setName_InMarathi("marathi");
                } else {
                    mdataset.get(i).setLanguages("ENGLISH");
                    //  mdataset.get(i).setName_InMarathi("marathi");
                }

                int count = DatabaseHelper.getInstance(ExamListActivity.this).getqdata1count(mdataset.get(i).getExamid(), mdataset.get(i).getLanguages());
                if (count > 0) {
                    mdataset.get(i).setIsexamgiven(true);
                } else {
                    mdataset.get(i).setIsexamgiven(false);
                }

                int synccount = DatabaseHelper.getInstance(ExamListActivity.this).getasyncdatacount(mdataset.get(i).getExamid(), mdataset.get(i).getLanguages());
                if (synccount > 0) {
                    mdataset.get(i).setIssync(true);
                } else {
                    mdataset.get(i).setIssync(false);
                }

                int qcount = DatabaseHelper.getInstance(ExamListActivity.this).getqdatacount(mdataset.get(i).getExamid(), mdataset.get(i).getLanguages());
                if (qcount > 0) {
                    mdataset.get(i).setIsqdownloaded(true);
                } else {
                    mdataset.get(i).setIsqdownloaded(false);
                }

                JSONArray array = DatabaseHelper.getInstance(ExamListActivity.this).getexamdetails(mdataset.get(i).getExamid(), mdataset.get(i).getExam_type());
                if (array.length() > 0) {
                    JSONObject obj = array.getJSONObject(0);
                    mdataset.get(i).setSkipp(obj.getInt(DatabaseHelper.SKIPP));
                    mdataset.get(i).setAnswered(obj.getInt(DatabaseHelper.ANSWERED));
                    mdataset.get(i).setWrong(obj.getInt(DatabaseHelper.WRONG));
                    mdataset.get(i).setCorrect(obj.getInt(DatabaseHelper.CORRECT));
                    mdataset.get(i).setProgress(obj.getDouble(DatabaseHelper.PROGRESS));
                    mdataset.get(i).setTimetaken(obj.getLong(DatabaseHelper.TIMETAKEN));
                    mdataset.get(i).setSpeed(obj.getDouble(DatabaseHelper.SPEED));
                }
            }
            adapter.notifyDataSetChanged();
            updatefragmentdata();

            if (coursedata.size() == 0) {
                JSONArray array = DatabaseHelper.getInstance(ExamListActivity.this).getcoursedata();
                Gson gson = new Gson();
                Type type = new TypeToken<ArrayList<Course>>() {
                }.getType();
                ArrayList<Course> courses1 = gson.fromJson(array.toString(), type);
                coursedata.add(new Course(-1, "Please Select Course", "कृपया कोर्स निवडा"));
                coursedata.addAll(courses1);
                CoursesAdapter spadapter = new CoursesAdapter(ExamListActivity.this, coursedata, lang);
                sp.setAdapter(spadapter);
            }
        } catch (Exception e) {
            reporterror(tag, e.toString());
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        try {
            GlobalValues.mdataset_New.clear();
            GlobalValues.mdataset_Attempted.clear();
            GlobalValues.mdataset_Missed.clear();
            GlobalValues.selectedtesttype = "";
        } catch (Exception e) {
            reporterror(tag, e.toString());
            e.printStackTrace();
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void syncdata(Exam exam) {
        try {
            JSONArray array = DatabaseHelper.getInstance(ExamListActivity.this).getansdata(exam.getExamid(), exam.getLanguages());
            if (array.length() > 0) {
                try {
                    JSONObject objdata = array.getJSONObject(0);
                    JSONObject obj = new JSONObject(objdata.getString(DatabaseHelper.JSONDATA));
                    JSONObject obj1 = new JSONObject(objdata.getString(DatabaseHelper.ANSWERDATA));
                    JSONObject obj2 = new JSONObject(objdata.getString(DatabaseHelper.STARTTIMEOBJ));
                    long id = objdata.getLong(DatabaseHelper.ID);
                    //if (InternetUtils.getInstance(ExamListActivity.this).available()) {
                    genloading(getResources().getString(R.string.loading));
                    ConnectionManager.getInstance(ExamListActivity.this).SAVE_ANSWERSHEET(obj.toString(), obj1.toString(), obj2.toString(), id);
               /* } else {
                    Toast.makeText(getApplicationContext(), "No Network available", Toast.LENGTH_LONG).show();
                }*/
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                if (!((Activity) context).isFinishing()) {
                    Toast.makeText(getApplicationContext(), "No offline data available", Toast.LENGTH_LONG).show();
                } else {
                }
            }
        } catch (Exception e) {
            reporterror(tag, e.toString());
            e.printStackTrace();
        }
    }

    int flag = -1;
   // int selectedposition = 0;

    public interface DataFromActivityToFragment {
        void sendData(ArrayList<Exam> data);
    }

    public void downloaddata(Exam exam) {
        try {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);
            } else {
                this.flag = flag;
               // selectedposition = 0;
                //   if (InternetUtils.getInstance(ExamListActivity.this).available()) {
                try {
                    genloading(getResources().getString(R.string.loading));
                    if (dialog != null)
                        dialog.setCancelable(true);
                    JSONObject obj = new JSONObject();
                    obj.put("examid", exam.getExamid());
                    if (lang.equals("mr")) {
                       exam.setLanguages("marathi");
                    } else {
                       exam.setLanguages("ENGLISH");
                    }
                    obj.put("language", "ENGLISH");
                    obj.put("ImageRelPath", "http://" + GlobalValues.IP);
                    JSONObject mainobj = new JSONObject();
                    mainobj.put("FilterParameter", obj.toString());
                    ConnectionManager.getInstance(ExamListActivity.this).getquestion(mainobj.toString(), exam.getExamid(), exam.getLanguages());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            reporterror(tag, e.toString());
            e.printStackTrace();
        }
     /*   } else {
            Toast.makeText(getApplicationContext(), "No Network", Toast.LENGTH_LONG).show();
        }*/
    }

    public void maildata(Exam exam) {
        try {
          //  if(mdataset.size()>position) {
            mailselectedexam = exam;
                JSONArray array = DatabaseHelper.getInstance(ExamListActivity.this).getansdata1(exam.getExamid(), exam.getLanguages());

                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);
                } else {
                    if (array.length() > 0) {
                        genloading(getResources().getString(R.string.loading));
                        StructureClass.clearAll();
                        filesdata.clear();
                        createfiles(array);
                    } else {
                        if (!((Activity) context).isFinishing()) {
                            Toast.makeText(getApplicationContext(), "No data available", Toast.LENGTH_LONG).show();
                        } else {

                        }
                    }
                }
            //}
        } catch (Exception e) {
            reporterror(tag, e.toString());
            e.printStackTrace();
        }
    }

    public void createfiles(JSONArray array) {
        try {
            File f = File.createTempFile(Constants.FILE_TEMP_NAME, Constants.FILE_NAME_EXT1, new File(StructureClass.generate(context.getResources().getString(R.string.storage_name))));
            BufferedWriter out = new BufferedWriter(new FileWriter(f.getAbsolutePath()));
            try {
                out.write(array.toString());
            } catch (IOException e) {
                System.out.println("Exception ");

            } finally {
                out.close();
            }
            Uri path = Uri.fromFile(f);
            filesdata.add(path);
            sendmail();
            //  }
        } catch (Exception e) {
            reporterror(tag, e.toString());
            e.printStackTrace();
        }
    }

    private void sendmail() {
        try {
            final Intent emailIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
            emailIntent.setType("text/plain");
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Examination Data");
            String to[] = {"targeteducareapp@gmail.com"};
            emailIntent.putExtra(Intent.EXTRA_EMAIL, to);
            emailIntent.putExtra(Intent.EXTRA_TEXT, "Please find the attachment.");
            emailIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, filesdata);
            dismissLoading();
            final PackageManager pm = context.getPackageManager();
            final java.util.List<ResolveInfo> matches = pm.queryIntentActivities(emailIntent, 0);
            ResolveInfo best = null;
            for (final ResolveInfo info : matches)
                if (info.activityInfo.packageName.endsWith(".gm") ||
                        info.activityInfo.name.toLowerCase().contains("gmail")) best = info;
            if (best != null)
                emailIntent.setClassName(best.activityInfo.packageName, best.activityInfo.name);
            emailIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, filesdata);

            if (best == null) {
                startActivity(Intent.createChooser(emailIntent, "Email:"));
            } else {
                context.startActivity(emailIntent);
            }
           // selectedposition = -1;
        } catch (Exception e) {
            reporterror(tag, e.toString());
            e.printStackTrace();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        try {
            adapter = new ExamAdapter(ExamListActivity.this, mdataset, lang,type);
            for (int i = 0; i < mdataset.size(); i++) {
                JSONArray array = DatabaseHelper.getInstance(ExamListActivity.this).getexamdetails(mdataset.get(i).getExamid(), mdataset.get(i).getExam_type());
                if (array.length() > 0) {
                    JSONObject obj = array.getJSONObject(0);
                    mdataset.get(i).setSkipp(obj.getInt(DatabaseHelper.SKIPP));
                    mdataset.get(i).setAnswered(obj.getInt(DatabaseHelper.ANSWERED));
                    mdataset.get(i).setWrong(obj.getInt(DatabaseHelper.WRONG));
                    mdataset.get(i).setCorrect(obj.getInt(DatabaseHelper.CORRECT));
                    mdataset.get(i).setProgress(obj.getDouble(DatabaseHelper.PROGRESS));
                    mdataset.get(i).setTimetaken(obj.getLong(DatabaseHelper.TIMETAKEN));
                    mdataset.get(i).setSpeed(obj.getDouble(DatabaseHelper.SPEED));
                }
            }

            adapter.notifyDataSetChanged();
            Intent intent = new Intent("sendingOnResume");
            intent.putExtra("datafromactivity", mdataset);
            sendBroadcast(intent);
        } catch (Exception e) {
            reporterror(tag, e.toString());
            e.printStackTrace();
        }
    }
}
