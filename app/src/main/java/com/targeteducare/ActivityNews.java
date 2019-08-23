package com.targeteducare;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import com.targeteducare.Adapter.AdapterNews;
import com.targeteducare.Adapter.AdapterNews2;
import com.targeteducare.Classes.SplashModel;
import com.targeteducare.database.DatabaseHelper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class ActivityNews extends Activitycommon {
    ArrayList<SplashModel> layouts = new ArrayList<>();
    ArrayList<SplashModel> layouts1 = new ArrayList<>();
    ArrayList<SplashModel> list = new ArrayList<>();
    RecyclerView recycle_promotion;
    RecyclerView recycle_news;
    CardView textView_promotion, textView_next;
    boolean expand_next = false;
    boolean expand_promotion = false;
    boolean click = false;
    DatabaseHelper databaseHelper;
    AdapterNews adapterNews;
    AdapterNews2 adapterNews1_promo;
    public String promoid = "";
    ArrayList<SplashModel> arrayList = new ArrayList<>();
    ArrayList<SplashModel> arrayList1 = new ArrayList<>();
    String tag = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            screenshot_capture_permission();
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_news);
            setmaterialDesign();
            back();

            toolbar.setTitleMargin(25, 10, 10, 10);
            setTitle("PEAK NEWS");
            tag = this.getClass().getSimpleName();
            GlobalValues.langs = getSharedPreferences("Settings", MODE_PRIVATE).getString("Current Language", "");

            loadLocale();
            recycle_promotion = (RecyclerView) findViewById(R.id.recycle_promotion);
            recycle_news = (RecyclerView) findViewById(R.id.recycle_news);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ActivityNews.this);
            recycle_promotion.setLayoutManager(linearLayoutManager);
            LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(ActivityNews.this);
            recycle_news.setLayoutManager(linearLayoutManager1);
            textView_next = (CardView) findViewById(R.id.card_news);
            textView_promotion = (CardView) findViewById(R.id.card_promotion);
            databaseHelper = new DatabaseHelper((ActivityNews.this));
            checkinternet();
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("Type", "");
                JSONObject jsonObject1 = new JSONObject();
                jsonObject1.put("FilterParameter", jsonObject.toString());
                ConnectionManager.getInstance(ActivityNews.this).getimages(jsonObject1.toString());
            } catch (JSONException e) {
                e.printStackTrace();
                reporterror(tag, e.toString());
            }


            textView_promotion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (expand_promotion == false) {
                  /*  AdapterNews adapterNews1=new AdapterNews(ActivityNews.this,layouts);
                    recycle_promotion.setAdapter(adapterNews1);*/
                            recycle_promotion.setVisibility(View.VISIBLE);
                            recycle_promotion.setNestedScrollingEnabled(false);
                            expand_promotion = true;
                            if (layouts.size() == 0) {
                                Toast.makeText(ActivityNews.this, getResources().getString(R.string.nodataavailable), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            recycle_promotion.setVisibility(View.GONE);
                            expand_promotion = false;
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            textView_next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (expand_next == false) {
                 /*   AdapterNews adapterNews=new AdapterNews(ActivityNews.this,layouts1);
                    recycle_news.setAdapter(adapterNews);*/
                            recycle_news.setVisibility(View.VISIBLE);
                            recycle_news.setNestedScrollingEnabled(false);
                            expand_next = true;

                            if (layouts1.size() == 0) {
                                Toast.makeText(ActivityNews.this, getResources().getString(R.string.nodataavailable), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            recycle_news.setVisibility(View.GONE);
                            expand_next = false;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkinternet() {
        try {
            if (!isOnline()) {
                try {
                    Toast.makeText(ActivityNews.this, getResources().getString(R.string.Connectiontimeout), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            /*Toast.makeText(ActivityNews.this, getResources().getString(R.string.Connectiontimeout), Toast.LENGTH_SHORT);*/
            return false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        try {
            MenuInflater menuInflater = getMenuInflater();
            menuInflater.inflate(R.menu.filtermenu, menu);
            MenuItem filter = menu.findItem(R.id.filter);
            filter.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    try {
                        if (click == false) {
                            getfav();
                            click = true;
                        } else if (click == true) {
                            promoid = "";
                            if (adapterNews != null)
                                adapterNews.getFilter().filter(promoid);

                            if (adapterNews1_promo != null)
                                adapterNews1_promo.getFilter().filter(promoid);
                            click = false;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        reporterror(tag, e.toString());
                    }
                    return false;
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.onCreateOptionsMenu(menu);
    }

    private void getfav() {
        try {
            JSONArray jsonArray = databaseHelper.getpromotion();
            for (int i = 0; i < jsonArray.length(); i++) {
                SplashModel splashModels = new SplashModel();
                JSONArray jsonArray1 = jsonArray.optJSONArray(i);

                if (jsonArray1 != null) {
                   // Log.e("jsonArray ", "is null 1 "/*+promotion_id*/);
                    String s = jsonArray1.getString(i);
                    JSONObject jsonObject = new JSONObject(s);
                    JSONObject jsonObject1 = jsonObject.optJSONObject("JSONDATA");
                    if (jsonObject1 != null) {
                        //     promotion_description = jsonObject.getString("JSONDATA");
                        splashModels.setId(jsonObject.getString("promotion_id"));
                        // Log.e("jsonobj ","is notnull if 1 "+promotion_id);
                    } else if (jsonObject1 == null) {
                        //      promotion_description = "";
                        splashModels.setId("");
                        //  Log.e("jsonobj ","is null else 1 "+promotion_id);
                    }
                } else if (jsonArray1 == null) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    splashModels.setId(object.getString("promotion_id"));
//                    promotion_description = object.getString("JSONDATA");
                    // Log.e("jsonArray ","is null 2 "+promotion_id);
                }
                list.add(splashModels);
                promoid = splashModels.getId() + " " + promoid;
                Log.e(" ", " " + promoid);
                adapterNews.getFilter().filter(promoid);
                adapterNews.notifyDataSetChanged();
                adapterNews1_promo.getFilter().filter(promoid);
                adapterNews1_promo.notifyDataSetChanged();
            }
            //    Log.e("promotion description", " " + promotion_description);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    /*private void check(String splashModel) {
        if (splashModel != null) {

            for (int i = 0; i < layouts.size(); i++) {
                SplashModel model = new SplashModel();
                if (splashModel.equalsIgnoreCase(layouts.get(i).getId())) {
                    Log.e("promotion id", " " + splashModel);
                    model.setId(layouts.get(i).getId());
                    model.setType(layouts.get(i).getType());
                    model.setCreatedDate(layouts.get(i).getCreatedDate());
                    model.setDescription(layouts.get(i).getDescription());
                    model.setTitle(layouts.get(i).getTitle());

                    model.setToDate(layouts.get(i).getToDate());

                    arrayList.add(model);
                    adapterNews.notifyDataSetChanged();

                } else  if (splashModel.equalsIgnoreCase(layouts1.get(i).getId()))
                    {
                        Log.e("promotion id1", " " + splashModel);
                        model.setId(layouts1.get(i).getId());
                        model.setType(layouts1.get(i).getType());
                        model.setCreatedDate(layouts1.get(i).getCreatedDate());
                        model.setDescription(layouts1.get(i).getDescription());
                        model.setTitle(layouts1.get(i).getTitle());

                        model.setToDate(layouts1.get(i).getToDate());

                        arrayList1.add(model);
                        adapterNews.notifyDataSetChanged();
                    }
                else{
                    Log.e("nothing to show ","");
                }

            }

            adapterNews = new AdapterNews(ActivityNews.this, arrayList1);
            recycle_news.setAdapter(adapterNews);
            adapterNews.notifyDataSetChanged();
            adapterNews1_promo = new AdapterNews2(ActivityNews.this, arrayList);
            recycle_promotion.setAdapter(adapterNews1_promo);


         *//*   for (int j = 0; j < layouts1.size(); j++) {
                SplashModel model = new SplashModel();
                if (splashModel.equalsIgnoreCase(layouts1.get(j).getId())) {
                    Log.e("promotion id1", " " + splashModel);
                    model.setId(layouts1.get(j).getId());
                    model.setType(layouts1.get(j).getType());
                    model.setCreatedDate(layouts1.get(j).getCreatedDate());
                    model.setDescription(layouts1.get(j).getDescription());
                    model.setTitle(layouts1.get(j).getTitle());

                    model.setToDate(layouts1.get(j).getToDate());


                }*//*
     *//* else {
                    Log.e("null"," splash2 ");
                }*//*

               // Log.e("null"," splash2size "+arrayList1.size());
            }


      //      adapterNews1.notifyDataSetChanged();
        }*/


    private void check(String splashModel) {
        if (splashModel != null) {
            Log.e("splashcheck ", " " + splashModel);
            //   adapterNews = new AdapterNews(ActivityNews.this, layouts1);
            adapterNews.getFilter().filter(promoid);
            adapterNews.notifyDataSetChanged();
         /*   adapterNews1_promo.getFilter().filter(promoid);
            adapterNews1_promo.notifyDataSetChanged()*/
        }


           /* adapterNews = new AdapterNews(ActivityNews.this, arrayList1);
            recycle_news.setAdapter(adapterNews);
            adapterNews.notifyDataSetChanged();
            adapterNews1_promo = new AdapterNews2(ActivityNews.this, arrayList);
            recycle_promotion.setAdapter(adapterNews1_promo);
*/
    }


    @Override
    protected void onResponsed(int statuscode, int accesscode, String data) {
        super.onResponsed(statuscode, accesscode, data);
        try {

            if (statuscode == Constants.STATUS_OK) {
                if (accesscode == Connection.SPLASHSCREEN1.ordinal()) {
                    data = GlobalValues.TEMP_STR;
                    if (data != null) {
                        try {
                            JSONObject jsonObject2 = new JSONObject(data);
                            JSONObject jsonObject = new JSONObject();
                            JSONObject jsonObject1 = new JSONObject();
                            jsonObject = jsonObject2.getJSONObject("root");

                            JSONObject object = jsonObject.optJSONObject("subroot");

                            if (object != null) {
                                jsonObject1 = jsonObject.getJSONObject("subroot");
                                SplashModel splashModel = new SplashModel();
                                if (jsonObject1.has("Id")) {
                                    splashModel.setId(jsonObject1.getString("Id"));
                                } else {
                                    splashModel.setId("");
                                }


                                if (jsonObject1.has("Type")) {
                                    splashModel.setType(jsonObject1.getString("Type"));
                                } else {
                                    splashModel.setType("");
                                }


                                if (jsonObject1.has("Description")) {
                                    splashModel.setDescription(jsonObject1.getString("Description"));
                                } else {
                                    splashModel.setDescription("Not available");
                                }


                                if (jsonObject1.has("FromDate")) {
                                    splashModel.setFromDate(jsonObject1.getString("FromDate"));
                                } else {
                                    splashModel.setFromDate("Not available");
                                }


                                if (jsonObject1.has("Title")) {
                                    splashModel.setTitle(jsonObject1.getString("Title"));
                                } else {
                                    splashModel.setTitle("Not available");
                                }


                                if (jsonObject1.has("ToDate")) {
                                    splashModel.setToDate(jsonObject1.getString("ToDate"));
                                } else {
                                    splashModel.setToDate("Not available");
                                }


                                if (jsonObject1.has("CreatedDate")) {
                                    splashModel.setCreatedDate(jsonObject1.getString("CreatedDate"));
                                } else {
                                    splashModel.setCreatedDate("Not available");
                                }


                                if (jsonObject1.has("Description_Marathi")) {
                                    splashModel.setDescription_Marathi(jsonObject1.getString("Description_Marathi"));
                                } else {
                                    splashModel.setDescription_Marathi("Not available");
                                }


                                if (jsonObject1.has("Title_Marathi")) {
                                    splashModel.setTitle_Marathi(jsonObject1.getString("Title_Marathi"));
                                } else {
                                    splashModel.setTitle_Marathi("Not available");
                                }


                                if (splashModel.getType().equalsIgnoreCase("Promotion")) {
                                    layouts.add(splashModel);
                                } else if (splashModel.getType().equalsIgnoreCase("News")) {
                                    layouts1.add(splashModel);
                                }

                            } else if (object == null) {
                                JSONArray jsonArray = new JSONArray();
                                /*if (jsonObject2.has("subroot")) {*/
                                JSONArray jsonArray1 = jsonObject.optJSONArray("subroot");
                                if ((jsonArray1 != null)) {
                                    jsonArray = jsonObject.getJSONArray("subroot");
                                    if (jsonArray.length() > 0) {
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject jsonObject3 = jsonArray.getJSONObject(i);
                                            SplashModel splashModel = new SplashModel();

                                            if (jsonObject3.has("Id")) {
                                                splashModel.setId(jsonObject3.getString("Id"));
                                            } else {
                                                splashModel.setId("");
                                            }

                                            if (jsonObject3.has("Type")) {
                                                splashModel.setType(jsonObject3.getString("Type"));
                                            } else {
                                                splashModel.setType("");
                                            }


                                            if (jsonObject3.has("Description")) {
                                                splashModel.setDescription(jsonObject3.getString("Description"));
                                            } else {
                                                splashModel.setDescription("Not available");
                                            }


                                            if (jsonObject3.has("FromDate")) {
                                                splashModel.setFromDate(jsonObject3.getString("FromDate"));
                                            } else {
                                                splashModel.setFromDate("Not available");
                                            }


                                            if (jsonObject3.has("Title")) {
                                                splashModel.setTitle(jsonObject3.getString("Title"));
                                            } else {
                                                splashModel.setTitle("Not available");
                                            }


                                            if (jsonObject3.has("ToDate")) {
                                                splashModel.setToDate(jsonObject3.getString("ToDate"));
                                            } else {
                                                splashModel.setToDate("Not available");
                                            }


                                            if (jsonObject3.has("CreatedDate")) {
                                                splashModel.setCreatedDate(jsonObject3.getString("CreatedDate"));
                                            } else {
                                                splashModel.setCreatedDate("Not available");
                                            }


                                            if (jsonObject3.has("Description_Marathi")) {
                                                splashModel.setDescription_Marathi(jsonObject3.getString("Description_Marathi"));
                                            } else {
                                                splashModel.setDescription_Marathi("Not available");
                                            }


                                            if (jsonObject3.has("Title_Marathi")) {
                                                splashModel.setTitle_Marathi(jsonObject3.getString("Title_Marathi"));
                                            } else {
                                                splashModel.setTitle_Marathi("Not available");
                                            }

                                            if (splashModel.getType().equalsIgnoreCase("Promotion")) {
                                                layouts.add(splashModel);
                                            } else if (splashModel.getType().equalsIgnoreCase("News")) {
                                                layouts1.add(splashModel);
                                            }

                                        }
                                    } else if (jsonArray.length() == 0) {
                                        Log.e("Arraylength", " 0");

                                    }
                                } else if (jsonArray1 == null) {

                                    Log.e("Arraylemgth ", "0");
                                }


                            }


                            adapterNews = new AdapterNews(ActivityNews.this, layouts1);
                            recycle_news.setAdapter(adapterNews);
                            adapterNews1_promo = new AdapterNews2(ActivityNews.this, layouts);
                            recycle_promotion.setAdapter(adapterNews1_promo);


                       /* for (int i=0;i<layouts.size();i++){

                        }*/



/*
                       textView_promotion.setOnClickListener(new View.OnClickListener() {
                           @Override
                           public void onClick(View v) {
                               boolean expand_promotion=false;
                               if (!expand_promotion){
                                   AdapterNews adapterNews1=new AdapterNews(ActivityNews.this,layouts);
                                   recycle_promotion.setAdapter(adapterNews1);
                                   recycle_promotion.setVisibility(View.VISIBLE);
                                   expand_promotion=true;
                               }
                               else {
                                   recycle_promotion.setVisibility(View.GONE);
                               }


                           }
                       });

                       textView_next.setOnClickListener(new View.OnClickListener() {
                           @Override
                           public void onClick(View v) {

                               if (!expand_next){
                                   AdapterNews adapterNews=new AdapterNews(ActivityNews.this,layouts1);
                                   recycle_news.setAdapter(adapterNews);
                                   recycle_news.setVisibility(View.VISIBLE);

                               }

                               else {
                                   recycle_news.setVisibility(View.GONE);
                               }
                               expand_next=true;
                           }
                       });*/


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } else if (accesscode == Connection.SPLASHSCREENEXCEPTION1.ordinal()) {
                    Toast.makeText(getApplicationContext(), ActivityNews.this.getResources().getString(R.string.Connectiontimeout), Toast.LENGTH_LONG).show();
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void gotoDescription(String i, String s, String id) {
        try {


            Intent intent = new Intent(ActivityNews.this, ActivityDescription.class);

            intent.putExtra("Description", s);
            intent.putExtra("ID", id);
            intent.putExtra("i", i);


            startActivity(intent);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void gotoshare(String Title, String Description) {
        try {

            String senddata = Title + Description;
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            Log.e("data", " " + senddata);
            /* sendIntent.putExtra(Intent.EXTRA_TEXT, senddata);*/
            sendIntent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(senddata)/*Intent.EXTRA_TEXT,("<p>hii<p>")*/);

            sendIntent.setType("text/html");

            startActivity(sendIntent);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}