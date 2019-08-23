package com.targeteducare.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.widget.Filter;
import android.widget.Filterable;

import com.targeteducare.AnswerPracticeFragment;
import com.targeteducare.Classes.Question;

import com.targeteducare.QuestionPracticeFragment;

import java.util.ArrayList;

public class PracticeAdapter extends FragmentStatePagerAdapter {
    ArrayList<Question> items;
    boolean isanswersheet;

    public PracticeAdapter(FragmentManager fragmentManager, ArrayList<Question> items, boolean isanswersheet) {
        super(fragmentManager);
        this.items = items;
        this.isanswersheet = isanswersheet;
        Log.e("item", "itemsize " + this.items.size());
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Fragment getItem(int position) {
        // switch (position) {

        if (isanswersheet){
            Log.e("I am in ", "PracticeAdapter");
            return AnswerPracticeFragment.newInstance(items.get(position), "data");
        }
        else return QuestionPracticeFragment.newInstance(items, items.get(position), "data");
        //  }
    }
}