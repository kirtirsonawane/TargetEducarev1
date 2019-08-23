package com.targeteducare;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;
import com.targeteducare.Adapter.AdapterSupportListview;
import com.targeteducare.Classes.CategoryModel;
import com.targeteducare.Classes.SubCategoryModel;
import org.json.JSONObject;
import java.io.Serializable;
import java.util.ArrayList;

public class SupportActivity extends Activitycommon {
    RecyclerView listView;
    ArrayList<CategoryModel> categoryModel = new ArrayList<>();
    LinearLayoutManager linearLayoutManager;

    ///Api:-SupportCategory_Get
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        screenshot_capture_permission();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);
        GlobalValues.langs = getSharedPreferences("Settings", MODE_PRIVATE).getString("Current Language", "");


        setmaterialDesign();
        setTitle("PEAK QnA");
        back();
        loadLocale();
        TextView selectlanguage = (TextView) findViewById(R.id.text_selectcategory);
        selectlanguage.setText(getResources().getString(R.string.selectcategory));
        listView = (RecyclerView) findViewById(R.id.recycle_support);
        linearLayoutManager = new LinearLayoutManager(SupportActivity.this);
        listView.setLayoutManager(linearLayoutManager);
        isOnline();
        genloading("loading...");
        ConnectionManager.getInstance(SupportActivity.this).getcategorydata();


    }
/*
    public void checkConnection() {
        try {
            if (isOnline()) {

            }
            else {
                Toast.makeText(SupportActivity.this, "You are note connected to Internet", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }*/


    protected void isOnline() {
        try {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnectedOrConnecting()) {

            } else {
                Toast.makeText(SupportActivity.this, "You are not connected to Internet", Toast.LENGTH_SHORT).show();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onResponsed(int statuscode, int accesscode, String data) {

        super.onResponsed(statuscode, accesscode, data);
        if (statuscode == Constants.STATUS_OK) {
            if (accesscode == Connection.GETCATEGORYDATA.ordinal()) {
                //data = GlobalValues.TEMP_STR;
                //    Log.e("data  ::", data);
                if (data != null) {
                    try {
                        JSONObject jsonObject2 = new JSONObject(GlobalValues.TEMP_STR);
                        JSONObject jsonObject3 = jsonObject2.getJSONObject("root");
                        categoryModel.addAll(CategoryModel.getjson(jsonObject3));


                      /*  for (int i=0;i<categoryModel.size();i++)
                        {

                        }*/

                        AdapterSupportListview adapterSupportListview = new AdapterSupportListview(context, categoryModel);

                        listView.setAdapter(adapterSupportListview);
                       /* adapter = new PaperReadorDownloadAdapter(SupportActivity.this, arrayListdata);
                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();*/
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else   if (accesscode == Connection.GETCATEGORYEXCEPTION.ordinal()) {
                Toast.makeText(getApplicationContext(),getResources().getString(R.string.Connectiontimeout),Toast.LENGTH_LONG).show();
            }
        }
    }

    public void gotonext(final int item) {

        // switch statement with int data type
        try {
            /////----------mock---------/////
            ArrayList<SubCategoryModel> subCategoryModels = new ArrayList<>();
            String s = categoryModel.get(item).getSubCategoryModels().toString();

            if (s != " ") {
                subCategoryModels = (categoryModel.get(item).getSubCategoryModels());
                String id_category = categoryModel.get(item).getId().toString();

                Intent intent = new Intent(SupportActivity.this, ActivitySubCategory.class);
                Bundle args = new Bundle();
                args.putSerializable("ARRAYLIST", (Serializable) subCategoryModels);

                // intent.putExtra("QuestionListExtra", ArrayList<Question>mQuestionList);
                args.putString("Id", id_category);
                intent.putExtra("BUNDLE", args);
                startActivity(intent);
            } else {
                Toast.makeText(SupportActivity.this, "Sorry Data is not avalaible we will update soon", Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

