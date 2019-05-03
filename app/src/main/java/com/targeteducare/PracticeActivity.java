package com.targeteducare;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;

import android.text.Html;
import android.util.Log;

import android.view.LayoutInflater;

import android.view.View;
import android.support.design.widget.NavigationView;

import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.WindowManager;

import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.targeteducare.Adapter.ItemClickListener;
import com.targeteducare.Adapter.PracticeAdapter;
import com.targeteducare.Adapter.PracticeTestAdapter;

import com.targeteducare.Adapter.Section;

import com.targeteducare.Classes.Exam;
import com.targeteducare.Classes.Item;

import com.targeteducare.Classes.Question;
import com.targeteducare.database.DatabaseHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class PracticeActivity extends Activitycommon implements NavigationView.OnNavigationItemSelectedListener, ItemClickListener, Html.ImageGetter {
    RecyclerView mRecyclerView;
    static final int ITEMS = 10;
    PracticeAdapter mAdapter;
    private ArrayList<Exam> practiceTestModels;
    ViewPager mPager;
    TextView txt1, txt2, txt3, txt4, txt5, txt6, txt7, txt8, txt9, txt10, txt11, txt12;
    //SectionedExpandableLayoutHelper sectionedExpandableLayoutHelper;
    ArrayList<Question> qdata;

    Button bt1, bt2, bt3, bt4, bt5;
    Exam exam;
    int notanswered = 0;
    int answered = 0;
    int review = 0;
    long id = 0;
    int skipped = 0;
    //DrawerLayout drawer;
    CountDownTimer timer;
    boolean isshown = false;
    String starttime = "";



    int alertbefore = 10;
    ImageView imgright;
    TextView title;
    ImageView icback;
    //TextView result;
    ProgressBar progress;
    TextView progresstext;
    TextView timertxt;
    ImageView resumebtn;
    Timer t;
    boolean isresume = false;

    private PracticeTestAdapter practiceTestAdapter;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_practice);
        Log.e("I am in ", "PracticeActivity");
        qdata = new ArrayList<>();
        registerreceiver();
        setmaterialDesign();

        progress = (ProgressBar) findViewById(R.id.progressBar);
        progresstext = (TextView) findViewById(R.id.pregressupdates);
        timertxt = (TextView) findViewById(R.id.timertxt);

        /*timertxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("data ", "data " + qdata.get(mPager.getCurrentItem()).isIsskipped());

            }
        });*/

        resumebtn = (ImageView) findViewById(R.id.timeview);
        startTime = SystemClock.uptimeMillis();
        customHandler.postDelayed(updateTimerThread, 0);


        t = new Timer();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                t.scheduleAtFixedRate(new TimerTask() {
                                          @Override
                                          public void run() {
                                              runOnUiThread(new Runnable() {
                                                  @Override
                                                  public void run() {
                                                      /*timeused=timeused+1000;
                                                      long hour = (((timeused / 1000) / 60)) / 60;
                                                      long   min = (((timeused / 1000) / 60)) % 60;
                                                      long sec = ((timeused / 1000) % 60);
                                                      timertxt.setText(hour+":"+min+":"+sec)*/
                                                      ;
                                                  }
                                              });

                                          }
                                      },
                        //Set how long before to start calling the TimerTask (in milliseconds)
                        0,
                        //Set the amount of time between each execution (in milliseconds)
                        1000);
            }
        });


        resumebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isresume) {
                    isresume = false;
                    startTime = SystemClock.uptimeMillis();
                    customHandler.postDelayed(updateTimerThread, 0);
                    resumebtn.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause));
                    //onResume();
                } else {
                    isresume = true;
                    timeSwapBuff += timeInMilliseconds;
                    customHandler.removeCallbacks(updateTimerThread);
                    resumebtn.setImageDrawable(getResources().getDrawable(R.drawable.ic_resume));
                    //onPause();
                    /*practiceTestModels = new ArrayList<Exam>();
                    PracticeTestActivity p = new PracticeTestActivity();
                    p.parseexamdata(GlobalValues.TEMP_STR);*/

                    practiceTestModels = new ArrayList<Exam>();
                    practiceTestAdapter = new PracticeTestAdapter(PracticeActivity.this, practiceTestModels);


                    Intent ip = new Intent(PracticeActivity.this, ProgressReportActivity.class);
                    ip.putExtra("progressreport", practiceTestModels);
                    ip.putExtra("flag", 1);
                    startActivity(ip);
                }
            }
        });
        //result = (TextView) findViewById(R.id.textview_result);
        //drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        Log.e("PracticeActivity ", "PracticeActivity");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Bundle b = getIntent().getExtras();
        if (b != null) {
            if (b.containsKey("exam")) {
                this.exam = (Exam) b.getSerializable("exam");
                setTitle(exam.getExamname());
                title = (TextView) findViewById(R.id.title);
                title.setText(exam.getExamname());
                //result.setText(exam.getExamname());
            }
        }
        /*ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (drawer.isDrawerOpen(Gravity.RIGHT)) {
                    drawer.closeDrawer(Gravity.RIGHT);
                } else {
                    drawer.openDrawer(Gravity.RIGHT);
                }
            }
        });*/

        /*ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        }; // Drawer Toggle Object Made
        drawer.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });
        imgright = (ImageView) findViewById(R.id.menuRight);
        imgright.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawer.isDrawerOpen(GravityCompat.END)) {
                    drawer.closeDrawer(GravityCompat.END);
                } else {
                    drawer.openDrawer(GravityCompat.END);
                }
            }
        });*/

        /*icback = (ImageView) findViewById(R.id.menuback);
        icback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                *//*if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else if (drawer.isDrawerOpen(GravityCompat.END)) {
                    drawer.closeDrawer(GravityCompat.END);
                } else {
                    PracticeActivity.this.finish();
                }*//*
                PracticeActivity.this.finish();
            }
        });*/
        back();

        bt1 = (Button) findViewById(R.id.first);
        bt1.setVisibility(View.GONE);
        bt2 = (Button) findViewById(R.id.previous);
        bt3 = (Button) findViewById(R.id.next);
        bt3.setText("Skip");
        bt4 = (Button) findViewById(R.id.last_button);
        bt4.setVisibility(View.GONE);
        bt5 = findViewById(R.id.submitquestion);


        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (qdata.size() > 1) {
                    mPager.setCurrentItem(0);
                    bt2.setEnabled(false);
                    bt3.setEnabled(true);
                }
            }
        });


        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentpgid = mPager.getCurrentItem() - 1;
                if ((currentpgid) >= 0 || (currentpgid) < qdata.size()) {
                    mPager.setCurrentItem((currentpgid));
                }
                if (currentpgid == 0) {
                    bt2.setEnabled(false);
                    bt3.setEnabled(true);
                }

                if (currentpgid < ((qdata.size() - 1))) {
                    bt3.setEnabled(true);
                }
            }
        });

        bt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //getanswersheet
                skipped = skipped + 1;
                int currentpgid = mPager.getCurrentItem() + 1;
                if ((currentpgid) < qdata.size()) {
                    mPager.setCurrentItem((currentpgid));
                }
                if (currentpgid == (qdata.size() - 1)) {
                    bt3.setEnabled(false);
                }
                if (currentpgid > 0) {
                    bt2.setEnabled(true);
                }
            }
        });

        bt4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (qdata.size() > 1) {
                    int id = (qdata.size() - 1);
                    mPager.setCurrentItem(id);
                    bt3.setEnabled(false);
                    bt2.setEnabled(true);
                    Log.e("current ", "" + id);
                }
            }
        });


        bt5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*if(qdata.get(mPager.getCurrentItem()).isIsskipped()){
                    //bt5.setEnabled(false);
                }
                else{
                    bt5.setEnabled(true);
                }*/

                    int currentpgid = mPager.getCurrentItem()+1;
                    if(currentpgid<qdata.size()){
                        mPager.setCurrentItem(currentpgid);
                        /*Toast.makeText(PracticeActivity.this, "You took "+time_taken_per_question+ " seconds for this question"
                                , Toast.LENGTH_SHORT).show();*/

                        savedataonbackorsubmit();
                    }
                    else{
                        mPager.setCurrentItem(currentpgid-1);
                        opendialog("Do you want to submit the test to know your scores?",2);
                    }

            }
        });


        txt1 = (TextView) findViewById(R.id.textview_review);
        txt1.setVisibility(View.GONE);
        //txt2 = (TextView) findViewById(R.id.textview_timer);
        txt3 = (TextView) findViewById(R.id.textview_submit);
        //txt3.setVisibility(View.GONE);
        //txt4 = (TextView) findViewById(R.id.textview_duration);
        //txt5 = (TextView) findViewById(R.id.textview_timeleft);
        //txt5.setVisibility(View.GONE);
        //txt6 = (TextView) findViewById(R.id.textview_timeextended);
        //txt7 = (TextView) findViewById(R.id.textview_question);
        //txt8 = (TextView) findViewById(R.id.textview_totalmarks);
        ////txt9 = (TextView) findViewById(R.id.textview_negativemarks);
        //txt10 = (TextView) findViewById(R.id.textview_attemptedquestions);
        //txt11 = (TextView) findViewById(R.id.textview_totalnotattemtedq);
        //txt12 = (TextView) findViewById(R.id.textview_totalreviewq);

        //txt1.setTypeface(Fonter.getTypefaceregular(PracticeActivity.this));
        //txt2.setTypeface(Fonter.getTypefaceregular(PracticeActivity.this));
        //txt2.setVisibility(View.GONE);
        //txt3.setTypeface(Fonter.getTypefaceregular(PracticeActivity.this));
        //txt3.setVisibility(View.GONE);
        //txt4.setTypeface(Fonter.getTypefacebold(PracticeActivity.this));
        //txt4.setVisibility(View.GONE);
        //txt5.setTypeface(Fonter.getTypefacebold(PracticeActivity.this));
        //txt6.setTypeface(Fonter.getTypefacebold(PracticeActivity.this));
        //txt7.setTypeface(Fonter.getTypefacebold(PracticeActivity.this));
        //txt8.setTypeface(Fonter.getTypefacebold(PracticeActivity.this));
        //txt9.setTypeface(Fonter.getTypefacebold(PracticeActivity.this));
        //txt10.setTypeface(Fonter.getTypefacebold(PracticeActivity.this));
        //txt11.setTypeface(Fonter.getTypefacebold(PracticeActivity.this));
        //txt12.setTypeface(Fonter.getTypefacebold(PracticeActivity.this));
        //txt4.setText(": " + exam.getDurationinMin() + " Mins");
        //txt9.setText(": " + exam.getNeagativemarks());
        //txt8.setText(": " + exam.getMarks());
        //txt10.setText(": " + answered);
        //txt11.setText(": " + notanswered);
        //txt12.setText(": " + review);*/
        txt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                opendialog("Want to check your progress?", 2);
                //opendialog("Do you wanna submit the test?", 1);
                //submitdata();
            }
        });

        /*txt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  if (qdata.get(mPager.getCurrentItem()).isIsanswered()) {
                qdata.get(mPager.getCurrentItem()).setIsreview(!qdata.get(mPager.getCurrentItem()).isIsreview());
                if (qdata.get(mPager.getCurrentItem()).isIsreview()) {
                    for (int i = 0; i < items.size(); i++) {
                        if (items.get(i).getSubjectid() == qdata.get(mPager.getCurrentItem()).getSubjectid()) {
                            items.get(i).setReview(items.get(i).getReview() + 1);
                            // items.get(i).setTotal(items.get(i).getTotalnotanswered()-1);
                            //   sectionedExpandableLayoutHelper.notifyDataSetChanged();
                        }
                    }

                    txt1.setText("Remove Review");
                    review = review + 1;
                    if (qdata.get(mPager.getCurrentItem()).isIsanswered()) {
                        Log.e("isanswered ", "is answerred ");
                        answered = answered - 1;
                        qdata.get(mPager.getCurrentItem()).setIsanswered(false);
                        for (int i = 0; i < qdata.get(mPager.getCurrentItem()).getOptions().size(); i++) {
                            qdata.get(mPager.getCurrentItem()).getOptions().get(i).setSelected(false);
                        }

                        for (int i = 0; i < items.size(); i++) {
                            if (items.get(i).getSubjectid() == qdata.get(mPager.getCurrentItem()).getSubjectid()) {
                                items.get(i).setTotalnotanswered(items.get(i).getTotalnotanswered() + 1);
                                // items.get(i).setTotal(items.get(i).getTotalnotanswered()-1);
                                //   sectionedExpandableLayoutHelper.notifyDataSetChanged();
                            }
                        }
                    }
                    mAdapter.notifyDataSetChanged();
                    try {
                        Intent intent = new Intent("QuestionUpdated");
                        intent.putExtra("Qid", qdata.get(mPager.getCurrentItem()).getId());
                        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    for (int i = 0; i < items.size(); i++) {
                        if (items.get(i).getSubjectid() == qdata.get(mPager.getCurrentItem()).getSubjectid()) {
                            items.get(i).setReview(items.get(i).getReview() - 1);
                            // items.get(i).setTotal(items.get(i).getTotalnotanswered()-1);
                            //   sectionedExpandableLayoutHelper.notifyDataSetChanged();
                        }
                    }
                    txt1.setText("Review");
                    if (review > 0)
                        review = review - 1;
                   *//* if (qdata.get(mPager.getCurrentItem()).isIsanswered())
                        answered = answered + 1;*//*
                }
                sectionedExpandableLayoutHelper.notifyDataSetChanged();
                txt10.setText(": " + answered);
                txt11.setText(": " + notanswered);
                txt12.setText(": " + review);
              *//*  } else {
                    if (!((Activity) context).isFinishing()) {
                        Toast.makeText(getApplicationContext(), "Not answered", Toast.LENGTH_LONG).show();
                    } else {
                        Log.e("no activity", "no activity ");
                    }
                }*//*
            }
        });*/

        final long time = Long.parseLong(exam.getDurationinMin()) * 60 * 1000;
        timer = new CountDownTimer(time, 1000) {
            @Override
            public void onTick(long l) {
                long l1 = time - l;
                long hour = (l / 1000) / (60 * 60);

                long min = (l / 1000) % (60 * 60);
                long sec = min % 60;
                hour = (((l / 1000) / 60)) / 60;
                min = (((l / 1000) / 60)) % 60;
                sec = ((l / 1000) % 60);
                String s = ((l / 1000) / 60) + ":" + ((l / 1000) % 60);
                String s1 = ((l1 / 1000) / 60) + ":" + ((l1 / 1000) % 60);
                //txt2.setText(s);

                //txt2.setText(hour + ":" + min + ":" + sec);
                //txt5.setText(": " + hour + ":" + min + ":" + sec);
                //txt5.setVisibility(View.GONE);

                long hour1 = (((l1 / 1000) / 60)) / 60;
                long min1 = (((l1 / 1000) / 60)) % 60;
                long sec1 = ((l1 / 1000) % 60);

                /*txt6.setText(": " + hour1 + ":" + min1 + ":" + sec1);
                if (l <= (alertbefore * 60000)) {
                    if (!isshown) {
                        opendialog("This exam will be submitting after " + alertbefore + " min automatically. Please Review and compile your exam.", 0);
                        isshown = true;
                    }
                }*/
            }

            @Override
            public void onFinish() {
                submitdata();
            }
        };

        /*ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();*/

        //NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        //navigationView.setNavigationItemSelectedListener(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        //sectionedExpandableLayoutHelper = new SectionedExpandableLayoutHelper(this, mRecyclerView, this, 6, false);

        //sectionedExpandableLayoutHelper.notifyDataSetChanged();
        mPager = (ViewPager) findViewById(R.id.pager);
        mAdapter = new PracticeAdapter(getSupportFragmentManager(), qdata, false);
        mPager.setAdapter(mAdapter);
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (qdata.get(position).isIsreview()) {
                    //txt1.setText("Remove Review");
                } else {
                    //txt1.setText("Review");
                }
                qdata.get(position).setIsvisited(true);

                //sectionedExpandableLayoutHelper.notifyDataSetChanged();
            }

            @Override
            public void onPageSelected(int position) {

                time_spent_current_page = SystemClock.uptimeMillis();


                total_time_per_question = qdata.get(position).getTimeperquestion();
                total_time_per_question = total_time_per_question+time_taken_per_question;

                qdata.get(position).setTimeperquestion((int)total_time_per_question);
                Log.e("You took ", total_time_per_question+ " seconds for question number "+position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

                time_taken_onpagechange = SystemClock.uptimeMillis();
                time_taken_per_question = (time_taken_onpagechange-time_spent_current_page)/1000;

            }
        });

        registerreceiver();


        JSONArray array = DatabaseHelper.getInstance(PracticeActivity.this).getqdata(exam.getExamid(), exam.getLanguages());
        Log.e("qdata ", "qdata " + array.toString());
        if (array.length() > 0) {
            try {
                //Log.e("via db ", "via db");
                id = array.getJSONObject(0).getLong(DatabaseHelper.ID);
                JSONObject obj = new JSONObject(array.getJSONObject(0).getString(DatabaseHelper.JSONDATA));
                parsedata(obj);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                genloading("loading");
                JSONObject obj = new JSONObject();
                obj.put("examid", exam.getExamid());
                obj.put("language", exam.getLanguages());
                obj.put("ImageRelPath", "http://" + GlobalValues.IP);
                JSONObject mainobj = new JSONObject();
                mainobj.put("FilterParameter", obj.toString());
                ConnectionManager.getInstance(PracticeActivity.this).getquestion(mainobj.toString(), exam.getExamid(), exam.getLanguages());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void notifyqdatachanged(Question q) {
        Log.e("notified ", "notified ");
        if (notanswered > 0)
            notanswered = notanswered - 1;
        answered = answered + 1;

        if (q.isIsreview()) {
            review = review - 1;
            q.setIsreview(false);
            //txt1.setText("Review");

            for (int i = 0; i < items.size(); i++) {
                if (items.get(i).getSubjectid() == qdata.get(mPager.getCurrentItem()).getSubjectid()) {
                    //items.get(i).setReview(items.get(i).getReview() - 1);
                    items.get(i).setTotalnotanswered(items.get(i).getTotalnotanswered() - 1);
                    //sectionedExpandableLayoutHelper.notifyDataSetChanged();
                }
            }
        }
        //txt10.setText(": " + answered);
        //txt11.setText(": " + notanswered);
        //txt12.setText(": " + review);
        //sectionedExpandableLayoutHelper.notifyDataSetChanged();

        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getSubjectid() == q.getSubjectid()) {
                items.get(i).setTotalnotanswered(items.get(i).getTotalnotanswered() - 1);
                // items.get(i).setTotal(items.get(i).getTotalnotanswered()-1);
                //sectionedExpandableLayoutHelper.notifyDataSetChanged();
            }
        }


        int x = (int) (((double) answered / (double) qdata.size()) * 100);
        Log.e("data ", "data " + answered + " " + qdata.size() + " " + x);
        progresstext.setText(x + "%");
        progress.setProgress(x);
    }

    boolean flag = false;

    @Override
    protected void onResponsed(int statuscode, int accesscode, String data) {
        super.onResponsed(statuscode, accesscode, data);
        if (statuscode == Constants.STATUS_OK) {
            if (accesscode == Connection.GET_QUESTIONS.ordinal()) {
                try {
                    JSONObject obj = new JSONObject(GlobalValues.TEMP_STR).getJSONObject("root").getJSONObject("subroot");
                    parsedata(obj);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (accesscode == Connection.GET_QUESTIONSEXCEPTION.ordinal()) {
                try {
                    if (!((Activity) context).isFinishing()) {
                        Toast.makeText(getApplicationContext(), Constants.connectiontimeout, Toast.LENGTH_LONG).show();
                    } else {
                        Log.e("no activity", "no activity ");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (accesscode == Connection.SAVE_ANSWEREXCEPTION.ordinal()) {
                try {
                    if (!((Activity) context).isFinishing()) {
                        Toast.makeText(getApplicationContext(), Constants.connectiontimeout, Toast.LENGTH_LONG).show();
                    } else {
                        Log.e("no activity", "no activity ");
                    }
                    try {
                        if (!flag) {
                            Log.e("received", "received " + mainobj.toString());
                            JSONArray questions = new JSONArray();

                            for (int i = 0; i < qdata.size(); i++) {
                                questions.put(qdata.get(i).getJsonObject(qdata.get(i)));
                            }
                            Log.e("mainobj", "mainobj " + mainobj.toString());
                            mainobj.put("questions", questions);
                            Log.e("Mainobj", "moainobj " + mainobj.toString());
                            Log.e("mainobj11 ", "questions " + mainobj.getJSONArray("questions").toString());
                            ContentValues c = new ContentValues();
                            c.put(DatabaseHelper.JSONDATA, mainobj.toString());
                            c.put(DatabaseHelper.LANGUAGE, exam.getLanguages());
                            c.put(DatabaseHelper.EXAMID, exam.getExamid());
                            c.put(DatabaseHelper.SAVEDTIME, DateUtils.getSqliteTime());
                            /*long id = DatabaseHelper.getInstance(context).saveqdata(c,  exam.getExamid(), exam.getLanguages());*/
                            try {
                                Intent intent = new Intent(Constants.BROADCAST_WIZARD);
                                intent.putExtra(Constants.BROADCAST_RESPONSE_CODE, 200);
                                intent.putExtra(Constants.BROADCAST_URL_ACCESS, Connection.Dataupdated.ordinal());
                                intent.putExtra(Constants.BROADCAST_DATA, Integer.toString(exam.getExamid()));
                                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            flag = true;
                            DatabaseHelper.getInstance(PracticeActivity.this).saveqdata(c, exam.getExamid(), exam.getLanguages());
                            if (exam.isInstantExamResult() || exam.isInstantExamResultWithAns()) {
                                Intent intent = new Intent(PracticeActivity.this, AnswersheetActivity.class);
                                intent.putExtra("exam", exam);
                                startActivity(intent);
                                this.finish();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (accesscode == Connection.SAVE_ANSWER.ordinal()) {
                try {
                    if (!flag) {
                        Log.e("received", "received " + mainobj.toString());
                        JSONArray questions = new JSONArray();

                        for (int i = 0; i < qdata.size(); i++) {
                            questions.put(qdata.get(i).getJsonObject(qdata.get(i)));
                        }
                        Log.e("mainobj", "mainobj " + mainobj.toString());
                        mainobj.put("questions", questions);
                        Log.e("Mainobj", "moainobj " + mainobj.toString());
                        Log.e("mainobj11 ", "questions " + mainobj.getJSONArray("questions").toString());
                        ContentValues c = new ContentValues();
                        c.put(DatabaseHelper.JSONDATA, mainobj.toString());
                        c.put(DatabaseHelper.LANGUAGE, exam.getLanguages());
                        c.put(DatabaseHelper.EXAMID, exam.getExamid());
                        c.put(DatabaseHelper.SAVEDTIME, DateUtils.getSqliteTime());
                        /*long id = DatabaseHelper.getInstance(context).saveqdata(c,  exam.getExamid(), exam.getLanguages());*/
                        try {
                            Intent intent = new Intent(Constants.BROADCAST_WIZARD);
                            intent.putExtra(Constants.BROADCAST_RESPONSE_CODE, 200);
                            intent.putExtra(Constants.BROADCAST_URL_ACCESS, Connection.Dataupdated.ordinal());
                            intent.putExtra(Constants.BROADCAST_DATA, Integer.toString(exam.getExamid()));
                            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        flag = true;
                        DatabaseHelper.getInstance(PracticeActivity.this).saveqdata(c, exam.getExamid(), exam.getLanguages());
                        if (exam.isInstantExamResult() || exam.isInstantExamResultWithAns()) {
                            Intent intent = new Intent(PracticeActivity.this, AnswersheetActivity.class);
                            intent.putExtra("exam", exam);
                            startActivity(intent);
                            this.finish();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    JSONObject mainobj;
    ArrayList<Item> items;

    public void parsedata(JSONObject obj) {
        try {
            Log.e("objexam ", "obj " + obj.toString());
            mainobj = obj;
            HashMap<Item, ArrayList<Item>> mSectionMap = new HashMap<Item, ArrayList<Item>>();
            items = new ArrayList<>();
            //sectionedExpandableLayoutHelper.removeallSection();
            JSONObject subobj = obj.getJSONObject("subjects").optJSONObject("questions");
            if (subobj != null) {
                Item item = new Item();
                item.setName(subobj.getJSONObject("subject").getString("subjectname"));
                item.setNoofques(subobj.getString("noofques"));
                item.setSubjectid(subobj.getInt("subjectid"));
                items.add(item);
                ArrayList<Item> arrayList = new ArrayList<>();
            } else {
                JSONArray subarry = obj.getJSONObject("subjects").optJSONArray("questions");
                for (int i = 0; i < subarry.length(); i++) {
                    subobj = subarry.getJSONObject(i);
                    Item item = new Item();
                    // if(subobj.getJSONObject("subject").opt("subjectname")!=null)
                    item.setName(subobj.getJSONObject("subject").getString("subjectname"));
                    item.setNoofques(subobj.getString("noofques"));
                    item.setSubjectid(subobj.getInt("subjectid"));
                    items.add(item);
                    ArrayList<Item> arrayList = new ArrayList<>();
                    mSectionMap.put(item, arrayList);
                }
            }

            JSONArray array = obj.optJSONArray("questions");
            ArrayList<Question> quesdata = new ArrayList<>();
            if (array != null) {

                for (int i = 0; i < array.length(); i++) {
                    quesdata.add(new Question(array.getJSONObject(i)));
                }

            } else {
                JSONObject obj1 = obj.optJSONObject("questions");
                quesdata.add(new Question(obj1));
            }
            if (quesdata != null) {
                qdata.clear();
                qdata.addAll(quesdata);
                mAdapter.notifyDataSetChanged();
                mAdapter = new PracticeAdapter(getSupportFragmentManager(), qdata, false);
                mPager.setAdapter(mAdapter);
            }

            if (quesdata != null) {
                for (int i = 0; i < quesdata.size(); i++) {
                    for (int j = 0; j < items.size(); j++) {
                        if (items.get(j).getSubjectid() == quesdata.get(i).getSubjectid()) {
                            quesdata.get(i).setDisplayid(items.get(j).getQdata().size() + 1);
                            items.get(j).getQdata().add(quesdata.get(i));
                            break;
                        }
                    }
                }
            }

            for (int j = 0; j < items.size(); j++) {
                items.get(j).setTotalnotanswered(items.get(j).getQdata().size());
            }

            for (int i = 0; i < items.size(); i++) {
                //sectionedExpandableLayoutHelper.addSection(items.get(i));
                //sectionedExpandableLayoutHelper.notifyDataSetChanged();
            }

            //txt7.setText(": " + qdata.size());
            notanswered = qdata.size();
            //txt10.setText(": " + answered);
            //txt11.setText(": " + notanswered);
            //txt12.setText(": " + review);

            Calendar cal = Calendar.getInstance();
            starttime = DateUtils.getSqliteTime();

            JSONObject configobj = obj.optJSONObject("config");
            if (configobj != null)
                alertbefore = configobj.getInt("AlertBefore");
            timer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onBackPressed() {

        Log.e("backpress ", "backpress ");
        savedataonbackorsubmit();
        PracticeActivity.this.finish();

        /*if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
        } else {
            Log.e("backpress ","backpress ");
            ContentValues c = new ContentValues();
            c.put(DatabaseHelper.EXAMID, exam.getExamid());
            c.put(DatabaseHelper.TIMETAKEN, updatedTime);
            c.put(DatabaseHelper.ANSWERED, answered);
            c.put(DatabaseHelper.SKIPP, 0);
            c.put(DatabaseHelper.CORRECT, answered);
            c.put(DatabaseHelper.WRONG, 0);
            c.put(DatabaseHelper.EXAMTYPE,exam.getExam_type());
            c.put(DatabaseHelper.PROGRESS,progresstext.getText().toString().replace("%",""));
            long sp=0;
            if(answered!=0)
             sp = ((long) updatedTime / (long) answered);
            long sec = ((sp / 1000) % 60);
            c.put(DatabaseHelper.SPEED, Long.toString(sec));
            Log.e("data ","data "+c.toString());
            DatabaseHelper.getInstance(PracticeActivity.this).saveexaminationdetails(c,exam.getExamid());
            updateexaminationdetails();
            PracticeActivity.this.finish();
        }*/
    }

    private void savedataonbackorsubmit() {
        ContentValues c = new ContentValues();
        c.put(DatabaseHelper.EXAMID, exam.getExamid());
        c.put(DatabaseHelper.TIMETAKEN, updatedTime);
        c.put(DatabaseHelper.ANSWERED, answered);
        c.put(DatabaseHelper.SKIPP, 0);
        c.put(DatabaseHelper.CORRECT, answered);
        c.put(DatabaseHelper.WRONG, 0);
        c.put(DatabaseHelper.EXAMTYPE, exam.getExam_type());
        c.put(DatabaseHelper.PROGRESS, progresstext.getText().toString().replace("%", ""));
        long sec = 1;
        long sp = 1;
        if (answered != 0) {
            sp = ((long) updatedTime / (long) answered);
            sec = ((sp / 1000) % 60);
        } else {
            //Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
        }
        c.put(DatabaseHelper.SPEED, Long.toString(sec));
        Log.e("data ", "data " + c.toString());


        DatabaseHelper.getInstance(PracticeActivity.this).saveexaminationdetails(c, exam.getExamid());

        //Take data from response generated through service call ---> save in database
        Gson gson = new Gson();
        try {
            JSONArray array = new JSONArray();
            for (int i = 0; i < qdata.size(); i++) {
                JSONObject obj = new JSONObject(gson.toJson(qdata.get(i)));
                JSONObject QuestionType = new JSONObject();
                JSONObject QuestionType1 = new JSONObject();
                obj.put("QuestionType", QuestionType);
                QuestionType.put("QuestionType", QuestionType1);
                QuestionType1.put("Id", qdata.get(i).getOptiontypeId());
                QuestionType1.put("Name", qdata.get(i).getOptiontypename());
                QuestionType1.put("IsActive", qdata.get(i).getOptiontypeisactive());


                JSONObject opt = new JSONObject();
                Gson gson1 = new Gson();
                JSONArray array1 = new JSONArray(gson1.toJson(qdata.get(i).getOptions()));
                opt.put("Options", array1);
                obj.put("Options", opt);
                array.put(obj);
            }


            mainobj.put("questions", array);

            //Add values in database
            ContentValues c1 = new ContentValues();
            c1.put(DatabaseHelper.JSONDATA, mainobj.toString());
            DatabaseHelper.getInstance(PracticeActivity.this).updateqdata(c1, exam.getExamid(), exam.getLanguages());
            Log.e("arrayata", "arraydata " + array.toString());
            Log.e("arrayata", "arraydata1 " + mainobj.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return true;
    }

   /* @Override
    public void itemClicked(Question item) {
        try {
            if (!((Activity) context).isFinishing()) {
                // Toast.makeText(this, "Item: " + item.getSrno() + " clicked", Toast.LENGTH_SHORT).show();
            } else {
                Log.e("no activity", "no activity ");
            }
            if (qdata.size() > (item.getSrno() - 1)) {
                mPager.setCurrentItem(item.getSrno() - 1);
                drawer.closeDrawer(GravityCompat.START);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    @Override
    public Drawable getDrawable(String s) {
        return null;
    }

    public void updateexaminationdetails() {
        ContentValues c1 = new ContentValues();
        c1.put(DatabaseHelper.EXAMID, exam.getExamid());
        c1.put(DatabaseHelper.TIMETAKEN, updatedTime);
        c1.put(DatabaseHelper.ANSWERED, answered);
        c1.put(DatabaseHelper.SKIPP, skipped);
        c1.put(DatabaseHelper.CORRECT, answered);
        c1.put(DatabaseHelper.WRONG, 0);
        //c1.put(DatabaseHelper.PRACTICE_QUESTION, qdata.size());
        c1.put(DatabaseHelper.EXAMTYPE, exam.getExam_type());
        c1.put(DatabaseHelper.PROGRESS, progresstext.getText().toString().replace("%", ""));

        long sec = 1;
        long sp = 1;
        if (answered != 0) {
            sp = ((long) updatedTime / (long) answered);
            sec = ((sp / 1000) % 60);
        } else {
            //opendialog("You haven't attempted a single question, do you still want to submit?", 1);
        }
        c1.put(DatabaseHelper.SPEED, Long.toString(sec));
        Log.e("data ", "data " + c1.toString());
        DatabaseHelper.getInstance(PracticeActivity.this).saveexaminationdetails(c1, exam.getExamid());
    }

    public void submitdata() {
        updateexaminationdetails();
        PracticeActivity.this.finish();
        if (dialog != null)
            dialog.dismiss();
        if (timer != null)
            timer.cancel();
        JSONArray array = new JSONArray();
        JSONArray array1 = new JSONArray();
        for (int i = 0; i < qdata.size(); i++) {
            String ans = "";
            for (int j = 0; j < qdata.get(i).getOptions().size(); j++) {
                if (qdata.get(i).getOptions().get(j).isSelected()) {
                    if (ans.length() > 0)
                        ans = ans + "," + qdata.get(i).getOptions().get(j).getId();
                    else
                        ans = "" + qdata.get(i).getOptions().get(j).getId();
                }
            }
            try {
                if (ans.length() > 0) {
                    JSONObject obj = new JSONObject();
                    obj.put("questionid", qdata.get(i).getId());
                    obj.put("answer", ans);
                    obj.put("subjectid", qdata.get(i).getSubjectid());
                    obj.put("unitid", qdata.get(i).getUnitid());
                    obj.put("srno", qdata.get(i).getSrno());
                    array.put(obj);
                }

                JSONObject obj1 = new JSONObject();
                obj1.put("quesid", qdata.get(i).getId());
                obj1.put("srno", qdata.get(i).getSrno());
                array1.put(obj1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        try {
            JSONObject obj = new JSONObject();
            obj.put("examid", exam.getExamid());
            obj.put("language", exam.getLanguages());
            obj.put("castecategory", "");
            obj.put("userid", GlobalValues.studentProfile.getId());
            obj.put("FilterParameter", array.toString());

            JSONObject root = new JSONObject();
            JSONObject subroot = new JSONObject();
            root.put("root", subroot);
            subroot.put("subroot", array1);

            JSONObject obj1 = new JSONObject();
            obj1.put("userid", GlobalValues.studentProfile.getId());
            obj1.put("examid", exam.getExamid());
            obj1.put("Type", "stop");
            obj1.put("examdatetime", DateUtils.getSqliteTime());
            obj1.put("filterparameter", root.toString());


            JSONObject obj2 = new JSONObject();
            obj2.put("userid", GlobalValues.studentProfile.getId());
            obj2.put("examid", exam.getExamid());
            obj2.put("Type", "start");
            obj2.put("examdatetime", starttime);
            obj2.put("filterparameter", root.toString());
            ContentValues c = new ContentValues();

            c.put(DatabaseHelper.JSONDATA, obj.toString());
            c.put(DatabaseHelper.ANSWERDATA, obj1.toString());
            c.put(DatabaseHelper.STARTTIMEOBJ, obj2.toString());
            c.put(DatabaseHelper.LANGUAGE, exam.getLanguages());
            c.put(DatabaseHelper.EXAMID, exam.getExamid());
            c.put(DatabaseHelper.SYNC, 0);
            c.put(DatabaseHelper.SAVEDTIME, DateUtils.getSqliteTime());
            long id = DatabaseHelper.getInstance(PracticeActivity.this).saveanswerdata(c);
            // if (InternetUtils.getInstance(PracticeActivity.this).available()) {
            Log.e("answerdataaaa", "anserdata " + obj.toString());
            genloading("loading..");
            ConnectionManager.getInstance(PracticeActivity.this).SAVE_ANSWERSHEET(obj.toString(), obj1.toString(), obj2.toString(), id);
            /*} else {
                Toast.makeText(getApplicationContext(), "Data saved offline Thank You", Toast.LENGTH_LONG).show();
            }*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    AlertDialog dialog = null;

    public void opendialog(String msg, int flag) {
        if (!((Activity) context).isFinishing()) {
            final AlertDialog.Builder alert = new AlertDialog.Builder(this);
            LayoutInflater inflater = getLayoutInflater();
            View alertLayout = inflater.inflate(R.layout.dialog_terms, null);
            alert.setView(alertLayout);
            final TextView txt = (TextView) alertLayout.findViewById(R.id.textview_terms);
            CheckBox cb = (CheckBox) alertLayout.findViewById(R.id.checkbox_1);
            cb.setVisibility(View.GONE);
            Button bt = (Button) alertLayout.findViewById(R.id.button_start);
            txt.setText(msg);
            bt.setVisibility(View.GONE);
            if (flag == 0) {
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
            } else if (flag == 1) {
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        submitdata();
                    }
                });
                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
            } else if (flag == 2) {
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(PracticeActivity.this, ProgressReportActivity.class);
                        i.putExtra("progressreport", practiceTestModels);
                        i.putExtra("flag", 1);
                        startActivity(i);
                        finish();
                    }
                });
                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
            }
            alert.setCancelable(true);
            dialog = alert.create();
            dialog.show();
        } else {
            Log.e("activity", "activity is not running");
        }

    }

    @Override
    public void itemClicked(Question item) {
        try {
            //  Toast.makeText(this, "Item: " + item.getSrno() + " clicked", Toast.LENGTH_SHORT).show();
            if (qdata.size() > (item.getSrno() - 1)) {
                mPager.setCurrentItem(item.getSrno() - 1);

                /*if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else if (drawer.isDrawerOpen(GravityCompat.END)) {
                    drawer.closeDrawer(GravityCompat.END);
                }*/

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void itemClicked(Section section) {

    }

    private long startTime = 0L;
    private Handler customHandler = new Handler();
    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedTime = 0L;

    long time_taken_per_question = 0L;
    long time_taken_onpagechange = 0L;
    long time_spent_current_page = 0L;

    long total_time_per_question = 0L;

    private Runnable updateTimerThread = new Runnable() {
        public void run() {
            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
            updatedTime = timeSwapBuff + timeInMilliseconds;
            long hour = (((updatedTime / 1000) / 60)) / 60;
            long min = (((updatedTime / 1000) / 60)) % 60;
            long sec = ((updatedTime / 1000) % 60);
            timertxt.setText(hour + ":" + min + ":" + sec);
           /* int secs = (int) (updatedTime / 1000);
            int mins = secs / 60;
            secs = secs % 60;
            int milliseconds = (int) (updatedTime % 1000);
            timertxt.setText("" + mins + ":"
                    + String.format("%02d", secs) + ":"
                    + String.format("%03d", milliseconds));*/
            customHandler.postDelayed(this, 0);
        }

    };

    @Override
    protected void onPostResume() {
        super.onPostResume();
        isresume = true;
        if (isresume) {
            isresume = false;
            startTime = SystemClock.uptimeMillis();
            customHandler.postDelayed(updateTimerThread, 0);
            resumebtn.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            for (int i = 0; i < practiceTestModels.size(); i++) {
                JSONArray array = DatabaseHelper.getInstance(PracticeActivity.this).getexamdetails(practiceTestModels.get(i).getExamid(), practiceTestModels.get(i).getExam_type());
                if (array.length() > 0) {
                    JSONObject obj = array.getJSONObject(0);
                    practiceTestModels.get(i).setSkipp(obj.getInt(DatabaseHelper.SKIPP));
                    practiceTestModels.get(i).setAnswered(obj.getInt(DatabaseHelper.ANSWERED));
                    practiceTestModels.get(i).setWrong(obj.getInt(DatabaseHelper.WRONG));
                    practiceTestModels.get(i).setCorrect(obj.getInt(DatabaseHelper.CORRECT));
                    practiceTestModels.get(i).setProgress(obj.getDouble(DatabaseHelper.PROGRESS));
                    practiceTestModels.get(i).setTimetaken(obj.getLong(DatabaseHelper.TIMETAKEN));
                    practiceTestModels.get(i).setSpeed(obj.getDouble(DatabaseHelper.SPEED));
                    //practiceTestModels.get(i).setTotal_questions(obj.getString(DatabaseHelper.QUESTION));
                }
            }
            practiceTestAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                    PracticeActivity.this.finish();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.back();
    }

}
