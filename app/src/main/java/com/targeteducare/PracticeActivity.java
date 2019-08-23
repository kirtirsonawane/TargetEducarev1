package com.targeteducare;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.ContactsContract;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.RecyclerView;
import android.text.Html;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.targeteducare.Adapter.ItemClickListener;
import com.targeteducare.Adapter.PracticeAdapter;
import com.targeteducare.Adapter.PracticeTestAdapter;
import com.targeteducare.Adapter.Section;
import com.targeteducare.Adapter.SectionedExpandableLayoutHelper;
import com.targeteducare.Classes.Exam;
import com.targeteducare.Classes.Item;
import com.targeteducare.Classes.Options;
import com.targeteducare.Classes.Question;
import com.targeteducare.Classes.QuestionURL;
import com.targeteducare.database.DatabaseHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
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
    Exam exam = new Exam();
    int notanswered = 0;
    int answered = 0;
    int review = 0;
    long id = 0;
    int skipped = 0;
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
    String type = "";
    String lang = "";
    Bundle b1;
    private PracticeTestAdapter practiceTestAdapter;
    int prevpos = 0;
    long starttimeforq = 0;
    DrawerLayout drawer;
    SectionedExpandableLayoutHelper sectionedExpandableLayoutHelper;
    TextView sbtxt;
    Button submitandview;
    ImageView refresh;
    int currentpos = 0;
    String tag = "";

    //int examid=0;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        try {
            screenshot_capture_permission();
            super.onCreate(savedInstanceState);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            loadLocale();
            setContentView(R.layout.activity_practice);
            tag = this.getClass().getSimpleName();
            qdata = new ArrayList<>();
            registerreceiver();
            setmaterialDesignv1();

            //Log.e("type ", "typeee Practice Activity");
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(Constants.firebasedbname);
            databaseReference.child(GlobalValues.student.getMobile()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    try {
                        if (dataSnapshot != null) {
                            if (dataSnapshot.getValue() != null) {
                                if (dataSnapshot.exists()) {

                                    addtofirebasedb(0);
                                } else {
                                    //Log.e("nt exists ", "nt exists " + dataSnapshot.toString());
                                    addtofirebasedb(1);
                                }
                            } else {
                                addtofirebasedb(1);
                            }
                        } else {
                            addtofirebasedb(1);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            LocalBroadcastManager.getInstance(PracticeActivity.this).registerReceiver(recforsubmit, new IntentFilter("QuestionUpdatedSubmitFragment"));
            submitandview = findViewById(R.id.submitviewanswer);
            lang = getSharedPreferences("Settings", Activity.MODE_PRIVATE).getString("Current Language", "");
            if (lang.equalsIgnoreCase("mr")) {
                exam.setLanguages("marathi");
            } else {
                exam.setLanguages("ENGLISH");
            }

            /*submitandview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        int currentpgid = mPager.getCurrentItem();
                        Log.e("current page ", currentpgid + "");
                        if (currentpgid < qdata.size()) {
                            submitandview.setVisibility(View.GONE);
                            bt5.setVisibility(View.VISIBLE);
                            qdata.get(currentpgid).setIssubmit(true);
                        } else {
                            qdata.get(currentpgid).setIssubmit(false);
                        }
                        Log.e("I got data qdataid ", qdata.get(currentpgid).getId() + "");
                        try {
                            Intent intent = new Intent("QuestionUpdatedSubmit");
                            intent.putExtra("QidNew", qdata.get(mPager.getCurrentItem()).getId());
                            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                            Log.e("send broad cast", "send broadcast");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } catch (Exception e) {
                        reporterror(tag, e.toString());
                    }
                }
            });*/


            submitandview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        int currentpgid = mPager.getCurrentItem();
                        if (currentpgid < qdata.size()) {
                            if (submitandview.getText().equals(getResources().getString(R.string.skip_question))) {
                                qdata.get(currentpgid).setIsskipped(true);
                                qdata.get(currentpgid).setIssubmit(false);
                            } else if (submitandview.getText().equals(getResources().getString(R.string.submit))) {
                                qdata.get(currentpgid).setIssubmit(true);
                                qdata.get(currentpgid).setIsskipped(false);
                            } else {
                                qdata.get(currentpgid).setIssubmit(false);
                                qdata.get(currentpgid).setIsskipped(false);
                            }
                            submitandview.setVisibility(View.GONE);
                            bt5.setVisibility(View.VISIBLE);
                        } else {
                            qdata.get(currentpgid).setIssubmit(false);
                            qdata.get(currentpgid).setIsskipped(false);
                        }

                        try {
                            Intent intent = new Intent("QuestionUpdatedSubmit");
                            intent.putExtra("QidNew", qdata.get(mPager.getCurrentItem()).getId());
                            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                        } catch (Exception e) {
                            reporterror(tag, e.toString());
                            e.printStackTrace();
                        }
                    } catch (Exception e) {
                        reporterror(tag, e.toString());
                        e.printStackTrace();
                    }
                }
            });

            practiceTestModels = new ArrayList<Exam>();
            practiceTestAdapter = new PracticeTestAdapter(PracticeActivity.this, practiceTestModels, lang);
            progress = (ProgressBar) findViewById(R.id.progressBar);
            progresstext = (TextView) findViewById(R.id.pregressupdates);
            timertxt = (TextView) findViewById(R.id.timertxt);
            sbtxt = (TextView) findViewById(R.id.subtxt);

            sbtxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        if (drawer.isDrawerOpen(GravityCompat.END)) {
                            drawer.closeDrawer(GravityCompat.END);
                        } else {
                            drawer.openDrawer(GravityCompat.END);
                        }
                    } catch (Exception e) {
                        reporterror(tag, e.toString());
                        e.printStackTrace();
                    }
                }
            });

            refresh = (ImageView) findViewById(R.id.refresh);
            refresh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        currentpos = mPager.getCurrentItem();
                        genloading(getResources().getString(R.string.loading_questions));
                        JSONObject obj = new JSONObject();
                        obj.put("examid", exam.getExamid());

                        if (lang.equals("mr")) {
                            exam.setLanguages("marathi");
                        } else {
                            exam.setLanguages("ENGLISH");
                        }

                        obj.put("language", "ENGLISH");
                        obj.put("ImageRelPath", "http://" + GlobalValues.IP);
                        //obj.put("ImageRelPath", "http://" + "exam.targeteducare.com");
                        JSONObject mainobj = new JSONObject();
                        mainobj.put("FilterParameter", obj.toString());
                        //ConnectionManager.getInstance(PracticeActivity.this).getquestion(mainobj.toString(), exam.getExamid(), exam.getLanguages());
                        ConnectionManager.getInstance(PracticeActivity.this).getquestion(mainobj.toString(), exam.getExamid(), exam.getLanguages());
                    } catch (Exception e) {
                        reporterror(tag, e.toString());
                        e.printStackTrace();
                    }
                }
            });
            //b1 = getIntent().getExtras();
            //type = b1.getString("testtype");
            //lang = getSharedPreferences("Settings", Activity.MODE_PRIVATE).getString("Current Language", "");
            Bundle b = getIntent().getExtras();
            if (b != null) {
                if (b.containsKey("exam")) {
                    this.exam = (Exam) b.getSerializable("exam");

                    setTitle(exam.getExamname());
                    title = (TextView) findViewById(R.id.title);
                    title.setText(exam.getExamname());
                    //result.setText(exam.getExamname());
                } else {
                    exam = new Exam();
                }
            } else {
                exam = new Exam();
            }

            if (lang.equals("mr")) {
                //exam.setExam_type(type);
                exam.setLanguages("marathi");
            } else {
                //exam.setExam_type(type);
                exam.setLanguages("ENGLISH");
            }
        /*timertxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("data ", "data " + qdata.get(mPager.getCurrentItem()).isIsskipped());

            }
        });*/

            resumebtn = (ImageView) findViewById(R.id.timeview);
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
                                                      timertxt.setText(hour+":"+min+":"+sec);*/
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
                    try {
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
                            updateexaminationdetails();
                            Intent ip = new Intent(PracticeActivity.this, ProgressReportActivity.class);
                            //ip.putExtra("progressreport", exam);
                            ip.putExtra("examidprogress", exam.getExamid());
                            ip.putExtra("flag", 1);
                            startActivity(ip);
                        }
                    } catch (Exception e) {
                        reporterror(tag, e.toString());
                        e.printStackTrace();
                    }
                }
            });
            //result = (TextView) findViewById(R.id.textview_result);
            //drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            progresstext.setText((int) Double.parseDouble(String.valueOf(exam.getProgress())) + "%");
            progress.setProgress((int) Double.parseDouble(String.valueOf(exam.getProgress())));

        /*progresstext.setText((int) Double.parseDouble(String.valueOf(exam.getProgress()))+"%");
        progress.setProgress((int) Double.parseDouble(String.valueOf(exam.getProgress())));*/
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
            bt3.setText(PracticeActivity.this.getResources().getString(R.string.skip));
            bt4 = (Button) findViewById(R.id.last_button);
            bt4.setVisibility(View.GONE);
            bt5 = findViewById(R.id.submitquestion);

        /*bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (qdata.size() > 1) {
                    mPager.setCurrentItem(0);
                    bt2.setEnabled(false);
                    bt3.setEnabled(true);
                }
            }
        });*/

            bt2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        int currentpgid = mPager.getCurrentItem() - 1;
                        if (currentpgid == 0) {
                            bt2.setEnabled(false);
                            bt3.setEnabled(true);
                        }

                        if ((currentpgid) > 0 || (currentpgid) < qdata.size()) {
                            bt2.setEnabled(true);
                            mPager.setCurrentItem((currentpgid));
                        }

                        if (currentpgid < ((qdata.size() - 1))) {
                            bt3.setEnabled(true);
                        }
                    } catch (Exception e) {
                        reporterror(tag, e.toString());
                    }
                }
            });

            bt3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //getanswersheet
                    try {
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
                    } catch (Exception e) {
                        reporterror(tag, e.toString());
                        e.printStackTrace();
                    }
                }
            });

            bt4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        if (qdata.size() > 1) {
                            int id = (qdata.size() - 1);
                            mPager.setCurrentItem(id);
                            bt3.setEnabled(false);
                            bt2.setEnabled(true);
                        }
                    } catch (Exception e) {
                        reporterror(tag, e.toString());
                        e.printStackTrace();
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
                    try {
                        int currentpgid = mPager.getCurrentItem() + 1;
                        if (currentpgid < qdata.size()) {
                            mPager.setCurrentItem(currentpgid);
                        /*Toast.makeText(PracticeActivity.this, "You took "+time_taken_per_question+ " seconds for this question"
                                , Toast.LENGTH_SHORT).show();*/
                            //  savedataonbackorsubmit();
                        } else {
                            mPager.setCurrentItem(currentpgid - 1);
                            //opendialog(getResources().getString(R.string.dialog_submittesttoknowyourscore), 2);
                        }
                    } catch (Exception e) {
                        reporterror(tag, e.toString());
                        e.printStackTrace();
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
    /*    timer = new CountDownTimer(time, 1000) {
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
              //  txt2.setText(s);

                //txt2.setText(hour + ":" + min + ":" + sec);
                //txt5.setText(": " + hour + ":" + min + ":" + sec);
                //txt5.setVisibility(View.GONE);

                long hour1 = (((l1 / 1000) / 60)) / 60;
                long min1 = (((l1 / 1000) / 60)) % 60;
                long sec1 = ((l1 / 1000) % 60);

                *//*txt6.setText(": " + hour1 + ":" + min1 + ":" + sec1);
                if (l <= (alertbefore * 60000)) {
                    if (!isshown) {
                        opendialog("This exam will be submitting after " + alertbefore + " min automatically. Please Review and compile your exam.", 0);
                        isshown = true;
                    }
                }*//*
            }

            @Override
            public void onFinish() {
                submitdata();
            }
        };*/

        /*ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();*/

            //NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            //navigationView.setNavigationItemSelectedListener(this);

            mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
            sectionedExpandableLayoutHelper = new SectionedExpandableLayoutHelper(this, mRecyclerView, this, 6, false, true);
            sectionedExpandableLayoutHelper.notifyDataSetChanged();
            mPager = (ViewPager) findViewById(R.id.pager);
            mAdapter = new PracticeAdapter(getSupportFragmentManager(), qdata, false);
            mPager.setAdapter(mAdapter);
            mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                /*if (qdata.get(position).isIsreview()) {
                    txt1.setText("Remove Review");
                } else {
                    txt1.setText("Review");
                }*/

                    //Log.e("view answer ", "button "+position);
                    loadLocale();
                    try {
                       /* if (position < qdata.size()) {
                            if (position == 0) {
                                bt2.setVisibility(View.GONE);
                                if (qdata.get(position).isIsanswered()) {

                            *//*submitandview.setVisibility(View.GONE);
                            bt5.setVisibility(View.VISIBLE);*//*
                                    if (qdata.get(position).isIsskipped() || qdata.get(position).isIssubmit()) {
                                        submitandview.setVisibility(View.GONE);
                                        bt5.setVisibility(View.VISIBLE);
                                    } else {
                                        // Log.e("here ", "3");
                                        submitandview.setText(PracticeActivity.this.getResources().getString(R.string.submit));
                                        submitandview.setBackgroundResource(R.drawable.rounded_button_layout_submit);
                                        submitandview.setTextColor(PracticeActivity.this.getResources().getColor(R.color.white));
                                        submitandview.setVisibility(View.VISIBLE);
                                        bt5.setVisibility(View.GONE);
                                    }
                                } else {
                                    if (qdata.get(position).isIsskipped() || qdata.get(position).isIssubmit()) {
                                        //   Log.e("here ", "4");
                                        submitandview.setVisibility(View.GONE);
                                        bt5.setVisibility(View.VISIBLE);
                                    } else {

                                        //  Log.e("here ", "5");
                                        submitandview.setText(getResources().getString(R.string.skip_question));
                                        submitandview.setBackgroundResource(R.drawable.rounded_button_layout);
                                        submitandview.setTextColor(PracticeActivity.this.getResources().getColor(R.color.Gray500));
                                        submitandview.setVisibility(View.VISIBLE);
                                        bt5.setVisibility(View.GONE);
                                    }
                                }
                            } else {
                                Log.e("visible4 ","visible ");
                                bt2.setVisibility(View.VISIBLE);
                                if (qdata.get(position).isIsanswered()) {
                                    if (qdata.get(position).isIsskipped() || qdata.get(position).isIssubmit()) {
                                        Log.e("visible3 ","visible ");
                                        submitandview.setVisibility(View.GONE);
                                        bt5.setVisibility(View.VISIBLE);
                                    } else {
                                        Log.e("visible2 ","visible ");
                                        submitandview.setText(getResources().getString(R.string.submit));
                                        submitandview.setBackgroundResource(R.drawable.rounded_button_layout_submit);
                                        submitandview.setTextColor(PracticeActivity.this.getResources().getColor(R.color.white));
                                        submitandview.setVisibility(View.VISIBLE);
                                        bt5.setVisibility(View.GONE);
                                    }
                                } else {
                                    Log.e("visible1 ","visible ");
                                    if (qdata.get(position).isIsskipped() || qdata.get(position).isIssubmit()) {
                                        submitandview.setVisibility(View.GONE);
                                        bt5.setVisibility(View.VISIBLE);
                                    } else {
                                        Log.e("visible ","visible ");
                                        submitandview.setText(getResources().getString(R.string.skip_question));
                                        submitandview.setBackgroundResource(R.drawable.rounded_button_layout);
                                        submitandview.setTextColor(PracticeActivity.this.getResources().getColor(R.color.Gray500));
                                        submitandview.setVisibility(View.VISIBLE);
                                        bt5.setVisibility(View.GONE);

                                        Log.e("visible ","visible "+submitandview.getText().toString());
                                    }
                                }
                            }
                        }
*/
                        sbtxt.setText(qdata.get(position).getSubjectname());
                        qdata.get(position).setIsvisited(true);
                        sectionedExpandableLayoutHelper.notifyDataSetChanged();
                    } catch (Exception e) {
                        reporterror(tag, e.toString());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onPageSelected(int position) {
                    try {
                        if (prevpos != position) {
                            long timeInMillisecondsv1 = qdata.get(prevpos).getTimeperquestion() + (SystemClock.uptimeMillis() - starttimeforq);
                            qdata.get(prevpos).setTimeperquestion((int) (timeInMillisecondsv1));
                            prevpos = position;
                        }

                        time_spent_current_page = SystemClock.uptimeMillis();
                        total_time_per_question = qdata.get(position).getTimeperquestion();
                        total_time_per_question = total_time_per_question + time_taken_per_question;



                        loadLocale();
                        try {
                        if (position < qdata.size()) {
                            if (position == 0) {
                                bt2.setVisibility(View.GONE);
                                if (qdata.get(position).isIsanswered()) {
                         /*   submitandview.setVisibility(View.GONE);
                            bt5.setVisibility(View.VISIBLE);*/
                                    if (qdata.get(position).isIsskipped() || qdata.get(position).isIssubmit()) {
                                        submitandview.setVisibility(View.GONE);
                                        bt5.setVisibility(View.VISIBLE);
                                    } else {
                                        // Log.e("here ", "3");
                                        submitandview.setText(PracticeActivity.this.getResources().getString(R.string.submit));
                                        submitandview.setBackgroundResource(R.drawable.rounded_button_layout_submit);
                                        submitandview.setTextColor(PracticeActivity.this.getResources().getColor(R.color.white));
                                        submitandview.setVisibility(View.VISIBLE);
                                        bt5.setVisibility(View.GONE);
                                    }
                                } else {
                                    if (qdata.get(position).isIsskipped() || qdata.get(position).isIssubmit()) {
                                        //   Log.e("here ", "4");
                                        submitandview.setVisibility(View.GONE);
                                        bt5.setVisibility(View.VISIBLE);
                                    } else {

                                        //  Log.e("here ", "5");
                                        submitandview.setText(getResources().getString(R.string.skip_question));
                                        submitandview.setBackgroundResource(R.drawable.rounded_button_layout);
                                        submitandview.setTextColor(PracticeActivity.this.getResources().getColor(R.color.Gray500));
                                        submitandview.setVisibility(View.VISIBLE);
                                        bt5.setVisibility(View.GONE);
                                    }
                                }
                            } else {
                                //Log.e("visible4 ","visible ");
                                bt2.setVisibility(View.VISIBLE);
                                if (qdata.get(position).isIsanswered()) {
                                    if (qdata.get(position).isIsskipped() || qdata.get(position).isIssubmit()) {
                                        //Log.e("visible3 ","visible ");
                                        submitandview.setVisibility(View.GONE);
                                        bt5.setVisibility(View.VISIBLE);
                                    } else {
                                        //Log.e("visible2 ","visible ");
                                        submitandview.setText(getResources().getString(R.string.submit));
                                        submitandview.setBackgroundResource(R.drawable.rounded_button_layout_submit);
                                        submitandview.setTextColor(PracticeActivity.this.getResources().getColor(R.color.white));
                                        submitandview.setVisibility(View.VISIBLE);
                                        bt5.setVisibility(View.GONE);
                                    }
                                } else {
                                    //Log.e("visible1 ","visible ");
                                    if (qdata.get(position).isIsskipped() || qdata.get(position).isIssubmit()) {
                                        submitandview.setVisibility(View.GONE);
                                        bt5.setVisibility(View.VISIBLE);
                                    } else {
                                        //Log.e("visible ","visible ");
                                        submitandview.setText(getResources().getString(R.string.skip_question));
                                        submitandview.setBackgroundResource(R.drawable.rounded_button_layout);
                                        submitandview.setTextColor(PracticeActivity.this.getResources().getColor(R.color.Gray500));
                                        submitandview.setVisibility(View.VISIBLE);
                                        bt5.setVisibility(View.GONE);

                                        //Log.e("visible ","visible "+submitandview.getText().toString());
                                    }
                                }
                            }
                        }
                            sbtxt.setText(qdata.get(position).getSubjectname());
                            qdata.get(position).setIsvisited(true);

                            sectionedExpandableLayoutHelper.notifyDataSetChanged();
                        } catch (Exception e) {
                            reporterror(tag, e.toString());
                            e.printStackTrace();
                        }

                        //qdata.get(position).setTimeperquestion((int) total_time_per_question);
                    } catch (Exception e) {
                        reporterror(tag, e.toString());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                    try {
                        time_taken_onpagechange = SystemClock.uptimeMillis();
                        starttimeforq = SystemClock.uptimeMillis();
                        time_taken_per_question = (time_taken_onpagechange - time_spent_current_page) / 1000;
                    } catch (Exception e) {
                        reporterror(tag, e.toString());
                        e.printStackTrace();
                    }
                }
            });
            registerreceiver();
            JSONArray array = DatabaseHelper.getInstance(PracticeActivity.this).getqdata(exam.getExamid(), exam.getLanguages());
            if (array.length() > 0) {
                try {
                    //Log.e("via db ", "via db");
                    id = array.getJSONObject(0).getLong(DatabaseHelper.ID);
                    JSONObject obj = new JSONObject(array.getJSONObject(0).getString(DatabaseHelper.JSONDATA));
                    parsedata(obj);
                    JSONArray array1 = DatabaseHelper.getInstance(PracticeActivity.this).getqurldata(exam.getExamid());
                    ArrayList<QuestionURL> qurl = new ArrayList<>();
                    for (int i = 0; i < array1.length(); i++) {
                        JSONObject obj1 = array1.getJSONObject(i);
                        QuestionURL q = new QuestionURL(obj1.getString(DatabaseHelper.IMAGESOURCE), obj1.getString(DatabaseHelper.TYPE), obj1.getInt(DatabaseHelper.ID));
                        qurl.add(q);
                    }

                    String language = "";
                    if (lang.equals("mr")) {
                        language = "marathi";
                    } else {
                        language = "ENGLISH";
                    }

                    ((Activitycommon) context).downloaddata(qurl, exam.getExamid(), language, "");

                } catch (Exception e) {
                    reporterror(tag, e.toString());
                    e.printStackTrace();
                }
            } else {
                try {
                    genloading(getResources().getString(R.string.loading_questions));
                    JSONObject obj = new JSONObject();
                    obj.put("examid", exam.getExamid());
                    if (lang.equals("mr")) {
                        exam.setLanguages("marathi");
                    } else {
                        exam.setLanguages("ENGLISH");
                    }
                    obj.put("language", "ENGLISH");
                    obj.put("ImageRelPath", "http://" + GlobalValues.IP);
                    //obj.put("ImageRelPath", "http://" + "exam.targeteducare.com");
                    JSONObject mainobj = new JSONObject();
                    mainobj.put("FilterParameter", obj.toString());
                    //ConnectionManager.getInstance(PracticeActivity.this).getquestion(mainobj.toString(), exam.getExamid(), exam.getLanguages());
                    ConnectionManager.getInstance(PracticeActivity.this).getquestion(mainobj.toString(), exam.getExamid(), exam.getLanguages());

                } catch (Exception e) {
                    reporterror(tag, e.toString());
                    e.printStackTrace();
                }
            }
            attachnavigation();
        } catch (Exception e) {
            reporterror(tag, e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        try {
            if(exam.getIsOmr()==0)
            {
                if (!exam.getExamstatus().equalsIgnoreCase("Attempted")) {
                    MenuInflater inflater = getMenuInflater();
                    inflater.inflate(R.menu.examend, menu);
                }
            }
        } catch (Exception e) {
            reporterror(tag, e.toString());
            e.printStackTrace();
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_endquiz:
                if (!exam.getExamstatus().equalsIgnoreCase("Attempted")) {
                    Double obtainedmarks = 0.0;
                    int total_skipped = 0;
                    int totalattempted = 0;
                    int totalwrong = 0;
                    int total_right = 0;
                    // int totalnonattempted = 0;
                    for (int i = 0; i < qdata.size(); i++) {
                        if (qdata.get(i).isIsanswered()) {
                            totalattempted++;
                            ArrayList<Options> optdata = qdata.get(i).getOptions();
                            for (int j = 0; j < optdata.size(); j++) {
                                if (optdata.get(j).isSelected()) {
                                    if (optdata.get(j).isAnswer()) {
                                        total_right++;
                                        obtainedmarks = obtainedmarks + (Double.parseDouble(qdata.get(i).getMarks()));
                                    } else {
                                        qdata.get(i).setIswrongAnswer(true);
                                        totalwrong++;
                                        //  negativemarks = negativemarks + (Double.parseDouble(exam.getNeagativemarks()));
                                    }
                                } else {

                                }
                            }
                        } else if (qdata.get(i).isIsskipped()) {
                            total_skipped++;
                        }
                    }
          /*      for (int i = 0; i < qdata.size(); i++) {
                    if (qdata.get(i).isIsanswered()) {
                        totalattempted++;

                        total_marks = total_marks + (Double.parseDouble(qdata.get(i).getMarks()));
                    } else if (qdata.get(i).isIsskipped()) {
                        total_skipped++;
                    }
                }*/
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
                            avgtime = ((updatedTime / 1000) / (total_skipped + totalattempted));

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
                        ConnectionManager.getInstance(PracticeActivity.this).savepracticetest(mainobj.toString());

                    } catch (Exception e) {
                        reporterror(tag, e.toString());
                        e.printStackTrace();
                    }
                } else {
                    /*try {
                        updatedatawithoutsendingtoserver();
                        Intent i = new Intent(PracticeActivity.this, ProgressReportActivity.class);
                        i.putExtra("progressreport", exam);
                        i.putExtra("flag", 1);
                        startActivity(i);
                        finish();
                    }catch (Exception e){
                        reporterror(tag,e.toString());
                        e.printStackTrace();
                    }*/
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.allready_submited), Toast.LENGTH_LONG).show();
                }
                break;
        }
        return super.onOptionsItemSelected(item);

    }

    public void notifyqdatachanged(Question q) {
        try {

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
                    //items.get(i).setTotal(items.get(i).getTotalnotanswered()-1);
                    sectionedExpandableLayoutHelper.notifyDataSetChanged();
                }
            }

            int x = 0;
            if (qdata.size() > 0)
                x = (int) (((double) answered / (double) qdata.size()) * 100);
            progresstext.setText((x + (int) Double.parseDouble(String.valueOf(exam.getProgress()))) + "%");
            progress.setProgress(x + (int) Double.parseDouble(String.valueOf(exam.getProgress())));
        /*progresstext.setText(x + "%");
        progress.setProgress(x);*/
        } catch (Exception e) {
            reporterror(tag, e.toString());
            e.printStackTrace();
        }
    }

    boolean flag = false;

    @Override
    protected void onResponsed(int statuscode, int accesscode, String data) {
        super.onResponsed(statuscode, accesscode, data);
        try {
            if (statuscode == Constants.STATUS_OK) {
                if (accesscode == Connection.GET_QUESTIONS.ordinal()) {
                    try {
                        JSONObject obj = new JSONObject(GlobalValues.TEMP_STR).getJSONObject("root").getJSONObject("subroot");
                        parsedata(obj);
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
                } else if (accesscode == Connection.GET_PARSINGEXCEPTION.ordinal()) {
                    try {
                        if (!((Activity) context).isFinishing()) {
                            if (lang.equalsIgnoreCase("mr")) {
                                opendialog(getResources().getString(R.string.user_langchange_message_marathi), 3);
                            } else {
                                opendialog(getResources().getString(R.string.user_langchange_message_marathi), 3);
                            }
                        } else {
                        }
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
                        try {
                            if (!flag) {
                                JSONArray questions = new JSONArray();
                                for (int i = 0; i < qdata.size(); i++) {
                                    questions.put(qdata.get(i).getJsonObject(qdata.get(i)));
                                }

                                mainobj.put("questions", questions);
                                ContentValues c = new ContentValues();
                                c.put(DatabaseHelper.JSONDATA, mainobj.toString());
                                c.put(DatabaseHelper.LANGUAGE, exam.getLanguages());
                                c.put(DatabaseHelper.EXAMID, exam.getExamid());
                                c.put(DatabaseHelper.SAVEDTIME, DateUtils.getSqliteTime());
                                flag = true;
                                DatabaseHelper.getInstance(PracticeActivity.this).saveqdata(c, exam.getExamid(), exam.getLanguages());

                                try {
                                    Intent intent = new Intent(Constants.BROADCAST_WIZARD);
                                    intent.putExtra(Constants.BROADCAST_RESPONSE_CODE, 200);
                                    intent.putExtra(Constants.BROADCAST_URL_ACCESS, Connection.Dataupdated.ordinal());
                                    intent.putExtra(Constants.BROADCAST_DATA, Integer.toString(exam.getExamid()));
                                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                                } catch (Exception e) {
                                    reporterror(tag, e.toString());
                                    e.printStackTrace();
                                }

                                if (exam.isInstantExamResult() || exam.isInstantExamResultWithAns()) {
                                    Intent intent = new Intent(PracticeActivity.this, AnswersheetActivity.class);
                                    intent.putExtra("exam", exam);
                                    startActivity(intent);
                                    this.finish();
                                }
                            }
                        } catch (Exception e) {
                            reporterror(tag, e.toString());
                            e.printStackTrace();
                        }
                    } catch (Exception e) {
                        reporterror(tag, e.toString());
                        e.printStackTrace();
                    }
                } else if (accesscode == Connection.SAVE_ANSWER.ordinal()) {
                    try {
                        if (!flag) {
                            JSONArray questions = new JSONArray();
                            for (int i = 0; i < qdata.size(); i++) {
                                questions.put(qdata.get(i).getJsonObject(qdata.get(i)));
                            }

                            mainobj.put("questions", questions);
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
                                reporterror(tag, e.toString());
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
                        reporterror(tag, e.toString());
                        e.printStackTrace();
                    }
                } else if (accesscode == Connection.SavePracticeTest.ordinal()) {
                    Intent intent = new Intent(Constants.BROADCAST_WIZARD);
                    intent.putExtra(Constants.BROADCAST_RESPONSE_CODE, 200);
                    intent.putExtra(Constants.BROADCAST_URL_ACCESS, Connection.Dataupdatedattemptest.ordinal());
                    intent.putExtra(Constants.BROADCAST_DATA, Integer.toString(exam.getExamid()));
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                    savedataonbackorsubmit();
                } else if (accesscode == Connection.SavePracticeTestExcetion.ordinal()) {

                }
            }
        } catch (Exception e) {
            reporterror(tag, e.toString());
            e.printStackTrace();
        }
    }

    JSONObject mainobj;
    ArrayList<Item> items;

    public void parsedata(JSONObject obj) {
        try {
            JSONArray jarray = DatabaseHelper.getInstance(PracticeActivity.this).getans(exam.getExamid());
            mainobj = obj;
            JSONArray array = obj.optJSONArray("questions");
            if (array.length() > 0) {
                JSONObject objcheck = array.getJSONObject(0);
                if (lang.equalsIgnoreCase("mr")) {
                    if (objcheck.has("NameInMarathi")) {

                        if (objcheck.getString("NameInMarathi").equalsIgnoreCase("null") || objcheck.getString("NameInMarathi").length() < 0) {
                            opendialog(getResources().getString(R.string.user_langchange_message_marathi), 3);

                            return;
                        }
                    } else {

                        opendialog(getResources().getString(R.string.user_langchange_message_marathi), 3);
                        return;
                    }
                } else {

                    if (objcheck.has("Name")) {
                        if (objcheck.getString("Name").equalsIgnoreCase("null") || objcheck.getString("Name").length() < 0) {
                            opendialog(getResources().getString(R.string.user_langchange_message), 3);
                            return;
                        }
                    } else {
                        opendialog(getResources().getString(R.string.user_langchange_message), 3);
                        return;
                    }
                }
            }

            HashMap<Item, ArrayList<Item>> mSectionMap = new HashMap<Item, ArrayList<Item>>();
            items = new ArrayList<>();
            sectionedExpandableLayoutHelper.removeallSection();
            JSONObject subobj = obj.getJSONObject("subjects").optJSONObject("questions");
            if (subobj != null) {
                Item item = new Item();
                item.setName(subobj.getString("subjectname"));
                item.setNoofques(subobj.getString("noofques"));
                item.setSubjectid(subobj.getInt("subjectid"));
                //item.setOptionalp(subobj.getString("optionalp"));
                items.add(item);
                ArrayList<Item> arrayList = new ArrayList<>();
            } else {
                JSONArray subarry = obj.getJSONObject("subjects").optJSONArray("questions");
                for (int i = 0; i < subarry.length(); i++) {
                    subobj = subarry.getJSONObject(i);
                    Item item = new Item();
                    // if(subobj.getJSONObject("subject").opt("subjectname")!=null)
                    item.setName(subobj.getString("subjectname"));
                    item.setNoofques(subobj.getString("noofques"));
                    item.setSubjectid(subobj.getInt("subjectid"));
                    //item.setOptionalp(subobj.getString("optionalp"));
                    items.add(item);
                    ArrayList<Item> arrayList = new ArrayList<>();
                    mSectionMap.put(item, arrayList);
                }
            }

            if (obj.has("config")) {
                JSONObject configobj = obj.getJSONObject("config");
                if (configobj.has("version")) {
                    exam.setVersion(configobj.getString("version"));
                }
            }

            // JSONArray array = obj.optJSONArray("questions");
            ArrayList<Question> quesdata = new ArrayList<>();
            if (array != null) {
                for (int i = 0; i < array.length(); i++) {
                    quesdata.add(new Question(array.getJSONObject(i)));
                }
            } else {
                JSONObject obj1 = obj.optJSONObject("questions");
                if (obj1 != null) {

                    quesdata.add(new Question(obj1));
                } else {
                    if (lang.equalsIgnoreCase("mr")) {
                        opendialog(getResources().getString(R.string.user_langchange_message_marathi), 3);
                    } else {
                        opendialog(getResources().getString(R.string.user_langchange_message_marathi), 3);
                    }
                    this.finish();
                }
            }

            if (quesdata != null) {
                for (int i = 0; i < quesdata.size(); i++) {
                    JSONArray jarray1 = DatabaseHelper.getInstance(PracticeActivity.this).getans(quesdata.get(i).getId());
                    if (jarray1.length() > 0) {
                        quesdata.get(i).setIsanswered(true);
                        for (int j = 0; j < quesdata.get(i).getOptions().size(); j++) {
                            if (quesdata.get(i).getOptions().get(j).getId() == jarray1.getJSONObject(0).getInt(DatabaseHelper.ANS)) {
                                quesdata.get(i).getOptions().get(j).setSelected(true);
                                //break;
                            }
                        }
                    }
                }

                qdata.clear();
                qdata.addAll(quesdata);
                //  mAdapter.notifyDataSetChanged();
                mAdapter = new PracticeAdapter(getSupportFragmentManager(), qdata, false);
                mPager.setAdapter(mAdapter);
                if (currentpos >= 0 && currentpos < qdata.size())
                    mPager.setCurrentItem(currentpos);

                if (mPager.getCurrentItem() < qdata.size()) {
                    if (mPager.getCurrentItem() == 0) {
                        bt2.setVisibility(View.GONE);
                        if (qdata.get(mPager.getCurrentItem()).isIsanswered()) {
                            /*submitandview.setVisibility(View.GONE);
                            bt5.setVisibility(View.VISIBLE);*/
                            if (qdata.get(mPager.getCurrentItem()).isIsskipped() || qdata.get(mPager.getCurrentItem()).isIssubmit()) {
                                submitandview.setVisibility(View.GONE);
                                bt5.setVisibility(View.VISIBLE);
                            } else {
                                submitandview.setText(getResources().getString(R.string.submit));
                                submitandview.setBackgroundResource(R.drawable.rounded_button_layout_submit);
                                submitandview.setTextColor(getResources().getColor(R.color.white));
                                submitandview.setVisibility(View.VISIBLE);
                                bt5.setVisibility(View.GONE);
                            }
                        } else {
                            if (qdata.get(mPager.getCurrentItem()).isIsskipped() || qdata.get(mPager.getCurrentItem()).isIssubmit()) {
                                submitandview.setVisibility(View.GONE);
                                bt5.setVisibility(View.VISIBLE);
                            } else {
                                submitandview.setText(getResources().getString(R.string.skip_question));
                                submitandview.setBackgroundResource(R.drawable.rounded_button_layout);
                                submitandview.setTextColor(getResources().getColor(R.color.Gray500));
                                submitandview.setVisibility(View.VISIBLE);
                                bt5.setVisibility(View.GONE);
                            }
                        }
                    } else {
                        bt2.setVisibility(View.VISIBLE);
                        if (qdata.get(mPager.getCurrentItem()).isIsanswered()) {
                            if (qdata.get(mPager.getCurrentItem()).isIsskipped() || qdata.get(mPager.getCurrentItem()).isIssubmit()) {
                                submitandview.setVisibility(View.GONE);
                                bt5.setVisibility(View.VISIBLE);
                            } else {
                                submitandview.setText(getResources().getString(R.string.submit));
                                submitandview.setBackgroundResource(R.drawable.rounded_button_layout_submit);
                                submitandview.setTextColor(getResources().getColor(R.color.white));
                                submitandview.setVisibility(View.VISIBLE);
                                bt5.setVisibility(View.GONE);
                            }
                        } else {
                            if (qdata.get(mPager.getCurrentItem()).isIsskipped() || qdata.get(mPager.getCurrentItem()).isIssubmit()) {
                                submitandview.setVisibility(View.GONE);
                                bt5.setVisibility(View.VISIBLE);
                            } else {
                                submitandview.setText(getString(R.string.skip_question));
                                submitandview.setBackgroundResource(R.drawable.rounded_button_layout);
                                submitandview.setTextColor(getResources().getColor(R.color.Gray500));
                                submitandview.setVisibility(View.VISIBLE);
                                bt5.setVisibility(View.GONE);
                            }
                        }
                    }
                }
            }

            if (quesdata != null) {
              /*  for (int i = 0; i < quesdata.size(); i++) {
                    for (int j = 0; j < items.size(); j++) {
                        if (items.get(j).getSubjectid() == quesdata.get(i).getSubjectid()) {
                            quesdata.get(i).setDisplayid(items.get(j).getQdata().size() + 1);
                            items.get(j).getQdata().add(quesdata.get(i));
                            break;
                        }
                    }
                }*/

                for (int i = 0; i < quesdata.size(); i++) {
                    for (int j = 0; j < items.size(); j++) {
                        if (items.get(j).getSubjectid() == quesdata.get(i).getSubjectid()) {
                            quesdata.get(i).setDisplayid(items.get(j).getQdata().size() + 1);
                            items.get(j).getQdata().add(quesdata.get(i));
                            if (qdata.get(i).isIsanswered()) {
                                ArrayList<Options> optdata = qdata.get(i).getOptions();
                                for (int k = 0; k < optdata.size(); k++) {
                                    if (optdata.get(k).isSelected()) {
                                        if (optdata.get(k).isAnswer()) {
                                            items.get(j).setTotalright(items.get(j).getTotalright() + 1);
                                            items.get(j).setObtainedmarks(items.get(j).getObtainedmarks() + (Double.parseDouble(qdata.get(i).getMarks())));
                                        } else {
                                            items.get(j).setTotalwrong(items.get(j).getTotalwrong() + 1);
                                            items.get(j).setNegativemarks(items.get(j).getNegativemarks() + (Double.parseDouble(exam.getNeagativemarks())));
                                           /*qdata.get(i).setIswrongAnswer(true);
                                            totalwrong++;
                                            negativemarks=negativemarks+(Double.parseDouble(exam.getNeagativemarks()));*/
                                        }
                                    }
                                }
                            } else {
                                items.get(j).setTotalnotanswered(items.get(j).getTotalnotanswered() + 1);
                            }
                            break;
                        }
                    }
                }
            }

          /*  for (int j = 0; j < items.size(); j++) {
                items.get(j).setTotalnotanswered(items.get(j).getQdata().size());
            }*/

            for (int i = 0; i < items.size(); i++) {
                sectionedExpandableLayoutHelper.addSection(items.get(i));
                sectionedExpandableLayoutHelper.notifyDataSetChanged();
            }

            //txt7.setText(": " + qdata.size());
            notanswered = qdata.size();
            //txt10.setText(": " + answered);
            //txt11.setText(": " + notanswered);
            //txt12.setText(": " + review);

            Calendar cal = Calendar.getInstance();
            starttime = DateUtils.getSqliteTime();
//            timer.start();
            startTime = SystemClock.uptimeMillis();
            customHandler.postDelayed(updateTimerThread, 0);
            try {
                JSONObject configobj = obj.optJSONObject("config");
                if (configobj != null)
                    if (configobj.has("AlertBefore"))
                        alertbefore = configobj.getInt("AlertBefore");
            } catch (Exception e) {
                reporterror(tag, e.toString());
                e.printStackTrace();
            }




            //for (int i = 0; i < practiceTestModels.size(); i++) {
                JSONArray array1 = DatabaseHelper.getInstance(PracticeActivity.this).getexamdetails(exam.getExamid(), exam.getExam_type());
                if (array1.length() > 0) {
                    JSONObject obj1 = array1.getJSONObject(0);
                    int lastq=obj1.getInt(DatabaseHelper.LAST_QID);
                    if(lastq==0)
                    {
                        if(mPager.getAdapter().getCount()>=lastq) {
                            mPager.setCurrentItem(lastq);
                        }
                    }else {
                        if(mPager.getAdapter().getCount()>=lastq) {
                            mPager.setCurrentItem(lastq);
                        }
                    }
              /*      practiceTestModels.get(i).setSkipp(obj.getInt(DatabaseHelper.SKIPP));
                    practiceTestModels.get(i).setAnswered(obj.getInt(DatabaseHelper.ANSWERED));
                    practiceTestModels.get(i).setWrong(obj.getInt(DatabaseHelper.WRONG));
                    practiceTestModels.get(i).setCorrect(obj.getInt(DatabaseHelper.CORRECT));
                    practiceTestModels.get(i).setProgress(obj.getDouble(DatabaseHelper.PROGRESS));
                    progress.setProgress(Integer.parseInt(String.valueOf(obj.getDouble(DatabaseHelper.PROGRESS))));
                    progresstext.setText(String.valueOf(obj.getDouble(DatabaseHelper.PROGRESS)) + "%");
                    practiceTestModels.get(i).setTimetaken(obj.getLong(DatabaseHelper.TIMETAKEN));
                    exam.setTimetaken(obj.getLong(DatabaseHelper.TIMETAKEN));
                    practiceTestModels.get(i).setSpeed(obj.getDouble(DatabaseHelper.SPEED));*/
                    //practiceTestModels.get(i).setTotal_questions(obj.getString(DatabaseHelper.QUESTION));
                }
            //}


        } catch (Exception e) {
            reporterror(tag, e.toString());
            e.printStackTrace();
        }
    }

    private void calltorefresh() {
        try {
            currentpos = mPager.getCurrentItem();
            //genloading(getResources().getString(R.string.loading_questions));
            JSONObject obj = new JSONObject();
            obj.put("examid", exam.getExamid());
            if (lang.equals("mr")) {
                exam.setLanguages("marathi");
            } else {
                exam.setLanguages("ENGLISH");
            }
            obj.put("language", "ENGLISH");
            obj.put("ImageRelPath", "http://" + GlobalValues.IP);
            //obj.put("ImageRelPath", "http://" + "exam.targeteducare.com");
            JSONObject mainobj = new JSONObject();
            mainobj.put("FilterParameter", obj.toString());
            //ConnectionManager.getInstance(PracticeActivity.this).getquestion(mainobj.toString(), exam.getExamid(), exam.getLanguages());
            ConnectionManager.getInstance(PracticeActivity.this).getquestion(mainobj.toString(), exam.getExamid(), exam.getLanguages());
        } catch (Exception e) {
            reporterror(tag, e.toString());
            e.printStackTrace();
        }
    }


    @Override
    public void onBackPressed() {
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

    public void attachnavigation() {
        try {
            drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

            ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

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
                    if (drawer.isDrawerOpen(GravityCompat.END)) {
                        drawer.closeDrawer(GravityCompat.END);
                    } else {
                        drawer.openDrawer(GravityCompat.END);
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
            });
            icback = (ImageView) findViewById(R.id.menuback);
            icback.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (drawer.isDrawerOpen(GravityCompat.END)) {
                        drawer.closeDrawer(GravityCompat.END);
                    } else {
                        drawer.openDrawer(GravityCompat.END);
                    }

               /* if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else if (drawer.isDrawerOpen(GravityCompat.END)) {
                    drawer.closeDrawer(GravityCompat.END);
                } else {
                    opendialog(getResources().getString(R.string.dialog_testsubmitexit), 1);
                    //ExamActivity.this.finish();
                }*/
                }
            });
        } catch (Exception e) {
            reporterror(tag, e.toString());
            e.printStackTrace();
        }
    }

    private void savedataonbackorsubmit() {
        try {
            //Log.e("backpressed called ","backpressedcalled ");
            int totalwrong = 0;
            int totalquestions = qdata.size();
            int totalright = 0;
            double obtainedmarks = 0;
            double negativemarks = 0;
            int totalnotanswered = 0;
            int skipp = 0;
            for (int i = 0; i < qdata.size(); i++) {
                if (qdata.get(i).isIsskipped()) {
                    totalnotanswered++;
                    skipp++;
                } else {
                    if (qdata.get(i).isIsanswered()) {

                        ArrayList<Options> optdata = qdata.get(i).getOptions();
                        for (int j = 0; j < optdata.size(); j++) {
                            if (optdata.get(j).isSelected()) {
                                if (optdata.get(j).isAnswer()) {
                                    totalright++;
                                    obtainedmarks = obtainedmarks + (Double.parseDouble(qdata.get(i).getMarks()));
                                } else {
                                    qdata.get(i).setIswrongAnswer(true);
                                    totalwrong++;
                                    negativemarks = negativemarks + (Double.parseDouble(exam.getNeagativemarks()));
                                }
                            } else {

                            }
                        }
                    } else {
                        totalnotanswered++;

                    }
                }
            }

            ContentValues c1 = new ContentValues();
            c1.put(DatabaseHelper.EXAMID, exam.getExamid());
            c1.put(DatabaseHelper.TIMETAKEN, updatedTime);
            exam.setTimetaken((Long) c1.get(DatabaseHelper.TIMETAKEN));
            c1.put(DatabaseHelper.ANSWERED, answered);
            c1.put(DatabaseHelper.SKIPP, skipp);
            c1.put(DatabaseHelper.CORRECT, totalright);
            c1.put(DatabaseHelper.WRONG, totalwrong);
            c1.put(DatabaseHelper.QUESTION, totalquestions);
            c1.put(DatabaseHelper.EXAMTYPE, exam.getExam_type());


            c1.put(DatabaseHelper.LAST_QID,mPager.getCurrentItem());
            c1.put(DatabaseHelper.PROGRESS, progresstext.getText().toString().replace("%", ""));
            long sec = 1;
            long sp = 1;

            if (answered != 0) {
                sp = ((long) updatedTime / (long) answered);
                sec = ((sp / 1000) % 60);
            } else {
                // opendialog(getResources().getString(R.string.message_notattemptedsinglequestion), 1);
            }

            //Log.e("c111","c111 "+c1.toString());

            c1.put(DatabaseHelper.SPEED, Long.toString(sec));

            try {
                DatabaseHelper.getInstance(PracticeActivity.this).saveexaminationdetails(c1, exam.getExamid());
            }catch (Exception e){
                reporterror(tag, e.toString());
                e.printStackTrace();
            }



      /* ContentValues c = new ContentValues();
        c.put(DatabaseHelper.EXAMID, exam.getExamid());
        c.put(DatabaseHelper.TIMETAKEN, updatedTime);
        c.put(DatabaseHelper.ANSWERED, answered);
        c.put(DatabaseHelper.SKIPP, 0);
        c.put(DatabaseHelper.CORRECT, answered);
        c.put(DatabaseHelper.WRONG, 0);
        c.put(DatabaseHelper.EXAMTYPE, exam.getExam_type());
        c.put(DatabaseHelper.PROGRESS, progresstext.getText().toString().replace("%", ""));
        //c.put(DatabaseHelper.TIMETAKENPERQUESTION, Long.toString(total_time_per_question));
        long sec = 1;
        long sp = 1;
        if (answered != 0) {
            sp = ((long) updatedTime / (long) answered);
            sec = ((sp / 1000) % 60);
        } else {
            //Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
        }
        c.put(DatabaseHelper.SPEED, Long.toString(sec));
        Log.e("data ", "data " + c.toString());*/


            //  DatabaseHelper.getInstance(PracticeActivity.this).saveexaminationdetails(c, exam.getExamid());

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
                ContentValues c = new ContentValues();
                c.put(DatabaseHelper.JSONDATA, mainobj.toString());
                DatabaseHelper.getInstance(PracticeActivity.this).updateqdata(c, exam.getExamid(), exam.getLanguages());

                try {
                    Intent intent = new Intent(Constants.BROADCAST_WIZARD);
                    intent.putExtra(Constants.BROADCAST_RESPONSE_CODE, 200);
                    intent.putExtra(Constants.BROADCAST_URL_ACCESS, Connection.Dataupdated.ordinal());
                    intent.putExtra(Constants.BROADCAST_DATA, Integer.toString(exam.getExamid()));
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                } catch (Exception e) {
                    reporterror(tag, e.toString());
                    e.printStackTrace();
                }
                this.finish();
            } catch (Exception e) {
                reporterror(tag, e.toString());
                e.printStackTrace();
            }

          //  exam.setExamstatus("Attempted");
            Intent i = new Intent(PracticeActivity.this, ProgressReportActivity.class);
            //i.putExtra("progressreport", exam);
            i.putExtra("examidprogress", exam.getExamid());
            i.putExtra("flag", 1);
            startActivity(i);
            finish();
        } catch (Exception e) {
            reporterror(tag, e.toString());
            e.printStackTrace();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        return true;
    }

/*    @Override
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
        try {
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
            //c1.put(DatabaseHelper.TIMETAKENPERQUESTION, Long.toString(total_time_per_question));

            long sec = 1;
            long sp = 1;
            if (answered != 0) {
                sp = ((long) updatedTime / (long) answered);
                sec = ((sp / 1000) % 60);
            } else {
                //opendialog("You haven't attempted a single question, do you still want to submit?", 1);
            }
            c1.put(DatabaseHelper.SPEED, Long.toString(sec));

            try{
            DatabaseHelper.getInstance(PracticeActivity.this).saveexaminationdetails(c1, exam.getExamid());

            }catch (Exception e){
                reporterror(tag, e.toString());
                e.printStackTrace();
            }

        } catch (Exception e) {
            reporterror(tag, e.toString());
            e.printStackTrace();
        }
    }

    public void submitdata() {
        try {
            updateexaminationdetails();
            PracticeActivity.this.finish();
            if (dialog != null)
                dialog.dismiss();
       /* if (timer != null)
            timer.cancel();*/
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
                        obj.put("answername", "AB");
                    /*Log.e("question id is ",qdata.get(i).getId()+"");
                    Log.e("answer is",ans+"");
                    Log.e("subjectid is ", qdata.get(i).getSubjectid()+"");
                    Log.e("unitid is ",qdata.get(i).getUnitid()+"");
                    Log.e("srno is ",qdata.get(i).getSrno()+"");*/
                        //Log.e("option type name is ",qdata.get(i).getOptiontypename());
                        array.put(obj);
                    }

                    JSONObject obj1 = new JSONObject();
                    obj1.put("quesid", qdata.get(i).getId());
                    obj1.put("srno", qdata.get(i).getSrno());
                    array1.put(obj1);
                } catch (Exception e) {
                    reporterror(tag, e.toString());
                    e.printStackTrace();
                }
            }

            try {
                JSONObject obj = new JSONObject();
                obj.put("examid", exam.getExamid());
                obj.put("language", exam.getLanguages());
                obj.put("castecategory", "");
                obj.put("userid", GlobalValues.student.getId());
                obj.put("FilterParameter", array.toString());
                JSONObject root = new JSONObject();
                JSONObject subroot = new JSONObject();
                root.put("root", subroot);
                subroot.put("subroot", array1);
                JSONObject obj1 = new JSONObject();

            /*obj1.put("attemptedques",);
            obj1.put("notattemptedques",);
            obj1.put("rightanswered",);
            obj1.put("wronganswered",);
            obj1.put("rightmarks",);
            obj1.put("negativemarks",);
            obj1.put("obtainedmarks",);
            obj1.put("version",);*/

                obj1.put("userid", GlobalValues.student.getId());
                obj1.put("examid", exam.getExamid());
                obj1.put("Type", "stop");
                obj1.put("examdatetime", DateUtils.getSqliteTime());
                obj1.put("filterparameter", root.toString());

                JSONObject obj2 = new JSONObject();
                obj2.put("userid", GlobalValues.student.getId());
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
                genloading(getResources().getString(R.string.loading_questions));

                //ConnectionManager.getInstance(PracticeActivity.this).SAVE_ANSWERSHEET(obj.toString(), obj1.toString(), obj2.toString(), id);
            /*} else {
                Toast.makeText(getApplicationContext(), "Data saved offline Thank You", Toast.LENGTH_LONG).show();
            }*/
            } catch (Exception e) {
                reporterror(tag, e.toString());
                e.printStackTrace();
            }
        } catch (Exception e) {
            reporterror(tag, e.toString());
            e.printStackTrace();
        }
    }

    AlertDialog dialog = null;

    public void opendialog(String msg, int flag) {
        try {
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
                    alert.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                } else if (flag == 1) {
                    alert.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            submitdata();
                        }
                    });
                    alert.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                } else if (flag == 2) {
                    alert.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent i = new Intent(PracticeActivity.this, ProgressReportActivity.class);
                            //i.putExtra("progressreport", exam);
                            i.putExtra("examidprogress", exam.getExamid());
                            i.putExtra("flag", 1);
                            startActivity(i);
                            finish();
                        }
                    });
                    alert.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                } else if (flag == 3) {
             /*   alert.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });*/
                    alert.setNegativeButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            PracticeActivity.this.finish();
                        }
                    });
                }
                alert.setCancelable(true);

                if (!((Activity) context).isFinishing()) {
                    dialog = alert.create();
                    dialog.show();
                }
            } else {

            }
        } catch (Exception e) {
            reporterror(tag, e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void itemClicked(Question item) {
        try {
            //  Toast.makeText(this, "Item: " + item.getSrno() + " clicked", Toast.LENGTH_SHORT).show();
            if (qdata.size() > (item.getSrno() - 1)) {
                mPager.setCurrentItem(item.getSrno() - 1);

               /* if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else */
                if (drawer.isDrawerOpen(GravityCompat.END)) {
                    drawer.closeDrawer(GravityCompat.END);
                }

            }

        } catch (Exception e) {
            reporterror(tag, e.toString());
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
            try {
                // Log.e("in update","inupdate");
                timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
                updatedTime = timeSwapBuff + timeInMilliseconds + exam.getTimetaken();
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
            } catch (Exception e) {
                reporterror(tag, e.toString());
                e.printStackTrace();
            }
        }

    };


    @Override
    protected void onPostResume() {
        super.onPostResume();
        try {
            isresume = true;
            if (isresume) {
                isresume = false;
                //startTime = SystemClock.uptimeMillis();
                //customHandler.postDelayed(updateTimerThread, 0);
                resumebtn.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause));
            }
        } catch (Exception e) {
            reporterror(tag, e.toString());
            e.printStackTrace();
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
                    progress.setProgress(Integer.parseInt(String.valueOf(obj.getDouble(DatabaseHelper.PROGRESS))));
                    progresstext.setText(String.valueOf(obj.getDouble(DatabaseHelper.PROGRESS)) + "%");
                    practiceTestModels.get(i).setTimetaken(obj.getLong(DatabaseHelper.TIMETAKEN));
                    exam.setTimetaken(obj.getLong(DatabaseHelper.TIMETAKEN));
                    practiceTestModels.get(i).setSpeed(obj.getDouble(DatabaseHelper.SPEED));
                    //practiceTestModels.get(i).setTotal_questions(obj.getString(DatabaseHelper.QUESTION));
                }
            }
            practiceTestAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            reporterror(tag, e.toString());
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
            reporterror(tag, e.toString());
            e.printStackTrace();
        }
        super.back();
    }

    BroadcastReceiver recforsubmit = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if (qdata.get(mPager.getCurrentItem()).getId() == intent.getIntExtra("QidFragment", 0)) {


                    if (qdata.get(mPager.getCurrentItem()).isIsanswered()) {

                        if ((qdata.get(mPager.getCurrentItem()).isIsskipped() || qdata.get(mPager.getCurrentItem()).isIssubmit())) {

                            submitandview.setVisibility(View.GONE);
                            bt5.setVisibility(View.VISIBLE);
                        } else {

                            submitandview.setVisibility(View.VISIBLE);
                            bt5.setVisibility(View.GONE);
                            submitandview.setText(getResources().getString(R.string.submit));
                            submitandview.setBackgroundResource(R.drawable.rounded_button_layout_submit);
                            submitandview.setTextColor(getResources().getColor(R.color.white));
                        }

                    }

                    if ((!qdata.get(mPager.getCurrentItem()).isIsanswered()) && (qdata.get(mPager.getCurrentItem()).isIsskipped() || qdata.get(mPager.getCurrentItem()).isIssubmit())) {
                        submitandview.setVisibility(View.GONE);
                        bt5.setVisibility(View.VISIBLE);
                    }
                /*else{
                    Log.e("answered data ","else skip");
                    submitandview.setText(getResources().getString(R.string.skip));
                    submitandview.setBackgroundResource(R.drawable.rounded_button_layout);
                    submitandview.setTextColor(getResources().getColor(R.color.Gray500));
                    qdata.get(mPager.getCurrentItem()).setIssubmit(false);
                }*/
                }
            } catch (Exception e) {
                reporterror(tag, e.toString());
                e.printStackTrace();
            }
        }
    };

    @Override
    protected void onDestroy() {
        try {
            LocalBroadcastManager.getInstance(PracticeActivity.this).unregisterReceiver(recforsubmit);
        } catch (Exception e) {
            reporterror(tag, e.toString());
            e.printStackTrace();
        }
        super.onDestroy();
    }


    public void updatedatawithoutsendingtoserver() {
        int totalwrong = 0;
        int totalquestions = qdata.size();
        int totalright = 0;
        double obtainedmarks = 0;
        double negativemarks = 0;
        int totalnotanswered = 0;
        int skipp = 0;
        for (int i = 0; i < qdata.size(); i++) {
            if (qdata.get(i).isIsskipped()) {
                totalnotanswered++;
                skipp++;
            } else {
                if (qdata.get(i).isIsanswered()) {

                    ArrayList<Options> optdata = qdata.get(i).getOptions();
                    for (int j = 0; j < optdata.size(); j++) {
                        if (optdata.get(j).isSelected()) {

                            if (optdata.get(j).isAnswer()) {
                                totalright++;
                                obtainedmarks = obtainedmarks + (Double.parseDouble(qdata.get(i).getMarks()));
                            } else {
                                qdata.get(i).setIswrongAnswer(true);
                                totalwrong++;
                                negativemarks = negativemarks + (Double.parseDouble(exam.getNeagativemarks()));
                            }
                        } else {

                        }
                    }
                } else {
                    totalnotanswered++;

                }
            }
        }
        ContentValues c1 = new ContentValues();
        c1.put(DatabaseHelper.EXAMID, exam.getExamid());
        c1.put(DatabaseHelper.TIMETAKEN, updatedTime);
        exam.setTimetaken((Long) c1.get(DatabaseHelper.TIMETAKEN));
        c1.put(DatabaseHelper.ANSWERED, answered);
        c1.put(DatabaseHelper.SKIPP, skipp);
        c1.put(DatabaseHelper.CORRECT, totalright);
        c1.put(DatabaseHelper.WRONG, totalwrong);
        c1.put(DatabaseHelper.QUESTION, totalquestions);
        c1.put(DatabaseHelper.EXAMTYPE, exam.getExam_type());
        c1.put(DatabaseHelper.PROGRESS, progresstext.getText().toString().replace("%", ""));
        long sec = 1;
        long sp = 1;
        if (answered != 0) {
            sp = ((long) updatedTime / (long) answered);
            sec = ((sp / 1000) % 60);
        } else {
            // opendialog(getResources().getString(R.string.message_notattemptedsinglequestion), 1);
        }

        c1.put(DatabaseHelper.SPEED, Long.toString(sec));

        try{
        DatabaseHelper.getInstance(PracticeActivity.this).saveexaminationdetails(c1, exam.getExamid());
        }catch (Exception e){
            reporterror(tag, e.toString());
            e.printStackTrace();
        }
    }

    public void addtofirebasedb(int flag) {
        try {
            if (InternetUtils.getInstance(getApplicationContext()).available()) {
                DatabaseReference databaseReference;
                databaseReference = FirebaseDatabase.getInstance().getReference(Constants.firebasedbname);

                //  databaseReference.child(GlobalValues.student.getMobile()).setValue(values);
                Map<String, Object> childUpdates = new HashMap<>();
                //  childUpdates.put("/posts/" + key, postValues);
                Map<String, Object> values = new HashMap<>();
                values.put("examid", exam.getExamid());
                values.put("examname", exam.getExamname());
                values.put("isattempted", 1);
                values.put("type", "Practice Test");
                childUpdates.put("" + exam.getExamid(), values);
                if (flag == 0) {
                    databaseReference.child(GlobalValues.student.getMobile()).child("Exam").updateChildren(childUpdates);
                } else {
                   /*Map<String, Object> childUpdates = new HashMap<>();
                    //childUpdates.put("/posts/" + key, postValues);
                    Map<String, Object> values = new HashMap<>();
                    values.put("examname", exam.getExamname());
                    values.put("isattempted", 1);
                    childUpdates.put(GlobalValues.student.getMobile() + "/Exam/"+exam.getExamid(), values);*/
                    databaseReference.child(GlobalValues.student.getMobile()).child("Exam").setValue(childUpdates);
                    // Log.e("update ","insert ");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
