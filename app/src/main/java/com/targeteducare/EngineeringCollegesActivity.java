package com.targeteducare;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.Toast;

import com.targeteducare.Adapter.CustomAdapterEngColg;
import com.targeteducare.Classes.EngColgData;
import com.targeteducare.Classes.EngColgDataModel;

import java.util.ArrayList;

public class EngineeringCollegesActivity extends Activitycommon {

    private ArrayList<EngColgDataModel> data;
    private CustomAdapterEngColg adapter;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerView;
    private Button compare_button;
    ArrayList<String> list = new ArrayList<>();
    int sum = 0;
    ArrayList<Integer> position_list = new ArrayList<>();
    ArrayList<String> college_data = new ArrayList<String>();
String tag="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        try {

            screenshot_capture_permission();
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_engineering_colleges);

            tag = this.getClass().getSimpleName();
            setmaterialDesign();
            setTitle(getResources().getString(R.string.engineering_colleges));
            back();

            recyclerView = findViewById(R.id.recyclerviewforengcolg);
            recyclerView.setHasFixedSize(true);

            layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);
            compare_button = (Button) findViewById(R.id.college_compare_button);
            data = new ArrayList<EngColgDataModel>();
            for (int i = 0; i < EngColgData.logo_img.length; i++) {
                data.add(new EngColgDataModel(
                        EngColgData.logo_img[i], EngColgData.rating[i], EngColgData.reviews[i], EngColgData.college_name[i],
                        EngColgData.established_year[i], EngColgData.courses[i], EngColgData.institute_type[i], EngColgData.exam_name[i]
                ));
            }
            adapter = new CustomAdapterEngColg(EngineeringCollegesActivity.this, data);
            recyclerView.setAdapter(adapter);

        } catch (Exception e) {
            reporterror(tag, e.toString());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        try {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu, menu);

            MenuItem searchItem = menu.findItem(R.id.action_search);
            SearchView searchView = (SearchView) searchItem.getActionView();

            searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    adapter.getFilter().filter(newText);
                    return false;
                }
            });
        } catch (Exception e) {
            reporterror(tag, e.toString());
        }
        return true;
    }

    public void gotocompare_function(int i, final int position) {
        try {
            if (i == 1) {
                sum++;

                Log.e("Sum : ", String.valueOf(sum));
                position_list.add(position);
                Log.e("list : ", String.valueOf(position_list));
                //  Log.e("College : ",String.valueOf(data));
                compare_button.setVisibility(View.VISIBLE);
            } else {

                for (int j = 0; j < position_list.size(); j++) {
                    if (position_list.get(j) == position) {
                        position_list.remove(j);
                    }
                }
                Log.e("list : ", String.valueOf(position_list));

                sum--;
                if (sum > 0 && i == 0) {
                    // Log.e("College : ",String.valueOf(data));
                    Log.e("Sum sum!=0: ", String.valueOf(sum));
                    compare_button.setVisibility(View.VISIBLE);
                } else {
                    Log.e("Sum sum==0: ", String.valueOf(sum));
                    compare_button.setVisibility(View.GONE);
                }

            }
            compare_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Log.e("College : ",String.valueOf(data));
                    if (sum > 1) {

                        Log.e("Onclick sum : ", String.valueOf(sum) + "  " + position + "position ");
                        ArrayList<EngColgDataModel> selecteddata = new ArrayList<>();
                        for (int i1 = 0; i1 < position_list.size(); i1++) {
                            int k = position_list.get(i1);
                            Log.e("Onclick sum : ", String.valueOf(sum) + "  " + position + "position ");
                            Log.e("Onclick sum : ", String.valueOf(data) + "  " + position + "position ");
                            selecteddata.add(data.get(k));

                        /*String College_name = data.get(k).getCollege_name();
                        Log.e("College : ", College_name);
                        college_data.add(College_name);
                        String Courses = String.valueOf(data.get(k).getCourses());
                        Log.e("Courses : ", String.valueOf(Courses));
                        college_data.add(Courses);
                        String Exam_name = data.get(k).getExam_name();
                        Log.e("Exam : ", String.valueOf(Exam_name));
                        college_data.add(Exam_name);
                        String Ratings = String.valueOf(data.get(k).getRating());
                        Log.e("Ratings : ", String.valueOf(Ratings));
                        college_data.add(Ratings);
                        String Reveiws = String.valueOf(data.get(k).getReviews());
                        Log.e("Reviews : ", String.valueOf(Reveiws));
                        college_data.add(Reveiws);

                        list.addAll(college_data);*/


                        }
                        Intent i = new Intent(getApplicationContext(), CollegeComparisionActivity.class);

                        /* i.putExtras(bundle);*/
                        i.putExtra("collegedata", selecteddata);
                        startActivity(i);

                    /*for (int i1 = 0; i1 < data.size(); i1++) {
                        int k = position_list.get(i1);
                        Log.e("Onclick sum : ", String.valueOf(sum) + "  " + position + "position ");
                        Log.e("Onclick sum : ", String.valueOf(data) + "  " + position + "position ");
                        String College_name = data.get(k).getCollege_name();
                        Log.e("College : ", College_name);
                        String Courses = String.valueOf(data.get(k).getCourses());
                        Log.e("Courses : ", String.valueOf(Courses));
                        String Exam_name = data.get(k).getExam_name();
                        Log.e("Exam : ", String.valueOf(Exam_name));
                        String Ratings = String.valueOf(data.get(k).getRating());
                        Log.e("Ratings : ", String.valueOf(Ratings));
                        String Reveiws = String.valueOf(data.get(k).getReviews());
                        Log.e("Reviews : ", String.valueOf(Reveiws));
                    }*/
                        //Toast.makeText(getApplicationContext(), sum + " colleges selected for comparision !!", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.message_selectcollegetocompare), Toast.LENGTH_LONG).show();
                    }
                }
            });
        } catch (Exception e) {
            reporterror(tag,e.toString());
            e.printStackTrace();
        }
    }
}
