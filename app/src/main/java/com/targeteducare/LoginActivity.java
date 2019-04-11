package com.targeteducare;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LevelListDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Html;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.targeteducare.Classes.Student;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;

public class LoginActivity extends Activitycommon implements Html.ImageGetter {
    EditText e1, e2,e3;
    Button bt;
    TextView txt;
    SharedPreferences preferences;
    SharedPreferences.Editor edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        attachUI();
        registerreceiver();
    }

    private void attachUI() {
        Display display = getWindowManager(). getDefaultDisplay();
        Point size = new Point();
        display. getSize(size);
        GlobalValues.width = size.x;

        preferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
        edit=preferences.edit();
        if (preferences.getBoolean("islogin", false)) {
            String stprofile = preferences.getString("profile", "");
            GlobalValues.IP = preferences.getString("IP", "");
            if (stprofile.length() > 0) {
                Gson gson = new Gson();
                Type type = new TypeToken<Student>() {
                }.getType();
                GlobalValues.student = gson.fromJson(stprofile, type);
            }
            Intent intent = new Intent(LoginActivity.this, ExamListActivity.class);
            startActivity(intent);
            this.finish();
        }

        e1 = (EditText) findViewById(R.id.edittext_1);
        e2 = (EditText) findViewById(R.id.edittext_2);
        e3 = (EditText) findViewById(R.id.edittext_3);
        bt = (Button) findViewById(R.id.button_1);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (e1.getText().toString().length() > 0 && e2.getText().toString().length() > 0 && e3.getText().toString().length() > 0) {
                        GlobalValues.IP=e3.getText().toString();
                        edit.putString("IP",e3.getText().toString());
                        edit.commit();
                        genloading("loading..");
                        JSONObject obj = new JSONObject();
                        obj.put("Username", e1.getText().toString().trim());
                        obj.put("Type", "Student");
                        obj.put("Password", e2.getText().toString().trim());
                        JSONObject mainobj = new JSONObject();
                        mainobj.put("FilterParameter", obj.toString());
                        ConnectionManager.getInstance(LoginActivity.this).login(mainobj.toString());
                    }else {
                        Toast.makeText(getApplicationContext(),"Please enter all the fields", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onResponsed(int statuscode, int accesscode, String data) {
        super.onResponsed(statuscode, accesscode, data);
        if (statuscode == Constants.STATUS_OK) {
            if (accesscode == Connection.LOGIN.ordinal()) {
                try {
                    JSONObject obj = new JSONObject(GlobalValues.TEMP_STR).getJSONObject("root").optJSONObject("subroot");
                    if (obj != null) {
                        if(obj.has("error"))
                        {
                            Toast.makeText(getApplicationContext(),"please enter valid details", Toast.LENGTH_LONG).show();
                        }else {
                            Gson gson = new Gson();
                            Type type = new TypeToken<Student>() {
                            }.getType();
                            GlobalValues.student = gson.fromJson(obj.toString(), type);
                            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                            SharedPreferences.Editor edit = pref.edit();
                            edit.putString("profile", obj.toString());
                            edit.putBoolean("islogin", true);
                            edit.commit();
                            Intent intent = new Intent(LoginActivity.this, ExamListActivity.class);
                            startActivity(intent);
                            this.finish();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Please enter valid details", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else if (accesscode == Connection.LOGINEXCEPTION.ordinal()) {
                try {
                    Toast.makeText(getApplicationContext(),Constants.connectiontimeout, Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public Drawable getDrawable(String source) {
        LevelListDrawable d = new LevelListDrawable();
        Drawable empty = getResources().getDrawable(R.mipmap.ic_launcher);
        d.addLevel(0, 0, empty);
        d.setBounds(0, 0, empty.getIntrinsicWidth(), empty.getIntrinsicHeight());

        new LoadImage().execute(source, d);

        return d;
    }

    class LoadImage extends AsyncTask<Object, Void, Bitmap> {
        private LevelListDrawable mDrawable;

        @Override
        protected Bitmap doInBackground(Object... params) {
            String source = (String) params[0];
            mDrawable = (LevelListDrawable) params[1];
            Log.d("tag", "doInBackground " + source);
            try {
                InputStream is = new URL(source).openStream();
                return BitmapFactory.decodeStream(is);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            Log.d("tag", "onPostExecute drawable " + mDrawable);
            Log.d("tag", "onPostExecute bitmap " + bitmap);
            if (bitmap != null) {
                BitmapDrawable d = new BitmapDrawable(bitmap);
                mDrawable.addLevel(1, 1, d);
                mDrawable.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
                mDrawable.setLevel(1);
                // i don't know yet a better way to refresh TextView
                // mTv.invalidate() doesn't work as expected
                CharSequence t = txt.getText();
                txt.setText(t);
            }
        }
    }
}
