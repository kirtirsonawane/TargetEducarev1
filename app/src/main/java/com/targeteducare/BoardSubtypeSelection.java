package com.targeteducare;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.targeteducare.Adapter.GetCategorySubBoardAdapter;
import com.targeteducare.Classes.GetCategorySubBoard;
import com.google.gson.Gson;

import java.util.ArrayList;

;

public class BoardSubtypeSelection extends AppCompatActivity {

    TextView tv_boarddisplay;
    ArrayList<GetCategorySubBoard> getCategorySubBoards;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;


    Bundle bdl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_subtype_selection);

        preferences = PreferenceManager.getDefaultSharedPreferences(BoardSubtypeSelection.this);
        editor = preferences.edit();

        tv_boarddisplay = findViewById(R.id.boarddisplay);

        recyclerView = findViewById(R.id.recyclerviewforsubboardtype);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        bdl = getIntent().getExtras();
        getCategorySubBoards = (ArrayList<GetCategorySubBoard>) bdl.getSerializable("customdata");

        tv_boarddisplay.setText("Am Studying in\n" + bdl.getString("textviewname"));


        adapter = new GetCategorySubBoardAdapter(BoardSubtypeSelection.this, getCategorySubBoards);
        recyclerView.setAdapter(adapter);

    }

    public void referTo(int position) {

        Gson gson = new Gson();
        editor.putBoolean("isloginv1", true);

        String subBoard = getCategorySubBoards.get(position).getName_subboard();
        GlobalValues.studentProfile.setSubboard_name(subBoard);

        String jsonstudentprofile = gson.toJson(GlobalValues.studentProfile);

        editor.putString("studentprofiledetails", jsonstudentprofile);
        editor.apply();
        Intent intent = new Intent(BoardSubtypeSelection.this, GridMainActivity.class);


        Log.e("sending value subboard",subBoard);
        intent.putExtra("subboardname", subBoard);
        startActivity(intent);
        finish();
    }
}
