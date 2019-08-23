package com.targeteducare;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.onesignal.OneSignal;
import com.targeteducare.Classes.Student;

import java.util.HashMap;
import java.util.Map;

import io.fabric.sdk.android.Fabric;

public class App extends MultiDexApplication implements Application.ActivityLifecycleCallbacks {//implements AppForegroundStateManager.OnAppForegroundStateChangeListener{
    private int activityReferences = 0;
    // long currentmillies = 0;
    private boolean isActivityChangingConfigurations = false;
   /* public App(){
        defaultUEH = Thread.getDefaultUncaughtExceptionHandler();
    }*/

    /*    private Thread.UncaughtExceptionHandler defaultUEH;

        private Thread.UncaughtExceptionHandler _unCaughtExceptionHandler =
                new Thread.UncaughtExceptionHandler() {
                    @Override
                    public void uncaughtException(Thread thread, Throwable ex) {

                        // here I do logging of exception to a db
                        Log.e("MyApp", "Uncaught exception.");
                        // Do what you want.

                        // re-throw exception to O.S. if that is serious and need to be handled by o.s. Uncomment the next line that time.
                        //defaultUEH.uncaughtException(thread, ex);
                    }
                };*/
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());

        try {
            registerActivityLifecycleCallbacks(this);
            //   AppForegroundStateManager.getInstance().addListener(this);
            OneSignal.startInit(this)
                    .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                    .unsubscribeWhenNotificationsAreDisabled(true)
                    .setNotificationOpenedHandler(new mNotificationOpenedHandler(this))
                    .setNotificationReceivedHandler(new mNotificationOpenedHandler(this))
                    .init();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {
        Log.e("Destroy ", "onActivityCreated ");
    }

    @Override
    public void onActivityStarted(Activity activity) {
        //   Log.e("Destroy ", "onActivityStarted ");
        try {
            if (++activityReferences == 1 && !isActivityChangingConfigurations) {
                GlobalValues.currentmillies = System.currentTimeMillis();
                try {
                    GlobalValues.currentmillies = System.currentTimeMillis();
                    if (GlobalValues.student != null) {
                        if (GlobalValues.student.getId().length() > 0) {
                            GlobalValues.student.setUseractive(1);
                            GlobalValues.student.setLastvisiteddate(DateUtils.getSqliteTime());


                            CheckUsers(GlobalValues.student.getMobile(), true);
                        }
                    }
             /*   GlobalValues.student.setIsactive(1);
                CheckUsers(GlobalValues.student.getMobile());*/
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.e("app in foreground", "app in foreground");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void CheckUsers(final String uId, final boolean isstart) {
        try {
            //     addtofirebasedb();
            if (InternetUtils.getInstance(this).available()) {
                DatabaseReference databaseReference;
                databaseReference = FirebaseDatabase.getInstance().getReference(Constants.firebasedbname);

                databaseReference.child(uId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        try {
                            if (dataSnapshot != null) {
                                if (dataSnapshot.getValue() != null) {
                                    if (dataSnapshot.exists()) {
                                        Log.e("exists ", "exists " + dataSnapshot.toString());
                                        Student s = dataSnapshot.getValue(Student.class);
                                        if (s.getIEMIno().length() > 0) {
                                            if (!s.getIEMIno().equalsIgnoreCase(GlobalValues.student.getIEMIno())) {
                                                return;
                                            }
                                        }
                                        if (isstart)
                                            GlobalValues.student.setTimetaken(s.getTimetaken());

                                        GlobalValues.student.setLastvisiteddate(DateUtils.getSqliteTime());
                                        addtofirebasedb(0);
                                    } else {
                                        Log.e("nt exists ", "nt exists " + dataSnapshot.toString());
                                        addtofirebasedb(1);
                                    }
                                } else {
                                    addtofirebasedb(1);
                                }
                            } else {
                                addtofirebasedb(1);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                databaseReference.child(uId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        try {
                            Log.e("Datas   ", "" + dataSnapshot.toString());
                            if (dataSnapshot.getValue() != null) {
                                Student s = dataSnapshot.getValue(Student.class);
                                // Log.e("data ", s.getMobile() + " " + s.getId() + " " + s.getTimetaken());
                                if (!dataSnapshot.exists()) {
                                    if (s.getIEMIno().length() > 0) {
                                        if (!s.getIEMIno().equalsIgnoreCase(GlobalValues.student.getIEMIno())) {
                                            return;
                                        }
                                    }

                                    GlobalValues.student.setLastvisiteddate(DateUtils.getSqliteTime());
                                    addtofirebasedb(1);
                                } else {
                                   /* GlobalValues.student.setTimetaken(s.getTimetaken());
                                    GlobalValues.student.setLastvisiteddate(DateUtils.getSqliteTime());
                                    addtofirebasedb();*/
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResumed(Activity activity) {
        Log.e("Destroy ", "onActivityResumed ");
    }

    @Override
    public void onActivityPaused(Activity activity) {
        Log.e("Destroy ", "onActivityPaused ");
    }

    @Override
    public void onActivityStopped(Activity activity) {
        Log.e("Destroy ", "onActivityStopped ");
        try {

            isActivityChangingConfigurations = activity.isChangingConfigurations();
            if (--activityReferences == 0 && !isActivityChangingConfigurations) {
                Log.e("app in background", "app in background");
                try {
                    if (GlobalValues.student != null) {
                        long totalmillies = System.currentTimeMillis() - GlobalValues.currentmillies;


                        GlobalValues.student.setUseractive(0);
                        GlobalValues.student.setLasttimetaken(totalmillies / 1000);

                        long timetaken = GlobalValues.student.getTimetaken() + GlobalValues.student.getLasttimetaken();
                        Log.e("timetaken ", " " + timetaken);

                        GlobalValues.student.setTimetaken(timetaken);
                        CheckUsers(GlobalValues.student.getMobile(), false);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addtofirebasedb(int flag) {
        try {
            if (InternetUtils.getInstance(getApplicationContext()).available()) {
                if (GlobalValues.student != null) {
                    DatabaseReference databaseReference;
                    databaseReference = FirebaseDatabase.getInstance().getReference(Constants.firebasedbname);
                    Map<String, Object> values = GlobalValues.student.toMap();
                    if (flag == 0) {
                        Map<String, Object> childUpdates = new HashMap<>();
                        Log.e("Timetaken ", "timetaken " + GlobalValues.student.getTimetaken());
                        childUpdates.put("useractive", GlobalValues.student.getUseractive());
                        childUpdates.put("timetaken", GlobalValues.student.getTimetaken());
                        childUpdates.put("lasttimetaken", GlobalValues.student.getLasttimetaken());
                        childUpdates.put("lastvisiteddate", GlobalValues.student.getLastvisiteddate());
                        databaseReference.child(GlobalValues.student.getMobile()).updateChildren(childUpdates);

                    } else {
                        databaseReference.child(GlobalValues.student.getMobile()).setValue(values);
                        Log.e("update ", "insert ");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
        Log.e("Destroy ", "onActivitySaveInstanceState ");
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        Log.e("Destroy ", "onActivityDestroyed ");
    }

   /* @Override
    public void onAppForegroundStateChange(AppForegroundStateManager.AppForegroundState newState) {
        if (AppForegroundStateManager.AppForegroundState.IN_FOREGROUND == newState) {
            Log.e("app in","foreground");
            // App just entered the foreground. Do something here!
        } else {
            Log.e("app in","background");
            // App just entered the background. Do something here!
        }
    }*/


}

