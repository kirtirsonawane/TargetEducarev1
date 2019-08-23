package com.targeteducare.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.targeteducare.Activitycommon;
import com.targeteducare.AnswerFragment;
import com.targeteducare.Classes.Question;
import com.targeteducare.QuestionFragment;

import java.util.ArrayList;

public class QuestionAdapter extends FragmentStatePagerAdapter {
    ArrayList<Question> items;
    boolean isanswersheet;

    public QuestionAdapter(FragmentManager fragmentManager, ArrayList<Question> items, boolean isanswersheet) {
        super(fragmentManager);

        try {
            this.items = items;
            this.isanswersheet = isanswersheet;
           // Log.e("item", "itemsize " + this.items.size());

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Fragment getItem(int position) {
        // switch (position) {

        if (isanswersheet)
            return AnswerFragment.newInstance(items.get(position), "data");
        else return QuestionFragment.newInstance(items.get(position), "data");
        //  }
    }
}

