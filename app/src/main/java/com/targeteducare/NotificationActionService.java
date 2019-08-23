package com.targeteducare;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

public class NotificationActionService extends IntentService {
    // NotificationClickReceiver rec;

    public NotificationActionService() {
        super("noticlick1");
    }

    public NotificationActionService(String name) {
        super(name);
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        try {
            //Log.e("on handle intent ","onHandle intent ");
            Bundle b = intent.getExtras();
            if (b != null) {
                if (b.containsKey("isnotified")) {
                    boolean isnotified = intent.getBooleanExtra("isnotified", false);
                    Log.e("isnotified","isnotified "+isnotified);
                    if (isnotified) {
                        if (b.containsKey("notiid")) {
                            int notiid = b.getInt("notiid");
                            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                            manager.cancel(notiid);
                            manager.cancelAll();
                        }

                        boolean isopen = false;
                        try {
                            isopen = GlobalValues.isopen;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Log.e("isopen", "isopen " + isopen);
                        if (isopen) {
                            Intent intent1 = new Intent(this, NotificationActivity.class);
                            startActivity(intent1);
                        } else {
                            Intent intent1 = new Intent(this, ChooseLanguageActivity.class);
                            intent1.putExtra("isnotified",isnotified);
                            intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent1);
                        }
                        //  Log.e("service ","service onHandleIntent ");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
