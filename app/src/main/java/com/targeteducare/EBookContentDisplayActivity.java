package com.targeteducare;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.targeteducare.Adapter.EBookVideoAdapter;
import com.targeteducare.Adapter.EbookPagerAdapter;
import com.targeteducare.Classes.EbookChapter;
import com.targeteducare.Classes.EbookContentDetails;
import com.targeteducare.Classes.EbookPageDetails;
import com.targeteducare.Classes.EbookVideoDetails;

import java.util.ArrayList;

public class EBookContentDisplayActivity extends Activitycommon {

    ViewPager pager;
    ArrayList<EbookPageDetails> ebookPageDetails = new ArrayList<>();
    ArrayList<EbookVideoDetails> ebookVideoDetails = new ArrayList<>();
    ArrayList<EbookContentDetails> ebookContentDetails = new ArrayList<>();
    EbookPagerAdapter ebookPagerAdapter;
    EBookVideoAdapter eBookVideoAdapter;
    Bundle b;
    LinearLayout linearlayout_pdf, linearlayout_content, linearlayout_video;
    TextView tv_chaptername, tv_pageno;
    //ImageView iv_fav, iv_bookmark;
    Button first, prev, next, last;
    RecyclerView recyclerview_video;
    RecyclerView.LayoutManager layoutManager;
    String type_ebook = "";
    LinearLayout layout_contentdisplay;
    int total_pages = 0;
    SeekBar seekbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        try {
            super.onCreate(savedInstanceState);
            loadLocale();
            screenshot_capture_permission();
            setContentView(R.layout.activity_ebook_content_display);

            tag = this.getClass().getSimpleName();
            pager = (ViewPager) findViewById(R.id.pager);
            linearlayout_pdf = findViewById(R.id.linearlayout_pdf);
            linearlayout_content = findViewById(R.id.linearlayout_content);
            linearlayout_video = findViewById(R.id.linearlayout_video);
            tv_chaptername = findViewById(R.id.tv_chaptername);
            layout_contentdisplay = findViewById(R.id.layout_contentdisplay);
            seekbar = findViewById(R.id.seekbar);

            tv_pageno = findViewById(R.id.tv_pageno);
            /*iv_fav = findViewById(R.id.iv_fav);
            iv_bookmark = findViewById(R.id.iv_bookmark);*/

            /*first = findViewById(R.id.first);
            prev = findViewById(R.id.prev);
            next = findViewById(R.id.next);
            last = findViewById(R.id.last);*/
            recyclerview_video = findViewById(R.id.recyclerview_video);

            //LocalBroadcastManager.getInstance(EBookContentDisplayActivity.this).registerReceiver(recforfav, new IntentFilter("Favorite"));
            //LocalBroadcastManager.getInstance(EBookContentDisplayActivity.this).registerReceiver(recforbookmark, new IntentFilter("Bookmark"));
            b = getIntent().getExtras();


            if(b!=null){

                if(b.containsKey("typebook")){
                    type_ebook = b.getString("typebook");
                }
                if(b.containsKey("ebookcontent")){

                    ebookContentDetails = (ArrayList<EbookContentDetails>) b.getSerializable("ebookcontent");


                }
            }

            if(type_ebook.equalsIgnoreCase("Ebook")){

                try {
                    linearlayout_content.setVisibility(View.VISIBLE);
                    linearlayout_pdf.setVisibility(View.GONE);
                    linearlayout_video.setVisibility(View.GONE);

                    Log.e("ebookcontent size ", String.valueOf(ebookContentDetails.size()));
                    for(int i = 0; i<ebookContentDetails.size(); i++){
                        tv_chaptername.setText(ebookContentDetails.get(i).getChapterName());
                        ebookPageDetails = ebookContentDetails.get(i).getEbookPageDetails();
                        total_pages = ebookContentDetails.get(i).getNoofpages();
                    }


                    FadeOutTransformation fadeOutTransformation = new FadeOutTransformation();
                    HorizontalFlipTransformation horizontalFlipTransformation = new HorizontalFlipTransformation();
                    VerticalFlipTransformation verticalFlipTransformation = new VerticalFlipTransformation();

                    ebookPagerAdapter = new EbookPagerAdapter(getSupportFragmentManager(), ebookPageDetails);
                    pager.setAdapter(ebookPagerAdapter);

                    /*linearlayout_content.setVisibility(View.VISIBLE);
                    linearlayout_pdf.setVisibility(View.GONE);
                    linearlayout_video.setVisibility(View.GONE);
                    ebookPagerAdapter = new EbookPagerAdapter(getSupportFragmentManager(), ebookPageDetails);
                    pager.setAdapter(ebookPagerAdapter);*/


                    /*linearlayout_content.setVisibility(View.GONE);
                    linearlayout_pdf.setVisibility(View.VISIBLE);
                    linearlayout_video.setVisibility(View.GONE);*/

                    /*first.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            try {
                                if (ebookPageDetails.size() > 1) {
                                    pager.setCurrentItem(0);
                                    prev.setEnabled(false);
                                    next.setEnabled(true);
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });

                    prev.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            try {
                                int currentpgid = pager.getCurrentItem() - 1;
                                if (currentpgid == 0) {
                                    prev.setEnabled(false);
                                    next.setEnabled(true);
                                }

                                if ((currentpgid) > 0 || (currentpgid) < ebookPageDetails.size()) {
                                    prev.setEnabled(true);
                                    pager.setCurrentItem((currentpgid));
                                }

                                if (currentpgid < ((ebookPageDetails.size() - 1))) {
                                    next.setEnabled(true);
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });

                    next.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            try {
                                int currentpgid = pager.getCurrentItem() + 1;
                                if ((currentpgid) < ebookPageDetails.size()) {
                                    pager.setCurrentItem((currentpgid));
                                }
                                if (currentpgid == (ebookPageDetails.size() - 1)) {
                                    next.setEnabled(false);
                                }
                                if (currentpgid > 0) {
                                    prev.setEnabled(true);
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });

                    last.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            try {
                                if (ebookPageDetails.size() > 1) {
                                    int id = (ebookPageDetails.size() - 1);
                                    pager.setCurrentItem(id);
                                    next.setEnabled(false);
                                    prev.setEnabled(true);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });*/

                    seekbar.setMax(total_pages);


                    seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                        @Override
                        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                            Log.e("prog ", String.valueOf(progress));
                            //pager.setCurrentItem(progress);
                        }

                        @Override
                        public void onStartTrackingTouch(SeekBar seekBar) {

                        }

                        @Override
                        public void onStopTrackingTouch(SeekBar seekBar) {

                        }
                    });

                    pager.setPageTransformer(true, verticalFlipTransformation);

                    //pager.setPageTransformer(true, fadeOutTransformation);
                    pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                        @Override
                        public void onPageScrolled(int i, float v, int i1) {
                            tv_pageno.setText((i+1)+"/"+total_pages);
                            seekbar.setProgress(i+1);
                        }

                        @Override
                        public void onPageSelected(int i) {

                        }

                        @Override
                        public void onPageScrollStateChanged(int i) {

                        }
                    });

                } catch (Exception e) {
                    //reporterror(tag, e.getMessage());
                    e.printStackTrace();
                }

            }else if(type_ebook.equalsIgnoreCase("Video")){

                setmaterialDesign();
                back();
                linearlayout_content.setVisibility(View.GONE);
                linearlayout_pdf.setVisibility(View.GONE);
                linearlayout_video.setVisibility(View.VISIBLE);

                for(int i = 0; i<ebookContentDetails.size(); i++){

                    ebookVideoDetails = ebookContentDetails.get(i).getEbookVideoDetails();

                }

                layoutManager = new LinearLayoutManager(EBookContentDisplayActivity.this);
                recyclerview_video.setLayoutManager(layoutManager);
                eBookVideoAdapter = new EBookVideoAdapter(EBookContentDisplayActivity.this, ebookVideoDetails, lang);
                recyclerview_video.setAdapter(eBookVideoAdapter);
            }


        } catch (Exception e) {
            //reporterror(tag, e.getMessage());
            e.printStackTrace();
        }
    }

    public void gotoebookfragment(EbookVideoDetails video) {

        Intent i = new Intent(EBookContentDisplayActivity.this, EbookVideoPlayerActivity.class);
        i.putExtra("videodetails", video);
        startActivity(i);

    }

    public class FadeOutTransformation implements ViewPager.PageTransformer{
        @Override
        public void transformPage(View page, float position) {

            page.setTranslationX(-position*page.getWidth());

            page.setAlpha(1-Math.abs(position));


        }
    }

    public class HorizontalFlipTransformation implements ViewPager.PageTransformer {
        @Override
        public void transformPage(View page, float position) {

            page.setTranslationX(-position*page.getWidth());
            page.setCameraDistance(20000);

            if (position < 0.5 && position > -0.5){
                page.setVisibility(View.VISIBLE);
            }
            else {
                page.setVisibility(View.INVISIBLE);
            }



            if (position < -1){     // [-Infinity,-1)
                // This page is way off-screen to the left.
                page.setAlpha(0);

            }
            else if (position <= 0 ){    // [-1,0]
                page.setAlpha(1);
                page.setRotationX(180*(1-Math.abs(position)+1));
                Log.e("HORIZONTAL", "position <= 0     " + (180 * (1 - Math.abs(position) + 1)));

            }
            else if (position <= 1){    // (0,1]
                page.setAlpha(1);
                page.setRotationX(-180*(1-Math.abs(position)+1));
                Log.e("HORIZONTAL", "position <= 1     " + (-180 * (1 - Math.abs(position) + 1)));

            }
            else {    // (1,+Infinity]
                // This page is way off-screen to the right.
                page.setAlpha(0);

            }


        }
    }

    public class VerticalFlipTransformation implements ViewPager.PageTransformer {
        @Override
        public void transformPage(View page, float position) {

            page.setTranslationX(-position * page.getWidth());
            page.setCameraDistance(12000);

            if (position < 0.5 && position > -0.5) {
                page.setVisibility(View.VISIBLE);
            } else {
                page.setVisibility(View.INVISIBLE);
            }



            if (position < -1){     // [-Infinity,-1)
                // This page is way off-screen to the left.
                page.setAlpha(0);

            }
            else if (position <= 0) {    // [-1,0]
                page.setAlpha(1);
                page.setRotationY(180 *(1-Math.abs(position)+1));

            }
            else if (position <= 1) {    // (0,1]
                page.setAlpha(1);
                page.setRotationY(-180 *(1-Math.abs(position)+1));

            }
            else {    // (1,+Infinity]
                // This page is way off-screen to the right.
                page.setAlpha(0);

            }


        }
    }

    public void updateforfavorite(EbookPageDetails ebookPageDetails) {
        try {
            Intent intent = new Intent("FavQuestionfromFragment");
            intent.putExtra("Favorite", ebookPageDetails.getPageId());
            LocalBroadcastManager.getInstance(EBookContentDisplayActivity.this).sendBroadcast(intent);
        } catch (Exception e) {
            //reporterror(tag, e.toString());
            e.printStackTrace();
        }
    }

    public void updateforbookmark(EbookPageDetails ebookPageDetails) {
        try {
            Intent intent = new Intent("BookmarkQuestionfromFragment");
            intent.putExtra("Bookmark", ebookPageDetails.getPageId());
            LocalBroadcastManager.getInstance(EBookContentDisplayActivity.this).sendBroadcast(intent);
        } catch (Exception e) {
            //reporterror(tag, e.toString());
            e.printStackTrace();
        }
    }

    /*BroadcastReceiver recforfav = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try{

                if(ebookPageDetails.get(pager.getCurrentItem()).getPageId() == intent.getIntExtra("Favorite", 0)){

                }


            }catch (Exception e){
                e.printStackTrace();
            }
        }
    };

    BroadcastReceiver recforbookmark = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try{
                try{
                    if(ebookPageDetails.get(pager.getCurrentItem()).getPageId() == intent.getIntExtra("Bookmark", 0)){

                        if(ebookPageDetails.get(pager.getCurrentItem()).isBookmark()){

                        }

                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    };*/

}
