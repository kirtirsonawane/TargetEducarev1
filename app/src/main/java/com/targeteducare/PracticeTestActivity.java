package com.targeteducare;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.targeteducare.Adapter.PracticeTestAdapter;

import com.targeteducare.Classes.Exam;
import com.targeteducare.database.DatabaseHelper;


import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;

public class PracticeTestActivity extends Activitycommon {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    private ArrayList<Exam> practiceTestModels;
    private PracticeTestAdapter practiceTestAdapter;
    Bundle b1;

    TextView tv_test, tv_notest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice_test);
        Log.e("I am in ", "PracticeTestActivity");
        setmaterialDesign();
        setTitle("Practice Test");
        back();

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

        b1 = getIntent().getExtras();
        tv_test.setText(b1.getString("testtype"));
        try {
            //if (InternetUtils.getInstance(PracticeTestActivity.this).available()) {
            JSONObject obj = new JSONObject();
          //  obj.put("studentid", GlobalValues.studentProfile.getId());
            obj.put("studentid", 1);
            obj.put("type", "current");
            obj.put("test_type",b1.getString("testtype"));
            //obj.put("test_type",b1.getString("testtype"));

            Calendar cal = Calendar.getInstance();
            obj.put("year", cal.get(Calendar.YEAR));
            JSONObject mainobj = new JSONObject();
            mainobj.put("FilterParameter", obj.toString());
            genloading("loading..");
            dialog.setCancelable(true);
            ConnectionManager.getInstance(PracticeTestActivity.this).getexam(mainobj.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void gotoAction(int i) {

        Intent iprogrprt = new Intent(PracticeTestActivity.this, ProgressReportActivity.class);
        iprogrprt.putExtra("progressreport",practiceTestModels);
        iprogrprt.putExtra("flag",1);
        startActivity(iprogrprt);
    }

    /*public void gotopracticetest(int i) {
        Intent icheckprogress = new Intent(PracticeTestActivity.this,ProgressReportActivity.class);
        startActivity(icheckprogress);
    }

    public int incrementprogress(int i) {
        return progress++;
    }*/


    @Override
    protected void onResume() {
        super.onResume();
        try {
            for (int i=0;i<practiceTestModels.size();i++) {
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
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResponsed(int statuscode, int accesscode, String data) {
        super.onResponsed(statuscode, accesscode, data);
        Log.e("onres ","res "+GlobalValues.TEMP_STR);

        if (statuscode == Constants.STATUS_OK) {
            if (accesscode == Connection.GET_EXAMS.ordinal()) {

                try {
                    JSONArray jsonArray = new JSONArray(GlobalValues.TEMP_STR);
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String cat_name = jsonObject.optString("categoryname");
                        String examid = jsonObject.optString("examid");
                        String examname = jsonObject.optString("examname");
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
                    /*
                    JSONObject jsonObject = new JSONObject(GlobalValues.TEMP_STR);
                    ContentValues c = new ContentValues();
                    c.put(PracticeDatabaseHelper.JSONDATA, GlobalValues.TEMP_STR);
                    c.put(PracticeDatabaseHelper.SAVEDTIME, DateUtils.getSqliteTime());
                    PracticeDatabaseHelper.getInstance(PracticeTestActivity.this).saveexaminationdata(c);*/
                    parseexamdata(GlobalValues.TEMP_STR);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void parseexamdata(String s) {
        try {
            Log.e("resdata ","resdata "+s);
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
                    if(dataset.size()>=0){
                        tv_notest.setVisibility(View.GONE);
                        practiceTestModels.addAll(dataset);
                        Log.e("practicedata size ", "practiceTestModels size " + practiceTestModels.size());
                    }
                    else{
                        tv_notest.setVisibility(View.VISIBLE);
                        tv_notest.setText("Sorry, we have no TESTS available at the moment! \n\nWe'll update you soon. Keep checking for updates.");
                    }

//                    practiceTestAdapter.notifyDataSetChanged();
                }
            } else {
                Log.e("else ", "hey no test ");
            }

            for (int i = 0; i < practiceTestModels.size(); i++) {
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

            practiceTestAdapter = new PracticeTestAdapter(PracticeTestActivity.this,practiceTestModels);
            recyclerView.setAdapter(practiceTestAdapter);
            practiceTestAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
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
    }
}
