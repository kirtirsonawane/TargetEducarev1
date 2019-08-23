package com.targeteducare;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.targeteducare.Adapter.ImagePagerAdapter;
import com.targeteducare.Classes.QuestionURL;

import java.util.ArrayList;

public class Imageenlargeactivity extends Activitycommon {
    ArrayList<QuestionURL> data;
    ViewPager pager;
    ImagePagerAdapter adapter;
    Button next, prev;
    String tag = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            screenshot_capture_permission();
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_imageenlargeactivity);
            setmaterialDesign();
            back();
            tag = this.getClass().getSimpleName();

            Bundle b = getIntent().getExtras();
            data = (ArrayList<QuestionURL>) b.getSerializable("data");
            for (int i = 0; i < data.size(); i++) {
                Log.e("image url ", "imager url " + data.get(i).getImagemainsource());
            }
            setTitle("Q." + b.getInt("srno"));
            pager = (ViewPager) findViewById(R.id.pager);
            adapter = new ImagePagerAdapter(Imageenlargeactivity.this, data);
            pager.setAdapter(adapter);
            pager.setCurrentItem(0);
            pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    if (position == 0)
                        prev.setEnabled(false);
                    else prev.setEnabled(true);

                    if (position == (data.size() - 1))
                        next.setEnabled(false);
                    else next.setEnabled(true);
                }

                @Override
                public void onPageSelected(int position) {

                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });

            prev = (Button) findViewById(R.id.previous);
            next = (Button) findViewById(R.id.next);

            prev.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int currentpgid = pager.getCurrentItem() - 1;
                    if ((currentpgid) >= 0 || (currentpgid) < data.size()) {
                        pager.setCurrentItem((currentpgid));
                    }
                    if (currentpgid == 0) {
                        prev.setEnabled(false);
                        next.setEnabled(true);
                    }

                    if (currentpgid < ((data.size() - 1))) {
                        next.setEnabled(true);
                    }
                }
            });

            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int currentpgid = pager.getCurrentItem() + 1;
                    if ((currentpgid) < data.size()) {
                        pager.setCurrentItem((currentpgid));
                    }
                    if (currentpgid == (data.size() - 1)) {
                        next.setEnabled(false);
                    }

                    if (currentpgid == (data.size() - 1)) {
                        next.setEnabled(false);
                    }
                    if (currentpgid > 0) {
                        prev.setEnabled(true);
                    }
                }
            });

            if (data.size() == 1) {
                next.setVisibility(View.GONE);
                prev.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            reporterror(tag, e.toString());
            e.printStackTrace();
        }
    }
}
