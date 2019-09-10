package com.targeteducare;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import com.targeteducare.Adapter.EbookDetailsAdapter;
import com.targeteducare.Adapter.EbookSubjectAdapter;
import com.targeteducare.Classes.EbookDetails;
import com.targeteducare.Classes.EbookSubjects;
import com.targeteducare.database.DatabaseHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class EbookSubjectActivity extends Activitycommon {

    RecyclerView recyclerview_textviewselect, recyclerview_ebookdetails, recyclerview_chapter;
    EbookSubjectAdapter ebookSubjectAdapter;
    RecyclerView.LayoutManager layoutManager1, layoutManager2, layoutManager3;
    EbookDetailsAdapter ebookDetailsAdapter;
    ArrayList<EbookSubjects> ebookSubjects = new ArrayList<>();
    LinearLayout linearlayout_ebook;

    Bundle b;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        try {
            super.onCreate(savedInstanceState);
            loadLocale();
            screenshot_capture_permission();
            setContentView(R.layout.activity_ebook_subject);
            setmaterialDesign();
            back();

            tag = this.getClass().getSimpleName();

            recyclerview_textviewselect = findViewById(R.id.recyclerview_textviewselect);
            recyclerview_ebookdetails = findViewById(R.id.recyclerview_ebookdetails);
            recyclerview_chapter = findViewById(R.id.recyclerview_chapter);

            linearlayout_ebook = findViewById(R.id.linearlayout_ebook);

            layoutManager1 = new GridLayoutManager(EbookSubjectActivity.this, 3);
            recyclerview_textviewselect.setLayoutManager(layoutManager1);

            /*layoutManager1 = new LinearLayoutManager(EbookSubjectActivity.this, LinearLayoutManager.HORIZONTAL, true);
            recyclerview_textviewselect.setLayoutManager(layoutManager1);*/

            layoutManager2 = new LinearLayoutManager(EbookSubjectActivity.this);
            recyclerview_ebookdetails.setLayoutManager(layoutManager2);

            /*ebookDetailsAdapter = new EbookDetailsAdapter(EbookSubjectActivity.this, ebookDetails, lang);
            recyclerview_ebookdetails.setAdapter(ebookDetailsAdapter);*/

            b = getIntent().getExtras();

            if (b != null) {
                int video = b.getInt("video");
                ebookSubjectAdapter = new EbookSubjectAdapter(EbookSubjectActivity.this, ebookSubjects, video, lang);
                recyclerview_textviewselect.setAdapter(ebookSubjectAdapter);

            }

            ebook_service();


        } catch (Exception e) {
            reporterror(tag, e.getMessage());
            e.printStackTrace();
        }
    }

    private void ebook_service() {

        try {

            JSONObject obj = new JSONObject();
            JSONObject mainobj = new JSONObject();

            obj.put("StudentId", /*GlobalValues.student.getId()*/"6");
            //obj.put("StudentId", GlobalValues.student.getMobile());
            obj.put("ImagePath", URLS.image_url_ebooktemp());
            mainobj.put("FilterParameter", obj.toString());

            ConnectionManager.getInstance(EbookSubjectActivity.this).student_ebookget(mainobj.toString());

        } catch (Exception e) {
            reporterror(tag, e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    protected void onResponsed(int statuscode, int accesscode, String data) {
        super.onResponsed(statuscode, accesscode, data);

        if (statuscode == Constants.STATUS_OK) {

            if (accesscode == Connection.GET_STUDENTEBOOK.ordinal()) {

                //Log.e("student ebook res ", GlobalValues.TEMP_STR);
                try {

                    ebookSubjects.clear();
                    JSONObject obj = new JSONObject(GlobalValues.TEMP_STR);
                    JSONObject root = obj.optJSONObject("root");

                    Object subrootcheck = root.opt("subroot");

                    if (subrootcheck instanceof JSONArray) {

                        JSONArray subroot = root.optJSONArray("subroot");
                        for (int i = 0; i < subroot.length(); i++) {
                            JSONObject c = subroot.optJSONObject(i);
                            ebookSubjects.add(new EbookSubjects(c));

                        }

                    } else {

                        JSONObject subroot = root.optJSONObject("subroot");
                        ebookSubjects.add(new EbookSubjects(subroot));
                    }

                    ebookSubjectAdapter.notifyDataSetChanged();

                } catch (Exception e) {
                    reporterror(tag, e.getMessage());
                    e.printStackTrace();
                }

            } else if (accesscode == Connection.GET_STUDENTEBOOK_EXCEPTION.ordinal()) {

                try {

                    ebookSubjects.clear();
                    JSONArray array = DatabaseHelper.getInstance(EbookSubjectActivity.this).get_subjects();
                    //Log.e("array rec", array.toString());

                    JSONObject obj = array.optJSONObject(0);

                    String a = obj.optString("JSONDATA");
                    JSONArray jsonArr = new JSONArray(a);

                    for (int i = 0; i < jsonArr.length(); i++) {

                        JSONObject a1 = jsonArr.optJSONObject(i);
                        ebookSubjects.add(new EbookSubjects(a1));
                    }

                    ebookSubjectAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }


        }
    }

    public void goto_ontextviewselected(ArrayList<EbookDetails> ebookDetails) {

        try {
            ebookDetailsAdapter = new EbookDetailsAdapter(EbookSubjectActivity.this, ebookDetails, lang);
            recyclerview_ebookdetails.setAdapter(ebookDetailsAdapter);
            ebookDetailsAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void gotogetbeookcontent(EbookDetails ebookDetails) {

        try {
            Intent i = new Intent(EbookSubjectActivity.this, EbookUnitSelectionActivity.class);
            i.putExtra("ebookdetailsid", ebookDetails.getId());
            i.putExtra("ebookdetailstype", ebookDetails.getType());
            startActivity(i);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
