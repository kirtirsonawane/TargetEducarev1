package com.targeteducare;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;

import org.json.JSONObject;

public class RewardPointsActivity extends Activitycommon {

    TextView tv_lvl1, tv_lvl2, tv_lvl3, tv_rp1, tv_rp2, tv_rp3, tv_total_points;
    int rp1 = 0, rp2 = 0, rp3 = 0, totalpoints = 0;
    LottieAnimationView animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            screenshot_capture_permission();
            super.onCreate(savedInstanceState);
            loadLocale();
            setContentView(R.layout.activity_reward_points);
            setmaterialDesign();
            back();
            setTitle(getResources().getString(R.string.reward_points));
            toolbar.setTitleMargin(30, 10, 10, 10);
            tag = this.getClass().getSimpleName();

            tv_lvl1 = findViewById(R.id.tv_lvl1);
            tv_lvl2 = findViewById(R.id.tv_lvl2);
            tv_lvl3 = findViewById(R.id.tv_lvl3);
            tv_rp1 = findViewById(R.id.tv_rp1);
            tv_rp2 = findViewById(R.id.tv_rp2);
            tv_rp3 = findViewById(R.id.tv_rp3);
            tv_total_points = findViewById(R.id.tv_totalpoints);
            animation = findViewById(R.id.animation);

            try {
                animation.setAnimation("reward_box.json");
                animation.loop(true);
                animation.playAnimation();
            } catch (Exception e) {
                reporterror(tag, e.toString());
                e.printStackTrace();
            }


            try {

                JSONObject obj = new JSONObject();
                JSONObject mainobj = new JSONObject();
                obj.put("ReferralNo", GlobalValues.student.getMobile());
                mainobj.put("FilterParameter", obj.toString());

                ConnectionManager.getInstance(RewardPointsActivity.this).getrewardpoints(mainobj.toString());

            } catch (Exception e) {
                reporterror(tag, e.toString());
                e.printStackTrace();
            }

        } catch (Exception e) {
            reporterror(tag, e.toString());
            e.printStackTrace();
        }

    }

    @Override
    protected void onResponsed(int statuscode, int accesscode, String data) {
        if (statuscode == Constants.STATUS_OK) {

            if (accesscode == Connection.GETREWARDPOINTS.ordinal()) {

                try {

                    JSONObject object = new JSONObject(GlobalValues.TEMP_STR);
                    JSONObject root = object.getJSONObject("root");

                    String s = root.optString("subroot");

                    if (s.equalsIgnoreCase("null")) {

                        tv_total_points.setText(String.valueOf(getResources().getString(R.string.total_reward_points) + ": " + totalpoints));

                    } else if (s.equalsIgnoreCase("")) {
                        tv_total_points.setText(String.valueOf(getResources().getString(R.string.total_reward_points) + ": " + totalpoints));
                    } else {
                        JSONObject subroot = root.optJSONObject("subroot");

                        if (subroot != null) {

                            if (subroot.has("lvl1")) {
                                String lvl1 = subroot.optString("lvl1");
                                //tv_lvl1.setText(lvl1);

                                //totalpoints = Integer.parseInt(lvl1);
                                Log.e("total points ", lvl1);
                                tv_total_points.setText(String.valueOf(getResources().getString(R.string.total_reward_points) + ": " + lvl1));
                            }
                            if (subroot.has("lvl2")) {
                                String lvl2 = subroot.getString("lvl2");
                                tv_lvl2.setText(lvl2);

                                int l2 = Integer.parseInt(lvl2);
                                rp2 = l2 * 7;
                                tv_rp2.setText(String.valueOf(rp2));
                            }
                            if (subroot.has("lvl3")) {
                                String lvl3 = subroot.getString("lvl3");
                                tv_lvl3.setText(lvl3);

                                int l3 = Integer.parseInt(lvl3);
                                rp3 = l3 * 3;
                                tv_rp3.setText(String.valueOf(rp3));
                            }

                            /*if (subroot.has("lvl1")) {
                                String lvl1 = subroot.getString("lvl1");
                                tv_lvl1.setText(lvl1);

                                int l1 = Integer.parseInt(lvl1);
                                rp1 = l1 * 10;
                                tv_rp1.setText(String.valueOf(rp1));
                            }
                            if (subroot.has("lvl2")) {
                                String lvl2 = subroot.getString("lvl2");
                                tv_lvl2.setText(lvl2);

                                int l2 = Integer.parseInt(lvl2);
                                rp2 = l2 * 7;
                                tv_rp2.setText(String.valueOf(rp2));
                            }
                            if (subroot.has("lvl3")) {
                                String lvl3 = subroot.getString("lvl3");
                                tv_lvl3.setText(lvl3);

                                int l3 = Integer.parseInt(lvl3);
                                rp3 = l3 * 3;
                                tv_rp3.setText(String.valueOf(rp3));
                            }*/

                            //totalpoints = rp1 + rp2 + rp3;

                        }
                    }

                } catch (Exception e) {
                    reporterror(tag, e.toString());
                    e.printStackTrace();
                }

            } else if (accesscode == Connection.GETREWARDPOINTSEXCEPTION.ordinal()) {
                Toast.makeText(getApplicationContext(), RewardPointsActivity.this.getResources().getString(R.string.Connectiontimeout), Toast.LENGTH_LONG).show();
            }
        }
    }
}
