package com.targeteducare;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.WindowManager;

import com.targeteducare.Adapter.QnaQuestionAdapter;
import com.targeteducare.Classes.QnAData;
import com.targeteducare.Classes.QnaDataModel;
import com.targeteducare.Classes.QnaQuestionData;
import com.targeteducare.Classes.QnaQuestionModel;

import java.util.ArrayList;

public class QnAFeedActivity extends Activitycommon {

    private ArrayList<QnaQuestionModel> dataQuestionmodel;
    private QnaQuestionAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        screenshot_capture_permission();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qn_afeed);
        setmaterialDesign();
        setTitle(getResources().getString(R.string.qna));
        back();
        recyclerView = findViewById(R.id.recyclerviewforquestion);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        dataQuestionmodel = new ArrayList<QnaQuestionModel>();
        for(int i = 0; i< QnaQuestionData.main_question.length; i++){

            QnaQuestionModel qdata = new QnaQuestionModel(
                    QnaQuestionData.main_question[i],QnaQuestionData.followers[i],QnaQuestionData.answers[i]);

            ArrayList<QnaDataModel> datamod = new ArrayList<>();

            for(int j = 0; j< QnAData.profile_pics.length; j++){

                QnaDataModel qdatamod = new QnaDataModel(
                        QnAData.profile_pics[j],QnAData.name[j],QnAData.time[j],QnAData.paragraphs[j]
                );
                datamod.add(qdatamod);
            }

            qdata.setQndataset(datamod);
            dataQuestionmodel.add(qdata);
        }
        adapter = new QnaQuestionAdapter(QnAFeedActivity.this,dataQuestionmodel);
        recyclerView.setAdapter(adapter);
    }
}
