package com.targeteducare;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.targeteducare.Adapter.UserProfAdapter;
import com.targeteducare.Classes.ArrUserProfData;
import com.targeteducare.Classes.Student;
import com.targeteducare.Classes.UserProfModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserProfileActivity extends Activitycommon {
    private static ArrayList<UserProfModel> userProfModels;
    private UserProfAdapter adapter;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    TextView tv_username;
    ImageView profile_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        try {
            screenshot_capture_permission();
            super.onCreate(savedInstanceState);
            loadLocale();
            setContentView(R.layout.activity_user_profile);

            tag = this.getClass().getSimpleName();
            setmaterialDesign();
            back();
            setTitle(Constants.TITLE);
            toolbar.setTitleMargin(30, 10, 10, 10);
            tv_username = findViewById(R.id.nameuserprof);
            profile_image = findViewById(R.id.iconuserprof);

            preferences = PreferenceManager.getDefaultSharedPreferences(UserProfileActivity.this);
            editor = preferences.edit();

            Gson gson = new Gson();
            Type type = new TypeToken<Student>() {
            }.getType();
            GlobalValues.student = gson.fromJson(preferences.getString("studentdetails", ""), type);
            preferences.getString("studentdetails", "");
            tv_username.setText(GlobalValues.student.getFullname());

            RecyclerView recyclerView = findViewById(R.id.recyclerviewforusersettings);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 3);
            recyclerView.setLayoutManager(gridLayoutManager);

            ArrayList<String> userprofiledata = new ArrayList<>();
            userprofiledata.add(getResources().getString(R.string.edit_profile));
            userprofiledata.add(getResources().getString(R.string.reward_points));
            userprofiledata.add(getResources().getString(R.string.my_subscriptions));
            //userprofiledata.add(getResources().getString(R.string.redeem_coupons));
            userprofiledata.add(getResources().getString(R.string.refer_earn));
            userprofiledata.add(getResources().getString(R.string.gift_cards));
            userprofiledata.add(getResources().getString(R.string.settings));


            userProfModels = new ArrayList<UserProfModel>();
            // for (int i = 0; i < ArrUserProfData.userprofsettingsdata.length; i++) {
            //  UserProfModel data = new UserProfModel(userprofiledata.get(i), ArrUserProfData.userprofsettings_image[i]);

            if (GlobalValues.student.getIsomr() == 0 && GlobalValues.student.getIsFaculty().equalsIgnoreCase("0") )
                userProfModels.add(new UserProfModel(getResources().getString(R.string.edit_profile), R.mipmap.edit_profile));


            userProfModels.add(new UserProfModel(getResources().getString(R.string.my_subscriptions), R.mipmap.subscription));
            userProfModels.add(new UserProfModel(getResources().getString(R.string.settings), R.mipmap.settings));
            userProfModels.add(new UserProfModel(getResources().getString(R.string.refer_earn), R.mipmap.refer));
            userProfModels.add(new UserProfModel(getResources().getString(R.string.reward_points), R.mipmap.reward_points));

            //userprofiledata.add(getResources().getString(R.string.redeem_coupons));

            userProfModels.add(new UserProfModel(getResources().getString(R.string.gift_cards), R.mipmap.gift_cards));


            //  }
            adapter = new UserProfAdapter(UserProfileActivity.this, userProfModels);
            recyclerView.setAdapter(adapter);


        } catch (Exception e) {
            reporterror(tag, e.toString());
            e.printStackTrace();
        }

    }

    public void referto(int position) {
        try {
            switch (position) {
                case R.mipmap.edit_profile:
                    Intent ieditprof = new Intent(UserProfileActivity.this, EditProfileActivity.class);
                    startActivity(ieditprof);
                    break;
                case R.mipmap.reward_points:
                    Intent irwdpt = new Intent(UserProfileActivity.this, RewardPointsActivity.class);
                    startActivity(irwdpt);
                    break;
                case R.mipmap.subscription:
                    Intent ieditpro = new Intent(UserProfileActivity.this, MySubscriptionsActivity.class);
                    startActivity(ieditpro);
                    break;
                case R.mipmap.refer:
                    Intent irefer = new Intent(UserProfileActivity.this, ReferAndEarnActivity.class);
                    startActivity(irefer);
                   /*  String msg="Hello,Now  Tests are available in Marathi and English. I am using it. Just download the app Target Educare PEAK  https://play.google.com/store/apps/details?id=com.targeteducare \n use my refernce code TEPLPEAK" + GlobalValues.student.getId() + "\n " +"via  Target Educare PEAK";
                    sharelinkusingintent(msg);*/
                    break;

                default:
                    Toast.makeText(UserProfileActivity.this, UserProfileActivity.this.getResources().getString(R.string.nodata), Toast.LENGTH_LONG).show();
                    break;
               /* case R.mipmap.refer:
                    break;
                case R.mipmap.gift_cards:
                    break;
                case R.mipmap.settings:
                    break;*/
            }

        } catch (Exception e) {
            reporterror(tag, e.toString());
            e.printStackTrace();
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        tv_username.setText(getResources().getString(R.string.terno) + ": " + GlobalValues.student.getId());
        //tv_username.setText(GlobalValues.student.getFullname() + "\n(" + getResources().getString(R.string.terno) + ": " + GlobalValues.student.getId() + ")");
        StructureClass.defineContext(UserProfileActivity.this);
        try {
            File f2 = new File(StructureClass.generate(context.getResources().getString(R.string.storage_name)));
            File f1 = new File(f2.getAbsolutePath() + "/" + GlobalValues.student.getId() + Constants.PROFILE_PIC + Constants.FILE_NAME_EXT);
            if ((f1.exists()) && (ActivityCompat.checkSelfPermission(UserProfileActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
                Bitmap myBitmap = BitmapFactory.decodeFile(f1.getAbsolutePath());
                profile_image.setImageBitmap(myBitmap);
            } else if (ActivityCompat.checkSelfPermission(UserProfileActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                profile_image.setImageResource(R.mipmap.profile);
            } else {
                profile_image.setImageResource(R.mipmap.profile);
            }
            Log.e("File path ", f1.toString());
        }catch (OutOfMemoryError error){
            reporterror(tag, error.toString());
        } catch (Exception e) {
            reporterror(tag, e.toString());
            e.printStackTrace();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        try {
            MenuInflater menuInflater = getMenuInflater();
            menuInflater.inflate(R.menu.logout_menuv1, menu);
            Drawable icon_color_change = menu.getItem(0).getIcon(); // change 0 with 1,2 ...

            icon_color_change.mutate();
            icon_color_change.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_IN);

            MenuItem logout_button = menu.findItem(R.id.logout);
            logout_button.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    logout(false);
                    return false;
                }
            });

        } catch (Exception e) {
            reporterror(tag, e.toString());
            e.printStackTrace();
        }
        return super.onCreateOptionsMenu(menu);
    }

    public void logout(boolean fromallreadylogin) {
        try {
            if (editor == null) {
                preferences = PreferenceManager.getDefaultSharedPreferences(UserProfileActivity.this);
                editor = preferences.edit();
            }
            editor.putBoolean("isloginv1", false);
            editor.apply();
            long totalmillies = System.currentTimeMillis() - GlobalValues.currentmillies;
            long timetaken = GlobalValues.student.getTimetaken() + (totalmillies / 1000);
            GlobalValues.student.setTimetaken(timetaken);
            GlobalValues.student.setUseractive(0);
            GlobalValues.student.setLasttimetaken(totalmillies / 1000);
            GlobalValues.student.setIslogin(0);
            if (!fromallreadylogin) {
                if (InternetUtils.getInstance(getApplicationContext()).available()) {
                    if (GlobalValues.student != null) {
                        //   DatabaseReference databaseReference;
                        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(Constants.firebasedbname);
                        Map<String, Object> values = GlobalValues.student.toMap();

                        if(mobilenotblankandlengthten(GlobalValues.student.getMobile())){

                            addtofirebasedb(0,GlobalValues.student);

                        }

                    }
                }
            }

            Intent login = new Intent(UserProfileActivity.this, LoginV1Activity.class);
            login.putExtra("StudId", GlobalValues.student.getId());
            login.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(login);
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void addtofirebasedb(int flag, Student student) {
        try {
            if (InternetUtils.getInstance(getApplicationContext()).available()) {
                DatabaseReference databaseReference;
                databaseReference = FirebaseDatabase.getInstance().getReference(Constants.firebasedbname);
                Map<String, Object> values = student.toMap();
                Log.e("put222 ", "put " + values.get("useractive"));
                //  databaseReference.child(GlobalValues.student.getMobile()).setValue(values);

                if (flag == 0) {
                    /*Map<String, Object> childUpdates = new HashMap<>();

                    Log.e("Timetaken ","timetaken "+GlobalValues.student.getTimetaken());
                    childUpdates.put(  "useractive",1);
                    childUpdates.put(  "timetaken",GlobalValues.student.getTimetaken());
                    childUpdates.put(  "lasttimetaken",GlobalValues.student.getLasttimetaken());
                    childUpdates.put(  "lastvisiteddate",GlobalValues.student.getLastvisiteddate());
                    databaseReference.child(GlobalValues.student.getMobile()).updateChildren(childUpdates);*/

                    long totalmillies = System.currentTimeMillis() - GlobalValues.currentmillies;
                    long timetaken = GlobalValues.student.getTimetaken() + (totalmillies / 1000);
                    Map<String, Object> childUpdates = new HashMap<>();
                    childUpdates.put( "timetaken", timetaken);
                    childUpdates.put(  "useractive", 0);
                    childUpdates.put( "lasttimetaken", totalmillies / 1000);
                    childUpdates.put( "islogin", 0);
                    childUpdates.put("IEMIno", Constants.IEMIno);
                    childUpdates.put("Mobile", GlobalValues.student.getMobile());
                    databaseReference.child(GlobalValues.student.getMobile()).updateChildren(childUpdates);

                } else {
                    databaseReference.child(GlobalValues.student.getMobile()).setValue(values);
                    Log.e("update ", "insert ");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}