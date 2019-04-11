package com.targeteducare;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.targeteducare.Adapter.CustomAdapter;
import com.targeteducare.Classes.ArrData;
import com.targeteducare.Classes.Menu;
import com.targeteducare.Classes.QnaQuestionModel;

import java.util.ArrayList;

public class GridMainActivity extends Activitycommon{
    private static ArrayList<QnaQuestionModel> dataQuestionmodel;
    private static ArrayList<Menu> data;
    private CustomAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerView1;

    //TextView viewmore;

    SharedPreferences preferences;
    SharedPreferences.Editor edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_main);

        setmaterialDesign();
        setTitle("Target Educare");

        //viewmore = findViewById(R.id.tv_viewmore);

        preferences = PreferenceManager.getDefaultSharedPreferences(GridMainActivity.this);
        edit = preferences.edit();

        if (preferences.getBoolean("isloginv1", false)) {
            edit.putBoolean("isloginv1",true);
            edit.apply();
        }

        /*viewmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iqnafeed = new Intent(GridMainActivity.this, QnAFeed.class);
                startActivity(iqnafeed);
            }
        });*/

        // get the reference of RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerviewforgrid);

        // set a GridLayoutManager with default vertical orientation and 2 number of columns
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),4);
        recyclerView.setLayoutManager(gridLayoutManager); // set LayoutManager to RecyclerView

        data = new ArrayList<Menu>();
        for(int i = 0; i< ArrData.gridnames.length; i++){

            Menu arrData = new Menu(ArrData.gridnames[i], ArrData.drawableArray[i]);
            data.add(arrData);
        }
        adapter = new CustomAdapter(GridMainActivity.this,data);
        recyclerView.setAdapter(adapter);


        /*recyclerView1 = findViewById(R.id.recyclerviewforqna);
        recyclerView1.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView1.setLayoutManager(layoutManager);
        dataQuestionmodel = new ArrayList<QnaQuestionModel>();
        for(int i1 = 0; i1< QnaQuestionData.main_question.length; i1++){

            QnaQuestionModel qdata = new QnaQuestionModel(
                    QnaQuestionData.main_question[i1],QnaQuestionData.followers[i1],QnaQuestionData.answers[i1]);

            ArrayList<QnaDataModel> datamod = new ArrayList<>();

            for(int j1 = 0; j1< QnAData.profile_pics.length; j1++){

                QnaDataModel qdatamod = new QnaDataModel(
                        QnAData.profile_pics[j1],QnAData.name[j1],QnAData.time[j1],QnAData.paragraphs[j1]);
                datamod.add(qdatamod);
            }

            qdata.setQndataset(datamod);
            dataQuestionmodel.add(qdata);
        }
        adapter = new QnaQuestionAdapter(GridMainActivity.this,dataQuestionmodel);
        recyclerView1.setAdapter(adapter);*/

    }


    public void referto(int position) {
        switch (position){
            case 0:
                Intent ipractest = new Intent(GridMainActivity.this,PracticeTestActivity.class);
                startActivity(ipractest);
                break;
            case 1:
                Intent imocktest = new Intent(GridMainActivity.this,PracticeTestActivity.class);
                startActivity(imocktest);
                break;
            case 2:
                Intent imnthranktest = new Intent(GridMainActivity.this,PracticeTestActivity.class);
                startActivity(imnthranktest);
                /*Intent iexamlist = new Intent(GridMainActivity.this, LoginActivity.class);
                startActivity(iexamlist);*/
                break;
            case 3:
                Intent ismppaper = new Intent(GridMainActivity.this, SamplePapers.class);
                startActivity(ismppaper);
                break;
            case 4:
                break;
            case 5:
                break;
            case 6:
                Intent imainsubjselect = new Intent(GridMainActivity.this, MainSubjectSelectionActivity.class);
                startActivity(imainsubjselect);
                break;
            case 7:
                Intent iprof = new Intent(GridMainActivity.this, UserProfile.class);
                startActivity(iprof);
                break;
            case 8:
                Intent iengcolg = new Intent(GridMainActivity.this, EngineeringColleges.class);
                startActivity(iengcolg);
                break;
            case 9:
                Intent iqnafeed = new Intent(GridMainActivity.this, QnAFeed.class);
                startActivity(iqnafeed);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.logout_menu,menu);

        Drawable icon_color_change = menu.getItem(0).getIcon(); // change 0 with 1,2 ...
        icon_color_change.mutate();
        icon_color_change.setColorFilter(getResources().getColor(R.color.White), PorterDuff.Mode.SRC_IN);

        MenuItem logout_button = menu.findItem(R.id.logout);
        logout_button.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                edit.putBoolean("isloginv1",false);
                edit.apply();
                Intent login = new Intent(GridMainActivity.this,LoginV1.class);
                startActivity(login);
                finish();
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
