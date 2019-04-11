package com.targeteducare;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;

import org.json.JSONObject;

public class SignUp extends Activitycommon implements GoogleApiClient.OnConnectionFailedListener {

    SignInButton signIn;
    Button register;
    EditText et_name, et_mobileno, et_referralmobileno, et_otp;

    TextView nametv, emailtv;

    String namesignup,mobilesignup;

    private static final int REQ_CODE = 2102;
    private GoogleApiClient googleApiClient;

    static int otp = 123;

    SharedPreferences preferences;
    SharedPreferences.Editor edit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        registerreceiver();
        preferences = PreferenceManager.getDefaultSharedPreferences(SignUp.this);
        edit = preferences.edit();

        /*if (preferences.getBoolean("isloginv1", false)) {
            Gson gson=new Gson();
            Type type = new TypeToken<StudentProfile>(){}.getType();
            GlobalValues.studentProfile=gson.fromJson(preferences.getString("studentprofiledetails",""),type);
            Intent intent = new Intent(SignUp.this, GridMainActivity.class);
            startActivity(intent);
            this.finish();
        }*/


        signIn = findViewById(R.id.sign_in_button);

        register = findViewById(R.id.button_register);
        nametv = findViewById(R.id.gmailname);
        emailtv = findViewById(R.id.gmailemail);


        et_name = findViewById(R.id.name_signup);
        et_mobileno = findViewById(R.id.mobile_no);
        et_referralmobileno = findViewById(R.id.referalmobileno);
        et_otp = findViewById(R.id.otp);

        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this).addApi(Auth.GOOGLE_SIGN_IN_API, signInOptions).build();


        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gsin = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(gsin, REQ_CODE);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                if (!(et_mobileno.getText().toString().trim().equals(""))) {

                    if (!(et_mobileno.getText().toString().trim().length() < 10)) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.this);
                        builder.setTitle("Confirmation");
                        builder.setMessage("We will verify the mobile number: " + et_mobileno.getText() + "\n\nIs this OK, or would you like to edit the number?\n");
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
                                        register.setText("Verify");

                                        register.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                if (!(et_otp.getText().toString().trim().equals(""))) {
                                                    JSONObject obj = new JSONObject();
                                                    JSONObject mainobj = new JSONObject();

                                                    try {
                                                        obj.put("Name", et_name.getText().toString().trim());
                                                        obj.put("Mobile", et_mobileno.getText().toString().trim());
                                                        obj.put("ReferalMobile", et_referralmobileno.getText().toString().trim());
                                                       // obj.put("Otp", et_otp.getText().toString().trim());
                                                        mainobj.put("FilterParameter", obj.toString());
                                                        Log.e("dataobj ", mainobj.toString());
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }

                                                    ConnectionManager.getInstance(SignUp.this).signup(mainobj.toString());
                                                    Log.e("test purpose",mainobj.toString());

                                                    //Toast.makeText(SignUp.this, et_otp+"", Toast.LENGTH_SHORT).show();
                                                    // SERVICE CALL

                                                } else {
                                                    Toast.makeText(SignUp.this, "Enter the OTP", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }
                                });
                        builder.show();
                    } else {
                        Toast.makeText(SignUp.this, "Please enter the 10 digit mobile number!!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(SignUp.this, "Please enter the mobile number!!", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_CODE) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleResult(result);
        }
    }

    private void handleResult(GoogleSignInResult result) {
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
    }

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


                    //Log.e("exists ",subroot.toString());

                    if(subroot!=null){
                        //Toast.makeText(context, "Enter", Toast.LENGTH_SHORT).show();
                        if(et_otp.getText().toString().trim().equals(Integer.toString(otp))){

                            String studentid = subroot.optString("StudentId");
                            Gson gson = new Gson();

                            namesignup = et_name.getText().toString().trim();
                            mobilesignup = et_mobileno.getText().toString().trim();

                            GlobalValues.studentProfile.setId(studentid);
                            GlobalValues.studentProfile.setName(namesignup);
                            GlobalValues.studentProfile.setMobile(mobilesignup);

                            /*String jsonstudentprofile = gson.toJson(GlobalValues.studentProfile);
                            edit.putString("studentprofiledetails",jsonstudentprofile);*/
                            //Log.e("Name is: ",jsonstudentprofile);
                            //edit.putBoolean("isloginv1",true);

                            /*edit.putString("studentprofiledetails",jsonstudentprofile);
                            edit.apply();*/

                            Intent igrid = new Intent(SignUp.this, SelectBoard.class);
                            startActivity(igrid);
                            finish();
                        }
                        else{
                            Toast.makeText(SignUp.this, "Invalid OTP!!", Toast.LENGTH_SHORT).show();
                            //edit.putBoolean("isloginv1",false);
                            edit.apply();
                        }
                    }

                    else{
                        Toast.makeText(SignUp.this, "User exists. Please Login!!", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
