package com.targeteducare;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.targeteducare.Adapter.PaperReadorDownloadAdapter;
import com.targeteducare.Classes.PaperList;
import com.targeteducare.Classes.PaperModel;

import java.util.ArrayList;

public class SamplePapers extends Activitycommon {

    private ArrayList<PaperModel> data;
    private PaperReadorDownloadAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_papers);

        setmaterialDesign();
        back();

        // get the reference of RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerviewforsamplepaper);

        // set a GridLayoutManager with default vertical orientation and 2 number of columns
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),2);
        recyclerView.setLayoutManager(gridLayoutManager); // set LayoutManager to RecyclerView

        data = new ArrayList<PaperModel>();
        for(int i = 0; i< PaperList.papername.length; i++){

            PaperModel arrData = new PaperModel(PaperList.papername[i], PaperList.readordownload[i]);
            data.add(arrData);
        }
        adapter = new PaperReadorDownloadAdapter(SamplePapers.this,data);
        recyclerView.setAdapter(adapter);

    }
}
