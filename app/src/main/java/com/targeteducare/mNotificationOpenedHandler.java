package com.targeteducare;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import com.onesignal.OSNotification;
import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OneSignal;
import com.targeteducare.database.DatabaseHelper;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Random;

import static android.content.Context.NOTIFICATION_SERVICE;

public class mNotificationOpenedHandler implements OneSignal.NotificationOpenedHandler, OneSignal.NotificationReceivedHandler, OneSignal.PostNotificationResponseHandler {
    Context context;

    mNotificationOpenedHandler(Context context) {
        this.context = context;
    }

    @Override
    public void notificationOpened(OSNotificationOpenResult result) {
        try {
            JSONObject data = result.notification.payload.additionalData;

            NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
            manager.cancelAll();
            Log.e("notification opened ", "notification opened " + data + " ");
            boolean isopen = false;
            try {
                isopen = GlobalValues.isopen;
            } catch (Exception e) {
                e.printStackTrace();
            }
           // Log.e("isopen", "isopen " + isopen);
            if (isopen) {
                Log.e("started  ", "started " + isopen);
                Intent intent1 = new Intent(context, NotificationActivity.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent1);
            } else {
                Log.e("started1  ", "started " + isopen);
                Intent intent1 = new Intent(context, ChooseLanguageActivity.class);
                intent1.putExtra("isnotified", true);
                intent1.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent1);
            }

           /* Intent resultIntent = new Intent(context, NotificationActionService.class);
            resultIntent.putExtra("isnotified", true);
            context.startService(resultIntent);*/
        } catch (Exception e) {
            e.printStackTrace();
        }
   /*     resultIntent.putExtra("flag",0);

        resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);*/


        //Intent resultIntent = new Intent(context, NotificationActionService.class);
        try {
          /*  resultIntent.putExtra("isnotified", true);
            resultIntent.putExtra("type", data.getString("type"));
            resultIntent.putExtra("flag", 0);
            Log.e("insurance alertss ", "notificationOpened received " + data.toString());
            try {
                String tablename="table_iaa";
                if (data.getString("type").equalsIgnoreCase(Constants.Tab1)) {
                    tablename="table_news";
                }else if (data.getString("type").equalsIgnoreCase(Constants.Tab2)) {
                    tablename="table_blogs";
                }
                JSONObject obj1= DatabaseHelper.getInstance(context).getnewsbyblogid(tablename,data.getInt("id"));
                JSONObject obj2=new JSONObject();
                JSONObject subobj=new JSONObject();
                subobj.put("subroot",obj1);
                obj2.put("root",subobj);

                Log.e("obj ","oobj "+obj2.toString());
                resultIntent.putExtra("data", obj2.toString());*/
        } catch (Exception e) {
            e.printStackTrace();
        }
        //context.startService(resultIntent);
    }

    /*  }catch (Exception e)
      {
          reporterror(e.toString());
          e.printStackTrace();
      }

  }*/
    public void reporterror(String error) {
        try {
            JSONObject obj = new JSONObject();
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            obj.put("email", preferences.getString("email", "news"));
            obj.put("osversion", Build.MODEL + " " + Build.BRAND + " " + Build.VERSION.SDK_INT);
            obj.put("errordetail", error.replaceAll("'", ""));
            obj.put("appname", Constants.APP_NAME);
            obj.put("activityname", "FragmenTabbedFragment");
            ConnectionManager.getInstance(context).reporterror(obj.toString());

        } catch (Exception e) {
            Log.e("error", "error " + e);
            e.printStackTrace();
        }
    }

    @Override
    public void notificationReceived(OSNotification notification) {
        try {
            Log.e("insurance alertss  ", "received ");
            Log.e("insurance alertss  ", "received " + notification.payload.body.toString() + " " + notification.toString());
            JSONObject jsonobj = new JSONObject();
            jsonobj.put("time", DateUtils.getSqliteTime());
            jsonobj.put("header", notification.payload.title);
            jsonobj.put("desc", notification.payload.body);
            jsonobj.put("imageurl", "");

            DatabaseHelper.getInstance(context).savenotificationData(DatabaseHelper.TABLE_NOTIFICATION,jsonobj.toString());
            DatabaseHelper.getInstance(context).getnotificationdata(DatabaseHelper.TABLE_NOTIFICATION);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSuccess(JSONObject response) {
        Log.e("insurance alertss  ", "onSuccess " + response.toString());
    }

    @Override
    public void onFailure(JSONObject response) {
        Log.e("insurance alertss  ", "onFailure " + response.toString());
    }

/*
    public void notidata(final String json1, final OSNotification notification) {
        if (InternetUtils.getInstance(context).available()) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject obj = new JSONObject(json1);

                        String type = obj.getString("type");
                        // JSONObject obj1 = new JSONObject(obj.getString("data")).getJSONObject("root").getJSONObject("subroot");
                        // Random r = new Random();
                        //   int notificationid = r.nextInt();
                        String lang = obj.getString("Language");
                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
                        String preferedlang = preferences.getString("preflang", "english");

                        if (lang.equalsIgnoreCase(preferedlang)) {

                            int cacheSize = 10 * 1024 * 1024; // 10 MiB
                            Cache cache = new Cache(context.getCacheDir(), cacheSize);
                            OkHttpClient client = new OkHttpClient();
                            client.setCache(cache);
                            //  Log.e("notified url",""+URLS.GETBLOGS(type, 0, 10, obj1.getInt("CatId"), obj1.getInt("id")));
                            Request request = new Request.Builder()
                                    .url(URLS.GETBLOGS(type, 0, 10, obj.getInt("CatId"), obj.getInt("id")))
                                    .build();
                            Response response = client.newCall(request).execute();
                            String s = response.body().string();

                            s = s.trim();
                            s = s.substring(1, s.length() - 1);
                            s = s.replace("\\n", "");
                            s = s.replace("\\t", "");
                            s = s.replace("\\", "");
                            JSONObject obj2 = new JSONObject(s);

                            Log.e("insurance alertss ","service called  data is "+obj2.toString());
                            notification.payload.additionalData.put("outputadata", obj2.toString());
                            JSONObject obj3 = obj2.getJSONObject("root").getJSONObject("subroot");

                            JSONObject jsonobj = new JSONObject();
                            String body = "";
                            body = Html.fromHtml(obj.getString("content")).toString();
                            String title = obj.getString("title");
                            if (obj.getString("type").equalsIgnoreCase(Constants.Tab2)) {
                                Blog blog = new Blog(obj3);
                                Intent intentblog = new Intent("addadevertise");
                                intentblog.putExtra(Constants.BROADCAST_RESPONSE_CODE, Constants.STATUS_OK);
                                intentblog.putExtra(Constants.BROADCAST_URL_ACCESS, Connection.BLOG_NOTIFIED.ordinal());
                                intentblog.putExtra(Constants.BROADCAST_DATA, obj3.toString());
                                LocalBroadcastManager.getInstance(context).sendBroadcast(intentblog);
                                long id = DatabaseHelper.getInstance(context).savenewsdata(obj3.toString(), "table_blogs", blog.getCatId(), Integer.parseInt(blog.getBlog_id()));
                                // resultIntent.putExtra("data", obj2.toString());
                            } else if (obj.getString("type").equalsIgnoreCase(Constants.Tab1)) {
                                DBClassNews news = new DBClassNews(obj3);
                                // resultIntent.putExtra("data", obj2.toString());
                                Intent intentnews = new Intent("addadevertise");
                                intentnews.putExtra(Constants.BROADCAST_RESPONSE_CODE, 200);
                                intentnews.putExtra(Constants.BROADCAST_URL_ACCESS, Connection.NEWS_NOTIFIED.ordinal());
                                intentnews.putExtra(Constants.BROADCAST_DATA, obj3.toString());
                                LocalBroadcastManager.getInstance(context).sendBroadcast(intentnews);
                                long id = DatabaseHelper.getInstance(context).savenewsdata(obj3.toString(), "table_news", news.getCatId(), Integer.parseInt(news.getNewsid()));
                                // confirmIntent.putExtra("id", id);
                            } else if (obj.getString("type").equalsIgnoreCase(Constants.Tab3)) {
                                DBClassNews news = new DBClassNews(obj3);
                                //   resultIntent.putExtra("data", obj2.toString());
                                Intent intentnews = new Intent("addadevertise");
                                intentnews.putExtra(Constants.BROADCAST_RESPONSE_CODE, 200);
                                intentnews.putExtra(Constants.BROADCAST_URL_ACCESS, Connection.IAA_NOTIFIED.ordinal());
                                intentnews.putExtra(Constants.BROADCAST_DATA, obj3.toString());
                                LocalBroadcastManager.getInstance(context).sendBroadcast(intentnews);
                                long id = DatabaseHelper.getInstance(context).savenewsdata(obj3.toString(), "table_iaa", news.getCatId(), Integer.parseInt(news.getNewsid()));
                                //confirmIntent.putExtra("id", id);
                            } else {
                                long id = DatabaseHelper.getInstance(context).savenotificationData("table_notification", obj3.toString());
                                //  confirmIntent.putExtra("id", id);
                            }
                            jsonobj.put("desc", body);
                        } else {
                            // Toast.makeText(getApplicationContext(),"lang differ ",Toast.LENGTH_LONG).show();
                        }
                        // }
                    } catch (Exception e) {
                        reporterror(e.toString());
                        Log.e("error ", "error " + e.toString());
                       *//* publishBroadcast(Constants.STATUS_OK,
                                Connection.EXCEPTION_ERROR.ordinal());*//*
                        return;
                    }
                }
            });
            thread.start();
        } else {
           *//* publishBroadcast(Constants.STATUS_OK,
                    Connection.NO_INTERNET.ordinal());*//*
        }
    }*/
}
