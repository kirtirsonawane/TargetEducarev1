package com.targeteducare;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.squareup.picasso.Picasso;

public class ActivityEnlargeImage extends Activitycommon {
    ImageView imageView;
    private ProgressDialog progressBar;
    private ProgressBar progressBar1;
    private int progressStatus = 0;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            screenshot_capture_permission();
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_enlarge_image);
            setmaterialDesign();
            toolbar.setTitleMargin(25, 10, 10, 10);
            setTitle("PEAK");
            back();
            genloading1("loading...");

    /*progressBar=new ProgressDialog(ActivityEnlargeImage.this);
     progressBar1=(ProgressBar)findViewById(R.id.progressBar);*/
            try {
                new Thread(new Runnable() {
                    public void run() {
                /*while (progressStatus < 100) {
                    progressStatus += 1;*/
                        // Update the progress bar and display the
                        //current value in the text view
                        try {
                            // Sleep for 200 milliseconds.
                            Thread.sleep(3000);
                            handler.post(new Runnable() {
                                public void run() {
                                    onResponsed1(1, 1, "1");
                                }
                            });
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        // }
                    }
                }).start();

                Intent intent = getIntent();
                String url = intent.getStringExtra("url");


                imageView = (ImageView) findViewById(R.id.enlargedimage);
                Picasso.with(ActivityEnlargeImage.this)
                        .load(url)
                        .placeholder(R.drawable.college)
                        .error(R.drawable.college)
                        //.networkPolicy(NetworkPolicy.NO_CACHE)
                        .skipMemoryCache()
                        .fit()

                        //.fit()
                        //   .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                        .into(imageView);

            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void genloading1(String msg) {
        try {
            if (!((Activity) this).isFinishing()) {
                dialog = ProgressDialog.show(this, msg, getResources().getString(R.string.dialog_please_wait));
                dialog.setCancelable(false);
            } else {

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dismissLoading() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    protected void onResponsed1(int statuscode, int accesscode, String data) {
        try {
            if (dialog != null) {
                dialog.dismiss();
            }
            dismissLoading();
           /* if (statuscode == Constants.STATUS_OK) {
                if (accesscode == 1610) {

                } else if (accesscode == 1016) {

                } else if (accesscode == 1994) {

                }
            }*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
