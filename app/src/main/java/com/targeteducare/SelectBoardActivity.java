package com.targeteducare;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.WindowManager;

import com.targeteducare.Adapter.GetCategoryBoardAdapter;
import com.targeteducare.Classes.GetCategoryBoard;
import com.targeteducare.Classes.GetCategorySubBoard;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class SelectBoardActivity extends Activitycommon {

    private GetCategoryBoardAdapter getCategoryBoardAdapter;

    ArrayList<GetCategoryBoard> getCategoryBoardsdata;

    private RecyclerView.Adapter adapter;
    private RecyclerView.Adapter adapterforboard;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerView;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        try {
            screenshot_capture_permission();
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_select_board);

            preferences = PreferenceManager.getDefaultSharedPreferences(SelectBoardActivity.this);
            editor = preferences.edit();

            registerreceiver();


            tag = this.getClass().getSimpleName();

            recyclerView = findViewById(R.id.recyclerviewforboard);
            layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);

            getCategoryBoardsdata = new ArrayList<GetCategoryBoard>();

            getCategoryBoardAdapter = new GetCategoryBoardAdapter(SelectBoardActivity.this, getCategoryBoardsdata);
            recyclerView.setAdapter(getCategoryBoardAdapter);


        /*selectBoardModels = new ArrayList<SelectBoardModel>();
        for(int i = 0; i< Board.board.length; i++){
            SelectBoardModel data = new SelectBoardModel(Board.board[i]); //make changes

            ArrayList<SubBoardDataModel> subBoardDataModels = new ArrayList<>();
            for(int j = 0; j< SubBoard.subboard.length; j++){

                SubBoardDataModel subBoardData = new SubBoardDataModel(SubBoard.subboard[i]);//make changes
                subBoardDataModels.add(subBoardData);
            }

            data.setSubBoards(subBoardDataModels);
            selectBoardModels.add(data);
        }
        adapter = new SelectBoardAdapter(SelectBoardActivity.this,selectBoardModels);
        recyclerView.setAdapter(adapter);*/

            JSONObject obj = new JSONObject();
            JSONObject mainobj = new JSONObject();
            try {
                obj.put("Type", "Category");
                mainobj.put("FilterParameter", obj.toString());
            } catch (Exception e) {
                reporterror(tag, e.toString());
                e.printStackTrace();
            }
            ConnectionManager.getInstance(SelectBoardActivity.this).getcategory(mainobj.toString());
        } catch (Exception e) {
            reporterror(tag, e.toString());
            e.printStackTrace();
        }
    }


    @Override
    protected void onResponsed(int statuscode, int accesscode, String data) {
        super.onResponsed(statuscode, accesscode, data);
        try {
            if (statuscode == Constants.STATUS_OK) {
                if (accesscode == Connection.GETCATEGORY.ordinal()) {
                    Log.e("res", "res " + GlobalValues.TEMP_STR);

                    JSONObject jsonObject = new JSONObject(GlobalValues.TEMP_STR);
                    JSONArray subroot = jsonObject.getJSONObject("root").getJSONArray("subroot");
                    //Gson gson = new Gson();
                    //Type type = new TypeToken<ArrayList<GetCategoryBoard>>(){}.getType();
                    //ArrayList<GetCategoryBoard> CategoryBoarddata = gson.fromJson(subroot.toString(),type);

                    for (int i = 0; i < subroot.length(); i++) {
                        JSONObject c = subroot.getJSONObject(i);

                        String totalrecord_board = c.getString("TotalRecord");
                        String id_board = c.getString("Id");
                        String name_board = c.getString("Name");
                        String abbr_board = c.getString("Abbr");
                        String description_board = c.getString("Description");
                        String parentid_board = c.getString("ParentId");
                        String deleted_board = c.getString("Deleted");
                        String subjectid_board = c.getString("SubjectId");
                        String unitidrecord_board = c.getString("UnitId");
                        String chapterid_board = c.getString("ChapterId");


                        if (subroot != null) {
                            GetCategoryBoard cat = new GetCategoryBoard(totalrecord_board, id_board, name_board,
                                    abbr_board, description_board, parentid_board, deleted_board, subjectid_board,
                                    unitidrecord_board, chapterid_board);
                            //getCategoryBoardsdata.add(cat);
                            try {
                                JSONArray jsonsubcategory = subroot.getJSONObject(i).getJSONArray("SubCategory");
                                //Log.e("this is the mainarray: ",c.toString());

                                ArrayList<GetCategorySubBoard> getCategorySubBoardsdata = new ArrayList<>();

                                for (int j = 0; j < jsonsubcategory.length(); j++) {
                                    JSONObject c1 = jsonsubcategory.getJSONObject(j);
                                    //Log.e("this is the sub array: ",c1.toString());

                                    String id_subboard = c1.getString("Id");
                                    String name_subboard = c1.getString("Name");
                                    String deleted_subboard = c1.getString("Deleted");
                                    String abbr_subboard = c1.getString("Abbr");
                                    String description_subboard = c1.getString("Description");
                                    String subjectid_subboard = c1.getString("SubjectId");
                                    String unitid_subboard = c1.getString("UnitId");
                                    String chapterid_subboard = c1.getString("ChapterId");
                                    String parentid_subboard = c1.getString("ParentId");

                                    GetCategorySubBoard subcat = new GetCategorySubBoard(id_subboard, name_subboard,
                                            deleted_subboard, abbr_subboard, description_subboard, subjectid_subboard,
                                            unitid_subboard, chapterid_subboard, parentid_subboard);
                                    getCategorySubBoardsdata.add(subcat);

                                }
                                cat.setGetCategorySubBoard(getCategorySubBoardsdata);
                            } catch (Exception e) {
                                reporterror(tag, e.toString());
                                e.printStackTrace();
                            }
                            getCategoryBoardsdata.add(cat);
                            getCategoryBoardAdapter.notifyDataSetChanged();
                        } else {

                        }
                    }
                }
            }
        } catch (Exception e) {
            reporterror(tag, e.toString());
            e.printStackTrace();
        }
    }

    public void referTo(int position, String boardvalue) {


        try {

            //Toast.makeText(context, "Value is: "+getCategoryBoardsdata.get(position).getGetCategorySubBoard().size(), Toast.LENGTH_SHORT).show();
            GlobalValues.studentProfile.setBoard_name(boardvalue);
            Gson gson = new Gson();

            String jsonstudentprofile = gson.toJson(GlobalValues.studentProfile);
            editor.putString("studentprofiledetails", jsonstudentprofile);
            editor.apply();

            if (getCategoryBoardsdata.get(position).getGetCategorySubBoard().size() != 0) {
                Intent ipassboard = new Intent(SelectBoardActivity.this, BoardSubtypeSelection.class);
                ipassboard.putExtra("textviewname", boardvalue);

                String temp = GlobalValues.studentProfile.getBoard_name();
                Log.e("entered in bsts ", temp);
                ipassboard.putExtra("customdata", getCategoryBoardsdata.get(position).getGetCategorySubBoard());
                startActivity(ipassboard);
                finish();
            } else {
                Intent igridmain = new Intent(SelectBoardActivity.this, GridMainActivity.class);
                igridmain.putExtra("textviewname", boardvalue);

                String temp = GlobalValues.studentProfile.getBoard_name();
                Log.e("entered in grid main ", temp);
                startActivity(igridmain);
                finish();
            }


        } catch (Exception e) {
            reporterror(tag, e.toString());
            e.printStackTrace();
        }
    }


}
