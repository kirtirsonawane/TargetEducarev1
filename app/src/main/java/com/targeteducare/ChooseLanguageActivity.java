package com.targeteducare;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.targeteducare.Classes.Student;

import java.lang.reflect.Type;

public class ChooseLanguageActivity extends Activitycommon {
    RadioButton rb_english, rb_marathi;
    RadioGroup rg_language;
    Button next;
    SharedPreferences preferences;
    SharedPreferences.Editor edit;
    int id_checked = -1;
    boolean isnotified = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            screenshot_capture_permission();
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_choose_language);
            setmaterialDesign();
            setTitle("Target Educare");

            preferences = PreferenceManager.getDefaultSharedPreferences(ChooseLanguageActivity.this);
            edit = preferences.edit();

            Bundle b=getIntent().getExtras();
            if(b!=null)
            {
                if(b.containsKey("isnotified"))
                    isnotified=b.getBoolean("isnotified");
            }

          /*  if (preferences.getBoolean("isloginv1", false)) {
                Gson gson = new Gson();
                Type type = new TypeToken<Student>() {
                }.getType();
                GlobalValues.student = gson.fromJson(preferences.getString("studentdetails", ""), type);
                Intent intent = new Intent(ChooseLanguageActivity.this, GridMainActivity.class);
                startActivity(intent);
                this.finish();
            }*/

            //  GlobalValues.IP = preferences.getString("IP", "exam.targeteducare.com");
            if (preferences.getBoolean("isloginv1", false)) {
                Gson gson = new Gson();
                Type type = new TypeToken<Student>() {
                }.getType();
                GlobalValues.student = gson.fromJson(preferences.getString("studentdetails", ""), type);
                Intent intent = new Intent(ChooseLanguageActivity.this, ActivitySplashScreen.class);
                intent.putExtra("isnotified",isnotified);
                startActivity(intent);
                Log.e("login v1", "login v1");
                this.finish();
            }

            rb_english = findViewById(R.id.rb_english);
            rb_marathi = findViewById(R.id.rb_marathi);
            next = findViewById(R.id.button_next);
            rg_language = findViewById(R.id.radiogroup_language);
            rb_english.setChecked(true);
            rg_language.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    id_checked = rg_language.getCheckedRadioButtonId();

                    next.setEnabled(true);
                    next.setTextColor(getResources().getColor(R.color.blue1));
                    if (id_checked == rb_english.getId()) {
                        setLocale("En");
                        Log.e("You have chosen ", "English");
                    } else if (id_checked == rb_marathi.getId()) {
                        setLocale("mr");
                        Log.e("You have chosen ", "Marathi");
                    } else {
                        setLocale("En");
                        Log.e("Default language ", "English");
                    }
                }

            });


            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (rb_english.isChecked()) {
                        Log.e("Default ", "English");
                        setLocale("En");
                        Intent ilogin = new Intent(ChooseLanguageActivity.this, ActivitySplashScreen.class);
                        ilogin.putExtra("isnotified",isnotified);
                        startActivity(ilogin);
                        ChooseLanguageActivity.this.finish();
                    } else if (id_checked == -1) {
                        Toast.makeText(ChooseLanguageActivity.this, getResources().getString(R.string.toast_selectlanguage), Toast.LENGTH_SHORT).show();
                    } else {
                        Intent ilogin = new Intent(ChooseLanguageActivity.this, ActivitySplashScreen.class);
                        ilogin.putExtra("isnotified",isnotified);
                        startActivity(ilogin);
                        ChooseLanguageActivity.this.finish();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
