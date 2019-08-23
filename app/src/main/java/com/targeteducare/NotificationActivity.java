package com.targeteducare;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.targeteducare.Adapter.AdapterNotification;
import com.targeteducare.database.DatabaseHelper;

import org.json.JSONObject;

import java.util.ArrayList;

public class NotificationActivity extends Activitycommon {
    RecyclerView recyclerview;
    RecyclerView.LayoutManager mLayoutManager;
    ArrayList<String> data;
    AdapterNotification adapter;
    String tag = "NotificationActivity";

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        screenshot_capture_permission();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        setmaterialDesign();
        back();
        setTitle("Alerts");
        try {
            registerReceiver(response, new IntentFilter("fragmentupdater"));
            recyclerview = (RecyclerView) findViewById(R.id.recycler_view);
            mLayoutManager = new LinearLayoutManager(this);
            recyclerview.setLayoutManager(mLayoutManager);

            databaseReference = FirebaseDatabase.getInstance().getReference(Constants.firebasedbname);

            DatabaseHelper.getInstance(this).updatenotification("table_notification");
            try {
                String manufacturer = "xiaomi";
                if (manufacturer.equalsIgnoreCase(android.os.Build.MANUFACTURER)) {

                } else {
                   // ShortcutBadger.applyCount(this, 0);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            // recyclerview.addItemDecoration(new DividerItemDecoration(ContextCompat.getDrawable(this, R.drawable.recycler_divider)));


            data = new ArrayList<String>();
            data = DatabaseHelper.getInstance(this).getnotificationdata("table_notification");
            adapter = new AdapterNotification(this, data);
            recyclerview.setAdapter(adapter);



        } catch (Exception e) {
            e.printStackTrace();
            reporterror(tag, e.toString());
        }
    }

    @Override
    protected void onDestroy() {
        try{
            unregisterReceiver(response);
        }catch (Exception e){

        }
        super.onDestroy();
    }

    private BroadcastReceiver response = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            Bundle b = intent.getExtras();
            int accesscode = b.getInt(Constants.BROADCAST_URL_ACCESS);
            /*if (accesscode == Connection.notificationfragment.ordinal()) {
                try {
                    DatabaseHelper.getInstance(NotificationActivity.this).updatenotification("table_notification");
                    String notifieddata = b.getString("data");
                    MainActivity.t1.setVisibility(View.GONE);
                    data.add(0, notifieddata);
                    adapter.notifyDataSetChanged();
                    try {
                        String manufacturer = "xiaomi";
                        if (manufacturer.equalsIgnoreCase(android.os.Build.MANUFACTURER)) {
                        } else {
                            //  ShortcutBadger.applyCount(getActivity(), 0);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } catch (Exception e) {
                    reporterror(tag,e.toString());
                }
            } else if (accesscode == Connection.syncdata.ordinal()) {
                for (int i = 0; i < data.size(); i++) {
                    try {
                        JSONObject obj = new JSONObject(data.get(i));
                        if (obj.has("issync")) {
                            if (obj.getInt("issync")==1) {
                                Log.e("updated", "updated");
                                obj.put("issync", 0);
                                dismissLoading();
                                data.set(i, obj.toString());
                            }
                            DatabaseHelper.getInstance(NotificationActivity.this).updatesyncdata("table_notification");
                        }

                    } catch (Exception e) {
                        Log.e("error", "error " + e);
                        reporterror(tag,e.toString());
                    }
                }
                adapter.notifyDataSetChanged();
                Log.e("received ", "received");
            }*/
        }
    };

    /*private void check_chat_available(final String uId) {
        try {
            if (databaseReference != null) {

                databaseReference.child(uId).child("chat").addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        try {
                            if (dataSnapshot != null) {
                                if (dataSnapshot.getValue() != null) {
                                    if (dataSnapshot.exists()) {

                                        adapter.notifyDataSetChanged();


                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

}
