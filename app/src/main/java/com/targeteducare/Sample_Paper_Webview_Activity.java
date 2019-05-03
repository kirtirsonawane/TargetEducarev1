package com.targeteducare;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.TextView;

public class Sample_Paper_Webview_Activity extends Activitycommon {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample__paper__webview_);
        Intent intent=getIntent();


        String Tilte= intent.getStringExtra("Title");
        setmaterialDesign();
        back();
        setTitle(Tilte);
        WebView discription = (WebView) findViewById(R.id.sample_papers_web_view);

        discription.getSettings().setJavaScriptEnabled(true);
        discription.loadData(GlobalValues.WEB_VIEW, "text/html", "utf-8");
    }
}
