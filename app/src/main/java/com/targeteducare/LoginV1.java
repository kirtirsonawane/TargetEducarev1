package com.targeteducare;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.targeteducare.Classes.StudentProfile;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;

public class LoginV1 extends Activitycommon {
    EditText mobile_no;
    Button submit;
    TextView tv_newuser;
    String et_mobileno;
    SharedPreferences preferences;
    SharedPreferences.Editor edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_v1);
        mobile_no = findViewById(R.id.mobile_no);
        submit = findViewById(R.id.button_submit);
        tv_newuser = findViewById(R.id.tv_newuser);

        GlobalValues.studentProfile = new StudentProfile();

        registerreceiver();
        preferences = PreferenceManager.getDefaultSharedPreferences(LoginV1.this);
        edit = preferences.edit();

        if (preferences.getBoolean("isloginv1", false)) {
            Gson gson=new Gson();
            Type type=new TypeToken<StudentProfile>(){}.getType();
            GlobalValues.studentProfile=gson.fromJson(preferences.getString("studentprofiledetails",""),type);
            Intent intent = new Intent(LoginV1.this, GridMainActivity.class);
            startActivity(intent);
            this.finish();
        }

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_mobileno = mobile_no.getText().toString();
                if (!(et_mobileno.trim().equals(""))) {

                    JSONObject obj = new JSONObject();
                    JSONObject mainobj = new JSONObject();

                    try {
                        obj.put("Mobile", et_mobileno.trim());
                        mainobj.put("FilterParameter", obj.toString());
                        Log.e("dataobj ",mainobj.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    ConnectionManager.getInstance(LoginV1.this).loginv1(mainobj.toString());
                } else {
                    Toast.makeText(LoginV1.this, "Please enter the mobile number!!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        tv_newuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent isignup = new Intent(LoginV1.this, SignUp.class);
                startActivity(isignup);
            }
        });
    }

    @Override
    protected void onResponsed(int statuscode, int accesscode, String data) {
        super.onResponsed(statuscode, accesscode, data);
        try {
            if (statuscode == Constants.STATUS_OK) {
                if (accesscode == Connection.LOGINV1.ordinal()) {

                    Log.e("res ", "reslogin " + GlobalValues.TEMP_STR);

                    JSONObject jsonObject = new JSONObject(GlobalValues.TEMP_STR);

                    JSONObject root = jsonObject.getJSONObject("root");
                    JSONObject subroot = root.getJSONObject("subroot");
                    //Log.e("subroot: ", String.valueOf(subroot));

                    /*for (int i = 0; i < subroot.length(); i++) {
                    }*/
                    //String error = subroot.getString("error");
                    /*Log.e("id ",id);
                    Log.e("name ",name);
                    Log.e("mobile ",mobile);
                    Log.e("referralmobile ",referralmobile);
                    Log.e("rolename ",rolename);*/
                    //Log.e("error ",error);

                    if(subroot!=null){

                       // if(!(et_mobileno.trim().length()<10)){
                            String id = subroot.optString("Id");
                            String name = subroot.optString("Name");
                            String mobile = subroot.optString("Mobile");
                            String referralmobile = subroot.optString("ReferalMobile");
                            String rolename = subroot.optString("RoleName");

                            Gson gson = new Gson();

                            GlobalValues.studentProfile.setId(id);
                            GlobalValues.studentProfile.setName(name);
                            GlobalValues.studentProfile.setMobile(mobile);
                            GlobalValues.studentProfile.setReferralmobile(referralmobile);
                            GlobalValues.studentProfile.setRollname(rolename);
                            String nametest = GlobalValues.studentProfile.getName();

                            String jsonstudentprofile = gson.toJson(GlobalValues.studentProfile);
                            Log.e("Id is: ",GlobalValues.studentProfile.getId());

                            edit.putString("studentprofiledetails",jsonstudentprofile);

                            edit.putBoolean("isloginv1",true);
                            edit.apply();
                            Intent intent = new Intent(LoginV1.this, GridMainActivity.class);
                            startActivity(intent);
                            finish();

                            /*else{
                                Toast.makeText(LoginV1.this, "Enter a valid mobile number!!", Toast.LENGTH_SHORT).show();
                                edit.putBoolean("isloginv1",false);
                                edit.apply();
                            }*/
                       /* }
                        else{
                            Toast.makeText(LoginV1.this, "Enter a 10 digit number!!", Toast.LENGTH_SHORT).show();
                        }*/
                    }
                    else {
                        Log.e("in else ","");
                    }


                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
