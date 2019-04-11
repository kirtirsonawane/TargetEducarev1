package com.targeteducare;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.NavigationView;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.targeteducare.Adapter.ItemClickListener;
import com.targeteducare.Adapter.QuestionAdapter;
import com.targeteducare.Adapter.Section;
import com.targeteducare.Adapter.SectionedExpandableLayoutHelper;
import com.targeteducare.Classes.Exam;
import com.targeteducare.Classes.Item;
import com.targeteducare.Classes.Question;
import com.targeteducare.database.DatabaseHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class ExamActivity extends Activitycommon implements NavigationView.OnNavigationItemSelectedListener, ItemClickListener, Html.ImageGetter {
    RecyclerView mRecyclerView;
    static final int ITEMS = 10;
    QuestionAdapter mAdapter;
    ViewPager mPager;
    TextView txt1, txt2, txt3, txt4, txt5, txt6, txt7, txt8, txt9, txt10, txt11, txt12;
    SectionedExpandableLayoutHelper sectionedExpandableLayoutHelper;
    ArrayList<Question> qdata;
    Button bt1, bt2, bt3, bt4;
    Exam exam;
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

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_exam);
        qdata = new ArrayList<>();
        registerreceiver();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        Log.e("examactivity ", "examactivity");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Bundle b = getIntent().getExtras();
        if (b != null) {
            if (b.containsKey("exam")) {
                this.exam = (Exam) b.getSerializable("exam");
                setTitle(exam.getExamname());
                title = (TextView) findViewById(R.id.title);
                title.setText(exam.getExamname());
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
        bt1 = (Button) findViewById(R.id.first);
        bt2 = (Button) findViewById(R.id.previous);
        bt3 = (Button) findViewById(R.id.next);
        bt4 = (Button) findViewById(R.id.last_button);

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
        txt4.setText("Duration : " + exam.getDurationinMin() + " Mins");
        txt9.setText("Negative Marks per question : " + exam.getNeagativemarks());
        txt8.setText("Total Marks : " + exam.getMarks());
        txt10.setText("Total Attempted Questions: " + answered);
        txt11.setText("Total Not Attempted Questions: " + notanswered);
        txt12.setText("Total Review Question: " + review);
        txt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (review > 0) {
                    opendialog("You have mention some questions as review question,still do you want to submit exam?", 1);
                } else {
                    opendialog("After submitting examination will be closed.Do you want to submit this exam?", 1);
                }
            }
        });

        txt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  if (qdata.get(mPager.getCurrentItem()).isIsanswered()) {
                qdata.get(mPager.getCurrentItem()).setIsreview(!qdata.get(mPager.getCurrentItem()).isIsreview());
                if (qdata.get(mPager.getCurrentItem()).isIsreview()) {
                    txt1.setText("Remove Review");
                    review = review + 1;
                    if (qdata.get(mPager.getCurrentItem()).isIsanswered()) {
                        Log.e("isanswered ", "is answerred ");
                        answered = answered - 1;
                        qdata.get(mPager.getCurrentItem()).setIsanswered(false);
                        for (int i = 0; i < qdata.get(mPager.getCurrentItem()).getOptions().size(); i++) {
                            qdata.get(mPager.getCurrentItem()).getOptions().get(i).setSelected(false);
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
                    txt1.setText("Review");
                    if (review > 0)
                        review = review - 1;
                   /* if (qdata.get(mPager.getCurrentItem()).isIsanswered())
                        answered = answered + 1;*/
                }
                sectionedExpandableLayoutHelper.notifyDataSetChanged();
                txt10.setText("Total Attempted Questions: " + answered);
                txt11.setText("Total Not Attempted Questions: " + notanswered);
                txt12.setText("Total Review Question: " + review);
              /*  } else {
                    if (!((Activity) context).isFinishing()) {
                        Toast.makeText(getApplicationContext(), "Not answered", Toast.LENGTH_LONG).show();
                    } else {
                        Log.e("no activity", "no activity ");
                    }
                }*/
            }
        });

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
                txt2.setText(s);

                txt2.setText(hour + ":" + min + ":" + sec);
                txt5.setText("Time left : " + hour + ":" + min + ":" + sec);

                long hour1 = (((l1 / 1000) / 60)) / 60;
                long min1 = (((l1 / 1000) / 60)) % 60;
                long sec1 = ((l1 / 1000) % 60);

                txt6.setText("Time Expended : " + hour1 + ":" + min1 + ":" + sec1);
                if (l <= (alertbefore * 60000)) {
                    if (!isshown) {
                        opendialog("This exam will be submitting after " + alertbefore + " min automatically. Please Review and compile your exam.", 0);
                        isshown = true;
                    }
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
        sectionedExpandableLayoutHelper = new SectionedExpandableLayoutHelper(this, mRecyclerView, this, 6, false);

        sectionedExpandableLayoutHelper.notifyDataSetChanged();
        mPager = (ViewPager) findViewById(R.id.pager);
        mAdapter = new QuestionAdapter(getSupportFragmentManager(), qdata, false);
        mPager.setAdapter(mAdapter);
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (qdata.get(position).isIsreview()) {
                    txt1.setText("Remove Review");
                } else {
                    txt1.setText("Review");
                }
                qdata.get(position).setIsvisited(true);
                sectionedExpandableLayoutHelper.notifyDataSetChanged();
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
                ConnectionManager.getInstance(ExamActivity.this).getquestion(mainobj.toString(), exam.getExamid(), exam.getLanguages());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void notifyqdatachanged(Question q) {
        if (notanswered > 0)
            notanswered = notanswered - 1;
        answered = answered + 1;

        if (q.isIsreview()) {
            review = review - 1;
            q.setIsreview(false);
            txt1.setText("Review");
        }

        txt10.setText("Total Attempted Questions: " + answered);
        txt11.setText("Total Not Attempted Questions: " + notanswered);
        txt12.setText("Total Review Question: " + review);
        sectionedExpandableLayoutHelper.notifyDataSetChanged();

        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getSubjectid() == q.getSubjectid()) {
                items.get(i).setTotalnotanswered(items.get(i).getTotalnotanswered() - 1);
                // items.get(i).setTotal(items.get(i).getTotalnotanswered()-1);
                sectionedExpandableLayoutHelper.notifyDataSetChanged();
            }
        }
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
                            DatabaseHelper.getInstance(ExamActivity.this).saveqdata1(c, exam.getExamid(), exam.getLanguages());
                            if (exam.isInstantExamResult() || exam.isInstantExamResultWithAns()) {
                                Intent intent = new Intent(ExamActivity.this, AnswersheetActivity.class);
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
                        DatabaseHelper.getInstance(ExamActivity.this).saveqdata1(c, exam.getExamid(), exam.getLanguages());
                        if (exam.isInstantExamResult() || exam.isInstantExamResultWithAns()) {
                            Intent intent = new Intent(ExamActivity.this, AnswersheetActivity.class);
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
            sectionedExpandableLayoutHelper.removeallSection();
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
                mAdapter = new QuestionAdapter(getSupportFragmentManager(), qdata, false);
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
                sectionedExpandableLayoutHelper.addSection(items.get(i));
                sectionedExpandableLayoutHelper.notifyDataSetChanged();
            }

            txt7.setText("Questions: " + qdata.size());
            notanswered = qdata.size();
            txt10.setText("Total Attempted Questions: " + answered);
            txt11.setText("Total Not Attempted Questions: " + notanswered);
            txt12.setText("Total Review Question: " + review);

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

        if (drawer.isDrawerOpen(Gravity.RIGHT)) {
            drawer.closeDrawer(Gravity.RIGHT);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return true;
    }

    @Override
    public void itemClicked(Question item) {
        try {
            if (!((Activity) context).isFinishing()) {
                // Toast.makeText(this, "Item: " + item.getSrno() + " clicked", Toast.LENGTH_SHORT).show();
            } else {
                Log.e("no activity", "no activity ");
            }
            if (qdata.size() > (item.getSrno() - 1)) {
                mPager.setCurrentItem(item.getSrno() - 1);
                drawer.closeDrawer(GravityCompat.END);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void itemClicked(Section section) {
        if (!((Activity) context).isFinishing()) {
            // Toast.makeText(this, "Section: " + section.getItem().getName() + " clicked", Toast.LENGTH_SHORT).show();
        } else {
            Log.e("no activity", "no activity ");
        }
    }

    @Override
    protected void onDestroy() {
        if (timer != null)
            timer.cancel();
        super.onDestroy();
    }

    @Override
    public Drawable getDrawable(String s) {
        return null;
    }

    public void submitdata() {
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
            obj.put("castecategory", GlobalValues.student.getCasteCategory());
            obj.put("userid", GlobalValues.student.getId());
            obj.put("FilterParameter", array.toString());

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
            // if (InternetUtils.getInstance(ExamActivity.this).available()) {
            Log.e("answerdataaaa", "anserdata " + obj.toString());
            genloading("loading..");
            ConnectionManager.getInstance(ExamActivity.this).SAVE_ANSWERSHEET(obj.toString(), obj1.toString(), obj2.toString(), id);
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
            }
            alert.setCancelable(true);
            dialog = alert.create();
            dialog.show();
        } else {
            Log.e("activity", "activity is not running");
        }

    }

}
