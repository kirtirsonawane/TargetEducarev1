package com.targeteducare;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.targeteducare.Adapter.GetMyPackagesAdapter;
import com.targeteducare.Classes.MyPackages;
import org.json.JSONObject;
import java.util.ArrayList;

public class MySubscriptionsActivity extends Activitycommon {
    ArrayList<MyPackages> myPackagesArrayList, myPackagesArrayListNew;
    GetMyPackagesAdapter getMyPackagesAdapter;
    RecyclerView Packages_RecyclerView;
    LinearLayoutManager linearLayoutManager;
    TextView tv_nosubscriptions;
    String lang = "";
    String tag="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            screenshot_capture_permission();
            super.onCreate(savedInstanceState);
            loadLocale();
            setContentView(R.layout.activity_my_subscriptions);

            setmaterialDesign();
            back();
            setTitle(getResources().getString(R.string.my_subscriptions));
            toolbar.setTitleMargin(30,10,10,10);
            tag = this.getClass().getSimpleName();
            lang = getSharedPreferences("Settings", Activity.MODE_PRIVATE).getString("Current Language", "");
            Packages_RecyclerView = (RecyclerView) findViewById(R.id.recyclerviewformysubscription);
            tv_nosubscriptions = findViewById(R.id.tv_nosubscriptions);
            myPackagesArrayList = new ArrayList<MyPackages>();
            myPackagesArrayListNew = new ArrayList<MyPackages>();
            linearLayoutManager = new LinearLayoutManager(MySubscriptionsActivity.this);
            JSONObject jsonObject = new JSONObject();
            JSONObject jsonObject1 = new JSONObject();

            try {
                jsonObject.put("studentid", GlobalValues.student.getId());
                jsonObject.put("ImagePath", URLS.packages_image_url());
                Log.e("student id ", GlobalValues.student.getId());
                jsonObject1.put("FilterParameter", jsonObject.toString());
                Log.e("parameters ", jsonObject1.toString());
                ConnectionManager.getInstance(MySubscriptionsActivity.this).getstudentpackage(jsonObject1.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        /*Log.e("New jsonlist size ", myPackagesArrayListNew.size()+"");
        if(myPackagesArrayListNew.size()>0){
            getMyPackagesAdapter = new GetMyPackagesAdapter(context, myPackagesArrayListNew , 1);
        }
        else {
            tv_nosubscriptions.setVisibility(View.VISIBLE);
            tv_nosubscriptions.setText(getResources().getString(R.string.nosubscriptions_message));
        }*/
        } catch (Exception e) {
            reporterror(tag, e.toString());
            e.printStackTrace();
        }
    }

    @Override
    protected void onResponsed(int statuscode, int accesscode, String data) {
        super.onResponsed(statuscode, accesscode, data);
        if (statuscode == Constants.STATUS_OK) {
            if (accesscode == Connection.GETPACKAGE.ordinal()) {
                try {
                    JSONObject jsonObject = new JSONObject(GlobalValues.TEMP_STR);
                    JSONObject root = jsonObject.getJSONObject("root");
                    myPackagesArrayList.addAll(MyPackages.getjson(root));
                    Log.e("Original jsonlist size ", myPackagesArrayList.size() + "");
                    for (int i = 0; i < myPackagesArrayList.size(); i++) {
                        if (myPackagesArrayList.get(i).getIsSubscribe().equalsIgnoreCase("1")) {
                            Log.e("is subscribe value ", myPackagesArrayList.get(i).getIsSubscribe());
                            myPackagesArrayListNew.add(myPackagesArrayList.get(i));
                        }
                    }

                    if (myPackagesArrayListNew.size() > 0) {
                        getMyPackagesAdapter = new GetMyPackagesAdapter(context, myPackagesArrayListNew, 1, lang);
                        Packages_RecyclerView.setLayoutManager(linearLayoutManager);
                        Packages_RecyclerView.setItemAnimator(new DefaultItemAnimator());
                        Packages_RecyclerView.setAdapter(getMyPackagesAdapter);
                    } else {
                        tv_nosubscriptions.setVisibility(View.VISIBLE);
                        tv_nosubscriptions.setText(getResources().getString(R.string.nosubscriptions_message));
                    }

                } catch (Exception e) {
                    reporterror(tag, e.toString());
                    e.printStackTrace();
                }

            }else if(accesscode==Connection.GETPACKAGEEXCEPTION.ordinal())
            {
                Toast.makeText(getApplicationContext(),MySubscriptionsActivity.this.getResources().getString(R.string.Connectiontimeout),Toast.LENGTH_LONG).show();
            }

        }

    }
}
