package com.targeteducare;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.onesignal.OneSignal;
import com.targeteducare.Classes.SplashModel;
import com.targeteducare.database.DatabaseHelper;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;

public class ActivityDescription extends Activitycommon {
    ImageView textView_img;
    TextView textview_description;
    String i, Description, ID;
    DatabaseHelper databaseHelper;
    String promotion_description = null;
    String promotion_id = null;
    boolean fav=false;
    ArrayList<SplashModel> arrayList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        screenshot_capture_permission();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);

        setmaterialDesign();



        back();
        toolbar.setTitleMargin(25, 10, 10, 10);
        setTitle("PEAK");
        loadLocale();
        Intent intent = getIntent();
        i = intent.getStringExtra("i");
        Description = intent.getStringExtra("Description");
        ID = intent.getStringExtra("ID");
        WebView webView = (WebView) findViewById(R.id.webview_description);
        textview_description = (TextView) findViewById(R.id.title_description);
        textView_img = (ImageView) findViewById(R.id.description_fav);
        databaseHelper = new DatabaseHelper((ActivityDescription.this));

        String style = "<style type=\"text/css\">\n" +
                "@font-face {\n" +
                "    font-family: Symbol;\n" +
                "    src:url(\"file:///android_asset/fonts/symbol.ttf\"), url(\"file:///android_asset/fonts/symbol_webfont.woff\"), url(\"file:///android_asset/fonts/symbol_webfont.woff2\");\n" +
                "}\n" +
                "@font-face {\n" +
                "    font-family: Calibri;\n" +
                "    src: url(\"file:///android_asset/fonts/calibri.ttf\")\n" +
                "}@font-face {\n" +
                "    font-family: AkrutiDevPriya;\n" +
                "    src: url(\"file:///android_asset/fonts/akrutidevpriyanormal.ttf\")\n" +
                "}@font-face {\n" +
                "    font-family: Times New Roman,serif;\n" +
                "    src: url(\"file:///android_asset/fonts/timesnewroman.ttf\")\n" +
                "}\n" +
                "\n" +
                "</style>";
        //   textView_img.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_star_white_24dp, 0, 0, 0);
        textview_description.setTypeface(Fonter.getTypefacebold(context));
        textview_description.setText(getResources().getString(R.string.title)+" :"+ i);
        webView.loadDataWithBaseURL("file:///",  style + Description, "text/html", "utf-8", null);
        textView_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setfav();

            }
        });
        Log.e("promotion id", " " + promotion_id);
        getfav();
    }
    private void getfav() {
        try {

            JSONArray jsonArray = databaseHelper.getpromotion();
            for (int i = 0; i < jsonArray.length(); i++) {
                SplashModel splashModels = new SplashModel();
                JSONArray jsonArray1 = jsonArray.optJSONArray(i);

                if (jsonArray1 != null) {
                    Log.e("jsonArray ","is null 1 "/*+promotion_id*/);
                    String s = jsonArray1.getString(i);
                    JSONObject jsonObject = new JSONObject(s);
                    JSONObject jsonObject1 = jsonObject.optJSONObject("JSONDATA");
                    if (jsonObject1 != null) {

                   //     promotion_description = jsonObject.getString("JSONDATA");

                        splashModels.setId( jsonObject.getString("promotion_id"));
                        Log.e("jsonobj ","is notnull if 1 "+promotion_id);
                    } else if (jsonObject1 == null) {
                  //      promotion_description = "";
                        splashModels.setId("");
                        Log.e("jsonobj ","is null else 1 "+promotion_id);
                    }
                } else if(jsonArray1 == null) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    splashModels.setId( object.getString("promotion_id"));
//                    promotion_description = object.getString("JSONDATA");
                    Log.e("jsonArray ","is null 2 "+promotion_id);
                }
                arrayList.add(splashModels);
                check(splashModels);
            }
        //    Log.e("promotion description", " " + promotion_description);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void check(SplashModel splashModel) {
        try {


            if (splashModel.getId() != null && ID != null) {
                if (splashModel.getId().equalsIgnoreCase(ID)) {
                    Log.e("promotion id", " " + splashModel.getId());
                    //Log.e("promotion description", " " + promotion_description);
                    textView_img.setImageDrawable(getResources().getDrawable(R.drawable.ic_fav2));
                    fav = true;
                } else if (splashModel.getId() == null || splashModel.getId().equalsIgnoreCase("")) {
                    textView_img.setImageDrawable(getResources().getDrawable(R.drawable.ic_fav1));
                }
            } else {
                textView_img.setImageDrawable(getResources().getDrawable(R.drawable.ic_fav1));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void setfav() {
        try{
        if(fav==false) {
            JSONObject jsonObject = new JSONObject();
            ContentValues contentValues = new ContentValues();
            try {
                /* jsonObject.put("Id", ID);*/
                jsonObject.put("Title", i);
               // jsonObject.put("Description", Description);
                String data = jsonObject.toString();
               // contentValues.put(DatabaseHelper.JSONDATA, data);
                contentValues.put(DatabaseHelper.PROMOTION_ID, ID);
                textView_img.setImageDrawable(getResources().getDrawable(R.drawable.ic_fav2));
                Log.e("Contentvalues"," "+contentValues);
                databaseHelper.savedata(contentValues);

                fav=true;
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        else if (fav==true){
            textView_img.setImageDrawable(getResources().getDrawable(R.drawable.ic_fav1));
            databaseHelper.deletpromotion(ID);
            fav=false;
        }
    }catch (Exception e){
            e.printStackTrace();
        }
    }

   /* private void insert(String jsondata) {
        ContentValues contentValues= new ContentValues();

        Gson gson = new Gson();


        String data = gson.toJson(jsondata);
        contentValues.put(DatabaseHelper.JSONDATA,data);
        databaseHelper.savedata(contentValues);
    }*/
}
