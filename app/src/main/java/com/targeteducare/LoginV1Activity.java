package com.targeteducare;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.targeteducare.Classes.Student;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.targeteducare.database.DatabaseHelper;
import org.json.JSONObject;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class LoginV1Activity extends Activitycommon {
    EditText mobile_no, enter_otp, ip_address;
    Button submit;
    TextView tv_newuser;
    String et_mobileno = "";
    SharedPreferences preferences;
    SharedPreferences.Editor edit;
    RelativeLayout layout_login;
    String Id = "";
    String Name = "";
    String RollNumber = "";
    String CenterId = "";
    String FatherName = "";
    String MotherName = "";
    String DOB = "";
    String Mobile = "";
    String Email = "";
    String QualificationId = "";
    String CountryId = "";
    String StateId = "";
    String CityId = "";
    String Password = "";
    String Address = "";
    String RegistrationDate = "";
    String CategoryId = "";
    String SubCategoryId = "";
    String CasteCategory = "";
    String Adhar = "";
    String IsActive = "";
    String Gender = "";
    String Nationality = "";
    String AltMobile = "";
    String AltEmail = "";
    String Totalamt = "";
    String dueAmt = "";
    String sanstha_id = "";
    String ReferalMobile = "";
    String RoleName = "";
    String UserTheme = "";
    boolean flag = false;
    String otp = "0";
    String IsFaculty = "";
    int isomr = 0;
    TextView problem_with_otp;
    TextView tv_resendotp;
    private FirebaseAuth mAuth;
    String verificationId = "";
    String tag = "";
    boolean isnotified = false;
    boolean showdevicelogin = false;
    TextView txtmsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            screenshot_capture_permission();
            super.onCreate(savedInstanceState);
            loadLocale();
            setContentView(R.layout.activity_login_v1);
            mAuth = FirebaseAuth.getInstance();
            tag = this.getClass().getSimpleName();
            mobile_no = findViewById(R.id.mobile_no);
            mobile_no.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    try {
                        if (showdevicelogin)
                            txtmsg.setVisibility(View.GONE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            submit = findViewById(R.id.button_submit);
            tv_newuser = findViewById(R.id.tv_newuser);
            enter_otp = findViewById(R.id.enter_otp);
            ip_address = findViewById(R.id.ip_address);
            ip_address.setText(GlobalValues.IP);
            layout_login = findViewById(R.id.layout_login);
            problem_with_otp = findViewById(R.id.tv_problemwith_otp);
            tv_resendotp = findViewById(R.id.tv_resendotplogin);
            tv_resendotp.setVisibility(View.GONE);
            txtmsg = (TextView) findViewById(R.id.tv_msg);
            Bundle b = getIntent().getExtras();
            if (b != null) {
                if (b.containsKey("isnotified"))
                    isnotified = b.getBoolean("isnotified");
                if (b.containsKey("showdevicelogin")) {
                    showdevicelogin = b.getBoolean("showdevicelogin");
                    if (showdevicelogin)
                        txtmsg.setVisibility(View.VISIBLE);
                    else {
                        txtmsg.setVisibility(View.GONE);
                    }
                } else txtmsg.setVisibility(View.GONE);
            } else txtmsg.setVisibility(View.GONE);


            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE}, 101);
            }
/*
            Bundle b = getIntent().getExtras();
            if (b != null) {
                if (b.containsKey("isnotified"))
                    isnotified = b.getBoolean("isnotified");
            }*/

            tv_resendotp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {

                        enter_otp.setText("");
                        enter_otp.requestFocus();
                        final int random1 = new Random().nextInt(899999) + 100000;
                        otp = Integer.toString(random1);

                        if (!(mobile_no.getText().toString().trim().length() < 10)) {
                            if (!mobile_no.getText().toString().equals("")) {
                                Log.e("Resent otp is ", otp);
                                sendVerificationCode(mobile_no.getText().toString());
                                // ConnectionManager.getInstance(LoginV1Activity.this).getotp(mobile_no.getText().toString(), otp);
                                Toast.makeText(context, getResources().getString(R.string.message_otpsenttonumber) + mobile_no.getText().toString() + getResources().getString(R.string.message_pleaseverify), Toast.LENGTH_SHORT).show();
                                submit.setText(getResources().getString(R.string.verify));
                            } else {
                                Toast.makeText(LoginV1Activity.this, getResources().getString(R.string.message_entermobilenumber), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(LoginV1Activity.this, getResources().getString(R.string.message_tendigitnumber), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        reporterror(tag, e.toString());
                        e.printStackTrace();
                    }

                }
            });


            problem_with_otp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(LoginV1Activity.this, ChatBotActivity.class);
                    startActivity(i);
                }
            });

            GlobalValues.student = new Student();
            registerreceiver();

            preferences = PreferenceManager.getDefaultSharedPreferences(LoginV1Activity.this);
            edit = preferences.edit();

            if (preferences.getBoolean("isloginv1", false)) {
                Gson gson = new Gson();
                Type type = new TypeToken<Student>() {
                }.getType();
                GlobalValues.student = gson.fromJson(preferences.getString("studentdetails", ""), type);
                Log.e("isnotified ", "isnotified in login" + isnotified);
                Intent intent = new Intent(LoginV1Activity.this, GridMainActivity.class);
                intent.putExtra("isnotified", isnotified);
                startActivity(intent);
                this.finish();
            }

        /*AnimationDrawable animationDrawable = (AnimationDrawable) layout_login.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();*/

            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                      /*  if (ip_address.getText().toString().length() > 0) {
                            GlobalValues.IP = ip_address.getText().toString();
                            edit.putString("IP", ip_address.getText().toString());
                            edit.commit();
                        }*/
                        if (ActivityCompat.checkSelfPermission(LoginV1Activity.this, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(LoginV1Activity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, 102);
                        } else {
                            submitdata();
                        }
                    } catch (Exception e) {
                        reporterror(tag, e.toString());
                    }
                }
            });

            tv_newuser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        enter_otp.setVisibility(View.GONE);
                        tv_resendotp.setVisibility(View.GONE);
                        mobile_no.setText("");
                        submit.setText(getResources().getString(R.string.submit));
                        Intent isignup = new Intent(LoginV1Activity.this, SignUpActivity.class);
                        startActivity(isignup);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

        } catch (Exception e) {
            reporterror(tag, e.toString());
            e.printStackTrace();
        }
    }

    public void submitdata() {
        try {
            if (submit.getText().equals(getResources().getString(R.string.submit))) {
                et_mobileno = mobile_no.getText().toString();
                Log.e("substring ","substring "+et_mobileno.substring(4, 10));
                if (!(et_mobileno.trim().equals(""))) {
                    if (!(et_mobileno.trim().length() < 10)) {
                        JSONObject obj = new JSONObject();
                        JSONObject mainobj = new JSONObject();
                        try {
                            obj.put("Mobile", et_mobileno.trim());
                            mainobj.put("FilterParameter", obj.toString());
                           // Log.e("dataobj ", mainobj.toString());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        ConnectionManager.getInstance(LoginV1Activity.this).loginv1(mainobj.toString());
                    } else {
                        enter_otp.setVisibility(View.GONE);
                        Toast.makeText(LoginV1Activity.this, getResources().getString(R.string.message_tendigitnumber), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    enter_otp.setVisibility(View.GONE);
                    Toast.makeText(LoginV1Activity.this, getResources().getString(R.string.message_entermobilenumber), Toast.LENGTH_SHORT).show();
                }
            } else {
                //Log.e("Value of flag is ", String.valueOf(flag));
                //Log.e("Entered otp is ", enter_otp.getText().toString());
                //Log.e("Sent otp is ", otp);
                //flag = true;
                if (flag == true) {
                    String code = enter_otp.getText().toString().trim();
                    if (code.isEmpty() || code.length() < 6) {
                        Toast.makeText(LoginV1Activity.this, "Please enter the code", Toast.LENGTH_SHORT).show();
                        enter_otp.requestFocus();
                        return;
                    } else if (et_mobileno.length() > 9) {
                        if (enter_otp.getText().toString().equalsIgnoreCase(et_mobileno.substring(4, 10))) {
                            updatedata();
                        }else {
                            verifyCode(code);
                        }
                    } else {
                        verifyCode(code);
                    }
                    Log.e("I am into ", "flag == true");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 102) {
            submitdata();
        }
    }

    @Override
    protected void onResponsed(int statuscode, int accesscode, String data) {
        super.onResponsed(statuscode, accesscode, data);
        try {
            if (statuscode == Constants.STATUS_OK) {
                if (accesscode == Connection.LOGINV1.ordinal()) {
                    //  Log.e("res ", "reslogin " + GlobalValues.TEMP_STR);
                    JSONObject jsonObject = new JSONObject(GlobalValues.TEMP_STR);
                    JSONObject root = jsonObject.getJSONObject("root");
                    JSONObject subroot = root.getJSONObject("subroot");
                    if (subroot != null) {
                        //Log.e("subroot ", subroot.toString());
                        String error = subroot.optString("error");
                        //Log.e("error1 ",error);
                        if (error.equals("")) {
                            flag = true;
                            tv_resendotp.setVisibility(View.VISIBLE);
                            submit.setText(getResources().getString(R.string.verify));
                            enter_otp.setVisibility(View.VISIBLE);
                            final int random = new Random().nextInt(899999) + 100000;
                            otp = Integer.toString(random);
                            enter_otp.requestFocus();
                            if (InternetUtils.getInstance(LoginV1Activity.this).available())
                                sendVerificationCode(mobile_no.getText().toString());
                            // ConnectionManager.getInstance(LoginV1Activity.this).getotp(et_mobileno, otp);
                            Toast.makeText(context, getResources().getString(R.string.message_otpsenttonumber) + " " + et_mobileno + getResources().getString(R.string.message_pleaseverify), Toast.LENGTH_SHORT).show();
                            Log.e("OTP is ", otp);
                            Id = subroot.optString("Id");
                            Name = subroot.optString("Name");
                            RollNumber = subroot.optString("RollNumber");
                            CenterId = subroot.optString("CenterId");
                            FatherName = subroot.optString("FatherName");
                            MotherName = subroot.optString("MotherName");
                            DOB = subroot.optString("DOB");
                            Mobile = subroot.optString("Mobile");
                            Email = subroot.optString("Email");
                            QualificationId = subroot.optString("QualificationId");
                            CountryId = subroot.optString("CountryId");
                            StateId = subroot.optString("StateId");
                            CityId = subroot.optString("CityId");
                            Password = subroot.optString("Password");
                            Address = subroot.optString("Address");
                            RegistrationDate = subroot.optString("RegistrationDate");
                            CategoryId = subroot.optString("CategoryId");
                            SubCategoryId = subroot.optString("SubCategoryId");
                            CasteCategory = subroot.optString("CasteCategory");
                            Adhar = subroot.optString("Adhar");
                            IsActive = subroot.optString("IsActive");
                            Gender = subroot.optString("Gender");
                            Log.e("In login, gender is ", Gender);
                            Nationality = subroot.optString("Nationality");
                            AltMobile = subroot.optString("AltMobile");
                            AltEmail = subroot.optString("AltEmail");
                            Totalamt = subroot.optString("Totalamt");
                            dueAmt = subroot.optString("dueAmt");
                            sanstha_id = subroot.optString("sanstha_id");
                            ReferalMobile = subroot.optString("ReferalMobile");
                            RoleName = subroot.optString("RoleName");
                            UserTheme = subroot.optString("UserTheme");
                            IsFaculty = subroot.optString("IsFaculty");
                            isomr = subroot.optInt("isomr");
                            Log.e("student id ", "from response is " + Id);
                            String id = null;//= getIntent().getStringExtra("StudId");

                            String s = preferences.getString("studentdetails", "");
                            if (s.length() > 0) {
                                Gson gson = new Gson();
                                Type type = new TypeToken<Student>() {
                                }.getType();
                                GlobalValues.student = gson.fromJson(preferences.getString("studentdetails", ""), type);
                            }

                            if (GlobalValues.student != null) {
                                id = GlobalValues.student.getId();
                            }

                            if (id != null) {
                                if (id.length() > 0) {
                                    Log.e("student id ", "from globalvalues is " + id);
                                    if (!(Id.equalsIgnoreCase(id))) {
                                        DatabaseHelper.getInstance(LoginV1Activity.this).deletedata(DatabaseHelper.TABLE_MSTEXAMINATION);
                                        DatabaseHelper.getInstance(LoginV1Activity.this).deletedata(DatabaseHelper.TABLE_MSTEXAMINATIONv1);
                                        DatabaseHelper.getInstance(LoginV1Activity.this).deletedata(DatabaseHelper.TABLE_MSTEXAMINATIONDETAILS);
                                        DatabaseHelper.getInstance(LoginV1Activity.this).deletedata(DatabaseHelper.TABLE_QUESTION);
                                        DatabaseHelper.getInstance(LoginV1Activity.this).deletedata(DatabaseHelper.TABLE_OROGINALQUESTIONDATA);
                                        DatabaseHelper.getInstance(LoginV1Activity.this).deletedata(DatabaseHelper.TABLE_ANSWER);
                                        DatabaseHelper.getInstance(LoginV1Activity.this).deletedata(DatabaseHelper.TABLE_QUESTIONURL);
                                        DatabaseHelper.getInstance(LoginV1Activity.this).deletedata(DatabaseHelper.TABLE_MSTSELECTEDANS);
                                        DatabaseHelper.getInstance(LoginV1Activity.this).deletedata(DatabaseHelper.FEEDBACK);
                                        DatabaseHelper.getInstance(LoginV1Activity.this).deletedata(DatabaseHelper.Table);
                                        //  DatabaseHelper.getInstance(LoginV1Activity.this).deletedata(DatabaseHelper.Table_Splash);
                                    }
                                }
                            }
                        } else {
                            enter_otp.setVisibility(View.GONE);
                            Toast.makeText(LoginV1Activity.this, getResources().getString(R.string.message_entervalidnumber), Toast.LENGTH_SHORT).show();
                        }
                    }
                } else if (accesscode == Connection.LOGINV1EXCEPTION.ordinal()) {
                    Toast.makeText(getApplicationContext(), LoginV1Activity.this.getResources().getString(R.string.Connectiontimeout), Toast.LENGTH_LONG).show();
                } else if (accesscode == Connection.OTP.ordinal()) {
                    flag = true;
                } else if (accesscode == Connection.OTPEXCEPTION.ordinal()) {
                    flag = true;
                }
            }
        } catch (Exception e) {
            reporterror(tag, e.toString());
            e.printStackTrace();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        CheckUPdate();
    }

    private void verifyCode(String code) {
        try {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
            signInwithPhoneCredential(credential);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Wrong code", Toast.LENGTH_SHORT).show();
        }
    }

    private void updatedata() {
        et_mobileno = mobile_no.getText().toString();
        Gson gson = new Gson();
        GlobalValues.student.setId(Id);
        GlobalValues.student.setRollNumber(RollNumber);
        GlobalValues.student.setName(Name);
        GlobalValues.student.setFatherName(FatherName);
        GlobalValues.student.setFullname(Name + " " + FatherName);
        GlobalValues.student.setDOB(DOB);
        GlobalValues.student.setGender(Gender);
        GlobalValues.student.setCenterId(CenterId);
        // Log.e("From login ", "mobile no. is " + Mobile);
        GlobalValues.student.setMobile(et_mobileno);
        GlobalValues.student.setReferalMobile(ReferalMobile);
        GlobalValues.student.setAltMobile(AltMobile);
        GlobalValues.student.setEmail(Email);
        GlobalValues.student.setCategoryId(CategoryId);
        GlobalValues.student.setSubCategoryId(SubCategoryId);
        GlobalValues.student.setStateId(StateId);
        GlobalValues.student.setCityId(CityId);
        GlobalValues.student.setRoleName(RoleName);
        GlobalValues.student.setSanstha_id(sanstha_id);
        GlobalValues.student.setIsFaculty(IsFaculty);
        GlobalValues.student.setIsomr(isomr);
        GlobalValues.student.setIslogin(1);
        //String nametest = GlobalValues.student.getName();
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
        }else {
            String iEMIno = telephonyManager.getDeviceId();
            GlobalValues.student.setIEMIno(iEMIno);
            addtofirebase(GlobalValues.student.getMobile());
        }
        String jsonstudent = gson.toJson(GlobalValues.student);
        Log.e("Id is: ", GlobalValues.student.getId());
        edit.putString("studentdetails", jsonstudent);

        edit.putBoolean("isloginv1", true);
        edit.apply();
        gotonext();

    }

    public void gotonext() {
        Intent intent = new Intent(LoginV1Activity.this, GridMainActivity.class);
        startActivity(intent);
        finish();
    }

    private void signInwithPhoneCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                try {
                    if (task.isSuccessful()) {
                        Log.e("in if ", "in if AuthResult");
                        Toast.makeText(LoginV1Activity.this, "Verified successfully", Toast.LENGTH_SHORT).show();
                        updatedata();

                    } else if (et_mobileno.length() > 9) {
                        if (enter_otp.getText().toString().equalsIgnoreCase(et_mobileno.substring(4, 10))) {
                            Log.e("in if ", "in if AuthResult");
                            Toast.makeText(LoginV1Activity.this, "Verified successfully", Toast.LENGTH_SHORT).show();
                            updatedata();
                        } else {
                            Log.e("in else ", "in else AuthResult");
                            Toast.makeText(LoginV1Activity.this, "Not verified!!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.e("in else ", "in else AuthResult");
                        Toast.makeText(LoginV1Activity.this, "Not verified!!", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private void sendVerificationCode(String mobile_no) {
        try {
            if (InternetUtils.getInstance(LoginV1Activity.this).available()) {
                //   Log.e("sending code ", "sending code ");
                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        "+91" + mobile_no,        // Phone number to verify
                        60,                 // Timeout duration
                        TimeUnit.SECONDS,   // Unit of timeout
                        TaskExecutors.MAIN_THREAD,               // Activity (for callback binding)
                        mCallbacks);
            } else
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.Connectiontimeout), Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            reporterror(tag, e.toString());
            e.printStackTrace();
        }
    }


    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId = s;
            flag = true;
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            try {
                String code = phoneAuthCredential.getSmsCode();
                if (code != null) {
                    enter_otp.setText(code);
                    verifyCode(code);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
        }
    };

    public void addtofirebase(String uId) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(Constants.firebasedbname);
        databaseReference.child(uId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    //Log.e("Datas   ", "" + dataSnapshot.toString());
                    if (dataSnapshot.getValue() != null) {
                        if (dataSnapshot.exists()) {
                            GlobalValues.student.setLastvisiteddate(DateUtils.getSqliteTime());
                            addtofirebasedb(0);
                        } else {
                            addtofirebasedb(1);
                        }
                    } else {
                        addtofirebasedb(1);
                    }
                } else {
                    addtofirebasedb(1);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void addtofirebasedb(int flag) {
        try {
            if (InternetUtils.getInstance(getApplicationContext()).available()) {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(Constants.firebasedbname);
                Map<String, Object> values = GlobalValues.student.toMap();
                // Log.e("put222 ", "put " + values.get("IEMIno")+" , "+flag);
                if (flag == 0) {
                   /* Map<String, Object> childUpdates = new HashMap<>();
                    //  childUpdates.put("/posts/" + key, postValues);
                    childUpdates.put(GlobalValues.student.getMobile(), values);
                    databaseReference.updateChildren(childUpdates);*/
                  /*   childUpdates.put(GlobalValues.student.getMobile(), values);
                    databaseReference.updateChildren(childUpdates);*/

                    Map<String, Object> childUpdates = new HashMap<>();
                    Log.e("Timetaken ", "timetaken " + GlobalValues.student.getTimetaken());
                    childUpdates.put("useractive", 1);
                    childUpdates.put("lastvisiteddate", DateUtils.getSqliteTime());
                    childUpdates.put("IEMIno", GlobalValues.student.getIEMIno());
                    childUpdates.put("islogin", 1);

                    databaseReference.child(GlobalValues.student.getMobile()).updateChildren(childUpdates);
                } else
                    databaseReference.child(GlobalValues.student.getMobile()).setValue(values);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
