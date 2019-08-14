package com.targeteducare;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
import com.targeteducare.Adapter.PracticeTestAdapter;
import com.targeteducare.Adapter.ViewPagerAdapter;
import com.targeteducare.Classes.Course;
import com.targeteducare.Classes.Exam;
import com.targeteducare.database.DatabaseHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;

public class PracticeTestActivity extends Activitycommon {
    Toolbar toolbar;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Exam> practiceTestModels;
    private PracticeTestAdapter practiceTestAdapter;
    Bundle b1;
    String type = "";
    TextView tv_test, tv_notest, tv_toolbar;
    String lang = "";
    String examname = "";
    TabLayout tabLayout;
    private ViewPager viewPager;
    ArrayList<Course> coursedata = new ArrayList<>();
    /* PracticeTestActivity.DataFromActivityToFragment dataFromActivityToFragment;
     PracticeTestActivity.DataFromActivityToFragment dataFromActivityToFragmentattempted;
     PracticeTestActivity.DataFromActivityToFragment dataFromActivityToFragmentmissed;*/
    Exam exam = new Exam();
    ViewPagerAdapter viewpageradapter;
    private ArrayList<String> test = new ArrayList<>();
    Spinner sp;
    ArrayAdapter<Course> spinnerArrayAdapter;

    public interface DataFromActivityToFragment {
        void sendData(ArrayList<Exam> data);
    }

    String tag = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            screenshot_capture_permission();
            super.onCreate(savedInstanceState);
            loadLocale();
            setContentView(R.layout.activity_practice_test);
            tag = this.getClass().getSimpleName();
            toolbar = findViewById(R.id.toolbar);
            tv_toolbar = findViewById(R.id.collapsing_toolbar_textview);
            tv_toolbar.setText(getResources().getString(R.string.toolbar_hey) + ", " + GlobalValues.student.getName() + "\n" + getResources().getString(R.string.toolbar_text));
            toolbar.setTitleTextColor(getResources().getColor(R.color.White));
            toolbar.setPadding(30, 10, 10, 10);
            toolbar.setLogo(R.mipmap.ic_launcher);
            setSupportActionBar(toolbar);
            setTitle(getResources().getString(R.string.practice_test));
            registerreceiver();
            //back();
            //  toolbar.setLogo(R.mipmap.ic_launcher);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

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
            tabLayout.setTabTextColors(Color.parseColor("#32c5f3"), Color.parseColor("#ffffff"));
            viewPager = findViewById(R.id.viewpager);
            viewpageradapter = new ViewPagerAdapter(PracticeTestActivity.this, getSupportFragmentManager(), GlobalValues.mdataset_New, GlobalValues.mdataset_Attempted, GlobalValues.mdataset_Missed, type);
            viewPager.setAdapter(viewpageradapter);
            tabLayout.setupWithViewPager(viewPager);

            recyclerView = findViewById(R.id.recyclerviewforpracticetest);
            layoutManager = new LinearLayoutManager(PracticeTestActivity.this);
            recyclerView.setLayoutManager(layoutManager);
            tv_test = findViewById(R.id.typeoftestname);
            tv_notest = findViewById(R.id.notestavailable);
            practiceTestModels = new ArrayList<Exam>();
       /* for(int i =0; i< PracticeDemo.topicname.length;i++){
            Exam practicedata = new Exam();
            practiceTestModels.add(practicedata);
        }*/
            lang = getSharedPreferences("Settings", Activity.MODE_PRIVATE).getString("Current Language", "");
            b1 = getIntent().getExtras();
            type = b1.getString("testtype");
            tv_test.setText(type);

        /*tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.e("tab selected ", tab.getPosition()+"");
                switch (tab.getPosition()){
                    case 0:
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });*/

            try {
                //if (InternetUtils.getInstance(PracticeTestActivity.this).available()) {
                JSONObject obj = new JSONObject();
                //  obj.put("studentid", GlobalValues.studentProfile.getId());
                obj.put("studentid", GlobalValues.student.getId());
                //  obj.put("type", "current");
                obj.put("test_type", type);
                //obj.put("test_type",b1.getString("testtype"));
                Calendar cal = Calendar.getInstance();
                obj.put("year", cal.get(Calendar.YEAR));
                JSONObject mainobj = new JSONObject();
                mainobj.put("FilterParameter", obj.toString());
                genloading(getResources().getString(R.string.loading));
                //dialog.setCancelable(true);
                ConnectionManager.getInstance(PracticeTestActivity.this).getexam(mainobj.toString(), type);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            reporterror(tag, e.toString());
        }
    }

    public void gotoAction(Exam exam) {
        try {
            Intent iprogrprt = new Intent(PracticeTestActivity.this, ProgressReportActivity.class);
            iprogrprt.putExtra("progressreport", exam);
            iprogrprt.putExtra("flag", 1);
            startActivity(iprogrprt);
        } catch (Exception e) {
            reporterror(tag, e.toString());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            practiceTestAdapter = new PracticeTestAdapter(PracticeTestActivity.this, practiceTestModels, lang);
            for (int i = 0; i < practiceTestModels.size(); i++) {
                JSONArray array = DatabaseHelper.getInstance(PracticeTestActivity.this).getexamdetails(practiceTestModels.get(i).getExamid(), practiceTestModels.get(i).getExam_type());
                if (array.length() > 0) {
                    JSONObject obj = array.getJSONObject(0);
                    practiceTestModels.get(i).setSkipp(obj.getInt(DatabaseHelper.SKIPP));
                    practiceTestModels.get(i).setAnswered(obj.getInt(DatabaseHelper.ANSWERED));
                    practiceTestModels.get(i).setWrong(obj.getInt(DatabaseHelper.WRONG));
                    practiceTestModels.get(i).setCorrect(obj.getInt(DatabaseHelper.CORRECT));
                    practiceTestModels.get(i).setProgress(obj.getDouble(DatabaseHelper.PROGRESS));
                    practiceTestModels.get(i).setTimetaken(obj.getLong(DatabaseHelper.TIMETAKEN));
                    practiceTestModels.get(i).setSpeed(obj.getDouble(DatabaseHelper.SPEED));
                    //practiceTestModels.get(i).setTotal_questions(obj.getString(PracticeDatabaseHelper.QUESTION));
                }
            }
            practiceTestAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResponsed(int statuscode, int accesscode, String data) {
        super.onResponsed(statuscode, accesscode, data);
        try {
            if (statuscode == Constants.STATUS_OK) {
                if (accesscode == Connection.GET_EXAMS.ordinal()) {
                    try {
                        Log.e("received ", "received GET_EXAMS");
                        try {
                            practiceTestModels.clear();
                        } catch (Exception e) {
                            reporterror(tag, e.toString());
                            e.printStackTrace();
                        }
                   /* JSONObject obj1 = new JSONObject(GlobalValues.TEMP_STR).getJSONObject("root");
                    JSONArray array = new JSONArray();
                    if (obj1.optJSONArray("subroot") != null) {
                        array = obj1.getJSONArray("subroot");
                    } else if (obj1.optJSONObject("subroot") != null) {
                        if (obj1.getJSONObject("subroot").has("error")) {
                            updatefragmentdata();
                            return;
                        } else {
                            array.put(obj1.getJSONObject("subroot"));
                        }
                    }
                    ContentValues c = new ContentValues();
                    c.put(DatabaseHelper.JSONDATA, array.toString());
                    c.put(DatabaseHelper.SAVEDTIME, DateUtils.getSqliteTime());
                    c.put(DatabaseHelper.TYPE, type);
                    DatabaseHelper.getInstance(PracticeTestActivity.this).saveexaminationdata(c, type);
                    practiceTestModels.clear();
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
                        DatabaseHelper.getInstance(PracticeTestActivity.this).saveexaminationdatav1(c1, obj.getInt("examid"), obj.getInt("courseid"));
                    }*/
                        genloading("loading");
                        parseexamdata(-1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else if (accesscode == Connection.NoExamAvailable.ordinal()) {
                    try {
                        if (!((Activity) context).isFinishing()) {
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.nodata), Toast.LENGTH_LONG).show();
                        } else {
                            Log.e("no activity", "no activity ");
                        }
           /*         practiceTestModels.clear();
                    parseexamdata(-1);*/
                    } catch (Exception e) {
                        reporterror(tag, e.toString());
                        e.printStackTrace();
                    }
                } else if (accesscode == Connection.GET_EXAMSEXCEPTION.ordinal()) {
                    try {
                  /*  JSONArray array = DatabaseHelper.getInstance(PracticeTestActivity.this).getexaminationdata(type);

                    if (array.length() > 0) {
                        genloading("loading");
                        parseexamdata(-1);
                    }*/

                        if (!((Activity) context).isFinishing()) {
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.Connectiontimeout), Toast.LENGTH_LONG).show();
                        } else {
                        }
                        parseexamdata(-1);
                    } catch (Exception e) {
                        e.printStackTrace();
                        // JSONArray array = DatabaseHelper.getInstance(PracticeTestActivity.this).getexaminationdata(type);
                    }
                } else if (accesscode == Connection.Dataupdated.ordinal()) {
                    if (data.length() > 0) {
                        for (int i = 0; i < practiceTestModels.size(); i++) {
                            try {
                                JSONArray array = DatabaseHelper.getInstance(PracticeTestActivity.this).getexamdetails(practiceTestModels.get(i).getExamid(), practiceTestModels.get(i).getExam_type());
                                if (array.length() > 0) {
                                    JSONObject obj = array.getJSONObject(array.length() - 1);
                                    practiceTestModels.get(i).setSkipp(obj.getInt(DatabaseHelper.SKIPP));
                                    practiceTestModels.get(i).setAnswered(obj.getInt(DatabaseHelper.ANSWERED));
                                    practiceTestModels.get(i).setWrong(obj.getInt(DatabaseHelper.WRONG));
                                    practiceTestModels.get(i).setCorrect(obj.getInt(DatabaseHelper.CORRECT));
                                    practiceTestModels.get(i).setProgress(obj.getDouble(DatabaseHelper.PROGRESS));
                                    practiceTestModels.get(i).setTimetaken(obj.getLong(DatabaseHelper.TIMETAKEN));
                                    practiceTestModels.get(i).setSpeed(obj.getDouble(DatabaseHelper.SPEED));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            if (practiceTestModels.get(i).getExamid() == Integer.parseInt(data)) {
                                practiceTestModels.get(i).setIsexamgiven(true);

                                break;
                            }
                            int synccount = DatabaseHelper.getInstance(PracticeTestActivity.this).getasyncdatacount(practiceTestModels.get(i).getExamid(), practiceTestModels.get(i).getLanguages());
                            if (synccount > 0) {
                                practiceTestModels.get(i).setIssync(true);
                            } else {
                                practiceTestModels.get(i).setIssync(false);
                            }
                        }
                    } else {
                        for (int i = 0; i < practiceTestModels.size(); i++) {
                            try {
                                JSONArray array = DatabaseHelper.getInstance(PracticeTestActivity.this).getexamdetails(practiceTestModels.get(i).getExamid(), practiceTestModels.get(i).getExam_type());
                                if (array.length() > 0) {
                                    JSONObject obj = array.getJSONObject(array.length() - 1);
                                    practiceTestModels.get(i).setSkipp(obj.getInt(DatabaseHelper.SKIPP));
                                    practiceTestModels.get(i).setAnswered(obj.getInt(DatabaseHelper.ANSWERED));
                                    practiceTestModels.get(i).setWrong(obj.getInt(DatabaseHelper.WRONG));
                                    practiceTestModels.get(i).setCorrect(obj.getInt(DatabaseHelper.CORRECT));
                                    practiceTestModels.get(i).setProgress(obj.getDouble(DatabaseHelper.PROGRESS));
                                    practiceTestModels.get(i).setTimetaken(obj.getLong(DatabaseHelper.TIMETAKEN));
                                    practiceTestModels.get(i).setSpeed(obj.getDouble(DatabaseHelper.SPEED));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            int count = DatabaseHelper.getInstance(PracticeTestActivity.this).getqdata1count(practiceTestModels.get(i).getExamid(), practiceTestModels.get(i).getLanguages());
                            if (count > 0) {
                                practiceTestModels.get(i).setIsexamgiven(true);
                            } else {
                                practiceTestModels.get(i).setIsexamgiven(false);
                            }

                            int synccount = DatabaseHelper.getInstance(PracticeTestActivity.this).getasyncdatacount(practiceTestModels.get(i).getExamid(), practiceTestModels.get(i).getLanguages());
                            if (synccount > 0) {
                                practiceTestModels.get(i).setIssync(true);
                            } else {
                                practiceTestModels.get(i).setIssync(false);
                            }
                        }
                    }
                    updatefragmentdata();
                } else if (accesscode == Connection.Dataupdatedattemptest.ordinal()) {
                    for (int i = 0; i < practiceTestModels.size(); i++) {
                        if (practiceTestModels.get(i).getExamid() == Integer.parseInt(data)) {
                            practiceTestModels.get(i).setExamstatus("Attempted");
                            break;
                        }
                    }
                    updatefragmentdata();
                } else if (accesscode == Connection.GetReceiptid.ordinal()) {
                    try {
                        //if (InternetUtils.getInstance(PracticeTestActivity.this).available()) {
                        JSONObject obj = new JSONObject();
                        //  obj.put("studentid", GlobalValues.studentProfile.getId());
                        obj.put("studentid", GlobalValues.student.getId());
                        //  obj.put("type", "current");
                        obj.put("test_type", type);
                        //obj.put("test_type",b1.getString("testtype"));

                        Calendar cal = Calendar.getInstance();
                        obj.put("year", cal.get(Calendar.YEAR));
                        JSONObject mainobj = new JSONObject();
                        mainobj.put("FilterParameter", obj.toString());
                        //genloading("loading..");
                        //dialog.setCancelable(true);
                        ConnectionManager.getInstance(PracticeTestActivity.this).getexam(mainobj.toString(), type);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            reporterror(tag, e.toString());
        }
    }

    public void parseexamdata(int courseid) {
        try {
            //  Log.e("received ","received parseexamdata");
            practiceTestModels.clear();
            JSONArray array1 = DatabaseHelper.getInstance(PracticeTestActivity.this).getexaminationdatav1(type, courseid);
            for (int i = 0; i < array1.length(); i++) {
                // JSONObject obj = new JSONObject(array1.getJSONObject(i).getString(DatabaseHelper.JSONDATA));
                try {
                    JSONObject obj = array1.getJSONObject(i);
                    JSONObject obj1 = new JSONObject(obj.getString(DatabaseHelper.JSONDATA));
                    Gson gson = new Gson();
                    Type type = new TypeToken<Exam>() {
                    }.getType();
                    Exam exam = gson.fromJson(obj1.toString(), type);
                    practiceTestModels.add(exam);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            for (int i = 0; i < practiceTestModels.size(); i++) {
                if (lang.equals("mr")) {
                    practiceTestModels.get(i).setLanguages("marathi");
                } else {
                    practiceTestModels.get(i).setLanguages("ENGLISH");
                }

                int count = DatabaseHelper.getInstance(PracticeTestActivity.this).getqdata1count(practiceTestModels.get(i).getExamid(), practiceTestModels.get(i).getLanguages());
                if (count > 0) {
                    practiceTestModels.get(i).setIsexamgiven(true);
                } else {
                    practiceTestModels.get(i).setIsexamgiven(false);
                }

                int synccount = DatabaseHelper.getInstance(PracticeTestActivity.this).getasyncdatacount(practiceTestModels.get(i).getExamid(), practiceTestModels.get(i).getLanguages());
                if (synccount > 0) {
                    practiceTestModels.get(i).setIssync(true);
                } else {
                    practiceTestModels.get(i).setIssync(false);
                }

                int qcount = DatabaseHelper.getInstance(PracticeTestActivity.this).getqdatacount(practiceTestModels.get(i).getExamid(), practiceTestModels.get(i).getLanguages());
                if (qcount > 0) {
                    practiceTestModels.get(i).setIsqdownloaded(true);
                } else {
                    practiceTestModels.get(i).setIsqdownloaded(false);
                }

                JSONArray array = DatabaseHelper.getInstance(PracticeTestActivity.this).getexamdetails(practiceTestModels.get(i).getExamid(), practiceTestModels.get(i).getExam_type());
                if (array.length() > 0) {
                    JSONObject obj = array.getJSONObject(0);
                    practiceTestModels.get(i).setSkipp(obj.getInt(DatabaseHelper.SKIPP));
                    practiceTestModels.get(i).setAnswered(obj.getInt(DatabaseHelper.ANSWERED));
                    practiceTestModels.get(i).setWrong(obj.getInt(DatabaseHelper.WRONG));
                    practiceTestModels.get(i).setCorrect(obj.getInt(DatabaseHelper.CORRECT));
                    practiceTestModels.get(i).setProgress(obj.getDouble(DatabaseHelper.PROGRESS));
                    practiceTestModels.get(i).setTimetaken(obj.getLong(DatabaseHelper.TIMETAKEN));
                    practiceTestModels.get(i).setSpeed(obj.getDouble(DatabaseHelper.SPEED));
                }
            }
            practiceTestAdapter.notifyDataSetChanged();
            updatefragmentdata();
        } catch (Exception e) {
            e.printStackTrace();
        }
   /*     viewPager = findViewById(R.id.viewpager);
        viewpageradapter = new ViewPagerAdapter(PracticeTestActivity.this, getSupportFragmentManager(), GlobalValues.mdataset_New, GlobalValues.mdataset_Attempted, GlobalValues.mdataset_Missed);
        viewPager.setAdapter(viewpageradapter);
        tabLayout.setupWithViewPager(viewPager);*/
        try {
            if (coursedata.size() == 0) {
                JSONArray array = DatabaseHelper.getInstance(PracticeTestActivity.this).getcoursedata();
                Gson gson = new Gson();
                Type type = new TypeToken<ArrayList<Course>>() {
                }.getType();
                ArrayList<Course> courses1 = gson.fromJson(array.toString(), type);
                coursedata.add(new Course(-1, "Please Select Course", "कृपया कोर्स निवडा"));
                coursedata.addAll(courses1);
                CoursesAdapter spadapter = new CoursesAdapter(PracticeTestActivity.this, coursedata, lang);
                sp.setAdapter(spadapter);
            }
        } catch (Exception e) {
            reporterror(tag, e.toString());
        }
    }

    /*  public void parseexamdata(String s) {
          try {
              Log.e("resdata ", "resdata " + s);
              Object json = new JSONTokener(s).nextValue();
              practiceTestModels.clear();
              if (json instanceof JSONObject) {
                  Log.e("JSONObject ", "else ");
                  JSONObject obj = new JSONObject(s);
                  Gson gson = new Gson();
                  Type type = new TypeToken<Exam>() {
                  }.getType();
                  Exam exam = gson.fromJson(obj.toString(), type);
                  practiceTestModels.add(exam);
                  practiceTestAdapter.notifyDataSetChanged();
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
                          practiceTestModels.addAll(dataset);

                          for (int i = 0; i < test.size(); i++) {
                              Log.e("examname to retrieve ", "" + test.get(i));
                              practiceTestModels.get(i).setExamname(test.get(i));
                          }

                          Log.e("practicedata size ", "practiceTestModels size " + practiceTestModels.size());
                      } else {
                          tv_notest.setVisibility(View.VISIBLE);
                          tv_notest.setText("Sorry, we have no TESTS available at the moment! \n\nWe'll update you soon. Keep checking for updates.");
                      }

  //                    practiceTestAdapter.notifyDataSetChanged();
                  }
              } else {
                  Log.e("else ", "hey no test ");
              }

              for (int i = 0; i < practiceTestModels.size(); i++) {
                  if (lang.equals("mr")) {
                      practiceTestModels.get(i).setLanguages("marathi");
                  } else {
                      practiceTestModels.get(i).setLanguages("ENGLISH");
                  }

                  int count = DatabaseHelper.getInstance(PracticeTestActivity.this).getqdatacount(practiceTestModels.get(i).getExamid(), practiceTestModels.get(i).getLanguages());
                  if (count > 0) {
                      Log.e("countif ", "count " + count);
                      practiceTestModels.get(i).setIsexamgiven(true);
                  } else {
                      Log.e("countelse ", "count " + count);
                      practiceTestModels.get(i).setIsexamgiven(false);
                  }

                  int synccount = DatabaseHelper.getInstance(PracticeTestActivity.this).getasyncdatacount(practiceTestModels.get(i).getExamid(), practiceTestModels.get(i).getLanguages());
                  if (synccount > 0) {
                      Log.e("sync ", "sync " + synccount);
                      practiceTestModels.get(i).setIssync(true);
                  } else {
                      Log.e("sync ", "sync " + synccount);
                      practiceTestModels.get(i).setIssync(false);
                  }

                  int qcount = DatabaseHelper.getInstance(PracticeTestActivity.this).getqdatacount(practiceTestModels.get(i).getExamid(), practiceTestModels.get(i).getLanguages());
                  if (qcount > 0) {
                      Log.e("attem ", "sync " + synccount);
                      practiceTestModels.get(i).setIsqdownloaded(true);
                  } else {
                      Log.e("sync ", "sync " + synccount);
                      practiceTestModels.get(i).setIsqdownloaded(false);
                  }

                  JSONArray array = DatabaseHelper.getInstance(PracticeTestActivity.this).getexamdetails(practiceTestModels.get(i).getExamid(), practiceTestModels.get(i).getExam_type());
                  if (array.length() > 0) {
                      JSONObject obj = array.getJSONObject(array.length()-1);
                      practiceTestModels.get(i).setSkipp(obj.getInt(DatabaseHelper.SKIPP));
                      practiceTestModels.get(i).setAnswered(obj.getInt(DatabaseHelper.ANSWERED));
                      practiceTestModels.get(i).setWrong(obj.getInt(DatabaseHelper.WRONG));
                      practiceTestModels.get(i).setCorrect(obj.getInt(DatabaseHelper.CORRECT));
                      practiceTestModels.get(i).setProgress(obj.getDouble(DatabaseHelper.PROGRESS));
                      practiceTestModels.get(i).setTimetaken(obj.getLong(DatabaseHelper.TIMETAKEN));
                      practiceTestModels.get(i).setSpeed(obj.getDouble(DatabaseHelper.SPEED));
                      //Log.e("progress for exam id ",practiceTestModels.get(i).getExamid()+" is: "+((int) Math.round(practiceTestModels.get(i).getProgress())));
                      Log.e("progress ", "prog");
                      *//*else if(currentdate > practiceTestModels.get(i).getEnddate()){
                        practiceTestModel_Missed.add(practiceTestModels.get(i));
                    }*//*
                }
            }
            updatefragmentdata();
           *//* practiceTestAdapter = new PracticeTestAdapter(PracticeTestActivity.this, practiceTestModels);
            recyclerView.setAdapter(practiceTestAdapter);
            practiceTestAdapter.notifyDataSetChanged();*//*
        } catch (Exception e) {
            Log.e("in catch ", "catch");
            e.printStackTrace();
        }

        Log.e("out of ", "catch");


        viewPager = findViewById(R.id.viewpager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(PracticeTestActivity.this, getSupportFragmentManager(), GlobalValues.mdataset_New, GlobalValues.mdataset_Attempted, GlobalValues.mdataset_Missed);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

       *//* for (int j = 0; j < practiceTestModel_New.size(); j++) {
            Log.e("in ", "flag==0, size " + practiceTestModel_New.size());
            viewPager = findViewById(R.id.viewpager);
            ViewPagerAdapter adapter = new ViewPagerAdapter(PracticeTestActivity.this, getSupportFragmentManager(), practiceTestModel_New);
            viewPager.setAdapter(adapter);
            tabLayout.setupWithViewPager(viewPager);
        }
        for (int j = 0; j < practiceTestModel_Attempted.size(); j++) {
            Log.e("in ", "flag==0, size " + practiceTestModel_Attempted.size());
            viewPager = findViewById(R.id.viewpager);
            ViewPagerAdapter adapter = new ViewPagerAdapter(PracticeTestActivity.this, getSupportFragmentManager(), practiceTestModel_Attempted);
            viewPager.setAdapter(adapter);
            tabLayout.setupWithViewPager(viewPager);
        }
        for (int j = 0; j < practiceTestModel_Missed.size(); j++) {
            Log.e("in ", "flag==0, size " + practiceTestModel_Missed.size());
            viewPager = findViewById(R.id.viewpager);
            ViewPagerAdapter adapter = new ViewPagerAdapter(PracticeTestActivity.this, getSupportFragmentManager(), practiceTestModel_Missed);
            viewPager.setAdapter(adapter);
            tabLayout.setupWithViewPager(viewPager);
        }*//*

    }*/
    public void updatefragmentdata() {
        try {
            Log.e("received ", "received updatefragmentdata");
            GlobalValues.mdataset_New.clear();
            GlobalValues.mdataset_Attempted.clear();
            GlobalValues.mdataset_Missed.clear();
            for (int i = 0; i < practiceTestModels.size(); i++) {
                if (((int) Math.round(practiceTestModels.get(i).getProgress())) > 0) {
                    if (GlobalValues.mdataset_Attempted.size() > 0) {
                        if (!GlobalValues.mdataset_Attempted.get(GlobalValues.mdataset_Attempted.size() - 1).getCoursename().equalsIgnoreCase(practiceTestModels.get(i).getCoursename())) {
                            Exam exam = new Exam();
                            exam.setIsheader(true);
                            exam.setCoursename(practiceTestModels.get(i).getCoursename());
                            exam.setCoursename_inmarathi(practiceTestModels.get(i).getCoursename_inmarathi());
                            GlobalValues.mdataset_Attempted.add(exam);
                        }
                    } else if (GlobalValues.mdataset_Attempted.size() == 0) {
                        Exam exam = new Exam();
                        exam.setIsheader(true);
                        exam.setCoursename(practiceTestModels.get(i).getCoursename());
                        exam.setCoursename_inmarathi(practiceTestModels.get(i).getCoursename_inmarathi());
                        GlobalValues.mdataset_Attempted.add(exam);
                    }
                    // practiceTestModels.get(i).setExamstatus("Attempted");
                    GlobalValues.mdataset_Attempted.add(practiceTestModels.get(i));
                } else if (practiceTestModels.get(i).getExamstatus().equalsIgnoreCase("Missed")) {
                    if (GlobalValues.mdataset_Missed.size() > 0) {
                        if (!GlobalValues.mdataset_Missed.get(GlobalValues.mdataset_Missed.size() - 1).getCoursename().equalsIgnoreCase(practiceTestModels.get(i).getCoursename())) {
                            Exam exam = new Exam();
                            exam.setIsheader(true);
                            exam.setCoursename(practiceTestModels.get(i).getCoursename());
                            exam.setCoursename_inmarathi(practiceTestModels.get(i).getCoursename_inmarathi());
                            GlobalValues.mdataset_Missed.add(exam);
                        }
                    } else if (GlobalValues.mdataset_Missed.size() == 0) {
                        Exam exam = new Exam();
                        exam.setIsheader(true);
                        exam.setCoursename(practiceTestModels.get(i).getCoursename());
                        exam.setCoursename_inmarathi(practiceTestModels.get(i).getCoursename_inmarathi());
                        GlobalValues.mdataset_Missed.add(exam);
                    }
                    GlobalValues.mdataset_Missed.add(practiceTestModels.get(i));
                } else if (practiceTestModels.get(i).getExamstatus().equalsIgnoreCase("Attempted")) {
                    if (GlobalValues.mdataset_Attempted.size() > 0) {
                        if (!GlobalValues.mdataset_Attempted.get(GlobalValues.mdataset_Attempted.size() - 1).getCoursename().equalsIgnoreCase(practiceTestModels.get(i).getCoursename())) {
                            Exam exam = new Exam();
                            exam.setIsheader(true);
                            exam.setCoursename(practiceTestModels.get(i).getCoursename());
                            exam.setCoursename_inmarathi(practiceTestModels.get(i).getCoursename_inmarathi());
                            GlobalValues.mdataset_Attempted.add(exam);
                        }
                    } else if (GlobalValues.mdataset_Attempted.size() == 0) {
                        Exam exam = new Exam();
                        exam.setIsheader(true);
                        exam.setCoursename(practiceTestModels.get(i).getCoursename());
                        exam.setCoursename_inmarathi(practiceTestModels.get(i).getCoursename_inmarathi());
                        GlobalValues.mdataset_Attempted.add(exam);
                    }
                    GlobalValues.mdataset_Attempted.add(practiceTestModels.get(i));
                } else {

                    if (((int) Math.round(practiceTestModels.get(i).getProgress())) == 0) {

                        if (GlobalValues.mdataset_New.size() > 0) {
                            if (!GlobalValues.mdataset_New.get(GlobalValues.mdataset_New.size() - 1).getCoursename().equalsIgnoreCase(practiceTestModels.get(i).getCoursename())) {
                                Exam exam = new Exam();
                                exam.setIsheader(true);
                                exam.setCoursename(practiceTestModels.get(i).getCoursename());
                                exam.setCoursename_inmarathi(practiceTestModels.get(i).getCoursename_inmarathi());
                                GlobalValues.mdataset_New.add(exam);
                            }
                        } else if (GlobalValues.mdataset_New.size() == 0) {
                            Exam exam = new Exam();
                            exam.setIsheader(true);
                            exam.setCoursename(practiceTestModels.get(i).getCoursename());
                            exam.setCoursename_inmarathi(practiceTestModels.get(i).getCoursename_inmarathi());
                            GlobalValues.mdataset_New.add(exam);
                        }
                        GlobalValues.mdataset_New.add(practiceTestModels.get(i));
                    }
                }
            }

            publishBroadcast(200, Connection.Syncdata.ordinal());
       /*  final Handler handler = new Handler();
       final Runnable r = new Runnable() {
            public void run() {
                if (dataFromActivityToFragment != null)
                    dataFromActivityToFragment.sendData(GlobalValues.mdataset_New);


                if (dataFromActivityToFragmentattempted != null)
                    dataFromActivityToFragmentattempted.sendData(GlobalValues.mdataset_Attempted);

                if (dataFromActivityToFragmentmissed != null)
                    dataFromActivityToFragmentmissed.sendData(GlobalValues.mdataset_Missed);

            }
        };
        handler.postDelayed(r, 0);*/
            try {
                Intent intent = new Intent("updateddata");
                intent.putExtra(Constants.BROADCAST_RESPONSE_CODE, 200);
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);


                Log.e("brodcast send  ", "broadcast send");
            } catch (Exception e) {
                e.printStackTrace();
            }
            dismissLoading();
        } catch (Exception e) {
            reporterror(tag, e.toString());
            dismissLoading();
        }
    }
    /*private void publishBroadcast() {
        try {
            Log.e("broadcast send","broadcast send");
            Intent intent = new Intent("updateddata");
            LocalBroadcastManager.getInstance(PracticeTestActivity.this).sendBroadcast(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

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

    @Override
    protected void onDestroy() {
        try {
            GlobalValues.mdataset_New.clear();
            GlobalValues.mdataset_Attempted.clear();
            GlobalValues.mdataset_Missed.clear();
            GlobalValues.selectedtesttype = "";
        } catch (Exception e) {
            reporterror(tag, e.toString());
        }
        super.onDestroy();
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        Drawable icon_color_change = menu.getItem(0).getIcon(); // change 0 with 1,2 ...
        icon_color_change.mutate();
        icon_color_change.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_IN);

        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                practiceTestAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }*/
}
