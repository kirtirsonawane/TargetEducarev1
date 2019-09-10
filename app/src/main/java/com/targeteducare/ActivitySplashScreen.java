package com.targeteducare;
import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.targeteducare.Adapter.AdapterSplashScreen;
import com.targeteducare.Classes.SplashModel;
import com.targeteducare.Classes.Student;
import com.targeteducare.database.DatabaseHelper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ActivitySplashScreen extends Activitycommon {
    private ViewPager viewPager;
    private int delay = 3000;
    AdapterSplashScreen splashAdapter;
    private LinearLayout dotsLayout;
    private TextView[] dots;
    Animation animFadein, animFadeout;
    protected ArrayList<SplashModel> layouts = new ArrayList<>();
    private Button btnSkip, btnNext, bntBack;
    public int dot_position = 0;
    private boolean flag = false;
    CountDownTimer timer;
    RelativeLayout relativeLayout;
    DatabaseHelper databaseHelper;
    boolean isdataset = false;
    boolean isnotified = false;

    SharedPreferences preferences;
    SharedPreferences.Editor edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            screenshot_capture_permission();
            super.onCreate(savedInstanceState);
            loadLocale();
            setContentView(R.layout.activity_splash_screen);
            setmaterialDesign();
            toolbar.setTitleMargin(25, 10, 10, 10);
            setTitle("PEAK");
            GlobalValues.langs = getSharedPreferences("Settings", MODE_PRIVATE).getString("Current Language", "");

            /*preferences = PreferenceManager.getDefaultSharedPreferences(ActivitySplashScreen.this);
            edit = preferences.edit();

            if (preferences.getBoolean("isloginv1", false)) {
                Gson gson = new Gson();
                Type type = new TypeToken<Student>() {
                }.getType();
                GlobalValues.student = gson.fromJson(preferences.getString("studentdetails", ""), type);
                Intent intent = new Intent(ActivitySplashScreen.this, LoginV1Activity.class);
                intent.putExtra("isnotified",isnotified);
                startActivity(intent);
                Log.e("login v1", "login v1");
                this.finish();
            }*/

            databaseHelper = new DatabaseHelper((ActivitySplashScreen.this));
            viewPager = (ViewPager) findViewById(R.id.view_pager);
            dotsLayout = (LinearLayout) findViewById(R.id.dot_layout);
            btnSkip = (Button) findViewById(R.id.btn_skip);
            btnNext = (Button) findViewById(R.id.btn_next);
            bntBack = (Button) findViewById(R.id.btn_back);
            relativeLayout = (RelativeLayout) findViewById(R.id.relativelyt);
            checkConnection();
            Bundle b=getIntent().getExtras();
            if(b!=null)
            {
                if(b.containsKey("isnotified"))
                    isnotified=b.getBoolean("isnotified");
            }

            btnNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (dot_position < layouts.size()) {
                            dot_position = viewPager.getCurrentItem();
                            viewPager.setCurrentItem(++dot_position, true);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

      /*  animFadein = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.fadein);
        animFadeout = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.fadeout);
        animFadein.setAnimationListener(this);
        animFadeout.setAnimationListener(this);*/
            btnSkip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  /*  Intent intent=new Intent(ActivitySplashScreen.this,GridMainActivity.class);
                    startActivity(intent);
                    finish();*/
                    try {
                        flag = true;
                        gotonext();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });

            bntBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (dot_position < layouts.size()) {
                            dot_position = viewPager.getCurrentItem();
                            viewPager.setCurrentItem(--dot_position, true);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });


            relativeLayout.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    int i = 0;
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        i = 0;
                        try {
                            stoptimer(i);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return false;
                    }

                    if (event.getAction() == MotionEvent.ACTION_SCROLL) {
                        i = 0;
                        try {
                            stoptimer(i);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return false;
                    } else/* if (event.getAction() == MotionEvent.ACTION_DOWN)*/ {
                        try {
                            i = 1;
                            stoptimer(i);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        return true;
                    }

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void checkConnection() {
        Log.e("check connection ", "check connection ");
        if (isOnline()) {
            Log.e("check connection ", "check connection isonline");
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("Type", "Announcement");
                JSONObject jsonObject1 = new JSONObject();
                jsonObject1.put("FilterParameter", jsonObject.toString());
                genloading("loading..");
                ConnectionManager.getInstance(ActivitySplashScreen.this).getimages(jsonObject1.toString());

            } catch (JSONException e) {
                e.printStackTrace();
            }


        } else {
            Toast.makeText(ActivitySplashScreen.this, getResources().getString(R.string.Connectiontimeout), Toast.LENGTH_SHORT).show();

            try {
                JSONArray jsonArray = databaseHelper.loaddata();

                if (jsonArray.length() == 0) {

                    gotonext();

                } else if (jsonArray.length() > 0) {


                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONArray jsonArray1 = jsonArray.optJSONArray(i);


                        if (jsonArray1 == null) {
                            JSONObject object1 = jsonArray.optJSONObject(i);

                            if (object1 == null) {

                                gotonext();

                            } else if (object1 != null) {
                                for (int i1 = 0; i1 < jsonArray.length(); i1++) {


                                    JSONObject jsonObject5 = jsonArray.getJSONObject(i1);
                                    String splashdata = jsonObject5.getString("SPLASHDATA");
                                    JSONObject jsonObject3 = new JSONObject(splashdata);
                                    JSONObject jsonObject6 = jsonObject3.getJSONObject("root");
                                    JSONObject jsonObject4 = jsonObject6.optJSONObject("subroot");

                                    if (jsonObject4 != null) {
                                        SplashModel splashModel = new SplashModel();
                                        JSONObject object = jsonObject6.getJSONObject("subroot");
                                        splashModel.setId(object.getString("Id"));
                                        splashModel.setCreatedDate(object.getString("Type"));
                                        splashModel.setDescription(object.getString("Description"));
                                        splashModel.setFromDate(object.getString("FromDate"));
                                        splashModel.setTitle(object.getString("Title"));
                                        splashModel.setToDate(object.getString("ToDate"));
                                        splashModel.setType(object.getString("Type"));
                                        splashModel.setDescription_Marathi(object.getString("Description_Marathi"));
                                        splashModel.setTitle_Marathi(object.getString("Title_Marathi"));
                                        layouts.add(splashModel);
                                    } else if (jsonObject4 == null) {
                                        JSONArray jsonArray2 = jsonObject6.optJSONArray("subroot");
                                        if (jsonArray2 == null) {
                                            gotonext();
                                        } else if (jsonArray2 != null) {
                                            for (int j = 0; j < jsonArray2.length(); j++) {
                                                SplashModel splashModel = new SplashModel();
                                                JSONObject jsonObject1 = jsonArray2.getJSONObject(j);
                                                splashModel.setId(jsonObject1.getString("Id"));
                                                splashModel.setCreatedDate(jsonObject1.getString("Type"));
                                                splashModel.setDescription(jsonObject1.getString("Description"));
                                                splashModel.setFromDate(jsonObject1.getString("FromDate"));
                                                splashModel.setTitle(jsonObject1.getString("Title"));
                                                splashModel.setToDate(jsonObject1.getString("ToDate"));
                                                splashModel.setType(jsonObject1.getString("Type"));
                                                splashModel.setDescription_Marathi(jsonObject1.getString("Description_Marathi"));
                                                splashModel.setTitle_Marathi(jsonObject1.getString("Title_Marathi"));
                                                layouts.add(splashModel);
                                            }
                                        }
                                    }
                                }
                                if (layouts.size() > 0) {
                                    btnSkip.setVisibility(View.VISIBLE);
                                }

                                splashAdapter = new AdapterSplashScreen(layouts, ActivitySplashScreen.this);
                                viewPager.setAdapter(splashAdapter);
                                viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                                    @Override
                                    public void onPageScrollStateChanged(int position) {

                                    }
                                    @Override
                                    public void onPageScrolled(int arg0, float arg1, int arg2) {
                                       /* try {
                                            Log.e("page ","onPageScrolled");
                                            if (arg0 == layouts.size()) {
                                                //     gotonext();
                                            } else {
                                                //  viewPager.startAnimation(animFadein);

                                                preparedot(arg0);
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }*/
                                        //timer.start();
                                    }

                                    @Override
                                    public void onPageSelected(int position) {
                                        // TODO Auto-generated method stub
                                        try {
                                            if (position == layouts.size()) {
                                                //     gotonext();
                                            } else {
                                                //  viewPager.startAnimation(animFadein);

                                                preparedot(position);
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }

                                });
                                //initButton();
                                /* preparedot(dot_position++);*/
                                getcountdown(layouts.size());
                            }
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }


    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void onResponsed(int statuscode, int accesscode, String data) {
        super.onResponsed(statuscode, accesscode, data);
        if (statuscode == Constants.STATUS_OK) {
            if (accesscode == Connection.SPLASHSCREEN1.ordinal()) {
                data = GlobalValues.TEMP_STR;
                if (data != null) {
                    //Log.e("Globalvalues ", "+ " + data);
                    try {
                        JSONObject jsonObject2 = new JSONObject(data);
                        JSONObject jsonObject = new JSONObject();
                        JSONObject jsonObject1 = new JSONObject();
                        jsonObject = jsonObject2.getJSONObject("root");

                        JSONObject object = jsonObject.optJSONObject("subroot");

                        if (object != null) {
                            jsonObject1 = jsonObject.getJSONObject("subroot");
                            SplashModel splashModel = new SplashModel();


                            if (jsonObject1.has("Id")) {
                                splashModel.setId(jsonObject1.getString("Id"));
                            } else {
                                splashModel.setId("");
                            }


                            if (jsonObject1.has("Type")) {
                                splashModel.setType(jsonObject1.getString("Type"));
                            } else {
                                splashModel.setType("");
                            }


                            if (jsonObject1.has("Description")) {
                                splashModel.setDescription(jsonObject1.getString("Description"));
                            } else {
                                splashModel.setDescription("Not available");
                            }


                            if (jsonObject1.has("FromDate")) {
                                splashModel.setFromDate(jsonObject1.getString("FromDate"));
                            } else {
                                splashModel.setFromDate("Not available");
                            }


                            if (jsonObject1.has("Title")) {
                                splashModel.setTitle(jsonObject1.getString("Title"));
                            } else {
                                splashModel.setTitle("Not available");
                            }


                            if (jsonObject1.has("ToDate")) {
                                splashModel.setToDate(jsonObject1.getString("ToDate"));
                            } else {
                                splashModel.setToDate("Not available");
                            }


                            if (jsonObject1.has("CreatedDate")) {
                                splashModel.setCreatedDate(jsonObject1.getString("CreatedDate"));
                            } else {
                                splashModel.setCreatedDate("Not available");
                            }


                            if (jsonObject1.has("Description_Marathi")) {
                                splashModel.setDescription_Marathi(jsonObject1.getString("Description_Marathi"));
                            } else {
                                splashModel.setDescription_Marathi("Not available");
                            }


                            if (jsonObject1.has("Title_Marathi")) {
                                splashModel.setTitle_Marathi(jsonObject1.getString("Title_Marathi"));
                            } else {
                                splashModel.setTitle_Marathi("Not available");
                            }





                            /*else {
                                splashModel.setCreatedDate("");
                            }*/


                            layouts.add(splashModel);


                        } else if (object == null) {
                            JSONArray jsonArray = new JSONArray();
                            /*if (jsonObject2.has("subroot")) {*/
                            JSONArray jsonArray1 = jsonObject.optJSONArray("subroot");
                            if ((jsonArray1 != null)) {
                                jsonArray = jsonObject.getJSONArray("subroot");
                                //Log.e("in array", " subroot");
                                if (jsonArray.length() > 0) {
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject3 = jsonArray.getJSONObject(i);
                                        SplashModel splashModel = new SplashModel();

                                        if (jsonObject3.has("Id")) {
                                            splashModel.setId(jsonObject3.getString("Id"));
                                        } else {
                                            splashModel.setId("");
                                        }


                                        if (jsonObject3.has("Type")) {
                                            splashModel.setType(jsonObject3.getString("Type"));
                                        } else {
                                            splashModel.setType("");
                                        }


                                        if (jsonObject3.has("Description")) {
                                            splashModel.setDescription(jsonObject3.getString("Description"));
                                        } else {
                                            splashModel.setDescription("Not available");
                                        }


                                        if (jsonObject3.has("FromDate")) {
                                            splashModel.setFromDate(jsonObject3.getString("FromDate"));
                                        } else {
                                            splashModel.setFromDate("Not available");
                                        }

                                        if (jsonObject3.has("Title")) {
                                            splashModel.setTitle(jsonObject3.getString("Title"));
                                        } else {
                                            splashModel.setTitle("Not available");
                                        }


                                        if (jsonObject3.has("ToDate")) {
                                            splashModel.setToDate(jsonObject3.getString("ToDate"));
                                        } else {
                                            splashModel.setToDate("Not available");
                                        }


                                        if (jsonObject3.has("CreatedDate")) {
                                            splashModel.setCreatedDate(jsonObject3.getString("CreatedDate"));
                                        } else {
                                            splashModel.setCreatedDate("Not available");
                                        }


                                        if (jsonObject3.has("Description_Marathi")) {
                                            splashModel.setDescription_Marathi(jsonObject3.getString("Description_Marathi"));
                                        } else {
                                            splashModel.setDescription_Marathi("Not available");
                                        }


                                        if (jsonObject3.has("Title_Marathi")) {
                                            splashModel.setTitle_Marathi(jsonObject3.getString("Title_Marathi"));
                                        } else {
                                            splashModel.setTitle_Marathi("Not available");
                                        }


                                        layouts.add(splashModel);

                                    }
                                } else if (jsonArray.length() == 0) {
                                    /*stoptimer(1);*/
                                    gotonext();
                                }
                            } else {
                                gotonext();
                                  /*  Intent intent = new Intent(ActivitySplashScreen.this, ChooseLanguageActivity.class);
                                    startActivity(intent);
                                    timer.cancel();
                                    finish();
                                    super.finish();*/
                                /*stoptimer(1);*/
                            }


                        }/*else {
                                Log.e("layouts length ", " " );
                            }*/
                        insert(GlobalValues.TEMP_STR);
                        if (layouts.size() > 0) {
                            btnSkip.setVisibility(View.VISIBLE);
                        }
                        splashAdapter = new AdapterSplashScreen(layouts, ActivitySplashScreen.this);
                        viewPager.setAdapter(splashAdapter);
                        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                            @Override
                            public void onPageScrollStateChanged(int position) {


                            }

                            @Override
                            public void onPageScrolled(int arg0, float arg1, int arg2) {

                                //timer.start();
                            }

                            @Override
                            public void onPageSelected(int position) {
                                // TODO Auto-generated method stub

                                try {

                                    if (position == layouts.size()) {
                                        //     gotonext();
                                    } else {
                                        //  viewPager.startAnimation(animFadein);

                                        preparedot(position);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                        });
                        //initButton();
                        /* preparedot(dot_position++);*/

                        getcountdown(layouts.size());

                    } catch (Exception e) {
                        gotonext();
                        e.printStackTrace();
                    }
                } else {

                }
                //}

            } else if (accesscode == Connection.SPLASHSCREENEXCEPTION1.ordinal()) {
                gotonext();
            }
        }
    }


    private void getcountdown(final int length) {

        timer = new CountDownTimer((length + 1) * 3000, 3000) {
            int i = 0;

            @Override
            public void onTick(long millisUntilFinished) {
                try {

                    viewPager.setCurrentItem(i, true);
                    setTab();
                    preparedot(viewPager.getCurrentItem());
                    splashAdapter.notifyDataSetChanged();
                    i++;

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }


            @Override
            public void onFinish() {
                   /* Intent intent = new Intent(ActivitySplashScreen.this, GridMainActivity.class);
                    startActivity(intent);
                    finish();*/
                //  cancel();
                try {
                    gotonext();
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

        }.start();

    }

    @Override
    protected void onResume() {
        try {
            if (isdataset)
                timer.start();
        } catch (Exception e) {
            e.printStackTrace();

        }
        super.onResume();
    }

    public void stoptimer(int i) {
        try {
            if (i == 1) {
                if (timer != null) {
                    timer.cancel();
                }

                // timer.wait();
            } else {
                viewPager.getCurrentItem();

                timer.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

   /*      viewPager.setOnTouchListener(new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            Log.e("touched ","");


            timer.cancel();
            return false;
        }
    });*/


/*
    private void initButton() {
        try {
            _btn1 = (Button) findViewById(R.id.btn1);
            _btn2 = (Button) findViewById(R.id.btn2);
            _btn3 = (Button) findViewById(R.id.btn3);
            _btn4 = (Button) findViewById(R.id.btn4);

            relativeLayout.setBackgroundColor(getResources().getColor(R.color.yellow));
            setButton(_btn1, "1", 60, 60);
            setButton(_btn2, "", 30, 30);
            setButton(_btn3, "", 30, 30);
            setButton(_btn4, "", 30, 30);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    private void setTab() {


    }


    @Override
    protected void onDestroy() {
        // this.finish();
        try {
            /*stoptimer(1);*/
            if (timer != null)
                timer.cancel();

        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    /*@Override
    protected void onResume() {
       try{
           timer.cancel();
       }catch (Exception e){
           e.printStackTrace();
       }
        super.onResume();
    }*/

    @Override
    public void onBackPressed() {
        try {
            if (timer != null)
                timer.cancel();
        } catch (Exception e) {
            e.printStackTrace();
        }

        super.onBackPressed();
    }


    @Override
    protected void onPause() {

        try {
            if (timer != null)
                timer.cancel();
            isdataset = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onPause();
    }

    private void preparedot(int currentposition) {

        try {
            setTab();

            TextView dots[];


            dots = new TextView[layouts.size()];

            dotsLayout.removeAllViews();
            for (int i = 0; i < dots.length; i++) {
                dots[i] = new TextView(this);
                dots[i].setText(Html.fromHtml("&#8226;"));
                dots[i].setTextSize(35);
                dots[i].setTextColor(Color.parseColor("#000000"));
                dotsLayout.addView(dots[i]);

            }


            if (dots.length > 0) {
                dots[currentposition].setTextColor(Color.parseColor("#FB1A07"));
            }


            if (viewPager.getCurrentItem() == (layouts.size() - 1)) {
                btnNext.setVisibility(View.GONE);
                //Log.e(" length1 ", " " + layouts.size() + " ");
            } else {
                btnNext.setVisibility(View.VISIBLE);
            }
            if (viewPager.getCurrentItem() == 0) {
                bntBack.setVisibility(View.GONE);
            } else {
                bntBack.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* @Override
     public void onAnimationStart(Animation animation) {

     }

     @Override
     public void onAnimationEnd(Animation animation) {

     }

     @Override
     public void onAnimationRepeat(Animation animation) {

     }*/
    boolean isnext = false;

    public void gotonext() {
        try {

            // if (!isnext) {

            /*if (rb_english.isChecked()) {
                Log.e("Default ", "English");
                setLocale("En");
                Intent ilogin = new Intent(ChooseLanguageActivity.this, ActivitySplashScreen.class);
                startActivity(ilogin);
            } else if (id_checked == -1) {
                Toast.makeText(ChooseLanguageActivity.this, getResources().getString(R.string.toast_selectlanguage), Toast.LENGTH_SHORT).show();
            } else {
                Intent ilogin = new Intent(ChooseLanguageActivity.this, ActivitySplashScreen.class);
                startActivity(ilogin);
            }
        }
    });*/


            isnext = true;
            Log.e("isnotified ","isnotified in splash"+isnotified);
            Intent intent = new Intent(ActivitySplashScreen.this, LoginV1Activity.class);
            intent.putExtra("isnotified",isnotified);
            startActivity(intent);
            int i = 0;

            //   finish();
            if (timer != null) {
                timer.cancel();
            }

            Log.e("error ", "error finish called");
            finish();
            //   }
        } catch (Exception e) {
            Log.e("error ", "error " + e.toString());
            e.printStackTrace();
        }
    }


    public void insert(String datatodatabase) {

        try {
            ContentValues c = new ContentValues();
            c.put(DatabaseHelper.SPLASH_DATA, datatodatabase);
            databaseHelper.insertsplashdata(c);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}












