package com.targeteducare;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class SubjectSelectionActivity extends AppCompatActivity {

    Bundle bd;
    TextView tv_mainsubjname;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_selection);
        bd = getIntent().getExtras();

        tv_mainsubjname = findViewById(R.id.mainsubjname);

        tv_mainsubjname.setText(bd.getString("subjname"));

    }
}
