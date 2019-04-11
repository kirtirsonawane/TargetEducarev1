package com.targeteducare;

import android.os.Bundle;

public class PracticeTestActivity extends Activitycommon {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice_test);

        setmaterialDesign();
        back();
    }
}
