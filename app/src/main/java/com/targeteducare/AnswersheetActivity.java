package com.targeteducare;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
    public static  Exam exam;
    int notanswered = 0;
    int answered = 0;
    int review = 0;
    LinearLayout layout1;
    TextView title;
    ImageView imgright;
    DrawerLayout drawer;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_answersheet);
        //Log.e("in answer ", "In answer");
        qdata = new ArrayList<>();
        registerreceiver();
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

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        sectionedExpandableLayoutHelper = new SectionedExpandableLayoutHelper(this, mRecyclerView, this, 6,true);

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


        JSONArray array = DatabaseHelper.getInstance(AnswersheetActivity.this).getqdata1(exam.getExamid(), exam.getLanguages());
        if (array.length() > 0) {
            try {
                Log.e("via db ", "via db" + array.toString());
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
                obj.put("ImageRelPath", GlobalValues.IP);
                JSONObject mainobj = new JSONObject();
                mainobj.put("FilterParameter", obj.toString());
                ConnectionManager.getInstance(AnswersheetActivity.this).getquestion(mainobj.toString(), exam.getExamid(), exam.getLanguages());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void notifyqdatachanged() {
        if (notanswered > 0)
            notanswered = notanswered - 1;
        answered = answered + 1;

        txt10.setText("Total Attempted Questions: " + answered);
        txt11.setText("Total Not Attempted Questions: " + notanswered);
        txt12.setText("Total Review Question: " + review);
        sectionedExpandableLayoutHelper.notifyDataSetChanged();
    }

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
            } else if (accesscode == Connection.SAVE_ANSWER.ordinal()) {
                try {
                    // JSONObject object = new JSONObject(GlobalValues.TEMP_STR).getJSONObject("root").getJSONObject("subroot");
                    //if (object.has("id")) {
                    Toast.makeText(getApplicationContext(), "Thank You", Toast.LENGTH_LONG).show();

                    // }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void parsedata(JSONObject obj) {
        try {
            Log.e("objparse ", "obj " + obj.toString());
            HashMap<Item, ArrayList<Item>> mSectionMap = new HashMap<Item, ArrayList<Item>>();
            ArrayList<Item> items = new ArrayList<>();
            sectionedExpandableLayoutHelper.removeallSection();
            Log.e("data ", "data " + obj.getJSONObject("subjects"));
            JSONObject subobj = obj.getJSONObject("subjects").optJSONObject("questions");

            if (subobj != null) {

                Log.e("sub ", "sub " + subobj.toString());
                Item item = new Item();
                item.setName(subobj.getJSONObject("subject").getString("subjectname"));
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
                        item.setName(subobj.getJSONObject("subject").getString("subjectname"));
                        item.setNoofques(subobj.getString("noofques"));
                        item.setSubjectid(subobj.getInt("subjectid"));
                        items.add(item);
                        ArrayList<Item> arrayList = new ArrayList<>();
                        mSectionMap.put(item, arrayList);
                    }
            }

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
                                           /* qdata.get(i).setIswrongAnswer(true);
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
            txt1.setText(Html.fromHtml("<b>Obtained Marks </b>: (" + obtainedmarks + ")  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <b>Negative Marks</b> : (" + negativemarks + ")&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>Final Marks</b> : (" + totalmarks + ")" + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <b> Total Question</b> :" + totalquestions + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b> Attempted Questions</b> : (" + (totalquestions - totalnotanswered) + ")&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <b> Not Attempted Questions</b> : (" + totalnotanswered + ")&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <b>Right Questions</b> : (" + totalright + ")&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <b>Wrong Questions</b> : (" + totalwrong + ")"));
            setTitle(exam.getExamname() + " ( " + totalmarks + " Marks )");
           /* txt7.setText("Questions: " + qdata.size());
            notanswered=qdata.size();
            txt10.setText("Total Attempted Questions: "+answered);
            txt11.setText("Total Not Attempted Questions: "+notanswered);
            txt12.setText("Total Review Question: "+review);*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
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
}
