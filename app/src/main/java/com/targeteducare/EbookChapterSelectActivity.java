package com.targeteducare;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.targeteducare.Adapter.EbookChapterAdapter;
import com.targeteducare.Classes.EbookChapterContentDetails;
import com.targeteducare.database.DatabaseHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class EbookChapterSelectActivity extends Activitycommon {

    ArrayList<EbookChapterContentDetails> ebookContentDetails = new ArrayList<>();
    EbookChapterAdapter ebookChapterAdapter;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView recyclerview_chapter;
    int ebook_id = 0;
    int chapterid = 0;
    int unitid = 0;
    String ebooktype = "";
    Bundle b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        try {
            super.onCreate(savedInstanceState);
            screenshot_capture_permission();
            loadLocale();
            setContentView(R.layout.activity_ebook_chapter_select);

            setmaterialDesign();
            back();

            recyclerview_chapter = findViewById(R.id.recyclerview_chapter);

            layoutManager = new LinearLayoutManager(EbookChapterSelectActivity.this);
            recyclerview_chapter.setLayoutManager(layoutManager);

            try {
                b = getIntent().getExtras();
                if (b != null) {
                    if (b.containsKey("ebookcontentunitid")) {
                        unitid = b.getInt("ebookcontentunitid");
                        //ebookContentDetails = (ArrayList<EbookChapterContentDetails>) b.getSerializable("ebookcontent");

                    }
                    if (b.containsKey("contentebookid")) {
                        ebook_id = b.getInt("contentebookid");
                    }
                    if (b.containsKey("ebooktype")) {
                        ebooktype = b.getString("ebooktype");
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {

                JSONArray array = DatabaseHelper.getInstance(EbookChapterSelectActivity.this).get_contentdetails(ebooktype, ebook_id, unitid);

                if (array.length() > 0) {

                    for (int i = 0; i < array.length(); i++) {

                        JSONObject obj = array.optJSONObject(i);
                        String a = obj.optString("JSONDATA");
                        JSONArray arr = new JSONArray(a);

                        for (int j = 0; j < arr.length(); j++) {

                            JSONObject objectdetails = arr.optJSONObject(j);
                            ebookContentDetails.add(new EbookChapterContentDetails(objectdetails));

                        }

                    }

                    ebookChapterAdapter = new EbookChapterAdapter(EbookChapterSelectActivity.this, ebookContentDetails, lang);
                    recyclerview_chapter.setAdapter(ebookChapterAdapter);
                    ebookChapterAdapter.notifyDataSetChanged();

                    getpagedetails();


                }

            } catch (Exception e) {
                e.printStackTrace();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void gotochapterselect(EbookChapterContentDetails ebookContentDetails) {

        try {

            unitid = ebookContentDetails.getUnitid();
            ebook_id = ebookContentDetails.getEbookId();
            chapterid = ebookContentDetails.getChapterid();
            Intent i = new Intent(EbookChapterSelectActivity.this, EBookContentDisplayActivity.class);
            i.putExtra("chapterid", chapterid);
            i.putExtra("unitid", unitid);
            i.putExtra("ebookid", ebook_id);
            //i.putExtra("ebookcontentselect", ebookContentDetails);
            i.putExtra("typebook", ebooktype);
            startActivity(i);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void getpagedetails() {

        try {
            JSONArray array = DatabaseHelper.getInstance(EbookChapterSelectActivity.this).get_pagedetails1(ebook_id/*, unitid*/);
            Log.e("arr ", ebook_id + " " + unitid + " " + chapterid + " " + array.length());
            if (array.length() > 0) {
                for (int i = 0; i < array.length(); i++) {
                    JSONObject obj = array.optJSONObject(i);
                    if (obj.has("chapterprogress")) {
                        //Log.e("progress is ", obj.optInt("chapterprogress") + "");
                        ebookContentDetails.get(i).setProgress(obj.optInt("chapterprogress"));
                    }

                    if(obj.has("lastvisitedpage")){
                        ebookContentDetails.get(i).setLastvisitedpage(obj.optInt("lastvisitedpage")+1);
                    }
                }
                ebookChapterAdapter.notifyDataSetChanged();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getpagedetails();
    }
}

