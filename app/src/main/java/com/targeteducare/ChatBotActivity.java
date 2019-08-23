package com.targeteducare;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class ChatBotActivity extends Activitycommon {
    WebView webView;
    SharedPreferences preferences;
    SharedPreferences.Editor edit;
    ProgressDialog dialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try{
            screenshot_capture_permission();
        super.onCreate(savedInstanceState);
        loadLocale();
        setContentView(R.layout.activity_chat_bot);
        setmaterialDesign();
        setTitle(getResources().getString(R.string.chat));
        back();

        preferences = PreferenceManager.getDefaultSharedPreferences(ChatBotActivity.this);
        edit = preferences.edit();

        webView = findViewById(R.id.webview_chatbot);

        genloading(getResources().getString(R.string.loading));


        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        webView.loadUrl(URLS.url_chatbot());

        webView.setWebViewClient(new WebViewClient(){

            public void onPageFinished(WebView view, String url) {
                try {
                    if (!((Activity) ChatBotActivity.this).isFinishing()) {
                        if (dialog != null)
                            if (dialog.isShowing()) {
                                dialog.dismiss();
                            }
                    }
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }

        });
        }catch (Exception e)
        {
            reporterror("Attemptedfrag",e.toString());
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
