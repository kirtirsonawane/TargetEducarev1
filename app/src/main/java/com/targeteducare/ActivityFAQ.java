package com.targeteducare;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.targeteducare.Adapter.AdapterFAQ;
import com.targeteducare.Classes.Questions;
import java.util.ArrayList;

public class ActivityFAQ extends Activitycommon {
   /* String Questions[] = new String[] {"Unable to view questions?", "Unable to view options?", "Unable to view subscribed courses?", "Unable to view images?","Unable to change language?"};
    String Answers[] = new String[] {"please try checking internet connection or restart your app.",
            "please try checking internet connection or restart your app.",
            "please try checking internet connection or restart your app then also problem persists try with selecting courses a/gain.",
            "please try checking internet connection.",
            "please try checking internet connection or restart your app, if then also problem persist try selecting language by using setting boutton on home screen?"};*/

    Button button;
    RecyclerView listView;
    LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            screenshot_capture_permission();
            super.onCreate(savedInstanceState);
            loadLocale();
            setContentView(R.layout.activity_faq);
            try {
                setmaterialDesign();
                toolbar.setTitleMargin(25, 10, 10, 10);
                back();
                loadLocale();

                final Intent intent = getIntent();
                Bundle args = intent.getBundleExtra("BUNDLE");
                final ArrayList<Questions> questions = (ArrayList<Questions>) args.getSerializable("ARRAYLIST");
                final String Id = args.getString("Id");
                String name = args.getString("Text");
                setTitle(name);

                //GlobalValues.langs = getSharedPreferences("Settings", Activity.MODE_PRIVATE).getString("Current Language", "");
                listView = (RecyclerView) findViewById(R.id.recycle_faq);
                linearLayoutManager = new LinearLayoutManager(ActivityFAQ.this);
                listView.setLayoutManager(linearLayoutManager);
                AdapterFAQ adapter = new AdapterFAQ(ActivityFAQ.this, questions);
                listView.setAdapter(adapter);
                TextView questions_text = (TextView) findViewById(R.id.text_questions);
                questions_text.setText(getResources().getString(R.string.questions));
                TextView buttonAskmore = (TextView) findViewById(R.id.buttontextview);
                buttonAskmore.setText(getResources().getString(R.string.askmorefaq));
                buttonAskmore.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
                buttonAskmore.setTypeface(Fonter.getTypefacebold(ActivityFAQ.this));

                buttonAskmore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent1 = new Intent(ActivityFAQ.this, ActivityChatFaq.class);
                        intent1.putExtra("Id", Id);
                        startActivity(intent1);
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

