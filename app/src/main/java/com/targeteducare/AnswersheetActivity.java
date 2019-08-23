package com.targeteducare;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.targeteducare.Adapter.ItemClickListener;
import com.targeteducare.Adapter.QuestionAdapter;
import com.targeteducare.Adapter.Section;
import com.targeteducare.Adapter.SectionedExpandableLayoutHelper;
import com.targeteducare.Classes.Exam;
import com.targeteducare.Classes.Item;
import com.targeteducare.Classes.Options;
import com.targeteducare.Classes.Question;
import com.targeteducare.database.DatabaseHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class AnswersheetActivity extends Activitycommon implements NavigationView.OnNavigationItemSelectedListener, ItemClickListener, Html.ImageGetter {
    RecyclerView mRecyclerView;
    static final int ITEMS = 10;
    QuestionAdapter mAdapter;
    ViewPager mPager;
    TextView txt1, txt2, txt3, txt4, txt5, txt6, txt7, txt8, txt9, txt10, txt11, txt12;
    SectionedExpandableLayoutHelper sectionedExpandableLayoutHelper;
    ArrayList<Question> qdata;
    Button bt1, bt2, bt3, bt4;
    public static Exam exam = new Exam();
    int notanswered = 0;
    int answered = 0;
    int review = 0;
    LinearLayout layout1;
    TextView title;
    ImageView imgright;
    DrawerLayout drawer;
    String lang = "";
    TextView txt1nav, txt2nav, txt3nav, txt4nav, txt5nav, txt6nav, txt7nav, txt8nav, txt9nav, result;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        try {
            screenshot_capture_permission();
            super.onCreate(savedInstanceState);
            loadLocale();
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            setContentView(R.layout.activity_answersheet);
            Log.e("got into ", "AnswersheetActivity");
            qdata = new ArrayList<>();
            registerreceiver();
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            result = (TextView) findViewById(R.id.textview_result);
            lang = getSharedPreferences("Settings", Activity.MODE_PRIVATE).getString("Current Language", "");
            Bundle b = getIntent().getExtras();
            if (b != null) {
                if (b.containsKey("exam")) {
                    this.exam = (Exam) b.getSerializable("exam");
                    setTitle(exam.getExamname());
                    title = (TextView) findViewById(R.id.title);
                    title.setText(exam.getExamname());
                    result.setText(exam.getExamname());
                }
            }
            bt1 = (Button) findViewById(R.id.first);
            bt2 = (Button) findViewById(R.id.previous);
            bt3 = (Button) findViewById(R.id.next);
            bt4 = (Button) findViewById(R.id.last_button);
            layout1 = (LinearLayout) findViewById(R.id.layout_1);
            layout1.setVisibility(View.GONE);

            bt1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (qdata.size() > 1) {
                        mPager.setCurrentItem(0);
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
                }
            });

            bt3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int currentpgid = mPager.getCurrentItem() + 1;
                    if ((currentpgid) < qdata.size()) {
                        mPager.setCurrentItem((currentpgid));
                    }
                }
            });

            bt4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (qdata.size() > 1) {
                        int id = (qdata.size() - 1);
                        mPager.setCurrentItem(id);
                        // Log.e("current ", "" + id);
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
            txt1nav = (TextView) findViewById(R.id.textview_nav1);
            txt2nav = (TextView) findViewById(R.id.textview_nav2);
            //txt3nav = (TextView) findViewById(R.id.textview_nav3);
            txt4nav = (TextView) findViewById(R.id.textview_nav4);
            txt5nav = (TextView) findViewById(R.id.textview_nav5);
            txt6nav = (TextView) findViewById(R.id.textview_nav6);
            txt7nav = (TextView) findViewById(R.id.textview_nav7);
            txt8nav = (TextView) findViewById(R.id.textview_nav8);
            txt9nav = (TextView) findViewById(R.id.textview_nav9);

            txt1.setTypeface(Fonter.getTypefaceregular(AnswersheetActivity.this));
            txt2.setTypeface(Fonter.getTypefaceregular(AnswersheetActivity.this));
            txt3.setTypeface(Fonter.getTypefaceregular(AnswersheetActivity.this));
            txt4.setTypeface(Fonter.getTypefacebold(AnswersheetActivity.this));
            txt5.setTypeface(Fonter.getTypefacebold(AnswersheetActivity.this));
            txt6.setTypeface(Fonter.getTypefacebold(AnswersheetActivity.this));
            txt7.setTypeface(Fonter.getTypefacebold(AnswersheetActivity.this));
            txt8.setTypeface(Fonter.getTypefacebold(AnswersheetActivity.this));
            txt9.setTypeface(Fonter.getTypefacebold(AnswersheetActivity.this));
            txt10.setTypeface(Fonter.getTypefacebold(AnswersheetActivity.this));
            txt11.setTypeface(Fonter.getTypefacebold(AnswersheetActivity.this));
            txt12.setTypeface(Fonter.getTypefacebold(AnswersheetActivity.this));

        /*txt4.setText("Duration : " + exam.getDurationinMin() + " Mins");
        txt9.setText("Negative Marks per question : " + exam.getNeagativemarks());
        txt8.setText("Total Marks : " + exam.getMarks());
        txt10.setText("Total Attempted Questions: " + answered);
        txt11.setText("Total Not Attempted Questions: " + notanswered);
        txt12.setText("Total Review Question: " + review);
        txt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitdata();
            }
        });*/

            txt1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
             /*   if (qdata.get(mPager.getCurrentItem()).isIsanswered()) {
                    qdata.get(mPager.getCurrentItem()).setIsreview(!qdata.get(mPager.getCurrentItem()).isIsreview());
                    if (qdata.get(mPager.getCurrentItem()).isIsreview()) {
                        txt1.setText("Remove from Review");
                        review = review + 1;
                        answered = answered - 1;
                    } else {
                        txt1.setText("Mark as Review");
                        if (review > 0)
                            review = review - 1;
                        answered = answered + 1;
                    }
                    sectionedExpandableLayoutHelper.notifyDataSetChanged();

                    txt10.setText("Total Attempted Questions: " + answered);
                    txt11.setText("Total Not Attempted Questions: " + notanswered);
                    txt12.setText("Total Review Question: " + review);
                } else {
                    Toast.makeText(getApplicationContext(), "Not answered", Toast.LENGTH_LONG).show();
                }*/
                }
            });

/* final long time = Long.parseLong(exam.getDurationinMin()) * 60 * 1000;
        new CountDownTimer(time, 1000) {
            @Override
            public void onTick(long l) {
              *//*  long l1 = time - l;
                String s = ((l / 1000) / 60) + ":" + ((l / 1000) % 60);
                String s1 = ((l1 / 1000) / 60) + ":" + ((l1 / 1000) % 60);
                txt2.setText(s);
                txt5.setText("Time left : " + s + " Mins");
                txt6.setText("Time Expended : " + s1 + " Mins");*//*
            }

            @Override
            public void onFinish() {
                submitdata();
            }
        }.start();*/

            drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);

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
            });

            mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
            sectionedExpandableLayoutHelper = new SectionedExpandableLayoutHelper(this, mRecyclerView, this, 6, true, false);
            sectionedExpandableLayoutHelper.notifyDataSetChanged();
            mPager = (ViewPager) findViewById(R.id.pager);
            mAdapter = new QuestionAdapter(getSupportFragmentManager(), qdata, true);
            mPager.setAdapter(mAdapter);
            mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
              /*  if (qdata.get(position).isIsreview()) {
                    txt1.setText("Remove from Review");
                } else {
                    txt1.setText("Mark as Review");
                }*/
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
            JSONArray array = DatabaseHelper.getInstance(AnswersheetActivity.this).getqdata(exam.getExamid(), exam.getLanguages());
          //  Log.e("Testarray ","test "+array.toString());
            if (array.length() > 0) {
                try {
                  Log.e("via db ", "via db" + array.toString());
                    JSONObject obj = new JSONObject(array.getJSONObject(array.length() - 1).getString(DatabaseHelper.JSONDATA));
                    parsedata(obj);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    genloading(getResources().getString(R.string.loading));
                    JSONObject obj = new JSONObject();
                    obj.put("examid", exam.getExamid());
                    obj.put("language", "ENGLISH");
                    obj.put("studentid",GlobalValues.student.getId());
                    obj.put("ImageRelPath", "http://"+GlobalValues.IP);
                    JSONObject mainobj = new JSONObject();
                    mainobj.put("FilterParameter", obj.toString());
                    if (lang.equals("mr")) {
                        exam.setLanguages("marathi");
                    } else {
                        exam.setLanguages("ENGLISH");
                    }
                    Log.e("got into ", "connection manager");
                    ConnectionManager.getInstance(AnswersheetActivity.this).getanswer(mainobj.toString(), exam.getExamid(), exam.getLanguages());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            reporterror("Anserfrag", e.toString());
        }
    }


    public void notifyqdatachanged() {
        if (notanswered > 0)
            notanswered = notanswered - 1;
        answered = answered + 1;

        txt10.setText(getResources().getString(R.string.total_attemptedquestions) + " " + answered);
        txt11.setText(getResources().getString(R.string.total_notattemptedquestions) + " " + notanswered);
        txt12.setText(getResources().getString(R.string.total_reviewquestions) + " " + review);
        sectionedExpandableLayoutHelper.notifyDataSetChanged();
    }

    @Override
    protected void onResponsed(int statuscode, int accesscode, String data) {
        super.onResponsed(statuscode, accesscode, data);
        if (statuscode == Constants.STATUS_OK) {
            if (accesscode == Connection.GET_QUESTIONS.ordinal()) {
                try {
                    JSONObject obj = new JSONObject(GlobalValues.TEMP_STR).getJSONObject("root").getJSONObject("subroot");
                    Log.e("rec response ", "answersheet activity");
                    parsedata(obj);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (accesscode == Connection.SAVE_ANSWER.ordinal()) {
                try {
                    // JSONObject object = new JSONObject(GlobalValues.TEMP_STR).getJSONObject("root").getJSONObject("subroot");
                    //if (object.has("id")) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.thank_you), Toast.LENGTH_LONG).show();

                    // }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    int totalquestions = 0;
    double totalmarksv1 = 0.0;
    public void parsedata(JSONObject obj) {
        try {
            HashMap<Item, ArrayList<Item>> mSectionMap = new HashMap<Item, ArrayList<Item>>();
            ArrayList<Item> items = new ArrayList<>();
            sectionedExpandableLayoutHelper.removeallSection();
            Log.e("data ", "data " + obj.getJSONObject("subjects"));
            JSONObject subobj = obj.getJSONObject("subjects").optJSONObject("questions");
            if (subobj != null) {
               // Log.e("sub ", "sub " + subobj.toString());
                Item item = new Item();
                if(subobj.has("subjectname"))
                item.setName(subobj.getString("subjectname"));
                item.setNoofques(subobj.getString("noofques"));
                item.setSubjectid(subobj.getInt("subjectid"));
                items.add(item);
                ArrayList<Item> arrayList = new ArrayList<>();
            } else {
                JSONArray subarry = obj.getJSONObject("subjects").optJSONArray("questions");
                if (subarry != null)
                    for (int i = 0; i < subarry.length(); i++) {
                        subobj = subarry.getJSONObject(i);
                        Log.e("subobj", "subobj " + subobj.toString());
                        Item item = new Item();
                        if(subobj.has("subjectname"))
                        item.setName(subobj.getString("subjectname"));
                        item.setNoofques(subobj.getString("noofques"));
                        item.setSubjectid(subobj.getInt("subjectid"));
                        items.add(item);
                        ArrayList<Item> arrayList = new ArrayList<>();
                        mSectionMap.put(item, arrayList);
                    }
            }

            if (items.size() != 0) {
              //  Log.e("items size subject ", items.size() + "");
                for (int n = 0; n < items.size(); n++) {
                    //subjectname^subjectquescount_rightanswered_rightobtainedmarks_wronganswered_negativemarks~0.00$0.00;
                    /*obj.put("Subject"+(n+1),items.get(n).getName()+"^"+items.get(n).getNoofques()+"_"+items.get(n).getTotalright()+"_"+
                            items.get(n).getObtainedmarks()+"_"+items.get(n).getTotalwrong()+"_"+items.get(n).getNegativemarks()+"~0.00$0.00");*/
                    Log.e("Subject" + (n + 1), items.get(n).getName() + "^" + items.get(n).getNoofques() + "_" + items.get(n).getTotalright() + "_" +
                            items.get(n).getObtainedmarks() + "_" + items.get(n).getTotalwrong() + "_" + items.get(n).getNegativemarks() + "~0.00$0.00");
                }
                for (int n1 = items.size(); n1 < 5; n1++) {
                    //subjectname^subjectquescount_rightanswered_rightobtainedmarks_wronganswered_negativemarks~0.00$0.00;
                    /*obj.put("Subject"+(n1+1),"" + "^" + "" + "_" + "" + "_" + "" +"_" + "" +"_" + "" + "~0.00$0.00");*/
                    Log.e("Subject" + (n1 + 1), "" + "^" + "" + "_" + "" + "_" + "" + "_" + "" + "_" + "" + "~0.00$0.00");
                }
            } else {
                Log.e("items size subject ", "0");
            }

            /*for(int n1=qdata.size(); n1<5 ; n1++ ){
             *//*obj.put("Subject"+(n1+1),exam.getTopic_name_select()+"^"+exam.getTotal_questions()+"_"+exam.getTotal_correct()+"_"+
                            exam.getMarks()+"_"+exam.getTotal_wrong()+"_"+exam.getMarks()+"~0.00$0.00");*//*
             *//*Log.e("Subject"+(n1+1),items.get(n1).getName()+"^"+items.get(n1).getNoofques()+"_"+items.get(n1).getTotalright()+"_"+
                        items.get(n1).getObtainedmarks()+"_"+items.get(n1).getTotalwrong()+"_"+items.get(n1).getNegativemarks()+"~0.00$0.00");*//*
                Log.e("Subject"+(n1+1),""+"^"+"0"+"_"+0+"_"+
                        0.0+"_"+0+"_"+0.0+"~0.00$0.00");
            }*/

         /*   JSONArray array = obj.optJSONArray("questions");

            if (array != null) {
                Log.e("questionsans ","questions "+array.toString());
                ArrayList<Question> quesdata = new ArrayList<>();
                for (int i = 0; i < array.length(); i++) {
                    quesdata.add(new Question(array.getJSONObject(i)));
                }
                Log.e("dataaded","");
                if (quesdata != null) {
                    qdata.clear();
                    qdata.addAll(quesdata);
                    mAdapter.notifyDataSetChanged();
                    mAdapter = new QuestionAdapter(getSupportFragmentManager(), qdata,true);
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
            }
*/

            JSONArray array = obj.optJSONArray("questions");
            Log.e("got array ", "got array " + array.toString());
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
                mAdapter = new QuestionAdapter(getSupportFragmentManager(), qdata, true);
                mPager.setAdapter(mAdapter);
            }

            if (quesdata != null) {
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

            for (int i = 0; i < items.size(); i++) {
                sectionedExpandableLayoutHelper.addSection(items.get(i));
                sectionedExpandableLayoutHelper.notifyDataSetChanged();
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
                        }
                    }
                } else {
                    totalnotanswered++;
                }
            }
            double totalmarks = obtainedmarks - negativemarks;
            txt1.setVisibility(View.GONE);
         /*   txt1.setText(Html.fromHtml("<b>"+getResources().getString(R.string.expandable_obtained_marks)+" </b>: (" + obtainedmarks + ")  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <b>"+getResources().getString(R.string.expandable_negative_marks)+"</b> : (" + negativemarks + ")&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"+getResources().getString(R.string.expandable_final_marks)+"</b> : (" + totalmarks + ")" +
                    "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <b> "+getResources().getString(R.string.expandable_total_questions)+"</b> :" + totalquestions +
                    "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b> "+getResources().getString(R.string.expandable_attemptedquestions)+"</b> : (" + (totalquestions - totalnotanswered) + ")&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <b> "+getResources().getString(R.string.expandable_notattemptedquestions)+"</b> : (" + totalnotanswered +
                    ")&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <b>"+getResources().getString(R.string.expandable_rightanswers)+"</b> : (" + totalright + ")&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <b>"+getResources().getString(R.string.expandable_wronganswers)+"</b> : (" + totalwrong + ")"));
          */
            totalmarksv1 = obtainedmarks - negativemarks;
            setTitle(exam.getExamname() + " ( " + totalmarks + " Marks )");
            txt4nav.setText(": " + totalquestions);
            txt5nav.setText(": " + (totalquestions - totalnotanswered));
            txt9nav.setText(": " + totalmarks);
            txt2nav.setText(": " + (Double.parseDouble(exam.getNeagativemarks())));
            txt7nav.setText(": " + totalright);
            txt8nav.setText(": " + totalwrong);
            txt6nav.setText(": " + totalnotanswered);
            txt1nav.setText(": " + totalmarksv1);

            ContentValues c1 = new ContentValues();
            c1.put(DatabaseHelper.EXAMID, exam.getExamid());
            c1.put(DatabaseHelper.TIMETAKEN, 0);
        //    exam.setTimetaken((Long) c1.get(DatabaseHelper.TIMETAKEN));
            c1.put(DatabaseHelper.ANSWERED, (totalquestions - totalnotanswered));
            c1.put(DatabaseHelper.SKIPP, 0);
            c1.put(DatabaseHelper.CORRECT, totalright);
            c1.put(DatabaseHelper.WRONG, totalwrong);
            c1.put(DatabaseHelper.QUESTION, totalquestions);
            c1.put(DatabaseHelper.EXAMTYPE, exam.getExam_type());

            int x = 0;
            if (qdata.size() > 0)
                x = (int) (((int) (totalquestions - totalnotanswered) / (double) qdata.size()) * 100);

            c1.put(DatabaseHelper.PROGRESS, x);
            c1.put(DatabaseHelper.SPEED, "0");
            DatabaseHelper.getInstance(AnswersheetActivity.this).saveexaminationdetails(c1, exam.getExamid());
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

            /*Exam exam = new Exam();
            exam.setTotal_correct(totalright+"");
            exam.setTotal_wrong(totalwrong+"");*/

            /* txt7.setText("Questions: " + qdata.size());
            notanswered=qdata.size();
            txt10.setText("Total Attempted Questions: "+answered);
            txt11.setText("Total Not Attempted Questions: "+notanswered);
            txt12.setText("Total Review Question: "+review);*/
        } catch (Exception e) {
           Log.e("error ","errror "+e.toString());
            // reporterror("Anserfrag", e.toString());
        }
    }


    @Override
    public void onBackPressed() {
        try {
            if (drawer.isDrawerOpen(GravityCompat.START)) {
            }
            if (drawer.isDrawerOpen(GravityCompat.END)) {
                drawer.closeDrawer(GravityCompat.END);
            } else {
                super.onBackPressed();
            }
        } catch (Exception e) {
            reporterror("Anserfrag", e.toString());
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
            //  Toast.makeText(this, "Item: " + item.getSrno() + " clicked", Toast.LENGTH_SHORT).show();
            if (qdata.size() > (item.getSrno() - 1)) {
                mPager.setCurrentItem(item.getSrno() - 1);
                if (drawer.isDrawerOpen(GravityCompat.END)) {
                    drawer.closeDrawer(GravityCompat.END);
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void itemClicked(Section section) {
        //Toast.makeText(this, "Section: " + section.getItem().getName() + " clicked", Toast.LENGTH_SHORT).show();
    }

    @Override
    public Drawable getDrawable(String s) {
        return null;
    }

    public void submitdata() {
     /*   JSONArray array = new JSONArray();
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
            obj1.put("filterparameter", root.toString());
            if (InternetUtils.getInstance(AnswersheetActivity.this).available()) {
                genloading("loading..");
                ConnectionManager.getInstance(AnswersheetActivity.this).SAVE_ANSWERSHEET(obj.toString(), obj1.toString());
            } else {
                ContentValues c = new ContentValues();
                c.put(DatabaseHelper.JSONDATA, obj.toString());
                c.put(DatabaseHelper.ANSWERDATA, obj1.toString());
                c.put(DatabaseHelper.LANGUAGE, exam.getLanguages());
                c.put(DatabaseHelper.EXAMID, exam.getExamid());
                c.put(DatabaseHelper.SYNC, 0);
                c.put(DatabaseHelper.SAVEDTIME, DateUtils.getSqliteTime());
                DatabaseHelper.getInstance(AnswersheetActivity.this).saveanswerdata(c);
                Toast.makeText(getApplicationContext(), "Data saved offline Thank You", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        try {
            MenuInflater menuInflater = getMenuInflater();
            menuInflater.inflate(R.menu.viewprogressreport, menu);

            Drawable icon_color_change = menu.getItem(0).getIcon(); // change 0 with 1,2 ...
            icon_color_change.mutate();
            icon_color_change.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_IN);

            MenuItem logout_button = menu.findItem(R.id.viewprogressreport);
            logout_button.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    Intent progreport = new Intent(AnswersheetActivity.this, ProgressReportActivity.class);
                    progreport.putExtra("progressreport", exam);
                 //   progreport.putExtra("flag", 1);
                    progreport.putExtra("flag", 0);
                    startActivity(progreport);
                    finish();
                    return false;
                }
            });
        } catch (Exception e) {
            reporterror("Anserfrag", e.toString());
        }

        return super.onCreateOptionsMenu(menu);
    }


/*    private void savedataonbackorsubmit() {
        try {
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


            DatabaseHelper.getInstance(AnswersheetActivity.this).saveexaminationdetails(c1, exam.getExamid());


            *//*Gson gson = new Gson();
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
                DatabaseHelper.getInstance(AnswersheetActivity.this).updateqdata(c, exam.getExamid(), exam.getLanguages());*//*

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



        } catch (Exception e) {
            reporterror(tag, e.toString());
            e.printStackTrace();
        }
    }*/
}
