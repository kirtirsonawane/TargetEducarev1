package com.targeteducare;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.targeteducare.Adapter.AdapterSubCategory;
import com.targeteducare.Classes.Questions;
import com.targeteducare.Classes.SubCategoryModel;

import java.io.Serializable;
import java.util.ArrayList;

public class ActivitySubCategory extends Activitycommon {

    RecyclerView listView;
    LinearLayoutManager linearLayoutManager;
    ArrayList<SubCategoryModel> subcategories = new ArrayList<>();
    String parent_id = null;
    String marathi = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try{
            screenshot_capture_permission();
        super.onCreate(savedInstanceState);
        loadLocale();
        setContentView(R.layout.activity_sub_category);
        setmaterialDesign();
        toolbar.setTitleMargin(25, 10, 10, 10);
        setTitle("PEAK");
        back();
        //  GlobalValues.langs = getSharedPreferences("Settings", Activity.MODE_PRIVATE).getString("Current Language", "");

        try {
            Intent intent = getIntent();
            Bundle args = intent.getBundleExtra("BUNDLE");
            subcategories = (ArrayList<SubCategoryModel>) args.getSerializable("ARRAYLIST");
            parent_id = args.getString("Id");
            TextView selectsubcategory = (TextView) findViewById(R.id.text_subcategory);
            selectsubcategory.setText(getResources().getString(R.string.selectcategory));
            listView = (RecyclerView) findViewById(R.id.recycle_subcategory);
            linearLayoutManager = new LinearLayoutManager(ActivitySubCategory.this);

            listView.setLayoutManager(linearLayoutManager);
            AdapterSubCategory adapterSubCategory = new AdapterSubCategory(ActivitySubCategory.this, subcategories);
            listView.setAdapter(adapterSubCategory);
        /*for (int i=0;i<subCategoryModels.size();i++){
            Log.e("subcategories ","sub in ac"+subCategoryModels.get(i).getName());
        }

        Log.e("parent ","sub in ac"+parent_id);*/
        }catch (Exception e){
            e.printStackTrace();
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    }
    public void gotonext(int i) {

        // Toast.makeText(ActivitySubCategory.this,"Sorry Data is not avalaible we will update soon", Toast.LENGTH_LONG).show();
        // switch statement with int data type

        /////----------mock---------/////
        try {


            ArrayList<Questions> questions = new ArrayList<>();


            String s = subcategories.get(i).getQuestions1().toString();

            if (subcategories.get(i).getQuestions1().size() == 0) {
                Toast.makeText(ActivitySubCategory.this, "Sorry Data is not avalaible we will update soon", Toast.LENGTH_SHORT).show();

          /*  questions = (subcategories.get(i).getQuestions1());

            if (GlobalValues.langs.equalsIgnoreCase("mr")) {
                marathi = subcategories.get(i).getNameInMarathi();
            }

            else {
                marathi = subcategories.get(i).getName();
            }

            // Log.e("item ", "item " + id_subcategory.toString() );
            Intent intent = new Intent(ActivitySubCategory.this, ActivityFAQ.class);
            Bundle args = new Bundle();
            args.putSerializable("ARRAYLIST", (Serializable) questions);
            // intent.putExtra("QuestionListExtra", ArrayList<Question>mQuestionList);
            args.putString("Id", marathi);

            intent.putExtra("BUNDLE", args);
            startActivity(intent)*/
                ;
            } else {
                //  Toast.makeText(ActivitySubCategory.this, "Sorry Data is not avalaible we will update soon", Toast.LENGTH_LONG).show();


                questions = (subcategories.get(i).getQuestions1());

                if (GlobalValues.langs.equalsIgnoreCase("mr")) {
                    marathi = subcategories.get(i).getNameInMarathi();
                } else {
                    marathi = subcategories.get(i).getName();
                }

                // Log.e("item ", "item " + id_subcategory.toString() );
                Intent intent = new Intent(ActivitySubCategory.this, ActivityFAQ.class);
                Bundle args = new Bundle();
                args.putSerializable("ARRAYLIST", (Serializable) questions);
                // intent.putExtra("QuestionListExtra", ArrayList<Question>mQuestionList);
                args.putString("Text", marathi);
                args.putString("Id", subcategories.get(i).getId());

                intent.putExtra("BUNDLE", args);
                startActivity(intent);


            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
