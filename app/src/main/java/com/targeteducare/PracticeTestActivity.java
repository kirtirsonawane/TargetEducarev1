package com.targeteducare;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.targeteducare.Adapter.PracticeTestAdapter;
import com.targeteducare.Classes.PracticeDemo;
import com.targeteducare.Classes.PracticeTestModel;

import java.util.ArrayList;
import java.util.List;

public class PracticeTestActivity extends Activitycommon {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    private ArrayList<PracticeTestModel> practiceTestModels;
    private PracticeTestAdapter practiceTestAdapter;

    int progress = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice_test);

        setmaterialDesign();
        back();

        recyclerView = findViewById(R.id.recyclerviewforpracticetest);
        layoutManager = new LinearLayoutManager(PracticeTestActivity.this);
        recyclerView.setLayoutManager(layoutManager);

        practiceTestModels = new ArrayList<PracticeTestModel>();
        for(int i =0; i< PracticeDemo.topicname.length;i++){
            PracticeTestModel practicedata = new PracticeTestModel(PracticeDemo.topicnumber[i], PracticeDemo.topicname[i],
                    PracticeDemo.totalvideos[i], PracticeDemo.totalgoals[i],PracticeDemo.totalconcepts[i],
                    PracticeDemo.progress_bar_val[i]);
            practiceTestModels.add(practicedata);
        }

        practiceTestAdapter = new PracticeTestAdapter(PracticeTestActivity.this,practiceTestModels);
        recyclerView.setAdapter(practiceTestAdapter);
    }

    public void gotopracticetest(int i) {
        Intent icheckprogress = new Intent(PracticeTestActivity.this,ProgressReport.class);
        startActivity(icheckprogress);
    }

    public int incrementprogress(int i) {
        return progress++;
    }
}
