package com.targeteducare;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.targeteducare.Classes.SpinnerStandard;
import com.targeteducare.Classes.SpinnerStream;
import com.targeteducare.Classes.Student;
import com.targeteducare.database.DatabaseHelper;
import org.json.JSONArray;
import org.json.JSONObject;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class SignUpActivity extends Activitycommon {
    Button register;
    EditText et_name, et_mobileno, et_referralmobileno, et_otp,ip_address;
    TextView tv_resendotp, tv_editno,setip;
    //TextView tv_otp;
    String namesignup, mobilesignup;
    private static final int REQ_CODE = 2102;
    //static int otp = 123;
    String otp = Integer.toString(0);
    String default_spinner_value = "";
    boolean flag = false;
    SharedPreferences preferences;
    SharedPreferences.Editor edit;
    String id_board = "";
    String name_board = "";
    String name_subboard = "";
    Spinner spin_stream, spin_standard;
    JSONArray subrootboard;
    ArrayList<SpinnerStream> spinnerStreams;
    ArrayAdapter<SpinnerStream> arrayAdapterstream;
    ArrayList<SpinnerStandard> spinnerStandards;
    ArrayAdapter<SpinnerStandard> arrayAdapterstandard;
    TextView tv_standard;
    View vstandard;
    String check_str, check_subboardstr;
    //SharedPreferences pref = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
    String lang = "";
    TextView problem_with_otp;
    //private FirebaseAuth mAuth;
    //String verificationId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            screenshot_capture_permission();
            super.onCreate(savedInstanceState);
            loadLocale();
            setContentView(R.layout.activity_sign_up);
            //mAuth = FirebaseAuth.getInstance();
            registerreceiver();
            preferences = PreferenceManager.getDefaultSharedPreferences(SignUpActivity.this);
            edit = preferences.edit();
            lang = getSharedPreferences("Settings", Activity.MODE_PRIVATE).getString("Current Language", "");
            tag = this.getClass().getSimpleName();
            try {
                Log.e("Otp is", otp);
            } catch (Exception e) {
                //reporterror(tag, e.toString());
                e.printStackTrace();
            }

            ip_address=(EditText) findViewById(R.id.ip_address);
            ip_address.setText(GlobalValues.IP);
            setip=(TextView)findViewById(R.id.setip);
            setip.setVisibility(View.GONE);
            setip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
               /*     GlobalValues.IP=ip_address.getText().toString();
                    edit.putString("IP",ip_address.getText().toString());
                    edit.commit();*/
                    JSONObject obj2 = new JSONObject();
                    JSONObject mainobj2 = new JSONObject();
                    try {
                        obj2.put("Type", "Category");
                        mainobj2.put("FilterParameter", obj2.toString());
                    } catch (Exception e) {
                        //reporterror(tag, e.toString());
                        e.printStackTrace();
                    }
                    ConnectionManager.getInstance(SignUpActivity.this).getcategory(mainobj2.toString());
                }
            });

            spin_stream = findViewById(R.id.spinner_stream);
            spin_standard = findViewById(R.id.spinner_standard);
            tv_standard = findViewById(R.id.tv_standard);
            vstandard = findViewById(R.id.viewforstandard);
            problem_with_otp = findViewById(R.id.tv_problemwith_otp);

            problem_with_otp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {
                        Intent i = new Intent(SignUpActivity.this, ChatBotActivity.class);
                        startActivity(i);

                    } catch (Exception e) {
                        //reporterror(tag, e.toString());
                        e.printStackTrace();
                    }
                }
            });
            //tv_otp = findViewById(R.id.tv_otp);
            //spin_standard.setPrompt(getResources().getString(R.string.please_select));
            //spin_stream.setPrompt(getResources().getString(R.string.please_select));

            spin_stream.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                    try {

                        if (subrootboard != null) {
                            if (position > 0) {
                                JSONArray jsonsubcategory = subrootboard.getJSONObject(position - 1).optJSONArray("SubCategory");
                                //Log.e("this is the mainarray: ",c.toString());
                                if (jsonsubcategory != null) {
                                    tv_standard.setVisibility(View.VISIBLE);
                                    vstandard.setVisibility(View.VISIBLE);
                                    spin_standard.setVisibility(View.VISIBLE);
                                    spinnerStandards = new ArrayList<>();
                                    //spinnerStandards.add(new SpinnerStandard(getResources().getString(R.string.please_select), "-1"));
                                    for (int j = 0; j < jsonsubcategory.length(); j++) {
                                        JSONObject c1 = jsonsubcategory.getJSONObject(j);
                                        Log.e("this is the sub array: ", c1.toString());
                                        String id_subboard = c1.getString("Id");
                                        if ((!c1.getString("Name").equals("null")) || !(c1.getString("Name_InMarathi").equals("null"))) {
                                            if ((lang.equals("mr")) && !(c1.getString("Name_InMarathi").equals("null"))) {
                                                name_subboard = c1.getString("Name_InMarathi");
                                            } else if ((lang.equals("mr")) && (c1.getString("Name_InMarathi").equals("null"))) {
                                                name_subboard = "-";
                                            } else if ((lang.equals("En")) && (c1.getString("Name").equals("null"))) {
                                                name_subboard = "-";
                                            } else {
                                                name_subboard = c1.getString("Name");
                                            }

                                        } else {
                                            name_subboard = "-";
                                        }

                                        String deleted_subboard = c1.getString("Deleted");
                                        String abbr_subboard = c1.getString("Abbr");
                                        String description_subboard = c1.getString("Description");
                                        String subjectid_subboard = c1.getString("SubjectId");
                                        String unitid_subboard = c1.getString("UnitId");
                                        String chapterid_subboard = c1.getString("ChapterId");
                                        String parentid_subboard = c1.getString("ParentId");
                                        Log.e("subboard name", name_subboard);
                                        SpinnerStandard spinnerStandard = new SpinnerStandard(name_subboard, id_subboard);
                                        spinnerStandards.add(spinnerStandard);
                                    }

                                    spinnerStandards.add(0, new SpinnerStandard(getResources().getString(R.string.please_select), "-1"));
                                    arrayAdapterstandard = new ArrayAdapter(SignUpActivity.this, android.R.layout.simple_spinner_item, spinnerStandards);
                                    arrayAdapterstandard.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    spin_standard.setAdapter(arrayAdapterstandard);
                                    //spin_standard.setSelection(spinnerStandards.size());
                                    arrayAdapterstandard.notifyDataSetChanged();
                                    check_subboardstr = GlobalValues.student.getSubboard_name();
                                    Log.e("subboard in editprof is", check_subboardstr);

                            /*for (int k = 0; k < spinnerStandards.size(); k++) {
                                if (spinnerStandards.get(k).getStandard().equalsIgnoreCase(check_subboardstr)) {
                                    spin_standard.setSelection(k);
                                    break;
                                }
                            }*/
                                } else {
                                    tv_standard.setVisibility(View.VISIBLE);
                                    vstandard.setVisibility(View.VISIBLE);
                                    spin_standard.setVisibility(View.VISIBLE);
                                    if (position > 0) {
                                        JSONObject c1 = subrootboard.getJSONObject(position - 1).optJSONObject("SubCategory");
                                        spinnerStandards = new ArrayList<>();
                                        if (c1 != null) {
                                            Log.e("this is the sub array: ", c1.toString());
                                            String id_subboard = c1.getString("Id");
                                            if ((!c1.getString("Name").equals("null")) || !(c1.getString("Name_InMarathi").equals("null"))) {
                                                if ((lang.equals("mr")) && !(c1.getString("Name_InMarathi").equals("null"))) {
                                                    name_subboard = c1.getString("Name_InMarathi");
                                                } else if ((lang.equals("mr")) && (c1.getString("Name_InMarathi").equals("null"))) {
                                                    name_subboard = "-";
                                                } else if ((lang.equals("En")) && (c1.getString("Name").equals("null"))) {
                                                    name_subboard = "-";
                                                } else {
                                                    name_subboard = c1.getString("Name");
                                                }
                                            } else {
                                                name_subboard = "-";
                                            }

                                            String deleted_subboard = c1.getString("Deleted");
                                            String abbr_subboard = c1.getString("Abbr");
                                            String description_subboard = c1.getString("Description");
                                            String subjectid_subboard = c1.getString("SubjectId");
                                            String unitid_subboard = c1.getString("UnitId");
                                            String chapterid_subboard = c1.getString("ChapterId");
                                            String parentid_subboard = c1.getString("ParentId");
                                            Log.e("subboard name", name_subboard);
                                            SpinnerStandard spinnerStandard = new SpinnerStandard(name_subboard, id_subboard);
                                            //spinnerStandards.add(new SpinnerStandard(getResources().getString(R.string.please_select), "-1"));
                                            spinnerStandards.add(spinnerStandard);
                                            spinnerStandards.add(0, new SpinnerStandard(getResources().getString(R.string.please_select), "-1"));
                                            arrayAdapterstandard = new ArrayAdapter(SignUpActivity.this, android.R.layout.simple_spinner_item, spinnerStandards);
                                            arrayAdapterstandard.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                            spin_standard.setAdapter(arrayAdapterstandard);
                                            //   spin_standard.setSelection(spinnerStandards.size());
                                            arrayAdapterstandard.notifyDataSetChanged();
                                            check_subboardstr = GlobalValues.student.getSubboard_name();
                                        } else {
                                            tv_standard.setVisibility(View.GONE);
                                            vstandard.setVisibility(View.GONE);
                                            spin_standard.setVisibility(View.GONE);
                                        }
                                    }
                                }
                            } else {

                            }
                        } else {
                            spinnerStandards.add(0, new SpinnerStandard(getResources().getString(R.string.please_select), "-1"));
                            arrayAdapterstandard = new ArrayAdapter(SignUpActivity.this, android.R.layout.simple_spinner_item, spinnerStandards);
                            arrayAdapterstandard.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spin_standard.setAdapter(arrayAdapterstandard);
                        }
                    } catch (Exception e) {
                        //reporterror(tag, e.toString());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
            JSONObject obj2 = new JSONObject();
            JSONObject mainobj2 = new JSONObject();
            try {
                obj2.put("Type", "Category");
                mainobj2.put("FilterParameter", obj2.toString());
            } catch (Exception e) {
                //reporterror(tag, e.toString());
                e.printStackTrace();
            }
            ConnectionManager.getInstance(SignUpActivity.this).getcategory(mainobj2.toString());
        /*if (preferences.getBoolean("isloginv1", false)) {
            Gson gson=new Gson();
            Type type = new TypeToken<StudentProfile>(){}.getType();
            GlobalValues.studentProfile=gson.fromJson(preferences.getString("studentprofiledetails",""),type);
            Intent intent = new Intent(SignUp.this, GridMainActivity.class);
            startActivity(intent);
            this.finish();
        }*/


            // signIn = findViewById(R.id.sign_in_button);

            register = findViewById(R.id.button_register);
   /*     nametv = findViewById(R.id.gmailname);
        emailtv = findViewById(R.id.gmailemail);*/


            et_name = findViewById(R.id.name_signup);
            et_mobileno = findViewById(R.id.mobile_no);
            et_referralmobileno = findViewById(R.id.referalmobileno);
            et_otp = findViewById(R.id.otp);

            tv_resendotp = findViewById(R.id.tv_resendotp);
            tv_resendotp.setVisibility(View.GONE);

            tv_editno = findViewById(R.id.tv_editnum);
            tv_editno.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        flag = false;
                        et_mobileno.setEnabled(true);
                        et_otp.setText("");
                        //tv_otp.setVisibility(View.GONE);
                        et_otp.setVisibility(View.GONE);
                        register.setText(getResources().getString(R.string.submit));
                    } catch (Exception e) {
                        //reporterror(tag, e.toString());
                        e.printStackTrace();
                    }
                }
            });
       /* GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this).addApi(Auth.GOOGLE_SIGN_IN_API, signInOptions).build();


        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gsin = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(gsin, REQ_CODE);
            }
        });*/

            tv_resendotp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        et_otp.setText("");
                        et_otp.requestFocus();
                        final int random1 = new Random().nextInt(899999) + 100000;
                        otp = Integer.toString(random1);

                        if (!(et_mobileno.getText().toString().trim().length() < 10)) {
                            if (!et_mobileno.getText().toString().equals("")) {
                                Log.e("Resent otp is ", otp);
                                //sendVerificationCode(et_mobileno.getText().toString());
                                ConnectionManager.getInstance(SignUpActivity.this).getotp(et_mobileno.getText().toString(), getResources().getString(R.string.message_otp_verifycode) + " " + otp);
                                Toast.makeText(context, getResources().getString(R.string.message_otpsenttonumber) + " " + et_mobileno.getText().toString() + getResources().getString(R.string.message_pleaseverify), Toast.LENGTH_SHORT).show();
                                register.setText(getResources().getString(R.string.verify));
                            } else {
                                Toast.makeText(SignUpActivity.this, getResources().getString(R.string.message_entermobilenumber), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(SignUpActivity.this, getResources().getString(R.string.message_tendigitnumber), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        //reporterror(tag, e.toString());
                        e.printStackTrace();
                    }
                }
            });

            register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    try {
                        //   spinnerStandards.get(spin_standard.getSelectedItemPosition()).getStandard();
                        if (register.getText().toString().equals(getResources().getString(R.string.submit))) {
                            final int random = new Random().nextInt(899999) + 100000;
                            otp = Integer.toString(random);
                            if (!(et_mobileno.getText().toString().trim().equals(""))) {
                                et_mobileno.setEnabled(false);
                                if (!(et_mobileno.getText().toString().trim().length() < 10)) {
                                    if (spin_stream.getSelectedItemPosition() < spinnerStreams.size() || spin_stream.getSelectedItemPosition() > -1) {
                                        if ((spinnerStandards.size() > 0)) {

                                            if (!((spinnerStreams.get(spin_stream.getSelectedItemPosition()).getID()).equalsIgnoreCase("-1")) && (!(spinnerStandards.get(spin_standard.getSelectedItemPosition()).getId()).equalsIgnoreCase("-1"))) {

                                                AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                                                builder.setTitle(getResources().getString(R.string.confirmation));
                                                builder.setMessage(getResources().getString(R.string.dialog_wewillverifymobilenumber) + et_mobileno.getText().toString() + "\n\n" + getResources().getString(R.string.dialog_canweverifythenumber) + "\n");
                                                builder.setNegativeButton(getResources().getString(R.string.dialog_edit),
                                                        new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                et_mobileno.setEnabled(true);
                                                                //Toast.makeText(getApplicationContext(),"No clicked",Toast.LENGTH_LONG).show();
                                                            }
                                                        });
                                                builder.setPositiveButton(getResources().getString(R.string.ok),
                                                        new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                //Toast.makeText(getApplicationContext(),"Yes clicked",Toast.LENGTH_LONG).show();
                                                                //et_mobileno.setFocusable(false);
                                                                //tv_otp.setVisibility(View.VISIBLE);

                                                                JSONObject obj = new JSONObject();
                                                                JSONObject mainobj = new JSONObject();
                                                                try {
                                                                    obj.put("Name", et_name.getText().toString());
                                                                    obj.put("Mobile", et_mobileno.getText().toString());
                                                                    obj.put("ReferalMobile", et_referralmobileno.getText().toString());
                                                                    Log.e("selected ", "selected " + spinnerStreams.get(spin_stream.getSelectedItemPosition()).getID() + " " + spinnerStreams.get(spin_stream.getSelectedItemPosition()).getBoard_name());
                                                                    if (spinnerStreams != null) {
                                                                        if (spinnerStreams.size() > 0) {
                                                                            obj.put("CategoryId", spinnerStreams.get(spin_stream.getSelectedItemPosition()).getID());
                                                                        } else
                                                                            obj.put("CategoryId", 0);
                                                                    } else obj.put("CategoryId", 0);
                                                                    if (spinnerStandards != null) {
                                                                        if (spinnerStandards.size() > 0) {
                                                                            obj.put("SubCategoryId", spinnerStandards.get(spin_standard.getSelectedItemPosition()).getId());
                                                                        } else
                                                                            obj.put("SubCategoryId", 0);
                                                                    } else
                                                                        obj.put("SubCategoryId", 0);
                                                                    mainobj.put("FilterParameter", obj.toString());
                                                                    Log.e("dataobj ", mainobj.toString());

                                                                } catch (Exception e) {
                                                                    //reporterror(tag, e.toString());
                                                                    e.printStackTrace();
                                                                }
                                                                flag = true;

                                                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(Constants.firebasedbname);
                                                                if(mobilenotblankandlengthten(et_mobileno.getText().toString())){
                                                                    databaseReference.child(et_mobileno.getText().toString()).addValueEventListener(new ValueEventListener() {
                                                                        @Override
                                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                                            if(dataSnapshot.exists()){
                                                                                if(dataSnapshot.child("IEMIno").getValue()!=null){
                                                                                    String iemi = dataSnapshot.child("IEMIno").getValue(String.class);

                                                                                    if(iemi.equalsIgnoreCase(Constants.IEMIno)){
                                                                                        Log.e("iemi inside signup ", iemi);

                                                                                        et_otp.setVisibility(View.GONE);
                                                                                        tv_resendotp.setVisibility(View.GONE);
                                                                                        updatedata();
                                                                                    }else{
                                                                                        //userexists = false;
                                                                                        register.setText(getResources().getString(R.string.verify));
                                                                                        et_otp.setVisibility(View.VISIBLE);
                                                                                        tv_resendotp.setVisibility(View.VISIBLE);
                                                                                        et_mobileno.setEnabled(false);
                                                                                        final int random = new Random().nextInt(899999) + 100000;
                                                                                        otp = Integer.toString(random);
                                                                                        et_otp.requestFocus();
                                                                                        if (InternetUtils.getInstance(SignUpActivity.this).available()){
                                                                                            ConnectionManager.getInstance(SignUpActivity.this).getotp(et_mobileno.getText().toString(), getResources().getString(R.string.message_otp_verifycode) + " " + otp);
                                                                                            Toast.makeText(context, getResources().getString(R.string.message_otpsenttonumber) + " " + et_mobileno + getResources().getString(R.string.message_pleaseverify), Toast.LENGTH_SHORT).show();
                                                                                            //Log.e("otp enter val ", "" + otp);
                                                                                        }

                                                                                        //Toast.makeText(context, getResources().getString(R.string.message_otpsenttonumber) + " " + et_mobileno.getText().toString() + getResources().getString(R.string.message_pleaseverify), Toast.LENGTH_SHORT).show();


                                                                                    }
                                                                                }
                                                                            }else{
                                                                                register.setText(getResources().getString(R.string.verify));
                                                                                et_otp.setVisibility(View.VISIBLE);
                                                                                tv_resendotp.setVisibility(View.VISIBLE);
                                                                                et_mobileno.setEnabled(false);
                                                                                final int random = new Random().nextInt(899999) + 100000;
                                                                                otp = Integer.toString(random);
                                                                                et_otp.requestFocus();
                                                                                if (InternetUtils.getInstance(SignUpActivity.this).available()){
                                                                                    ConnectionManager.getInstance(SignUpActivity.this).getotp(et_mobileno.getText().toString(), getResources().getString(R.string.message_otp_verifycode) + " " +  otp);
                                                                                    Toast.makeText(context, getResources().getString(R.string.message_otpsenttonumber) + " " + et_mobileno + getResources().getString(R.string.message_pleaseverify), Toast.LENGTH_SHORT).show();
                                                                                    //Log.e("otp enter val ", "" + otp);
                                                                                }
                                                                            }


                                                                        }

                                                                        @Override
                                                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                        }
                                                                    });
                                                                }
                                                            }
                                                        });
                                                builder.show();

                                            } else {
                                                Toast.makeText(context, getResources().getString(R.string.please_select_streamandstandard), Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            Toast.makeText(getApplicationContext(), R.string.contact, Toast.LENGTH_LONG).show();

                                        }
                                    } else {
                                        Toast.makeText(context, getResources().getString(R.string.please_select_streamandstandard), Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    et_mobileno.setEnabled(true);
                                    Toast.makeText(SignUpActivity.this, getResources().getString(R.string.message_tendigitnumber), Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(SignUpActivity.this, getResources().getString(R.string.message_entermobilenumber), Toast.LENGTH_SHORT).show();
                            }
                            et_mobileno.setEnabled(true);
                        } else {
                            //Log.e("I am in Verify", " Hi");
                            if (flag == true) {
                                if (!(et_otp.getText().toString().trim().equals(""))) {
                                    if(et_mobileno.length()>9) {
                                        if (et_otp.getText().toString().equalsIgnoreCase(et_mobileno.getText().toString().substring(4, 10))) {
                                            updatedata();
                                        }else {
                                            verifyCode(et_otp.getText().toString());
                                        }
                                    }else
                                        verifyCode(et_otp.getText().toString());
                                } else {
                                    Toast.makeText(context, getResources().getString(R.string.message_enterotp), Toast.LENGTH_SHORT).show();
                                }
                            } else {

                            }
                        }


                    } catch (Exception e) {
                        //reporterror(tag, e.toString());
                        e.printStackTrace();
                    }
                }
            });


        } catch (Exception e) {
            //reporterror(tag, e.toString());
            e.printStackTrace();
        }

    }

  /*  @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

      /*  if (requestCode == REQ_CODE) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleResult(result);
        }*/
    }

/*    private void handleResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            GoogleSignInAccount account = result.getSignInAccount();
            String name = account.getDisplayName();
            String email = account.getEmail();
            //String img_url = account.getPhotoUrl().toString();
            //nametv.setText(name);
            //emailtv.setText(email);

            updateUI(true);
        } else {
            updateUI(false);
        }
    }*/

    private void updateUI(boolean isLogin) {
    }


    @Override
    protected void onResponsed(int statuscode, int accesscode, String data) {
        super.onResponsed(statuscode, accesscode, data);
        try {
            if (statuscode == Constants.STATUS_OK) {
                if (accesscode == Connection.SIGNUP.ordinal()) {
                    Log.e("res ", "reslogin " + GlobalValues.TEMP_STR);
                    JSONObject jsonObject = new JSONObject(GlobalValues.TEMP_STR);
                    JSONObject root = jsonObject.getJSONObject("root");
                    JSONObject subroot = root.optJSONObject("subroot");
                    if (subroot != null) {
                        namesignup = et_name.getText().toString().trim();
                        mobilesignup = et_mobileno.getText().toString().trim();

                        //String studentid = subroot.optString("StudentId");
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
                                if (!(subroot.optString("Id").equalsIgnoreCase(id))) {
                                    DatabaseHelper.getInstance(SignUpActivity.this).deletedata(DatabaseHelper.TABLE_MSTEXAMINATION);
                                    DatabaseHelper.getInstance(SignUpActivity.this).deletedata(DatabaseHelper.TABLE_MSTEXAMINATIONv1);
                                    DatabaseHelper.getInstance(SignUpActivity.this).deletedata(DatabaseHelper.TABLE_MSTEXAMINATIONDETAILS);
                                    DatabaseHelper.getInstance(SignUpActivity.this).deletedata(DatabaseHelper.TABLE_QUESTION);
                                    DatabaseHelper.getInstance(SignUpActivity.this).deletedata(DatabaseHelper.TABLE_OROGINALQUESTIONDATA);
                                    DatabaseHelper.getInstance(SignUpActivity.this).deletedata(DatabaseHelper.TABLE_ANSWER);
                                    DatabaseHelper.getInstance(SignUpActivity.this).deletedata(DatabaseHelper.TABLE_QUESTIONURL);
                                    DatabaseHelper.getInstance(SignUpActivity.this).deletedata(DatabaseHelper.TABLE_MSTSELECTEDANS);
                                }
                            }
                        }

                        String Id = subroot.optString("Id");
                        String Name = subroot.optString("Name");
                        String RollNumber = subroot.optString("RollNumber");
                        String MotherName = subroot.optString("MotherName");
                        String CenterId = subroot.optString("CenterId");
                        String FatherName = subroot.optString("FatherName");
                        String DOB = subroot.optString("DOB");
                        String Mobile = subroot.optString("Mobile");
                        String Email = subroot.optString("Email");
                        String QualificationId = subroot.optString("QualificationId");
                        String CountryId = subroot.optString("CountryId");
                        String StateId = subroot.optString("StateId");
                        String CityId = subroot.optString("CityId");
                        String Password = subroot.optString("Password");
                        String Address = subroot.optString("Address");
                        String RegistrationDate = subroot.optString("RegistrationDate");
                        String CategoryId = subroot.optString("CategoryId");
                        String SubCategoryId = subroot.optString("SubCategoryId");
                        String CasteCategory = subroot.optString("CasteCategory");
                        String Adhar = subroot.optString("Adhar");
                        String IsActive = subroot.optString("IsActive");
                        String Gender = subroot.optString("Gender");
                        String Nationality = subroot.optString("Nationality");
                        String AltMobile = subroot.optString("AltMobile");
                        String AltEmail = subroot.optString("AltEmail");
                        String Totalamt = subroot.optString("Totalamt");
                        String dueAmt = subroot.optString("dueAmt");
                        String sanstha_id = subroot.optString("sanstha_id");
                        String ReferalMobile = subroot.optString("ReferalMobile");
                        String RoleName = subroot.optString("RoleName");
                        String UserTheme = subroot.optString("UserTheme");
                        String IsFaculty = subroot.optString("IsFaculty");
                        int isomr = subroot.optInt("isomr");

                        Log.e("id ", "" + Id);
                        Log.e("id ", "" + Name);
                        Log.e("id ", "" + DOB);
                        Log.e("id ", "" + Email);
                        Log.e("id ", "" + Gender);


                        GlobalValues.student.setId(Id);
                        GlobalValues.student.setName(Name);
                        GlobalValues.student.setFatherName(FatherName);
                        GlobalValues.student.setDOB(DOB);
                        GlobalValues.student.setGender(Gender);
                        GlobalValues.student.setMobile(mobilesignup);
                        GlobalValues.student.setAltMobile(AltMobile);
                        GlobalValues.student.setEmail(Email);
                        GlobalValues.student.setCategoryId(CategoryId);
                        GlobalValues.student.setSubCategoryId(SubCategoryId);
                        GlobalValues.student.setCountryId(CountryId);
                        GlobalValues.student.setStateId(StateId);
                        GlobalValues.student.setCityId(CityId);
                        GlobalValues.student.setSanstha_id(sanstha_id);
                        GlobalValues.student.setCenterId(CenterId);
                        GlobalValues.student.setReferalMobile(ReferalMobile);
                        GlobalValues.student.setRollNumber(RollNumber);

                        GlobalValues.student.setId(Id);
                        //GlobalValues.student.setName(namesignup);
                        GlobalValues.student.setMobile(mobilesignup);
                        GlobalValues.student.setIsFaculty(IsFaculty);
                        GlobalValues.student.setIsomr(isomr);
                        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                          //  return;
                        }else {
                            String iEMIno = telephonyManager.getDeviceId();
                            GlobalValues.student.setIEMIno(iEMIno);
                            addtofirebase(GlobalValues.student.getMobile());
                        }
                        /*if (spinnerStreams != null) {
                            if (spinnerStreams.size() > 0) {
                                GlobalValues.student.setBoard_name(spinnerStreams.get(spin_stream.getSelectedItemPosition()).getBoard_name());
                                GlobalValues.student.setCategoryId(spinnerStreams.get(spin_stream.getSelectedItemPosition()).getID());
                            }
                        }
                        if (spinnerStandards != null) {
                            if (spinnerStandards.size() > 0) {
                                GlobalValues.student.setSubboard_name(spinnerStandards.get(spin_standard.getSelectedItemPosition()).getStandard());
                                GlobalValues.student.setSubCategoryId(spinnerStandards.get(spin_standard.getSelectedItemPosition()).getId());

                            }
                        }*/
                        if(lang.equalsIgnoreCase("mr")){
                            ConnectionManager.getInstance(SignUpActivity.this).getotp(et_mobileno.getText().toString(), getResources().getString(R.string.message_downloadapp_thankyou)+ GlobalValues.student.getRollNumber() + " आहे.");
                            Log.e("message", " in marathi");
                        }else{
                            ConnectionManager.getInstance(SignUpActivity.this).getotp(et_mobileno.getText().toString(), getResources().getString(R.string.message_downloadapp_thankyou) + GlobalValues.student.getRollNumber());
                            Log.e("message", " in english");
                        }


                        Gson gson = new Gson();
                        String jsonstudentprofile = gson.toJson(GlobalValues.student);
                        Log.e("Id is: ", GlobalValues.student.getId());
                        edit.putString("studentdetails", jsonstudentprofile);
                        edit.putBoolean("isloginv1", true);
                        edit.apply();

                        Intent igrid = new Intent(SignUpActivity.this, GridMainActivity.class);
                        startActivity(igrid);
                        finish();


                    } else {
                        flag = false;
                        et_mobileno.setEnabled(true);
                        et_otp.setText("");
                        //tv_otp.setVisibility(View.GONE);
                        et_otp.setVisibility(View.GONE);
                        register.setText(getResources().getString(R.string.submit));
                        //Toast.makeText(SignUpActivity.this, getResources().getString(R.string.message_user_exists_login), Toast.LENGTH_SHORT).show();
                    }

                } else if (accesscode == Connection.OTP.ordinal()) {
                    Log.e("value of flag ", String.valueOf(flag));
                    flag = true;

                } else if (accesscode == Connection.OTPEXCEPTION.ordinal()) {
                    flag = true;
                } else if (accesscode == Connection.GETCATEGORY.ordinal()) {
                    Log.e("res", "res " + GlobalValues.TEMP_STR);
                    JSONObject jsonObject = new JSONObject(GlobalValues.TEMP_STR);
                    subrootboard = jsonObject.getJSONObject("root").optJSONArray("subroot");
                    spinnerStreams = new ArrayList<>();
                    if (subrootboard != null) {
                        //spinnerStreams.add(new SpinnerStream(getResources().getString(R.string.please_select), "-1"));

                        for (int i = 0; i < subrootboard.length(); i++) {
                            JSONObject c = subrootboard.getJSONObject(i);
                            String totalrecord_board = c.getString("TotalRecord");
                            id_board = c.getString("Id");
                            if (lang.equals("mr")) {
                                name_board = c.getString("Name_InMarathi");
                            } else {
                                name_board = c.getString("Name");
                            }
                            Log.e("You have chosen ", name_board + "");
                            String abbr_board = c.getString("Abbr");
                            String description_board = c.getString("Description");
                            String parentid_board = c.getString("ParentId");
                            String deleted_board = c.getString("Deleted");
                            String subjectid_board = c.getString("SubjectId");
                            String unitidrecord_board = c.getString("UnitId");
                            String chapterid_board = c.getString("ChapterId");

                            SpinnerStream spinnerStream = new SpinnerStream(name_board, id_board);
                            spinnerStreams.add(spinnerStream);
                        }
                        spinnerStreams.add(0, new SpinnerStream(getResources().getString(R.string.please_select), "-1"));
                        Log.e("size ", "size " + spinnerStreams.size());
                        arrayAdapterstream = new ArrayAdapter(this, android.R.layout.simple_spinner_item, spinnerStreams);
                        arrayAdapterstream.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        spin_stream.setAdapter(arrayAdapterstream);
                        // spin_stream.setSelection(spinnerStreams.size());

                        //spin_standard.setPrompt(getResources().getString(R.string.please_select));
                        //spin_stream.setPrompt(getResources().getString(R.string.please_select));
                        arrayAdapterstream.notifyDataSetChanged();

                      /*  check_str =GlobalValues.student.getBoard_name();
                        Log.e("board name is ", check_str);
                        for (int check = 0; check < spinnerStreams.size(); check++) {
                            Log.e("spinner board name is ", spinnerStreams.get(check).getBoard_name());

                            if (spinnerStreams.get(check).getBoard_name().equalsIgnoreCase(check_str)) {
                                spin_stream.setSelection(check);
                                break;
                            }
                        }*/
                    } else {
                        Log.e("null ", "null ");
                        subrootboard = new JSONArray();
                        JSONObject c = jsonObject.getJSONObject("root").optJSONObject("subroot");
                        subrootboard.put(c);
                        String totalrecord_board = c.getString("TotalRecord");
                        String id_board = c.getString("Id");
                        if (lang.equals("mr")) {
                            name_board = c.getString("Name_InMarathi");
                        } else {
                            name_board = c.getString("Name");
                        }

                        String abbr_board = c.getString("Abbr");
                        String description_board = c.getString("Description");
                        String parentid_board = c.getString("ParentId");
                        String deleted_board = c.getString("Deleted");
                        String subjectid_board = c.getString("SubjectId");
                        String unitidrecord_board = c.getString("UnitId");
                        String chapterid_board = c.getString("ChapterId");

                        SpinnerStream spinnerStream = new SpinnerStream(name_board, id_board);
                        //spinnerStreams.add(new SpinnerStream(getResources().getString(R.string.please_select), "-1"));
                        spinnerStreams.add(spinnerStream);
                        spinnerStreams.add(0, new SpinnerStream(getResources().getString(R.string.please_select), "-1"));

                        arrayAdapterstream = new ArrayAdapter(this, android.R.layout.simple_spinner_item, spinnerStreams);
                        arrayAdapterstream.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        spin_stream.setAdapter(arrayAdapterstream);
                        //  spin_stream.setSelection(spinnerStreams.size());

                        //spin_standard.setPrompt(getResources().getString(R.string.please_select));
                        //spin_stream.setPrompt(getResources().getString(R.string.please_select));
                        arrayAdapterstream.notifyDataSetChanged();
                    }

                    JSONObject obj = new JSONObject();
                    JSONObject mainobj = new JSONObject();

                    try {
                        obj.put("CountryName", 1);
                        mainobj.put("FilterParameter", obj.toString());
                        Log.e("dataobj ", mainobj.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    ConnectionManager.getInstance(SignUpActivity.this).editprofile(mainobj.toString());
                }else if(accesscode==Connection.GETCATEGORYEXCEPTION.ordinal())
                {
                    Toast.makeText(getApplicationContext(),SignUpActivity.this.getResources().getString(R.string.Connectiontimeout),Toast.LENGTH_LONG).show();
                }else if(accesscode==Connection.SIGNUPEXCEPTION.ordinal())
                {
                    Toast.makeText(getApplicationContext(),SignUpActivity.this.getResources().getString(R.string.Connectiontimeout),Toast.LENGTH_LONG).show();
                }
            }
        } catch (Exception e) {
            //or(tag, e.toString());
            e.printStackTrace();
        }
    }
    public void addtofirebase(String uId) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(Constants.firebasedbname);;
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
                   // Log.e("Timetaken ","timetaken "+GlobalValues.student.getTimetaken());
                    childUpdates.put(  "useractive",1);
                    childUpdates.put(  "lastvisiteddate",DateUtils.getSqliteTime());
                    childUpdates.put("IEMIno", Constants.IEMIno);
                    childUpdates.put("Mobile", GlobalValues.student.getMobile());
                    childUpdates.put("islogin",1);
                    databaseReference.child(GlobalValues.student.getMobile()).updateChildren(childUpdates);

                } else
                    databaseReference.child(GlobalValues.student.getMobile()).setValue(values);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /*private void sendVerificationCode(String mobile_no) {
        try {
            if (InternetUtils.getInstance(SignUpActivity.this).available()) {
                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        "+91" + mobile_no,        // Phone number to verify
                        60,                 // Timeout duration
                        TimeUnit.SECONDS,   // Unit of timeout
                        TaskExecutors.MAIN_THREAD,               // Activity (for callback binding)
                        mCallbacks);
            } else
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.Connectiontimeout), Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            //reporterror(tag, e.toString());
        }

    }*/

    /*private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            try {
                verificationId = s;
                flag = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            try {
                if (code != null) {
                    et_otp.setText(code);
                    verifyCode(code);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
        }
    };*/

    private void verifyCode(String code) {

        if(otp.equals(code)){
            Toast.makeText(SignUpActivity.this, "Verified successfully", Toast.LENGTH_SHORT).show();
            updatedata();
        }else{
            Toast.makeText(SignUpActivity.this, "Not verified!!", Toast.LENGTH_SHORT).show();
        }


        /*try {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
            signInwithPhoneCredential(credential);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Wrong code", Toast.LENGTH_SHORT).show();
        }*/
    }

    public void updatedata() {
        try {
            JSONObject obj = new JSONObject();
            JSONObject mainobj = new JSONObject();
            try {
                obj.put("Name", et_name.getText().toString());
                obj.put("Mobile", et_mobileno.getText().toString());
                obj.put("ReferalMobile", et_referralmobileno.getText().toString());
                //Log.e("selected ", "selected " + spinnerStreams.get(spin_stream.getSelectedItemPosition()).getID() + " " + spinnerStreams.get(spin_stream.getSelectedItemPosition()).getBoard_name());
                if (spinnerStreams != null) {
                    if (spinnerStreams.size() > 0) {
                        obj.put("CategoryId", spinnerStreams.get(spin_stream.getSelectedItemPosition()).getID());
                    } else obj.put("CategoryId", 0);
                } else obj.put("CategoryId", 0);
                if (spinnerStandards != null) {
                    if (spinnerStandards.size() > 0) {
                        obj.put("SubCategoryId", spinnerStandards.get(spin_standard.getSelectedItemPosition()).getId());
                    } else obj.put("SubCategoryId", 0);
                } else obj.put("SubCategoryId", 0);
                mainobj.put("FilterParameter", obj.toString());
                //Log.e("dataobj ", mainobj.toString());

            } catch (Exception e) {
                e.printStackTrace();
            }

            ConnectionManager.getInstance(SignUpActivity.this).signup(mainobj.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*private void signInwithPhoneCredential(PhoneAuthCredential credential) {

        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                try {
                    if (task.isSuccessful() ) {
                        updatedata();
                    }else if(et_mobileno.length()>9){
                        if(et_otp.getText().toString().equalsIgnoreCase(et_mobileno.getText().toString().substring(4, 10)))
                        {
                            updatedata();
                        }else {
                            Toast.makeText(context, getResources().getString(R.string.message_invalidotp), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(context, getResources().getString(R.string.message_invalidotp), Toast.LENGTH_SHORT).show();
                    }



                      *//*  Intent i = new Intent(SignUpActivity.this, GridMainActivity.class);
                        edit.putBoolean("islogin", true);
                        edit.apply();
                        startActivity(i);
                        finish();*//*

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }*/
}
