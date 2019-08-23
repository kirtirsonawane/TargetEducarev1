package com.targeteducare;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class ActivitySelectNAddText extends Activitycommon {
    ImageView imageView, send;
    EditText textView;
    TextView textView1;
    private ProgressBar progressBar;
    private int progressStatus = 0;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            screenshot_capture_permission();
            setContentView(R.layout.activity_select_nadd_text);
            setmaterialDesign();
            toolbar.setTitleMargin(25, 10, 10, 10);
            setTitle("PEAK");
            back();
            genloading1("loading...");
            textView1 = (TextView) findViewById(R.id.textView);
            progressBar = (ProgressBar) findViewById(R.id.progressBar_cyclic);
            try {
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
                    final String urls = intent.getStringExtra("URL");
                    back();

                    textView = (EditText) findViewById(R.id.addtext);
                    imageView = (ImageView) findViewById(R.id.display);
                    send = (ImageView) findViewById(R.id.send_img);
                    Picasso.with(ActivitySelectNAddText.this)
                            .load(urls)
                            .placeholder(R.drawable.college)
                            .error(R.drawable.college)
                            .networkPolicy(NetworkPolicy.NO_CACHE)
                            .skipMemoryCache()
                            .fit()
                            //.fit()
                            .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                            .into(imageView);

                    send.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (textView.getText().toString().equalsIgnoreCase("") || textView.getText().toString() == null || textView.getText().toString().length() < 1) {
                                Toast.makeText(ActivitySelectNAddText.this, "Please enter msg", Toast.LENGTH_SHORT).show();
                            } else {
                                Intent returnIntent = new Intent();
                                returnIntent.putExtra("url", urls);
                                returnIntent.putExtra("text", textView.getText().toString());
                                setResult(RESULT_OK, returnIntent);
                                finish();
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
