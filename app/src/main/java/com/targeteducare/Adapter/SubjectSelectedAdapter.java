package com.targeteducare.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.targeteducare.Classes.SubjectSelectedDataModel;
import com.targeteducare.GlobalValues;
import com.targeteducare.R;

import java.util.ArrayList;

public class SubjectSelectedAdapter extends RecyclerView.Adapter<SubjectSelectedAdapter.MyViewHolder> {

    Context context;
    ArrayList<SubjectSelectedDataModel> subjectSelectedDataModels;

    public SubjectSelectedAdapter(Context context, ArrayList<SubjectSelectedDataModel> subjectSelectedDataModels){
        this.context = context;
        this.subjectSelectedDataModels = subjectSelectedDataModels;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.subject_select,parent,false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.student_name.setText("Hey "+ GlobalValues.studentProfile.getName());
    }

    @Override
    public int getItemCount() {
        return subjectSelectedDataModels.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView student_name, random_text;
        ImageView iv_topicicon;

        public MyViewHolder(View itemView) {
            super(itemView);

            student_name = itemView.findViewById(R.id.studentname);
            random_text = itemView.findViewById(R.id.somerandomtext);
            iv_topicicon = itemView.findViewById(R.id.iconimage);
        }
    }
}
