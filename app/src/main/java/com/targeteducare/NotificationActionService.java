package com.targeteducare;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.util.Random;

public class NotificationActionService extends IntentService {

    /*private int currentNotificationID = 0;
    private NotificationManager notificationManager;
    private NotificationCompat.Builder notificationBuilder;
    private String notificationTitle;
    private String notificationText;
    private Bitmap icon;
    private int combinedNotificationCounter;*/
    Bundle b ;
    String title = "";
    String body = "";


    public NotificationActionService() {
        super("noticlick1");
    }

    public NotificationActionService(String name) {
        super(name);
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        Log.e("on handle intent ","onHandle intent ");
        return super.onStartCommand(intent, flags, startId);

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        try{
            if(intent!=null){
                b = intent.getExtras();
                if(b!=null){
                    title = b.getString("notificationtitle");
                    body = b.getString("notificationbody");
                    sendNotification(title, body);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*private void setDataForSimpleNotification() {
        notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(notificationTitle)
                .setContentText(notificationText);
        sendNotification();
    }*/

    private void sendNotification(String title, String body) {
        /*Intent notificationIntent = new Intent(this, NotificationActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        notificationBuilder.setContentIntent(contentIntent);
        Notification notification = notificationBuilder.build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.defaults |= Notification.DEFAULT_SOUND;
        currentNotificationID++;
        int notificationId = currentNotificationID;
        if (notificationId == Integer.MAX_VALUE - 1)
            notificationId = 0;
        notificationManager.notify(notificationId, notification);*/

        String id = "id";
        Context context = getApplicationContext();
        int requestCode = (int) System.currentTimeMillis();
        Intent resultIntent = new Intent(this, NotificationActivity.class);

        PendingIntent pendingIntent = PendingIntent.getService(context, requestCode, resultIntent, 0);
        Uri defaultRintoneUri = RingtoneManager.getActualDefaultRingtoneUri(this, RingtoneManager.TYPE_NOTIFICATION);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(NotificationActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(1, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationManager mNotificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = mNotificationManager.getNotificationChannel(id);
            if (mChannel == null) {
                mChannel = new NotificationChannel(id, title, importance);
                mChannel.enableVibration(true);
                mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                mNotificationManager.createNotificationChannel(mChannel);
            }
            mBuilder = new NotificationCompat.Builder(context, id);
            mBuilder.setSmallIcon(R.drawable.roundedtxtanswer)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                    .setColor(ContextCompat.getColor(context, R.color.green))
                    .setContentIntent(pendingIntent)
                    .setContentTitle(title)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(body))
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    .setAutoCancel(true).setSound(defaultRintoneUri);
        } else {
            mBuilder = new NotificationCompat.Builder(
                    context).setSmallIcon(R.drawable.roundedtxtanswer)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                    .setContentIntent(pendingIntent)
                    .setColor(ContextCompat.getColor(context, R.color.green))
                    .setContentTitle(title)
                    .setContentText(body)
                    .setAutoCancel(true)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(body))
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    .setAutoCancel(true).setSound(defaultRintoneUri);
        }
        Random r = new Random();
        Notification notification = mBuilder.build();
        mNotificationManager.notify(r.nextInt(), notification);

    }
}
