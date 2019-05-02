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

import com.targeteducare.Classes.Student;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Random;

public class LoginV1Activity extends Activitycommon {
    EditText mobile_no, enter_otp;
    Button submit;
    TextView tv_newuser;
    String et_mobileno;
    SharedPreferences preferences;
    SharedPreferences.Editor edit;

    String id = "";
    String name = "";
    String mobile = "";
    String referralmobile = "";
    String rolename = "";

    boolean flag = false;

    String otp = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_v1);
        mobile_no = findViewById(R.id.mobile_no);
        submit = findViewById(R.id.button_submit);
        tv_newuser = findViewById(R.id.tv_newuser);
        enter_otp = findViewById(R.id.enter_otp);

        GlobalValues.student = new Student();

        registerreceiver();
        preferences = PreferenceManager.getDefaultSharedPreferences(LoginV1Activity.this);
        edit = preferences.edit();

        if (preferences.getBoolean("isloginv1", false)) {
            Gson gson = new Gson();
            Type type = new TypeToken<Student>() {
            }.getType();
            GlobalValues.student = gson.fromJson(preferences.getString("studentdetails", ""), type);
            Intent intent = new Intent(LoginV1Activity.this, GridMainActivity.class);
            startActivity(intent);
            this.finish();
        }

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(submit.getText().equals("Submit")){
                    et_mobileno = mobile_no.getText().toString();
                    if (!(et_mobileno.trim().equals(""))) {

                        if (!(et_mobileno.trim().length() < 10)) {

                            JSONObject obj = new JSONObject();
                            JSONObject mainobj = new JSONObject();

                            try {
                                obj.put("Mobile", et_mobileno.trim());
                                mainobj.put("FilterParameter", obj.toString());
                                Log.e("dataobj ", mainobj.toString());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            ConnectionManager.getInstance(LoginV1Activity.this).loginv1(mainobj.toString());

                        } else {
                            enter_otp.setVisibility(View.GONE);
                            Toast.makeText(LoginV1Activity.this, "Enter a 10 digit number!!", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        enter_otp.setVisibility(View.GONE);
                        Toast.makeText(LoginV1Activity.this, "Please enter the mobile number!!", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Log.e("Value of flag is ",String.valueOf(flag));
                    Log.e("Entered otp is ",enter_otp.getText().toString());
                    Log.e("Sent otp is ",otp);
                    if(flag == true){
                        Log.e("I am into ","flag == true");
                        if (!(enter_otp.getText().toString().trim().equals(""))) {

                            if (enter_otp.getText().toString().equals(otp)) {

                                Gson gson = new Gson();

                                GlobalValues.student.setId(id);
                                GlobalValues.student.setName(name);
                                Log.e("From login ","mobile no. is "+mobile);
                                GlobalValues.student.setMobile(et_mobileno);
                                GlobalValues.student.setReferalMobile(referralmobile);
                                GlobalValues.student.setRollNumber(rolename);
                                String nametest = GlobalValues.student.getName();

                                String jsonstudent = gson.toJson(GlobalValues.student);
                                Log.e("Id is: ", GlobalValues.student.getId());

                                edit.putString("studentdetails", jsonstudent);

                                edit.putBoolean("isloginv1", true);
                                edit.apply();

                                Intent intent = new Intent(LoginV1Activity.this, GridMainActivity.class);
                                startActivity(intent);
                                finish();

                            } else {
                                Toast.makeText(context, "Invalid otp!!", Toast.LENGTH_SHORT).show();
                                enter_otp.setText("");
                            }

                        } else {
                            Toast.makeText(context, "Please enter the otp", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });

        tv_newuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enter_otp.setVisibility(View.GONE);
                Intent isignup = new Intent(LoginV1Activity.this, SignUpActivity.class);
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
                    if (subroot != null) {
                        //Log.e("subroot ", subroot.toString());
                        String error = subroot.optString("error");
                        //Log.e("error1 ",error);

                        if (error.equals("")) {
                            submit.setText("Verify");
                            enter_otp.setVisibility(View.VISIBLE);
                            final int random = new Random().nextInt(899999) + 100000;
                            otp = Integer.toString(random);
                            ConnectionManager.getInstance(LoginV1Activity.this).getotp(et_mobileno, otp);
                            Toast.makeText(context, "We have sent an OTP to the registered mobile number " + et_mobileno + ". Please Verify the same", Toast.LENGTH_SHORT).show();
                            Log.e("OTP is ",otp);
                            id = subroot.optString("Id");
                            name = subroot.optString("Name");
                            mobile = subroot.optString("Mobile");
                            referralmobile = subroot.optString("ReferalMobile");
                            rolename = subroot.optString("RoleName");

                        } else {

                            enter_otp.setVisibility(View.GONE);
                            Toast.makeText(LoginV1Activity.this, "Enter a valid mobile number!!", Toast.LENGTH_SHORT).show();
                        }

                    }
                } else if (accesscode == Connection.OTP.ordinal()) {

                    flag = true;

                }
                else if (accesscode == Connection.OTPEXCEPTION.ordinal()) {

                    flag = true;

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}