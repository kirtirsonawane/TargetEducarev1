package com.targeteducare;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LevelListDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.targeteducare.Classes.Exam;
import com.targeteducare.Classes.MyPackages;
import com.targeteducare.Classes.QuestionURL;
import com.targeteducare.Classes.Student;
import com.targeteducare.database.DatabaseHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class Activitycommon extends AppCompatActivity {
    Toolbar toolbar;
    Context context;
    ProgressDialog dialog = null;
    String tag = "";
    float mHeadingFontSize = 20.0f;
    float mValueFontSize = 20.0f;
    String lang = "";
    private FirebaseAnalytics mFirebaseAnalytics;
    public static String type = "";

    SharedPreferences preferences;
    SharedPreferences.Editor edit;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        // getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        super.onCreate(savedInstanceState, persistentState);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
    }

    public void setmaterialDesign() {
        try {
            tag = "ActivityCommon";
            toolbar = findViewById(R.id.toolbar);
            toolbar.setTitleTextColor(getResources().getColor(R.color.White));
            setSupportActionBar(toolbar);
            toolbar.setLogo(R.mipmap.ic_launcher);
            //toolbar.addView();
            LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(responseRec, new IntentFilter(Constants.BROADCAST_WIZARD));
            context = this;
        } catch (Exception e) {
            reporterror(tag, e.toString());
            e.printStackTrace();
        }
    }

    public void setmaterialDesignv1() {
        try {
            tag = "ActivityCommon";
            toolbar = findViewById(R.id.toolbar);
            toolbar.setTitleTextColor(getResources().getColor(R.color.White));
            setSupportActionBar(toolbar);
            LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(responseRec, new IntentFilter(Constants.BROADCAST_WIZARD));
            context = this;
        } catch (Exception e) {
            reporterror(tag, e.toString());
            e.printStackTrace();
        }
    }

    public void setmaterialDesign_my_package() {
        try {
            tag = "ActivityCommon";
            toolbar = findViewById(R.id.packages_toolbar_xm);
            toolbar.setTitleTextColor(getResources().getColor(R.color.White));
            setSupportActionBar(toolbar);
            //toolbar.setLogo(R.mipmap.ic_launcher);
            toolbar.setTitle("Packages");
            ((TextView) toolbar.findViewById(R.id.package_toolbar_textview_amount_sign)).setText("₹");
            ((TextView) toolbar.findViewById(R.id.package_toolbar_textview_payment)).setText("0.00");
            //toolbar.addView((Button)findViewById(R.id.package_toolbar_button_1));
            LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(responseRec, new IntentFilter(Constants.BROADCAST_WIZARD));
            context = this;
        } catch (Exception e) {
            reporterror(tag, e.toString());
            Log.e("error ", "error " + e.toString());
            e.printStackTrace();
        }
    }


    public void registerreceiver() {
        try {
            context = this;
            LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(responseRec, new IntentFilter(Constants.BROADCAST_WIZARD));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void unregisterreceiver() {
        try {
            LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(responseRec);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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
                    finish();
                }
            });
        } catch (Exception e) {
            reporterror(tag, e.toString());
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        try {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(responseRec);
        } catch (Exception e) {
            reporterror(tag, e.toString());
            e.printStackTrace();
        }
        super.onDestroy();
    }

    private BroadcastReceiver responseRec = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            try {
                Bundle b = intent.getExtras();
                int statuscode = b.getInt(Constants.BROADCAST_RESPONSE_CODE);
                int urlaccess = b.getInt(Constants.BROADCAST_URL_ACCESS);
                String data = "";
                if (b.containsKey(Constants.BROADCAST_DATA))
                    data = b.getString(Constants.BROADCAST_DATA);
                onResponsed(statuscode, urlaccess, data);
            } catch (Exception e) {
                reporterror(tag, e.toString());
                e.printStackTrace();
            }
        }
    };

    public void genloading(String msg) {
        try {
            if (!((Activity) this).isFinishing()) {
                dialog = ProgressDialog.show(this, msg, getResources().getString(R.string.dialog_please_wait));
                dialog.setCancelable(false);
            } else {
              //  Log.e("activity", "activity is not running genloading");
            }
        } catch (Exception e) {
            reporterror(tag, e.toString());
            e.printStackTrace();
        }
    }

    public void dismissLoading() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    protected void onResponsed(int statuscode, int accesscode, String data) {
        try {
            if (dialog != null) {
                dialog.dismiss();
            }
            dismissLoading();
            /*if (statuscode == Constants.STATUS_OK) {
                if (accesscode == Connection.GET_PATIENTLABRESULTS.ordinal()) {

                } else if (accesscode == Connection.GET_BILLING.ordinal()) {

                } else if (accesscode == Connection.GET_PAYMENTS.ordinal()) {

                }
            }*/
        } catch (Exception e) {
            reporterror(tag, e.toString());
            e.printStackTrace();
        }
    }

    public boolean cangiveexam(final Exam exam1) {
        final boolean[] cangive = new boolean[1];
        cangive[0] = true;

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(Constants.firebasedbname);
     /*   if (exam1.getIspaid() == 1) {
            gotopracticeexam(exam1);
        } else {*/
        databaseReference.child(GlobalValues.student.getMobile() + "/Exam").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    if (dataSnapshot != null) {
                        if (dataSnapshot.getValue() != null) {
                            if (dataSnapshot.exists()) {
                                //  HashMap<String, Object> data = ((HashMap<String, Object>) dataSnapshot.getValue());
                                ArrayList<Exam> examdata = new ArrayList<>();
                                for (DataSnapshot postSnapShot : dataSnapshot.getChildren()) {
                                    Exam exam = postSnapShot.getValue(Exam.class);
                                    if (exam.getType().equalsIgnoreCase("Mock Test") || exam.getType().equalsIgnoreCase("Practice Test")) {
                                        examdata.add(exam);
                                    }
                                }
                                if (examdata.size() < 5) {
                                    // if (data.get(Integer.toString(exam1.getExamid())) != null) {
                                   /* if (ActivityCompat.checkSelfPermission(Activitycommon.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                        ActivityCompat.requestPermissions(Activitycommon.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);
                                    } else {
                                      //  gotopracticeexam(exam1);
                                    }*/
                                    // }  /*else
                                    //  Toast.makeText(getApplicationContext(), "Please buy exam package", Toast.LENGTH_LONG).show();*/
                                } else {
                                     /*   ArrayList<Exam> examdata=new ArrayList<>();
                                        for (DataSnapshot postSnapShot : dataSnapshot.getChildren()) {
                                            Exam exam = postSnapShot.getValue(Exam.class);
                                            if(exam.getType().equalsIgnoreCase("Mock Test") || exam.getType().equalsIgnoreCase("Practice Test")) {
                                                examdata.add(exam);
                                            }
                                        }*/
                                    if (examdata.size() >= 5) {
                                        int i = 0;
                                        for (i = 0; i < examdata.size(); i++) {
                                            if (exam1.getExamid() == examdata.get(i).getExamid()) {
                                                // gotopracticeexam(exam1);
                                                break;
                                            }
                                        }
                                        if (i >= examdata.size()) {
                                            cangive[0] = false;
                                            Toast.makeText(getApplicationContext(), "please buy package " + cangive[0], Toast.LENGTH_LONG).show();
                                        }
                                    } else {
                                        // gotopracticeexam(exam1);
                                    }
                                       /* ArrayList<Exam> examdata=new ArrayList<>();
                                        for (DataSnapshot postSnapShot : dataSnapshot.getChildren()) {
                                            Log.e("models ", "data 1" + postSnapShot.toString());
                                            Exam exam = postSnapShot.getValue(Exam.class);
                                            examdata.add(exam);
                                        }

                                        for (int i=0;i<examdata.size();i++)
                                        {
                                            Log.e("examdata ","examdata "+exam.getExam_type()+" "+exam.getExamid());
                                        }*/
                                    //gotopracticeexam(exam);
                                }
                            } else {
                                // gotopracticeexam(exam1);

                            }
                        } else {
                            //  gotopracticeexam(exam1);
                            Log.e("nt exists ", "nt exists2" + dataSnapshot.toString());
                        }
                    } else {
                        //  gotopracticeexam(exam1);
                        Log.e("nt exists ", "nt exists1 ");
                    }

                } catch (Exception e) {

                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Toast.makeText(getApplicationContext(), "cangive " + cangive[0], Toast.LENGTH_LONG).show();

        return cangive[0];
    }

    public void buypackage() {
        try {
            final android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(this);
            alert.setTitle(R.string.readcarefully);
            alert.setMessage(R.string.limitfreepkg);
            alert.setPositiveButton(getResources().getString(R.string.buy), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    gotobuypackages();
                }
            });

            alert.setCancelable(true);
            final android.app.AlertDialog dialog1 = alert.create();
            if (!((Activity) context).isFinishing()) {
                dialog1.show();
            }
        } catch (Exception e) {
            reporterror(tag, e.toString());
        }
    }

    public void gotoexamActivity(final Exam exam1, final String type) {
        try {
            Log.e("Got into ", "activity common from mocktestadapter");
            boolean cangive = true;
            Log.e("type ", "type " + type);

            if (type.equalsIgnoreCase("Mock Test")) {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(Constants.firebasedbname);
                if (exam1.getIspaid() == 1) {
                    //gotopracticeexam(exam1);
                    gotoexam(exam1, type);
                } else {
                    databaseReference.child(GlobalValues.student.getMobile() + "/Exam").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            try {
                                if (dataSnapshot != null) {
                                    if (dataSnapshot.getValue() != null) {
                                        if (dataSnapshot.exists()) {
                                            //  HashMap<String, Object> data = ((HashMap<String, Object>) dataSnapshot.getValue());
                                            ArrayList<Exam> examdata = new ArrayList<>();
                                            for (DataSnapshot postSnapShot : dataSnapshot.getChildren()) {
                                                Exam exam = postSnapShot.getValue(Exam.class);
                                                if (exam.getType().equalsIgnoreCase("Mock Test") || exam.getType().equalsIgnoreCase("Practice Test")) {
                                                    examdata.add(exam);
                                                }
                                            }
                                            if (examdata.size() < 5) {
                                                // if (data.get(Integer.toString(exam1.getExamid())) != null) {
                                                if (ActivityCompat.checkSelfPermission(Activitycommon.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                                    ActivityCompat.requestPermissions(Activitycommon.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);
                                                } else {
                                                    gotoexam(exam1, type);
                                                    //  gotopracticeexam(exam1);
                                                }
                                                // }  /*else
                                                //  Toast.makeText(getApplicationContext(), "Please buy exam package", Toast.LENGTH_LONG).show();*/
                                            } else {
                                     /*   ArrayList<Exam> examdata=new ArrayList<>();
                                        for (DataSnapshot postSnapShot : dataSnapshot.getChildren()) {
                                            Exam exam = postSnapShot.getValue(Exam.class);
                                            if(exam.getType().equalsIgnoreCase("Mock Test") || exam.getType().equalsIgnoreCase("Practice Test")) {
                                                examdata.add(exam);
                                            }
                                        }*/
                                                if (examdata.size() >= 5) {
                                                    int i = 0;
                                                    for (i = 0; i < examdata.size(); i++) {
                                                        if (exam1.getExamid() == examdata.get(i).getExamid()) {
                                                            gotoexam(exam1, type);
                                                            break;
                                                        }
                                                    }
                                                    if (i >= examdata.size()) {
                                                        buypackage();
                                                    }
                                                } else {
                                                    gotoexam(exam1, type);
                                                }
                                       /* ArrayList<Exam> examdata=new ArrayList<>();
                                        for (DataSnapshot postSnapShot : dataSnapshot.getChildren()) {
                                            Log.e("models ", "data 1" + postSnapShot.toString());
                                            Exam exam = postSnapShot.getValue(Exam.class);
                                            examdata.add(exam);
                                        }

                                        for (int i=0;i<examdata.size();i++)
                                        {
                                            Log.e("examdata ","examdata "+exam.getExam_type()+" "+exam.getExamid());
                                        }*/
                                                //gotopracticeexam(exam);
                                            }
                                        } else {
                                            gotoexam(exam1, type);

                                        }
                                    } else {
                                        gotoexam(exam1, type);
                                        Log.e("nt exists ", "nt exists2" + dataSnapshot.toString());
                                    }
                                } else {
                                    gotoexam(exam1, type);
                                    Log.e("nt exists ", "nt exists1 ");
                                }

                            } catch (Exception e) {
                                gotoexam(exam1, type);

                                reporterror("ActivityCommon ",e.toString());
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            } else {
                if (exam1.getIspaid() == 1)
                    gotoexam(exam1, type);
                else buypackage();
            }

          /*  Log.e("cangive ","cangive "+cangive);
            if (cangive) {
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);
                } else {
                    Intent intent = new Intent(Activitycommon.this, ExamActivity.class);
                    intent.putExtra("exam", exam);
                    intent.putExtra("testtype", type);
                    startActivity(intent);
                }
            } else {
               buypackage();
            }*/
        } catch (Exception e) {
            gotoexam(exam1, type);
            reporterror(tag, e.toString());
        }
    }

    public void gotoexam(Exam exam, String type) {
        Log.e("exam ", "exam activity ");
        Intent intent = new Intent(Activitycommon.this, ExamActivity.class);
        intent.putExtra("exam", exam);
        intent.putExtra("testtype", type);
        startActivity(intent);
    }

    public void gotobuypackages() {
        try {
            Intent intent = new Intent(this, MyPackagesActivity.class);
            startActivity(intent);
        } catch (Exception e) {
            reporterror(tag, e.toString());
        }
    }

    public void opendialog(final Exam exam) {
        try {
            if (!((Activity) context).isFinishing()) {
                final AlertDialog.Builder alert = new AlertDialog.Builder(this);
                LayoutInflater inflater = getLayoutInflater();
                View alertLayout = inflater.inflate(R.layout.dialog_terms, null);
                alert.setView(alertLayout);
                final TextView txt = (TextView) alertLayout.findViewById(R.id.textview_terms);
                final CheckBox cb = (CheckBox) alertLayout.findViewById(R.id.checkbox_1);
                cb.setVisibility(View.VISIBLE);
                Button bt = (Button) alertLayout.findViewById(R.id.button_start);
                Html.ImageGetter imggetter = new Html.ImageGetter() {
                    @Override
                    public Drawable getDrawable(String s) {
                        LevelListDrawable d = new LevelListDrawable();
                        Drawable empty = getResources().getDrawable(R.mipmap.ic_launcher);
                        d.addLevel(0, 0, empty);
                        d.setBounds(0, 0, empty.getIntrinsicWidth(), empty.getIntrinsicHeight());
                        new LoadImage(txt, 1).execute(s, d);
                        return d;
                    }
                };

                txt.setText(Html.fromHtml(exam.getTerms(), imggetter, null));
                alert.setCancelable(true);
                if (!((Activity) context).isFinishing()) {
                    final AlertDialog dialog = alert.create();
                    dialog.show();
                }

                bt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (cb.isChecked()) {
                            Intent intent = new Intent(Activitycommon.this, ExamActivity.class);
                            intent.putExtra("exam", exam);
                            startActivity(intent);
                            if (dialog != null)
                                dialog.cancel();
                        } else {
                            Toast.makeText(getApplicationContext(), "Please accept Terms & Conditions", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            } else {
                Log.e("activity", "activity is not running genloading");
            }
        } catch (Exception e) {
            reporterror(tag, e.toString());
        }
    }

    public void gotoanswersheet(Exam exam, int flag) {
        try {
            if (flag == 0) {
                Intent intent = new Intent(Activitycommon.this, AnswersheetActivity.class);
                intent.putExtra("exam", exam);
                startActivity(intent);
                if (dialog != null)
                    dialog.cancel();
            } else {
                Intent intent = new Intent(Activitycommon.this, AnswersheetActivity.class);
                intent.putExtra("exam", exam);
                startActivity(intent);
            }
        } catch (Exception e) {
            reporterror(tag, e.toString());
            e.printStackTrace();
        }
    }

    public void downloaddata(final ArrayList<QuestionURL> qdata, final int examid, final String language, final String jsondata) {
        try {
            //   Log.e("download error  ", "download error1 " + qdata.size());
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        new DownloadImage(qdata, examid, language, jsondata).execute(qdata);
                    } catch (Exception e) {

                    }
                }
            });
            thread.start();
//            ZipManager.zip(StructureClass.generate(Integer.toString(examid)));
        } catch (Exception e) {
            reporterror(tag, e.toString());
            e.printStackTrace();
        }
    }

    public void gotopracticeActivitytoAttempt(final Exam exam1) {
        try {
            Log.e("Got into ", "attempt click practice test adapter");
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(Constants.firebasedbname);
            if (exam1.getIspaid() == 1) {
                gotopracticeexam(exam1);
            } else {
                databaseReference.child(GlobalValues.student.getMobile() + "/Exam").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        try {
                            if (dataSnapshot != null) {
                                if (dataSnapshot.getValue() != null) {
                                    if (dataSnapshot.exists()) {
                                        //  HashMap<String, Object> data = ((HashMap<String, Object>) dataSnapshot.getValue());
                                        ArrayList<Exam> examdata = new ArrayList<>();
                                        for (DataSnapshot postSnapShot : dataSnapshot.getChildren()) {
                                            Exam exam = postSnapShot.getValue(Exam.class);
                                            if (exam.getType().equalsIgnoreCase("Mock Test") || exam.getType().equalsIgnoreCase("Practice Test")) {
                                                examdata.add(exam);
                                            }
                                        }
                                        if (examdata.size() < 5) {
                                            // if (data.get(Integer.toString(exam1.getExamid())) != null) {
                                            if (ActivityCompat.checkSelfPermission(Activitycommon.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                                ActivityCompat.requestPermissions(Activitycommon.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);
                                            } else {
                                                gotopracticeexam(exam1);
                                            }
                                            // }  /*else
                                            //  Toast.makeText(getApplicationContext(), "Please buy exam package", Toast.LENGTH_LONG).show();*/
                                        } else {
                                     /*   ArrayList<Exam> examdata=new ArrayList<>();
                                        for (DataSnapshot postSnapShot : dataSnapshot.getChildren()) {
                                            Exam exam = postSnapShot.getValue(Exam.class);
                                            if(exam.getType().equalsIgnoreCase("Mock Test") || exam.getType().equalsIgnoreCase("Practice Test")) {
                                                examdata.add(exam);
                                            }
                                        }*/
                                            if (examdata.size() >= 5) {
                                                int i = 0;
                                                for (i = 0; i < examdata.size(); i++) {
                                                    if (exam1.getExamid() == examdata.get(i).getExamid()) {
                                                        gotopracticeexam(exam1);
                                                        break;
                                                    }
                                                }
                                                if (i >= examdata.size()) {
                                                    buypackage();
                                                }
                                            } else {
                                                gotopracticeexam(exam1);
                                            }
                                       /* ArrayList<Exam> examdata=new ArrayList<>();
                                        for (DataSnapshot postSnapShot : dataSnapshot.getChildren()) {
                                            Log.e("models ", "data 1" + postSnapShot.toString());
                                            Exam exam = postSnapShot.getValue(Exam.class);
                                            examdata.add(exam);
                                        }

                                        for (int i=0;i<examdata.size();i++)
                                        {
                                            Log.e("examdata ","examdata "+exam.getExam_type()+" "+exam.getExamid());
                                        }*/
                                            //gotopracticeexam(exam);
                                        }
                                    } else {
                                        gotopracticeexam(exam1);

                                    }
                                } else {
                                    gotopracticeexam(exam1);
                                    Log.e("nt exists ", "nt exists2" + dataSnapshot.toString());
                                }
                            } else {
                                gotopracticeexam(exam1);
                                Log.e("nt exists ", "nt exists1 ");
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            reporterror("activityCommmon ",e.toString());
                            gotopracticeexam(exam1);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

       /* if (cangive) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);
            } else {
                Intent intent = new Intent(Activitycommon.this, PracticeActivity.class);
                intent.putExtra("exam", exam);
                startActivity(intent);
            }
        } else {
            Toast.makeText(getApplicationContext(), "Please buy exam package", Toast.LENGTH_LONG).show();
        }*/
            }
        } catch (Exception e) {
            gotopracticeexam(exam1);
            reporterror("ActivityCommon ", e.toString());
        }
    }

    public void gotopracticeexam(final Exam exam) {
        if (ActivityCompat.checkSelfPermission(Activitycommon.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Activitycommon.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);
        } else {
            Log.e("exam ", "practice activity ");
            Intent intent = new Intent(Activitycommon.this, PracticeActivity.class);
            intent.putExtra("exam", exam);
            //intent.putExtra("exam", exam.getExamid());
            startActivity(intent);
        }
    }

    public void gotoload_image(ArrayList<QuestionURL> mdataset, int srno) {
        try {
            Intent intent = new Intent(this, Imageenlargeactivity.class);
            intent.putExtra("data", mdataset);
            intent.putExtra("srno", srno);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class LoadImage extends AsyncTask<Object, Void, Bitmap> {
        //   private LevelListDrawable mDrawable;
        View v;
        int type;

        public LoadImage(View v, int type) {
            super();
            this.v = v;
            this.type = type;
            // do stuff
        }

        @Override
        protected Bitmap doInBackground(Object... params) {
            String source = (String) params[0];
            try {
                InputStream is = new URL(source).openStream();
                return BitmapFactory.decodeStream(is);
            } catch (FileNotFoundException e) {
                reporterror(tag, e.toString());
                e.printStackTrace();
            } catch (MalformedURLException e) {
                reporterror(tag, e.toString());
                e.printStackTrace();
            } catch (IOException e) {
                reporterror(tag, e.toString());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
          /*  if (bitmap != null) {
                BitmapDrawable d = new BitmapDrawable(bitmap);
                mDrawable.addLevel(1, 1, d);
                mDrawable.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
                mDrawable.setLevel(1);
                switch (type) {
                    case 0:
                        CharSequence t = ((TextView) v).getText();
                        ((TextView) v).setText(t);
                        break;
                    case 1:
                        CharSequence t1 = ((RadioButton) v).getText();
                        ((RadioButton) v).setText(t1);
                        break;
                    case 2:
                        CharSequence t2 = ((CheckBox) v).getText();
                        ((CheckBox) v).setText(t2);
                        break;
                }
            }*/
        }
    }

    public void downloadimage(Bitmap bitmap, String name) {

    }

    public class DownloadImage extends AsyncTask<ArrayList<QuestionURL>, Void, String> {
        String name = "";
        ArrayList<QuestionURL> qdata = new ArrayList<>();
        int examid = 0;
        String language = "";
        String jsondata = "";

        public DownloadImage(ArrayList<QuestionURL> qdata, int examid, String language, String jsondata) {
            this.qdata = qdata;
            this.examid = examid;
            this.language = language;
            this.jsondata = jsondata;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(ArrayList<QuestionURL>... URL) {
            ArrayList<QuestionURL> qdata = URL[0];
            for (int i = 0; i < qdata.size(); i++) {
                QuestionURL qurldata = qdata.get(i);
                String imageURL = qdata.get(i).getImagemainsource();
                // Log.e("image url ","image url "+imageURL);
                int index = imageURL.lastIndexOf("/");
                name = imageURL.substring((index + 1), imageURL.length());

                Bitmap bitmap = null;
                try {
                 /*   * InputStream input = new java.net.URL(imageURL).openStream();
                    bitmap = BitmapFactory.decodeStream(input);*/

                    java.net.URL url = new URL(imageURL);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    InputStream input = connection.getInputStream();
                    bitmap = BitmapFactory.decodeStream(input);
                    Log.e("bitmap abbbbbbbb", "bitmap " + bitmap);

                    StructureClass.defineContext(Activitycommon.this);
                    File file = new File(StructureClass.generate(Integer.toString(examid)));
                    file = new File(file, name);
                    try {
                        OutputStream stream = null;
                        stream = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        stream.flush();
                        stream.close();
                    } catch (IOException e) // Catch the exception
                    {
                        e.printStackTrace();
                    }
                    Uri savedImageURI = Uri.parse(file.getAbsolutePath());

                    ContentValues c = new ContentValues();
                    c.put(DatabaseHelper.ID, qurldata.getId());
                    c.put(DatabaseHelper.IMAGESOURCE, imageURL);
                    c.put(DatabaseHelper.SAVEDTIME, DateUtils.getSqliteTime());
                    c.put(DatabaseHelper.TYPE, qurldata.getType());
                    c.put(DatabaseHelper.OFFLINEPATH, file.getAbsolutePath());
                    DatabaseHelper.getInstance(context).savequestionurl(c, qurldata.getId(), qurldata.getType(), imageURL);
                    // Log.e("filepath ", "filepath " + savedImageURI);
                } catch (Exception e) {
                    //Log.e("download error  ", "download error " + e.toString());
                    e.printStackTrace();
                }
            }
            return "";
        }

        @Override
        protected void onPostExecute(String result) {
            Log.e(" jsondata.toString", jsondata.toString());
         /*   ContentValues c = new ContentValues();
            c.put(DatabaseHelper.JSONDATA, jsondata.toString());

            if (lang.equals("mr")) {
                language = "marathi";
            } else {
                language = "ENGLISH";
            }
            c.put(DatabaseHelper.LANGUAGE, language);
            c.put(DatabaseHelper.EXAMID, examid);
            c.put(DatabaseHelper.SAVEDTIME, DateUtils.getSqliteTime());
            long id = DatabaseHelper.getInstance(context).saveqdata(c, examid, language);*/
            //  Log.e("iddata", "idadata " + id);

            // downloadimage(result,name);
        }
    }

    /* public class DownloadImage extends AsyncTask<ArrayList<QuestionURL>, Void, String> {
         String name = "";
         ArrayList<QuestionURL> qdata = new ArrayList<>();
         int examid = 0;
         String language = "";
         String jsondata = "";

         public DownloadImage(ArrayList<QuestionURL> qdata, int examid, String language, String jsondata) {
             this.qdata = qdata;
             this.examid = examid;
             this.language = language;
             this.jsondata = jsondata;
         }

         @Override
         protected void onPreExecute() {
             super.onPreExecute();
         }

         @Override
         protected String doInBackground(ArrayList<QuestionURL>... URL) {
             ArrayList<QuestionURL> qdata = URL[0];
             for (int i = 0; i < qdata.size(); i++) {

                 QuestionURL qurldata = qdata.get(i);
                 String imageURL = qdata.get(i).getImagemainsource();
                 Log.e("image url ","image url "+imageURL);
                 int index = imageURL.lastIndexOf("/");
                 name = imageURL.substring((index + 1), imageURL.length());

                 Bitmap bitmap = null;
                 try {
                     InputStream input = new java.net.URL(imageURL).openStream();
                     bitmap = BitmapFactory.decodeStream(input);

                     java.net.URL url = new URL(imageURL);
                     HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                     connection.setDoInput(true);
                     connection.connect();
                     InputStream input = connection.getInputStream();
                     bitmap = BitmapFactory.decodeStream(input);
                     Log.e("bitmap abbbbbbbb", "bitmap " + bitmap);

                     StructureClass.defineContext(Activitycommon.this);
                     File file = new File(StructureClass.generate(Integer.toString(examid)));
                     file = new File(file, name);
                     try {
                         OutputStream stream = null;
                         stream = new FileOutputStream(file);
                         bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                         stream.flush();
                         stream.close();
                     } catch (IOException e) // Catch the exception
                     {
                         e.printStackTrace();
                     }
                     Uri savedImageURI = Uri.parse(file.getAbsolutePath());

                     ContentValues c = new ContentValues();
                     c.put(DatabaseHelper.ID, qurldata.getId());
                     c.put(DatabaseHelper.IMAGESOURCE, imageURL);
                     c.put(DatabaseHelper.SAVEDTIME, DateUtils.getSqliteTime());
                     c.put(DatabaseHelper.TYPE, qurldata.getType());
                     c.put(DatabaseHelper.OFFLINEPATH, file.getAbsolutePath());
                     c.put(DatabaseHelper.EXAMID,examid);
                     DatabaseHelper.getInstance(context).savequestionurl(c, qurldata.getId(), qurldata.getType(), imageURL);
                     Log.e("filepath ", "filepath " + savedImageURI);
                 } catch (Exception e) {
                     Log.e("download error  ", "download error " + e.toString());
                     e.printStackTrace();
                 }
             }
             return "";
         }

         @Override
         protected void onPostExecute(String result) {

             try {
                 Intent intent = new Intent(Constants.BROADCAST_WIZARD);
                 intent.putExtra(Constants.BROADCAST_RESPONSE_CODE, 200);
                 intent.putExtra(Constants.BROADCAST_URL_ACCESS, Connection.GET_QUESTIONS.ordinal());
                 LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
             } catch (Exception e) {
                 e.printStackTrace();
             }

             Log.e(" jsondata.toString", jsondata.toString());
             ContentValues c = new ContentValues();
             c.put(DatabaseHelper.JSONDATA, jsondata.toString());

             if (lang.equals("mr")) {
                 language = "marathi";
             } else {
                 language = "ENGLISH";
             }

             c.put(DatabaseHelper.LANGUAGE, language);
             c.put(DatabaseHelper.EXAMID, examid);
             c.put(DatabaseHelper.SAVEDTIME, DateUtils.getSqliteTime());
             long id = DatabaseHelper.getInstance(context).saveqdata(c, examid, language);
             //Log.e("iddata", "idadata " + id);
             try {
                 Intent intent = new Intent(Constants.BROADCAST_WIZARD);
                 intent.putExtra(Constants.BROADCAST_RESPONSE_CODE, 200);
                 intent.putExtra(Constants.BROADCAST_URL_ACCESS, Connection.GET_QUESTIONS.ordinal());
                 LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
             } catch (Exception e) {
                 e.printStackTrace();
             }
             // downloadimage(result,name);
         }
     }
 */
    public void setLocale(String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale; //configure as per language code
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
        editor.putString("Current Language", language);
        editor.apply();
    }

    public void loadLocale() {
        //Retrieving the stored values
        SharedPreferences pref = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        lang = pref.getString("Current Language", "");
        setLocale(lang);
    }

 /*   public void gotoAction(Exam exam) {
        Intent iprogrprt = new Intent(this, ProgressReportActivity.class);
        iprogrprt.putExtra("progressreport", exam);
        iprogrprt.putExtra("flag", 2);
        startActivity(iprogrprt);
    }*/

    public void gotoviewmarksheet(String url) {
        Intent iprogrprt = new Intent(this, WebViewActivity.class);
        iprogrprt.putExtra("url", url);
        startActivity(iprogrprt);
    }

    public int getos() {
        return android.os.Build.VERSION.SDK_INT;
    }

    public String getmodel() {
        return Build.MODEL + " " + Build.BRAND;
    }


    public void reporterror(String classname, String error) {
        try {
            JSONObject obj = new JSONObject();
            if (GlobalValues.student != null)
                obj.put("email", GlobalValues.student.getMobile());
            else obj.put("email", "");
            obj.put("osversion", getmodel() + " " + getos());
            obj.put("errordetail", error.replaceAll("'", ""));
            obj.put("appname", "Target Educare Peak " + GlobalValues.version);
            obj.put("activityname", classname);
            ConnectionManager.getInstance(this).reporterror(obj.toString());
            Log.e("error ", "error " + obj.toString());
        } catch (Exception e) {
            Log.e("error", "error " + e);
            e.printStackTrace();
        }
    }

    public void CheckUPdate() {
        VersionChecker versionChecker = new VersionChecker();
        try {
            String appVersionName = BuildConfig.VERSION_NAME;
            String mLatestVersionName = versionChecker.execute().get();
            Log.e("dataaa ", "dattaaa " + appVersionName + " " + mLatestVersionName);
            if (mLatestVersionName != null)
                if (!appVersionName.equals(mLatestVersionName)) {
                    if (!((Activity) context).isFinishing()) {
                        android.support.v7.app.AlertDialog.Builder alertDialog = new android.support.v7.app.AlertDialog.Builder(this);
                        alertDialog.setTitle("Please update app");
                        alertDialog.setMessage("This app version is no longer supported. Please update your app from the Play Store.");
                        alertDialog.setPositiveButton("UPDATE NOW", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                final String appPackageName = getPackageName();
                                try {
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                                } catch (android.content.ActivityNotFoundException anfe) {
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                                }
                            }
                        });
                        if (!((Activity) context).isFinishing()) {
                            alertDialog.setCancelable(false);
                            alertDialog.show();
                        }
                    }
                }

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    public class VersionChecker extends AsyncTask<String, String, String> {
        private String newVersion;

        @Override
        protected String doInBackground(String... params) {
            try {
                if (InternetUtils.getInstance(Activitycommon.this).available()) {
                    newVersion = Jsoup.connect("https://play.google.com/store/apps/details?id=" + getPackageName())
                            .timeout(30000)
                            .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                            .referrer("http://www.google.com")
                            .get()
                            .select(".hAyfc .htlgb")
                            .get(7)
                            .ownText();

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return newVersion;
        }
    }

    public void screenshot_capture_permission() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
    }

    public void opendialog(MyPackages packagedata) {
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.dialog_layout, null);
        alert.setView(alertLayout);
        WebView dialog_discp = (WebView) alertLayout.findViewById(R.id.webview_dialog_discription);
        TextView dialog_heading = (TextView) alertLayout.findViewById((R.id.textview_dialog_heading));
        dialog_heading.setText(context.getResources().getString(R.string.dialog_details));
        TextView Textview_dialog_Amount = (TextView) alertLayout.findViewById(R.id.textview_dialog_amount);

        Textview_dialog_Amount.setText("" + packagedata.getAmount());
        TextView text1 = (TextView) alertLayout.findViewById(R.id.textview_course_name);

        if (!lang.equalsIgnoreCase("mr"))
            text1.setText(Html.fromHtml(packagedata.getName()));
        else
            text1.setText(Html.fromHtml(packagedata.getName_InMarathi()));


        if (lang.equalsIgnoreCase("mr")) {
            Log.e("marathi ", " " + packagedata.getDescription_InMarathi());
            dialog_discp.loadData(packagedata.getDescription_InMarathi().replaceAll("/images/Uploadvideo", Constants.ippath), "text/html", "utf-8");
            //textView.setText("\" " + splashModels.get(position).getTitle_Marathi() + " \"");
        } else {
            Log.e("english ", " " + packagedata.getDescription());

            dialog_discp.loadData(packagedata.getDescription().replaceAll("/images/Uploadvideo", Constants.ippath), "text/html", "utf-8");
            // textView.setText("\" " + splashModels.get(position).getTitle() + " \"");
        }

        dialog_discp.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                view.loadData(url, "text/html", "utf-8");

                return true;
            }
        });
        alert.setCancelable(true);
        final AlertDialog dialog1 = alert.create();
        if (!((Activity) context).isFinishing()) {
            dialog1.show();
        }

        ImageView dialog_close = (ImageView) alertLayout.findViewById(R.id.dialog_close_button);
        dialog_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
            }
        });
    }


    public void sharelinkusingintent(String text) {
        try {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, text);
            startActivity(Intent.createChooser(intent, "Share Link!"));
        } catch (Exception e) {
            e.printStackTrace();
            reporterror(tag, e.toString());
        }
    }

    public void shareviawhatsapp(String text) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, text);
        sendIntent.setType("text/plain");
        sendIntent.setPackage("com.whatsapp");
        startActivity(Intent.createChooser(sendIntent, "Share Via Whatsapp"));
        startActivity(sendIntent);
    }

    public boolean appInstalledOrNot(String uri) {
        PackageManager pm = getPackageManager();
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getDistance(final double lat1, final double lon1, final double lat2, final double lon2) {
        final String parsedDistance = "";
        final JSONObject obj = new JSONObject();
        String response;
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("http://maps.googleapis.com/maps/api/directions/json?origin=" + lat1 + "," + lon1 + "&destination=" + lat2 + "," + lon2 + "&sensor=false&units=metric&mode=driving");
                    final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    InputStream in = new BufferedInputStream(conn.getInputStream());
                    String response = org.apache.commons.io.IOUtils.toString(in, "UTF-8");
                    Log.e("url", "url " + url);
                    Log.e("dist ", "dist " + response);
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray array = jsonObject.getJSONArray("routes");
                    JSONObject routes = array.getJSONObject(0);
                    JSONArray legs = routes.getJSONArray("legs");
                    JSONObject steps = legs.getJSONObject(0);
                    JSONObject distance = steps.getJSONObject("distance");
                    Log.e("dist", "dist " + distance.getString("text"));
                    ;
                    obj.put("distance", distance.getString("text"));
                    JSONObject duration = steps.getJSONObject("duration");
                    obj.put("duration", duration.getString("text"));
                } catch (MalformedURLException e) {
                    Log.e("error ", "error " + e);
                    e.printStackTrace();
                } catch (IOException e) {
                    Log.e("error1 ", "error1 " + e);
                    e.printStackTrace();
                } catch (JSONException e) {
                    Log.e("error2 ", "error2 " + e);
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return obj.toString();
    }

}
