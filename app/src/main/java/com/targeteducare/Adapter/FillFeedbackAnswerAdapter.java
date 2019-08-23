package com.targeteducare.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.targeteducare.Activitycommon;
import com.targeteducare.Classes.EventModel;
import com.targeteducare.Classes.FeedbackAnswers;
import com.targeteducare.Classes.FeedbackQuestions;
import com.targeteducare.EventFeedbackActivity;
import com.targeteducare.FillFeedbackActivity;
import com.targeteducare.Fonter;
import com.targeteducare.R;

import java.lang.reflect.Type;
import java.util.ArrayList;


public class FillFeedbackAnswerAdapter extends RecyclerView.Adapter<FillFeedbackAnswerAdapter.MyViewHolder> {

    Context context;
    ArrayList<FeedbackAnswers> feedbackAnswers = new ArrayList<>();
    FeedbackQuestions feedbackQuestion;
    int pos = 0;
    int selectedCount = 0;
    String lang = "";

    public FillFeedbackAnswerAdapter(Context context, FeedbackQuestions feedbackQuestion, String lang) {

        try {
            this.context = context;
            this.feedbackQuestion = feedbackQuestion;
            this.feedbackAnswers = feedbackQuestion.getFeedbackAnswers();
            this.lang = lang;
        } catch (Exception e) {
            ((Activitycommon) context).reporterror("FillFeedbackAnswerAdapter", e.toString());
            e.printStackTrace();
        }

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(context).inflate(R.layout.feedback_answer_layout, viewGroup, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);



        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {


        try {

            myViewHolder.roundtext.setText(feedbackAnswers.get(i).getAns());

            if (lang.equalsIgnoreCase("mr")) {
                myViewHolder.feedback_answer.setText(Html.fromHtml(feedbackAnswers.get(i).getAnswer_Marathi()));
            } else {
                myViewHolder.feedback_answer.setText(Html.fromHtml(feedbackAnswers.get(i).getAnswer()));
            }

            if(feedbackAnswers.get(i).isSelected()){
                myViewHolder.linearlayout_answer.setBackgroundResource(R.drawable.feedbackanswerlayout_onclick);
            }else{
                myViewHolder.linearlayout_answer.setBackgroundResource(R.drawable.feedbackanswerlayout);
            }


            myViewHolder.feedback_answer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (feedbackQuestion.getType() == 1) {

                        for(int i = 0; i<feedbackAnswers.size(); i++){

                            feedbackAnswers.get(i).setSelected(false);
                            feedbackQuestion.setAnswered(false);

                        }

                        feedbackAnswers.get(i).setSelected(!myViewHolder.feedback_answer.isSelected());
                        feedbackQuestion.setAnswered(feedbackAnswers.get(i).isSelected());
                        myViewHolder.linearlayout_answer.setBackgroundResource(R.drawable.feedbackanswerlayout_onclick);

                    }

                    else {

                        feedbackAnswers.get(i).setSelected(!myViewHolder.feedback_answer.isSelected());
                        myViewHolder.feedback_answer.setSelected(!myViewHolder.feedback_answer.isSelected());
                        myViewHolder.linearlayout_answer.setBackgroundResource(R.drawable.feedbackanswerlayout_onclick);

                        for(int k=0; k<feedbackAnswers.size(); k++){

                            if(feedbackAnswers.get(i).isSelected()){
                                feedbackQuestion.setAnswered(true);
                            }

                        }

                    }

                    notifyDataSetChanged();
                }
            });



        } catch (Exception e) {
            ((Activitycommon) context).reporterror("FillFeedbackAnswerAdapter", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return feedbackAnswers.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView feedback_answer, roundtext;
        LinearLayout linearlayout_answer;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            feedback_answer = itemView.findViewById(R.id.feedback_answer);
            roundtext = itemView.findViewById(R.id.roundtext);
            linearlayout_answer = itemView.findViewById(R.id.linearlayout_answer);

        }
    }
}
