package com.targeteducare;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.targeteducare.Adapter.CoursesAdapter;
import com.targeteducare.Adapter.MockTestAdapter;
import com.targeteducare.Adapter.ViewPagerAdapter;
import com.targeteducare.Classes.Course;
import com.targeteducare.Classes.Exam;
import com.targeteducare.database.DatabaseHelper;
import org.json.JSONArray;
import org.json.JSONObject;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;

public class MockTestActivity extends Activitycommon {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Exam> mockTestModels;
    private MockTestAdapter mockTestAdapter;
    Bundle b1;
    ViewPagerAdapter viewpageradapter;
    TextView tv_test, tv_notest, tv_toolbar;
    String type = "";
    String lang = "";
    Spinner sp;
    ArrayList<Course> coursedata = new ArrayList<>();
    ArrayAdapter<Course> spinnerArrayAdapter;
    TabLayout tabLayout;
    private ViewPager viewPager;
    String tag="";
    Exam exam = new Exam();

    public interface DataFromActivityToFragment {
        void sendData(ArrayList<Exam> data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            screenshot_capture_permission();
            super.onCreate(savedInstanceState);
            loadLocale();
            setContentView(R.layout.activity_mock_test);

            tag=this.getClass().getSimpleName();
            //setmaterialDesign();
            //back();
           // Log.e("Got into ", "mock test activity");
            toolbar = findViewById(R.id.toolbar);
            tv_toolbar = findViewById(R.id.collapsing_toolbar_textview);
            tv_toolbar.setText(getResources().getString(R.string.toolbar_hey) + ", " + GlobalValues.student.getName() + "\n" + getResources().getString(R.string.toolbar_text));
            toolbar.setTitleTextColor(getResources().getColor(R.color.White));
            setSupportActionBar(toolbar);
            setTitle(getResources().getString(R.string.mock_test));
            toolbar.setPadding(30, 10, 10, 10);
            registerreceiver();

            lang = getSharedPreferences("Settings", Activity.MODE_PRIVATE).getString("Current Language", "");
            sp = (Spinner) findViewById(R.id.spinner_1);

            spinnerArrayAdapter = new ArrayAdapter<Course>(this, android.R.layout.simple_spinner_item, coursedata); //selected item will look like a spinner set from XML
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sp.setAdapter(spinnerArrayAdapter);

            sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    Log.e("item selected ", "item selected ");
                    parseexamdata(coursedata.get(i).getCourse_id());
                    //  DatabaseHelper.getInstance(MockTestActivity.this).getexaminationdatav1(type,coursedata.get(i).getCourse_id());
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            toolbar.setLogo(R.mipmap.ic_launcher);
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

            recyclerView = findViewById(R.id.recyclerviewformocktest);
            layoutManager = new LinearLayoutManager(MockTestActivity.this);
            recyclerView.setLayoutManager(layoutManager);
            tv_test = findViewById(R.id.typeoftestname);
            tv_notest = findViewById(R.id.notestavailable);
            mockTestModels = new ArrayList<Exam>();
            viewPager = findViewById(R.id.viewpager);
            viewpageradapter = new ViewPagerAdapter(MockTestActivity.this, getSupportFragmentManager(), GlobalValues.mdataset_New, GlobalValues.mdataset_Attempted, GlobalValues.mdataset_Missed,"Mock Test");
            viewPager.setAdapter(viewpageradapter);
            tabLayout.setupWithViewPager(viewPager);

            lang = getSharedPreferences("Settings", Activity.MODE_PRIVATE).getString("Current Language", "");
            tv_notest = findViewById(R.id.notestavailable);
       /* for(int i =0; i< PracticeDemo.topicname.length;i++){
            Exam practicedata = new Exam();
            mockTestModels.add(practicedata);
        }*/
            b1 = getIntent().getExtras();
            type = b1.getString("testtype");
            if (lang.equals("mr")) {
                //exam.setExam_type(type);
                exam.setLanguages("marathi");
            } else {
                //exam.setExam_type(type);
                exam.setLanguages("ENGLISH");
            }
            tv_test.setText(type);
            try {
                //if (InternetUtils.getInstance(MockTestActivity.this).available()) {
                JSONObject obj = new JSONObject();
                //  obj.put("studentid", GlobalValues.studentProfile.getId());
                obj.put("studentid", GlobalValues.student.getId());
                //   obj.put("type", "current");
                //obj.put("test_type","Mock Test");
                obj.put("test_type", type);
                Calendar cal = Calendar.getInstance();
                obj.put("year", cal.get(Calendar.YEAR));
                JSONObject mainobj = new JSONObject();
                mainobj.put("FilterParameter", obj.toString());
                genloading(getResources().getString(R.string.loading));
                if(dialog!=null)
                dialog.setCancelable(true);
                ConnectionManager.getInstance(MockTestActivity.this).getexam(mainobj.toString(),type);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            reporterror(tag,e.toString());
            e.printStackTrace();
        }
    }

    public void parseexamdata(int courseid) {
        try {
            //Log.e("in parsedata ", "parsedata " + courseid);
            mockTestModels.clear();
            Log.e("type of test ", type);
            JSONArray array1 = DatabaseHelper.getInstance(MockTestActivity.this).getexaminationdatav1(type, courseid);
            for (int i = 0; i < array1.length(); i++) {
                // JSONObject obj = new JSONObject(array1.getJSONObject(i).getString(DatabaseHelper.JSONDATA));
                try {
                    JSONObject obj = array1.getJSONObject(i);
                    //Log.e("via array1", "via array1 " + obj.toString());
                    JSONObject obj1 = new JSONObject(obj.getString(DatabaseHelper.JSONDATA));
                   // Log.e("via array2", "via array2 " + obj.getString(DatabaseHelper.JSONDATA));
                    Gson gson = new Gson();
                    Type type = new TypeToken<Exam>() {
                    }.getType();
                    Exam exam = gson.fromJson(obj1.toString(), type);
                    mockTestModels.add(exam);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            for (int i = 0; i < mockTestModels.size(); i++) {
                if (lang.equals("mr")) {
                    mockTestModels.get(i).setLanguages("marathi");
                } else {
                    mockTestModels.get(i).setLanguages("ENGLISH");
                }

                int count = DatabaseHelper.getInstance(MockTestActivity.this).getqdata1count(mockTestModels.get(i).getExamid(), mockTestModels.get(i).getLanguages());
                if (count > 0) {
                    Log.e("countif ", "count " + count);
                    mockTestModels.get(i).setIsexamgiven(true);
                } else {
                    Log.e("countelse ", "count " + count);
                    mockTestModels.get(i).setIsexamgiven(false);
                }

                int synccount = DatabaseHelper.getInstance(MockTestActivity.this).getasyncdatacount(mockTestModels.get(i).getExamid(), mockTestModels.get(i).getLanguages());
                if (synccount > 0) {
                    Log.e("sync ", "sync " + synccount);
                    mockTestModels.get(i).setIssync(true);
                } else {
                    Log.e("sync ", "sync " + synccount);
                    mockTestModels.get(i).setIssync(false);
                }

                int qcount = DatabaseHelper.getInstance(MockTestActivity.this).getqdatacount(mockTestModels.get(i).getExamid(), mockTestModels.get(i).getLanguages());
                if (qcount > 0) {
                    Log.e("attem ", "sync " + synccount);
                    mockTestModels.get(i).setIsqdownloaded(true);
                } else {
                    Log.e("sync ", "sync " + synccount);
                    mockTestModels.get(i).setIsqdownloaded(false);
                }

                JSONArray array = DatabaseHelper.getInstance(MockTestActivity.this).getexamdetails(mockTestModels.get(i).getExamid(), mockTestModels.get(i).getExam_type());
                if (array.length() > 0) {
                    JSONObject obj = array.getJSONObject(0);
                    mockTestModels.get(i).setSkipp(obj.getInt(DatabaseHelper.SKIPP));
                    mockTestModels.get(i).setAnswered(obj.getInt(DatabaseHelper.ANSWERED));
                    mockTestModels.get(i).setWrong(obj.getInt(DatabaseHelper.WRONG));
                    mockTestModels.get(i).setCorrect(obj.getInt(DatabaseHelper.CORRECT));
                    mockTestModels.get(i).setProgress(obj.getDouble(DatabaseHelper.PROGRESS));
                    mockTestModels.get(i).setTimetaken(obj.getLong(DatabaseHelper.TIMETAKEN));
                    mockTestModels.get(i).setSpeed(obj.getDouble(DatabaseHelper.SPEED));
                }
            }
            Log.e("updated data ", "updated data " + mockTestModels.size());
            mockTestAdapter.notifyDataSetChanged();
            updatefragmentdata();


       /* viewPager = findViewById(R.id.viewpager);
        viewpageradapter = new ViewPagerAdapter(MockTestActivity.this, getSupportFragmentManager(), GlobalValues.mdataset_New, GlobalValues.mdataset_Attempted, GlobalValues.mdataset_Missed);
        viewPager.setAdapter(viewpageradapter);
        tabLayout.setupWithViewPager(viewPager);*/
        if (coursedata.size() == 0) {
            JSONArray array = DatabaseHelper.getInstance(MockTestActivity.this).getcoursedata();
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<Course>>() {
            }.getType();
            ArrayList<Course> courses1 = gson.fromJson(array.toString(), type);
            coursedata.add(new Course(-1, "Please Select Course",  "कृपया कोर्स निवडा"));
            coursedata.addAll(courses1);
            Log.e("coursedata ", "coursedata " + coursedata.size() + " " + array.toString());
            CoursesAdapter spadapter = new CoursesAdapter(MockTestActivity.this, coursedata, lang);

            sp.setAdapter(spadapter);
        }
        } catch (Exception e) {
            reporterror("missedfragment",e.toString());
            e.printStackTrace();
        }
    }

    public void gotoAction(Exam exam) {
        try{
        Intent iprogrprt = new Intent(MockTestActivity.this, ProgressReportActivity.class);
        iprogrprt.putExtra("progressreport", exam);
        iprogrprt.putExtra("flag", 0);
        startActivity(iprogrprt);
        } catch (Exception e) {
            reporterror("missedfragment",e.toString());
            e.printStackTrace();
        }
    }

    /*public void gotopracticetest(int i) {
        Intent icheckprogress = new Intent(MockTestActivity.this,ProgressReportActivity.class);
        startActivity(icheckprogress);
    }

    public int incrementprogress(int i) {
        return progress++;
    }*/


    @Override
    protected void onResponsed(int statuscode, int accesscode, String data) {
        super.onResponsed(statuscode, accesscode, data);
     //   Log.e("onres ", "res " + GlobalValues.TEMP_STR);
        if (statuscode == Constants.STATUS_OK) {
            if (accesscode == Connection.GET_EXAMS.ordinal()) {
                try {
               /*     JSONObject obj1 = new JSONObject(GlobalValues.TEMP_STR).getJSONObject("root");
                    JSONArray array = new JSONArray();
                    if (obj1.optJSONArray("subroot") != null) {
                        array = obj1.getJSONArray("subroot");
                    } else if (obj1.optJSONObject("subroot") != null) {

                        if(obj1.getJSONObject("subroot").has("error"))
                        {
                            updatefragmentdata();
                            return;
                        }else{
                            array.put(obj1.getJSONObject("subroot"));
                        }
                    }

                    ContentValues c = new ContentValues();
                    c.put(DatabaseHelper.JSONDATA, array.toString());
                    c.put(DatabaseHelper.SAVEDTIME, DateUtils.getSqliteTime());
                    c.put(DatabaseHelper.TYPE, type);
                    DatabaseHelper.getInstance(MockTestActivity.this).saveexaminationdata(c, type);*/
                    mockTestModels.clear();
                   /* Log.e("JSONArray else ", "else ");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject obj = array.getJSONObject(i);
                        ContentValues c1 = new ContentValues();
                        c1.put(DatabaseHelper.SAVEDTIME, DateUtils.getSqliteTime());
                        c1.put(DatabaseHelper.TYPE, type);
                        c1.put(DatabaseHelper.JSONDATA, obj.toString());
                        c1.put(DatabaseHelper.COURSENAME, obj.getString("coursename"));
                        c1.put(DatabaseHelper.COURSEID, obj.getString("courseid"));
                        c1.put(DatabaseHelper.COURSENAMEINMARATHI, obj.getString("coursename_inmarathi"));
                        c1.put(DatabaseHelper.EXAMID, obj.getInt("examid"));
                        DatabaseHelper.getInstance(MockTestActivity.this).saveexaminationdatav1(c1, obj.getInt("examid"), obj.getInt("courseid"));
                    }*/
                    parseexamdata(-1);
                } catch (Exception e) {
                    e.printStackTrace();
                }

          /*      try {
                    JSONObject obj1 = new JSONObject(GlobalValues.TEMP_STR).getJSONObject("root");
                    JSONArray array = new JSONArray();
                    if (obj1.optJSONArray("subroot") != null) {
                        array = obj1.getJSONArray("subroot");
                    }else if(obj1.optJSONArray("subroot") != null){
                        array.put(obj1.getJSONObject("subroot"));
                    }

                    ContentValues c = new ContentValues();
                    c.put(DatabaseHelper.JSONDATA, array.toString());
                    c.put(DatabaseHelper.SAVEDTIME, DateUtils.getSqliteTime());
                    c.put(DatabaseHelper.TYPE, type);
                    DatabaseHelper.getInstance(MockTestActivity.this).saveexaminationdata(c, type);

                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonObject = array.getJSONObject(i);
                        String cat_name = jsonObject.optString("categoryname");
                        String examid = jsonObject.optString("examid");
                        if (lang.equals("mr")) {
                            examname = jsonObject.optString("Name_InMarathi");
                        } else {
                            examname = jsonObject.optString("examname");
                        }

                        //Log.e("exam name is ", examname);
                        test.add(i,examname);
                        String marks = jsonObject.optString("marks");
                        String neagativemarks = jsonObject.optString("neagativemarks");
                        String startdate = jsonObject.optString("startdate");
                        String enddate = jsonObject.optString("enddate");
                        String duration = jsonObject.optString("duration");
                        String durationinMin = jsonObject.optString("durationinMin");
                        String InstantExamResult = jsonObject.optString("InstantExamResult");
                        String InstantExamResultWithAns = jsonObject.optString("InstantExamResultWithAns");
                        String isDownload = jsonObject.optString("isDownload");
                        String isshowexam = jsonObject.optString("isshowexam");
                        String isshowanswersheet = jsonObject.optString("isshowanswersheet");
                        String resultdate = jsonObject.optString("resultdate");
                        String examresulttime = jsonObject.optString("examresulttime");
                        //Log.e("parsing done ", duration);
                    }
                    parseexamdata(-2);
                } catch (Exception e) {
                    e.printStackTrace();
                }*/
            }else if(accesscode==Connection.NoExamAvailable.ordinal()){
                try {
                    if (!((Activity) context).isFinishing()) {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.nodata), Toast.LENGTH_LONG).show();
                    } else {
                        Log.e("no activity", "no activity ");
                    }
                 /*   mockTestModels.clear();
                    parseexamdata(-1);*/
                } catch (Exception e) {
                    reporterror(tag,e.toString());
                    e.printStackTrace();
                }
            } else if (accesscode == Connection.GET_EXAMSEXCEPTION.ordinal()) {
                try {
                   // JSONArray array = DatabaseHelper.getInstance(MockTestActivity.this).getexaminationdata(type);
                   // Log.e("data ", array.toString());
                   // if (array.length() > 0) {
                        parseexamdata(-1);
                   // }
                    Log.e("GET_EXAMSEXCEPTION ","GET_EXAMSEXCEPTION ");
                    if (!((Activity) context).isFinishing()) {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.Connectiontimeout), Toast.LENGTH_LONG).show();
                    } else {
                        Log.e("no activity", "no activity ");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (accesscode == Connection.Dataupdated.ordinal()) {
                Log.e("data ", "data " + data);

                if (data.length() > 0) {
                    for (int i = 0; i < mockTestModels.size(); i++) {
                        try {
                            JSONArray array = DatabaseHelper.getInstance(MockTestActivity.this).getexamdetails(mockTestModels.get(i).getExamid(), mockTestModels.get(i).getExam_type());
                            if (array.length() > 0) {
                                JSONObject obj = array.getJSONObject(array.length() - 1);
                                Log.e("data updated111", "data updated111 " + obj.toString());
                                mockTestModels.get(i).setSkipp(obj.getInt(DatabaseHelper.SKIPP));
                                mockTestModels.get(i).setAnswered(obj.getInt(DatabaseHelper.ANSWERED));
                                mockTestModels.get(i).setWrong(obj.getInt(DatabaseHelper.WRONG));
                                mockTestModels.get(i).setCorrect(obj.getInt(DatabaseHelper.CORRECT));
                                mockTestModels.get(i).setProgress(obj.getDouble(DatabaseHelper.PROGRESS));
                                mockTestModels.get(i).setTimetaken(obj.getLong(DatabaseHelper.TIMETAKEN));
                                mockTestModels.get(i).setSpeed(obj.getDouble(DatabaseHelper.SPEED));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        if (mockTestModels.get(i).getExamid() == Integer.parseInt(data)) {
                            mockTestModels.get(i).setIsexamgiven(true);
                            mockTestAdapter.notifyDataSetChanged();
                            break;
                        }
                        int synccount = DatabaseHelper.getInstance(MockTestActivity.this).getasyncdatacount(mockTestModels.get(i).getExamid(), mockTestModels.get(i).getLanguages());
                        if (synccount > 0) {
                            Log.e("synccount ", "synccount " + synccount);
                            mockTestModels.get(i).setIssync(true);
                        } else {
                            Log.e("synccount ", "synccount " + synccount);
                            mockTestModels.get(i).setIssync(false);
                        }
                    }
                } else {
                    for (int i = 0; i < mockTestModels.size(); i++) {
                        try {
                            JSONArray array = DatabaseHelper.getInstance(MockTestActivity.this).getexamdetails(mockTestModels.get(i).getExamid(), mockTestModels.get(i).getExam_type());
                            if (array.length() > 0) {
                                JSONObject obj = array.getJSONObject(array.length() - 1);
                                mockTestModels.get(i).setSkipp(obj.getInt(DatabaseHelper.SKIPP));
                                mockTestModels.get(i).setAnswered(obj.getInt(DatabaseHelper.ANSWERED));
                                mockTestModels.get(i).setWrong(obj.getInt(DatabaseHelper.WRONG));
                                mockTestModels.get(i).setCorrect(obj.getInt(DatabaseHelper.CORRECT));
                                mockTestModels.get(i).setProgress(obj.getDouble(DatabaseHelper.PROGRESS));
                                mockTestModels.get(i).setTimetaken(obj.getLong(DatabaseHelper.TIMETAKEN));
                                mockTestModels.get(i).setSpeed(obj.getDouble(DatabaseHelper.SPEED));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        int count = DatabaseHelper.getInstance(MockTestActivity.this).getqdata1count(mockTestModels.get(i).getExamid(), mockTestModels.get(i).getLanguages());
                        if (count > 0) {
                            mockTestModels.get(i).setIsexamgiven(true);
                        } else {
                            mockTestModels.get(i).setIsexamgiven(false);
                        }

                        int synccount = DatabaseHelper.getInstance(MockTestActivity.this).getasyncdatacount(mockTestModels.get(i).getExamid(), mockTestModels.get(i).getLanguages());
                        if (synccount > 0) {
                            Log.e("synccount ", "synccount " + synccount);
                            mockTestModels.get(i).setIssync(true);
                        } else {
                            Log.e("synccount ", "synccount " + synccount);
                            mockTestModels.get(i).setIssync(false);
                        }
                    }
                }

                updatefragmentdata();
            }
        }
    }

  /*  public void parseexamdata(String s) {
        try {
           //Log.e("resdata ", "resdata " + s);
            Object json = new JSONTokener(s).nextValue();
            mockTestModels.clear();
            if (json instanceof JSONObject) {
                Log.e("JSONObject ", "else ");
                JSONObject obj = new JSONObject(s);
                Gson gson = new Gson();
                Type type = new TypeToken<Exam>() {
                }.getType();
                Exam exam = gson.fromJson(obj.toString(), type);
                mockTestModels.add(exam);
                mockTestAdapter.notifyDataSetChanged();
            } else if (json instanceof JSONArray) {
                Log.e("JSONArray else ", "else ");
                JSONArray array1 = new JSONArray(s);
                Gson gson = new Gson();
                Type type = new TypeToken<ArrayList<Exam>>() {
                }.getType();

                ArrayList<Exam> dataset = gson.fromJson(array1.toString(), type);
                if (dataset != null) {
                    if (dataset.size() > 0) {
                        tv_notest.setVisibility(View.GONE);
                        mockTestModels.addAll(dataset);
                        //mockTestModel_NewMissedAttempted.addAll(dataset);
                        for (int i = 0; i < test.size(); i++) {
                            Log.e("examname to retrieve ", "" + test.get(i));
                            mockTestModels.get(i).setExamname(test.get(i));
                        }

                        Log.e("mocktestdata size ", "mocktestdata size " + mockTestModels.size());
                    } else {
                        tv_notest.setVisibility(View.VISIBLE);
                        tv_notest.setText("Sorry, we have no TESTS available at the moment! \n\nWe'll update you soon. Keep checking for updates.");
                    }
                    mockTestAdapter.notifyDataSetChanged();
                }
            } else {
                Log.e("else ", "else ");
            }

            for (int i = 0; i < mockTestModels.size(); i++) {
                if (lang.equals("mr")) {
                    mockTestModels.get(i).setLanguages("marathi");
                } else {
                    mockTestModels.get(i).setLanguages("ENGLISH");
                }

                int count = DatabaseHelper.getInstance(MockTestActivity.this).getqdata1count(mockTestModels.get(i).getExamid(), mockTestModels.get(i).getLanguages());
                if (count > 0) {
                    Log.e("countif ", "count " + count);
                    mockTestModels.get(i).setIsexamgiven(true);
                } else {
                    Log.e("countelse ", "count " + count);
                    mockTestModels.get(i).setIsexamgiven(false);
                }

                int synccount = DatabaseHelper.getInstance(MockTestActivity.this).getasyncdatacount(mockTestModels.get(i).getExamid(), mockTestModels.get(i).getLanguages());
                if (synccount > 0) {
                    Log.e("sync ", "sync " + synccount);
                    mockTestModels.get(i).setIssync(true);
                } else {
                    Log.e("sync ", "sync " + synccount);
                    mockTestModels.get(i).setIssync(false);
                }

                int qcount = DatabaseHelper.getInstance(MockTestActivity.this).getqdatacount(mockTestModels.get(i).getExamid(), mockTestModels.get(i).getLanguages());
                if (qcount > 0) {
                    Log.e("attem ", "sync " + synccount);
                    mockTestModels.get(i).setIsqdownloaded(true);
                } else {
                    Log.e("sync ", "sync " + synccount);
                    mockTestModels.get(i).setIsqdownloaded(false);
                }

                JSONArray array = DatabaseHelper.getInstance(MockTestActivity.this).getexamdetails(mockTestModels.get(i).getExamid(), mockTestModels.get(i).getExam_type());
                if (array.length() > 0) {
                    JSONObject obj = array.getJSONObject(0);
                    mockTestModels.get(i).setSkipp(obj.getInt(DatabaseHelper.SKIPP));
                    mockTestModels.get(i).setAnswered(obj.getInt(DatabaseHelper.ANSWERED));
                    mockTestModels.get(i).setWrong(obj.getInt(DatabaseHelper.WRONG));
                    mockTestModels.get(i).setCorrect(obj.getInt(DatabaseHelper.CORRECT));
                    mockTestModels.get(i).setProgress(obj.getDouble(DatabaseHelper.PROGRESS));
                    mockTestModels.get(i).setTimetaken(obj.getLong(DatabaseHelper.TIMETAKEN));
                    mockTestModels.get(i).setSpeed(obj.getDouble(DatabaseHelper.SPEED));
                }
                if (((int) Math.round(mockTestModels.get(i).getProgress())) == 0) {
                    mockTestModel_New.add(mockTestModels.get(i));
                }
                else if (((int) Math.round(mockTestModels.get(i).getProgress())) > 0) {
                    mockTestModel_Attempted.add(mockTestModels.get(i));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Log.e("mock data is ", mockTestModel_NewMissedAttempted.size() + "");

        Log.e("size of attemptedData", mockTestModel_Attempted.size()+"");
        Log.e("size of newData", mockTestModel_New.size()+"");
        Log.e("size of missedData", mockTestModel_Missed.size()+"");
        viewPager = findViewById(R.id.viewpager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(MockTestActivity.this, getSupportFragmentManager(), mockTestModel_New,mockTestModel_Attempted,mockTestModel_Missed);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }*/

    @Override
    protected void onDestroy() {
        try{
        GlobalValues.mdataset_New.clear();
        GlobalValues.mdataset_Attempted.clear();
        GlobalValues.mdataset_Missed.clear();
            GlobalValues.selectedtesttype="";
        } catch (Exception e) {
            reporterror("missedfragment",e.toString());
            e.printStackTrace();
        }
        super.onDestroy();
    }

    public void updatefragmentdata() {
        try{
        GlobalValues.mdataset_New.clear();
        GlobalValues.mdataset_Attempted.clear();
        GlobalValues.mdataset_Missed.clear();

        for (int i = 0; i < mockTestModels.size(); i++) {
            if (((int) Math.round(mockTestModels.get(i).getProgress())) > 0) {
                Log.e("updated in attempted", "in attempted");
                mockTestModels.get(i).setExamstatus("Attempted");

                if (GlobalValues.mdataset_Attempted.size() > 0) {
                    if (!GlobalValues.mdataset_Attempted.get(GlobalValues.mdataset_Attempted.size() - 1).getCoursename().equalsIgnoreCase(mockTestModels.get(i).getCoursename())) {
                        Exam exam = new Exam();
                        exam.setIsheader(true);
                        exam.setCoursename(mockTestModels.get(i).getCoursename());
                        exam.setCoursename_inmarathi(mockTestModels.get(i).getCoursename_inmarathi());
                        GlobalValues.mdataset_Attempted.add(exam);
                    }
                } else if (GlobalValues.mdataset_Attempted.size() == 0) {
                    Exam exam = new Exam();
                    exam.setIsheader(true);
                    exam.setCoursename(mockTestModels.get(i).getCoursename());
                    exam.setCoursename_inmarathi(mockTestModels.get(i).getCoursename_inmarathi());
                    GlobalValues.mdataset_Attempted.add(exam);
                }

               // mockTestModels.get(i).setExamstatus("Attempted");
                GlobalValues.mdataset_Attempted.add(mockTestModels.get(i));
            }else
            if (mockTestModels.get(i).getExamstatus().equalsIgnoreCase("Missed")) {
                if (GlobalValues.mdataset_Missed.size() > 0) {
                    if (!GlobalValues.mdataset_Missed.get(GlobalValues.mdataset_Missed.size() - 1).getCoursename().equalsIgnoreCase(mockTestModels.get(i).getCoursename())) {
                        Exam exam = new Exam();
                        exam.setIsheader(true);
                        exam.setCoursename(mockTestModels.get(i).getCoursename());
                        exam.setCoursename_inmarathi(mockTestModels.get(i).getCoursename_inmarathi());
                        GlobalValues.mdataset_Missed.add(exam);
                    }
                } else if (GlobalValues.mdataset_Missed.size() == 0) {
                    Exam exam = new Exam();
                    exam.setIsheader(true);
                    exam.setCoursename(mockTestModels.get(i).getCoursename());
                    exam.setCoursename_inmarathi(mockTestModels.get(i).getCoursename_inmarathi());
                    GlobalValues.mdataset_Missed.add(exam);
                }
                GlobalValues.mdataset_Missed.add(mockTestModels.get(i));
            } else if (mockTestModels.get(i).getExamstatus().equalsIgnoreCase("Attempted")) {
                if (GlobalValues.mdataset_Attempted.size() > 0) {
                    if (!GlobalValues.mdataset_Attempted.get(GlobalValues.mdataset_Attempted.size() - 1).getCoursename().equalsIgnoreCase(mockTestModels.get(i).getCoursename())) {
                        Exam exam = new Exam();
                        exam.setIsheader(true);
                        exam.setCoursename(mockTestModels.get(i).getCoursename());
                        exam.setCoursename_inmarathi(mockTestModels.get(i).getCoursename_inmarathi());
                        GlobalValues.mdataset_Attempted.add(exam);
                    }
                } else if (GlobalValues.mdataset_Attempted.size() == 0) {
                    Exam exam = new Exam();
                    exam.setIsheader(true);
                    exam.setCoursename(mockTestModels.get(i).getCoursename());
                    exam.setCoursename_inmarathi(mockTestModels.get(i).getCoursename_inmarathi());
                    GlobalValues.mdataset_Attempted.add(exam);
                }
                GlobalValues.mdataset_Attempted.add(mockTestModels.get(i));
            } else {
                //Log.e("updated ", "updated " + (int) Math.round(mockTestModels.get(i).getProgress()) + " " + mockTestModels.get(i).getExamname());
                if (((int) Math.round(mockTestModels.get(i).getProgress())) == 0) {
                  //  Log.e("updated in new", "in new");
                    if (GlobalValues.mdataset_New.size() > 0) {
                        if (!GlobalValues.mdataset_New.get(GlobalValues.mdataset_New.size() - 1).getCoursename().equalsIgnoreCase(mockTestModels.get(i).getCoursename())) {
                            Exam exam = new Exam();
                            exam.setIsheader(true);
                            exam.setCoursename(mockTestModels.get(i).getCoursename());
                            exam.setCoursename_inmarathi(mockTestModels.get(i).getCoursename_inmarathi());
                            GlobalValues.mdataset_New.add(exam);
                        }
                    } else if (GlobalValues.mdataset_New.size() == 0) {
                        Exam exam = new Exam();
                        exam.setIsheader(true);
                        exam.setCoursename(mockTestModels.get(i).getCoursename());
                        exam.setCoursename_inmarathi(mockTestModels.get(i).getCoursename_inmarathi());
                        GlobalValues.mdataset_New.add(exam);
                    }
                    GlobalValues.mdataset_New.add(mockTestModels.get(i));
                }
            }
        }
            publishBroadcast(200, Connection.Syncdata.ordinal());
      //  Log.e("updated in ", "in attempted" + GlobalValues.mdataset_New.size() + " " + GlobalValues.mdataset_Attempted.size());
        final Handler handler = new Handler();

       /* final Runnable r = new Runnable() {
            public void run() {
                if (dataFromActivityToFragment != null)
                    dataFromActivityToFragment.sendData(GlobalValues.mdataset_New);
                else Log.e("updated ", "updated null");

                if (dataFromActivityToFragmentmissed != null)
                    dataFromActivityToFragmentmissed.sendData(GlobalValues.mdataset_Missed);
                else Log.e("updated ", "updated null2");

                if (dataFromActivityToFragmentattempted != null)
                    dataFromActivityToFragmentattempted.sendData(GlobalValues.mdataset_Attempted);
                else Log.e("updated ", "updated null1");
            }
        };

        handler.postDelayed(r, 0);*/

            try {
                Intent intent = new Intent("updateddata");
                intent.putExtra(Constants.BROADCAST_RESPONSE_CODE, 200);
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            reporterror("missedfragment",e.toString());
            e.printStackTrace();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        try {
            mockTestAdapter = new MockTestAdapter(MockTestActivity.this, mockTestModels, lang,type);
            for (int i = 0; i < mockTestModels.size(); i++) {
                JSONArray array = DatabaseHelper.getInstance(MockTestActivity.this).getexamdetails(mockTestModels.get(i).getExamid(), mockTestModels.get(i).getExam_type());
                if (array.length() > 0) {
                    JSONObject obj = array.getJSONObject(0);
                    mockTestModels.get(i).setSkipp(obj.getInt(DatabaseHelper.SKIPP));
                    mockTestModels.get(i).setAnswered(obj.getInt(DatabaseHelper.ANSWERED));
                    mockTestModels.get(i).setWrong(obj.getInt(DatabaseHelper.WRONG));
                    mockTestModels.get(i).setCorrect(obj.getInt(DatabaseHelper.CORRECT));
                    mockTestModels.get(i).setProgress(obj.getDouble(DatabaseHelper.PROGRESS));
                    mockTestModels.get(i).setTimetaken(obj.getLong(DatabaseHelper.TIMETAKEN));
                    mockTestModels.get(i).setSpeed(obj.getDouble(DatabaseHelper.SPEED));

                    //practiceTestModels.get(i).setTotal_questions(obj.getString(EBookDatabaseHelper.QUESTION));

                    //practiceTestModels.get(i).setTotal_questions(obj.getString(PracticeDatabaseHelper.QUESTION));

                }
            }
            mockTestAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            reporterror("missedfragment",e.toString());
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
}
