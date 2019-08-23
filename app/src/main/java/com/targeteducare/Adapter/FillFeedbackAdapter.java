package com.targeteducare.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.targeteducare.Activitycommon;
import com.targeteducare.Classes.EventModel;
import com.targeteducare.Classes.FeedbackAnswers;
import com.targeteducare.Classes.FeedbackQuestions;
import com.targeteducare.EventFeedbackActivity;
import com.targeteducare.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class FillFeedbackAdapter extends RecyclerView.Adapter<FillFeedbackAdapter.MyViewHolder> {

    Context context;
    ArrayList<FeedbackQuestions> feedbackQuestions = new ArrayList<>();
    ArrayList<FeedbackAnswers> feedbackAnswers;
    String lang = "";

    public FillFeedbackAdapter(Context context, ArrayList<FeedbackQuestions> feedbackQuestions, String lang) {

        try {
            this.context = context;
            this.feedbackQuestions = feedbackQuestions;
            this.lang = lang;
        } catch (Exception e) {
            ((Activitycommon) context).reporterror("FillFeedbackAdapter", e.toString());
            e.printStackTrace();
        }

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(context).inflate(R.layout.feedback_question_layout, viewGroup, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        try {
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);

            myViewHolder.srno_question.setText((i + 1) + ". ");


            if (lang.equalsIgnoreCase("mr")) {
                myViewHolder.feedback_question.setText(Html.fromHtml(feedbackQuestions.get(i).getQuestion_Marathi()));
            } else {
                myViewHolder.feedback_question.setText(Html.fromHtml(feedbackQuestions.get(i).getQuestion()));
            }


            myViewHolder.recycler_view_foranswer.setLayoutManager(layoutManager);

            feedbackAnswers = new ArrayList<>();
            FillFeedbackAnswerAdapter adapter = new FillFeedbackAnswerAdapter(context, feedbackQuestions.get(i), lang);
            myViewHolder.recycler_view_foranswer.setAdapter(adapter);
            adapter.notifyDataSetChanged();


        } catch (Exception e) {
            ((Activitycommon) context).reporterror("FillFeedbackAdapter", e.toString());
            e.printStackTrace();
        }


    }

    @Override
    public int getItemCount() {
        return feedbackQuestions.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView feedback_question, srno_question;
        RecyclerView recycler_view_foranswer;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            feedback_question = itemView.findViewById(R.id.feedback_question);
            srno_question = itemView.findViewById(R.id.srno_question);
            recycler_view_foranswer = itemView.findViewById(R.id.recycler_view_foranswer);

        }
    }
}
