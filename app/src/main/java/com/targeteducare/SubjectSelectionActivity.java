package com.targeteducare;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.TextView;

public class SubjectSelectionActivity extends Activitycommon {

    Bundle bd;
    TextView tv_mainsubjname;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        try {
            screenshot_capture_permission();
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_subject_selection);
            bd = getIntent().getExtras();
            tag = this.getClass().getSimpleName();
            tv_mainsubjname = findViewById(R.id.mainsubjname);

            tv_mainsubjname.setText(bd.getString("subjname"));

        } catch (Exception e) {
            reporterror(tag, e.toString());
            e.printStackTrace();
        }

    }
}
