package com.targeteducare.Adapter;

import android.support.v4.app.FragmentStatePagerAdapter;

import com.targeteducare.Classes.EbookPageDetails;
import com.targeteducare.EbookPagesFragment;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

public class EbookPagerAdapter extends FragmentStatePagerAdapter {

    ArrayList<EbookPageDetails> ebookPageDetails;

    public EbookPagerAdapter(FragmentManager fragmentManager, ArrayList<EbookPageDetails> ebookPageDetails) {
        super(fragmentManager);
        this.ebookPageDetails = ebookPageDetails;

    }

    @Override
    public int getCount() {
        return ebookPageDetails.size();
    }

    @Override
    public Fragment getItem(int position) {
        return EbookPagesFragment.newInstance(ebookPageDetails.get(position), "data");
    }
}
