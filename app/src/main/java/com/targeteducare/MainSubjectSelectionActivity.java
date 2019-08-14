package com.targeteducare;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.targeteducare.Adapter.MainSubjectAdapter;
import com.targeteducare.Classes.MainSubjectDataModel;
import java.util.ArrayList;

public class MainSubjectSelectionActivity extends Activitycommon {
    private MainSubjectAdapter mainSubjectAdapter;
    private ArrayList<MainSubjectDataModel> mainSubjectDataArrayList;
    private ArrayList<String> temp_data;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    String tag = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        screenshot_capture_permission();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_subject_selection);
        try {
            tag = this.getClass().getSimpleName();
            setmaterialDesign();
            back();

            recyclerView = findViewById(R.id.recyclerviewformainsubjectselection);
            layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);
            temp_data = new ArrayList<String>();
            temp_data.add("Physics");
            temp_data.add("Maths");
            temp_data.add("Chemistry");
            temp_data.add("Biology");
            mainSubjectDataArrayList = new ArrayList<>();

            for (int i = 0; i < temp_data.size(); i++) {
                MainSubjectDataModel dataModel = new MainSubjectDataModel(temp_data.get(i));
                mainSubjectDataArrayList.add(dataModel);
            }

            mainSubjectAdapter = new MainSubjectAdapter(MainSubjectSelectionActivity.this, mainSubjectDataArrayList);
            recyclerView.setAdapter(mainSubjectAdapter);

        } catch (Exception e) {
            reporterror(tag, e.toString());
            e.printStackTrace();
        }
    }

    public void referto(int position) {
        try {
            String main_subject_name = mainSubjectDataArrayList.get(position).getMainsubject();
            Intent imainsubj = new Intent(MainSubjectSelectionActivity.this, SubjectSelectionActivity.class);
            imainsubj.putExtra("subjname", main_subject_name);
            startActivity(imainsubj);
            finish();
        } catch (Exception e) {
            reporterror(tag, e.toString());
            e.printStackTrace();
        }
    }
}
