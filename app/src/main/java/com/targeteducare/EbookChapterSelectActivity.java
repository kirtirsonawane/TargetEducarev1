package com.targeteducare;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.targeteducare.Adapter.EbookChapterAdapter;
import com.targeteducare.Classes.EbookChapter;
import com.targeteducare.Classes.EbookContentDetails;
import com.targeteducare.Classes.EbookDetails;
import com.targeteducare.database.DatabaseHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class EbookChapterSelectActivity extends Activitycommon {

    ArrayList<EbookChapter> ebookChapters = new ArrayList<>();
    EbookChapterAdapter ebookChapterAdapter;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView recyclerview_chapter;
    EbookDetails ebookDetails;
    String type_ebook = "";
    int ebook_id = 0;
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

            b = getIntent().getExtras();
            if (b != null) {
                if (b.containsKey("ebookdetails")) {
                    ebookDetails = (EbookDetails) b.getSerializable("ebookdetails");

                    try {

                        JSONObject obj = new JSONObject();
                        JSONObject mainobj = new JSONObject();

                        obj.put("Id", ebookDetails.getId());
                        obj.put("Type", ebookDetails.getType());
                        obj.put("ImagePath", URLS.url_ebook_content());
                        mainobj.put("FilterParameter", obj.toString());

                        ConnectionManager.getInstance(EbookChapterSelectActivity.this).ebook_contentget(mainobj.toString(), ebookDetails.getId(), ebookDetails.getType());

                        ebook_id = ebookDetails.getId();
                        type_ebook = ebookDetails.getType();

                    } catch (Exception e) {
                        reporterror(tag, e.getMessage());
                        e.printStackTrace();
                    }

                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onResponsed(int statuscode, int accesscode, String data) {
        super.onResponsed(statuscode, accesscode, data);

        if (statuscode == Constants.STATUS_OK) {
            if (accesscode == Connection.GET_EBOOKCONTENT.ordinal()) {

                Log.e("ebook content res ", GlobalValues.TEMP_STR);
                try {

                    JSONObject obj = new JSONObject(GlobalValues.TEMP_STR);
                    JSONObject root = obj.optJSONObject("root");

                    Object subrootcheck = root.opt("subroot");

                    if (subrootcheck instanceof JSONArray) {

                        JSONArray subroot = root.optJSONArray("subroot");
                        for (int i = 0; i < subroot.length(); i++) {
                            JSONObject c = subroot.optJSONObject(i);
                            ebookChapters.add(new EbookChapter(c));

                        }

                    } else {

                        JSONObject subroot = root.optJSONObject("subroot");
                        ebookChapters.add(new EbookChapter(subroot));
                    }

                    ebookChapterAdapter = new EbookChapterAdapter(EbookChapterSelectActivity.this, ebookChapters, lang);
                    recyclerview_chapter.setAdapter(ebookChapterAdapter);
                    ebookChapterAdapter.notifyDataSetChanged();

                } catch (Exception e) {
                    //reporterror(tag, e.getMessage());
                    e.printStackTrace();
                }

            }else if(accesscode == Connection.GET_EBOOKCONTENT_EXCEPTION.ordinal()){

                try {

                    //Log.e("ebook id", String.valueOf(ebook_id));
                    JSONArray array = DatabaseHelper.getInstance(EbookChapterSelectActivity.this).get_contentdetails(type_ebook, ebook_id);
                    Log.e("array rec", array.toString());

                    JSONObject obj = array.optJSONObject(0);

                    String a = obj.optString("JSONDATA");
                    JSONArray jsonArr = new JSONArray(a);

                    for (int i = 0; i < jsonArr.length(); i++) {

                        JSONObject a1 = jsonArr.optJSONObject(i);
                        ebookChapters.add(new EbookChapter(a1));
                    }

                    ebookChapterAdapter = new EbookChapterAdapter(EbookChapterSelectActivity.this, ebookChapters, lang);
                    recyclerview_chapter.setAdapter(ebookChapterAdapter);
                    ebookChapterAdapter.notifyDataSetChanged();

                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }

    }

    public void gotochapterselect(ArrayList<EbookContentDetails> ebookContentDetails) {

        Intent i = new Intent(EbookChapterSelectActivity.this, EBookContentDisplayActivity.class);
        i.putExtra("ebookcontent", ebookContentDetails);
        i.putExtra("typebook", ebookDetails.getType());
        startActivity(i);

    }
}

