package com.targeteducare;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

public class IPSettingActivity extends Activitycommon {
    EditText e1;
    Button bt;
    SharedPreferences pref;
    SharedPreferences.Editor edit;
    String tag = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            screenshot_capture_permission();
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_ipsetting);
            setmaterialDesign();
            setTitle("");
            attachUI();
        } catch (Exception e) {
            reporterror(tag, e.toString());
            e.printStackTrace();
        }
    }

    private void attachUI() {
        try {
            tag = this.getClass().getSimpleName();

            pref = PreferenceManager.getDefaultSharedPreferences(IPSettingActivity.this);
            edit = pref.edit();
            e1 = (EditText) findViewById(R.id.edittext_1);
            bt = (Button) findViewById(R.id.button_1);
            e1.setText(pref.getString("IP", ""));
            bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (e1.getText().length() > 0) {
                        GlobalValues.IP = e1.getText().toString();
                        Log.e("Global ip", "Global IP " + GlobalValues.IP);
                        edit.putString("IP", GlobalValues.IP);
                        edit.commit();
                        try {
                            Intent intent = new Intent(Constants.BROADCAST_WIZARD);
                            intent.putExtra(Constants.BROADCAST_RESPONSE_CODE, 200);
                            intent.putExtra(Constants.BROADCAST_URL_ACCESS, Connection.IPChange.ordinal());
                            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        IPSettingActivity.this.finish();
                    }
                }
            });
        } catch (Exception e) {
            reporterror(tag, e.toString());
            e.printStackTrace();
        }
    }

}
