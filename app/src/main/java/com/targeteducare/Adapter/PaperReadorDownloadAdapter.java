package com.targeteducare.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.targeteducare.Classes.PaperModel;
import com.targeteducare.R;

import java.util.ArrayList;

public class PaperReadorDownloadAdapter extends RecyclerView.Adapter<PaperReadorDownloadAdapter.MyViewHolder> {
    Context context;
    private ArrayList<PaperModel> dataSet;

    public PaperReadorDownloadAdapter(Context context, ArrayList<PaperModel> data) {
        this.dataSet = data;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // infalte the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.samplepaper_layout, parent, false);
        // set the view's size, margins, paddings and layout parameters
        MyViewHolder vh = new MyViewHolder(v); // pass the view to View Holder
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull PaperReadorDownloadAdapter.MyViewHolder holder, final int position) {
        // set the data in items
        holder.readordownload.setText(dataSet.get(position).getDownloadorread());
        holder.papername.setImageResource(dataSet.get(position).getPapername());
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        // init the item view's
        TextView readordownload;
        ImageView papername;
        public MyViewHolder(View itemView) {
            super(itemView);
            // get the reference of item view's
            papername = itemView.findViewById(R.id.papers);
            readordownload = itemView.findViewById(R.id.downloadpaper);
        }
    }

}
