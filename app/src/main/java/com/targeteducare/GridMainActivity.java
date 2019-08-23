package com.targeteducare;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.targeteducare.Adapter.AdapterNotification;
import com.targeteducare.Adapter.CustomAdapterforGridMain;
import com.targeteducare.Classes.Chat;
import com.targeteducare.Classes.Menu;
import com.targeteducare.Classes.QnaQuestionModel;
import com.targeteducare.Classes.Student;
import com.targeteducare.database.DatabaseHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GridMainActivity extends Activitycommon {
    private static ArrayList<QnaQuestionModel> dataQuestionmodel;
    private static ArrayList<Menu> data;
    private CustomAdapterforGridMain adapter;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerView1;
    Intent itest;
    String lang = "";
    SharedPreferences preferences;
    SharedPreferences.Editor edit;
    String currentVersion = "";
    StorageReference storageRef;
    DatabaseReference databaseReference;
    ArrayList<Chat> chatdata = new ArrayList<>();
    // long currentmillies = 0;
    String tag = "";

    AdapterNotification adapterNotification;
    ArrayList<String> chats = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            screenshot_capture_permission();
            super.onCreate(savedInstanceState);
            loadLocale();
            setContentView(R.layout.activity_grid_main);
          /*   currentVersion = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
             new GetVersionCode().execute();*/
            setTitle(Constants.TITLE);
            setmaterialDesign();
            //setTitle("Target Educare");
            toolbar.setTitleMargin(30, 10, 10, 10);
            GlobalValues.isopen = true;
            storageRef = FirebaseStorage.getInstance().getReference();
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
            } else {
                TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                String id = telephonyManager.getDeviceId();
                if (GlobalValues.student.getIEMIno().length() <= 0) {
                    GlobalValues.student.setIEMIno(id);
                }
            }

            try {
                if (InternetUtils.getInstance(GridMainActivity.this).available()) {
                    GlobalValues.currentmillies = System.currentTimeMillis();
                    databaseReference = FirebaseDatabase.getInstance().getReference(Constants.firebasedbname);
                    GlobalValues.student.setLastvisiteddate(DateUtils.getSqliteTime());
                    GlobalValues.student.setUseractive(1);
                    CheckUsers(GlobalValues.student.getMobile());


                   /* try {
                        Intent i = new Intent(GridMainActivity.this, BackgroundService.class);
                        startService(i);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }*/


                    check_chat_available(GlobalValues.student.getMobile());

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            //Log.e("Screen Width1 ", Resources.getSystem().getDisplayMetrics().widthPixels + "");
            //  Log.e("Screen Height ", Resources.getSystem().getDisplayMetrics().heightPixels + "");

            DatabaseHelper.getInstance(GridMainActivity.this).checkqurl();
            DatabaseHelper.getInstance(GridMainActivity.this).checklastq();
            try {
                if (GlobalValues.student != null) {
                    Crashlytics.setString("mobile", GlobalValues.student.getMobile());
                    Crashlytics.setUserIdentifier(GlobalValues.student.getMobile());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE}, 101);
            }

            preferences = PreferenceManager.getDefaultSharedPreferences(GridMainActivity.this);
            edit = preferences.edit();
            //   CheckUPdate();
            lang = getSharedPreferences("Settings", Activity.MODE_PRIVATE).getString("Current Language", "");

            if (preferences.getBoolean("isloginv1", false)) {
                edit.putBoolean("isloginv1", true);
                edit.apply();
            }

        /*viewmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iqnafeed = new Intent(GridMainActivity.this, QnAFeedActivity.class);
                startActivity(iqnafeed);
            }
        });*/

            // get the reference of RecyclerView
            RecyclerView recyclerView = findViewById(R.id.recyclerviewforgrid);
            int count = (int) (Resources.getSystem().getDisplayMetrics().widthPixels / 250);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
            recyclerView.setLayoutManager(gridLayoutManager); // set LayoutManager to RecyclerView
            data = new ArrayList<Menu>();

            // for (int i = 0; i < ArrData.gridnames.length; i++) {

            /*"Practice Test", "Mock Test", "Monthly Ranking Test", "Context Test",
                    "E-Books", "Predicators", "Chat-Bot", "Profile","Colleges","QnA", "Subscription"*/
            //String s=getResources().getString(R.string.practice_test);
            // ArrayList<String> gridnames = new ArrayList<>();
            /*    gridnames.add(getResources().getString(R.string.practice_test));
                gridnames.add(getResources().getString(R.string.mock_test));
                gridnames.add(getResources().getString(R.string.monthly_test));
                gridnames.add(getResources().getString(R.string.context_test));
                //   gridnames.add(getResources().getString(R.string.e_books));
                // gridnames.add(getResources().getString(R.string.news));
                // gridnames.add(getResources().getString(R.string.support));
                gridnames.add(getResources().getString(R.string.profile));
                gridnames.add(getResources().getString(R.string.announcements));
                // gridnames.add(getResources().getString(R.string.qna));
                gridnames.add(getResources().getString(R.string.subscription));
                gridnames.add(getResources().getString(R.string.chat));
                Menu arrData = new Menu(gridnames.get(i), ArrData.drawableArray[i]);*/

            data.add(new Menu(getResources().getString(R.string.practice_test), R.mipmap.practice_test));
            data.add(new Menu(getResources().getString(R.string.mock_test), R.mipmap.mock_test));
            data.add(new Menu(getResources().getString(R.string.monthly_test), R.mipmap.ranking_test));
            data.add(new Menu(getResources().getString(R.string.context_test), R.mipmap.contest_test));
            //   gridnames.add(getResources().getString(R.string.e_books));
            // gridnames.add(getResources().getString(R.string.news));
            // gridnames.add(getResources().getString(R.string.support));
            data.add(new Menu(getResources().getString(R.string.profile), R.mipmap.profile));
            data.add(new Menu(getResources().getString(R.string.news), R.mipmap.news));
            data.add(new Menu(getResources().getString(R.string.announcements), R.mipmap.annoucement));
            data.add(new Menu(getResources().getString(R.string.qna), R.mipmap.qna));
            data.add(new Menu(getResources().getString(R.string.subscription), R.mipmap.subscription));
            data.add(new Menu(getResources().getString(R.string.chat), R.mipmap.chat_bot));

            if (GlobalValues.student.getIsFaculty().equalsIgnoreCase("1"))
                data.add(new Menu(getResources().getString(R.string.feedback), R.mipmap.ebooks));
            //}
            adapter = new CustomAdapterforGridMain(GridMainActivity.this, data);
            recyclerView.setAdapter(adapter);


        /*recyclerView1 = findViewById(R.id.recyclerviewforqna);
        recyclerView1.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView1.setLayoutManager(layoutManager);
        dataQuestionmodel = new ArrayList<QnaQuestionModel>();
        for(int i1 = 0; i1< QnaQuestionData.main_question.length; i1++){

            QnaQuestionModel qdata = new QnaQuestionModel(
                    QnaQuestionData.main_question[i1],QnaQuestionData.followers[i1],QnaQuestionData.answers[i1]);

            ArrayList<QnaDataModel> datamod = new ArrayList<>();

            for(int j1 = 0; j1< QnAData.profile_pics.length; j1++){

                QnaDataModel qdatamod = new QnaDataModel(
                        QnAData.profile_pics[j1],QnAData.name[j1],QnAData.time[j1],QnAData.paragraphs[j1]);
                datamod.add(qdatamod);
            }

            qdata.setQndataset(datamod);
            dataQuestionmodel.add(qdata);
        }
        adapter = new QnaQuestionAdapter(GridMainActivity.this,dataQuestionmodel);
        recyclerView1.setAdapter(adapter);*/


            Bundle b = getIntent().getExtras();
            if (b != null) {
                if (b.containsKey("isnotified")) {
                    if (b.getBoolean("isnotified")) {
                        Log.e("isnotified ", "isnotified in splash true");
                        Intent intent = new Intent(GridMainActivity.this, NotificationActivity.class);
                        startActivity(intent);
                    } else Log.e("isnotified ", "isnotified in grid false");
                }
            }

        } catch (Exception e) {
            reporterror(tag, e.toString());
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
      /*  try {
            long totalmillies = System.currentTimeMillis() - currentmillies;
            long timetaken = GlobalValues.student.getTimetaken() + (totalmillies / 1000);
            Log.e("timetaken ", " " + timetaken);
            GlobalValues.student.setTimetaken(timetaken);
            addtofirebasedb();
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        super.onDestroy();
    }

    /*private void setLocale(String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale; //configure as per language code
        getBaseContext().getResources().updateConfiguration(config,getBaseContext().getResources().getDisplayMetrics());
        SharedPreferences.Editor editor = getSharedPreferences("Settings",MODE_PRIVATE).edit();
        editor.putString("Current Language",language);
        editor.apply();
    }

    public void loadLocale() {
        //Retrieving the stored values
        SharedPreferences pref = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String lang = pref.getString("Current Language","");
        setLocale(lang);
    }*/

    @Override
    protected void onResume() {
        super.onResume();
        try {
            databaseReference = FirebaseDatabase.getInstance().getReference(Constants.firebasedbname);
            databaseReference.child(GlobalValues.student.getMobile()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    try {
                        if (dataSnapshot != null) {
                            if (dataSnapshot.getValue() != null) {
                                if (dataSnapshot.exists()) {
                                    Log.e("exists ", "exists " + dataSnapshot.toString());
                                    Student s = dataSnapshot.getValue(Student.class);

                                    if (s.getIEMIno().length() > 0) {
                                        if (!s.getIEMIno().equalsIgnoreCase(GlobalValues.student.getIEMIno())) {

                                            showdialog();
                                            return;
                                        }
                                    }
//                                            GlobalValues.student.setTimetaken(s.getTimetaken());
//                                            GlobalValues.student.setLastvisiteddate(DateUtils.getSqliteTime());
                                    // addtofirebasedb(0);
                                } else {
                                    // Log.e("nt exists ", "nt exists " + dataSnapshot.toString());
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
            CheckUPdate();
        } catch (Exception e) {
            reporterror(tag, e.toString());
        }
    }

    public void referto(int position) {
        try {
        /*Test Names to pass
        Mock Test
        Monthly Practice Test
        Practice Test
        Context Test*/
            switch (position) {
                case R.mipmap.practice_test:
                    try {
                        itest = new Intent(GridMainActivity.this, PracticeTestSelectActivity.class);
                        GlobalValues.selectedtesttype = "Practice Test";
                        if (lang.equals("mr")) {
                            itest.putExtra("testtype", "Practice Test");
                        } else {
                            itest.putExtra("testtype", "Practice Test");
                        }
                        startActivity(itest);
                    } catch (Exception e) {
                        reporterror(tag, e.toString());
                        e.printStackTrace();
                    }
                    break;

                case R.mipmap.mock_test:
                    try {
                        itest = new Intent(GridMainActivity.this, MockTestActivity.class);
                        GlobalValues.selectedtesttype = "Mock Test";
                        if (lang.equals("mr")) {
                            itest.putExtra("testtype", "Mock Test");
                        } else {
                            itest.putExtra("testtype", "Mock Test");
                        }
                        startActivity(itest);
                    } catch (Exception e) {
                        reporterror(tag, e.toString());
                        e.printStackTrace();
                    }
                    break;

                case R.mipmap.ranking_test:
                    try {
                        itest = new Intent(GridMainActivity.this, ExamListActivity.class);
                        GlobalValues.selectedtesttype = "Monthly Ranking Test";
                        if (lang.equals("mr")) {
                            itest.putExtra("testtype", "Monthly Ranking Test");
                        } else {
                            itest.putExtra("testtype", "Monthly Ranking Test");
                        }
                        startActivity(itest);
                    } catch (Exception e) {
                        reporterror(tag, e.toString());
                        e.printStackTrace();
                    }
                    break;

                case R.mipmap.contest_test:
                    try {
                        itest = new Intent(GridMainActivity.this, ExamListActivity.class);
                        GlobalValues.selectedtesttype = "Context Test";
                        if (lang.equals("mr")) {
                            itest.putExtra("testtype", "Context Test");
                        } else {
                            itest.putExtra("testtype", "Context Test");
                        }
                        startActivity(itest);
                    } catch (Exception e) {
                        reporterror(tag, e.toString());
                        e.printStackTrace();
                    }
                    break;

          /*  case 4:
                Intent ismppaper = new Intent(GridMainActivity.this, SamplePapersActivity.class);
                startActivity(ismppaper);
                break;

            case 5:
                break;

            case 6:
                Intent imainsubjselect = new Intent(GridMainActivity.this, MainSubjectSelectionActivity.class);
                startActivity(imainsubjselect);
                break;*/
                case R.mipmap.news:
                    Log.e("Ebbok ", "Ebook ");
                    Intent intent = new Intent(GridMainActivity.this, ActivityNews.class);
                    startActivity(intent);
                    break;
                case R.mipmap.profile:
                    Intent iprof = new Intent(GridMainActivity.this, UserProfileActivity.class);
                    startActivity(iprof);
                    break;

                case R.mipmap.annoucement:
                    Intent iengcolg = new Intent(GridMainActivity.this, EbookSubjectActivity.class);
                    startActivity(iengcolg);
                    break;

                case R.mipmap.qna:
                    Intent iqna = new Intent(GridMainActivity.this, com.targeteducare.SupportActivity.class);
                    startActivity(iqna);
                    break;
                case 9:
                    Intent iqnafeed = new Intent(GridMainActivity.this, QnAFeedActivity.class);
                    startActivity(iqnafeed);
                    break;

                case R.mipmap.subscription:
                    Intent imypackage = new Intent(GridMainActivity.this, MyPackagesActivity.class);
                    startActivity(imypackage);
                    break;

                case R.mipmap.chat_bot:
                    Intent ichatbot = new Intent(GridMainActivity.this, ChatBotActivity.class);
                    startActivity(ichatbot);
                    break;
                case R.mipmap.ebooks:
                    Intent ifeedback = new Intent(GridMainActivity.this, EventFeedbackActivity.class);
                    startActivity(ifeedback);
                    break;

            }
        } catch (Exception e) {
            reporterror(tag, e.toString());
            e.printStackTrace();
        }
    }

    public void logout(boolean fromallreadylogin) {
        try {
            if (edit == null) {
                preferences = PreferenceManager.getDefaultSharedPreferences(GridMainActivity.this);
                edit = preferences.edit();
            }
            edit.putBoolean("isloginv1", false);
            edit.apply();
            long totalmillies = System.currentTimeMillis() - GlobalValues.currentmillies;
            long timetaken = GlobalValues.student.getTimetaken() + (totalmillies / 1000);
            GlobalValues.student.setTimetaken(timetaken);
            GlobalValues.student.setUseractive(0);
            GlobalValues.student.setLasttimetaken(totalmillies / 1000);
            GlobalValues.student.setIslogin(0);
            if (!fromallreadylogin) {
                if (InternetUtils.getInstance(getApplicationContext()).available()) {
                    if (GlobalValues.student != null) {
                        //   DatabaseReference databaseReference;
                        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(Constants.firebasedbname);
                        Map<String, Object> values = GlobalValues.student.toMap();

                        databaseReference.child(GlobalValues.student.getMobile()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot != null) {
                                    if (dataSnapshot.getValue() != null) {
                                        if (dataSnapshot.exists()) {
                                            //  Log.e("exists ", "exists " + dataSnapshot.toString());
                                            Student s = dataSnapshot.getValue(Student.class);
                                            if (s.getIEMIno().length() > 0) {
                                                if (!s.getIEMIno().equalsIgnoreCase(GlobalValues.student.getIEMIno())) {
                                                    return;
                                                }
                                            }

                                            long totalmillies = System.currentTimeMillis() - GlobalValues.currentmillies;
                                            long timetaken = GlobalValues.student.getTimetaken() + (totalmillies / 1000);
                                            Map<String, Object> childUpdates = new HashMap<>();
                                            childUpdates.put("timetaken", timetaken);
                                            childUpdates.put("useractive", 0);
                                            childUpdates.put("lasttimetaken", totalmillies / 1000);
                                            childUpdates.put("islogin", 0);
                                            databaseReference.child(GlobalValues.student.getMobile()).updateChildren(childUpdates);
                                        } else {
                                            addtofirebasedb(1);
                                        }
                                    } else {
                                        addtofirebasedb(1);
                                    }
                                } else {
                                    addtofirebasedb(1);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                }
            }

            Intent login = new Intent(GridMainActivity.this, LoginV1Activity.class);
            login.putExtra("StudId", GlobalValues.student.getId());

            if (fromallreadylogin)
                login.putExtra("showdevicelogin", true);

            startActivity(login);
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.notification:
                Intent intent = new Intent(GridMainActivity.this, NotificationActivity.class);
                startActivity(intent);
                break;
            /*case R.id.settings:
                break;*/
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        try {
            MenuInflater menuInflater = getMenuInflater();
            menuInflater.inflate(R.menu.logout_menu, menu);

            Drawable icon_color_change = menu.getItem(0).getIcon(); // change 0 with 1,2 ...
            Drawable icon_color_change2 = menu.getItem(1).getIcon();
            // Drawable icon_color_change3 = menu.getItem(2).getIcon();
            icon_color_change.mutate();
            icon_color_change.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_IN);
            icon_color_change2.mutate();
            icon_color_change2.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_IN);
            //  icon_color_change3.mutate();
            // icon_color_change3.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_IN);
            /*MenuItem logout_button = menu.findItem(R.id.logout);
            logout_button.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    logout(false);
                    return false;
                }
            });*/

            MenuItem setting_button = menu.findItem(R.id.settings);
            setting_button.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    final String[] listItems = {"English", "मराठी"};
                    final AlertDialog.Builder mb = new AlertDialog.Builder(GridMainActivity.this);
                    mb.setTitle(getResources().getString(R.string.change_language));
                    mb.setSingleChoiceItems(listItems, -1, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (which == 0) {
                                setLocale("En");
                                recreate();
                            } else {
                                setLocale("mr");
                                recreate();
                            }
                            if (dialog != null)
                                dialog.dismiss();
                        }
                    });
                    AlertDialog ad = mb.create();
                    if (!((Activity) context).isFinishing()) {
                        ad.show();
                    }

                    return false;
                }
            });
        } catch (Exception e) {
            reporterror(tag, e.toString());
            e.printStackTrace();
        }
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public void onBackPressed() {
        final android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.grid_back_dialog, null);
        alert.setView(alertLayout);

        alert.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        alert.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (dialog != null)
                    dialog.dismiss();
            }
        });

        if (!((Activity) context).isFinishing()) {
            Dialog dialog = alert.create();
            dialog.show();
        }
      /*  final Dialog dialog = new Dialog(context);
        alert.setTitle("Target Educare");
        TextView Text_dialog_yes_button = (TextView) dialog.findViewById(R.id.textview_grid_back_exit_yes_button);
        TextView Text_dialog_no_button = (TextView) dialog.findViewById(R.id.textview_grid_back_exit_no_button);

        Text_dialog_no_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        Text_dialog_yes_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });*/

        //  ImageView dialog_close = (ImageView) dialog.findViewById(R.id.dialog_close_button);


       /* dialog_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });*/
    }

    boolean isupdated = false;

    public void showdialog() {
        logout(true);
       /* try {
            final android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(this);
            alert.setTitle(R.string.attention);
            alert.setMessage(R.string.logoumsg);
            alert.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    logout(true);
                }
            });

            alert.setCancelable(false);
            final android.app.AlertDialog dialog1 = alert.create();
            if (!((Activity) context).isFinishing()) {
                dialog1.show();
            }
        } catch (Exception e) {
            reporterror(tag, e.toString());
        }*/
    }

    private void CheckUsers(final String uId) {
        try {
            if (databaseReference != null) {
                databaseReference.child(uId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        try {
                            if (dataSnapshot != null) {
                                if (dataSnapshot.getValue() != null) {
                                    // Log.e("datasnapshot ", "datasnap shot " + dataSnapshot.toString());
                                    if (dataSnapshot.exists()) {
                                        Log.e("datasnapshot ", "data snap " + uId);

                                        Student s = dataSnapshot.getValue(Student.class);

                                        if (s.getIEMIno().length() > 0 && GlobalValues.student.getIEMIno().length() > 0) {
                                            if (!s.getIEMIno().equalsIgnoreCase(GlobalValues.student.getIEMIno())) {
                                                Log.e("via IEMIno", "via IEMIno");
                                                showdialog();
                                                return;
                                            }
                                        }

                                       /* Map<String, Object> newdata = (Map<String, Object>) dataSnapshot.getValue();
                                        Map<String, Object> chatdata = (Map<String, Object>) newdata.get("chat");*/
                                        //  Log.e("data ","data chat"+chatdata.toString());

                                    /*    for (int i = 0; i < chatdata.size(); i++) {
                                            Log.e("msgdata ", "msgdata " + chatdata.get(i).toString());

                                        }*/
                                       /* for (int i = 0; i < chatdata.size(); i++) {
                                            Log.e("msgdata ", "msgdata " + chatdata.get(i).toString());
                                            *//*Map<String, Object> msgdata = (Map<String, Object>) chatdata.get(i);
                                            if (msgdata.size() > 0) {
                                                Log.e("msgdata ", "msgdata " + msgdata.get(0));
                                            }*//*
                                        }
*/
                                        GlobalValues.student.setTimetaken(s.getTimetaken());
                                        if (!isupdated) {
                                            isupdated = true;
                                            GlobalValues.student.setLastvisiteddate(DateUtils.getSqliteTime());
                                            Map<String, Object> childUpdates = new HashMap<>();
                                      /*  Map<String, Object> values = new HashMap<>();
                                        values.put("lastvisiteddate",DateUtils.getSqliteTime());
                                        values.put("isactive",1);
                                        values.put("IEMIno",GlobalValues.student.getIEMIno());*/
                                            childUpdates.put("lastvisiteddate", DateUtils.getSqliteTime());
                                            childUpdates.put("useractive", 1);
                                            childUpdates.put("IEMIno", GlobalValues.student.getIEMIno());
                                            databaseReference.child(GlobalValues.student.getMobile()).updateChildren(childUpdates);
                                            // addtofirebasedb(0);
                                        }
                                    } else {
                                        addtofirebasedb(1);
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
/*
                databaseReference.child(uId).addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        try {
                            if (dataSnapshot != null) {
                                Log.e("Datas   ", "" + dataSnapshot.toString());
                                if (dataSnapshot.getValue() != null) {
                                    Student s = dataSnapshot.getValue(Student.class);
                                    Log.e("data ", s.getMobile() + " " + s.getId() + " " + s.getTimetaken());
                                    // GlobalValues.student.setUseractive(1);
                                    if (!dataSnapshot.exists()) {
                                        addtofirebasedb(1);
                                    } else {
                                        Log.e("need onDataChange", "onDataChange");
                                        GlobalValues.student.setTimetaken(s.getTimetaken());

                                        if (s.getIEMIno().length() > 0) {
                                            if (!s.getIEMIno().equalsIgnoreCase(GlobalValues.student.getIEMIno())) {
                                                Log.e("need onDataChange", "need to call logout");
                                            }
                                        }
                                        if (!isupdated) {
                                            isupdated = true;
                                            GlobalValues.student.setLastvisiteddate(DateUtils.getSqliteTime());
                                            Map<String, Object> childUpdates = new HashMap<>();
                                            Map<String, Object> values = new HashMap<>();
                                            values.put("lastvisiteddate",DateUtils.getSqliteTime());
                                            childUpdates.put(  GlobalValues.student.getMobile(), values);
                                            databaseReference.updateChildren(childUpdates);
                                            //addtofirebasedb(0);
                                        }
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });*/
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addtofirebasedb(int flag) {
        try {
            if (InternetUtils.getInstance(getApplicationContext()).available()) {
                DatabaseReference databaseReference;
                databaseReference = FirebaseDatabase.getInstance().getReference(Constants.firebasedbname);
                Map<String, Object> values = GlobalValues.student.toMap();
                Log.e("put222 ", "put " + values.get("useractive"));
                //  databaseReference.child(GlobalValues.student.getMobile()).setValue(values);

                if (flag == 0) {
                    Map<String, Object> childUpdates = new HashMap<>();

                    Log.e("Timetaken ", "timetaken " + GlobalValues.student.getTimetaken());
                    childUpdates.put("useractive", 1);
                    childUpdates.put("timetaken", GlobalValues.student.getTimetaken());
                    childUpdates.put("lasttimetaken", GlobalValues.student.getLasttimetaken());
                    childUpdates.put("lastvisiteddate", GlobalValues.student.getLastvisiteddate());
                    databaseReference.child(GlobalValues.student.getMobile()).updateChildren(childUpdates);

                } else {
                    databaseReference.child(GlobalValues.student.getMobile()).setValue(values);
                    Log.e("update ", "insert ");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 /*   public void addtofirebasedb() {
        try {
            if(InternetUtils.getInstance(GridMainActivity.this).available()) {
                Map<String, Object> values = GlobalValues.student.toMap();
                Log.e("put11 ", "put " + values.get("useractive"));
                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put("Users/" + GlobalValues.student.getMobile(), values);
                Task<Void> i = databaseReference.updateChildren(childUpdates);
                i.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        reporterror(tag, "firebase db " + GlobalValues.student.getMobile() + " " + e.toString());
                    }
                });
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }*/


 public void check_chat_available(String uId){
     try {
         if (databaseReference != null) {

             databaseReference.child(uId).child("chat").addChildEventListener(new ChildEventListener() {
                 @Override
                 public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                     try {
                         if (dataSnapshot != null) {
                             if (dataSnapshot.getValue() != null) {
                                 if (dataSnapshot.exists()) {


                                        /*String key = dataSnapshot.getKey();

                                        if(key!=null){
                                            dataSnapshot.child(key);
                                        }*/

                                        /*String name = dataSnapshot.child("data").getValue(String.class);
                                        Log.e("chat data val ", name);
                                        chats.add(name);*/

                                     try {
                                         String key = dataSnapshot.getKey();
                                         String chatmsg = dataSnapshot.child("data").getValue(String.class);
                                         String chatdate = dataSnapshot.child("date").getValue(String.class);
                                         String isread = dataSnapshot.child("isread").getValue(String.class);
                                         JSONObject jsonobj = new JSONObject();
                                         jsonobj.put("time", chatdate);
                                         jsonobj.put("header", "TARGET EDUCARE PEAK app");
                                         jsonobj.put("desc", chatmsg);
                                         jsonobj.put("imageurl", "");
                                         jsonobj.put("key", key);
                                         jsonobj.put("isread", isread);

                                         DatabaseHelper.getInstance(context).savenotificationData(DatabaseHelper.TABLE_NOTIFICATION,  jsonobj.toString());
                                         DatabaseHelper.getInstance(context).getnotificationdata(DatabaseHelper.TABLE_NOTIFICATION);


                                     } catch (Exception e) {
                                         e.printStackTrace();
                                     }


                                        /*Iterable<DataSnapshot> contactChildren = dataSnapshot.getChildren();
                                        for (DataSnapshot contact : contactChildren) {
                                            Chat c = contact.getValue(Chat.class);
                                            if(c!=null){
                                                Log.e("contact:: ", c.getData());
                                                chatdata.add(c);
                                            }
                                        }*/

                                 }
                             }
                         }
                     } catch (Exception e) {
                         e.printStackTrace();
                     }
                 }

                 @Override
                 public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                 }

                 @Override
                 public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                 }

                 @Override
                 public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                 }

                 @Override
                 public void onCancelled(@NonNull DatabaseError databaseError) {

                 }
             });
         }
     } catch (Exception e) {
         e.printStackTrace();
     }
 }




}
