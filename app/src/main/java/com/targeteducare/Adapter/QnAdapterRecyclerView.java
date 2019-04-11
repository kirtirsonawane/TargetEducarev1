package com.targeteducare.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.targeteducare.Classes.QnaDataModel;
import com.targeteducare.R;

import java.util.ArrayList;

public class QnAdapterRecyclerView extends RecyclerView.Adapter<QnAdapterRecyclerView.MyViewHolder>{
    private ArrayList<QnaDataModel> dataSet;
    Context context;

    public QnAdapterRecyclerView(Context context, ArrayList<QnaDataModel> data) {
        this.dataSet = data;
        this.context=context;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.follower_details, parent, false);

        //view.setOnClickListener(EngineeringColleges.myOnClickListener);

        MyViewHolder vh = new MyViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull QnAdapterRecyclerView.MyViewHolder holder, int i) {
        holder.profile_pic.setImageResource(dataSet.get(i).getProfile_pic());
        holder.name.setText(dataSet.get(i).getName());
        holder.xminsago.setText(dataSet.get(i).getMinutes_ago()+" minutes ago");
        holder.paragraphs.setText(dataSet.get(i).getParagraph());
    }


    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView profile_pic;
        TextView name;
        TextView xminsago;
        TextView paragraphs;

        public MyViewHolder(View itemView) {
            super(itemView);
            profile_pic = itemView.findViewById(R.id.profilepic);
            name = itemView.findViewById(R.id.personname);
            xminsago = itemView.findViewById(R.id.xminsago);
            paragraphs = itemView.findViewById(R.id.paragraph);
        }
    }

}
