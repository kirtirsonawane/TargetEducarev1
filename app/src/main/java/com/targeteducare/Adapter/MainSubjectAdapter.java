package com.targeteducare.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.targeteducare.Classes.MainSubjectDataModel;
import com.targeteducare.MainSubjectSelectionActivity;
import com.targeteducare.R;

import java.util.ArrayList;

public class MainSubjectAdapter extends RecyclerView.Adapter<MainSubjectAdapter.MyViewHolder> {

    ArrayList<MainSubjectDataModel> mainSubjectDataModels;
    Context context;

    public MainSubjectAdapter(Context context, ArrayList<MainSubjectDataModel> mainSubjectDataModels){
        this.context = context;
        this.mainSubjectDataModels = mainSubjectDataModels;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View vh = LayoutInflater.from(parent.getContext()).inflate(R.layout.subject_list,parent,false);
        MyViewHolder viewHolder = new MyViewHolder(vh);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.tv_mainsubjectselect.setText(mainSubjectDataModels.get(position).getMainsubject());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainSubjectSelectionActivity)context).referto(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mainSubjectDataModels.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tv_mainsubjectselect;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_mainsubjectselect = itemView.findViewById(R.id.tv_mainsubjectselect);
        }
    }
}
