package com.targeteducare.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.targeteducare.Classes.PracticeTestModel;
import com.targeteducare.PracticeTestActivity;
import com.targeteducare.R;

import java.util.ArrayList;

public class PracticeTestAdapter extends RecyclerView.Adapter<PracticeTestAdapter.MyViewHolder> {
    PracticeTestActivity pt = new PracticeTestActivity();
    int max = 100;
    int p;

    Context context;
    ArrayList<PracticeTestModel> practiceTestModels;

    public PracticeTestAdapter(Context context, ArrayList<PracticeTestModel> practiceTestModels){
        this.context = context;
        this.practiceTestModels = practiceTestModels;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.practice_or_progress,viewGroup,false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {


        myViewHolder.tv_chapterno.setText(practiceTestModels.get(i).getTopic_no());
        myViewHolder.tv_topicname.setText(practiceTestModels.get(i).getTopic_name_select());
        myViewHolder.tv_totalvideo.setText(practiceTestModels.get(i).getTotal_videos()+" videos");
        myViewHolder.tv_total_goals.setText(practiceTestModels.get(i).getTotal_goals()+" goals");
        myViewHolder.tv_totalconcepts.setText(practiceTestModels.get(i).getTotal_concepts()+" concepts");


        myViewHolder.btn_startpractice.setOnClickListener(new View.OnClickListener() {
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
        });
    }

    @Override
    public int getItemCount() {
        return practiceTestModels.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tv_chapterno, tv_topicname, tv_totalvideo, tv_total_goals, tv_totalconcepts, tv_percentcovered;
        Button btn_startpractice, btn_checkprogress;
        ProgressBar progressBar;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_chapterno = itemView.findViewById(R.id.chapternumber);
            tv_topicname = itemView.findViewById(R.id.nameoftopic);
            tv_totalvideo = itemView.findViewById(R.id.totalvideos);
            tv_total_goals = itemView.findViewById(R.id.totalgoals);
            tv_totalconcepts = itemView.findViewById(R.id.totalconcepts);
            tv_percentcovered = itemView.findViewById(R.id.coveredpercentage);
            btn_startpractice = itemView.findViewById(R.id.button_startpractice);
            btn_checkprogress = itemView.findViewById(R.id.button_checkprogress);
            progressBar = itemView.findViewById(R.id.progress_topiccovered);

        }
    }
}
