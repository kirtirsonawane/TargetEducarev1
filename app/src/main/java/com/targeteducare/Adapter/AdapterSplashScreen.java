package com.targeteducare.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.targeteducare.ActivitySplashScreen;
import com.targeteducare.Classes.SplashModel;
import com.targeteducare.Fonter;
import com.targeteducare.GlobalValues;
import com.targeteducare.R;

import java.util.ArrayList;

public class AdapterSplashScreen extends PagerAdapter {
    ArrayList<SplashModel> splashModels = new ArrayList<>();
    SplashModel splashModel = new SplashModel();
    Context context;
    LayoutInflater layoutInflater;

    String style = "<style type=\"text/css\">\n" +
            "@font-face {\n" +
            "    font-family: Symbol;\n" +
            "    src:url(\"file:///android_asset/fonts/symbol.ttf\"), url(\"file:///android_asset/fonts/symbol_webfont.woff\"), url(\"file:///android_asset/fonts/symbol_webfont.woff2\");\n" +
            "}\n" +
            "@font-face {\n" +
            "    font-family: Calibri;\n" +
            "    src: url(\"file:///android_asset/fonts/calibri.ttf\")\n" +
            "}@font-face {\n" +
            "    font-family: AkrutiDevPriya;\n" +
            "    src: url(\"file:///android_asset/fonts/akrutidevpriyanormal.ttf\")\n" +
            "}@font-face {\n" +
            "    font-family: Times New Roman,serif;\n" +
            "    src: url(\"file:///android_asset/fonts/timesnewroman.ttf\")\n" +
            "}\n" +
            "\n" +
            "</style>";

    public AdapterSplashScreen(ArrayList<SplashModel> splashModels, Context context) {
        this.splashModels = splashModels;
        this.context = context;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        if (splashModels != null) {
            return splashModels.size();
        }
        return 0;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        View view = layoutInflater.inflate(R.layout.splash_adapter, container, false);
        try {

            TextView textView = (TextView) view.findViewById(R.id.title_text_splash);
            final WebView webView = (WebView) view.findViewById(R.id.viewPagerItem_webview);
            RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.relative_layout);
            webView.getSettings().setJavaScriptEnabled(true);
            //  textView.setTextColor(Color.WHITE);
                    /*if (position%2==0){
                        relativeLayout.setBackgroundColor(Color.BLUE);
                    }
                    else {
                        relativeLayout.setBackgroundColor(Color.GREEN);
                    }*/
            String first = "\"";



            /* webView.loadData(splashModels.get(position).getDescription(),"text/html","uft-8");*/
            if (GlobalValues.langs.equalsIgnoreCase("mr")) {
                webView.loadDataWithBaseURL("file:///", style + splashModels.get(position).getDescription_Marathi(), "text/html", "utf-8", null);
                textView.setText("\" " + splashModels.get(position).getTitle_Marathi() + " \"");
            } else {
                webView.loadDataWithBaseURL("file:///", style + splashModels.get(position).getDescription(), "text/html", "utf-8", null);
                textView.setText("\" " + splashModels.get(position).getTitle() + " \"");
            }


            textView.setTypeface(Fonter.getTypefacebold(context));
            textView.setTextColor(Color.BLACK);
            /* Log.e("title of list","list11"+webView.getUrl());*/
            webView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    try {
                        int i = 0;


                        if (event.getAction() == MotionEvent.ACTION_SCROLL) {
                            try {
                                i = 0;
                                ((ActivitySplashScreen) context).stoptimer(i);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            return false;
                        } else if (event.getAction() == MotionEvent.ACTION_UP) {
                            try {
                                i = 0;
                                ((ActivitySplashScreen) context).stoptimer(i);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            return false;
                        } /*else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        try {
                            i = 1;
                            ((ActivitySplashScreen) context).stoptimer(i);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return true;
                    }*/ else {
                            try {
                                i = 1;
                                ((ActivitySplashScreen) context).stoptimer(i);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            return true;
                        }

                    }catch (Exception e)
                    {
                        e.printStackTrace();
                        return  false;
                    }
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
        container.addView(view);
        return view;

    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout) object);
    }


}
