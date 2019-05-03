package com.targeteducare.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.targeteducare.Activitycommon;
import com.targeteducare.Classes.Exam;

import com.targeteducare.MockTestActivity;
import com.targeteducare.PracticeTestActivity;

import com.targeteducare.R;


import java.util.ArrayList;

public class MockTestAdapter extends RecyclerView.Adapter<MockTestAdapter.MyViewHolder> {
    /*int max = 100;
    int p;*/

    Context context;
    ArrayList<Exam> practiceTestModels;

    public MockTestAdapter(Context context, ArrayList<Exam> practiceTestModels){
        this.context = context;
        this.practiceTestModels = practiceTestModels;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_exam,viewGroup,false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {

        myViewHolder.tv_chapterno.setVisibility(View.VISIBLE);

        int chap_no = i;
        if(chap_no>=0 && chap_no<10){
            myViewHolder.tv_chapterno.setText("0"+(chap_no+1));
        }
        else{
            myViewHolder.tv_chapterno.setText(""+(chap_no++));
        }

        myViewHolder.tv_topicname.setVisibility(View.VISIBLE);
        myViewHolder.tv_total_questions.setVisibility(View.VISIBLE);
        myViewHolder.tv_totalvideo.setVisibility(View.VISIBLE);
        myViewHolder.tv_totalconcepts.setVisibility(View.VISIBLE);
        myViewHolder.timemarks1.setVisibility(View.VISIBLE);

        //myViewHolder.lpracticetest.setVisibility(View.VISIBLE);
        //myViewHolder.lprogbar.setVisibility(View.VISIBLE);
        //myViewHolder.progressBar.setProgress((int) Math.round(practiceTestModels.get(i).getProgress()));
        //myViewHolder.lattemptexam.setVisibility(View.GONE);
        //myViewHolder.timemarks1.setVisibility(View.GONE);

        myViewHolder.imgview.setVisibility(View.GONE);
        myViewHolder.time2.setVisibility(View.GONE);

        //myViewHolder.tv_chapterno.setText(practiceTestModels.get(i).getTopic_no());


        myViewHolder.timemarks1.setText("Total marks for this exam");
        myViewHolder.tv_topicname.setText(practiceTestModels.get(i).getExamname());
        Log.e("name","name "+practiceTestModels.get(i).getExamname());
        myViewHolder.tv_total_questions.setText(practiceTestModels.get(i).getTotal_questions()+" questions");
        myViewHolder.tv_totalvideo.setText(practiceTestModels.get(i).getTotal_videos()+" videos");
        myViewHolder.tv_totalconcepts.setText(practiceTestModels.get(i).getTotal_concepts()+" concepts");


        //myViewHolder.tv_checkprogress.setVisibility(View.GONE);
        //myViewHolder.progressBar.setVisibility(View.VISIBLE);

        myViewHolder.lprogbar.setVisibility(View.VISIBLE);


        if (((int) Math.round(practiceTestModels.get(i).getProgress()) > 0) && ((int) Math.round(practiceTestModels.get(i).getProgress())<100)) {
            myViewHolder.tv_attempt.setText("Resume");
            myViewHolder.lprogbar.setVisibility(View.VISIBLE);
            myViewHolder.tv_checkprogress.setVisibility(View.VISIBLE);
            myViewHolder.progressBar.setVisibility(View.VISIBLE);
            myViewHolder.progressBar.setProgress((int) Math.round(practiceTestModels.get(i).getProgress()));
            int progress_percent = (int) Math.round(practiceTestModels.get(i).getProgress());
            myViewHolder.tv_percentcovered.setText(progress_percent+" % covered");
        }
        else if((int) Math.round(practiceTestModels.get(i).getProgress()) == 100){
            myViewHolder.tv_attempt.setText("Retry");
            myViewHolder.lprogbar.setVisibility(View.VISIBLE);
            myViewHolder.tv_checkprogress.setVisibility(View.VISIBLE);
            myViewHolder.progressBar.setVisibility(View.VISIBLE);
            myViewHolder.progressBar.setProgress((int) Math.round(practiceTestModels.get(i).getProgress()));
            int progress_percent = (int) Math.round(practiceTestModels.get(i).getProgress());
            myViewHolder.tv_percentcovered.setText(progress_percent+" % covered");
        }
        else {
            myViewHolder.tv_attempt.setText("Attempt");
            myViewHolder.lprogbar.setVisibility(View.GONE);
            myViewHolder.tv_checkprogress.setVisibility(View.GONE);
            myViewHolder.progressBar.setVisibility(View.GONE);
        }

        myViewHolder.tv_checkprogress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MockTestActivity)context).gotoAction(i);
            }
        });

        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             /*   ((PracticeTestActivity)context).gotoprogreport(i);
                myViewHolder.progressBar.setVisibility(View.VISIBLE);
                myViewHolder.lprogbar.setVisibility(View.VISIBLE);
                myViewHolder.tv_checkprogress.setVisibility(View.VISIBLE);

                myViewHolder.tv_checkprogress.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((PracticeTestActivity)context).gotoAction(i);
                    }
                });*/
                ((Activitycommon)context).gotoexamActivity(practiceTestModels.get(i));
            }
        });
    /*    if(practiceTestModels.get(i).getIsshowexam()==0 && !practiceTestModels.get(i).isIsexamgiven() )
        {
            try {
                Calendar cal1 = Calendar.getInstance();
                Date date2 = DateUtils.parseDate(practiceTestModels.get(i).getEnddate(), "dd MMM yyyy");
                Date date3 = DateUtils.parseDate(practiceTestModels.get(i).getExamendtime(), "HH:mm:ss");
                date2.setHours(date3.getHours());
                date2.setMinutes(date3.getMinutes());
                cal1.setTime(date2);

                Calendar cal = Calendar.getInstance();
                Date date = DateUtils.parseDate(practiceTestModels.get(i).getStartdate(), "dd MMM yyyy");
                Date date1 = DateUtils.parseDate(practiceTestModels.get(i).getExamstarttime(), "HH:mm:ss");
                date.setHours(date1.getHours());
                date.setMinutes(date1.getMinutes());
                cal.setTime(date);

                if (cal.before(Calendar.getInstance()) && cal1.after(Calendar.getInstance())) {
                    if(practiceTestModels.get(i).isIsqdownloaded()) {
                        myViewHolder.attempt.setVisibility(View.VISIBLE);
                        myViewHolder.download.setVisibility(View.GONE);
                    }else {
                        myViewHolder.attempt.setVisibility(View.GONE);
                        myViewHolder.download.setVisibility(View.VISIBLE);
                    }
                } else {
                    myViewHolder.attempt.setVisibility(View.GONE);
                    myViewHolder.download.setVisibility(View.GONE);
                }
            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }else {
            myViewHolder.attempt.setVisibility(View.GONE);
            myViewHolder.download.setVisibility(View.GONE);
        }*/
        /*myViewHolder.btn_startpractice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((PracticeTestActivity)context).incrementprogress(i);
                myViewHolder.progressBar.setMax(max);
                p = pt.incrementprogress(20);
                myViewHolder.progressBar.setProgress(p);
                myViewHolder.tv_percentcovered.setText(practiceTestModels.get(i).getCovered_percentage()+" %covered");
            }
        });

        myViewHolder.btn_checkprogress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((PracticeTestActivity)context).gotopracticetest(i);
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return practiceTestModels.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tv_chapterno, tv_topicname, tv_totalvideo, tv_total_questions, tv_totalconcepts, tv_percentcovered;
        //Button btn_startpractice, btn_checkprogress;
        ProgressBar progressBar;
        TextView tv_checkprogress;
        TextView tv_attempt;

        ImageView imgview;
        TextView timemarks1, time2;

        LinearLayout lpracticetest;
        LinearLayout lattemptexam;
        LinearLayout lprogbar;



        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_chapterno = itemView.findViewById(R.id.chapternumber);
            tv_topicname = itemView.findViewById(R.id.textview_1);
            tv_totalvideo = itemView.findViewById(R.id.totalvideos);
            tv_total_questions = itemView.findViewById(R.id.totalquestions);
            tv_totalconcepts = itemView.findViewById(R.id.totalconcepts);
            tv_percentcovered = itemView.findViewById(R.id.tv_coveredpercentage);
            tv_checkprogress = itemView.findViewById(R.id.tv_checkprogress);
            //download = (TextView) itemView.findViewById(R.id.download);
            tv_attempt = (TextView) itemView.findViewById(R.id.attempt);
            //btn_startpractice = itemView.findViewById(R.id.button_startpractice);
            //btn_checkprogress = itemView.findViewById(R.id.button_checkprogress);
            progressBar = itemView.findViewById(R.id.progress_topiccovered);

            imgview = itemView.findViewById(R.id.imageview_1);
            timemarks1 = itemView.findViewById(R.id.textview_2);
            time2 = itemView.findViewById(R.id.textview_3);

            lattemptexam = itemView.findViewById(R.id.linearlayouttoattemptexam);
            lpracticetest = itemView.findViewById(R.id.linearlayoutforpracticetest);
            lprogbar = itemView.findViewById(R.id.linearlayoutprogresbar);

        }
    }
}
