package com.targeteducare;


import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;

import com.targeteducare.Adapter.CustomAdapterEngColg;
import com.targeteducare.Classes.EngColgData;
import com.targeteducare.Classes.EngColgDataModel;

import java.util.ArrayList;

public class EngineeringCollegesActivity extends Activitycommon {

    private ArrayList<EngColgDataModel> data;
    private CustomAdapterEngColg adapter;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_engineering_colleges);

        setmaterialDesign();
        setTitle("Engineering Colleges");
        back();

        recyclerView = findViewById(R.id.recyclerviewforengcolg);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        data = new ArrayList<EngColgDataModel>();
        for(int i = 0; i< EngColgData.logo_img.length; i++){
            data.add(new EngColgDataModel(
                    EngColgData.logo_img[i],EngColgData.rating[i],EngColgData.reviews[i],EngColgData.college_name[i],
                    EngColgData.established_year[i],EngColgData.courses[i],EngColgData.institute_type[i],EngColgData.exam_name[i]
            ));
        }
        adapter = new CustomAdapterEngColg(EngineeringCollegesActivity.this,data);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        Drawable icon_color_change = menu.getItem(0).getIcon(); // change 0 with 1,2 ...
        icon_color_change.mutate();
        icon_color_change.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_IN);

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
        return true;
    }
}
