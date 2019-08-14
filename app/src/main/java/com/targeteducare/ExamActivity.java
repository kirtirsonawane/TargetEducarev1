package com.targeteducare;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
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
import com.targeteducare.Adapter.ExamAdapter;
import com.targeteducare.Adapter.ItemClickListener;
import com.targeteducare.Adapter.QuestionAdapter;
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
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class ExamActivity extends Activitycommon implements NavigationView.OnNavigationItemSelectedListener, ItemClickListener, Html.ImageGetter {
    RecyclerView mRecyclerView;
    static final int ITEMS = 10;
    QuestionAdapter mAdapter;
    ViewPager mPager;
    TextView txt1, txt2, txt3, txt4, txt5, txt6, txt7, txt8, txt9, txt10, txt11, txt12;
    SectionedExpandableLayoutHelper sectionedExpandableLayoutHelper;
    ArrayList<Question> qdata;
    Button bt1, bt2, bt3, bt4;
    Exam exam = new Exam();
    int notanswered = 0;
    int answered = 0;
    int review = 0;
    long id = 0;
    DrawerLayout drawer;
    CountDownTimer timer;
    boolean isshown = false;
    String starttime = "";
    int alertbefore = 10;
    ImageView imgright;
    TextView title;
    ImageView icback;
    TextView result;
    ProgressBar progress;
    TextView progresstext, timertxt;
    ImageView resumebtn;
    Timer t;
    boolean isresume = false;
    String lang = "";
    String type = "";
    Bundle b1;
    long hour = 0L;
    long min = 0L;
    long sec = 0L;
    String tag = "";

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        try {
            screenshot_capture_permission();
            super.onCreate(savedInstanceState);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

            tag = this.getClass().getSimpleName();
            loadLocale();
            setContentView(R.layout.activity_exam);
            qdata = new ArrayList<>();
            registerreceiver();
            progress = (ProgressBar) findViewById(R.id.progressBar);
            progresstext = (TextView) findViewById(R.id.pregressupdates);
            timertxt = (TextView) findViewById(R.id.timertxt);
            resumebtn = (ImageView) findViewById(R.id.timeview);
            lang = getSharedPreferences("Settings", Activity.MODE_PRIVATE).getString("Current Language", "");

            b1 = getIntent().getExtras();
            type = b1.getString("testtype");

            Log.e("type ", "typeee " + type + "ExamActivity");

            try {
                if (lang.equals("mr")) {
                    exam.setLanguages("marathi");
                } else {
                    exam.setLanguages("ENGLISH");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

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

            resumebtn.setVisibility(View.GONE);
            resumebtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        if (isresume) {
                            isresume = false;
                            startTime = SystemClock.uptimeMillis();
                            customHandler.postDelayed(updateTimerThread, 0);
                            resumebtn.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause));
                        } else {
                            isresume = true;
                            timeSwapBuff += timeInMilliseconds;
                            customHandler.removeCallbacks(updateTimerThread);
                            resumebtn.setImageDrawable(getResources().getDrawable(R.drawable.ic_resume));
                        }
                    } catch (Exception e) {
                        reporterror(tag, e.toString());
                        e.printStackTrace();
                    }
                }
            });

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
                                    Log.e("nt exists ", "nt exists " + dataSnapshot.toString());
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

            result = (TextView) findViewById(R.id.textview_result);
            drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            Bundle b = getIntent().getExtras();
            if (b != null) {
                if (b.containsKey("exam")) {
                    this.exam = (Exam) b.getSerializable("exam");
                    setTitle(exam.getExamname());
                    title = (TextView) findViewById(R.id.title);
                    title.setText(exam.getExamname());
                    result.setText(exam.getExamname());

                    Log.e("exam ", "exam " + exam.getExamid());
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
                    try {
                        if (drawer.isDrawerOpen(GravityCompat.START)) {
                            drawer.closeDrawer(GravityCompat.START);
                        } else {
                            drawer.openDrawer(GravityCompat.START);
                        }
                    } catch (Exception e) {
                        reporterror(tag, e.toString());
                        e.printStackTrace();
                    }
                }
            });

            imgright = (ImageView) findViewById(R.id.menuRight);
            imgright.setOnClickListener(new View.OnClickListener() {
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
            icback = (ImageView) findViewById(R.id.menuback);
            icback.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        if (drawer.isDrawerOpen(GravityCompat.START)) {
                            drawer.closeDrawer(GravityCompat.START);
                        } else if (drawer.isDrawerOpen(GravityCompat.END)) {
                            drawer.closeDrawer(GravityCompat.END);
                        } else {
                            opendialog(getResources().getString(R.string.dialog_testsubmitexit), 1);
                        }
                    } catch (Exception e) {
                        reporterror(tag, e.toString());
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

            bt1 = (Button) findViewById(R.id.first);
            bt2 = (Button) findViewById(R.id.previous);
            bt3 = (Button) findViewById(R.id.next);
            bt4 = (Button) findViewById(R.id.last_button);

            bt1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        if (qdata.size() > 1) {
                            mPager.setCurrentItem(0);
                            bt2.setEnabled(false);
                            bt3.setEnabled(true);
                        }
                    } catch (Exception e) {
                        reporterror(tag, e.toString());
                        e.printStackTrace();
                    }
                }
            });


            bt2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
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
                    } catch (Exception e) {
                        reporterror(tag, e.toString());
                        e.printStackTrace();
                    }
                }
            });

            bt3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        int currentpgid = mPager.getCurrentItem() + 1;
                        if ((currentpgid) < qdata.size()) {
                            mPager.setCurrentItem((currentpgid));
                        }
                        if (currentpgid == (qdata.size() - 1)) {
                            bt3.setEnabled(false);
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

            txt1 = (TextView) findViewById(R.id.textview_review);
            txt2 = (TextView) findViewById(R.id.textview_timer);
            txt3 = (TextView) findViewById(R.id.textview_submit);
            txt4 = (TextView) findViewById(R.id.textview_duration);
            txt5 = (TextView) findViewById(R.id.textview_timeleft);
            txt6 = (TextView) findViewById(R.id.textview_timeextended);
            txt7 = (TextView) findViewById(R.id.textview_question);
            txt8 = (TextView) findViewById(R.id.textview_totalmarks);
            txt9 = (TextView) findViewById(R.id.textview_negativemarks);
            txt10 = (TextView) findViewById(R.id.textview_attemptedquestions);
            txt11 = (TextView) findViewById(R.id.textview_totalnotattemtedq);
            txt12 = (TextView) findViewById(R.id.textview_totalreviewq);

            txt1.setTypeface(Fonter.getTypefaceregular(ExamActivity.this));
            txt2.setTypeface(Fonter.getTypefaceregular(ExamActivity.this));
            txt3.setTypeface(Fonter.getTypefaceregular(ExamActivity.this));
            txt4.setTypeface(Fonter.getTypefacebold(ExamActivity.this));
            txt5.setTypeface(Fonter.getTypefacebold(ExamActivity.this));
            txt6.setTypeface(Fonter.getTypefacebold(ExamActivity.this));
            txt7.setTypeface(Fonter.getTypefacebold(ExamActivity.this));
            txt8.setTypeface(Fonter.getTypefacebold(ExamActivity.this));
            txt9.setTypeface(Fonter.getTypefacebold(ExamActivity.this));
            txt10.setTypeface(Fonter.getTypefacebold(ExamActivity.this));
            txt11.setTypeface(Fonter.getTypefacebold(ExamActivity.this));
            txt12.setTypeface(Fonter.getTypefacebold(ExamActivity.this));

            txt4.setText(": " + exam.getDurationinMin() + " Mins");
            txt9.setText(": " + exam.getNeagativemarks());
            txt8.setText(": " + exam.getMarks());
            txt10.setText(": " + answered);
            txt11.setText(": " + notanswered);
            txt12.setText(": " + review);

            txt3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        if (review > 0) {
                            opendialog(getResources().getString(R.string.message_questionreview_stillsubmit), 1);
                        } else {
                            opendialog(getResources().getString(R.string.message_questionsubmit_noreview), 1);
                        }
                    } catch (Exception e) {
                        reporterror(tag, e.toString());
                        e.printStackTrace();
                    }
                }
            });

            txt1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
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
                            } else {
                                notanswered = notanswered - 1;
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

                            if (qdata.get(mPager.getCurrentItem()).isIsanswered()) {

                            } else {
                                notanswered = notanswered + 1;
                            }
                   /* if (qdata.get(mPager.getCurrentItem()).isIsanswered())
                        answered = answered + 1;*/
                        }
                        sectionedExpandableLayoutHelper.notifyDataSetChanged();
                        txt10.setText(": " + answered);
                        txt11.setText(": " + notanswered);
                        txt12.setText(": " + review);

                        //  if (qdata.get(mPager.getCurrentItem()).isIsanswered()) {
               /* qdata.get(mPager.getCurrentItem()).setIsreview(!qdata.get(mPager.getCurrentItem()).isIsreview());
                if (qdata.get(mPager.getCurrentItem()).isIsreview()) {
                    for (int i = 0; i < items.size(); i++) {
                        if (items.get(i).getSubjectid() == qdata.get(mPager.getCurrentItem()).getSubjectid()) {
                            items.get(i).setReview(items.get(i).getReview() + 1);
                        }
                    }

                    txt1.setText(getResources().getString(R.string.remove_review));
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
                        }
                    }
                    txt1.setText(getResources().getString(R.string.review_contentexam));
                    if (review > 0)
                        review = review - 1;
                }
                sectionedExpandableLayoutHelper.notifyDataSetChanged();
                txt10.setText(": " + answered);
                txt11.setText(": " + notanswered);
                txt12.setText(": " + review);*/
                    } catch (Exception e) {
                        reporterror(tag, e.toString());
                        e.printStackTrace();
                    }
                }
            });

            final long time = Long.parseLong(exam.getDurationinMin()) * 60 * 1000;
            timer = new CountDownTimer(time, 1000) {
                @Override
                public void onTick(long l) {
                    //l = l - exam.getTimetaken();
                    //long l1 = time - l;
                    try {
                        long l1 = time - l;
                        long hour = (l / 1000) / (60 * 60);
                        long min = (l / 1000) % (60 * 60);
                        long sec = min % 60;
                        hour = (((l / 1000) / 60)) / 60;
                        min = (((l / 1000) / 60)) % 60;
                        sec = ((l / 1000) % 60);
                        String s = ((l / 1000) / 60) + ":" + ((l / 1000) % 60);
                        String s1 = ((l1 / 1000) / 60) + ":" + ((l1 / 1000) % 60);
                        txt2.setText(s);
                        txt2.setText(hour + ":" + min + ":" + sec);
                        txt5.setText(": " + hour + ":" + min + ":" + sec);
                        long hour1 = (((l1 / 1000) / 60)) / 60;
                        long min1 = (((l1 / 1000) / 60)) % 60;
                        long sec1 = ((l1 / 1000) % 60);

                        txt6.setText(": " + hour1 + ":" + min1 + ":" + sec1);
                        if (l <= (alertbefore * 60000)) {
                            if (!isshown) {
                                opendialog("This exam will be submitting after " + alertbefore + " min automatically. Please Review and compile your exam.", 0);
                                isshown = true;
                            }
                        }
                    } catch (Exception e) {
                        reporterror(tag, e.toString());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFinish() {
                    submitdata();
                }
            };

        /*ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();*/

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);

            mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
            sectionedExpandableLayoutHelper = new SectionedExpandableLayoutHelper(this, mRecyclerView, this, 6, false, false);

            sectionedExpandableLayoutHelper.notifyDataSetChanged();
            mPager = (ViewPager) findViewById(R.id.pager);
            mAdapter = new QuestionAdapter(getSupportFragmentManager(), qdata, false);
            mPager.setAdapter(mAdapter);
            mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    try {
                        if (qdata.get(position).isIsreview()) {
                            txt1.setText(getResources().getString(R.string.remove_review));
                        } else {
                            txt1.setText(getResources().getString(R.string.review_contentexam));
                        }
                        qdata.get(position).setIsvisited(true);
                        sectionedExpandableLayoutHelper.notifyDataSetChanged();


                        if (position == (qdata.size() - 1)) {
                            bt3.setEnabled(false);
                        }

                        if (position > 0) {
                            bt2.setEnabled(true);
                        }

                        if (position == 0)
                            bt2.setEnabled(false);

                        if (position < ((qdata.size() - 1))) {
                            bt3.setEnabled(true);
                        }
                    } catch (Exception e) {
                        reporterror(tag, e.toString());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onPageSelected(int position) {

                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });

            registerreceiver();
            JSONArray array = DatabaseHelper.getInstance(ExamActivity.this).getqdata(exam.getExamid(), exam.getLanguages());

            if (array.length() > 0) {
                try {
                    id = array.getJSONObject(0).getLong(DatabaseHelper.ID);
                    JSONObject obj = new JSONObject(array.getJSONObject(0).getString(DatabaseHelper.JSONDATA));
                    parsedata(obj);
                    JSONArray array1 = DatabaseHelper.getInstance(ExamActivity.this).getqurldata(exam.getExamid());

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
                    JSONObject mainobj = new JSONObject();
                    mainobj.put("FilterParameter", obj.toString());
                    ConnectionManager.getInstance(ExamActivity.this).getquestion(mainobj.toString(), exam.getExamid(), exam.getLanguages());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            reporterror(tag, e.toString());
            e.toString();
        }
    }

    public void notifyqdatachanged(Question q) {
        try {
            answered = answered + 1;
            if (q.isIsreview()) {
                review = review - 1;
                q.setIsreview(false);
                txt1.setText("Review");
                for (int i = 0; i < items.size(); i++) {
                    if (items.get(i).getSubjectid() == qdata.get(mPager.getCurrentItem()).getSubjectid()) {
                        items.get(i).setReview(items.get(i).getReview() - 1);
                        // items.get(i).setTotal(items.get(i).getTotalnotanswered()-1);
                        //   sectionedExpandableLayoutHelper.notifyDataSetChanged();
                    }
                }
            } else {
                if (notanswered > 0)
                    notanswered = notanswered - 1;
            }

            txt10.setText(": " + answered);
            txt11.setText(": " + notanswered);
            txt12.setText(": " + review);
            sectionedExpandableLayoutHelper.notifyDataSetChanged();

            for (int i = 0; i < items.size(); i++) {
                if (items.get(i).getSubjectid() == q.getSubjectid()) {
                    items.get(i).setTotalnotanswered(items.get(i).getTotalnotanswered() - 1);
                    sectionedExpandableLayoutHelper.notifyDataSetChanged();
                }
            }

            int x = 0;
            if (qdata.size() > 0)
                x = (int) (((double) answered / (double) qdata.size()) * 100);
            progresstext.setText(x + "%");
            progress.setProgress(x);
        } catch (Exception e) {
            reporterror(tag, e.toString());
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
                        e.printStackTrace();
                    }
                } else if (accesscode == Connection.GET_QUESTIONSEXCEPTION.ordinal()) {
                    try {
                        if (!((Activity) context).isFinishing()) {
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.Connectiontimeout), Toast.LENGTH_LONG).show();
                        } else {
                        }
                        this.finish();
                    } catch (Exception e) {
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
                                Log.e("received", "received " + mainobj.toString());
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
                                long id = DatabaseHelper.getInstance(context).saveqdata(c, exam.getExamid(), exam.getLanguages());

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
                                DatabaseHelper.getInstance(ExamActivity.this).saveqdata(c, exam.getExamid(), exam.getLanguages());
                                if (exam.isInstantExamResult() || exam.isInstantExamResultWithAns()) {
                                    Intent intent = new Intent(ExamActivity.this, AnswersheetActivity.class);
                                    intent.putExtra("exam", exam);
                                    startActivity(intent);
                                }
                                this.finish();
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
                            try {
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
                                long id = DatabaseHelper.getInstance(context).saveqdata(c, exam.getExamid(), exam.getLanguages());
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
                                DatabaseHelper.getInstance(ExamActivity.this).saveqdata(c, exam.getExamid(), exam.getLanguages());
                                if (exam.isInstantExamResult() || exam.isInstantExamResultWithAns()) {
                                    Intent intent = new Intent(ExamActivity.this, AnswersheetActivity.class);
                                    intent.putExtra("exam", exam);
                                    startActivity(intent);
                                }
                                this.finish();
                            } catch(Exception e){
                                e.printStackTrace();
                            }
                        }else{
                            Log.e("not flag ", "not flag " + flag);
                        }
                    } catch(Exception e){
                        Log.e("error ","error "+e.toString());
                        e.printStackTrace();
                    }
                } else if (accesscode == Connection.GET_PARSINGEXCEPTION.ordinal()) {
                    try {
                        if (!((Activity) context).isFinishing()) {
                            if (lang.equalsIgnoreCase("mr")) {
                                opendialog(getResources().getString(R.string.user_langchange_message_marathi), 2);

                            } else {
                                opendialog(getResources().getString(R.string.user_langchange_message_marathi), 2);
                            }
                        } else {
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            reporterror(tag, e.toString());
        }
    }

    JSONObject mainobj;
    ArrayList<Item> items;

    public void parsedata(JSONObject obj) {
        try {
            mainobj = obj;
            JSONArray array = obj.optJSONArray("questions");
            if (array.length() > 0) {
                JSONObject objcheck = array.getJSONObject(0);
                if (lang.equalsIgnoreCase("mr")) {
                    if (objcheck.has("NameInMarathi")) {
                        if (objcheck.getString("NameInMarathi").equalsIgnoreCase("null") || objcheck.getString("NameInMarathi").length() < 0) {
                            opendialog(getResources().getString(R.string.user_langchange_message_marathi), 2);
                        }
                    } else {
                        opendialog(getResources().getString(R.string.user_langchange_message_marathi), 2);
                    }
                } else {
                    if (objcheck.has("Name")) {
                        if (objcheck.getString("Name").equalsIgnoreCase("null") || objcheck.getString("Name").length() < 0) {
                            opendialog(getResources().getString(R.string.user_langchange_message), 2);
                        }
                    } else {
                        opendialog(getResources().getString(R.string.user_langchange_message), 2);
                    }
                }
            }

            HashMap<Item, ArrayList<Item>> mSectionMap = new HashMap<Item, ArrayList<Item>>();
            items = new ArrayList<>();
            sectionedExpandableLayoutHelper.removeallSection();
            if (obj.has("subjects")) {
                JSONObject subobj = obj.getJSONObject("subjects").optJSONObject("questions");
                if (subobj != null) {
                    Item item = new Item();
                    item.setName(subobj.getString("subjectname"));
                    item.setNoofques(subobj.getString("noofques"));
                    item.setSubjectid(subobj.getInt("subjectid"));
                    items.add(item);
                    ArrayList<Item> arrayList = new ArrayList<>();
                } else {
                    JSONArray subarry = obj.getJSONObject("subjects").optJSONArray("questions");
                    if (subarry != null) {
                        for (int i = 0; i < subarry.length(); i++) {
                            subobj = subarry.getJSONObject(i);
                            Item item = new Item();
                            // if(subobj.getJSONObject("subject").opt("subjectname")!=null)
                            item.setName(subobj.getString("subjectname"));
                            item.setNoofques(subobj.getString("noofques"));
                            item.setSubjectid(subobj.getInt("subjectid"));
                            items.add(item);
                            ArrayList<Item> arrayList = new ArrayList<>();
                            mSectionMap.put(item, arrayList);
                        }
                    }
                }

                if (obj.has("config")) {
                    JSONObject configobj = obj.getJSONObject("config");
                    if (configobj.has("version")) {
                        exam.setVersion(configobj.getString("version"));
                    }
                }
                //JSONArray array = obj.optJSONArray("questions");
                ArrayList<Question> quesdata = new ArrayList<>();
                if (array != null) {
                    for (int i = 0; i < array.length(); i++) {
                        Question q = new Question(array.getJSONObject(i));
                        q.setIsanswered(false);
                        for (int j = 0; j < q.getOptions().size(); j++) {
                            q.getOptions().get(j).setSelected(false);
                        }
                        quesdata.add(q);
                    }
                } else {
                    JSONObject obj1 = obj.optJSONObject("questions");
                    if (obj1 != null) {
                        Question q = new Question(obj1);
                        q.setIsanswered(false);
                        for (int j = 0; j < q.getOptions().size(); j++) {
                            q.getOptions().get(j).setSelected(false);
                        }
                        quesdata.add(q);
                    } else {
                        Toast.makeText(getApplicationContext(), "Something went wrong Please try after some time", Toast.LENGTH_LONG).show();
                        this.finish();
                    }
                }

                if (quesdata != null) {
                    qdata.clear();
                    qdata.addAll(quesdata);
                    mAdapter.notifyDataSetChanged();
                    mAdapter = new QuestionAdapter(getSupportFragmentManager(), qdata, false);
                    mPager.setAdapter(mAdapter);
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
                    sectionedExpandableLayoutHelper.addSection(items.get(i));
                    sectionedExpandableLayoutHelper.notifyDataSetChanged();
                }

                txt7.setText(": " + qdata.size());
                notanswered = qdata.size();
                txt10.setText(": " + answered);
                txt11.setText(": " + notanswered);
                txt12.setText(": " + review);
                Calendar cal = Calendar.getInstance();
                starttime = DateUtils.getSqliteTime();
                JSONObject configobj = obj.optJSONObject("config");
                if (configobj != null)
                    if (configobj.has("AlertBefore"))
                        alertbefore = configobj.getInt("AlertBefore");
                timer.start();
                startTime = SystemClock.uptimeMillis();
                customHandler.postDelayed(updateTimerThread, 0);
            } else {
                opendialog(getResources().getString(R.string.user_langchange_message), 2);
            }

        } catch (Exception e) {
            reporterror(tag, e.toString());
        }
    }


    @Override
    public void onBackPressed() {
        try {
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else if (drawer.isDrawerOpen(GravityCompat.END)) {
                drawer.closeDrawer(GravityCompat.END);
            } else if(answered == 0){
                //newly added
                opendialog(getResources().getString(R.string.message_notattemptedsinglequestion), 2);
            }
            else {
                opendialog(getResources().getString(R.string.dialog_testsubmitexit), 1);
            }
        } catch (Exception e) {
            reporterror(tag, e.toString());
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
    protected void onDestroy() {
        try {
            if (timer != null)
                timer.cancel();
        } catch (Exception e) {
            e.toString();
        }
        super.onDestroy();
    }

    @Override
    public Drawable getDrawable(String s) {
        return null;
    }

    public void submitdata() {
        try {
            //  ExamActivity.this.finish();
            if (dialog != null)
                dialog.dismiss();
            if (timer != null)
                timer.cancel();
            JSONArray array = new JSONArray();
            JSONArray array1 = new JSONArray();
            for (int i = 0; i < qdata.size(); i++) {
                String ans = "";
                String ansname = "";
                for (int j = 0; j < qdata.get(i).getOptions().size(); j++) {
                    if (qdata.get(i).getOptions().get(j).isSelected()) {
                        if (ans.length() > 0) {
                            ans = ans + "," + qdata.get(i).getOptions().get(j).getId();
                            ansname = ansname + qdata.get(i).getOptions().get(j).getOptionalp();
                        } else {
                            ans = "" + qdata.get(i).getOptions().get(j).getId();
                            ansname = ansname + qdata.get(i).getOptions().get(j).getOptionalp();
                        }
                    }
                /* Don't implement this
                if(qdata.get(i).getOptions().get(j).isSelected()){
                    optnames.add(String.valueOf(qdata.get(i).getOptions().get(j).getName().charAt(0)));
                    Collections.sort(optnames);
                    Log.e("option order ",optnames.toString());
                    sb = sb.append(optnames.get(i));
                }*/
                }

                String input = ansname;
                char[] charArray = input.toCharArray();
                Arrays.sort(charArray);
                String sortedString = new String(charArray);
                // Log.e("String is ", sb.toString());

                try {
                    if (ans.length() > 0) {
                        //Log.e("here","here");
                        JSONObject obj = new JSONObject();
                        obj.put("questionid", qdata.get(i).getId());
                        obj.put("answer", ans);
                        obj.put("subjectid", qdata.get(i).getSubjectid());
                        obj.put("unitid", qdata.get(i).getUnitid());
                        obj.put("srno", qdata.get(i).getSrno());
                        obj.put("answername", sortedString);
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

            int totalwrong = 0;
            int totalquestions = qdata.size();
            int totalright = 0;
            double obtainedmarks = 0;
            double negativemarks = 0;
            int totalnotanswered = 0;
            for (int i = 0; i < qdata.size(); i++) {

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

            ContentValues c1 = new ContentValues();
            c1.put(DatabaseHelper.EXAMID, exam.getExamid());
            c1.put(DatabaseHelper.TIMETAKEN, updatedTime);
            exam.setTimetaken((Long) c1.get(DatabaseHelper.TIMETAKEN));
            c1.put(DatabaseHelper.ANSWERED, answered);
            c1.put(DatabaseHelper.SKIPP, 0);
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

                opendialog(getResources().getString(R.string.message_notattemptedsinglequestion), 2);
            }

            c1.put(DatabaseHelper.SPEED, Long.toString(sec));

            DatabaseHelper.getInstance(ExamActivity.this).saveexaminationdetails(c1, exam.getExamid());

            double totalmarks = obtainedmarks - negativemarks;

            JSONObject obj = new JSONObject();
            obj.put("examid", exam.getExamid());
            obj.put("attemptedques", (totalquestions - totalnotanswered));
            obj.put("notattemptedques", totalnotanswered);
            obj.put("rightanswered", totalright);
            obj.put("wronganswered", totalwrong);
            obj.put("rightmarks", obtainedmarks);
            obj.put("negativemarks", negativemarks);
            obj.put("obtainedmarks", totalmarks);
            obj.put("version", exam.getVersion());
            obj.put("language", exam.getLanguages());
            obj.put("castecategory", GlobalValues.student.getCasteCategory());
            obj.put("userid", GlobalValues.student.getId());
            obj.put("FilterParameter", array.toString());

            if (qdata != null) {
                for (int i = 0; i < qdata.size(); i++) {
                    totalmarks = totalmarks + (Double.parseDouble(qdata.get(i).getMarks()));
                    for (int j = 0; j < items.size(); j++) {
                        if (items.get(j).getSubjectid() == qdata.get(i).getSubjectid()) {
                            items.get(j).setTotalmarks(items.get(j).getTotalmarks() + (Double.parseDouble(qdata.get(i).getMarks())));
                            qdata.get(i).setDisplayid(items.get(j).getQdata().size() + 1);
                            items.get(j).getQdata().add(qdata.get(i));
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
                                           /* qdata.get(i).setIswrongAnswer(true);
                                            totalwrong++;
                                            negativemarks=negativemarks+(Double.parseDouble(exam.getNeagativemarks()));*/
                                        }
                                    } else {
                                    }
                                }
                            } else {
                                items.get(j).setTotalnotanswered(items.get(j).getTotalnotanswered() + 1);
                            }

                        } else {
                        }
                    }
                }
            } else {
            }

            int i = 0;
            for (i = 0; i < items.size(); i++) {
                Item item = items.get(i);
                String name = item.getName() + "-" + item.getTotalmarks() + "^" + item.getNoofques() + "_" + item.getTotalright() + "_" + item.getObtainedmarks() + "_" + item.getTotalwrong() + "_" + item.getNegativemarks() + "~0.00$0.00#0.00";
                int pos = i + 1;
                obj.put("subject" + pos, name);
            }

            for (int j = i + 1; j <= 5; j++) {
                obj.put("subject" + j, "");
            }

            JSONObject root = new JSONObject();
            JSONObject subroot = new JSONObject();
            root.put("root", subroot);
            subroot.put("subroot", array1);

            JSONObject obj1 = new JSONObject();
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
            long id = DatabaseHelper.getInstance(ExamActivity.this).saveanswerdata(c);

            ContentValues c2 = new ContentValues();
            c2.put(DatabaseHelper.JSONDATA, mainobj.toString());
            c2.put(DatabaseHelper.LANGUAGE, exam.getLanguages());
            c2.put(DatabaseHelper.EXAMID, exam.getExamid());
            c2.put(DatabaseHelper.SAVEDTIME, DateUtils.getSqliteTime());
            DatabaseHelper.getInstance(ExamActivity.this).saveqdata(c2, exam.getExamid(), exam.getLanguages());
//&& GlobalValues.selectedtesttype.equalsIgnoreCase("Monthly Ranking Test"))
            if (exam.getIsOmr() == 1 || exam.getExamstatus().equalsIgnoreCase("Missed")){
                DatabaseHelper.getInstance(context).updatesync(id);
                new_func();
                /*try {
                    //  if (!flag) {
                    JSONArray questions = new JSONArray();
                    for (int i1 = 0; i1 < qdata.size(); i1++) {
                        questions.put(qdata.get(i1).getJsonObject(qdata.get(i1)));
                    }

                    mainobj.put("questions", questions);
                    ContentValues c3 = new ContentValues();
                    c3.put(DatabaseHelper.JSONDATA, mainobj.toString());
                    c3.put(DatabaseHelper.LANGUAGE, exam.getLanguages());
                    c3.put(DatabaseHelper.EXAMID, exam.getExamid());
                    c3.put(DatabaseHelper.SAVEDTIME, DateUtils.getSqliteTime());
                    long id1 = DatabaseHelper.getInstance(context).saveqdata(c3, exam.getExamid(), exam.getLanguages());
                    try {
                        Intent intent = new Intent(Constants.BROADCAST_WIZARD);
                        intent.putExtra(Constants.BROADCAST_RESPONSE_CODE, 200);
                        intent.putExtra(Constants.BROADCAST_URL_ACCESS, Connection.Dataupdated.ordinal());
                        intent.putExtra(Constants.BROADCAST_DATA, Integer.toString(exam.getExamid()));
                        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    // DatabaseHelper.getInstance(ExamActivity.this).saveqdata(c3, exam.getExamid(), exam.getLanguages());
                    if (exam.isInstantExamResult() || exam.isInstantExamResultWithAns()) {
                        Intent intent = new Intent(ExamActivity.this, AnswersheetActivity.class);
                        intent.putExtra("exam", exam);
                        startActivity(intent);
                        this.finish();
                    }
                    this.finish();
                    //  }
                } catch (Exception e) {
                    e.printStackTrace();
                }*/
            } else {
                ConnectionManager.getInstance(ExamActivity.this).SAVE_ANSWERSHEET(obj.toString(), obj1.toString(), obj2.toString(), id);
                /*Log.e("exam status ", exam.getExamstatus() + " exam end date: " +exam.getEnddate());

                if (exam.getExamstatus().equalsIgnoreCase("Missed")) {
                    try {
                        //  if (!flag) {
                        JSONArray questions = new JSONArray();
                        for (int i1 = 0; i1 < qdata.size(); i1++) {
                            questions.put(qdata.get(i1).getJsonObject(qdata.get(i1)));
                        }

                        mainobj.put("questions", questions);
                        ContentValues c3 = new ContentValues();
                        c3.put(DatabaseHelper.JSONDATA, mainobj.toString());
                        c3.put(DatabaseHelper.LANGUAGE, exam.getLanguages());
                        c3.put(DatabaseHelper.EXAMID, exam.getExamid());
                        c3.put(DatabaseHelper.SAVEDTIME, DateUtils.getSqliteTime());
                        long id1 = DatabaseHelper.getInstance(context).saveqdata(c3, exam.getExamid(), exam.getLanguages());
                        try {
                            Intent intent = new Intent(Constants.BROADCAST_WIZARD);
                            intent.putExtra(Constants.BROADCAST_RESPONSE_CODE, 200);
                            intent.putExtra(Constants.BROADCAST_URL_ACCESS, Connection.Dataupdated.ordinal());
                            intent.putExtra(Constants.BROADCAST_DATA, Integer.toString(exam.getExamid()));
                            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        // DatabaseHelper.getInstance(ExamActivity.this).saveqdata(c3, exam.getExamid(), exam.getLanguages());
                        if (exam.isInstantExamResult() || exam.isInstantExamResultWithAns()) {
                            Intent intent = new Intent(ExamActivity.this, AnswersheetActivity.class);
                            intent.putExtra("exam", exam);
                            startActivity(intent);
                            this.finish();
                        }
                        this.finish();
                        //  }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    ConnectionManager.getInstance(ExamActivity.this).SAVE_ANSWERSHEET(obj.toString(), obj1.toString(), obj2.toString(), id);
                }*/
            }
            //finish();
            // if (InternetUtils.getInstance(ExamActivity.this).available()) {
            /*Log.e("answerdataaaa", "anserdata " + obj.toString());
            genloading(getResources().getString(R.string.loading));
            ConnectionManager.getInstance(ExamActivity.this).SAVE_ANSWERSHEET(obj.toString(), obj1.toString(), obj2.toString(), id);*/
            /*} else {
                Toast.makeText(getApplicationContext(), "Data saved offline Thank You", Toast.LENGTH_LONG).show();
            }*/
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
             /*   alert.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });*/
                    alert.setNegativeButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            ExamActivity.this.finish();
                        }
                    });
                }
                alert.setCancelable(true);
                dialog = alert.create();
                if (!((Activity) context).isFinishing()) {
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

                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else if (drawer.isDrawerOpen(GravityCompat.END)) {
                    drawer.closeDrawer(GravityCompat.END);
                }

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
    private Runnable updateTimerThread = new Runnable() {
        public void run() {
            try {
                timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
                //updatedTime = timeSwapBuff + timeInMilliseconds + exam.getTimetaken();
                updatedTime = timeSwapBuff + timeInMilliseconds + exam.getTimetaken();

            /*long hour = (((updatedTime / 1000) / 60)) / 60;
            long min = (((updatedTime / 1000) / 60)) % 60;
            long sec = ((updatedTime / 1000) % 60);*/
                hour = (((updatedTime / 1000) / 60)) / 60;
                min = (((updatedTime / 1000) / 60)) % 60;
                sec = ((updatedTime / 1000) % 60);
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

/*    @Override
    protected void back() {
        opendialog(getResources().getString(R.string.dialog_testsubmitexit), 1);
        //super.back();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_HOME)) {
            opendialog(getResources().getString(R.string.dialog_testsubmitexit), 1);
            return true;
        }
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            opendialog(getResources().getString(R.string.dialog_testsubmitexit), 1);
            return true;
        }
        if ((keyCode == KeyEvent.KEYCODE_MENU)) {
            opendialog(getResources().getString(R.string.dialog_testsubmitexit), 1);
            return true;
        }
        return false;
    }*/

    @Override
    protected void onResume() {
        super.onResume();
        try {
            ArrayList<Exam> mdataset = new ArrayList<>();
            ExamAdapter adapter = new ExamAdapter(ExamActivity.this, mdataset, lang, type);
            for (int i = 0; i < mdataset.size(); i++) {
                JSONArray array = DatabaseHelper.getInstance(ExamActivity.this).getexamdetails(mdataset.get(i).getExamid(), mdataset.get(i).getExam_type());
                if (array.length() > 0) {
                    JSONObject obj = array.getJSONObject(0);
                    mdataset.get(i).setSkipp(obj.getInt(DatabaseHelper.SKIPP));
                    mdataset.get(i).setAnswered(obj.getInt(DatabaseHelper.ANSWERED));
                    mdataset.get(i).setWrong(obj.getInt(DatabaseHelper.WRONG));
                    mdataset.get(i).setCorrect(obj.getInt(DatabaseHelper.CORRECT));
                    mdataset.get(i).setProgress(obj.getDouble(DatabaseHelper.PROGRESS));
                    progress.setProgress(Integer.parseInt(String.valueOf(obj.getDouble(DatabaseHelper.PROGRESS))));
                    progresstext.setText(String.valueOf(obj.getDouble(DatabaseHelper.PROGRESS)) + "%");
                    mdataset.get(i).setTimetaken(obj.getLong(DatabaseHelper.TIMETAKEN));
                    exam.setTimetaken(obj.getLong(DatabaseHelper.TIMETAKEN));
                    mdataset.get(i).setSpeed(obj.getDouble(DatabaseHelper.SPEED));
                    //mdataset.get(i).setTotal_questions(obj.getString(DatabaseHelper.QUESTION));
                }
            }
            adapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }

        /*try{

        ArrayList<Exam> mdataset = new ArrayList<>();

        MockTestAdapter adapter1 = new MockTestAdapter(ExamActivity.this, mdataset);

        for (int i = 0; i < mdataset.size(); i++) {
            JSONArray array = DatabaseHelper.getInstance(ExamActivity.this).getexamdetails(mdataset.get(i).getExamid(), mdataset.get(i).getExam_type());
            if (array.length() > 0) {
                JSONObject obj = array.getJSONObject(0);
                mdataset.get(i).setSkipp(obj.getInt(DatabaseHelper.SKIPP));
                mdataset.get(i).setAnswered(obj.getInt(DatabaseHelper.ANSWERED));
                mdataset.get(i).setWrong(obj.getInt(DatabaseHelper.WRONG));
                mdataset.get(i).setCorrect(obj.getInt(DatabaseHelper.CORRECT));
                mdataset.get(i).setProgress(obj.getDouble(DatabaseHelper.PROGRESS));
                mdataset.get(i).setTimetaken(obj.getLong(DatabaseHelper.TIMETAKEN));
                mdataset.get(i).setSpeed(obj.getDouble(DatabaseHelper.SPEED));
                //practiceTestModels.get(i).setTotal_questions(obj.getString(PracticeDatabaseHelper.QUESTION));
            }
        }
        adapter1.notifyDataSetChanged();

    } catch (Exception e) {
        e.printStackTrace();
    }*/

    }

    public void addtofirebasedb(int flag) {
        try {
            if (InternetUtils.getInstance(getApplicationContext()).available()) {
                DatabaseReference databaseReference;
                databaseReference = FirebaseDatabase.getInstance().getReference(Constants.firebasedbname);
                Map<String, Object> childUpdates = new HashMap<>();
                Map<String, Object> values = new HashMap<>();
                values.put("examid", exam.getExamid());
                values.put("examname", exam.getExamname());
                values.put("isattempted", 1);
                values.put("type", type);
                Log.e("data saved ", "data saved " + exam.getExamid() + " " + exam.getExamname() + " " + exam.getExam_type());
                childUpdates.put( ""+ exam.getExamid(), values);
                if (flag == 0) {
                    databaseReference.child(GlobalValues.student.getMobile()).child("Exam").updateChildren(childUpdates);
                } else {
                   /* Map<String, Object> childUpdates = new HashMap<>();
                    //  childUpdates.put("/posts/" + key, postValues);
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


    private void new_func(){
        try {
            if (!flag) {
                try {
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
                    long id = DatabaseHelper.getInstance(context).saveqdata(c, exam.getExamid(), exam.getLanguages());
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
                    DatabaseHelper.getInstance(ExamActivity.this).saveqdata(c, exam.getExamid(), exam.getLanguages());
                    if (exam.isInstantExamResult() || exam.isInstantExamResultWithAns()) {
                        Intent intent = new Intent(ExamActivity.this, AnswersheetActivity.class);
                        intent.putExtra("exam", exam);
                        startActivity(intent);
                    }
                    this.finish();
                } catch(Exception e){
                    e.printStackTrace();
                }
            }else{
                Log.e("not flag ", "not flag " + flag);
            }
        } catch(Exception e){
            Log.e("error ","error "+e.toString());
            e.printStackTrace();
        }
    }

}
