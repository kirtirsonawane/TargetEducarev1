package com.targeteducare;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.targeteducare.Adapter.ExamAdapter;
import com.targeteducare.Classes.Exam;
import com.targeteducare.database.DatabaseHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

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
    ArrayList<Exam> mdataset;
    ArrayList<Uri> filesdata;
   int  sendmailselectedpos=-1;

    Bundle b1;

   TextView tv_notest;
 //  ImageView img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_list);

        tv_notest = findViewById(R.id.notestavailable);



        setmaterialDesign();
        setTitle("Monthly Ranking Test");
        back();
        setTitle("");
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        attachUI();
    }


    private void attachUI() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);
        }

        /*img=(ImageView)findViewById(R.id.imageview_1);

        ArrayList<QuestionURL> qdata=new ArrayList<>();
        qdata.add(new QuestionURL("http://192.168.1.59:8095/images/Temp/636888521160850000_files/image042.png"));
        new DownloadImage().execute(qdata);*/
        StructureClass.defineContext(ExamListActivity.this);
        filesdata=new ArrayList<>();
        mdataset = new ArrayList<>();
        mRecyclerview = (RecyclerView) findViewById(R.id.recyclerview_1);
        mLayoutmanager = new LinearLayoutManager(ExamListActivity.this);
        mRecyclerview.setLayoutManager(mLayoutmanager);
        adapter = new ExamAdapter(ExamListActivity.this, mdataset);
        mRecyclerview.setAdapter(adapter);

        try {
            //if (InternetUtils.getInstance(ExamListActivity.this).available()) {
            JSONObject obj = new JSONObject();
            //obj.put("studentid", GlobalValues.studentProfile.getId());
            obj.put("studentid", "1");
            obj.put("type", "current");

            //newly added
            b1 = getIntent().getExtras();
            //obj.put("test_type",b1.getString("testtype"));
            obj.put("test_type","Monthly Ranking Test");

            Calendar cal = Calendar.getInstance();
            obj.put("year", cal.get(Calendar.YEAR));
            JSONObject mainobj = new JSONObject();
            mainobj.put("FilterParameter", obj.toString());
            genloading("loading..");
            dialog.setCancelable(true);
            ConnectionManager.getInstance(ExamListActivity.this).getexam(mainobj.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 101: {
                try {
                   // if (selectedposition != -1) {
                        JSONArray array = DatabaseHelper.getInstance(ExamListActivity.this).getansdata1(mdataset.get(sendmailselectedpos).getExamid(), mdataset.get(sendmailselectedpos).getLanguages());
                        Log.e("array", "array " + array.toString());
                        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);
                        } else {
                            if (array.length() > 0) {
                                genloading("loading");
                                StructureClass.clearAll();
                                filesdata.clear();
                                createfiles(array);
                            } else {
                                if (!((Activity) context).isFinishing()) {
                                    Toast.makeText(getApplicationContext(), "No data available", Toast.LENGTH_LONG).show();
                                } else {
                                    Log.e("no activity", "no activity ");
                                }
                            }
                        }
                   // }
                }catch (Exception e)
                {
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
                    ContentValues c = new ContentValues();
                    c.put(DatabaseHelper.JSONDATA, GlobalValues.TEMP_STR);
                    c.put(DatabaseHelper.SAVEDTIME, DateUtils.getSqliteTime());
                    DatabaseHelper.getInstance(ExamListActivity.this).saveexaminationdata(c);
                    parseexamdata(GlobalValues.TEMP_STR);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (accesscode == Connection.GET_EXAMSEXCEPTION.ordinal()) {
                try {
                    JSONArray array = DatabaseHelper.getInstance(ExamListActivity.this).getexaminationdata();
                    Log.e("data ",array.toString());
                    if (array.length() > 0) {
                        parseexamdata(array.getJSONObject(0).getString(DatabaseHelper.JSONDATA));
                    }
                    if (!((Activity) context).isFinishing()) {
                        Toast.makeText(getApplicationContext(), Constants.connectiontimeout, Toast.LENGTH_LONG).show();
                    }else {
                        Log.e("no activity","no activity ");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (accesscode == Connection.GET_QUESTIONS.ordinal()) {
                try {
                    for (int i = 0; i < mdataset.size(); i++) {
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
                    }else {
                        Log.e("no activity","no activity ");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (accesscode == Connection.GET_QUESTIONSEXCEPTION.ordinal()) {
                try {
                    if (!((Activity) context).isFinishing()) {
                        Toast.makeText(getApplicationContext(), Constants.connectiontimeout, Toast.LENGTH_LONG).show();
                    }else {
                    Log.e("no activity","no activity ");
                }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (accesscode == Connection.IPChange.ordinal()) {
                try {
                    // if (InternetUtils.getInstance(ExamListActivity.this).available()) {
                    JSONObject obj = new JSONObject();
                    obj.put("studentid", "1");
                    //obj.put("studentid", GlobalValues.studentProfile.getId());
                    obj.put("type", "current");
                    Calendar cal = Calendar.getInstance();
                    obj.put("year", cal.get(Calendar.YEAR));
                    JSONObject mainobj = new JSONObject();
                    mainobj.put("FilterParameter", obj.toString());
                    genloading("loading..");
                    ConnectionManager.getInstance(ExamListActivity.this).getexam(mainobj.toString());
                    //  }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (accesscode == Connection.SAVE_ANSWER.ordinal()) {
                try {
                    for (int i = 0; i < mdataset.size(); i++) {
                        int synccount = DatabaseHelper.getInstance(ExamListActivity.this).getasyncdatacount(mdataset.get(i).getExamid(), mdataset.get(i).getLanguages());
                        if (synccount > 0) {
                            Log.e("synccount ", "synccount " + synccount);
                            mdataset.get(i).setIssync(true);
                        } else {
                            Log.e("synccount ", "synccount " + synccount);
                            mdataset.get(i).setIssync(false);
                        }
                    }
                    adapter.notifyDataSetChanged();
                    if (!((Activity) context).isFinishing()) {
                        Toast.makeText(getApplicationContext(), "Thank You", Toast.LENGTH_LONG).show();
                    }else {
                        Log.e("no activity","no activity ");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (accesscode == Connection.SAVE_ANSWEREXCEPTION.ordinal()) {
                try {
                    if (!((Activity) context).isFinishing()) {
                        Toast.makeText(getApplicationContext(), Constants.connectiontimeout, Toast.LENGTH_LONG).show();
                    }else {
                        Log.e("no activity","no activity ");
                    }
                    for (int i = 0; i < mdataset.size(); i++) {
                        int synccount = DatabaseHelper.getInstance(ExamListActivity.this).getasyncdatacount(mdataset.get(i).getExamid(), mdataset.get(i).getLanguages());
                        if (synccount > 0) {
                            Log.e("synccount ", "synccount " + synccount);
                            mdataset.get(i).setIssync(true);
                        } else {
                            Log.e("synccount ", "synccount " + synccount);
                            mdataset.get(i).setIssync(false);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (accesscode == Connection.Dataupdated.ordinal()) {
                Log.e("data ", "data " + data);
                if (data.length() > 0) {
                    for (int i = 0; i < mdataset.size(); i++) {
                        if (mdataset.get(i).getExamid() == Integer.parseInt(data)) {
                            mdataset.get(i).setIsexamgiven(true);
                            adapter.notifyDataSetChanged();
                            break;
                        }

                        int synccount = DatabaseHelper.getInstance(ExamListActivity.this).getasyncdatacount(mdataset.get(i).getExamid(), mdataset.get(i).getLanguages());
                        if (synccount > 0) {
                            Log.e("synccount ", "synccount " + synccount);
                            mdataset.get(i).setIssync(true);
                        } else {
                            Log.e("synccount ", "synccount " + synccount);
                            mdataset.get(i).setIssync(false);
                        }
                    }
                } else {
                    for (int i = 0; i < mdataset.size(); i++) {
                        int count = DatabaseHelper.getInstance(ExamListActivity.this).getqdata1count(mdataset.get(i).getExamid(), mdataset.get(i).getLanguages());
                        if (count > 0) {
                            mdataset.get(i).setIsexamgiven(true);
                        } else {
                            mdataset.get(i).setIsexamgiven(false);
                        }

                        int synccount = DatabaseHelper.getInstance(ExamListActivity.this).getasyncdatacount(mdataset.get(i).getExamid(), mdataset.get(i).getLanguages());
                        if (synccount > 0) {
                            Log.e("synccount ", "synccount " + synccount);
                            mdataset.get(i).setIssync(true);
                        } else {
                            Log.e("synccount ", "synccount " + synccount);
                            mdataset.get(i).setIssync(false);
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.exam, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_ip:
                Intent intent = new Intent(ExamListActivity.this, IPSettingActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void parseexamdata(String s) {
        try {
            Object json = new JSONTokener(s).nextValue();
            mdataset.clear();
            if (json instanceof JSONObject) {
                Log.e("JSONObject ", "else ");
                JSONObject obj = new JSONObject(s);
                Gson gson = new Gson();
                Type type = new TypeToken<Exam>() {
                }.getType();
                Exam exam = gson.fromJson(obj.toString(), type);
                mdataset.add(exam);
                adapter.notifyDataSetChanged();
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
                        mdataset.addAll(dataset);
                        Log.e("mdataset size ", "mdataset size " + mdataset.size());
                    }
                    else{
                        tv_notest.setVisibility(View.VISIBLE);
                        tv_notest.setText("Sorry, we have no TESTS available at the moment! \n\nWe'll update you soon. Keep checking for updates.");
                    }

                    adapter.notifyDataSetChanged();
                }
            } else {
                Log.e("else ", "else ");
            }

            for (int i = 0; i < mdataset.size(); i++) {
                int count = DatabaseHelper.getInstance(ExamListActivity.this).getqdata1count(mdataset.get(i).getExamid(), mdataset.get(i).getLanguages());
                if (count > 0) {
                    Log.e("countif ", "count " + count);
                    mdataset.get(i).setIsexamgiven(true);
                } else {
                    Log.e("countelse ", "count " + count);
                    mdataset.get(i).setIsexamgiven(false);
                }

                int synccount = DatabaseHelper.getInstance(ExamListActivity.this).getasyncdatacount(mdataset.get(i).getExamid(), mdataset.get(i).getLanguages());
                if (synccount > 0) {
                    Log.e("sync ", "sync " + synccount);
                    mdataset.get(i).setIssync(true);
                } else {
                    Log.e("sync ", "sync " + synccount);
                    mdataset.get(i).setIssync(false);
                }

                int qcount = DatabaseHelper.getInstance(ExamListActivity.this).getqdatacount(mdataset.get(i).getExamid(), mdataset.get(i).getLanguages());
                if (qcount > 0) {
                    Log.e("attem ", "sync " + synccount);
                    mdataset.get(i).setIsqdownloaded(true);
                } else {
                    Log.e("sync ", "sync " + synccount);
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void syncdata(int position) {
        JSONArray array = DatabaseHelper.getInstance(ExamListActivity.this).getansdata(mdataset.get(position).getExamid(), mdataset.get(position).getLanguages());
        if (array.length() > 0) {
            try {
                JSONObject objdata = array.getJSONObject(0);
                JSONObject obj = new JSONObject(objdata.getString(DatabaseHelper.JSONDATA));
                JSONObject obj1 = new JSONObject(objdata.getString(DatabaseHelper.ANSWERDATA));
                JSONObject obj2 = new JSONObject(objdata.getString(DatabaseHelper.STARTTIMEOBJ));
                long id = objdata.getLong(DatabaseHelper.ID);

                Log.e("obj", "obj " + obj.toString());
                Log.e("obj1", "obj1" + obj1.toString());

                //if (InternetUtils.getInstance(ExamListActivity.this).available()) {
                genloading("loading..");
                ConnectionManager.getInstance(ExamListActivity.this).SAVE_ANSWERSHEET(obj.toString(), obj1.toString(),obj2.toString(), id);
               /* } else {
                    Toast.makeText(getApplicationContext(), "No Network available", Toast.LENGTH_LONG).show();
                }*/
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            if (!((Activity) context).isFinishing()) {
                Toast.makeText(getApplicationContext(), "No offline data available", Toast.LENGTH_LONG).show();
            }else {
                Log.e("no activity","no activity ");
            }
        }
    }

    int flag=-1;
   int  selectedposition=0;
    public void downloaddata(int position) {
           if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);
        }else {
               this.flag = flag;
               selectedposition = 0;
               //   if (InternetUtils.getInstance(ExamListActivity.this).available()) {
               try {
                   genloading("loading");
                   dialog.setCancelable(true);
                   JSONObject obj = new JSONObject();
                   obj.put("examid", mdataset.get(position).getExamid());
                   obj.put("language", mdataset.get(position).getLanguages());
                   obj.put("ImageRelPath", "http://" + GlobalValues.IP);
                   JSONObject mainobj = new JSONObject();
                   mainobj.put("FilterParameter", obj.toString());
                   ConnectionManager.getInstance(ExamListActivity.this).getquestion(mainobj.toString(), mdataset.get(position).getExamid(), mdataset.get(position).getLanguages());
               } catch (Exception e) {
                   e.printStackTrace();
               }
           }
     /*   } else {
            Toast.makeText(getApplicationContext(), "No Network", Toast.LENGTH_LONG).show();
        }*/
    }

    public void maildata(int position) {
        sendmailselectedpos=position;
        JSONArray array = DatabaseHelper.getInstance(ExamListActivity.this).getansdata1(mdataset.get(position).getExamid(), mdataset.get(position).getLanguages());
        Log.e("array","array "+array.toString());
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);
        } else {
            if (array.length() > 0) {
                genloading("loading");
                StructureClass.clearAll();
                filesdata.clear();
                createfiles(array);
            } else {
                if (!((Activity) context).isFinishing()) {
                    Toast.makeText(getApplicationContext(), "No data available", Toast.LENGTH_LONG).show();
                }else {
                    Log.e("no activity","no activity ");
                }
            }
        }
    }

    public void createfiles(JSONArray array) {
        try {
           // JSONObject finalobj = new JSONObject();
            //if (array.length() > 0) {
              /*  try {
                    JSONObject rootobj = new JSONObject();
                    JSONObject mainobj = new JSONObject();
                    JSONArray mainarray = new JSONArray();
                    for (int i = 0; i < array.length(); i++) {
                        try {
                            JSONObject obj1 = new JSONObject();
                            obj1.put("survey", array.getJSONArray(i));
                            mainarray.put(obj1);
                            Log.e("data", "data " + mainobj.toString());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    rootobj.put("subroot", mainarray);
                    mainobj.put("root", rootobj);
                    finalobj.put("data", mainobj.toString());
                    Log.e("data ", "data " + finalobj.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }*/
                File f = File.createTempFile(Constants.FILE_TEMP_NAME,
                        Constants.FILE_NAME_EXT1,
                        new File(StructureClass.generate()));
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
                JSONArray array1 = new JSONArray();
               /* if (array.getJSONArray(array.length() - 1).length() > 0) {
                    int lastid = array.getJSONArray(array.length() - 1).getJSONObject(0).getInt("userid");
                    array1 = DatabaseHelper.getInstance(ExamListActivity.this).getAllData(lastid);
                }
                if (array1.length() > 0) {
                    createfiles(array1);
                } else {
                    sendmail();
                }*/
                sendmail();
          //  }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendmail() {
        try {
            final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND_MULTIPLE);
            emailIntent.setType("text/plain");
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Survey Data");
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
            selectedposition=-1;
        } catch (Exception e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    public void gotoAction(int position) {
        Intent iprogrprt = new Intent(ExamListActivity.this, ProgressReportActivity.class);
        iprogrprt.putExtra("progressreport",mdataset);
        iprogrprt.putExtra("flag",2);
        startActivity(iprogrprt);
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            for (int i=0;i<mdataset.size();i++) {
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
                    //mdataset.get(i).setTotal_questions(obj.getString(PracticeDatabaseHelper.QUESTION));
                }
            }
            adapter.notifyDataSetChanged();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
