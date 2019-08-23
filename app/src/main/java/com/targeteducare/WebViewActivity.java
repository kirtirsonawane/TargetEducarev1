package com.targeteducare;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewActivity extends Activitycommon {
    WebView webView;
    SharedPreferences preferences;
    SharedPreferences.Editor edit;
    ProgressDialog dialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        screenshot_capture_permission();
        super.onCreate(savedInstanceState);
        loadLocale();
        setContentView(R.layout.activity_web_view);
        try {
            webView = findViewById(R.id.webview);
            genloading(getResources().getString(R.string.loading));
            String url = getIntent().getStringExtra("url");
            Log.e("url ","url "+url);
            webView.getSettings().setDomStorageEnabled(true);
            webView.getSettings().setLoadsImagesAutomatically(true);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
            webView.loadUrl(url);
            webView.setWebViewClient(new WebViewClient() {
                public void onPageFinished(WebView view, String url) {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }

            });
        } catch (Exception e) {
            reporterror("Attemptedfrag", e.toString());
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
            e.printStackTrace();
        }
    }
}
