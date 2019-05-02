package com.targeteducare;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.targeteducare.Classes.SpinnerStandard;
import com.targeteducare.Classes.SpinnerStream;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Random;

public class SignUpActivity extends Activitycommon {
    Button register;
    EditText et_name, et_mobileno, et_referralmobileno, et_otp;
    TextView tv_resendotp, tv_editno;
    String namesignup, mobilesignup;
    private static final int REQ_CODE = 2102;
    //static int otp = 123;
    String otp = Integer.toString(0);

    boolean flag = false;
    SharedPreferences preferences;
    SharedPreferences.Editor edit;

    String id_board = "";
    String name_board = "" ;

    Spinner spin_stream, spin_standard;
    JSONArray subrootboard;
    ArrayList<SpinnerStream> spinnerStreams;
    ArrayAdapter<SpinnerStream> arrayAdapterstream;

    ArrayList<SpinnerStandard> spinnerStandards;
    ArrayAdapter<SpinnerStandard> arrayAdapterstandard;
    TextView tv_standard;
    View vstandard;
    String check_str, check_subboardstr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        try {
            Log.e("Otp is", otp);
        } catch (Exception e) {
            e.printStackTrace();
        }

        registerreceiver();
        preferences = PreferenceManager.getDefaultSharedPreferences(SignUpActivity.this);
        edit = preferences.edit();
        spin_stream = findViewById(R.id.spinner_stream);
        spin_standard = findViewById(R.id.spinner_standard);
        tv_standard = findViewById(R.id.tv_standard);
        vstandard = findViewById(R.id.viewforstandard);
        spin_stream.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    if (subrootboard!=null) {
                        JSONArray jsonsubcategory = subrootboard.getJSONObject(i).optJSONArray("SubCategory");
                        //Log.e("this is the mainarray: ",c.toString());

                        if (jsonsubcategory != null) {

                            tv_standard.setVisibility(View.VISIBLE);
                            vstandard.setVisibility(View.VISIBLE);
                            spin_standard.setVisibility(View.VISIBLE);

                            spinnerStandards = new ArrayList<>();
                            for (int j = 0; j < jsonsubcategory.length(); j++) {

                                JSONObject c1 = jsonsubcategory.getJSONObject(j);
                                Log.e("this is the sub array: ", c1.toString());
                                String id_subboard = c1.getString("Id");
                                String name_subboard = c1.getString("Name");
                                String deleted_subboard = c1.getString("Deleted");
                                String abbr_subboard = c1.getString("Abbr");
                                String description_subboard = c1.getString("Description");
                                String subjectid_subboard = c1.getString("SubjectId");
                                String unitid_subboard = c1.getString("UnitId");
                                String chapterid_subboard = c1.getString("ChapterId");
                                String parentid_subboard = c1.getString("ParentId");
                                Log.e("subboard name", name_subboard);
                                SpinnerStandard spinnerStandard = new SpinnerStandard(name_subboard,id_subboard);
                                spinnerStandards.add(spinnerStandard);
                            }
                            arrayAdapterstandard = new ArrayAdapter(SignUpActivity.this, android.R.layout.simple_spinner_item, spinnerStandards);
                            arrayAdapterstandard.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spin_standard.setAdapter(arrayAdapterstandard);
                            arrayAdapterstandard.notifyDataSetChanged();
                            check_subboardstr = GlobalValues.student.getSubboard_name();
                            Log.e("subboard in editprof is", check_subboardstr);

                            for (int k = 0; k < spinnerStandards.size(); k++) {
                                if (spinnerStandards.get(k).getStandard().equalsIgnoreCase(check_subboardstr)) {
                                    spin_standard.setSelection(k);
                                    break;
                                }
                            }
                        } else {
                            JSONObject c1 = subrootboard.getJSONObject(i).optJSONObject("SubCategory");
                            if(c1!=null) {
                                Log.e("this is the sub array: ", c1.toString());
                                String id_subboard = c1.getString("Id");
                                String name_subboard = c1.getString("Name");
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
                                arrayAdapterstandard = new ArrayAdapter(SignUpActivity.this, android.R.layout.simple_spinner_item, spinnerStandards);
                                arrayAdapterstandard.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spin_standard.setAdapter(arrayAdapterstandard);
                                arrayAdapterstandard.notifyDataSetChanged();
                                check_subboardstr = GlobalValues.student.getSubboard_name();
                            }else {
                                tv_standard.setVisibility(View.GONE);
                                vstandard.setVisibility(View.GONE);
                                spin_standard.setVisibility(View.GONE);
                            }
                        }
                    }else {

                    }
                } catch (Exception e) {
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
        tv_editno = findViewById(R.id.tv_editnum);
        tv_editno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag=false;
                et_mobileno.setEnabled(true);
                et_otp.setText("");
                et_otp.setVisibility(View.GONE);
                register.setText("Submit");
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
                et_otp.setText("");
                final int random1 = new Random().nextInt(899999) + 100000;
                String otp1 = Integer.toString(random1);
                Log.e("Resent otp is ", otp1);
                ConnectionManager.getInstance(SignUpActivity.this).getotp(et_mobileno.getText().toString(), otp1);
                Toast.makeText(context, "We have sent an OTP to the registered mobile number " + et_mobileno.getText().toString() + ". Please Verify the same", Toast.LENGTH_SHORT).show();
                flag = true;
                register.setText("Verify");
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                //   spinnerStandards.get(spin_standard.getSelectedItemPosition()).getStandard();
                if (register.getText().toString().equals("Submit")) {
                    final int random = new Random().nextInt(899999) + 100000;
                    otp = Integer.toString(random);
                    if (!(et_mobileno.getText().toString().trim().equals(""))) {
                        et_mobileno.setEnabled(false);
                        if (!(et_mobileno.getText().toString().trim().length() < 10)) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                            builder.setTitle("Confirmation");
                            builder.setMessage("We will verify the mobile number: " + et_mobileno.getText().toString() + "\n\nIs this OK, or would you like to edit the number?\n");
                            builder.setNegativeButton("EDIT",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            //Toast.makeText(getApplicationContext(),"No clicked",Toast.LENGTH_LONG).show();
                                        }
                                    });
                            builder.setPositiveButton("OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            //Toast.makeText(getApplicationContext(),"Yes clicked",Toast.LENGTH_LONG).show();
                                            //et_mobileno.setFocusable(false);
                                            et_otp.setVisibility(View.VISIBLE);
                                            //register.setText("Verify");
                                            flag = true;
                                            ConnectionManager.getInstance(SignUpActivity.this).getotp(et_mobileno.getText().toString(), otp);
                                            Log.e("otp enter val ",""+otp);
                                            Toast.makeText(context, "We have sent an OTP to the registered mobile number " + et_mobileno.getText().toString() + ". Please Verify the same", Toast.LENGTH_SHORT).show();
                                            register.setText("Verify");
                                        }
                                    });
                            builder.show();
                        } else {
                            Toast.makeText(SignUpActivity.this, "Please enter the 10 digit mobile number!!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(SignUpActivity.this, "Please enter the mobile number!!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e("I am in Verify", " Hi");
                    if (flag == true) {
                        if (!(et_otp.getText().toString().trim().equals(""))) {
                            if (et_otp.getText().toString().equals(otp)) {
                                JSONObject obj = new JSONObject();
                                JSONObject mainobj = new JSONObject();
                                try {
                                    obj.put("Name", et_name.getText().toString());
                                    obj.put("Mobile", et_mobileno.getText().toString());
                                    obj.put("ReferalMobile", et_referralmobileno.getText().toString());
                                    Log.e("selected ","selected "+spinnerStreams.get(spin_stream.getSelectedItemPosition()).getID()+" "+spinnerStreams.get(spin_stream.getSelectedItemPosition()).getBoard_name());
                                    if(spinnerStreams!=null) {
                                        if (spinnerStreams.size() > 0) {
                                            obj.put("CategoryId", spinnerStreams.get(spin_stream.getSelectedItemPosition()).getID());
                                        }else obj.put("CategoryId",0);
                                    }else obj.put("CategoryId",0);
                                    if(spinnerStandards!=null) {
                                        if (spinnerStandards.size() > 0) {
                                            obj.put("SubCategoryId", spinnerStandards.get(spin_standard.getSelectedItemPosition()).getId());
                                        } else obj.put("SubCategoryId", 0);
                                    }else obj.put("SubCategoryId",0);
                                    mainobj.put("FilterParameter", obj.toString());
                                    Log.e("dataobj ", mainobj.toString());

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                ConnectionManager.getInstance(SignUpActivity.this).signup(mainobj.toString());
                            } else {
                                Toast.makeText(context, "Invalid otp!!", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(context, "Please enter the otp", Toast.LENGTH_SHORT).show();
                        }
                    } else {

                    }
                }
            }
        });

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

                        String studentid = subroot.optString("StudentId");

                        GlobalValues.student.setId(studentid);
                        GlobalValues.student.setName(namesignup);
                        GlobalValues.student.setMobile(mobilesignup);


                        if(spinnerStreams!=null) {
                            if (spinnerStreams.size() > 0) {
                                GlobalValues.student.setBoard_name(spinnerStreams.get(spin_stream.getSelectedItemPosition()).getBoard_name());
                                GlobalValues.student.setCategoryId(spinnerStreams.get(spin_stream.getSelectedItemPosition()).getID());
                            }
                        }
                        if(spinnerStandards!=null) {
                            if (spinnerStandards.size() > 0) {
                                GlobalValues.student.setSubboard_name(spinnerStandards.get(spin_standard.getSelectedItemPosition()).getStandard());
                                GlobalValues.student.setSubCategoryId(spinnerStandards.get(spin_standard.getSelectedItemPosition()).getId());

                            }
                        }

                        Gson gson=new Gson();
                        String jsonstudentprofile = gson.toJson(GlobalValues.student);
                        Log.e("Id is: ", GlobalValues.student.getId());
                        edit.putString("studentdetails", jsonstudentprofile);
                        edit.putBoolean("isloginv1", true);
                        edit.apply();

                        Intent igrid = new Intent(SignUpActivity.this, GridMainActivity.class);
                        startActivity(igrid);
                        finish();

                    } else {
                        flag=false;
                        et_mobileno.setEnabled(true);
                        et_otp.setText("");
                        et_otp.setVisibility(View.GONE);
                        register.setText("Submit");
                        Toast.makeText(SignUpActivity.this, "User exists. Please Login!!", Toast.LENGTH_SHORT).show();
                    }

                } else if (accesscode == Connection.OTP.ordinal()) {
                    Log.e("value of flag ", String.valueOf(flag));

                }else if (accesscode == Connection.GETCATEGORY.ordinal()) {

                    Log.e("res", "res " + GlobalValues.TEMP_STR);

                    JSONObject jsonObject = new JSONObject(GlobalValues.TEMP_STR);
                    subrootboard = jsonObject.getJSONObject("root").optJSONArray("subroot");
                    spinnerStreams = new ArrayList<>();
                    if (subrootboard != null) {

                        for (int i = 0; i < subrootboard.length(); i++) {
                            JSONObject c = subrootboard.getJSONObject(i);
                            String totalrecord_board = c.getString("TotalRecord");
                            id_board = c.getString("Id");
                            name_board = c.getString("Name");
                            String abbr_board = c.getString("Abbr");
                            String description_board = c.getString("Description");
                            String parentid_board = c.getString("ParentId");
                            String deleted_board = c.getString("Deleted");
                            String subjectid_board = c.getString("SubjectId");
                            String unitidrecord_board = c.getString("UnitId");
                            String chapterid_board = c.getString("ChapterId");

                            SpinnerStream spinnerStream = new SpinnerStream(name_board,id_board);
                            spinnerStreams.add(spinnerStream);
                        }
                        Log.e("size ","size "+spinnerStreams.size());
                        arrayAdapterstream = new ArrayAdapter(this, android.R.layout.simple_spinner_item, spinnerStreams);
                        arrayAdapterstream.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spin_stream.setAdapter(arrayAdapterstream);
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
                        Log.e("null ","null ");
                        subrootboard=new JSONArray();
                        JSONObject c = jsonObject.getJSONObject("root").optJSONObject("subroot");
                        subrootboard.put(c);
                        String totalrecord_board = c.getString("TotalRecord");
                        String id_board = c.getString("Id");
                        String name_board = c.getString("Name");
                        String abbr_board = c.getString("Abbr");
                        String description_board = c.getString("Description");
                        String parentid_board = c.getString("ParentId");
                        String deleted_board = c.getString("Deleted");
                        String subjectid_board = c.getString("SubjectId");
                        String unitidrecord_board = c.getString("UnitId");
                        String chapterid_board = c.getString("ChapterId");

                        SpinnerStream spinnerStream = new SpinnerStream(name_board,id_board);
                        spinnerStreams.add(spinnerStream);
                        arrayAdapterstream = new ArrayAdapter(this, android.R.layout.simple_spinner_item, spinnerStreams);
                        arrayAdapterstream.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spin_stream.setAdapter(arrayAdapterstream);
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
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
