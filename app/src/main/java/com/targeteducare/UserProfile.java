package com.targeteducare;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.targeteducare.Adapter.UserProfAdapter;
import com.targeteducare.Classes.ArrUserProfData;
import com.targeteducare.Classes.StudentProfile;
import com.targeteducare.Classes.UserProfModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class UserProfile extends Activitycommon {

    private static ArrayList<UserProfModel> userProfModels;
    private UserProfAdapter adapter;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    TextView tv_username;
    ImageView profile_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        setmaterialDesign();
        back();

        tv_username = findViewById(R.id.nameuserprof);
        profile_image = findViewById(R.id.iconuserprof);

                preferences = PreferenceManager.getDefaultSharedPreferences(UserProfile.this);
        editor = preferences.edit();

        Gson gson=new Gson();
        Type type = new TypeToken<StudentProfile>(){}.getType();
        GlobalValues.studentProfile=gson.fromJson(preferences.getString("studentprofiledetails",""),type);

        preferences.getString("studentprofiledetails","");

        tv_username.setText(GlobalValues.studentProfile.getFullname());

        RecyclerView recyclerView = findViewById(R.id.recyclerviewforusersettings);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),3);
        recyclerView.setLayoutManager(gridLayoutManager);

        userProfModels = new ArrayList<UserProfModel>();
        for(int i=0;i<ArrUserProfData.userprofsettingsdata.length;i++){
            UserProfModel data = new UserProfModel(ArrUserProfData.userprofsettingsdata[i],ArrUserProfData.userprofsettings_image[i]);
            userProfModels.add(data);
        }
        adapter = new UserProfAdapter(UserProfile.this,userProfModels);
        recyclerView.setAdapter(adapter);

    }

    public void referto(int position) {
        switch (position){
            case 0:
                Intent ieditprof = new Intent(UserProfile.this, EditProfileActivity.class);
                startActivity(ieditprof);
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                break;
            case 6:
                break;
            case 7:
                break;
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        tv_username.setText(GlobalValues.studentProfile.getFullname());
    }
}