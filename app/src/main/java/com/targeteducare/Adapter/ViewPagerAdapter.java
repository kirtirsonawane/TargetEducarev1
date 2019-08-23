package com.targeteducare.Adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.targeteducare.Activitycommon;
import com.targeteducare.AttemptedFragment;
import com.targeteducare.Classes.Exam;
import com.targeteducare.MissedFragment;
import com.targeteducare.NewFragment;
import com.targeteducare.R;

import java.io.Serializable;
import java.util.ArrayList;

public class ViewPagerAdapter extends FragmentStatePagerAdapter implements Serializable {
    Fragment fragment = new Fragment();
    Context context;
    ArrayList<Exam> modelDataSetnnew;
    ArrayList<Exam> modelDataSetattempted;
    ArrayList<Exam> modelDataSetmissed;
    int flag = 0;
    ArrayList<String> titles = new ArrayList<>();
String type="";
    public ViewPagerAdapter(Context context, FragmentManager manager, ArrayList<Exam> modelDataSetnnew, ArrayList<Exam> modelDataSetattempted, ArrayList<Exam> modelDataSetmissed,String type) {
        super(manager);

        try {
            this.context = context;
            titles.add(context.getResources().getString(R.string.tab_new));
            titles.add(context.getResources().getString(R.string.tab_missed));
            titles.add(context.getResources().getString(R.string.tab_attempted));

            this.modelDataSetnnew = modelDataSetnnew;
            this.modelDataSetattempted = modelDataSetattempted;
            this.modelDataSetmissed = modelDataSetmissed;
            this.type=type;
            this.flag = flag;
        } catch (Exception e) {
            ((Activitycommon) context).reporterror("ViewPagerAdapter", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                try {
                    fragment = NewFragment.newInstance(context, modelDataSetnnew, type);
                } catch (Exception e) {
                    ((Activitycommon) context).reporterror("ViewPagerAdapter", e.toString());
                    e.printStackTrace();
                }
                break;
            case 1:
                try {
                    Log.e("Got into ", "view pager adapter for missed");
                    fragment = MissedFragment.newInstance(context, modelDataSetmissed, type);
                } catch (Exception e) {
                    ((Activitycommon) context).reporterror("ViewPagerAdapter", e.toString());
                    e.printStackTrace();
                }
                //fragment = MissedFragment.newInstance(modelDataSet,"");
                break;
            case 2:
                try {
                    fragment = AttemptedFragment.newInstance(context, modelDataSetattempted, type);
                } catch (Exception e) {
                    ((Activitycommon) context).reporterror("ViewPagerAdapter", e.toString());
                    e.printStackTrace();
                }
                //fragment = AttemptedFragment.newInstance(modelDataSet,"");
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return titles.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }

}
