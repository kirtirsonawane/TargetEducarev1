package com.targeteducare;


import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;
import com.onesignal.OSNotification;
import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OneSignal;
import com.targeteducare.database.DatabaseHelper;
import org.json.JSONObject;


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


        } catch (Exception e) {
            e.printStackTrace();
        }

    }


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
            Log.d("error notification", "error " + e);
            e.printStackTrace();
        }
    }

    @Override
    public void notificationReceived(OSNotification notification) {
        try {
            //Log.e("insurance alertss  ", "received ");
            //Log.e("insurance alertss  ", "received " + notification.payload.body.toString() + " " + notification.toString());
            JSONObject jsonobj = new JSONObject();
            jsonobj.put("time", DateUtils.getSqliteTime());
            jsonobj.put("header", notification.payload.title);
            jsonobj.put("desc", notification.payload.body);
            jsonobj.put("imageurl", notification.payload.bigPicture);

            DatabaseHelper.getInstance(context).savenotificationData(DatabaseHelper.TABLE_NOTIFICATION,jsonobj.toString());
            DatabaseHelper.getInstance(context).getnotificationdata(DatabaseHelper.TABLE_NOTIFICATION);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSuccess(JSONObject response) {
        //Log.e("insurance alertss  ", "onSuccess " + response.toString());
    }

    @Override
    public void onFailure(JSONObject response) {

    }

}
