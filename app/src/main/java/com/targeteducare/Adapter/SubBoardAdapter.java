package com.targeteducare.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.targeteducare.Classes.SubBoardDataModel;
import com.targeteducare.R;

import java.util.ArrayList;

public class SubBoardAdapter extends RecyclerView.Adapter<SubBoardAdapter.MyViewHolder>{

    Context context;
    ArrayList<SubBoardDataModel> subBoardDataModels;

    public SubBoardAdapter(Context context, ArrayList<SubBoardDataModel> subBoardDataModels){
        this.context = context;
        this.subBoardDataModels = subBoardDataModels;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.sub_board_select,parent,false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tv_mainboard.setText(subBoardDataModels.get(position).getSubboard());
    }

    @Override
    public int getItemCount() {
        return subBoardDataModels.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tv_mainboard;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_mainboard = itemView.findViewById(R.id.textviewsubselectboard);
        }
    }
}
