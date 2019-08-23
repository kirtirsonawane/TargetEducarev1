package com.targeteducare.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.targeteducare.Activitycommon;
import com.targeteducare.Classes.QnAData;
import com.targeteducare.Classes.QnaDataModel;
import com.targeteducare.Classes.QnaQuestionModel;
import com.targeteducare.R;

import java.util.ArrayList;

public class QnaQuestionAdapter extends RecyclerView.Adapter<QnaQuestionAdapter.MyViewHolder> implements Filterable {
    private ArrayList<QnaDataModel> data;
    private ArrayList<QnaQuestionModel> dataSet;
    Context context;

    public static String[] question_search;
    public static String[] course_name_search;
    public static String[] branch_search;


    public QnaQuestionAdapter(Context context, ArrayList<QnaQuestionModel> data) {

        try {
            this.dataSet = data;
            this.context = context;
        } catch (Exception e) {
            ((Activitycommon) context).reporterror("QnaQuestionAdapter", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_details, parent, false);

        //view.setOnClickListener(EngineeringCollegesActivity.myOnClickListener);

        MyViewHolder vh = new MyViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int i) {


        try {
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
            ;
            holder.main_question.setText(dataSet.get(i).getMain_question());
            holder.followers.setText(dataSet.get(i).getFollowers() + " " + context.getResources().getString(R.string.qna_followers));
            holder.answers.setText(dataSet.get(i).getAnswers() + " " + context.getResources().getString(R.string.qna_answers));
            holder.recyclerView.setLayoutManager(layoutManager);

            data = new ArrayList<QnaDataModel>();
            for (int j = 0; j < QnAData.profile_pics.length; j++) {
                data.add(new QnaDataModel(
                        QnAData.profile_pics[j], QnAData.name[j], QnAData.time[j], QnAData.paragraphs[j]
                ));
            }
            QnAdapterRecyclerView adapter = new QnAdapterRecyclerView(context, data);
            holder.recyclerView.setAdapter(adapter);

        } catch (Exception e) {
            ((Activitycommon) context).reporterror("QnaQuestionAdapter", e.toString());
            e.printStackTrace();
        }

    }


    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    @Override
    public Filter getFilter() {
        return null;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView main_question;
        TextView followers;
        TextView answers;
        RecyclerView recyclerView;
        CheckBox cb_engineeringcollege, cb_course, cb_branch;

        public MyViewHolder(View itemView) {
            super(itemView);
            main_question = itemView.findViewById(R.id.mainquestion);
            followers = itemView.findViewById(R.id.followers);
            answers = itemView.findViewById(R.id.answers);
            recyclerView = itemView.findViewById(R.id.recyclerviewfollowers);
            recyclerView.setHasFixedSize(true);
            /*cb_engineeringcollege = itemView.findViewById(R.id.question);
            cb_course = itemView.findViewById(R.id.course);*/
            cb_branch = itemView.findViewById(R.id.branch);
        }
    }

}
