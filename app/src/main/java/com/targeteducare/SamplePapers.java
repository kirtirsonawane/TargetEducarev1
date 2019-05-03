package com.targeteducare;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.targeteducare.Adapter.PaperReadorDownloadAdapter;
import com.targeteducare.Classes.PaperModel;

import org.json.JSONObject;

import java.util.ArrayList;

public class SamplePapers extends Activitycommon {

    private ArrayList<PaperModel> arrayListdata;
    private PaperReadorDownloadAdapter adapter;
    RecyclerView recyclerView;
    CheckBox Sample_Papers_Videos;
    CheckBox Sample_Papers_E_Books;
    String data = "";
    public static String sdata = "";
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_papers);
        Sample_Papers_E_Books = (CheckBox) findViewById(R.id.sample_papers_e_books);
        Sample_Papers_Videos = (CheckBox) findViewById(R.id.sample_papers_videos);
        arrayListdata = new ArrayList<>();
        setmaterialDesign();
        setTitle("E-books");
        back();
        JSONObject jsonObject = new JSONObject();
        JSONObject jsonObject1 = new JSONObject();

        // get the reference of RecyclerView
        recyclerView = findViewById(R.id.recyclerviewforsamplepaper);

        // set a GridLayoutManager with default vertical orientation and 2 number of columns
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager); // set LayoutManager to RecyclerView
        //    adapter = new PaperReadorDownloadAdapter(SamplePapers.this,arrayListdata);
        //recyclerView.setAdapter(adapter);
        String Student_id = Constants.projectid;

        try {
            jsonObject.put("StudentId", Student_id);
            jsonObject1.put("FilterParameter", jsonObject.toString());
            Log.e("parameters ::  :: ", jsonObject1.toString());
            ConnectionManager.getInstance(SamplePapers.this).getsamplepapers(jsonObject1.toString());
        } catch (Exception e) {
            e.printStackTrace();


        }





        Sample_Papers_Videos.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
              updatedata(searchView.getQuery().toString());

           /*  if (isChecked == true) {
                 data=data+" "+String.valueOf(Sample_Papers_Videos.getText());
                 Log.e("data video if::: ",data);
               sdata=String.valueOf(Sample_Papers_Videos.getText());
               //  Log.e("data video if::sdata: ",sdata);
                    adapter.getFilter().filter(data);

               }
             else if (isChecked==false){
                 data=data.replace(Sample_Papers_Videos.getText(),"");
                 Log.e("data videos else::: ",data);
                 adapter.getFilter().filter(data);
             }*/
            }
        });


        Sample_Papers_E_Books.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                updatedata(searchView.getQuery().toString());
              /*  if (isChecked == true) {
                    data=data+" "+String.valueOf(Sample_Papers_E_Books.getText().toString());
                    sdata=String.valueOf(Sample_Papers_E_Books.getText());
                    Log.e("data ebook if::: ",data);
                    adapter.getFilter().filter(data);

                }
                else if (isChecked==false){
                    data=data.replace(Sample_Papers_E_Books.getText().toString(),"");
                    Log.e("data ebooks else::: ",data);
                    adapter.getFilter().filter(data);
                }*/
            }
        });
       /* Sample_Papers_Videos.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked==true){
                    adapter.getFilter().filter(Sample_Papers_Videos.getText());
                }
            }
        });*/

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);

        searchView = (SearchView) searchItem.getActionView();

        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                updatedata(newText);
                //if(Sample_Papers_Videos.isChecked()){
                //  sdata = String.valueOf(Sample_Papers_Videos.getText());
                Log.e("in checked :: ", sdata);
                //  adapter.getFilter().filter(newText);
                // }
                /*else {
                    adapter.getFilter().filter(newText);
                }*/

                return false;
            }
        });


        return true;
    }





    public void updatedata(String searchkey) {
        sdata = "";
        if (Sample_Papers_E_Books.isChecked()) {
            sdata = Sample_Papers_E_Books.getText().toString();
        }

        if(Sample_Papers_Videos.isChecked())
        {
            if(sdata.length()==0)
            {
                sdata = Sample_Papers_Videos.getText().toString();
            }else {
                sdata =sdata+" "+ Sample_Papers_Videos.getText().toString();

            }
        }

        adapter.getFilter().filter(searchkey);

    }

    @Override
    protected void onResponsed(int statuscode, int accesscode, String data) {
        super.onResponsed(statuscode, accesscode, data);
        if (statuscode == Constants.STATUS_OK) {
            if (accesscode == Connection.SAMPLEPAPERS.ordinal()) {
                data = GlobalValues.TEMP_STR;
                Log.e("data  ::", data);
                if (data != null) {
                    try {

                        JSONObject jsonObject2 = new JSONObject(data);
                        JSONObject jsonObject3 = jsonObject2.getJSONObject("root");

                        arrayListdata.addAll(PaperModel.getjson(jsonObject3));
                        Log.e("Array     ", arrayListdata.size() + " " + arrayListdata.toString());
                        adapter = new PaperReadorDownloadAdapter(SamplePapers.this, arrayListdata);
                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }



   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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
        return true;
    }*/
}
