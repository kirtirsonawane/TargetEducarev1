package com.targeteducare;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class SyllabusActivity extends Activitycommon {
    WebView webView;
    SharedPreferences preferences;
    SharedPreferences.Editor edit;
    ProgressDialog dialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            screenshot_capture_permission();
            super.onCreate(savedInstanceState);
            loadLocale();
            setContentView(R.layout.activity_syllabus);
            tag = this.getClass().getSimpleName();

            setmaterialDesign();
            setTitle(getResources().getString(R.string.syllabus));
            back();

            preferences = PreferenceManager.getDefaultSharedPreferences(SyllabusActivity.this);
            edit = preferences.edit();


            webView = findViewById(R.id.webview_syllabus);

            genloading(getResources().getString(R.string.loading));

            webView.getSettings().setDomStorageEnabled(true);
            webView.getSettings().setLoadsImagesAutomatically(true);
            webView.getSettings().setJavaScriptEnabled(true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
                webView.getSettings().setAllowFileAccessFromFileURLs(true);
                webView.getSettings().getJavaScriptCanOpenWindowsAutomatically();
            }
            webView.getSettings().setDomStorageEnabled(true);
            webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

            webView.loadUrl(URLS.syllabus_url());

            webView.setWebViewClient(new WebViewClient() {

                public void onPageFinished(WebView view, String url) {

                    try {
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }

                        webView.loadUrl(
                                "javascript:(function() { " +
                                        "var element = document.getElementById('hplogo');"
                                        + "element.parentNode.removeChild(element);" +
                                        "})()");


                    } catch (Exception e) {
                        reporterror(tag, e.toString());
                        e.printStackTrace();
                    }

                }

            });

            webView.setWebChromeClient(new WebChromeClient());


        } catch (Exception e) {
            reporterror(tag, e.toString());
            e.printStackTrace();
        }
    }

    public void genloading(String msg) {
        try {
            if (!((Activity) this).isFinishing()) {
                dialog = ProgressDialog.show(this, msg, getResources().getString(R.string.dialog_please_wait));
                dialog.setCancelable(false);
            } else {
                Log.e("activity", "activity is not running genloading");
            }


        } catch (Exception e) {
            reporterror(tag, e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {


        try {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                switch (keyCode) {
                    case KeyEvent.KEYCODE_BACK:
                        if (webView.canGoBack()) {
                            webView.goBack();
                        } else {
                            finish();
                        }
                        return true;
                }

            }


        } catch (Exception e) {
            reporterror(tag, e.toString());
            e.printStackTrace();
        }
        return super.onKeyDown(keyCode, event);
    }
}
