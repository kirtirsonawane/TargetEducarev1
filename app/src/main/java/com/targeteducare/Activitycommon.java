package com.targeteducare;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LevelListDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.targeteducare.Classes.Exam;
import com.targeteducare.Classes.QuestionURL;
import com.targeteducare.database.DatabaseHelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Activitycommon extends AppCompatActivity {
    Toolbar toolbar;
    Context context;
    ProgressDialog dialog = null;
    String tag = "";
    float mHeadingFontSize = 20.0f;
    float mValueFontSize = 20.0f;

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
            e.printStackTrace();
        }
    }



    public void setmaterialDesign_my_package() {
        try {
            tag = "ActivityCommon";
            toolbar = findViewById(R.id.packages_toolbar_xm);
            toolbar.setTitleTextColor(getResources().getColor(R.color.White));

            setSupportActionBar(toolbar);
            toolbar.setLogo(R.mipmap.ic_launcher);

            toolbar.setTitle("Packages");

            ((TextView) toolbar.findViewById(R.id.package_toolbar_textview_amount_sign)).setText("₹");
            ((TextView) toolbar.findViewById(R.id.package_toolbar_textview_payment)).setText("0.00");
           //toolbar.addView((Button)findViewById(R.id.package_toolbar_button_1));


            LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(responseRec, new IntentFilter(Constants.BROADCAST_WIZARD));
            context = this;
        } catch (Exception e) {
            Log.e("error ","error "+e.toString());
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

    protected void back() {
        try {
            final Drawable upArrow = getResources().getDrawable(R.drawable.backarrow);
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
            e.printStackTrace();
        }
    }


    @Override
    protected void onDestroy() {
        try{
            LocalBroadcastManager.getInstance(this).unregisterReceiver(responseRec);
        }catch (Exception e)
        {
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
                e.printStackTrace();
            }
        }
    };

    public void genloading(String msg) {
        try {
            if (!((Activity) this).isFinishing()) {
                dialog = ProgressDialog.show(this, msg, "Please wait");
                dialog.setCancelable(false);
            } else {
                Log.e("activity", "activity is not running genloading");
            }

        } catch (Exception e) {
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
            e.printStackTrace();
        }
    }

    public void gotoexamActivity(Exam exam) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);
        }else {
            Calendar cal1 = Calendar.getInstance();
            Date date2 = DateUtils.parseDate(exam.getEnddate(), "dd MMM yyyy");
            Date date3 = DateUtils.parseDate(exam.getExamendtime(), "HH:mm:ss");
            date2.setHours(date3.getHours());
            date2.setMinutes(date3.getMinutes());
            cal1.setTime(date2);

            Calendar cal = Calendar.getInstance();
            Date date = DateUtils.parseDate(exam.getStartdate(), "dd MMM yyyy");
            Date date1 = DateUtils.parseDate(exam.getExamstarttime(), "HH:mm:ss");
            date.setHours(date1.getHours());
            date.setMinutes(date1.getMinutes());
            cal.setTime(date);

            if (cal.before(Calendar.getInstance()) && cal1.after(Calendar.getInstance()))
                opendialog(exam);
            else Toast.makeText(getApplicationContext(), "Expired", Toast.LENGTH_LONG).show();
        }
    }

    public void opendialog(final Exam exam) {
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
            final AlertDialog dialog = alert.create();
            dialog.show();
            bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(cb.isChecked()) {
                        Intent intent = new Intent(Activitycommon.this, ExamActivity.class);
                        intent.putExtra("exam", exam);
                        startActivity(intent);
                        dialog.cancel();
                    }else {
                        Toast.makeText(getApplicationContext(),"Please accetpt Terms & Conditions", Toast.LENGTH_LONG).show();
                    }
                }
            });
        } else {
            Log.e("activity", "activity is not running genloading");
        }
    }

    public void gotoanswersheet(Exam exam) {
        Intent intent = new Intent(Activitycommon.this, AnswersheetActivity.class);
        intent.putExtra("exam", exam);
        startActivity(intent);
        dialog.cancel();
    }

    public void downloaddata(ArrayList<QuestionURL> qdata , int examid, String language, String jsondata) {
      new DownloadImage(qdata,examid,language,jsondata).execute(qdata);
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
       //     mDrawable = (LevelListDrawable) params[1];
            //Log.d("tag", "doInBackground " + source);
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

    public void downloadimage(Bitmap bitmap, String name){

    }

    public class DownloadImage extends AsyncTask<ArrayList<QuestionURL>, Void, String> {
        String name="";
        ArrayList<QuestionURL> qdata=new ArrayList<>() ;
        int examid=0;
        String language="";
        String jsondata="";
        public DownloadImage(ArrayList<QuestionURL> qdata , int examid, String language, String jsondata){
            this.qdata=qdata;
            this.examid=examid;
            this.language=language;
            this.jsondata=jsondata;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(ArrayList<QuestionURL>... URL) {
            ArrayList<QuestionURL> qdata= URL[0];
            for (int i=0;i<qdata.size();i++) {
                QuestionURL qurldata=qdata.get(i);
                String imageURL=qdata.get(i).getImagemainsource();
                int index = imageURL.lastIndexOf("/");
                name = imageURL.substring((index + 1), imageURL.length());

                Bitmap bitmap = null;
                try {
                   /* InputStream input = new java.net.URL(imageURL).openStream();
                    bitmap = BitmapFactory.decodeStream(input);*/

                    java.net.URL url = new URL(imageURL);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    InputStream input = connection.getInputStream();
                    bitmap = BitmapFactory.decodeStream(input);
                    Log.e("bitmap ", "bitmap " + bitmap);

                    StructureClass.defineContext(Activitycommon.this);
                    File file = new File(StructureClass.generate());
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
                    c.put(DatabaseHelper.OFFLINEPATH,file.getAbsolutePath());
                    DatabaseHelper.getInstance(context).savequestionurl(c, qurldata.getId(), qurldata.getType(),imageURL);
                    Log.e("filepath ", "filepath " + savedImageURI);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return "";
        }

        @Override
        protected void onPostExecute(String result) {

            ContentValues c = new ContentValues();
            c.put(DatabaseHelper.JSONDATA, jsondata.toString());
            c.put(DatabaseHelper.LANGUAGE, language);
            c.put(DatabaseHelper.EXAMID, examid);
            c.put(DatabaseHelper.SAVEDTIME, DateUtils.getSqliteTime());
            long id = DatabaseHelper.getInstance(context).saveqdata(c, examid, language);
            Log.e("iddata", "idadata " + id);
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
}
